package pl.kossa.myflights.fragments.flights.details

import android.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.Flight
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.api.responses.HttpCode
import pl.kossa.myflights.api.responses.flights.FlightResponse
import pl.kossa.myflights.architecture.fragments.BaseFragment
import pl.kossa.myflights.databinding.FragmentFlightDetailsBinding
import pl.kossa.myflights.exstensions.toDateString
import pl.kossa.myflights.exstensions.toTimeString

@AndroidEntryPoint
class FlightDetailsFragment : BaseFragment<FlightDetailsViewModel, FragmentFlightDetailsBinding>() {

    override val viewModel: FlightDetailsViewModel by viewModels()


    override fun setOnClickListeners() {
        binding.shareAppbar.setBackOnClickListener {
            viewModel.navigateBack()
        }
        binding.shareAppbar.setDeleteOnClickListener {
            showDeleteDialog()
        }
        binding.shareAppbar.setShareOnClickListener {
            viewModel.navigateToFlightShareDialog()
        }
        binding.shareAppbar.setResignOnClickListener {
            showResignDialog()
        }
        binding.editButton.setOnClickListener {
            viewModel.navigateToFlightEdit()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchFlight()
    }

    override fun collectFlow() {
        super.collectFlow()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.flightResponse.collect {
                it?.let {
                    setupFlightData(it.flight)
                    //TODO setup sharing data
                    setupAppBar(it)
                }
            }
        }
    }

    private fun setupFlightData(flight: Flight) {
        binding.airplaneEwtv.valueText = flight.airplane.name
        binding.departureAirportEwtv.valueText =
            "${flight.departureAirport.name} (${flight.departureAirport.icaoCode})"
        binding.departureRunwayEwtv.valueText = flight.departureRunway.name

        binding.arrivalAirportEwtv.valueText =
            "${flight.arrivalAirport.name} (${flight.arrivalAirport.icaoCode})"
        binding.arrivalRunwayEwtv.valueText = flight.arrivalRunway.name

        binding.departureTimeEwtv.valueText =
            "${flight.departureDate.toDateString()} ${flight.departureDate.toTimeString()}"
        binding.arrivalTimeEwtv.valueText =
            "${flight.arrivalDate.toDateString()} ${flight.arrivalDate.toTimeString()}"

        binding.distanceEwtv.valueText = flight.distance?.toString() ?: ""
        binding.noteTagTv.isVisible = !flight.note.isNullOrBlank()
        binding.noteTv.isVisible = !flight.note.isNullOrBlank()
        binding.noteTv.text = flight.note ?: ""
    }

    private fun setupAppBar(flightResponse: FlightResponse) {
        val isMyFlight = viewModel.isMyFlight(flightResponse.ownerData.userId)
        binding.shareAppbar.isDeleteIconVisible = isMyFlight
        binding.shareAppbar.isShareVisible = isMyFlight
        binding.shareAppbar.isResignVisible = !isMyFlight
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


    private fun showResignDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(R.string.flight_resign_question_info)
        builder.setPositiveButton(
            R.string.resign
        ) { dialog, _ ->
            viewModel.resignFromSharedFlight()
            dialog?.dismiss()
        }
        builder.setNegativeButton(R.string.cancel) { dialog, _ ->
            dialog?.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun handleApiError(apiError: ApiError) {
        when (apiError.code) {
            HttpCode.NOT_FOUND.code -> {
                viewModel.setToastMessage(R.string.error_flight_not_found)
            }
            HttpCode.INTERNAL_SERVER_ERROR.code -> {
                viewModel.setToastMessage(R.string.unexpected_error)
            }
            HttpCode.FORBIDDEN.code -> {
                viewModel.setToastMessage(R.string.error_forbidden)
            }
            else -> {
                viewModel.setToastMessage(R.string.unexpected_error)
            }
        }
    }

}