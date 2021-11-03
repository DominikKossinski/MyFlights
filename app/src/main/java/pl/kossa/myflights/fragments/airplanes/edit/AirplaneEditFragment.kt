package pl.kossa.myflights.fragments.airplanes.edit

import android.os.Bundle
import android.view.View
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
import pl.kossa.myflights.databinding.FragmentAirplaneEditBinding
import pl.kossa.myflights.exstensions.doOnTextChanged

@AndroidEntryPoint
class AirplaneEditFragment : BaseFragment<AirplaneEditViewModel, FragmentAirplaneEditBinding>() {

    override val viewModel: AirplaneEditViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.loadingProgressBar
    }

    override fun collectFlow() {
        super.collectFlow()
       viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.airplane.collect {
                it?.let { setupAirplaneData(it)}
            }
        }
       viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isSaveButtonEnabled.collect {
                binding.saveButton.isEnabled = it
                binding.saveAppBar.isSaveIconEnabled = it
            }
        }
    }

    override fun setOnClickListeners() {
        binding.saveAppBar.setBackOnClickListener {
            viewModel.navigateBack()
        }
        binding.saveAppBar.setSaveOnClickListener {
            viewModel.putAirplane()
        }
        binding.saveButton.setOnClickListener {
            viewModel.putAirplane()
        }

        binding.nameTie.doOnTextChanged{ text ->
            viewModel.setAirplaneName(text)
        }
        binding.maxSpeedTie.doOnTextChanged { text ->
            text.toIntOrNull().let {
                viewModel.setMaxSpeed(it)
            }
        }
        binding.weightTie.doOnTextChanged { text ->
            text.toIntOrNull().let {
                viewModel.setWeight(it)
            }
        }
    }

    private fun setupAirplaneData(airplane: Airplane) {
        binding.nameTie.setText(airplane.name)
        binding.maxSpeedTie.setText(airplane.maxSpeed?.toString() ?: "")
        binding.weightTie.setText(airplane.weight?.toString() ?: "")
    }

    override fun handleApiError(apiError: ApiError) {
        when(apiError.code) {
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