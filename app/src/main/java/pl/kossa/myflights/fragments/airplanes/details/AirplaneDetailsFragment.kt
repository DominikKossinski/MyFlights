package pl.kossa.myflights.fragments.airplanes.details

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentAirplaneDetailsBinding

class AirplaneDetailsFragment :
    BaseFragment<FragmentAirplaneDetailsBinding, AirplaneDetailsViewModel>() {

    override val layoutId = R.layout.fragment_airplane_details

    private val args by navArgs<AirplaneDetailsFragmentArgs>()

    override val viewModel by lazy {
        AirplaneDetailsViewModel(
            args.airplaneId,
            findNavController(),
            preferencesHelper
        )
    }

    override fun setBindingVariables(binding: FragmentAirplaneDetailsBinding) {
        binding.viewModel = viewModel
    }

    override fun setOnClickListeners() {
    }

}