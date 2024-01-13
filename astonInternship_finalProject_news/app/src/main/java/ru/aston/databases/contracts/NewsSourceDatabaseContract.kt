package ru.aston.databases.contracts

object NewsSourceDatabaseContract {
    const val TABLE_NAME = "news_sources"

    object COLUMNS {
        const val ID = "id"
        const val NAME = "name"
        const val CATEGORY = "category"
        const val COUNTRY = "country"
        const val LANGUAGE = "language"
        const val LOGO_URL = "logo_url"
        const val CASH_DATE = "cash_date"
    }
}