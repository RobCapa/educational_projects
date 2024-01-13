package ru.aston.databases.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.aston.data.NewsSource
import ru.aston.databases.contracts.NewsSourceDatabaseContract
import ru.aston.databases.utils.Constants

@Dao
interface NewsSourceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewsSources(items: List<NewsSource>)

    @Delete
    suspend fun delete(sources: List<NewsSource>)

    @Query(
        "SELECT * FROM ${NewsSourceDatabaseContract.TABLE_NAME} " +
                "WHERE (:language IS NULL OR ${NewsSourceDatabaseContract.COLUMNS.LANGUAGE} = :language)"
    )
    suspend fun getNewsSources(language: String? = null): List<NewsSource>

    @Query(
        "DELETE FROM ${NewsSourceDatabaseContract.TABLE_NAME} " +
                "WHERE ${NewsSourceDatabaseContract.COLUMNS.CASH_DATE} <= (strftime('%s', datetime('now', '-${Constants.CASH_DAYS} day')) * 1000)"
    )
    suspend fun clearOldCache()

    @Query(
        "SELECT * FROM ${NewsSourceDatabaseContract.TABLE_NAME} " +
                "WHERE ${NewsSourceDatabaseContract.COLUMNS.CASH_DATE} <= (strftime('%s', datetime('now', '-${Constants.CASH_DAYS} day')) * 1000)"
    )
    suspend fun getOldCache(): List<NewsSource>
}