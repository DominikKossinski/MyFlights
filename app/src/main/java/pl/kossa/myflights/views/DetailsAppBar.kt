package pl.kossa.myflights.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import pl.kossa.myflights.R
import pl.kossa.myflights.databinding.ViewDetailsAppBarBinding

class DetailsAppBar(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private val binding = ViewDetailsAppBarBinding.inflate(LayoutInflater.from(context), this)
    var title = ""
        set(value) {
            field = value
            binding.appBarTitle.text = value
        }
    private var backOnClickListener: (() -> Unit)? = null
    private var editOnClickListener: (() -> Unit)? = null
    private var deleteOnClickListener: (() -> Unit)? = null


    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.DetailsAppBar, 0, 0).apply {
            title = getString(R.styleable.DetailsAppBar_title) ?: ""
        }
        binding.root.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
        binding.backArrow.setOnClickListener {
            backOnClickListener?.invoke()
        }
        binding.editIv.setOnClickListener {
            editOnClickListener?.invoke()
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