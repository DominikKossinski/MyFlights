package pl.kossa.myflights.fragments.airplanes.edit

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_airplane_edit.*
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentAirplaneEditBinding

class AirplaneEditFragment : BaseFragment<FragmentAirplaneEditBinding, AirplaneEditViewModel>() {

    override val layoutId: Int = R.layout.fragment_airplane_edit

    private val args by navArgs<AirplaneEditFragmentArgs>()

    override val viewModel: AirplaneEditViewModel by lazy {
        AirplaneEditViewModel(args.airplaneId, findNavController(), preferencesHelper)
    }

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