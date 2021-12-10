package pl.kossa.myflights.fragments.flights.select.airport

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import pl.kossa.myflights.R
import pl.kossa.myflights.api.server.responses.ApiError
import pl.kossa.myflights.api.server.responses.HttpCode
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
        binding.backAppbar.setBackOnClickListener {
            viewModel.navigateBack()
        }
        binding.searchTie.doOnTextChanged { text ->
            viewModel.fetchAirports(text)
        }
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

    override fun collectFlow() {
        super.collectFlow()
       viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.airportsList.collect {
                binding.noAirportsTextView.isVisible = it.isEmpty()
                adapter.items.clear()
                adapter.items.addAll(it)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun handleApiError(apiError: ApiError) {
        when(apiError.code) {
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
        viewModel.navigateBack()
    }

    companion object {
        const val DEPARTURE_AIRPORT_KEY = "DEPARTURE_AIRPORT"
        const val ARRIVAL_AIRPORT_KEY = "ARRIVAL_AIRPORT"

        const val AIRPORT_ID_KEY = "AIRPORT_ID"
        const val AIRPORT_ICAO_CODE_KEY = "AIRPORT_ICAO_CODE"
        const val AIRPORT_NAME_KEY = "AIRPORT_NAME"
    }
}