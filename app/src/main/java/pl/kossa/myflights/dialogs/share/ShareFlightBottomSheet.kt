package pl.kossa.myflights.dialogs.share

import android.content.Intent
import android.os.CountDownTimer
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
import pl.kossa.myflights.utils.timers.ProgressBarCountDownTimer
import java.net.URLEncoder
import java.util.*

@AndroidEntryPoint
class ShareFlightBottomSheet : BaseBottomSheet<ShareFlightViewModel, DialogShareFlightBinding>() {

    override val viewModel: ShareFlightViewModel by viewModels()

    private var timer: CountDownTimer? = null
    private var progressTimer: ProgressBarCountDownTimer? = null

    override fun setOnClickListeners() {
        super.setOnClickListeners()
        binding.progressIv.isProgressVisible = false
        binding.progressIv.max = 1_000
    }

    override fun collectFlow() {
        super.collectFlow()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isLoadingData.collectLatest {
                if (it) {
                    binding.progressIv.setImageResource(R.drawable.progress_placeholder)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.sharedFlightFlow.collectLatest {
                it?.let { setupSharedFlight(it) }
            }
        }
    }

    private fun setupSharedFlight(sharedFlight: SharedFlight) {
        val size = requireContext().dpToPx(200f).toInt()
        val appLink = getString(R.string.share_flight_uri_format, sharedFlight.sharedFlightId)
        val dynamicLink = getString(
            R.string.share_flight_dynamic_link_format,
            URLEncoder.encode(appLink, "utf-8")
        )
        val qrCodeBitmap =
            QRCode.from(dynamicLink)
                .withSize(size, size)
                .bitmap()
        binding.progressIv.setImageBitmap(qrCodeBitmap)
        binding.timeTv.isVisible = true
        binding.progressIv.isProgressVisible = true
        binding.sendButton.isEnabled = true
        binding.sendButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.share_flight_invitation_link_format, dynamicLink)
            )
            intent.putExtra(
                Intent.EXTRA_SUBJECT,
                getString(R.string.share_flight_invitation_link_title)
            )
            intent.type = "text/plain"
            val shareIntent = Intent.createChooser(intent, null)
            viewModel.logClickShareWithLink()
            startActivity(shareIntent)
        }
        timer = object : CountDownTimer(sharedFlight.expiresAt.time - Date().time, 1_000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / (60 * 1_000)
                val seconds = (millisUntilFinished - minutes * (60 * 1_000)) / 1_000
                binding.timeTv.text = getString(R.string.share_flight_time_format, minutes, seconds)
            }

            override fun onFinish() {
                viewModel.setToastMessage(R.string.share_flight_code_expired)
                binding.progressIv.isProgressVisible = false
                binding.sendButton.isEnabled = false
                binding.progressIv.setImageResource(R.drawable.progress_placeholder)
                viewModel.fetchSharedFlight()
            }
        }
        progressTimer =
            ProgressBarCountDownTimer(sharedFlight.expiresAt.time - Date().time, binding.progressIv)
        progressTimer?.start()
        timer?.start()
    }

    override fun onResume() {
        super.onResume()
        val sharedFlight = viewModel.sharedFlightFlow.value
        sharedFlight?.let {
            setupSharedFlight(it)
        }
    }

    override fun onPause() {
        super.onPause()
        progressTimer?.cancel()
        timer?.cancel()
    }
}