package pl.kossa.myflights.fragments.prelogin

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.R
import pl.kossa.myflights.api.server.responses.ApiError
import pl.kossa.myflights.api.server.responses.HttpCode
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentPreLoginBinding

@AndroidEntryPoint
class PreLoginFragment: BaseFragment<PreLoginViewModel, FragmentPreLoginBinding>() {

    override val viewModel: PreLoginViewModel by viewModels()

    override fun setOnClickListeners() {
        binding.loginButton.setOnClickListener {
            viewModel.navigateToLogin()
        }

        binding.createAccountButton.setOnClickListener {
            viewModel.navigateToCrateAccount()
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
    }
}