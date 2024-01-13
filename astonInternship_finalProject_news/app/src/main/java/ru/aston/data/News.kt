package ru.aston.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ru.aston.databases.contracts.NewsDatabaseContract

@Entity(tableName = NewsDatabaseContract.TABLE_NAME)
@Parcelize
@JsonClass(generateAdapter = true)
data class News(
    @PrimaryKey
    @ColumnInfo(name = NewsDatabaseContract.COLUMNS.TITLE)
    val title: String,
    @ColumnInfo(name = NewsDatabaseContract.COLUMNS.DATE)
    val date: String? = null,
    @ColumnInfo(name = NewsDatabaseContract.COLUMNS.DESCRIPTION)
    val description: String? = null,
    @ColumnInfo(name = NewsDatabaseContract.COLUMNS.CONTENT)
    val content: String? = null,
    @ColumnInfo(name = NewsDatabaseContract.COLUMNS.NEWS_URL)
    val newsUrl: String? = null,
    @ColumnInfo(name = NewsDatabaseContract.COLUMNS.IMAGE_URL)
    val imageUrl: String? = null,
    @ColumnInfo(name = NewsDatabaseContract.COLUMNS.SOURCE_NAME)
    val sourceName: String? = null,
    @ColumnInfo(name = NewsDatabaseContract.COLUMNS.SOURCE_ID)
    val sourceId: String? = null,
    @ColumnInfo(name = NewsDatabaseContract.COLUMNS.LANGUAGE)
    val language: String? = null,
    @ColumnInfo(name = NewsDatabaseContract.COLUMNS.CATEGORY)
    val category: String? = null,
    @ColumnInfo(name = NewsDatabaseContract.COLUMNS.CASH_DATE)
    val cashDate: Long? = null,
    @ColumnInfo(name = NewsDatabaseContract.COLUMNS.IS_SAVED)
    val isSaved: Boolean = false,
) : Parcelable

@JsonClass(generateAdapter = true)
data class NewsWrapper(
    @Json(name = "articles")
    val items: List<News>
)
