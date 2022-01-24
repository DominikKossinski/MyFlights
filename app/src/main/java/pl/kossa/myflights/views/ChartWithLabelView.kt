package pl.kossa.myflights.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.github.mikephil.charting.data.BarEntry
import pl.kossa.myflights.R
import pl.kossa.myflights.databinding.ViewChartWithLabelBinding
import pl.kossa.myflights.exstensions.setupStatsChart

class ChartWithLabelView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private val binding: ViewChartWithLabelBinding =
        ViewChartWithLabelBinding.inflate(LayoutInflater.from(context), this)

    var tagText = ""
        set(value) {
            field = value
            binding.tagTextView.text = value
        }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.ChartWithLabelView, 0, 0).apply {
            tagText = getString(R.styleable.ChartWithLabelView_tagText) ?: ""
        }
    }

    fun setupStatsChart(entries: List<BarEntry>, labels: List<String>) {
        binding.root.isVisible = entries.size > 1
        binding.barChart.setupStatsChart(entries, labels)
    }
}