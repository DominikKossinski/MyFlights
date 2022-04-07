package pl.kossa.myflights.dialogs.request

import android.util.Log
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.kossa.myflights.architecture.dialogs.BaseDialog
import pl.kossa.myflights.databinding.DialogJoinRequestSentBinding

@AndroidEntryPoint
class JoinRequestSentDialog : BaseDialog<JoinRequestSentViewModel, DialogJoinRequestSentBinding>() {

    override val viewModel: JoinRequestSentViewModel by viewModels()

    override fun setupClickListeners() {
        binding.okButton.setOnClickListener {
            Log.d("MyLog", "Ok Clicked")
            viewModel.navigateBack()
        }
    }
}