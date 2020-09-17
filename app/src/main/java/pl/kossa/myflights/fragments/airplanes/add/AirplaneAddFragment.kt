package pl.kossa.myflights.fragments.airplanes.add

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_airplane_add.*
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentAirplaneAddBinding

class AirplaneAddFragment : BaseFragment<FragmentAirplaneAddBinding, AirplaneAddViewModel>() {

    override val layoutId = R.layout.fragment_airplane_add

    override val viewModel by lazy {
        AirplaneAddViewModel(
            findNavController(),
            preferencesHelper
        )
    }

    override fun setBindingVariables(binding: FragmentAirplaneAddBinding) {
        binding.viewModel = viewModel
    }

    override fun setOnClickListeners() {
        addButton.setOnClickListener {
            viewModel.postAirplane()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = loadingProgressBar
    }
}