package pl.kossa.myflights.fragments.profile.avatar

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import pl.kossa.myflights.R
import pl.kossa.myflights.api.server.responses.ApiError
import pl.kossa.myflights.architecture.BaseFragment
import pl.kossa.myflights.databinding.FragmentAcceptAvatarBinding
import pl.kossa.myflights.exstensions.getFilePath
import java.io.File

@AndroidEntryPoint
class AcceptAvatarFragment : BaseFragment<AcceptAvatarViewModel, FragmentAcceptAvatarBinding>() {

    override val viewModel: AcceptAvatarViewModel by viewModels()

    override fun setOnClickListeners() {
        viewModel.imageUri.getFilePath(requireContext())?.let {
            viewModel.setFilePath(it)
            Glide.with(this)
                .load(File(it))
                .placeholder(R.drawable.ic_profile)
                .transform(CircleCrop())
                .into(binding.profileIv)
        }
        binding.cancelButton.setOnClickListener {
            viewModel.navigateBack()
        }
        binding.saveButton.setOnClickListener {
            viewModel.saveAvatar(requireContext())
        }
        progressBar = binding.progressBar
    }

    override fun collectFlow() {
        super.collectFlow()
       viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isSaveButtonEnabled.collect {
                binding.saveButton.isEnabled = it
            }
        }
    }

    override fun handleApiError(apiError: ApiError) {
        viewModel.setToastMessage( R.string.unexpected_error)
    }
}