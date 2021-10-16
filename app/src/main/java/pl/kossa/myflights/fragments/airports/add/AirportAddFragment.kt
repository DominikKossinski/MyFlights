package pl.kossa.myflights.fragments.airports.add

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.kossa.myflights.R
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.api.responses.HttpCode
import pl.kossa.myflights.architecture.BaseFragment
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
        lifecycleScope.launch {
            viewModel.isAddButtonEnabled.collect {
                binding.addButton.isEnabled = it
                binding.saveAppBar.isEnabled = it
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
                viewModel.setToastError( R.string.error_airplane_not_found)
            }
            HttpCode.INTERNAL_SERVER_ERROR.code -> {
                viewModel.setToastError( R.string.unexpected_error)
            }
            HttpCode.FORBIDDEN.code -> {
                viewModel.setToastError( R.string.error_forbidden)
            }
            else -> {
                viewModel.setToastError( R.string.unexpected_error)
            }
        }
        viewModel.navigateBack()
    }
}