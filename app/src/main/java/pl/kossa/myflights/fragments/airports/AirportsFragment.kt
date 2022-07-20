package pl.kossa.myflights.fragments.airports

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.R
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.api.responses.HttpCode
import pl.kossa.myflights.architecture.fragments.BaseFragment
import pl.kossa.myflights.architecture.BaseSwipeDeleteCallback
import pl.kossa.myflights.databinding.FragmentAirportsBinding
import pl.kossa.myflights.fragments.airports.adapter.AirportsAdapter

@AndroidEntryPoint
class AirportsFragment : BaseFragment<AirportsViewModel, FragmentAirportsBinding>() {

    override val viewModel: AirportsViewModel by viewModels()

    private val swipeDeleteCallback by lazy {
        object : BaseSwipeDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val item = adapter.items[position]
                adapter.items.remove(item)
                adapter.notifyItemRemoved(position)
                val snackbar = Snackbar.make(
                    binding.coordinatorLayout,
                    R.string.airport_delete_question,
                    Snackbar.LENGTH_LONG
                )
                snackbar.setAction(R.string.cancel) {
                    snackbar.dismiss()
                    adapter.items.add(position, item)
                    adapter.notifyItemInserted(position)
                }
                snackbar.addCallback(object : Snackbar.Callback() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        super.onDismissed(transientBottomBar, event)
                        if (event == DISMISS_EVENT_TIMEOUT) {
                            viewModel.deleteAirport(item.airport.airportId)
                        }
                    }
                })
                snackbar.show()
            }

        }
    }

    private val adapter = AirportsAdapter()

    override fun setOnClickListeners() {
        binding.airportsSwipeRefresh.setOnRefreshListener {
            viewModel.fetchAirports()
        }
        binding.fab.setOnClickListener {
            viewModel.navigateToAddAirport()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }


    override fun collectFlow() {
        super.collectFlow()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isLoadingData.collect {
                binding.airportsSwipeRefresh.isRefreshing = it
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.airportsList.collect {
                binding.noAirportsTextView.isVisible = it.isEmpty()
                adapter.items.clear()
                adapter.items.addAll(it)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun setupRecyclerView() {
        adapter.setOnItemClickListener {
            viewModel.navigateToAirportDetails(it.airport.airportId)
        }
        binding.airportsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.airportsRecyclerView.adapter = adapter
        val itemTouchHelper = ItemTouchHelper(swipeDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.airportsRecyclerView)
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchAirports()
    }

    override fun handleApiError(apiError: ApiError) {
        when (apiError.code) {
            HttpCode.NOT_FOUND.code -> {
                viewModel.setToastMessage( R.string.error_airport_not_found)
            }
            HttpCode.BAD_REQUEST.code -> {
                viewModel.setToastMessage( R.string.error_airport_exists_in_flights)
                viewModel.fetchAirports()
            }
            HttpCode.INTERNAL_SERVER_ERROR.code -> {
                viewModel.setToastMessage( R.string.unexpected_error)
            }
            HttpCode.FORBIDDEN.code -> {
                viewModel.setToastMessage( R.string.error_forbidden)
            }
            else -> {
                viewModel.setToastMessage( R.string.unexpected_error)
            }
        }
    }
}