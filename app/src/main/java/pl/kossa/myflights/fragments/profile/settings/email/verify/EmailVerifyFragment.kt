package pl.kossa.myflights.fragments.profile.settings.email.verify

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.R
import pl.kossa.myflights.api.server.responses.ApiError
import pl.kossa.myflights.api.server.responses.HttpCode
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentEmailVerifyBinding

@AndroidEntryPoint
class EmailVerifyFragment : BaseFragment<EmailVerifyViewModel, FragmentEmailVerifyBinding>() {

    override val viewModel: EmailVerifyViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.loadingProgressBar
    }

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