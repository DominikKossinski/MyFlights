package pl.kossa.myflights.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import pl.kossa.myflights.R
import pl.kossa.myflights.databinding.ViewDateTimeSelectBinding
import pl.kossa.myflights.exstensions.toDateString
import pl.kossa.myflights.exstensions.toTimeString
import java.util.*

class DateTimeSelectView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    val binding = ViewDateTimeSelectBinding.inflate(LayoutInflater.from(context), this)

    private var onSelectClickListener: (() -> Unit)? = null
//    private var onChangeClickListener: (() -> Unit)? = null

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.DateTimeSelectView, 0, 0).apply {
            binding.dateTimeTv.text = getString(R.styleable.DateTimeSelectView_dateTimeName)
        }
    }

    private var timeText: String = ""
        set(value) {
            field = value
            binding.timeTv.text = value
        }
    private var dateText: String = ""
        set(value) {
            field = value
            binding.dateTv.text = value
        }
    var date: Date? = null
        set(value) {

            field = value
            binding.dateTimeSelectButton.isVisible = value == null
            binding.dateTimeChangeButton.isVisible = value != null
            binding.dateTv.isVisible = value != null
            binding.timeTv.isVisible = value != null
            value?.let {
                timeText = it.toTimeString()
                dateText = it.toDateString()
            }
        }

    init {
        binding.dateTimeSelectButton.setOnClickListener {
            onSelectClickListener?.invoke()
        }
        binding.dateTimeChangeButton.setOnClickListener {
            onSelectClickListener?.invoke()
        }
//        binding.dateTv.setOnClickListener {
//            onDateClickListener?.invoke()
//        }
//        binding.timeTv.setOnClickListener {
//            onTimeClickListener?.invoke()
//        }
    }

    fun setOnSelectClickListener(listener: (() -> Unit)?) {
        onSelectClickListener = listener
    }

//    fun setOnDateClickListener(listener: (() -> Unit)?) {
//        onDateClickListener = listener
//    }
//
//    fun setOnTimeClickListener(listener: (() -> Unit)?) {
//        onTimeClickListener = listener
//    }
}