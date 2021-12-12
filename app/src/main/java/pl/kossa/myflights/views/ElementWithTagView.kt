package pl.kossa.myflights.views

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
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
            binding.root.isVisible = value.isNotBlank()
        }

    var isCopyVisible = false
        set(value) {
            field = value
            binding.copyIv.isVisible = value
        }

    var clipLabel = "label"

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.ElementWithTagView, 0, 0).apply {
            tagText = getString(R.styleable.ElementWithTagView_tagText) ?: ""
            valueText = getString(R.styleable.ElementWithTagView_valueText) ?: ""
            isCopyVisible = getBoolean(R.styleable.ElementWithTagView_isCopyVisible, false)
        }
        binding.copyIv.setOnClickListener {
            val clipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboardManager.setPrimaryClip(
                ClipData.newPlainText(
                    clipLabel,
                    binding.valueTextView.text
                )
            )
            Toast.makeText(context, R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show()
        }
    }

}