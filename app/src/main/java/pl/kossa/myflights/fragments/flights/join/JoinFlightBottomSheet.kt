package pl.kossa.myflights.fragments.flights.join

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import pl.kossa.myflights.R
import pl.kossa.myflights.api.responses.sharedflights.SharedFlightJoinDetails
import pl.kossa.myflights.architecture.dialogs.BaseBottomSheet
import pl.kossa.myflights.databinding.DialogJoinFlightBinding
import pl.kossa.myflights.exstensions.toUTCDateTimeString

@AndroidEntryPoint
class JoinFlightBottomSheet : BaseBottomSheet<JoinFlightViewModel, DialogJoinFlightBinding>() {

    override val viewModel: JoinFlightViewModel by viewModels()

    override fun setOnClickListeners() {
        super.setOnClickListeners()
        binding.ownerEtw.isMyFlight = false
        binding.cancelButton.setOnClickListener {
            viewModel.navigateBack()
        }
        binding.confirmButton.setOnClickListener {
            viewModel.joinSharedFlight()
        }
    }

    override fun collectFlow() {
        super.collectFlow()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isLoadingData.collectLatest {
                binding.progressBar.isVisible = it
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.sharedFlightJoinDetails.collectLatest {
                it?.let { setupSharedFlightData(it) }
            }
        }
    }

    private fun setupSharedFlightData(sharedFlightJoinDetails: SharedFlightJoinDetails) {
        binding.ownerEtw.profileUrl = sharedFlightJoinDetails.ownerData.avatar?.thumbnailUrl
        binding.ownerEtw.ownerNick = sharedFlightJoinDetails.ownerData.nick
        binding.ownerEtw.ownerEmail = sharedFlightJoinDetails.ownerData.email

        binding.routeEwtv.valueText = getString(
            R.string.route_format,
            sharedFlightJoinDetails.flight.departureAirport.icaoCode,
            sharedFlightJoinDetails.flight.arrivalAirport.icaoCode
        )
        binding.departureTimeEwtv.valueText =
            sharedFlightJoinDetails.flight.departureDate.toUTCDateTimeString()
    }
}