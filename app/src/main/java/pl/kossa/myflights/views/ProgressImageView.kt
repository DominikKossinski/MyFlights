package pl.kossa.myflights.views

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import pl.kossa.myflights.R
import pl.kossa.myflights.databinding.ViewProgressImageViewBinding

class ProgressImageView(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {

    private val binding: ViewProgressImageViewBinding =
        ViewProgressImageViewBinding.inflate(LayoutInflater.from(context), this)

    private val redDrawable = ContextCompat.getDrawable(context, R.drawable.progress_drawable_red)
    private val greenDrawable = ContextCompat.getDrawable(context, R.drawable.progress_drawable)

    var max = 100
        set(value) {
            if (value >= 1) {
                field = value
                binding.p1.max = value
                binding.p2.max = value
                binding.p3.max = value
                binding.p4.max = value
                progress = value
            }
        }
    var isProgressVisible = false
        set(value) {
            field = value
            binding.p1.isVisible = value
            binding.p2.isVisible = value
            binding.p3.isVisible = value
            binding.p4.isVisible = value
        }
    var progress = 0
        set(value) {
            val ratio = if (value < 0) {
                field = 0
                0.0
            } else {
                field = value
                progress.toDouble() / max.toDouble()
            }
            if (ratio < 0.25) {
                binding.p1.progressDrawable = redDrawable
            } else {
                binding.p1.progressDrawable = greenDrawable
            }
            when {
                ratio < 0.25 -> {
                    binding.p1.progress = field * 4
                    binding.p2.progress = 0
                    binding.p3.progress = 0
                    binding.p4.progress = 0
                }
                ratio < 0.5 -> {
                    binding.p1.progress = max
                    binding.p2.progress = field * 4 - max
                    binding.p3.progress = 0
                    binding.p4.progress = 0
                }
                ratio < 0.75 -> {
                    binding.p1.progress = max
                    binding.p2.progress = max
                    binding.p3.progress = field * 4 - 2 * max
                    binding.p4.progress = 0
                }
                else -> {
                    binding.p1.progress = max
                    binding.p2.progress = max
                    binding.p3.progress = max
                    binding.p4.progress = field * 4 - 3 * max

                }
            }
        }

    fun setImageBitmap(bitmap: Bitmap) {
        binding.iV.setImageBitmap(bitmap)
    }

    fun setImageResource(@DrawableRes resourceId: Int) {
        binding.iV.setImageResource(resourceId)
    }
}