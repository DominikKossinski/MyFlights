package pl.kossa.myflights.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import pl.kossa.myflights.R
import pl.kossa.myflights.databinding.ViewLogoAppBarBinding

class LogoAppBar(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private val binding = ViewLogoAppBarBinding.inflate(LayoutInflater.from(context), this)

    private var onProfileClickListener: (() -> Unit)? = null

    var isProfileVisible = true
        set(value) {
            field = value
            binding.profileIv.isVisible = value
        }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.LogoAppBar, 0, 0).apply {
            isProfileVisible = getBoolean(R.styleable.LogoAppBar_profileVisible, true)
        }
        binding.root.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.white_background_day_night
            )
        )
        binding.profileIv.setOnClickListener {
            onProfileClickListener?.invoke()
        }
    }

    fun setOnProfileClickListener(listener: () -> Unit) {
        onProfileClickListener = listener
    }
}