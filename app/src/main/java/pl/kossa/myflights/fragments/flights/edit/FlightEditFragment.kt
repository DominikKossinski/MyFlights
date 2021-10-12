package pl.kossa.myflights.fragments.flights.edit

import androidx.fragment.app.viewModels
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentFlightEditBinding

class FlightEditFragment : BaseFragment<FlightEditViewModel, FragmentFlightEditBinding>() {

    override val viewModel: FlightEditViewModel by viewModels()

    override fun setOnClickListeners() {
        //TODO
    }
}