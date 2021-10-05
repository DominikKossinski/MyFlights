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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            viewModel.navigateBack()
        }
        binding.toolbar.setOnMenuItemClickListener {
            val id = it.itemId
            onMenuItemClick(id)
        }
    }

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
        //TODO
    }

    private fun setupAirplaneData(airplane: Airplane) {
        //TODO
    }

    private fun onMenuItemClick(id: Int): Boolean {
        when (id) {
            R.id.actionDelete -> showDeleteDialog()
            R.id.actionEdit -> viewModel.navigateToAirplaneEdit()
        }
        return true
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