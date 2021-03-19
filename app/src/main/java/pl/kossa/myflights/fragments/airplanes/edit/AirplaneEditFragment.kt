package pl.kossa.myflights.fragments.airplanes.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_airplane_edit.*
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentAirplaneEditBinding

@AndroidEntryPoint
class AirplaneEditFragment : BaseFragment<FragmentAirplaneEditBinding, AirplaneEditViewModel>() {

    override val layoutId: Int = R.layout.fragment_airplane_edit


    override val viewModel: AirplaneEditViewModel by viewModels()

    override fun setBindingVariables(binding: FragmentAirplaneEditBinding) {
        binding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = loadingProgressBar
    }

    override fun setObservers() {
        super.setObservers()
        viewModel.airplaneLiveData.observe(viewLifecycleOwner) {
            viewModel.airplane = it
        }
    }

    override fun setOnClickListeners() {
        saveButton.setOnClickListener {
            viewModel.saveAirplane()
        }
    }
}