package pl.kossa.myflights.fragments.profile.settings

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentSettingsBinding

@AndroidEntryPoint
class SettingsFragment : BaseFragment<SettingsViewModel, FragmentSettingsBinding>() {

    override val viewModel: SettingsViewModel by viewModels()

    override fun setOnClickListeners() {
        binding.backAppbar.setBackOnClickListener {
            viewModel.navigateBack()
        }
        binding.changeNickButton.setOnClickListener {
            viewModel.navigateToChangeNick()
        }
        binding.changeEmailButton.setOnClickListener {
            viewModel.navigateToChangeEmail()
        }
        binding.changePasswordButton.setOnClickListener {
            viewModel.navigateToChangePassword()
        }
        binding.changeAvatarButton.setOnClickListener {
            //TODO changing avatar
            viewModel.showComingSoonDialog()
        }
        binding.deleteAccountButton.setOnClickListener {
            viewModel.showAccountDeleteDialog()
        }
    }


}