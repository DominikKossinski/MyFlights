package pl.kossa.myflights.fragments.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.kossa.myflights.R
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.api.responses.HttpCode
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentLoginBinding
import pl.kossa.myflights.exstensions.doOnTextChanged

@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>() {

    override val viewModel: LoginViewModel by viewModels()

    override fun setOnClickListeners() {
        binding.loginButton.setOnClickListener {
            viewModel.login()
        }
        binding.createAccountTv.setOnClickListener {
            viewModel.navigateToCreateAccount()
        }
        binding.emailTie.doOnTextChanged { text ->
            viewModel.setEmail(text)
        }
        binding.passwordTie.doOnTextChanged { text ->
            viewModel.setPassword(text)
        }
    }

    override fun collectFlow() {
        super.collectFlow()
        lifecycleScope.launch {
            viewModel.isLoginButtonEnabled.collect {
                binding.loginButton.isEnabled = it
            }
        }
        lifecycleScope.launch {
            viewModel.emailError.collect {
                binding.emailTil.error = it?.let {
                    binding.emailTie.setText("")
                    binding.passwordTie.setText("")
                    getString(it)
                }
            }
        }
        lifecycleScope.launch {
            viewModel.passwordError.collect {
                binding.passwordTil.error = it?.let {
                    binding.passwordTie.setText("")
                    getString(it)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.loadingProgressBar
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