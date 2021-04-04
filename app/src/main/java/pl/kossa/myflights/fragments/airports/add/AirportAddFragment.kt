package pl.kossa.myflights.fragments.airports.add

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_airport_add.*
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentAirportAddBinding

class AirportAddFragment : BaseFragment<AirportAddViewModel>() {

    override val layoutId = R.layout.fragment_airport_add

    override val viewModel: AirportAddViewModel by viewModels()

    override fun setOnClickListeners() {
        addButton.setOnClickListener {
            viewModel.postAirport()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = loadingProgressBar
    }
}