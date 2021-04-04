package pl.kossa.myflights.fragments.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentLoginBinding

@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginViewModel>() {

    override val layoutId: Int = R.layout.fragment_login

    override val viewModel : LoginViewModel by viewModels()

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