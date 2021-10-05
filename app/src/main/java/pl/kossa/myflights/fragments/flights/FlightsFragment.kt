package pl.kossa.myflights.fragments.flights

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentFlightsBinding
import pl.kossa.myflights.fragments.flights.adapter.FlightsAdapter

@AndroidEntryPoint
class FlightsFragment : BaseFragment<FlightsViewModel, FragmentFlightsBinding>() {

    override val viewModel: FlightsViewModel by viewModels()

    private val adapter = FlightsAdapter()

    override fun setOnClickListeners() {
        binding.flightsSwipeRefresh.setOnRefreshListener {
            viewModel.fetchFlights()
        }
        binding.fab.setOnClickListener {
            viewModel.navigateToAddFlight()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    override fun setObservers() {
        super.setObservers()
        viewModel.isLoadingData.observe(viewLifecycleOwner) {
            binding.flightsSwipeRefresh.isRefreshing = it
        }
        collectFlow()
    }

    private fun collectFlow() {
        lifecycleScope.launch {
            viewModel.flightsList.collect {
                binding.noFlightsTextView.isVisible = it.isEmpty()
                adapter.items.clear()
                adapter.items.addAll(it)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun setupRecyclerView() {
        adapter.setOnItemClickListener {
            viewModel.navigateToFlightDetails(it.flightId)
        }
        binding.flightsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.flightsRecyclerView.adapter = adapter
//    TODO    val itemTouchHelper = ItemTouchHelper(swipeDeleteCallback)
//        itemTouchHelper.attachToRecyclerView(binding.flightsRecyclerView)
    }


}