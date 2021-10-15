package pl.kossa.myflights.fragments.airports.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.kossa.myflights.api.models.Airport
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentAirportEditBinding
import pl.kossa.myflights.exstensions.doOnTextChanged

@AndroidEntryPoint
class AirportEditFragment : BaseFragment<AirportEditViewModel, FragmentAirportEditBinding>() {

    override val viewModel: AirportEditViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.loadingProgressBar
    }

    override fun setOnClickListeners() {
        binding.saveButton.setOnClickListener {
            viewModel.putAirport()
        }
        binding.saveAppBar.setBackOnClickListener {
            viewModel.navigateBack()
        }
        binding.saveAppBar.setSaveOnClickListener {
            viewModel.putAirport()
        }

        binding.nameTie.doOnTextChanged { text ->
            viewModel.setName(text)
        }
        binding.cityTie.doOnTextChanged { text ->
            viewModel.setCity(text)
        }
        binding.icaoCodeTie.doOnTextChanged { text ->
            viewModel.setIcaoCode(text)
        }
        binding.towerFrequencyTie.doOnTextChanged { text ->
            viewModel.setTowerFrequency(text)
        }
        binding.groundFrequencyTie.doOnTextChanged { text ->
            viewModel.setGroundFrequency(text)
        }
    }

    override fun setObservers() {
        super.setObservers()
        collectFlow()
    }

    private fun collectFlow() {
        lifecycleScope.launch {
            viewModel.isSaveButtonEnabled.collect {
                binding.saveButton.isEnabled = it
                binding.saveAppBar.isSaveIconEnabled = it
            }
        }
        lifecycleScope.launch {
            viewModel.airport.collect {
                it?.let { setupAirportData(it) }
            }
        }
    }

    private fun setupAirportData(airport: Airport) {
        binding.nameTie.setText(airport.name)
        binding.cityTie.setText(airport.city)
        binding.icaoCodeTie.setText(airport.icaoCode)
        binding.towerFrequencyTie.setText(airport.towerFrequency ?: "")
        binding.groundFrequencyTie.setText(airport.groundFrequency ?: "")
    }
}