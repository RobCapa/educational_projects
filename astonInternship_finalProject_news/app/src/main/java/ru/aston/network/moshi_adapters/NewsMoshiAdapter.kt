package ru.aston.network.moshi_adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.aston.data.News

class NewsMoshiAdapter {

    @FromJson
    fun getNews(customNews: CustomNews): News {
        return with(customNews) {
            News(
                title = title,
                date = date,
                description = description,
                content = content,
                newsUrl = newsUrl,
                imageUrl = imageUrl,
                sourceName = customSource.name,
                sourceId = customSource.id,
            )
        }

    }

    @JsonClass(generateAdapter = true)
    data class CustomNews(
        @Json(name = "title")
        val title: String,
        @Json(name = "description")
        val description: String?,
        @Json(name = "url")
        val newsUrl: String?,
        @Json(name = "urlToImage")
        val imageUrl: String?,
        @Json(name = "publishedAt")
        val date: String?,
        @Json(name = "content")
        val content: String?,
        @Json(name = "source")
        val customSource: CustomSource
    )

    @JsonClass(generateAdapter = true)
    data class CustomSource(
        @Json(name = "name")
        val name: String?,
        @Json(name = "id")
        val id: String?,
    )
}

