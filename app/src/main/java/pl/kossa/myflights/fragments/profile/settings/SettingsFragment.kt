package pl.kossa.myflights.fragments.profile.settings

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.BuildConfig
import pl.kossa.myflights.R
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.api.responses.HttpCode
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentSettingsBinding

@AndroidEntryPoint
class SettingsFragment : BaseFragment<SettingsViewModel, FragmentSettingsBinding>() {

    override val viewModel: SettingsViewModel by viewModels()

    override fun setOnClickListeners() {
        binding.backAppbar.setBackOnClickListener {
            viewModel.navigateBack()
        }
        binding.changeNickButton.setOnClickListener {
            viewModel.navigateToChangeNick()
        }
        binding.changeEmailButton.setOnClickListener {
            viewModel.navigateToChangeEmail()
        }
        binding.changePasswordButton.setOnClickListener {
            viewModel.navigateToChangePassword()
        }
        binding.aboutAppButton.setOnClickListener {
            viewModel.navigateToAboutApp()
        }
        binding.deleteAccountButton.setOnClickListener {
            viewModel.showAccountDeleteDialog()
        }
    }

    override fun handleApiError(apiError: ApiError) {
        when(apiError.code) {
            HttpCode.INTERNAL_SERVER_ERROR.code -> {
                viewModel.setToastMessage( R.string.unexpected_error)
            }
            HttpCode.FORBIDDEN.code -> {
                viewModel.setToastMessage( R.string.error_forbidden)
            }
            else -> {
                viewModel.setToastMessage( R.string.unexpected_error)
            }
        }
        viewModel.navigateBack()
    }

}