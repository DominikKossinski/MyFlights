package pl.kossa.myflights.fragments.flights.select.runway

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.R
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.api.responses.HttpCode
import pl.kossa.myflights.architecture.fragments.BaseFragment
import pl.kossa.myflights.databinding.FragmentRunwaySelectBinding
import pl.kossa.myflights.fragments.airports.runways.adapter.RunwaysAdapter

@AndroidEntryPoint
class RunwaySelectFragment : BaseFragment<RunwaySelectViewModel, FragmentRunwaySelectBinding>() {

    override val viewModel: RunwaySelectViewModel by viewModels()
    private val args: RunwaySelectFragmentArgs by navArgs()

    private val adapter = RunwaysAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    override fun setOnClickListeners() {
        binding.backAppbar.setBackOnClickListener {
            viewModel.navigateBack()
        }
    }

    override fun collectFlow() {
        super.collectFlow()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.airport.collect {
                it?.let {
                    binding.noRunwaysTextView.isVisible = it.runways.isEmpty()
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
                putString(RUNWAY_ID_KEY, it.runway.runwayId)
                putString(RUNWAY_NAME_KEY, it.runway.name)
            }
            parentFragmentManager.setFragmentResult(args.key, bundle)
            viewModel.navigateBack()
        }
    }

    override fun handleApiError(apiError: ApiError) {
        when (apiError.code) {
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

    companion object {
        const val DEPARTURE_RUNWAY_KEY = "DEPARTURE_RUNWAY"
        const val ARRIVAL_RUNWAY_KEY = "ARRIVAL_RUNWAY"

        const val RUNWAY_ID_KEY = "RUNWAY_ID"
        const val RUNWAY_NAME_KEY = "RUNWAY_NAME"
    }
}