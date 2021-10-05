package pl.kossa.myflights.fragments.flights.select.airport

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentAirportSelectBinding
import pl.kossa.myflights.exstensions.doOnTextChanged
import pl.kossa.myflights.fragments.airports.adapter.AirportsAdapter

@AndroidEntryPoint
class AirportSelectFragment : BaseFragment<AirportSelectViewModel, FragmentAirportSelectBinding>() {

    override val viewModel: AirportSelectViewModel by viewModels()
    private val args: AirportSelectFragmentArgs by navArgs()

    private val adapter = AirportsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    override fun setOnClickListeners() {
        binding.searchEt.doOnTextChanged { text ->
            viewModel.fetchAirports(text)
        }
    }

    override fun setObservers() {
        super.setObservers()
        collectFlow()
    }

    private fun setupRecyclerView() {
        adapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putString(AIRPORT_ID_KEY, it.airportId)
                putString(AIRPORT_NAME_KEY, it.name)
                putString(AIRPORT_ICAO_CODE_KEY, it.icaoCode)
            }
            parentFragmentManager.setFragmentResult(args.key, bundle)
            viewModel.navigateBack()
        }
        binding.airportsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.airportsRecyclerView.adapter = adapter
    }

    private fun collectFlow() {
        lifecycleScope.launch {
            viewModel.airportsList.collect {
                //TODO no airports info
                adapter.items.clear()
                adapter.items.addAll(it)
                adapter.notifyDataSetChanged()
            }
        }
    }

    companion object {
        const val DEPARTURE_AIRPORT_KEY = "DEPARTURE_AIRPORT"
        const val ARRIVAL_AIRPORT_KEY = "ARRIVAL_AIRPORT"

        const val AIRPORT_ID_KEY = "AIRPORT_ID"
        const val AIRPORT_ICAO_CODE_KEY = "AIRPORT_ICAO_CODE"
        const val AIRPORT_NAME_KEY = "AIRPORT_NAME"
    }
}