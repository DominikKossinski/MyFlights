package pl.kossa.myflights.utils.timers

import android.os.CountDownTimer
import pl.kossa.myflights.views.ProgressImageView

class ProgressBarCountDownTimer(
    millisUntilFinish: Long,
    private val progressImageView: ProgressImageView
) : CountDownTimer(millisUntilFinish, 100) {

    init {
        progressImageView.max = 1000
    }

    override fun onTick(millisUntilFinished: Long) {
        val progress =
            (millisUntilFinished.toDouble() / 60_000.0 * 1_000.0).toInt()
        progressImageView.progress = progress
    }

    override fun onFinish() {}
}