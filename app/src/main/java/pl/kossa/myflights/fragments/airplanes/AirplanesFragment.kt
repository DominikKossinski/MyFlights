package pl.kossa.myflights.fragments.airplanes

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.architecture.BaseSwipeDeleteCallback
import pl.kossa.myflights.databinding.FragmentAirplanesBinding
import pl.kossa.myflights.fragments.airplanes.adapter.AirplanesAdapter

@AndroidEntryPoint
class AirplanesFragment : BaseFragment<AirplanesViewModel, FragmentAirplanesBinding>() {

    override val viewModel: AirplanesViewModel by viewModels()

    private val swipeDeleteCallback by lazy {
        object : BaseSwipeDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val item = adapter.items[position]
                adapter.items.remove(item)
                adapter.notifyItemRemoved(position)
                val snackbar = Snackbar.make(
                    binding.coordinatorLayout,
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
                            viewModel.deleteAirplane(item.airplaneId)
                        }
                    }
                })
                snackbar.show()
            }
        }
    }

    private val adapter = AirplanesAdapter()

    override fun setOnClickListeners() {
        binding.airplanesSwipeRefresh.setOnRefreshListener {
            viewModel.fetchAirplanes()
        }
        binding.fab.setOnClickListener {
            viewModel.navigateToAddAirplane()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    override fun setObservers() {
        super.setObservers()
        viewModel.isLoadingData.observe(viewLifecycleOwner) {
            binding.airplanesSwipeRefresh.isRefreshing = it
        }
        viewModel.airplanesList.observe(viewLifecycleOwner) {
            binding.noAirplanesTextView.isVisible = it.isEmpty()
            adapter.items.clear()
            adapter.items.addAll(it)
            adapter.notifyDataSetChanged()
        }
    }

    private fun setupRecyclerView() {
        adapter.setOnItemClickListener {
            viewModel.navigateToAirplaneDetails(it.airplaneId)
        }
        binding.airplanesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.airplanesRecyclerView.adapter = adapter
        val itemTouchHelper = ItemTouchHelper(swipeDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.airplanesRecyclerView)
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchAirplanes()
    }

}