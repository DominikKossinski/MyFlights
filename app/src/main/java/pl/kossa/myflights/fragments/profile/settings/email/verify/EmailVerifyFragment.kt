package pl.kossa.myflights.fragments.profile.settings.email.verify

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
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
}