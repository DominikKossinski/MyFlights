package pl.kossa.myflights.fragments.profile

import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.User
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.api.responses.HttpCode
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentProfileBinding

@AndroidEntryPoint
class ProfileFragment : BaseFragment<ProfileViewModel, FragmentProfileBinding>() {

    override val viewModel: ProfileViewModel by viewModels()

    override fun setOnClickListeners() {
        binding.statsButton.setOnClickListener {
            //TODO statistics display
            viewModel.showComingSoonDialog()
        }
        binding.settingsButton.setOnClickListener {
            viewModel.navigateToSettings()
        }
        binding.signOutButton.setOnClickListener {
            viewModel.signOut()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchUser()
    }

    override fun collectFlow() {
        super.collectFlow()
        lifecycleScope.launch {
            viewModel.user.collect {
                it?.let { setupUserData(it) }
            }
        }
    }

    private fun setupUserData(user: User) {
        binding.nameTv.text = if (user.nick.isBlank()) {
            getString(R.string.no_nick)
        } else user.nick

        val textColor = if (user.nick.isBlank()) {
            ContextCompat.getColor(requireContext(), R.color.color_delete)
        } else ContextCompat.getColor(requireContext(), R.color.black_day_night)
        binding.nameTv.setTextColor(textColor)
        binding.emailTv.text = user.email
    }

    override fun handleApiError(apiError: ApiError) {
        when(apiError.code) {
            HttpCode.INTERNAL_SERVER_ERROR.code -> {
                viewModel.setToastError( R.string.unexpected_error)
            }
            HttpCode.FORBIDDEN.code -> {
                viewModel.setToastError( R.string.error_forbidden)
            }
            else -> {
                viewModel.setToastError( R.string.unexpected_error)
            }
        }
    }
}