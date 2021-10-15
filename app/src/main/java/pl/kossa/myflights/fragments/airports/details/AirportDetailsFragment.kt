package pl.kossa.myflights.fragments.airports.details

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.Airport
import pl.kossa.myflights.architecture.BaseFragment
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

    override fun setObservers() {
        super.setObservers()
        viewModel.airportLiveData.observe(viewLifecycleOwner) {
            setupAirportData(it)
        }
        viewModel.isLoadingData.observe(viewLifecycleOwner) {
            binding.airportSwipeRefresh.isRefreshing = it
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
        binding.towerFrequencyEwt.isVisible = airport.towerFrequency != null
        binding.towerFrequencyEwt.valueText = airport.towerFrequency ?: ""
        binding.groundFrequencyEwt.isVisible = airport.groundFrequency != null
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


}