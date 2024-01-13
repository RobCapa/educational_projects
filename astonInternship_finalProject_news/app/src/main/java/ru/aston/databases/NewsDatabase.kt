package ru.aston.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.aston.data.News
import ru.aston.data.NewsSource
import ru.aston.databases.dao.NewsDao
import ru.aston.databases.dao.NewsSourceDao

@Database(
    entities = [
        NewsSource::class,
        News::class,
    ],
    version = NewsDatabase.DB_VERSION
)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun sourceDao(): NewsSourceDao

    abstract fun newsDao(): NewsDao

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "NewsDatabase"
    }
}