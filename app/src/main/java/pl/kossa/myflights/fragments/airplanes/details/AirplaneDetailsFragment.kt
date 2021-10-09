package pl.kossa.myflights.fragments.airplanes.details

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.Airplane
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentAirplaneDetailsBinding

@AndroidEntryPoint
class AirplaneDetailsFragment :
    BaseFragment<AirplaneDetailsViewModel, FragmentAirplaneDetailsBinding>() {

    override val viewModel: AirplaneDetailsViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        viewModel.fetchAirplane()
    }

    override fun setObservers() {
        super.setObservers()
        viewModel.airplaneLiveData.observe(viewLifecycleOwner) {
            setupAirplaneData(it)
        }
        viewModel.isLoadingData.observe(viewLifecycleOwner) {
            binding.airplaneSwipeRefresh.isRefreshing = it
        }
    }

    override fun setOnClickListeners() {
        binding.detailsAppbar.setBackOnClickListener {
            viewModel.navigateBack()
        }
        binding.detailsAppbar.setEditOnClickListener {
            viewModel.navigateToAirplaneEdit()
        }
        binding.detailsAppbar.setDeleteOnClickListener {
            showDeleteDialog()
        }

    }

    private fun setupAirplaneData(airplane: Airplane) {
        binding.nameEwt.valueText = airplane.name
        binding.maxSpeedEtw.valueText = airplane.maxSpeed?.toString() ?: ""
        binding.weightEwt.valueText = airplane.weight?.toString() ?: ""
    }

    private fun showDeleteDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(R.string.airplane_delete_question_info)
        builder.setPositiveButton(
            R.string.delete
        ) { dialog, _ ->
            viewModel.deleteAirplane()
            dialog?.dismiss()
        }
        builder.setNegativeButton(R.string.cancel) { dialog, _ ->
            dialog?.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

}