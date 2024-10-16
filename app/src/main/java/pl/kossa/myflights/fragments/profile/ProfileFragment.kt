package pl.kossa.myflights.fragments.profile

import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import pl.kossa.myflights.R
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.api.responses.HttpCode
import pl.kossa.myflights.architecture.fragments.BaseFragment
import pl.kossa.myflights.databinding.FragmentProfileBinding

@AndroidEntryPoint
class ProfileFragment : BaseFragment<ProfileViewModel, FragmentProfileBinding>() {

    override val viewModel: ProfileViewModel by viewModels()

    override fun setOnClickListeners() {
        binding.profileSwipeRefresh.setOnRefreshListener {
            viewModel.fetchUser()
        }
        binding.statsButton.setOnClickListener {
            viewModel.navigateToStatistics()
        }
        binding.settingsButton.setOnClickListener {
            viewModel.navigateToSettings()
        }
        binding.signOutButton.setOnClickListener {
            viewModel.logSignOut()
            viewModel.signOut()
        }
        binding.editAvatarIv.setOnClickListener {
            viewModel.showChangeAccountBottomSheet()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchUser()
    }

    override fun collectFlow() {
        super.collectFlow()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isLoadingData.collectLatest {
                binding.profileSwipeRefresh.isRefreshing = it
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.avatar.collectLatest {
                Log.d("MyLog", "Profile $it")
                it?.let { setupAvatar(it) }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.email.collectLatest { email ->
                binding.emailTv.text = email
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.nick.collectLatest { nick ->
                setupNick(nick)
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.buttonText.collectLatest {
                Log.d("MyLog", "Collecting: $it")
                binding.signOutButton.text = it
            }
        }
    }


    private fun setupNick(nick: String) {
        binding.nameTv.text = nick.ifBlank {
            getString(R.string.no_nick)
        }
        val textColor = if (nick.isBlank()) {
            ContextCompat.getColor(requireContext(), R.color.color_delete)
        } else ContextCompat.getColor(requireContext(), R.color.black_day_night)
        binding.nameTv.setTextColor(textColor)
    }

    private fun setupAvatar(avatarUrl: String?) {
        avatarUrl?.let {
            Glide.with(requireContext())
                .load(it)
                .transform(CircleCrop())
                .placeholder(R.drawable.ic_profile)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(binding.profileIv)
        } ?: binding.profileIv.setImageResource(R.drawable.ic_profile)

    }

    override fun handleApiError(apiError: ApiError) {
        when (apiError.code) {
            HttpCode.INTERNAL_SERVER_ERROR.code -> {
                viewModel.setToastMessage(R.string.unexpected_error)
            }
            HttpCode.FORBIDDEN.code -> {
                viewModel.setToastMessage(R.string.error_forbidden)
            }
            else -> {
                viewModel.setToastMessage(R.string.unexpected_error)
            }
        }
    }
}