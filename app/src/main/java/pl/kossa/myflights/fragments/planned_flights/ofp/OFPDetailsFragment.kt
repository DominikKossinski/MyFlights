package pl.kossa.myflights.fragments.planned_flights.ofp

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.api.server.responses.ApiError
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentOfpDetailsBinding

@AndroidEntryPoint
class OFPDetailsFragment: BaseFragment<OFPDetailsViewModel, FragmentOfpDetailsBinding>() {

    override val viewModel: OFPDetailsViewModel by viewModels()

    override fun setOnClickListeners() {
        //TODO
    }

    override fun handleApiError(apiError: ApiError) {
        TODO("Not yet implemented")
    }
}