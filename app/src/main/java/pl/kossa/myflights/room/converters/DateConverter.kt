package pl.kossa.myflights.room.converters

import androidx.room.ProvidedAutoMigrationSpec
import androidx.room.TypeConverter
import pl.kossa.myflights.exstensions.toDateTime
import pl.kossa.myflights.exstensions.toUTCDateTimeString
import java.util.*

@ProvidedAutoMigrationSpec
class DateConverter {

    @TypeConverter
    fun stringToDate(string: String?): Date? {
        return string?.toDateTime()
    }

    @TypeConverter
    fun dateToString(date: Date?): String? {
        return date?.toUTCDateTimeString()
    }

}