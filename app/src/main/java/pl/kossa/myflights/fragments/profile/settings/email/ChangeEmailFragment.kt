package pl.kossa.myflights.fragments.profile.settings.email

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.kossa.myflights.api.models.User
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

    override fun setObservers() {
        super.setObservers()
        collectFlow()
    }

    private fun collectFlow() {
        lifecycleScope.launch {
            viewModel.isSaveButtonEnabled.collect {
                binding.saveButton.isEnabled = it
            }
        }
        lifecycleScope.launch {
            viewModel.user.collect {
                it?.let { setupUserData(it) }
            }
        }
        lifecycleScope.launch {
            viewModel.passwordError.collect {
                binding.passwordTil.error = it?.let { getString(it) }
            }
        }
        lifecycleScope.launch {
            viewModel.newEmailError.collect {
                binding.newEmailTil.error = it?.let { getString(it) }
            }
        }
    }

    private fun setupUserData(user: User) {
        binding.emailTv.text = user.email
    }
}