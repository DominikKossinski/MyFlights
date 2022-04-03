package pl.kossa.myflights.views.appbars

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import pl.kossa.myflights.R
import pl.kossa.myflights.databinding.ViewNotificationAppBarBinding

class NotificationAppBar(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private val binding = ViewNotificationAppBarBinding.inflate(LayoutInflater.from(context), this)

    private var logoOnClickListener: (() -> Unit)? = null
    private var notificationOnClickListener: (() -> Unit)? = null

    var isNotificationIconVisible = false
        set(value) {
            field = value
            binding.notificationIv.isVisible = value
        }


    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.NotificationAppBar, 0, 0).apply {
            isNotificationIconVisible =
                getBoolean(R.styleable.NotificationAppBar_notificationVisible, false)
        }
        binding.root.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.white_background_day_night
            )
        )
        binding.logoTv.setOnClickListener {
            logoOnClickListener?.invoke()
        }
        binding.notificationIv.setOnClickListener {
            notificationOnClickListener?.invoke()
        }
    }

    fun setLogoOnClickListener(listener: (() -> Unit)?) {
        logoOnClickListener = listener
    }

    fun setNotificationOnClickListener(listener: (() -> Unit)?) {
        notificationOnClickListener = listener
    }
}