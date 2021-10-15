package pl.kossa.myflights.fragments.flights.details

import android.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.Flight
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentFlightDetailsBinding
import pl.kossa.myflights.exstensions.toDateString
import pl.kossa.myflights.exstensions.toTimeString

@AndroidEntryPoint
class FlightDetailsFragment : BaseFragment<FlightDetailsViewModel, FragmentFlightDetailsBinding>() {

    override val viewModel: FlightDetailsViewModel by viewModels()


    override fun setOnClickListeners() {
        binding.backAppbar.setBackOnClickListener {
            viewModel.navigateBack()
        }
        binding.backAppbar.setDeleteOnClickListener {
            showDeleteDialog()
        }
        binding.editButton.setOnClickListener { 
            viewModel.navigateToFlightEdit()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchFlight()
    }

    override fun setObservers() {
        super.setObservers()
        collectFlow()
    }

    private fun collectFlow() {
        lifecycleScope.launch {
            viewModel.flight.collect {
                it?.let{ setupFlightData(it)}
            }
        }
    }

    private fun setupFlightData(flight: Flight) {
        binding.airplaneEwtv.valueText = flight.airplane.name
        binding.departureAirportEwtv.valueText = "${flight.departureAirport.name} (${flight.departureAirport.icaoCode})"
        binding.departureRunwayEwtv.valueText = flight.departureRunway.name

        binding.arrivalAirportEwtv.valueText = "${flight.arrivalAirport.name} (${flight.arrivalAirport.icaoCode})"
        binding.arrivalRunwayEwtv.valueText = flight.arrivalRunway.name

        binding.departureTimeEwtv.valueText = "${flight.departureDate.toDateString()} ${flight.departureDate.toTimeString()}"
        binding.arrivalTimeEwtv.valueText = "${flight.arrivalDate.toDateString()} ${flight.arrivalDate.toTimeString()}"

        binding.distanceEwtv.valueText = flight.distance?.toString() ?: ""
        binding.noteTagTv.isVisible = !flight.note.isNullOrBlank()
        binding.noteTv.isVisible = !flight.note.isNullOrBlank()
        binding.noteTv.text = flight.note ?: ""
    }

    private fun showDeleteDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(R.string.flight_delete_question_info)
        builder.setPositiveButton(
            R.string.delete
        ) { dialog, _ ->
            viewModel.deleteFlight()
            dialog?.dismiss()
        }
        builder.setNegativeButton(R.string.cancel) { dialog, _ ->
            dialog?.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

}