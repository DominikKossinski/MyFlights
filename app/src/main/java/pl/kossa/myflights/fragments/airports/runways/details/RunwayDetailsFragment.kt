package pl.kossa.myflights.fragments.airports.runways.details

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.Runway
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentRunwayDetailsBinding

@AndroidEntryPoint
class RunwayDetailsFragment : BaseFragment<RunwayDetailsViewModel, FragmentRunwayDetailsBinding>() {

    override val viewModel: RunwayDetailsViewModel by viewModels()

    override fun setOnClickListeners() {
        binding.runwaySwipeRefresh.setOnRefreshListener {
            viewModel.fetchRunway()
        }
        setupAppBar()
    }

    private fun setupAppBar() {
        binding.detailsAppbar.setBackOnClickListener {
            viewModel.navigateBack()
        }
        binding.detailsAppbar.setEditOnClickListener {
            viewModel.navigateToRunwayEdit()
        }
        binding.detailsAppbar.setDeleteOnClickListener {
            showDeleteDialog()
        }
    }


    override fun setObservers() {
        super.setObservers()
        viewModel.isLoadingData.observe(viewLifecycleOwner) {
            binding.runwaySwipeRefresh.isRefreshing = it
        }
        collectFlow()
    }

    private fun collectFlow() {
        lifecycleScope.launch {
            viewModel.runway.collect {
                it?.let { setupRunwayData(it) }
            }
        }
    }

    private fun setupRunwayData(runway: Runway) {
        binding.runwayNameEwtv.valueText = runway.name
        //TODO add formatting
        binding.runwayLengthEwtv.valueText = runway.length.toString()
        //TODO formatting
        binding.runwayHeadingEwtv.valueText = runway.heading.toString()
        binding.runwayIlsFrequencyEwtv.isVisible = runway.ilsFrequency != null
        binding.runwayIlsFrequencyEwtv.valueText = runway.ilsFrequency ?: ""
    }

    private fun showDeleteDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(R.string.runway_delete_question_info)
        builder.setPositiveButton(
            R.string.delete
        ) { dialog, _ ->
            viewModel.deleteRunway()
            dialog?.dismiss()
        }
        builder.setNegativeButton(R.string.cancel) { dialog, _ ->
            dialog?.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchRunway()
    }
}