package pl.kossa.myflights.fragments.airports.runways.add

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentRunwayAddBinding
import pl.kossa.myflights.exstensions.doOnTextChanged

@AndroidEntryPoint
class RunwayAddFragment : BaseFragment<RunwayAddViewModel, FragmentRunwayAddBinding>() {

    override val viewModel: RunwayAddViewModel by viewModels()

    override fun setOnClickListeners() {
        binding.addRunwayButton.setOnClickListener {
            viewModel.postRunway()
        }

        binding.nameTie.doOnTextChanged { text ->
            viewModel.setName(text)
        }
        binding.lengthTie.doOnTextChanged { text ->
            text.toIntOrNull()?.let {
                viewModel.setLength(it)
            }
        }
        binding.headingTie.doOnTextChanged { text ->
            text.toIntOrNull()?.let {
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
            viewModel.isAddButtonEnabled.collect {
                binding.addRunwayButton.isEnabled = it
            }
        }
    }
}