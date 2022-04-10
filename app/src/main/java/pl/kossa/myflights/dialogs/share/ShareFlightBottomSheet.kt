package pl.kossa.myflights.dialogs.share

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import net.glxn.qrgen.android.QRCode
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.dialogs.BaseBottomSheet
import pl.kossa.myflights.databinding.DialogShareFlightBinding
import pl.kossa.myflights.exstensions.dpToPx

@AndroidEntryPoint
class ShareFlightBottomSheet : BaseBottomSheet<ShareFlightViewModel, DialogShareFlightBinding>() {

    override val viewModel: ShareFlightViewModel by viewModels()

    override fun collectFlow() {
        super.collectFlow()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isLoadingData.collectLatest {
                if (it) {
                    binding.qrIv.setImageResource(R.drawable.progress_placeholder)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.sharedFlightId.collectLatest {
                it?.let { setupSharedFlightId(it) }
            }
        }
    }

    private fun setupSharedFlightId(sharedFlightId: String) {
        val size = requireContext().dpToPx(200f).toInt()
        val qrCodeBitmap =
            QRCode.from(getString(R.string.share_flight_uri_format, sharedFlightId))
                .withSize(size, size)
                .bitmap()
        binding.qrIv.setImageBitmap(qrCodeBitmap)
    }
}