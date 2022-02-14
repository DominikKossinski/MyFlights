package pl.kossa.myflights.fragments.airports.details

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.Airport
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.api.responses.HttpCode
import pl.kossa.myflights.architecture.fragments.BaseFragment
import pl.kossa.myflights.databinding.FragmentAirportDetailsBinding
import pl.kossa.myflights.fragments.airports.runways.adapter.RunwaysAdapter

@AndroidEntryPoint
class AirportDetailsFragment :
    BaseFragment<AirportDetailsViewModel, FragmentAirportDetailsBinding>() {


    override val viewModel: AirportDetailsViewModel by viewModels()

    private val runwaysAdapter = RunwaysAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupRecyclerView()
    }

    private fun setupToolbar() {
        binding.backAppbar.setBackOnClickListener {
            viewModel.navigateBack()
        }
        binding.backAppbar.setDeleteOnClickListener {
            showDeleteDialog()
        }

        binding.editButton.setOnClickListener {
            viewModel.navigateToAirportEdit()
        }

    }

    private fun setupRecyclerView() {
        runwaysAdapter.setOnItemClickListener {
            viewModel.navigateToRunwayDetails(it.runwayId)
        }
        binding.runwaysRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.runwaysRecyclerView.adapter = runwaysAdapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchAirport()
    }

    override fun collectFlow() {
        super.collectFlow()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isLoadingData.collect {
                binding.airportSwipeRefresh.isRefreshing = it
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.airport.collect {
                it?.let { setupAirportData(it) }
            }
        }
    }

    override fun setOnClickListeners() {
        binding.airportSwipeRefresh.setOnRefreshListener {
            viewModel.fetchAirport()
        }
        binding.addRunwayButton.setOnClickListener {
            viewModel.navigateToRunwayAdd()
        }
    }

    private fun setupAirportData(airport: Airport) {
        binding.nameEwt.valueText = airport.name
        binding.cityEwt.valueText = airport.city
        binding.icaoCodeEwt.valueText = airport.icaoCode
        binding.towerFrequencyEwt.isVisible = !airport.towerFrequency.isNullOrBlank()
        binding.towerFrequencyEwt.valueText = airport.towerFrequency ?: ""
        binding.groundFrequencyEwt.isVisible = !airport.groundFrequency.isNullOrBlank()
        binding.groundFrequencyEwt.valueText = airport.groundFrequency ?: ""
        binding.runwaysTv.isVisible = airport.runways.isNotEmpty()
        binding.runwaysRecyclerView.isVisible = airport.runways.isNotEmpty()
        runwaysAdapter.items.clear()
        runwaysAdapter.items.addAll(airport.runways)
        runwaysAdapter.notifyDataSetChanged()
    }

    private fun showDeleteDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(R.string.airport_delete_question_info)
        builder.setPositiveButton(
            R.string.delete
        ) { dialog, _ ->
            viewModel.deleteAirport()
            dialog?.dismiss()
        }
        builder.setNegativeButton(R.string.cancel) { dialog, _ ->
            dialog?.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun handleApiError(apiError: ApiError) {
        when (apiError.code) {
            HttpCode.BAD_REQUEST.code -> {
                viewModel.setToastMessage(R.string.error_airport_exists_in_flights)
            }
            HttpCode.NOT_FOUND.code -> {
                viewModel.setToastMessage(R.string.error_airport_not_found)
                viewModel.navigateBack()
            }
            HttpCode.INTERNAL_SERVER_ERROR.code -> {
                viewModel.setToastMessage(R.string.unexpected_error)
                viewModel.navigateBack()
            }
            HttpCode.FORBIDDEN.code -> {
                viewModel.setToastMessage(R.string.error_forbidden)
                viewModel.navigateBack()
            }
            else -> {
                viewModel.setToastMessage(R.string.unexpected_error)
                viewModel.navigateBack()
            }
        }
    }


}