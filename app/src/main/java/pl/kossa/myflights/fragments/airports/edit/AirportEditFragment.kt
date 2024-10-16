package pl.kossa.myflights.fragments.airports.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.R
import pl.kossa.myflights.room.entities.Airport
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.api.responses.HttpCode
import pl.kossa.myflights.architecture.fragments.BaseFragment
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

    override fun collectFlow() {
        super.collectFlow()
       viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isSaveButtonEnabled.collect {
                binding.saveButton.isEnabled = it
                binding.saveAppBar.isSaveIconEnabled = it
            }
        }
       viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.airport.collect {
                it?.let { setupAirportData(it) }
            }
        }
    }

    private fun setupAirportData(airport: Airport) {
        binding.nameTie.setText(airport.airport.name)
        binding.cityTie.setText(airport.airport.city)
        binding.icaoCodeTie.setText(airport.airport.icaoCode)
        binding.towerFrequencyTie.setText(airport.airport.towerFrequency ?: "")
        binding.groundFrequencyTie.setText(airport.airport.groundFrequency ?: "")
    }

    override fun handleApiError(apiError: ApiError) {
        when(apiError.code) {
            HttpCode.NOT_FOUND.code -> {
                viewModel.setToastMessage( R.string.error_airport_not_found)
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