package pl.kossa.myflights.fragments.airports.add

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentAirportAddBinding
import pl.kossa.myflights.exstensions.doOnTextChanged

@AndroidEntryPoint
class AirportAddFragment : BaseFragment<AirportAddViewModel, FragmentAirportAddBinding>() {

    override val viewModel: AirportAddViewModel by viewModels()

    override fun setOnClickListeners() {
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

    override fun setObservers() {
        super.setObservers()
        collectFlow()
    }

    private fun collectFlow() {
        //TODO
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.loadingProgressBar
    }
}