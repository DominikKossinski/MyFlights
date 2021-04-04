package pl.kossa.myflights.fragments.airplanes

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_airplanes.*
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.Airplane
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.architecture.BaseSwipeDeleteCallback
import pl.kossa.myflights.databinding.FragmentAirplanesBinding
import pl.kossa.myflights.fragments.airplanes.adapter.AirplaneViewModel
import pl.kossa.myflights.fragments.airplanes.adapter.AirplanesAdapter

@AndroidEntryPoint
class AirplanesFragment : BaseFragment<AirplanesViewModel>() {

    override val layoutId = R.layout.fragment_airplanes

    override val viewModel: AirplanesViewModel by viewModels()

    private val swipeDeleteCallback by lazy {
        object : BaseSwipeDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val item = adapter.items[position]
                adapter.items.remove(item)
                adapter.notifyItemRemoved(position)
                val snackbar = Snackbar.make(
                    coordinatorLayout,
                    R.string.airplane_delete_question,
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
                            viewModel.deleteAirplane(item.model.airplaneId)
                        }
                    }
                })
                snackbar.show()
            }
        }
    }

    private val adapter = AirplanesAdapter()

    override fun setOnClickListeners() {
        airplanesSwipeRefresh.setOnRefreshListener {
            viewModel.fetchAirplanes()
        }
        fab.setOnClickListener {
            //TODO viewModel.navigateToAddAirplane()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            adapter.items.addAll(it.map { airplane ->
                object : AirplaneViewModel(airplane) {
                    override fun onRowClick(model: Airplane) {
                        //TODO    viewModel.navigateToAirplaneDetails(model.airplaneId)
                    }
                }
            })
            adapter.notifyDataSetChanged()
        }
    }

    private fun setupRecyclerView() {
        airplanesRecyclerView.layoutManager = LinearLayoutManager(context)
        airplanesRecyclerView.adapter = adapter
        val itemTouchHelper = ItemTouchHelper(swipeDeleteCallback)
        itemTouchHelper.attachToRecyclerView(airplanesRecyclerView)
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchAirplanes()
    }

}