package pl.kossa.myflights.fragments.airplanes

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_airplanes.*
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentAirplanesBinding
import pl.kossa.myflights.fragments.airplanes.adapter.AirplaneViewModel
import pl.kossa.myflights.fragments.airplanes.adapter.AirplanesAdapter

class AirplanesFragment : BaseFragment<FragmentAirplanesBinding, AirplanesViewModel>() {

    override val layoutId = R.layout.fragment_airplanes

    override val viewModel by lazy {
        AirplanesViewModel(findNavController(), preferencesHelper)
    }

    private val adapter = AirplanesAdapter()

    override fun setBindingVariables(binding: FragmentAirplanesBinding) {
        binding.viewModel = viewModel
    }

    override fun setOnClickListeners() {
        airplanesSwipeRefresh.setOnRefreshListener {
            viewModel.fetchAirplanes()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchAirplanes()
        setupRecyclerView()
    }

    override fun setObservers() {
        super.setObservers()
        viewModel.isLoadingData.observe(viewLifecycleOwner) {
            airplanesSwipeRefresh.isRefreshing = it
        }
        viewModel.airplanesList.observe(viewLifecycleOwner) {
            viewModel.noAirplanesVisibility = it.isEmpty()
            adapter.items.clear()
            adapter.items.addAll(it.map { airplane -> AirplaneViewModel(airplane) })
            adapter.notifyDataSetChanged()
        }
    }

    fun setupRecyclerView() {
        airplanesRecyclerView.layoutManager = LinearLayoutManager(context)
        airplanesRecyclerView.adapter = adapter
    }

}