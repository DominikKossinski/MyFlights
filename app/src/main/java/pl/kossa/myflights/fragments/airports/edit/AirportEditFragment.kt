package pl.kossa.myflights.fragments.airports.edit

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_airplane_edit.*
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentAirportEditBinding

class AirportEditFragment : BaseFragment<FragmentAirportEditBinding, AirportEditViewModel>() {

    override val layoutId: Int = R.layout.fragment_airport_edit

    private val args by navArgs<AirportEditFragmentArgs>()

    override val viewModel by lazy {
        AirportEditViewModel(args.airportId, findNavController(), preferencesHelper)
    }

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