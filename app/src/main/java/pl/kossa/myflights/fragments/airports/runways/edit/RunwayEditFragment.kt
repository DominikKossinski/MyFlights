package pl.kossa.myflights.fragments.airports.runways.edit

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.kossa.myflights.api.models.Runway
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentRunwayEditBinding
import pl.kossa.myflights.exstensions.doOnTextChanged

@AndroidEntryPoint
class RunwayEditFragment : BaseFragment<RunwayEditViewModel, FragmentRunwayEditBinding>() {

    override val viewModel: RunwayEditViewModel by viewModels()

    override fun setOnClickListeners() {
        binding.saveRunwayButton.setOnClickListener {
            viewModel.putRunway()
        }
        binding.editAppBar.setSaveOnClickListener {
            viewModel.putRunway()
        }

        binding.editAppBar.setBackOnClickListener {
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

    override fun setObservers() {
        super.setObservers()
        collectFlow()
    }

    private fun collectFlow() {
        lifecycleScope.launch {
            viewModel.runway.collect {
                it?.let { setupRunwayData(it) }
            }
        }
        lifecycleScope.launch {
            viewModel.isSaveButtonEnabled.collect {
                binding.saveRunwayButton.isEnabled = it
                binding.editAppBar.isSaveIconEnabled = it
            }
        }
    }

    private fun setupRunwayData(runway: Runway) {
        binding.nameTie.setText(runway.name)
        binding.lengthTie.setText(runway.length.toString())
        binding.headingTie.setText(runway.heading.toString())
        binding.ilsFrequencyTie.setText(runway.ilsFrequency.toString())
    }
}