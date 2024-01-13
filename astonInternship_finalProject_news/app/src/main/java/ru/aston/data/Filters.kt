package ru.aston.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Filters(
    var orderBy: SortingStrategy? = null,
    var dates: Pair<Date, Date>? = null,
    var language: Language? = null,
) : Parcelable {

    enum class SortingStrategy(val value: String) {
        RELEVANT("relevancy"),
        POPULARITY("popularity"),
        DATE("publishedAt"),
    }

    enum class Language(
        val value: String,
        val fullName: String,
    ) {
        ARABIC("ar", "Arabic"),
        GERMAN("de", "German"),
        ENGLISH("en", "English"),
        SPANISH("es", "Spanish"),
        FRENCH("fr", "French"),
        HEBREW("he", "Hebrew"),
        ITALIAN("it", "Italian"),
        DUTCH("nl", "Dutch"),
        NORWEGIAN("no", "Norwegian"),
        PORTUGUESE("pt", "Portuguese"),
        RUSSIAN("ru", "Russian"),
        SWEDISH("sv", "Swedish"),
        CHINESE("zh", "Chinese"),
    }
}
