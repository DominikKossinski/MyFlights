package pl.kossa.myflights.fragments.emailresend

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_email_resend.*
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentEmailResendBinding

@AndroidEntryPoint
class EmailResendFragment : BaseFragment<EmailResendViewModel>() {
    override val layoutId: Int = pl.kossa.myflights.R.layout.fragment_email_resend

    override val viewModel: EmailResendViewModel by viewModels()

    override fun setOnClickListeners() {
        resendButton.setOnClickListener {
            viewModel.resendEmail()
        }
        loginButton.setOnClickListener {
            viewModel.navigateToLogin()
        }
    }
}