package pl.kossa.myflights.fragments.airports.add

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.R
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.api.responses.HttpCode
import pl.kossa.myflights.architecture.fragments.BaseFragment
import pl.kossa.myflights.databinding.FragmentAirportAddBinding
import pl.kossa.myflights.exstensions.doOnTextChanged

@AndroidEntryPoint
class AirportAddFragment : BaseFragment<AirportAddViewModel, FragmentAirportAddBinding>() {

    override val viewModel: AirportAddViewModel by viewModels()

    override fun setOnClickListeners() {
        binding.saveAppBar.setBackOnClickListener {
            viewModel.navigateBack()
        }
        binding.saveAppBar.setSaveOnClickListener {
            viewModel.postAirport()
        }
        binding.addButton.setOnClickListener {
            viewModel.postAirport()
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
            viewModel.isAddButtonEnabled.collect {
                binding.addButton.isEnabled = it
                binding.saveAppBar.isSaveIconEnabled = it
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.loadingProgressBar
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