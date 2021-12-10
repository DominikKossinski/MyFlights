package pl.kossa.myflights.fragments.airports.runways.add

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import pl.kossa.myflights.R
import pl.kossa.myflights.api.server.responses.ApiError
import pl.kossa.myflights.api.server.responses.HttpCode
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentRunwayAddBinding
import pl.kossa.myflights.exstensions.doOnTextChanged

@AndroidEntryPoint
class RunwayAddFragment : BaseFragment<RunwayAddViewModel, FragmentRunwayAddBinding>() {

    override val viewModel: RunwayAddViewModel by viewModels()

    override fun setOnClickListeners() {
        binding.saveAppBar.setBackOnClickListener {
            viewModel.navigateBack()
        }
        binding.saveAppBar.setSaveOnClickListener {
            viewModel.postRunway()
        }
        binding.addButton.setOnClickListener {
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
       viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isAddButtonEnabled.collect {
                binding.addButton.isEnabled = it
                binding.saveAppBar.isSaveIconEnabled = it
            }
        }
    }

    override fun handleApiError(apiError: ApiError) {
        when(apiError.code) {
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