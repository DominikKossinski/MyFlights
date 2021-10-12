package pl.kossa.myflights.fragments.createAccount

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentCreateAccountBinding
import pl.kossa.myflights.exstensions.doOnTextChanged

@AndroidEntryPoint
class CreateAccountFragment : BaseFragment<CreateAccountViewModel, FragmentCreateAccountBinding>() {

    override val viewModel: CreateAccountViewModel by viewModels()

    override fun setOnClickListeners() {
        binding.regulationsTv.setOnClickListener {
            //TODO show regulations
        }
        binding.backTv.setOnClickListener {
            viewModel.navigateBack()
        }
        binding.createAccountButton.setOnClickListener {
            viewModel.createAccount()
        }


        binding.emailTie.doOnTextChanged { text ->
            viewModel.setEmail(text)
        }
        binding.passwordTie.doOnTextChanged { text ->
            viewModel.setPassword(text)
        }
        binding.confirmPasswordTie.doOnTextChanged { text ->
            viewModel.setConfirmPassword(text)
        }

        binding.regulationsCheckBox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setRegulationsAccepted(isChecked)
        }
    }

    override fun setObservers() {
        super.setObservers()
        collectFlow()
    }

    private fun collectFlow() {
        lifecycleScope.launch {
            viewModel.isCreateAccountButtonEnabled.collect {
                binding.createAccountButton.isEnabled = it
            }
        }
        lifecycleScope.launch {
            viewModel.emailError.collect {
                binding.emailTil.error = it?.let {
                    binding.emailTie.setText("")
                    binding.passwordTie.setText("")
                    binding.confirmPasswordTie.setText("")
                    getString(it)
                }
            }
        }
        lifecycleScope.launch {
            viewModel.passwordError.collect {
                binding.passwordTil.error = it?.let {
                    binding.passwordTie.setText("")
                    binding.confirmPasswordTie.setText("")
                    getString(it)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.loadingProgressBar
    }


}