package pl.kossa.myflights.fragments.flights.select.runway

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentRunwaySelectBinding
import pl.kossa.myflights.fragments.airports.runways.adapter.RunwaysAdapter

@AndroidEntryPoint
class RunwaySelectFragment : BaseFragment<RunwaySelectViewModel, FragmentRunwaySelectBinding>() {

    override val viewModel: RunwaySelectViewModel by viewModels()
    private val args: RunwaySelectFragmentArgs by navArgs()

    private val adapter = RunwaysAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("MyLog", "Runway args: $args")
        setupRecyclerView()
    }

    override fun setOnClickListeners() {
        // TODO("Not yet implemented")
    }

    override fun setObservers() {
        super.setObservers()
        collectFlow()
    }

    private fun collectFlow() {
        lifecycleScope.launch {
            viewModel.airport.collect {
                it?.let {
                    //TODO no runways info
                    adapter.items.clear()
                    adapter.items.addAll(it.runways)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.runwaysRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.runwaysRecyclerView.adapter = adapter
        adapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putString(RUNWAY_ID_KEY, it.runwayId)
                putString(RUNWAY_NAME_KEY, it.name)
            }
            parentFragmentManager.setFragmentResult(args.key, bundle)
            viewModel.navigateBack()
        }
    }

    companion object {
        const val DEPARTURE_RUNWAY_KEY = "DEPARTURE_RUNWAY"
        const val ARRIVAL_RUNWAY_KEY = "ARRIVAL_RUNWAY"

        const val RUNWAY_ID_KEY = "RUNWAY_ID"
        const val RUNWAY_NAME_KEY = "RUNWAY_NAME"
    }
}