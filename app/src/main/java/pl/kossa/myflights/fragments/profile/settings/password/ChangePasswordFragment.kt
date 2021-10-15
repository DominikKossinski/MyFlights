package pl.kossa.myflights.fragments.profile.settings.password

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentChangePasswordBinding
import pl.kossa.myflights.exstensions.doOnTextChanged

@AndroidEntryPoint
class ChangePasswordFragment :
    BaseFragment<ChangePasswordViewModel, FragmentChangePasswordBinding>() {

    override val viewModel: ChangePasswordViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.progressBar
    }

    override fun setOnClickListeners() {
        binding.backAppbar.setBackOnClickListener {
            viewModel.navigateBack()
        }
        binding.saveButton.setOnClickListener {
            viewModel.changePassword()
        }

        binding.passwordTie.doOnTextChanged {
            viewModel.setPassword(it)
        }
        binding.newPasswordTie.doOnTextChanged {
            viewModel.setNewPassword(it)
        }
        binding.confirmNewPasswordTie.doOnTextChanged {
            viewModel.setConfirmNewPassword(it)
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
            viewModel.passwordError.collect {
                binding.passwordTil.error = it?.let { getString(it) }
            }
        }
        lifecycleScope.launch {
            viewModel.newPasswordError.collect {
                binding.newPasswordTil.error = it?.let { getString(it) }
            }
        }
    }
}