package pl.kossa.myflights.fragments.airplanes.add

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentAirplaneAddBinding
import pl.kossa.myflights.exstensions.doOnTextChanged

@AndroidEntryPoint
class AirplaneAddFragment : BaseFragment<AirplaneAddViewModel, FragmentAirplaneAddBinding>() {

    override val viewModel: AirplaneAddViewModel by viewModels()

    override fun setOnClickListeners() {
        binding.saveAppBar.setBackOnClickListener {
            viewModel.navigateBack()
        }
        binding.saveAppBar.setSaveOnClickListener {
            viewModel.postAirplane()
        }
        binding.addButton.setOnClickListener {
            viewModel.postAirplane()
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

    override fun setObservers() {
        super.setObservers()
        collectFlow()
    }

    private fun collectFlow() {
        lifecycleScope.launch {
            viewModel.isAddButtonEnabled.collect {
                binding.addButton.isEnabled = it
                binding.saveAppBar.isSaveIconEnabled = it
            }
        }
        lifecycleScope.launch {
            viewModel.nameError.collect {
                binding.nameTil.error = it?.let {
                    getString(it)
                }
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.loadingProgressBar
    }
}