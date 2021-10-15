package pl.kossa.myflights.fragments.prelogin

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
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
}