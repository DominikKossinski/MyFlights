package pl.kossa.myflights.fragments.airports.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_airplane_edit.*
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentAirportEditBinding

@AndroidEntryPoint
class AirportEditFragment : BaseFragment<FragmentAirportEditBinding, AirportEditViewModel>() {

    override val layoutId: Int = R.layout.fragment_airport_edit

    override val viewModel: AirportEditViewModel by viewModels()

    override fun setBindingVariables(binding: FragmentAirportEditBinding) {
        binding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = loadingProgressBar
    }

    override fun setObservers() {
        super.setObservers()
        viewModel.airportLiveData.observe(viewLifecycleOwner) {
            viewModel.airport = it
        }
    }

    override fun setOnClickListeners() {
        saveButton.setOnClickListener {
            viewModel.saveAirport()
        }
    }
}