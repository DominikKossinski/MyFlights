package pl.kossa.myflights.fragments.flights.pending

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.architecture.fragments.BaseFragment
import pl.kossa.myflights.databinding.FragmentPendingSharedFlightsBinding
import pl.kossa.myflights.fragments.flights.pending.adapter.PendingSharedFlightsAdapter

@AndroidEntryPoint
class PendingSharedFlightsFragment :
    BaseFragment<PendingSharedFlightsViewModel, FragmentPendingSharedFlightsBinding>() {

    override val viewModel: PendingSharedFlightsViewModel by viewModels()

    private val adapter = PendingSharedFlightsAdapter()

    override fun setOnClickListeners() {
        binding.backAppbar.setBackOnClickListener {
            viewModel.navigateBack()
        }
        binding.sharedFlightsSwipeRefresh.setOnRefreshListener {
            viewModel.fetchPendingSharedFlights()
        }
        adapter.setOnItemClickListener {
            viewModel.navigateToPendingSharedFlightDetails(it.sharedFlightId)
        }
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.sharedFlightsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.sharedFlightsRecyclerView.adapter = adapter
    }

    override fun collectFlow() {
        super.collectFlow()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isLoadingData.collectLatest {
                binding.sharedFlightsSwipeRefresh.isRefreshing = it
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.pendingSharedFlights.collectLatest {
                adapter.items.clear()
                adapter.items.addAll(it)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun handleApiError(apiError: ApiError) {
        TODO("Not yet implemented")
    }
}