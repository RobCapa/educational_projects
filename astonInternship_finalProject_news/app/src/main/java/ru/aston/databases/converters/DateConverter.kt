package ru.aston.databases.converters

import androidx.room.TypeConverter
import ru.aston.utils.DateConverterHelper
import java.util.Date

class DateConverter {
    @TypeConverter
    fun dateToString(date: Date?): String? {
        return DateConverterHelper.format(
            DateConverterHelper.Formatter.NEWS_API_DATE,
            date
        )
    }
}