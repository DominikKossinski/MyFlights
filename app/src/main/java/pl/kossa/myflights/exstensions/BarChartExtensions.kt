package pl.kossa.myflights.exstensions

import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import pl.kossa.myflights.R
import pl.kossa.myflights.utils.charts.IntValueFormatter
import pl.kossa.myflights.utils.charts.StringLabelsFormatter

fun BarChart.setupStatsChart(entries: List<BarEntry>, labels: List<String>) {
    val textColor = ContextCompat.getColor(this.context, R.color.black_day_night)
    val datasetColor = ContextCompat.getColor(this.context, R.color.colorPrimary)

    this.isVisible = entries.size > 1
    val maxY = entries.maxOfOrNull { it.y } ?: 0f
    val dataSet = BarDataSet(entries, "")
    dataSet.color = datasetColor
    val data = BarData(dataSet)
    this.data = data
    this.barData.setValueTextColor(textColor)
    this.barData.setValueFormatter(IntValueFormatter())

    this.legend.isEnabled = false
    this.description.isEnabled = false

    this.xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
    this.xAxis.setDrawLabels(true)
    this.xAxis.setDrawGridLines(false)
    this.xAxis.setDrawAxisLine(false)
    this.xAxis.textColor = textColor
    this.xAxis.valueFormatter = StringLabelsFormatter(labels)
    this.xAxis.granularity = 1f


    this.axisLeft.setDrawGridLines(false)
    this.axisLeft.isEnabled = false
    this.axisRight.isEnabled = false
    this.axisLeft.axisMinimum = 0f
    this.axisLeft.axisMaximum = maxY + 1f

    this.animateY(3000)
    this.invalidate()

}