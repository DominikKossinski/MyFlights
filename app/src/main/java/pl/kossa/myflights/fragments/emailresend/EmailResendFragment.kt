package pl.kossa.myflights.fragments.emailresend

import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_email_resend.*
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentEmailResendBinding

class EmailResendFragment : BaseFragment<FragmentEmailResendBinding, EmailResendViewModel>() {
    override val layoutId: Int = pl.kossa.myflights.R.layout.fragment_email_resend

    override val viewModel by lazy {
        EmailResendViewModel(
            findNavController(),
            preferencesHelper
        )
    }

    override fun setBindingVariables(binding: FragmentEmailResendBinding) {
        binding.viewModel = viewModel
    }

    override fun setOnClickListeners() {
        resendButton.setOnClickListener {
            viewModel.resendEmail()
        }
        loginButton.setOnClickListener {
            viewModel.navigateToLogin()
        }
    }
}