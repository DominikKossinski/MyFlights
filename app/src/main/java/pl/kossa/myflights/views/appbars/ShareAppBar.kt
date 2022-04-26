package pl.kossa.myflights.views.appbars

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import pl.kossa.myflights.R
import pl.kossa.myflights.databinding.ViewShareAppBarBinding

class ShareAppBar(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private val binding = ViewShareAppBarBinding.inflate(LayoutInflater.from(context), this)

    private var backOnClickListener: (() -> Unit)? = null
    private var deleteOnClickListener: (() -> Unit)? = null
    private var shareOnClickListener: (() -> Unit)? = null
    private var resignOnClickListener: (() -> Unit)? = null

    var isShareVisible = false
        set(value) {
            field = value
            binding.shareIv.isVisible = value
        }
    var isDeleteIconVisible = false
        set(value) {
            field = value
            binding.deleteIv.isVisible = value
        }
    var isResignVisible = false
        set(value) {
            field = value
            binding.resignIv.isVisible = value
        }


    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.BackAppBar, 0, 0).apply {
            isDeleteIconVisible = getBoolean(R.styleable.BackAppBar_deleteVisible, false)
        }
        binding.root.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.white_background_day_night
            )
        )
        binding.backTv.setOnClickListener {
            backOnClickListener?.invoke()
        }
        binding.deleteIv.setOnClickListener {
            deleteOnClickListener?.invoke()
        }
        binding.shareIv.setOnClickListener {
            shareOnClickListener?.invoke()
        }
        binding.resignIv.setOnClickListener {
            resignOnClickListener?.invoke()
        }
    }

    fun setBackOnClickListener(listener: (() -> Unit)?) {
        backOnClickListener = listener
    }

    fun setDeleteOnClickListener(listener: (() -> Unit)?) {
        deleteOnClickListener = listener
    }

    fun setShareOnClickListener(listener: (() -> Unit)?) {
        shareOnClickListener = listener
    }

    fun setResignOnClickListener(listener: (() -> Unit)?) {
        resignOnClickListener = listener
    }
}