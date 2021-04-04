package pl.kossa.myflights.fragments.flights

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_flights.*
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentFlightsBinding

@AndroidEntryPoint
class FlightsFragment : BaseFragment<FlightsViewModel>() {

    override val layoutId: Int = R.layout.fragment_flights

    override val viewModel: FlightsViewModel by viewModels()

    override fun setOnClickListeners() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = loadingProgressBar
        viewModel.fetchFlights()
    }


}