package pl.kossa.myflights.dialogs.coming

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.architecture.BaseBottomSheet
import pl.kossa.myflights.databinding.DialogComingSoonBinding

@AndroidEntryPoint
class ComingSoonBottomSheet : BaseBottomSheet<ComingSoonViewModel, DialogComingSoonBinding>() {

    override val viewModel: ComingSoonViewModel by viewModels()
}