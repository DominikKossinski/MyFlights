package pl.kossa.myflights.views.pickers

import androidx.fragment.app.FragmentManager

class DateTimePicker(
    private val date: Triple<Int, Int, Int>?,
    private val hourAndMinute: Pair<Int, Int>?,
    private val onDateTimeSet: (year: Int, month: Int, day: Int, hourOfDay: Int, minute: Int) -> Unit
) {

    fun show(fragmentManager: FragmentManager) {
        DatePicker(date) { year, month, day ->
            TimePicker(hourAndMinute) { hourOfDay, minute ->
                onDateTimeSet.invoke(year, month, day, hourOfDay, minute)
            }.show(fragmentManager, TimePicker.TAG)
        }.show(fragmentManager, DatePicker.TAG)
    }
}