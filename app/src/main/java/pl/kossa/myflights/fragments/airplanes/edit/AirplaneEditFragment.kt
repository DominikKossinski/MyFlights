package pl.kossa.myflights.fragments.airplanes.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentAirplaneEditBinding

@AndroidEntryPoint
class AirplaneEditFragment : BaseFragment<AirplaneEditViewModel, FragmentAirplaneEditBinding>() {

    override val viewModel: AirplaneEditViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.loadingProgressBar
    }


    override fun setObservers() {
        super.setObservers()
        viewModel.airplaneLiveData.observe(viewLifecycleOwner) {
            viewModel.airplane = it
        }
    }

    override fun setOnClickListeners() {
        binding.saveButton.setOnClickListener {
            viewModel.saveAirplane()
        }
    }
}