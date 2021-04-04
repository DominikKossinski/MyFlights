package pl.kossa.myflights.fragments.airports.details

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_airport_details.*
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentAirportDetailsBinding

@AndroidEntryPoint
class AirportDetailsFragment : BaseFragment<AirportDetailsViewModel>() {

    override val layoutId = R.layout.fragment_airport_details

    override val viewModel: AirportDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()

    }

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener {
            viewModel.navigateBack()
        }
        toolbar.setOnMenuItemClickListener {
            val id = it.itemId
            onMenuItemClick(id)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchAirport()
    }

    override fun setObservers() {
        super.setObservers()
        viewModel.airportLiveData.observe(viewLifecycleOwner) {
            viewModel.airport = it
        }
        viewModel.isLoadingData.observe(viewLifecycleOwner) {
            airportSwipeRefresh.isRefreshing = it
        }
    }

    override fun setOnClickListeners() {

    }

    private fun onMenuItemClick(id: Int): Boolean {
        when (id) {
            R.id.actionDelete -> showDeleteDialog()
            R.id.actionEdit -> viewModel.navigateToAirportEdit()
        }
        return true
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