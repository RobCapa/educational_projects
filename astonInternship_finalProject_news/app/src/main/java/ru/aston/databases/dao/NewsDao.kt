package ru.aston.databases.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Update
import ru.aston.data.News
import ru.aston.databases.contracts.NewsDatabaseContract
import ru.aston.databases.converters.DateConverter
import ru.aston.databases.utils.Constants
import java.util.Date

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNews(listItem: List<News>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateNews(news: News)

    @Delete
    suspend fun delete(sources: List<News>)

    @TypeConverters(DateConverter::class)
    @Query(
        "SELECT * FROM ${NewsDatabaseContract.TABLE_NAME} " +
                "WHERE (:language IS NULL OR ${NewsDatabaseContract.COLUMNS.LANGUAGE} = :language)" +
                "AND (:isSaved IS NULL OR ${NewsDatabaseContract.COLUMNS.IS_SAVED} = :isSaved)" +
                "AND (:source IS NULL OR ${NewsDatabaseContract.COLUMNS.SOURCE_NAME} = :source)" +
                "AND (:category IS NULL OR ${NewsDatabaseContract.COLUMNS.CATEGORY} GLOB :category)" +
                "AND ((:fromDate IS NULL OR :toDate IS NULL) OR " +
                "DATE(${NewsDatabaseContract.COLUMNS.DATE}) BETWEEN DATE(:fromDate) AND DATE(:toDate))" +
                "AND (:searchCriteria IS NULL OR " +
                "(${NewsDatabaseContract.COLUMNS.TITLE} LIKE '%' || :searchCriteria || '%'" +
                "OR ${NewsDatabaseContract.COLUMNS.DESCRIPTION} LIKE '%' || :searchCriteria || '%'" +
                "OR ${NewsDatabaseContract.COLUMNS.CONTENT} LIKE '%' || :searchCriteria || '%'))" +
                "ORDER BY ${NewsDatabaseContract.COLUMNS.DATE} DESC"
    )
    suspend fun getNews(
        searchCriteria: String? = null,
        language: String? = null,
        category: String? = null,
        source: String? = null,
        fromDate: Date? = null,
        toDate: Date? = null,
        isSaved: Boolean? = null,
    ): List<News>

    @TypeConverters(DateConverter::class)
    @Query(
        "SELECT * FROM ${NewsDatabaseContract.TABLE_NAME} " +
                "WHERE (:language IS NULL OR ${NewsDatabaseContract.COLUMNS.LANGUAGE} = :language)" +
                "AND (:isSaved IS NULL OR ${NewsDatabaseContract.COLUMNS.IS_SAVED} = :isSaved)" +
                "AND (:source IS NULL OR ${NewsDatabaseContract.COLUMNS.SOURCE_NAME} = :source)" +
                "AND (:category IS NULL OR ${NewsDatabaseContract.COLUMNS.CATEGORY} GLOB :category)" +
                "AND ((:fromDate IS NULL OR :toDate IS NULL) OR " +
                "DATE(${NewsDatabaseContract.COLUMNS.DATE}) BETWEEN DATE(:fromDate) AND DATE(:toDate))" +
                "AND (:searchCriteria IS NULL OR " +
                "(${NewsDatabaseContract.COLUMNS.TITLE} LIKE '%' || :searchCriteria || '%'" +
                "OR ${NewsDatabaseContract.COLUMNS.DESCRIPTION} LIKE '%' || :searchCriteria || '%'" +
                "OR ${NewsDatabaseContract.COLUMNS.CONTENT} LIKE '%' || :searchCriteria || '%'))" +
                "ORDER BY ${NewsDatabaseContract.COLUMNS.DATE} DESC " +
                "LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)"
    )
    suspend fun getNews(
        searchCriteria: String? = null,
        language: String? = null,
        category: String? = null,
        source: String? = null,
        fromDate: Date? = null,
        toDate: Date? = null,
        isSaved: Boolean? = null,
        page: Int = 1,
        pageSize: Int = 10,
    ): List<News>

    @Query(
        "SELECT EXISTS(SELECT * FROM ${NewsDatabaseContract.TABLE_NAME} " +
                "WHERE ${NewsDatabaseContract.COLUMNS.TITLE} = :title " +
                "AND ${NewsDatabaseContract.COLUMNS.IS_SAVED} = 1 )"
    )
    suspend fun newsIsSaved(title: String): Boolean

    @Query(
        "SELECT * FROM ${NewsDatabaseContract.TABLE_NAME} " +
                "WHERE ${NewsDatabaseContract.COLUMNS.CASH_DATE} <= (strftime('%s', datetime('now', '-${Constants.CASH_DAYS} day')) * 1000)"
    )
    suspend fun getOldCache(): List<News>
}