package pl.kossa.myflights.fragments.flights.select.airplane

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.R
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.api.responses.HttpCode
import pl.kossa.myflights.architecture.fragments.BaseFragment
import pl.kossa.myflights.databinding.FragmentAirplaneSelectBinding
import pl.kossa.myflights.exstensions.doOnTextChanged
import pl.kossa.myflights.fragments.airplanes.adapter.AirplanesAdapter
import pl.kossa.myflights.room.entities.Airplane

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
                putString(AIRPLANE_ID_KEY, it.airplane.airplaneId)
                putString(AIRPLANE_NAME_KEY, it.airplane.name)
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

    override fun collectFlow() {
        super.collectFlow()
       viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isLoadingData.collect {
                binding.airplanesSwipeRefresh.isRefreshing = it
            }
        }
       viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.airplanesList.collect {
                binding.noAirplanesTextView.isVisible = it.isEmpty()
                adapter.items.clear()
                adapter.items.addAll(it.map { item -> Airplane.fromApiAirplane(item) })
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun handleApiError(apiError: ApiError) {
        when (apiError.code) {
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
        const val AIRPLANE_KEY = "AIRPLANE"
        const val AIRPLANE_ID_KEY = "AIRPLANE_ID"
        const val AIRPLANE_NAME_KEY = "AIRPLANE_NAME"
    }
}