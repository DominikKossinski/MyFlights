package pl.kossa.myflights.exstensions

import java.text.SimpleDateFormat
import java.util.*

fun String.toDateTime(): Date? {
    val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.parse(this)
}