package pl.kossa.myflights.fragments.airplanes.details

import android.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.Airplane
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.api.responses.HttpCode
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentAirplaneDetailsBinding

@AndroidEntryPoint
class AirplaneDetailsFragment :
    BaseFragment<AirplaneDetailsViewModel, FragmentAirplaneDetailsBinding>() {

    override val viewModel: AirplaneDetailsViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        viewModel.fetchAirplane()
    }

    override fun setObservers() {
        super.setObservers()
        viewModel.airplane.observe(viewLifecycleOwner) {
            setupAirplaneData(it)
        }
    }

    override fun collectFlow() {
        super.collectFlow()
        lifecycleScope.launch {
            viewModel.isLoadingData.collect {
                binding.airplaneSwipeRefresh.isRefreshing = it
            }
        }
    }

    override fun setOnClickListeners() {
        binding.backAppbar.setBackOnClickListener {
            viewModel.navigateBack()
        }
        binding.editButton.setOnClickListener {
            viewModel.navigateToAirplaneEdit()
        }
        binding.backAppbar.setDeleteOnClickListener {
            showDeleteDialog()
        }

    }

    private fun setupAirplaneData(airplane: Airplane) {
        binding.nameEwt.valueText = airplane.name
        binding.maxSpeedEtw.valueText = airplane.maxSpeed?.toString() ?: ""
        binding.weightEwt.valueText = airplane.weight?.toString() ?: ""
    }

    private fun showDeleteDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(R.string.airplane_delete_question_info)
        builder.setPositiveButton(
            R.string.delete
        ) { dialog, _ ->
            viewModel.deleteAirplane()
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
            HttpCode.BAD_REQUEST.code -> {
                viewModel.setToastMessage( R.string.error_airplane_exists_in_flights)
            }
            HttpCode.NOT_FOUND.code -> {
                viewModel.setToastMessage( R.string.error_airplane_not_found)
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
        viewModel.navigateBack()
    }

}