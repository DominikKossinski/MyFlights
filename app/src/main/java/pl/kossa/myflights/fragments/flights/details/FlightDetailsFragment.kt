package pl.kossa.myflights.fragments.flights.details

import android.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.R
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.api.responses.HttpCode
import pl.kossa.myflights.architecture.fragments.BaseFragment
import pl.kossa.myflights.databinding.FragmentFlightDetailsBinding
import pl.kossa.myflights.exstensions.toDateString
import pl.kossa.myflights.exstensions.toTimeString
import pl.kossa.myflights.room.entities.Flight
import pl.kossa.myflights.room.entities.ShareData

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
        binding.seeAllTv.setOnClickListener {
            viewModel.navigateToSharedUsers()
        }
        binding.sharedFlightSwipeRefresh.setOnRefreshListener {
            viewModel.fetchFlight()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchFlight()
    }

    override fun collectFlow() {
        super.collectFlow()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.flight.collect {
                it?.let {
                    val isMyFlight = viewModel.isMyFlight(it.ownerData.ownerData.ownerId)
                    setupFlightData(it)
                    setupAppBar(it, isMyFlight)
                    setupSharedUsersData(it.sharedUsers, isMyFlight)
                }
            }
        }
    }

    private fun setupFlightData(flight: Flight) {
        binding.airplaneEwtv.valueText = flight.airplane.airplane.name
        binding.departureAirportEwtv.valueText =
            "${flight.departureAirport.airport.name} (${flight.departureAirport.airport.icaoCode})"
        binding.departureRunwayEwtv.valueText = flight.departureRunway.runway.name

        binding.arrivalAirportEwtv.valueText =
            "${flight.arrivalAirport.airport.name} (${flight.arrivalAirport.airport.icaoCode})"
        binding.arrivalRunwayEwtv.valueText = flight.arrivalRunway.runway.name

        binding.departureTimeEwtv.valueText =
            "${flight.flight.departureDate.toDateString()} ${flight.flight.departureDate.toTimeString()}"
        binding.arrivalTimeEwtv.valueText =
            "${flight.flight.arrivalDate.toDateString()} ${flight.flight.arrivalDate.toTimeString()}"

        binding.distanceEwtv.valueText = flight.flight.distance?.toString() ?: ""
        binding.noteTagTv.isVisible = !flight.flight.note.isNullOrBlank()
        binding.noteTv.isVisible = !flight.flight.note.isNullOrBlank()
        binding.noteTv.text = flight.flight.note ?: ""
    }

    private fun setupAppBar(flight: Flight, isMyFlight: Boolean) {
        binding.shareAppbar.isDeleteIconVisible = isMyFlight
        binding.shareAppbar.isShareVisible = isMyFlight
        binding.editButton.isVisible = isMyFlight

        binding.shareAppbar.isResignVisible = !isMyFlight

        binding.ownerEtw.isMyFlight = isMyFlight
        binding.ownerEtw.ownerEmail = flight.ownerData.ownerData.email
        binding.ownerEtw.ownerNick = flight.ownerData.ownerData.nick
        binding.ownerEtw.profileUrl = flight.ownerData.image?.thumbnailUrl
    }

    private fun setupSharedUsersData(shareList: List<ShareData>, isMyFlight: Boolean) {
        binding.sharedUsersCg.removeAllViews()
        val anyUsers =
            shareList.any { it.sharedUserData != null && (it.sharedData.isConfirmed || isMyFlight) }
        binding.sharedUsersTv.isVisible = anyUsers
        binding.sharedUsersCg.isVisible = anyUsers
        binding.seeAllTv.isVisible = anyUsers
        shareList.filter { it.sharedData.isConfirmed && (it.sharedData.isConfirmed || isMyFlight) }
            .forEach {
                it.sharedUserData?.let { userData ->
                    val child = layoutInflater.inflate(R.layout.element_shared_user_chip, null)
                    val chip = child.findViewById<Chip>(R.id.chip)!!
                    chip.text = userData.sharedUser.nick.ifBlank {
                        userData.sharedUser.email
                    }
                    userData.image?.thumbnailUrl?.let { url ->
                        Glide.with(chip)
                            .load(url)
                            .submit(100, 100)
                            .get()?.let { drawable ->
                                chip.chipIcon = drawable
                            }
                    }
                    binding.sharedUsersCg.addView(chip)
                }

            }
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