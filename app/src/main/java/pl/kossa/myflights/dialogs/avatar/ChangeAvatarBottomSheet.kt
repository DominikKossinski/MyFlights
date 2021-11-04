package pl.kossa.myflights.dialogs.avatar

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.github.florent37.runtimepermission.kotlin.askPermission
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.kossa.myflights.architecture.BaseBottomSheet
import pl.kossa.myflights.databinding.DialogChangeAvatarBinding

@AndroidEntryPoint
class ChangeAvatarBottomSheet :
    BaseBottomSheet<ChangeAvatarViewModel, DialogChangeAvatarBinding>() {

    override val viewModel: ChangeAvatarViewModel by viewModels()

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                Log.d("MyLog", "Uri: ${data?.data}")
                data?.data?.let {
                    viewModel.navigateToAcceptAvatar(it)
                }
            }
        }


    override fun setOnClickListeners() {
        binding.takePhotoButton.setOnClickListener {
            viewModel.showComingSoonDialog()
        }
        binding.galleryButton.setOnClickListener {
            askPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                resultLauncher.launch(intent)
            }.onDeclined {
                //TODO
            }
        }
        binding.deleteButton.setOnClickListener {
            viewModel.deleteAvatar()
        }
    }

    override fun collectFlow() {
        super.collectFlow()
       viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.user.collect {
                it?.let { binding.deleteButton.isVisible = it.avatar != null }
            }
        }
       viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isLoadingData.collect {
                binding.progressBar.isVisible = it
            }
        }
    }
}