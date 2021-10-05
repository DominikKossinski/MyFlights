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
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentFlightAddBinding
import pl.kossa.myflights.fragments.flights.select.airplane.AirplaneSelectFragment
import pl.kossa.myflights.fragments.flights.select.airport.AirportSelectFragment
import pl.kossa.myflights.fragments.flights.select.runway.RunwaySelectFragment

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
    }
}