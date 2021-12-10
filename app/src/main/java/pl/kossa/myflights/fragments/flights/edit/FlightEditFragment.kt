package pl.kossa.myflights.fragments.flights.edit

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import pl.kossa.myflights.R
import pl.kossa.myflights.api.server.models.Flight
import pl.kossa.myflights.api.server.responses.ApiError
import pl.kossa.myflights.api.server.responses.HttpCode
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentFlightEditBinding
import pl.kossa.myflights.exstensions.*
import pl.kossa.myflights.fragments.flights.select.airplane.AirplaneSelectFragment
import pl.kossa.myflights.fragments.flights.select.airport.AirportSelectFragment
import pl.kossa.myflights.fragments.flights.select.runway.RunwaySelectFragment
import pl.kossa.myflights.views.pickers.DateTimePicker
import java.util.*

@AndroidEntryPoint
class FlightEditFragment : BaseFragment<FlightEditViewModel, FragmentFlightEditBinding>() {

    override val viewModel: FlightEditViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentResultListeners()
    }

    override fun setOnClickListeners() {
        binding.airplaneSelectView.setOnElementSelectListener {
            viewModel.navigateToAirplaneSelect()
        }

        binding.departureAirportSelectView.setOnElementSelectListener {
            viewModel.navigateToAirportSelect(AirportSelectFragment.DEPARTURE_AIRPORT_KEY)
        }
        binding.departureRunwaySelectView.setOnElementSelectListener {
            viewModel.navigateToRunwaySelect(RunwaySelectFragment.DEPARTURE_RUNWAY_KEY)
        }

        binding.arrivalAirportSelectView.setOnElementSelectListener {
            viewModel.navigateToAirportSelect(AirportSelectFragment.ARRIVAL_AIRPORT_KEY)
        }
        binding.arrivalRunwaySelectView.setOnElementSelectListener {
            viewModel.navigateToRunwaySelect(RunwaySelectFragment.ARRIVAL_RUNWAY_KEY)
        }

        binding.departureDts.setOnSelectClickListener {
            val currentDate = viewModel._departureDate.value?.let {
                val year = it.extractYear()
                val month = it.extractMonth0()
                val day = it.extractDayOfMonth()
                Triple(year, month, day)
            }
            val currentTime = viewModel._departureDate.value?.let {
                val hour = it.extractHour()
                val minute = it.extractMinute()
                Pair(hour, minute)
            }
            DateTimePicker(currentDate, currentTime) { year, month, day, hourOfDay, minute ->
                val dateStr = "%02d.%02d.%04d %02d:%02d".format(day, month, year, hourOfDay, minute)
                val date = dateStr.toDateTime()
                viewModel._departureDate.value =
                    verifyDepartureDate(date, viewModel._arrivalDate.value)
            }.show(parentFragmentManager)
        }

        binding.arrivalDts.setOnSelectClickListener {
            val currentDate = viewModel._arrivalDate.value?.let {
                val year = it.extractYear()
                val month = it.extractMonth0()
                val day = it.extractDayOfMonth()
                Triple(year, month, day)
            }
            val currentTime = viewModel._arrivalDate.value?.let {
                val hour = it.extractHour()
                val minute = it.extractMinute()
                Pair(hour, minute)
            }
            DateTimePicker(currentDate, currentTime) { year, month, day, hourOfDay, minute ->
                val dateStr = "%02d.%02d.%04d %02d:%02d".format(day, month, year, hourOfDay, minute)
                val date = dateStr.toDateTime()
                viewModel._arrivalDate.value =
                    verifyArrivalDate(date, viewModel._departureDate.value)
            }.show(parentFragmentManager)
        }

        binding.noteTie.doOnTextChanged {
            viewModel.setNote(it)
        }

        binding.distanceTie.doOnTextChanged {
            it.toIntOrNull().let { distance -> viewModel.setDistance(distance) }
        }

        binding.saveAppBar.setBackOnClickListener {
            viewModel.navigateBack()
        }
        binding.saveAppBar.setSaveOnClickListener {
            viewModel.putFlight()
        }
        binding.saveButton.setOnClickListener {
            viewModel.putFlight()
        }
    }

    private fun setFragmentResultListeners() {
        parentFragmentManager.setFragmentResultListener(
            AirplaneSelectFragment.AIRPLANE_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val airplaneId = bundle.getString(AirplaneSelectFragment.AIRPLANE_ID_KEY, "")
            val name = bundle.getString(AirplaneSelectFragment.AIRPLANE_NAME_KEY, "")
            viewModel.setAirplaneId(airplaneId)
            viewModel._airplaneName.value = name
        }

        parentFragmentManager.setFragmentResultListener(
            AirportSelectFragment.DEPARTURE_AIRPORT_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val airportId = bundle.getString(AirportSelectFragment.AIRPORT_ID_KEY, "")
            val airportName = bundle.getString(AirportSelectFragment.AIRPORT_NAME_KEY, "")
            val icaoCode = bundle.getString(AirportSelectFragment.AIRPORT_ICAO_CODE_KEY, "")
            viewModel.setDepartureAirportId(airportId)
            viewModel._departureAirportName.value = "$airportName ($icaoCode)"

            binding.departureRunwaySelectView.isVisible = airportId.isNotBlank()
            viewModel.setDepartureRunwayId("")
            viewModel._departureRunwayName.value = ""
        }

        parentFragmentManager.setFragmentResultListener(
            RunwaySelectFragment.DEPARTURE_RUNWAY_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val runwayId = bundle.getString(RunwaySelectFragment.RUNWAY_ID_KEY, "")
            val runwayName = bundle.getString(RunwaySelectFragment.RUNWAY_NAME_KEY, "")
            viewModel.setDepartureRunwayId(runwayId)
            viewModel._departureRunwayName.value = runwayName
        }

        parentFragmentManager.setFragmentResultListener(
            AirportSelectFragment.ARRIVAL_AIRPORT_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val airportId = bundle.getString(AirportSelectFragment.AIRPORT_ID_KEY, "")
            val airportName = bundle.getString(AirportSelectFragment.AIRPORT_NAME_KEY, "")
            val icaoCode = bundle.getString(AirportSelectFragment.AIRPORT_ICAO_CODE_KEY, "")
            viewModel.setArrivalAirportId(airportId)
            viewModel._arrivalAirportName.value = "$airportName ($icaoCode)"

            binding.arrivalRunwaySelectView.isVisible = airportId.isNotBlank()
            viewModel.setArrivalRunwayId("")
            viewModel._arrivalRunwayName.value = ""
        }

        parentFragmentManager.setFragmentResultListener(
            RunwaySelectFragment.ARRIVAL_RUNWAY_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val runwayId = bundle.getString(RunwaySelectFragment.RUNWAY_ID_KEY, "")
            val runwayName = bundle.getString(RunwaySelectFragment.RUNWAY_NAME_KEY, "")
            viewModel.setArrivalRunwayId(runwayId)
            viewModel._arrivalRunwayName.value = runwayName
        }

    }

    override fun collectFlow() {
        super.collectFlow()
       viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.flight.collect {
                setupFlightData(it)
            }
        }

       viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel._airplaneName.collect {
                binding.airplaneSelectView.elementName = it
            }
        }
       viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel._departureAirportName.collect {
                binding.departureAirportSelectView.elementName = it
                binding.departureRunwaySelectView.isVisible = it.isNotBlank()
            }
        }
       viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel._departureRunwayName.collect {
                binding.departureRunwaySelectView.elementName = it
            }
        }
       viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel._arrivalAirportName.collect {
                binding.arrivalAirportSelectView.elementName = it
                binding.arrivalRunwaySelectView.isVisible = it.isNotBlank()
            }
        }
       viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel._arrivalRunwayName.collect {
                binding.arrivalRunwaySelectView.elementName = it
            }
        }
       viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel._departureDate.collect {
                binding.departureDts.date = it
            }
        }
       viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel._arrivalDate.collect {
                binding.arrivalDts.date = it
            }
        }
       viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isAddButtonEnabled.collect {
                binding.saveAppBar.isSaveIconEnabled = it
                binding.saveButton.isEnabled = it
            }
        }
    }

    private fun setupFlightData(flight: Flight) {
        viewModel.setAirplaneId(flight.airplane.airplaneId)
        viewModel._airplaneName.value = flight.airplane.name
        
        viewModel.setDepartureAirportId(flight.departureAirport.airportId)
        viewModel._departureAirportName.value = "${flight.departureAirport.name} (${flight.departureAirport.icaoCode})"
        
        viewModel.setDepartureRunwayId(flight.departureRunway.runwayId)
        viewModel._departureRunwayName.value = flight.departureRunway.name

        viewModel.setArrivalAirportId(flight.arrivalAirport.airportId)
        viewModel._arrivalAirportName.value = "${flight.arrivalAirport.name} (${flight.arrivalAirport.icaoCode})"

        viewModel.setArrivalRunwayId(flight.arrivalRunway.runwayId)
        viewModel._arrivalRunwayName.value = flight.arrivalRunway.name

        viewModel._departureDate.value = flight.departureDate
        viewModel._arrivalDate.value = flight.arrivalDate

        binding.distanceTie.setText(flight.distance?.toString())
        binding.noteTie.setText(flight.note)
    }

    private fun verifyDepartureDate(departure: Date?, arrival: Date?): Date? {
        return departure?.let {
            when {
                arrival != null && departure.time > arrival.time -> {
                    viewModel.setToastMessage( R.string.error_departure_after_arrival)
                    arrival
                }
                departure.time > Date().time -> {
                    viewModel.setToastMessage( R.string.error_departure_in_future)
                    Date()
                }
                else -> {
                    departure
                }
            }
        }
    }

    private fun verifyArrivalDate(arrival: Date?, departure: Date?): Date? {
        return arrival?.let {
            when {
                departure != null && arrival.time < departure.time -> {
                    viewModel.setToastMessage( R.string.error_arrival_before_departure)
                    departure
                }
                arrival.time > Date().time -> {
                    viewModel.setToastMessage( R.string.error_arrival_in_future)
                    Date()
                }
                else -> {
                    arrival
                }
            }
        }
    }

    override fun handleApiError(apiError: ApiError) {
        when(apiError.code) {
            HttpCode.NOT_FOUND.code -> {
                viewModel.setToastMessage( R.string.error_flight_not_found)
            }
            HttpCode.INTERNAL_SERVER_ERROR.code -> {
                viewModel.setToastMessage( R.string.unexpected_error)
            }
            HttpCode.FORBIDDEN.code -> {
                viewModel.setToastMessage( R.string.error_forbidden)
            }
            else -> {
                viewModel.setToastMessage( R.string.unexpected_error)
            }
        }
    }

}