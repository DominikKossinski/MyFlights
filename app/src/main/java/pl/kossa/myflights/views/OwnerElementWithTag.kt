package pl.kossa.myflights.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import pl.kossa.myflights.R
import pl.kossa.myflights.databinding.ViewOwnerElementWithTagBinding

class OwnerElementWithTag(context: Context, attrs: AttributeSet) :
    LinearLayout(context, attrs) {

    private val binding: ViewOwnerElementWithTagBinding =
        ViewOwnerElementWithTagBinding.inflate(LayoutInflater.from(context), this)

    var isMyFlight = true
        set(value) {
            field = value
            setupVisibility()
        }

    var ownerNick = ""
        set(value) {
            field = value
            binding.nickTextView.isVisible = value.isNotBlank()
            binding.nickTextView.text = value
            setupVisibility()
        }

    var ownerEmail: String? = ""
        set(value) {
            field = value
            binding.emailTextView.isVisible = !value.isNullOrBlank()
            binding.emailTextView.text = value
            setupVisibility()
        }

    private fun setupVisibility() {
        this.isVisible = (!ownerEmail.isNullOrBlank() || ownerNick.isNotBlank()) && !isMyFlight
    }

    var profileUrl: String? = null
        set(value) {
            field = value
            value?.let {
                Glide.with(context)
                    .load(it)
                    .transform(CircleCrop())
                    .placeholder(R.drawable.ic_profile)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(binding.profileIv)
            } ?: binding.profileIv.setImageResource(R.drawable.ic_profile)
        }

    init {
        orientation = VERTICAL
    }
}