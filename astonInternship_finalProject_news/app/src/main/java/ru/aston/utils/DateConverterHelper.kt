package ru.aston.utils

import android.nfc.FormatException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

object DateConverterHelper {

    fun combineDates(
        pair1: Pair<Date, Formatter>,
        pair2: Pair<Date, Formatter>,
        separator: String = "",
    ): String {
        val date1 = with(pair1) { second.format.format(first) }
        val date2 = with(pair2) { second.format.format(first) }
        return "$date1$separator$date2"
    }

    fun format(
        formatter: Formatter,
        date: Date?,
    ): String? {
        date ?: return null

        return try {
            formatter.format.format(date)
        } catch (e: FormatException) {
            null
        }
    }

    fun convertFromFormatsToFormat(
        oldFormatters: List<Formatter>,
        newFormatter: Formatter,
        date: String
    ): String? {
        oldFormatters.forEach { formatter ->
            try {
                return formatter.format
                    .parse(date)
                    ?.let { newFormatter.format.format(it) }
            } catch (e: ParseException) {
                return@forEach
            }
        }
        return null
    }

    enum class Formatter(val format: SimpleDateFormat) {
        NEWS_API_DATE_TIME_1(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")),
        NEWS_API_DATE_TIME_2(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")),
        NEWS_API_DATE(SimpleDateFormat("yyyy-MM-dd")),

        APP_DATE_TIME(SimpleDateFormat("MMM dd, yyyy | HH:mm aa")),
        APP_DATE_1(SimpleDateFormat("MMM dd")),
        APP_DATE_2(SimpleDateFormat("MMM dd, yyyy")),
    }
}