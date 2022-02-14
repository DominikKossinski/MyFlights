package pl.kossa.myflights.views.google

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import pl.kossa.myflights.R
import pl.kossa.myflights.databinding.ViewGoogleSignInButtonBinding

class GoogleSignInButton(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private val binding = ViewGoogleSignInButtonBinding.inflate(LayoutInflater.from(context), this)

    init {
        val bg = ContextCompat.getDrawable(context, R.drawable.ripple_rounded_primary)
        binding.root.background = bg
        this.gravity = Gravity.CENTER
        val padding = context.resources.getDimensionPixelOffset(R.dimen.padding)
        binding.root.setPadding(padding, padding, padding, padding)
    }

}