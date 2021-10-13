package pl.kossa.myflights.fragments.flights.select.airplane

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentAirplaneSelectBinding
import pl.kossa.myflights.exstensions.doOnTextChanged
import pl.kossa.myflights.fragments.airplanes.adapter.AirplanesAdapter

@AndroidEntryPoint
class AirplaneSelectFragment :
    BaseFragment<AirplaneSelectViewModel, FragmentAirplaneSelectBinding>() {

    override val viewModel: AirplaneSelectViewModel by viewModels()

    private val adapter = AirplanesAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putString(AIRPLANE_ID_KEY, it.airplaneId)
                putString(AIRPLANE_NAME_KEY, it.name)
            }
            parentFragmentManager.setFragmentResult(AIRPLANE_KEY, bundle)
            viewModel.navigateBack()
        }
        binding.airplanesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.airplanesRecyclerView.adapter = adapter
    }

    override fun setOnClickListeners() {
        binding.backAppbar.setBackOnClickListener {
            viewModel.navigateBack()
        }
        binding.searchTie.doOnTextChanged { text ->
            viewModel.fetchAirplanes(text)
        }
    }

    override fun setObservers() {
        super.setObservers()
        viewModel.isLoadingData.observe(viewLifecycleOwner) {
            binding.airplanesSwipeRefresh.isRefreshing = it
        }
        collectFlow()
    }

    private fun collectFlow() {
        lifecycleScope.launch {
            viewModel.airplanesList.collect {
                binding.noAirplanesTextView.isVisible = it.isEmpty()
                adapter.items.clear()
                adapter.items.addAll(it)
                adapter.notifyDataSetChanged()
            }
        }
    }

    companion object {
        const val AIRPLANE_KEY = "AIRPLANE"
        const val AIRPLANE_ID_KEY = "AIRPLANE_ID"
        const val AIRPLANE_NAME_KEY = "AIRPLANE_NAME"
    }
}