package pl.kossa.myflights.fragments.airports.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentAirportEditBinding

@AndroidEntryPoint
class AirportEditFragment : BaseFragment<AirportEditViewModel, FragmentAirportEditBinding>() {

    override val viewModel: AirportEditViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.loadingProgressBar
    }

    override fun setObservers() {
        super.setObservers()
        viewModel.airportLiveData.observe(viewLifecycleOwner) {
            viewModel.airport = it
        }
    }

    override fun setOnClickListeners() {
        binding.saveButton.setOnClickListener {
            viewModel.saveAirport()
        }
    }
}