package pl.kossa.myflights.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import pl.kossa.myflights.R
import pl.kossa.myflights.databinding.ViewElementSelectBinding

class ElementSelectView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private val binding: ViewElementSelectBinding =
        ViewElementSelectBinding.inflate(LayoutInflater.from(context), this)
    private var onSelectListener: (() -> Unit)? = null

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.ElementSelectView, 0, 0).apply {
            binding.elementTv.text = getString(R.styleable.ElementSelectView_elementName)
        }
    }

    var elementName: String = ""
        set(value) {
            field = value
            binding.elementNameTv.text = value
            binding.elementNameTv.isVisible = value.isNotBlank()
            binding.elementChangeTv.isVisible = value.isNotBlank()
            binding.elementSelectTv.isVisible = value.isBlank()
        }

    init {
        binding.elementSelectTv.setOnClickListener {
            onSelectListener?.invoke()
        }
        binding.elementChangeTv.setOnClickListener {
            onSelectListener?.invoke()
        }
    }

    fun setOnElementSelectListener(listener: (() -> Unit)?) {
        onSelectListener = listener
    }
}