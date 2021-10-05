package pl.kossa.myflights.fragments.emailresend

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.architecture.BaseFragment
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
}