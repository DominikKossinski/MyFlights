package pl.kossa.myflights.fragments.airports.runways.add

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.kossa.myflights.R
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.api.responses.HttpCode
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentRunwayAddBinding
import pl.kossa.myflights.exstensions.doOnTextChanged

@AndroidEntryPoint
class RunwayAddFragment : BaseFragment<RunwayAddViewModel, FragmentRunwayAddBinding>() {

    override val viewModel: RunwayAddViewModel by viewModels()

    override fun setOnClickListeners() {
        binding.saveAppBar.setBackOnClickListener {
            viewModel.postRunway()
        }
        binding.saveAppBar.setBackOnClickListener {
            viewModel.navigateBack()
        }
        binding.addRunwayButton.setOnClickListener {
            viewModel.postRunway()
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
        lifecycleScope.launch {
            viewModel.isAddButtonEnabled.collect {
                binding.addRunwayButton.isEnabled = it
                binding.saveAppBar.isSaveIconEnabled = it
            }
        }
    }

    override fun handleApiError(apiError: ApiError) {
        when(apiError.code) {
            HttpCode.INTERNAL_SERVER_ERROR.code -> {
                viewModel.setToastError( R.string.unexpected_error)
                viewModel.navigateBack()
            }
            HttpCode.FORBIDDEN.code -> {
                viewModel.setToastError( R.string.error_forbidden)
                viewModel.navigateBack()
            }
            else -> {
                viewModel.setToastError( R.string.unexpected_error)
                viewModel.navigateBack()
            }
        }
    }
}