package pl.kossa.myflights.fragments.profile.settings.nick

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentChangeNickBinding
import pl.kossa.myflights.exstensions.doOnTextChanged

@AndroidEntryPoint
class ChangeNickFragment : BaseFragment<ChangeNickViewModel, FragmentChangeNickBinding>() {

    override val viewModel: ChangeNickViewModel by viewModels()

    override fun setOnClickListeners() {
        binding.backAppbar.setBackOnClickListener {
            viewModel.navigateBack()
        }
        binding.nickTie.doOnTextChanged {
            viewModel.setNick(it)
        }
        binding.saveButton.setOnClickListener {
            viewModel.putUser()
        }
    }

    override fun setObservers() {
        super.setObservers()
        collectFlow()
    }

    private fun collectFlow() {
        lifecycleScope.launch {
            viewModel.user.collect {
                it?.let { binding.nickTie.setText(it.nick) }
            }
        }
        lifecycleScope.launch {
            viewModel.isSaveButtonEnabled.collect {
                binding.saveButton.isEnabled = it
            }
        }
    }
}