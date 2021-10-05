package pl.kossa.myflights.fragments.profile

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentProfileBinding

@AndroidEntryPoint
class ProfileFragment : BaseFragment<ProfileViewModel, FragmentProfileBinding>() {

    override val viewModel: ProfileViewModel by viewModels()

    override fun setOnClickListeners() {
        binding.signOutButton.setOnClickListener {
            viewModel.signOut()
        }
    }
}