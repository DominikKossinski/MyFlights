package pl.kossa.myflights.fragments.login

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_login.*
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentLoginBinding

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {

    override val layoutId: Int = R.layout.fragment_login

    override val viewModel by lazy {
        LoginViewModel(
            findNavController(),
            preferencesHelper
        )
    }

    override fun setBindingVariables(binding: FragmentLoginBinding) {
        binding.viewModel = viewModel
    }

    override fun setOnClickListeners() {
        loginButton.setOnClickListener {
            viewModel.login()
        }
        createAccountButton.setOnClickListener {
            viewModel.navigateToCreateAccount()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = loadingProgressBar
    }
}