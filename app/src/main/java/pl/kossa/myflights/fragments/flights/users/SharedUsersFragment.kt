package pl.kossa.myflights.fragments.flights.users

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.architecture.fragments.BaseFragment
import pl.kossa.myflights.databinding.FragmentSharedUsersBinding

@AndroidEntryPoint
class SharedUsersFragment : BaseFragment<SharedUsersViewModel, FragmentSharedUsersBinding>() {

    override val viewModel: SharedUsersViewModel by viewModels()

    override fun setOnClickListeners() {
        // TOOD
    }

    override fun handleApiError(apiError: ApiError) {
        TODO("Not yet implemented")
    }
}