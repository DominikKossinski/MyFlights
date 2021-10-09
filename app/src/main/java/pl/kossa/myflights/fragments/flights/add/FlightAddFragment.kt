package pl.kossa.myflights.fragments.flights.add

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentFlightAddBinding
import pl.kossa.myflights.exstensions.*
import pl.kossa.myflights.fragments.flights.select.airplane.AirplaneSelectFragment
import pl.kossa.myflights.fragments.flights.select.airport.AirportSelectFragment
import pl.kossa.myflights.fragments.flights.select.runway.RunwaySelectFragment
import pl.kossa.myflights.views.pickers.DatePicker
import pl.kossa.myflights.views.pickers.DateTimePicker
import pl.kossa.myflights.views.pickers.TimePicker
import java.time.LocalTime
import java.util.*

@AndroidEntryPoint
class FlightAddFragment : BaseFragment<FlightAddViewModel, FragmentFlightAddBinding>() {

    override val viewModel: FlightAddViewModel by viewModels()

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
            val currentDate = viewModel.departureDate.value?.let {
                val year = it.extractYear()
                val month = it.extractMonth0()
                val day = it.extractDayOfMonth()
                Triple(year, month, day)
            }
            val currentTime = viewModel.departureDate.value?.let {
                val hour = it.extractHour()
                val minute = it.extractMinute()
                Pair(hour, minute)
            }
            DateTimePicker(currentDate, currentTime) { year, month, day, hourOfDay, minute ->
                val dateStr = "%02d.%02d.%04d %02d:%02d".format(day, month, year, hourOfDay, minute)
                val date = dateStr.toDateTime()
                viewModel.departureDate.value =
                    verifyDepartureDate(date, viewModel.arrivalDate.value)
            }.show(parentFragmentManager)
        }
        binding.departureDts.setOnDateClickListener {
            val currentDate = viewModel.departureDate.value?.let {
                val year = it.extractYear()
                val month = it.extractMonth0()
                val day = it.extractDayOfMonth()
                Triple(year, month, day)
            }
            val hourAndMinute = viewModel.departureDate.value?.toTimeString()
            DatePicker(currentDate) { year, month, day ->
                val dateStr = "%04d.%02d.%02d ".format(day, month, year) + hourAndMinute
                val date = dateStr.toDateTime()
                viewModel.departureDate.value =
                    verifyDepartureDate(date, viewModel.arrivalDate.value)
            }.show(parentFragmentManager, DatePicker.TAG)
        }
        binding.departureDts.setOnTimeClickListener {
            val currentTime = viewModel.departureDate.value?.let {
                val hour = it.extractHour()
                val minute = it.extractMinute()
                Pair(hour, minute)
            }
            val day = viewModel.departureDate.value?.toDateString()
            TimePicker(currentTime) { hourOfDay, minute ->
                val dateStr = day + " %02d:%02d".format(hourOfDay, minute)
                val date = dateStr.toDateTime()
                viewModel.departureDate.value =
                    verifyDepartureDate(date, viewModel.arrivalDate.value)
            }.show(parentFragmentManager, TimePicker.TAG)
        }


        binding.arrivalDts.setOnSelectClickListener {
            val currentDate = viewModel.arrivalDate.value?.let {
                val year = it.extractYear()
                val month = it.extractMonth0()
                val day = it.extractDayOfMonth()
                Triple(year, month, day)
            }
            val currentTime = viewModel.arrivalDate.value?.let {
                val hour = it.extractHour()
                val minute = it.extractMinute()
                Pair(hour, minute)
            }
            DateTimePicker(currentDate, currentTime) { year, month, day, hourOfDay, minute ->
                val dateStr = "%02d.%02d.%04d %02d:%02d".format(day, month, year, hourOfDay, minute)
                val date = dateStr.toDateTime()
                viewModel.arrivalDate.value =
                    verifyArrivalDate(date, viewModel.departureDate.value)
            }.show(parentFragmentManager)
        }
        binding.arrivalDts.setOnDateClickListener {
            val currentDate = viewModel.arrivalDate.value?.let {
                val year = it.extractYear()
                val month = it.extractMonth0()
                val day = it.extractDayOfMonth()
                Triple(year, month, day)
            }
            val hourAndMinute = viewModel.arrivalDate.value?.toTimeString()
            DatePicker(currentDate) { year, month, day ->
                val dateStr = "%04d.%02d.%02d ".format(day, month, year) + hourAndMinute
                val date = dateStr.toDateTime()
                viewModel.arrivalDate.value =
                    verifyArrivalDate(date, viewModel.departureDate.value)
            }.show(parentFragmentManager, DatePicker.TAG)
        }
        binding.arrivalDts.setOnTimeClickListener {
            val currentTime = viewModel.arrivalDate.value?.let {
                val hour = it.extractHour()
                val minute = it.extractMinute()
                Pair(hour, minute)
            }
            val day = viewModel.arrivalDate.value?.toDateString()
            TimePicker(currentTime) { hourOfDay, minute ->
                val dateStr = day + " %02d:%02d".format(hourOfDay, minute)
                val date = dateStr.toDateTime()
                viewModel.arrivalDate.value =
                    verifyArrivalDate(date, viewModel.departureDate.value)
            }.show(parentFragmentManager, TimePicker.TAG)
        }
    }

    override fun setObservers() {
        super.setObservers()
        collectFlow()
    }

    private fun setFragmentResultListeners() {
        parentFragmentManager.setFragmentResultListener(
            AirplaneSelectFragment.AIRPLANE_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val airplaneId = bundle.getString(AirplaneSelectFragment.AIRPLANE_ID_KEY, "")
            val name = bundle.getString(AirplaneSelectFragment.AIRPLANE_NAME_KEY, "")
            viewModel.airplaneId.value = airplaneId
            viewModel.airplaneName.value = name
        }

        parentFragmentManager.setFragmentResultListener(
            AirportSelectFragment.DEPARTURE_AIRPORT_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val airportId = bundle.getString(AirportSelectFragment.AIRPORT_ID_KEY, "")
            val airportName = bundle.getString(AirportSelectFragment.AIRPORT_NAME_KEY, "")
            val icaoCode = bundle.getString(AirportSelectFragment.AIRPORT_ICAO_CODE_KEY, "")
            viewModel.departureAirportId.value = airportId
            viewModel.departureAirportName.value = "$airportName ($icaoCode)"

            binding.departureRunwaySelectView.isVisible = airportId.isNotBlank()
            viewModel.departureRunwayId.value = ""
            viewModel.departureRunwayName.value = ""
        }

        parentFragmentManager.setFragmentResultListener(
            RunwaySelectFragment.DEPARTURE_RUNWAY_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val runwayId = bundle.getString(RunwaySelectFragment.RUNWAY_ID_KEY, "")
            val runwayName = bundle.getString(RunwaySelectFragment.RUNWAY_NAME_KEY, "")
            viewModel.departureRunwayId.value = runwayId
            viewModel.departureRunwayName.value = runwayName
        }

        parentFragmentManager.setFragmentResultListener(
            AirportSelectFragment.ARRIVAL_AIRPORT_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val airportId = bundle.getString(AirportSelectFragment.AIRPORT_ID_KEY, "")
            val airportName = bundle.getString(AirportSelectFragment.AIRPORT_NAME_KEY, "")
            val icaoCode = bundle.getString(AirportSelectFragment.AIRPORT_ICAO_CODE_KEY, "")
            viewModel.arrivalAirportId.value = airportId
            viewModel.arrivalAirportName.value = "$airportName ($icaoCode)"

            binding.arrivalRunwaySelectView.isVisible = airportId.isNotBlank()
            viewModel.arrivalRunwayId.value = ""
            viewModel.arrivalRunwayName.value = ""
        }

        parentFragmentManager.setFragmentResultListener(
            RunwaySelectFragment.ARRIVAL_RUNWAY_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val runwayId = bundle.getString(RunwaySelectFragment.RUNWAY_ID_KEY, "")
            val runwayName = bundle.getString(RunwaySelectFragment.RUNWAY_NAME_KEY, "")
            viewModel.arrivalRunwayId.value = runwayId
            viewModel.arrivalRunwayName.value = runwayName
        }

    }

    private fun collectFlow() {
        lifecycleScope.launch {
            viewModel.airplaneName.collect {
                binding.airplaneSelectView.elementName = it
            }
        }
        lifecycleScope.launch {
            viewModel.departureAirportName.collect {
                binding.departureAirportSelectView.elementName = it
                binding.departureRunwaySelectView.isVisible = it.isNotBlank()
            }
        }
        lifecycleScope.launch {
            viewModel.departureRunwayName.collect {
                binding.departureRunwaySelectView.elementName = it
            }
        }
        lifecycleScope.launch {
            viewModel.arrivalAirportName.collect {
                binding.arrivalAirportSelectView.elementName = it
                binding.arrivalRunwaySelectView.isVisible = it.isNotBlank()
            }
        }
        lifecycleScope.launch {
            viewModel.arrivalRunwayName.collect {
                binding.arrivalRunwaySelectView.elementName = it
            }
        }
        lifecycleScope.launch {
            viewModel.departureDate.collect {
                binding.departureDts.date = it
            }
        }
        lifecycleScope.launch {
            viewModel.arrivalDate.collect {
                binding.arrivalDts.date = it
            }
        }
    }

    private fun verifyDepartureDate(departure: Date?, arrival: Date?): Date? {
        return departure?.let {
            when {
                arrival != null && departure.time > arrival.time -> {
                    viewModel.toastError.value = R.string.error_departure_after_arrival
                    arrival
                }
                departure.time > Date().time -> {
                    viewModel.toastError.value = R.string.error_departure_in_future
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
                    viewModel.toastError.value = R.string.error_arrival_before_departure
                    departure
                }
                arrival.time > Date().time -> {
                    viewModel.toastError.value = R.string.error_arrival_in_future
                    Date()
                }
                else -> {
                    arrival
                }
            }
        }
    }
}