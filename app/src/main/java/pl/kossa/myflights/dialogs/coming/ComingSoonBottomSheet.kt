package pl.kossa.myflights.dialogs.coming

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import pl.kossa.myflights.architecture.dialogs.BaseBottomSheet
import pl.kossa.myflights.databinding.DialogComingSoonBinding

@AndroidEntryPoint
class ComingSoonBottomSheet : BaseBottomSheet<ComingSoonViewModel, DialogComingSoonBinding>() {

    override val viewModel: ComingSoonViewModel by viewModels()

    override fun collectFlow() {
        super.collectFlow()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.textFlow.collectLatest {
                binding.infoTv.text = it
                binding.infoTv.isVisible = !it.isNullOrBlank()
            }
        }
    }
}