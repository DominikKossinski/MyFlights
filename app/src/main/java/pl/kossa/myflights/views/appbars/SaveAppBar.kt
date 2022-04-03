package pl.kossa.myflights.views.appbars

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import pl.kossa.myflights.R
import pl.kossa.myflights.databinding.ViewSaveAppBarBinding

class SaveAppBar(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private val binding = ViewSaveAppBarBinding.inflate(LayoutInflater.from(context), this)
    var isSaveIconEnabled = false
        set(value) {
            field = value
            binding.saveIv.isEnabled = value
        }
    var isScanQrCodeVisible = false
        set(value) {
            field = value
            binding.scanQrCodeIv.isVisible = value
        }
    private var backOnClickListener: (() -> Unit)? = null
    private var saveOnClickListener: (() -> Unit)? = null
    private var scanQrCodeOnClickListener: (() -> Unit)? = null


    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.SaveAppBar, 0, 0).apply {
            isScanQrCodeVisible = getBoolean(R.styleable.SaveAppBar_isScanQrCodeVisible, false)
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
        binding.saveIv.setOnClickListener {
            saveOnClickListener?.invoke()
        }
        binding.scanQrCodeIv.setOnClickListener {
            scanQrCodeOnClickListener?.invoke()
        }
    }

    fun setBackOnClickListener(listener: (() -> Unit)?) {
        backOnClickListener = listener
    }

    fun setSaveOnClickListener(listener: (() -> Unit)?) {
        saveOnClickListener = listener
    }

    fun setScanQrCodeOnClickListener(listener: (() -> Unit)?) {
        scanQrCodeOnClickListener = listener
    }

}