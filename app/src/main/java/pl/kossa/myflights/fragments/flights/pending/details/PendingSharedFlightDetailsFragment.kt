package pl.kossa.myflights.fragments.flights.pending.details

import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.Flight
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.api.responses.sharedflights.SharedUserData
import pl.kossa.myflights.architecture.fragments.BaseFragment
import pl.kossa.myflights.databinding.FragmentPendingSharedFlightDetailsBinding
import pl.kossa.myflights.exstensions.toDateString
import pl.kossa.myflights.exstensions.toTimeString

@AndroidEntryPoint
class PendingSharedFlightDetailsFragment :
    BaseFragment<PendingSharedFlightDetailsViewModel, FragmentPendingSharedFlightDetailsBinding>() {

    override val viewModel: PendingSharedFlightDetailsViewModel by viewModels()

    override fun setOnClickListeners() {
        binding.backAppbar.setBackOnClickListener {
            viewModel.navigateBack()
        }
        binding.backAppbar.setDeleteOnClickListener {
            viewModel.deleteSharedFlight()
        }
        binding.declineButton.setOnClickListener {
            viewModel.deleteSharedFlight()
        }
        binding.confirmButton.setOnClickListener {
            viewModel.confirmSharedFlight()
        }
        binding.sharedFlightSwipeRefresh.setOnRefreshListener {
            viewModel.fetchSharedFlight()
        }
    }

    override fun collectFlow() {
        super.collectFlow()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isLoadingData.collectLatest {
                binding.sharedFlightSwipeRefresh.isRefreshing = it
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.sharedFlight.collectLatest {
                it?.let {
                    setupFlightData(it.flight)
                    setupSharedUserData(it.sharedUserData)
                }
            }
        }
    }

    override fun handleApiError(apiError: ApiError) {
        TODO("Not yet implemented")
    }

    private fun setupSharedUserData(sharedUserData: SharedUserData?) {
        binding.nickEwtv.isVisible = !sharedUserData?.nick.isNullOrBlank()
        binding.nickEwtv.valueText = sharedUserData?.nick ?: ""

        binding.emailEwtv.isVisible = sharedUserData?.email != null
        binding.emailEwtv.valueText = sharedUserData?.email ?: ""

        binding.confirmButton.isVisible = sharedUserData != null
        binding.declineButton.isVisible = sharedUserData != null
        binding.backAppbar.isDeleteIconVisible = sharedUserData == null
        binding.deleteButton.isVisible = sharedUserData == null

        sharedUserData?.avatar?.let {
            Glide.with(requireContext())
                .load(it.url)
                .transform(CircleCrop())
                .placeholder(R.drawable.ic_flight)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(binding.flightIv)
        } ?: run {
            binding.flightIv.setImageResource(R.drawable.ic_flight)
            binding.flightIv.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black_day_night
                )
            )
        }
    }

    private fun setupFlightData(flight: Flight) {
        binding.airplaneEwtv.valueText = flight.airplane.name
        binding.departureAirportEwtv.valueText =
            "${flight.departureAirport.name} (${flight.departureAirport.icaoCode})" // TODO format
        binding.departureRunwayEwtv.valueText = flight.departureRunway.name

        binding.arrivalAirportEwtv.valueText =
            "${flight.arrivalAirport.name} (${flight.arrivalAirport.icaoCode})" // TODO format
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
}