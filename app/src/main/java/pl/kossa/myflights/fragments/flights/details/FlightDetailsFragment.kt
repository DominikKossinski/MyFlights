package pl.kossa.myflights.fragments.flights.details

import android.app.AlertDialog
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentFlightDetailsBinding

@AndroidEntryPoint
class FlightDetailsFragment : BaseFragment<FlightDetailsViewModel, FragmentFlightDetailsBinding>() {

    override val viewModel: FlightDetailsViewModel by viewModels()


    override fun setOnClickListeners() {
        binding.detailsAppbar.setBackOnClickListener {
            viewModel.navigateBack()
        }
        binding.detailsAppbar.setEditOnClickListener {
            viewModel.navigateToFlightEdit()
        }
        binding.detailsAppbar.setDeleteOnClickListener {
            showDeleteDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchFlight()
    }

    private fun showDeleteDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(R.string.flight_delete_question_info)
        builder.setPositiveButton(
            R.string.delete
        ) { dialog, _ ->
            viewModel.deleteFlight()
            dialog?.dismiss()
        }
        builder.setNegativeButton(R.string.cancel) { dialog, _ ->
            dialog?.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

}