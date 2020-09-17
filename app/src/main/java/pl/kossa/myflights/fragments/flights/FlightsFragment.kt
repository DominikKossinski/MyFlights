package pl.kossa.myflights.fragments.flights

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_flights.*
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentFlightsBinding

class FlightsFragment : BaseFragment<FragmentFlightsBinding, FlightsViewModel>() {

    override val layoutId: Int = R.layout.fragment_flights

    override val viewModel: FlightsViewModel by lazy {
        FlightsViewModel(findNavController(), preferencesHelper)
    }

    override fun setBindingVariables(binding: FragmentFlightsBinding) {
        binding.viewModel = viewModel
    }

    override fun setOnClickListeners() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = loadingProgressBar
        viewModel.fetchFlights()
    }


}