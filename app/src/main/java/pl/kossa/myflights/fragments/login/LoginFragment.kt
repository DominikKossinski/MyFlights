package pl.kossa.myflights.fragments.login

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.R
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.api.responses.HttpCode
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentLoginBinding
import pl.kossa.myflights.exstensions.doOnTextChanged

@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>() {

    override val viewModel: LoginViewModel by viewModels()

    private val googleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    val googleSignInActivityLauncher = this.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            //TODO handle firebase auth
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            accountTask.addOnSuccessListener {
                firebaseAuthWithGoogleAccount(it)
            }.addOnFailureListener {
                viewModel.setToastMessage(R.string.error_sign_in_with_google)
            }
        } else {
            viewModel.setToastMessage(R.string.error_sign_in_with_google)
        }
    }

    private val googleSignInClient by lazy {
        GoogleSignIn.getClient(requireActivity(), googleSignInOptions)
    }

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
        binding.googleSignInButton.setOnClickListener {
            signInWithGoogle()
        }
    }

    override fun collectFlow() {
        super.collectFlow()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isLoginButtonEnabled.collect {
                binding.loginButton.isEnabled = it
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.emailError.collect {
                binding.emailTil.error = it?.let {
                    binding.emailTie.setText("")
                    binding.passwordTie.setText("")
                    getString(it)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.passwordError.collect {
                binding.passwordTil.error = it?.let {
                    binding.passwordTie.setText("")
                    getString(it)
                }
            }
        }
    }

    private fun signInWithGoogle() {
        val intent = googleSignInClient.signInIntent
        googleSignInActivityLauncher.launch(intent)
    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        viewModel.signInWithCredential(credential)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.loadingProgressBar
    }

    override fun handleApiError(apiError: ApiError) {
        when (apiError.code) {
            HttpCode.INTERNAL_SERVER_ERROR.code -> {
                viewModel.setToastMessage(R.string.unexpected_error)
            }
            HttpCode.FORBIDDEN.code -> {
                viewModel.setToastMessage(R.string.error_forbidden)
            }
            else -> {
                viewModel.setToastMessage(R.string.unexpected_error)
            }
        }
    }
}