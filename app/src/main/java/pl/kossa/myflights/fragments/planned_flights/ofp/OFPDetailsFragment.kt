package pl.kossa.myflights.fragments.planned_flights.ofp

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import pl.kossa.myflights.R
import pl.kossa.myflights.api.server.responses.ApiError
import pl.kossa.myflights.api.simbrief.models.OFP
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentOfpDetailsBinding

@AndroidEntryPoint
class OFPDetailsFragment : BaseFragment<OFPDetailsViewModel, FragmentOfpDetailsBinding>() {

    override val viewModel: OFPDetailsViewModel by viewModels()

    override fun setOnClickListeners() {
        binding.backAppbar.setBackOnClickListener {
            viewModel.navigateBack()
        }
    }

    override fun collectFlow() {
        super.collectFlow()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isLoadingData.collectLatest {
                binding.ofpSwipeRefresh.isRefreshing = it
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.ofpFlow.collectLatest {
                it?.let { setupOFPData(it) }
            }
        }
    }

    private fun setupOFPData(ofp: OFP) {
        binding.originEwt.valueText = ofp.origin?.icaoCode ?: ""
        binding.destEtw.valueText = ofp.destination?.icaoCode ?: ""
        binding.routeEtw.valueText = ofp.general?.route ?: ""
        binding.alternateEtw.valueText = ofp.alternate?.airport?.icaoCode ?: ""
        binding.alternateRouteEtw.valueText = ofp.alternate?.route ?: ""
    }

    override fun handleApiError(apiError: ApiError) {
        //TODO
        when(apiError.code) {
            404 -> {
                viewModel.setToastMessage(R.string.error_ofp_not_found)
            }
            else -> {
                viewModel.setToastMessage(R.string.unexpected_error)
            }
        }
        viewModel.navigateBack()
        Log.d("MyLog", "OFP: $apiError")
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchOFP()
    }
}