package pl.kossa.myflights.utils.charts

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class StringLabelsFormatter(
    private val labels: List<String>
): IndexAxisValueFormatter() {

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        val index = value.toInt()
        return  if(index < labels.size) {
            labels[index]
        } else ""
    }
}