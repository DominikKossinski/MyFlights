package pl.kossa.myflights.dialogs.accountdelete

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.kossa.myflights.architecture.BaseDialog
import pl.kossa.myflights.databinding.DialogAccountDeleteBinding
import pl.kossa.myflights.exstensions.doOnTextChanged

@AndroidEntryPoint
class AccountDeleteDialog: BaseDialog<AccountDeleteViewModel, DialogAccountDeleteBinding>() {

    override val viewModel: AccountDeleteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cancelButton.setOnClickListener {
            viewModel.navigateBack()
        }
        binding.deleteButton.setOnClickListener {
            viewModel.deleteAccount()
        }
        binding.passwordTie.doOnTextChanged {
            viewModel.setPassword(it)
        }
    }

    override fun setObservers() {
        super.setObservers()
        viewModel.isLoadingData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }
        collectFlow()
    }

    private fun collectFlow() {
        lifecycleScope.launch {
            viewModel.isDeleteButtonEnabled.collect {
                binding.deleteButton.isEnabled = it
            }
        }
        lifecycleScope.launch {
            viewModel.passwordError.collect {
                binding.passwordTil.error = it?.let { getString(it) }
            }
        }
    }
}