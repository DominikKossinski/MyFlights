package pl.kossa.myflights.fragments.emailresend

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.R
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.api.responses.HttpCode
import pl.kossa.myflights.architecture.fragments.BaseFragment
import pl.kossa.myflights.databinding.FragmentEmailResendBinding

@AndroidEntryPoint
class EmailResendFragment : BaseFragment<EmailResendViewModel, FragmentEmailResendBinding>() {

    override val viewModel: EmailResendViewModel by viewModels()

    override fun setOnClickListeners() {
        binding.resendButton.setOnClickListener {
            viewModel.resendEmail()
        }
        binding.loginButton.setOnClickListener {
            viewModel.navigateToLogin()
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