package ru.aston.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ru.aston.databases.contracts.NewsSourceDatabaseContract

@Parcelize
@Entity(tableName = NewsSourceDatabaseContract.TABLE_NAME)
@JsonClass(generateAdapter = true)
data class NewsSource(
    @PrimaryKey
    @ColumnInfo(name = NewsSourceDatabaseContract.COLUMNS.ID)
    @Json(name = "id")
    val id: String,
    @ColumnInfo(name = NewsSourceDatabaseContract.COLUMNS.NAME)
    @Json(name = "name")
    val name: String,
    @ColumnInfo(name = NewsSourceDatabaseContract.COLUMNS.CATEGORY)
    @Json(name = "category")
    val category: String,
    @ColumnInfo(name = NewsSourceDatabaseContract.COLUMNS.COUNTRY)
    @Json(name = "country")
    val country: String,
    @ColumnInfo(name = NewsSourceDatabaseContract.COLUMNS.LANGUAGE)
    @Json(name = "language")
    val language: String,
    @ColumnInfo(name = NewsSourceDatabaseContract.COLUMNS.LOGO_URL)
    @Json(name = "url")
    val siteUrl: String,
    @ColumnInfo(name = NewsSourceDatabaseContract.COLUMNS.CASH_DATE)
    val cashDate: Long? = null,
) : Parcelable

@JsonClass(generateAdapter = true)
data class NewsSourceWrapper(
    @Json(name = "sources")
    val items: List<NewsSource>
)
