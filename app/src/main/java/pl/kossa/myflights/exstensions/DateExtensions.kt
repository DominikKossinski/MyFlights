package pl.kossa.myflights.exstensions

import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.*

fun Date.toDateString(): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return formatter.format(this)
}

fun Date.toTimeString(): String {
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return formatter.format(this)
}

fun Date.extractYear(): Int {
    val formatter = SimpleDateFormat("yyyy", Locale.getDefault())
    return formatter.format(this).toInt()
}

fun Date.extractMonth(): Int {
    val formatter = SimpleDateFormat("MM", Locale.getDefault())
    return formatter.format(this).toInt()
}

fun Date.extractMonth0(): Int {
    return this.extractMonth() - 1
}

fun Date.extractDayOfMonth(): Int {
    val formatter = SimpleDateFormat("dd", Locale.getDefault())
    return formatter.format(this).toInt()
}

fun Date.extractHour(): Int {
    val formatter = SimpleDateFormat("HH", Locale.getDefault())
    return formatter.format(this).toInt()
}

fun Date.extractMinute(): Int {
    val formatter = SimpleDateFormat("mm", Locale.getDefault())
    return formatter.format(this).toInt()
}


