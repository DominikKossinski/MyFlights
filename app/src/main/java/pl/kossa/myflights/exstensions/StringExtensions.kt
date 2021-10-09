package pl.kossa.myflights.exstensions

import java.text.SimpleDateFormat
import java.util.*

fun String.toDateTime(): Date? {
    val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return formatter.parse(this)
}