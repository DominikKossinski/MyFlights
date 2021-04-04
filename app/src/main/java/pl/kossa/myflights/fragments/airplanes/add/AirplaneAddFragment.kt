package pl.kossa.myflights.fragments.airplanes.add

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_airplane_add.*
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentAirplaneAddBinding
import pl.kossa.myflights.fragments.airplanes.adapter.AirplaneViewModel

@AndroidEntryPoint
class AirplaneAddFragment : BaseFragment<AirplaneAddViewModel>() {

    override val layoutId = R.layout.fragment_airplane_add

    override val viewModel: AirplaneAddViewModel by viewModels()

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