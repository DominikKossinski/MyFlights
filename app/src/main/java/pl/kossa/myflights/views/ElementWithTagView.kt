package pl.kossa.myflights.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import pl.kossa.myflights.R
import pl.kossa.myflights.databinding.ViewElementWithTagBinding

class ElementWithTagView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private val binding: ViewElementWithTagBinding =
        ViewElementWithTagBinding.inflate(LayoutInflater.from(context), this)

    var tagText = ""
        set(value) {
            field = value
            binding.tagTextView.text = value
        }

    var valueText = ""
        set(value) {
            field = value
            binding.valueTextView.text = value
        }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.ElementWithTagView, 0, 0).apply {
            tagText = getString(R.styleable.ElementWithTagView_tagText) ?: ""
            valueText = getString(R.styleable.ElementWithTagView_valueText) ?: ""
        }
    }
}