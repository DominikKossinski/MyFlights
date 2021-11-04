package pl.kossa.myflights.fragments.airports.runways.edit

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.Runway
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.api.responses.HttpCode
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentRunwayEditBinding
import pl.kossa.myflights.exstensions.doOnTextChanged

@AndroidEntryPoint
class RunwayEditFragment : BaseFragment<RunwayEditViewModel, FragmentRunwayEditBinding>() {

    override val viewModel: RunwayEditViewModel by viewModels()

    override fun setOnClickListeners() {
        binding.saveButton.setOnClickListener {
            viewModel.putRunway()
        }
        binding.saveAppBar.setSaveOnClickListener {
            viewModel.putRunway()
        }

        binding.saveAppBar.setBackOnClickListener {
            viewModel.navigateBack()
        }

        binding.nameTie.doOnTextChanged { text ->
            viewModel.setName(text)
        }
        binding.lengthTie.doOnTextChanged { text ->
            text.toIntOrNull().let {
                viewModel.setLength(it)
            }
        }
        binding.headingTie.doOnTextChanged { text ->
            text.toIntOrNull().let {
                viewModel.setHeading(it)
            }
        }
        binding.ilsFrequencyTie.doOnTextChanged { text ->
            viewModel.setIlsFrequency(text)
        }
    }

    override fun collectFlow() {
        super.collectFlow()
       viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.runway.collect {
                it?.let { setupRunwayData(it) }
            }
        }
       viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isSaveButtonEnabled.collect {
                binding.saveButton.isEnabled = it
                binding.saveAppBar.isSaveIconEnabled = it
            }
        }
    }

    private fun setupRunwayData(runway: Runway) {
        binding.nameTie.setText(runway.name)
        binding.lengthTie.setText(runway.length.toString())
        binding.headingTie.setText(runway.heading.toString())
        binding.ilsFrequencyTie.setText(runway.ilsFrequency.toString())
    }

    override fun handleApiError(apiError: ApiError) {
        when (apiError.code) {
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