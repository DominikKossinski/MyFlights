package pl.kossa.myflights.fragments.flights.details

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseFragment

@AndroidEntryPoint
class FlightDetailsFragment : BaseFragment<FlightDetailsViewModel>() {

    override val layoutId = R.layout.fragment_flight_details
    override val viewModel: FlightDetailsViewModel by viewModels()


    override fun setOnClickListeners() {
        //TODO
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchFlight()
    }


}