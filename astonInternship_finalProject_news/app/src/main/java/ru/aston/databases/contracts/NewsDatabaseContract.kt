package ru.aston.databases.contracts

object NewsDatabaseContract {
    const val TABLE_NAME = "news"

    object COLUMNS {
        const val TITLE = "news_title"
        const val DATE = "date"
        const val DESCRIPTION = "description"
        const val CONTENT = "content"
        const val NEWS_URL = "newsUrl"
        const val IMAGE_URL = "imageUrl"
        const val SOURCE_NAME = "sourceName"
        const val SOURCE_ID = "sourceId"
        const val LANGUAGE = "language"
        const val CATEGORY = "category"
        const val CASH_DATE = "cash_date"
        const val IS_SAVED = "is_saved"
    }
}