package pl.kossa.myflights.fragments.flights

import android.content.Intent
import android.net.Uri
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
import pl.kossa.myflights.architecture.BaseSwipeDeleteCallback
import pl.kossa.myflights.architecture.fragments.BaseFragment
import pl.kossa.myflights.databinding.FragmentFlightsBinding
import pl.kossa.myflights.fragments.flights.adapter.FlightsAdapter

@AndroidEntryPoint
class FlightsFragment : BaseFragment<FlightsViewModel, FragmentFlightsBinding>() {

    override val viewModel: FlightsViewModel by viewModels()

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
                            viewModel.deleteFlight(item.flight.flightId)
                        }
                    }
                })
                snackbar.show()
            }

        }
    }


    private val adapter = FlightsAdapter()

    override fun setOnClickListeners() {
        binding.flightsSwipeRefresh.setOnRefreshListener {
            viewModel.fetchFlights()
        }
        binding.fab.setOnClickListener {
            viewModel.navigateToAddFlight()
//            val uri = Uri.parse(
//                "https://myflightsdev.page.link/?link=https%3A%2F%2Fmyflights.kossa.pl%2Fjoin%3FsharedFlightId%3D1a03424e-0b8b-4085-afc1-94a298fa0305&apn=pl.kossa.myflights"
//            )
//            val intent = Intent(Intent.ACTION_VIEW, uri)
//            startActivity(intent)
        }


        binding.notificationAppBar.setNotificationOnClickListener {
            viewModel.navigateToPendingFlights()
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
                binding.flightsSwipeRefresh.isRefreshing = it
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
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
            viewModel.navigateToFlightDetails(it.flight.flightId)
        }
        binding.flightsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.flightsRecyclerView.adapter = adapter
        val itemTouchHelper = ItemTouchHelper(swipeDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.flightsRecyclerView)
    }

    override fun handleApiError(apiError: ApiError) {
        when (apiError.code) {
            HttpCode.NOT_FOUND.code -> {
                viewModel.setToastMessage(R.string.error_flight_not_found)
            }
            HttpCode.INTERNAL_SERVER_ERROR.code -> {
                viewModel.setToastMessage(R.string.unexpected_error)
            }
            HttpCode.FORBIDDEN.code -> {
                viewModel.setToastMessage(R.string.error_forbidden)
            }
            else -> {
                viewModel.setToastMessage(R.string.unexpected_error)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchFlights()
    }
}