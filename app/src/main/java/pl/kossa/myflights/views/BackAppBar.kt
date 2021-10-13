package pl.kossa.myflights.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import pl.kossa.myflights.R

class BackAppBar(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private val binding = pl.kossa.myflights.databinding.ViewBackAppBarBinding.inflate(LayoutInflater.from(context), this)

    private var backOnClickListener: (() -> Unit)? = null
    private var editOnClickListener: (() -> Unit)? = null
    private var deleteOnClickListener: (() -> Unit)? = null


    init {
        binding.root.setBackgroundColor(ContextCompat.getColor(context, R.color.white_background_day_night))
        binding.backTv.setOnClickListener {
            backOnClickListener?.invoke()
        }
        binding.deleteIv.setOnClickListener {
            deleteOnClickListener?.invoke()
        }
    }

    fun setBackOnClickListener(listener: (() -> Unit)?) {
        backOnClickListener = listener
    }

    fun setEditOnClickListener(listener: (() -> Unit)?) {
        editOnClickListener = listener
    }


    fun setDeleteOnClickListener(listener: (() -> Unit)?) {
        deleteOnClickListener = listener
    }


}