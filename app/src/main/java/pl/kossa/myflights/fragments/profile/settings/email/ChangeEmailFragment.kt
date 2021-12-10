package pl.kossa.myflights.fragments.profile.settings.email

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import pl.kossa.myflights.R
import pl.kossa.myflights.api.server.models.User
import pl.kossa.myflights.api.server.responses.ApiError
import pl.kossa.myflights.api.server.responses.HttpCode
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentChangeEmailBinding
import pl.kossa.myflights.exstensions.doOnTextChanged

@AndroidEntryPoint
class ChangeEmailFragment : BaseFragment<ChangeEmailViewModel, FragmentChangeEmailBinding>() {

    override val viewModel: ChangeEmailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.progressBar
    }

    override fun setOnClickListeners() {
        binding.backAppbar.setBackOnClickListener {
            viewModel.navigateBack()
        }

        binding.saveButton.setOnClickListener {
            viewModel.changeEmail()
        }

        binding.passwordTie.doOnTextChanged {
            viewModel.setPassword(it)
        }
        binding.newEmailTie.doOnTextChanged {
            viewModel.setNewEmail(it)
        }
        binding.confirmNewPasswordTie.doOnTextChanged {
            viewModel.setConfirmNewEmail(it)
        }
    }

    override fun collectFlow() {
        super.collectFlow()
       viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isSaveButtonEnabled.collect {
                binding.saveButton.isEnabled = it
            }
        }
       viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.user.collect {
                it?.let { setupUserData(it) }
            }
        }
       viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.passwordError.collect {
                binding.passwordTil.error = it?.let { getString(it) }
            }
        }
       viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.newEmailError.collect {
                binding.newEmailTil.error = it?.let { getString(it) }
            }
        }
    }

    private fun setupUserData(user: User) {
        binding.emailTv.text = user.email
    }

    override fun handleApiError(apiError: ApiError) {
        when (apiError.code) {
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
        viewModel.navigateBack()
    }
}