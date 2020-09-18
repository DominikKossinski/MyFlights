package pl.kossa.myflights.fragments.airplanes.details

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_airplane_details.*
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentAirplaneDetailsBinding

class AirplaneDetailsFragment :
    BaseFragment<FragmentAirplaneDetailsBinding, AirplaneDetailsViewModel>() {

    override val layoutId = R.layout.fragment_airplane_details

    private val args by navArgs<AirplaneDetailsFragmentArgs>()

    override val viewModel by lazy {
        AirplaneDetailsViewModel(
            args.airplaneId,
            findNavController(),
            preferencesHelper
        )
    }

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
        viewModel.fetchAirplane()
    }

    override fun setBindingVariables(binding: FragmentAirplaneDetailsBinding) {
        binding.viewModel = viewModel
    }

    override fun setObservers() {
        super.setObservers()
        viewModel.airplaneLiveData.observe(viewLifecycleOwner) {
            viewModel.airplane = it
        }
        viewModel.isLoadingData.observe(viewLifecycleOwner) {
            airplaneSwipeRefresh.isRefreshing = it
        }
    }

    override fun setOnClickListeners() {

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