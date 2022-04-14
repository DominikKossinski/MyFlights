package pl.kossa.myflights.dialogs.share

import android.os.CountDownTimer
import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import net.glxn.qrgen.android.QRCode
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.SharedFlight
import pl.kossa.myflights.architecture.dialogs.BaseBottomSheet
import pl.kossa.myflights.databinding.DialogShareFlightBinding
import pl.kossa.myflights.exstensions.dpToPx
import java.net.URLEncoder
import java.util.*

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

    private fun setupSharedFlightId(sharedFlight: SharedFlight) {
        val size = requireContext().dpToPx(200f).toInt()
        val appLink = getString(R.string.share_flight_uri_format, sharedFlight.sharedFlightId)
        val dynamicLink = getString(
            R.string.share_flight_dynamic_link_format,
            URLEncoder.encode(appLink, "utf-8")
        )
        Log.d("MyLog", "Link: $appLink")
        Log.d("MyLog", "Dynamic link: $dynamicLink")
        val qrCodeBitmap =
            QRCode.from(dynamicLink)
                .withSize(size, size)
                .bitmap()
        binding.qrIv.setImageBitmap(qrCodeBitmap)
        binding.timeTv.isVisible = true
        val a = object : CountDownTimer(sharedFlight.expiresAt.time - Date().time, 1_000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / (60 * 1_000)
                val seconds = (millisUntilFinished - minutes * (60 * 1_000)) / 1_000
                binding.timeTv.text = getString(R.string.share_flight_time_format, minutes, seconds)
            }

            override fun onFinish() {
                viewModel.setToastMessage(R.string.share_flight_code_expired)
                viewModel.navigateBack()
            }
        }
        a.start()
    }
}