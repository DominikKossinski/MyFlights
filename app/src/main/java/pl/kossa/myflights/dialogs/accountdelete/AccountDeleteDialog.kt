package pl.kossa.myflights.dialogs.accountdelete

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.ProviderType
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.api.responses.HttpCode
import pl.kossa.myflights.architecture.dialogs.GoogleAuthDialog
import pl.kossa.myflights.databinding.DialogAccountDeleteBinding
import pl.kossa.myflights.exstensions.doOnTextChanged

@AndroidEntryPoint
class AccountDeleteDialog : GoogleAuthDialog<AccountDeleteViewModel, DialogAccountDeleteBinding>() {

    override val viewModel: AccountDeleteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cancelButton.setOnClickListener {
            viewModel.navigateBack()
        }
        binding.deleteButton.setOnClickListener {
            when (viewModel.getAuthProviderType()) {
                ProviderType.PASSWORD -> viewModel.deleteAccount()
                ProviderType.GOOGLE -> {
                    launchGoogleSignInIntent()
                }

                null -> TODO()
            }
        }
        binding.passwordTie.doOnTextChanged {
            viewModel.setPassword(it)
        }
    }

    override fun collectFlow() {
        super.collectFlow()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isLoadingData.collect {
                binding.progressBar.isVisible = it
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isDeleteButtonEnabled.collect {
                binding.deleteButton.isEnabled = it
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.passwordError.collect {
                binding.passwordTil.error = it?.let { getString(it) }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.providerType.collectLatest {
                binding.passwordTil.isVisible = it == ProviderType.PASSWORD
            }
        }
    }

    override fun handleApiError(apiError: ApiError) {
        when (apiError.code) {
            HttpCode.INTERNAL_SERVER_ERROR.code -> {
                viewModel.setToastMessage(R.string.unexpected_error)
                viewModel.navigateBack()
            }
            HttpCode.FORBIDDEN.code -> {
                viewModel.setToastMessage(R.string.error_forbidden)
                viewModel.navigateBack()
            }
            else -> {
                viewModel.setToastMessage(R.string.unexpected_error)
                viewModel.navigateBack()
            }
        }
    }

    override fun onGoogleAuthSuccess(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        viewModel.deleteUserWithCredential(credential)
    }

}