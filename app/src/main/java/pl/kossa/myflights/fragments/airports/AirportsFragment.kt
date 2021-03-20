package pl.kossa.myflights.fragments.airports

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_airports.*
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.Airport
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.architecture.BaseSwipeDeleteCallback
import pl.kossa.myflights.databinding.FragmentAirportsBinding
import pl.kossa.myflights.fragments.airports.adapter.AirportViewModel
import pl.kossa.myflights.fragments.airports.adapter.AirportsAdapter

@AndroidEntryPoint
class AirportsFragment : BaseFragment<FragmentAirportsBinding, AirportsViewModel>() {

    override val layoutId = R.layout.fragment_airports

    override val viewModel: AirportsViewModel by viewModels()

    private val swipeDeleteCallback by lazy {
        object : BaseSwipeDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val item = adapter.items[position]
                adapter.items.remove(item)
                adapter.notifyItemRemoved(position)
                val snackbar = Snackbar.make(
                    coordinatorLayout,
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
                            viewModel.deleteAirport(item.model.airportId)
                        }
                    }
                })
                snackbar.show()
            }

        }
    }

    private val adapter = AirportsAdapter()

    override fun setBindingVariables(binding: FragmentAirportsBinding) {
        binding.viewModel = viewModel
    }

    override fun setOnClickListeners() {
        airportsSwipeRefresh.setOnRefreshListener {
            viewModel.fetchAirports()
        }
        fab.setOnClickListener {
            viewModel.navigateToAddAirport()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("MyLog", "Air")
        setupRecyclerView()
    }

    override fun setObservers() {
        super.setObservers()
        viewModel.isLoadingData.observe(viewLifecycleOwner) {
            airportsSwipeRefresh.isRefreshing = it
        }
        viewModel.airportsList.observe(viewLifecycleOwner) {
            viewModel.noAirportsVisibility = it.isEmpty()
            adapter.items.clear()
            adapter.items.addAll(it.map { airport ->
                object : AirportViewModel(airport) {
                    override fun onRowClick(model: Airport) {
                        viewModel.navigateToAirportDetails(model.airportId)
                    }
                }

            })
            adapter.notifyDataSetChanged()
        }
    }

    private fun setupRecyclerView() {
        airportsRecyclerView.layoutManager = LinearLayoutManager(context)
        airportsRecyclerView.adapter = adapter
        val itemTouchHelper = ItemTouchHelper(swipeDeleteCallback)
        itemTouchHelper.attachToRecyclerView(airportsRecyclerView)
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchAirports()
    }
}