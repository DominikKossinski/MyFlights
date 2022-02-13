package pl.kossa.myflights.fragments.fill_profile

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import pl.kossa.myflights.api.models.User
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentFillProfileBinding
import pl.kossa.myflights.exstensions.doOnTextChanged

@AndroidEntryPoint
class FillProfileFragment : BaseFragment<FillProfileViewModel, FragmentFillProfileBinding>() {

    override val viewModel: FillProfileViewModel by viewModels()


    override fun onResume() {
        super.onResume()
        viewModel.fetchUser()
    }

    override fun setOnClickListeners() {
        binding.nickTie.doOnTextChanged { text ->
            viewModel.setNick(text)
        }
        binding.regulationsCheckBox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setRegulationsAccepted(isChecked)
        }
        binding.createAccountButton.setOnClickListener {
            viewModel.createAccount()
        }
    }

    override fun collectFlow() {
        super.collectFlow()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.user.collectLatest {
                it?.let { setupUserData(it) }
            }
        }
        // TODO loading
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isCreateAccountEnabled.collectLatest {
                binding.createAccountButton.isEnabled = it
            }
        }
    }

    private fun setupUserData(user: User) {
        binding.nickTie.setText(user.nick)
        binding.regulationsCheckBox.isChecked = user.regulationsAccepted
    }

    override fun handleApiError(apiError: ApiError) {
        Log.e("MyLog", "ApiError: $apiError")
        TODO("Not yet implemented")
    }
}