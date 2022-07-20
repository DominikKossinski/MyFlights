package pl.kossa.myflights.fragments.airports.runways.details

import android.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.R
import pl.kossa.myflights.room.entities.Runway
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.api.responses.HttpCode
import pl.kossa.myflights.architecture.fragments.BaseFragment
import pl.kossa.myflights.databinding.FragmentRunwayDetailsBinding

@AndroidEntryPoint
class RunwayDetailsFragment : BaseFragment<RunwayDetailsViewModel, FragmentRunwayDetailsBinding>() {

    override val viewModel: RunwayDetailsViewModel by viewModels()

    override fun setOnClickListeners() {
        binding.runwaySwipeRefresh.setOnRefreshListener {
            viewModel.fetchRunway()
        }
        setupAppBar()
    }

    private fun setupAppBar() {
        binding.backAppbar.setBackOnClickListener {
            viewModel.navigateBack()
        }
        binding.backAppbar.setDeleteOnClickListener {
            showDeleteDialog()
        }

        binding.editButton.setOnClickListener {
            viewModel.navigateToRunwayEdit()
        }

    }

    override fun collectFlow() {
        super.collectFlow()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isLoadingData.collect {
                binding.runwaySwipeRefresh.isRefreshing = it
            }
        }
       viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.runway.collect {
                it?.let { setupRunwayData(it) }
            }
        }
    }

    private fun setupRunwayData(runway: Runway) {
        binding.nameEwt.valueText = runway.runway.name
        //TODO add formatting
        binding.lengthEwt.valueText = runway.runway.length.toString()
        //TODO formatting
        binding.headingEwt.valueText = runway.runway.heading.toString()
        binding.ilsFrequencyEwt.isVisible = runway.runway.ilsFrequency != null
        binding.ilsFrequencyEwt.valueText = runway.runway.ilsFrequency ?: ""
    }

    private fun showDeleteDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(R.string.runway_delete_question_info)
        builder.setPositiveButton(
            R.string.delete
        ) { dialog, _ ->
            viewModel.deleteRunway()
            dialog?.dismiss()
        }
        builder.setNegativeButton(R.string.cancel) { dialog, _ ->
            dialog?.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchRunway()
    }

    override fun handleApiError(apiError: ApiError) {
        when(apiError.code) {
            HttpCode.BAD_REQUEST.code -> {
                viewModel.setToastMessage( R.string.error_runway_exists_in_flights)
            }
            HttpCode.NOT_FOUND.code -> {
                viewModel.setToastMessage( R.string.error_runway_not_found)
                viewModel.navigateBack()
            }
            HttpCode.INTERNAL_SERVER_ERROR.code -> {
                viewModel.setToastMessage( R.string.unexpected_error)
                viewModel.navigateBack()
            }
            HttpCode.FORBIDDEN.code -> {
                viewModel.setToastMessage( R.string.error_forbidden)
                viewModel.navigateBack()
            }
            else -> {
                viewModel.setToastMessage( R.string.unexpected_error)
                viewModel.navigateBack()
            }
        }
    }
}