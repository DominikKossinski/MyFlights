package pl.kossa.myflights.fragments.flights.users

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.architecture.fragments.BaseFragment
import pl.kossa.myflights.databinding.FragmentSharedUsersBinding
import pl.kossa.myflights.fragments.flights.users.adapter.SharedUsersAdapter

@AndroidEntryPoint
class SharedUsersFragment : BaseFragment<SharedUsersViewModel, FragmentSharedUsersBinding>() {

    override val viewModel: SharedUsersViewModel by viewModels()

    private val adapter = SharedUsersAdapter()

    override fun setOnClickListeners() {
        binding.backAppbar.setBackOnClickListener {
            viewModel.navigateBack()
        }
        binding.sharedUsersSwipeRefresh.setOnRefreshListener {
            viewModel.fetchFlight()
        }
        setupRecyclerView()
    }

    override fun collectFlow() {
        super.collectFlow()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isLoadingData.collectLatest {
                binding.sharedUsersSwipeRefresh.isRefreshing = it
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.flightResponse.collectLatest {
                it?.let {
                    adapter.items.clear()
                    val isMyFlight = viewModel.getUserId() == it.ownerData.userId
                    adapter.isMyFlight = isMyFlight
                    adapter.items.addAll(it.sharedUsers.filter { sU -> sU.isConfirmed || isMyFlight })
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.sharedUsersRv.layoutManager = LinearLayoutManager(requireContext())
        binding.sharedUsersRv.adapter = adapter
    }

    override fun handleApiError(apiError: ApiError) {
        TODO("Not yet implemented")
    }
}