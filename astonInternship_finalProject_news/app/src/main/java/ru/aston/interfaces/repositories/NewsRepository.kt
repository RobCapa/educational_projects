package ru.aston.interfaces.repositories

import kotlinx.coroutines.flow.Flow
import ru.aston.data.Filters
import ru.aston.data.News
import ru.aston.data.NewsCategory
import ru.aston.data.NewsSource
import ru.aston.repositories.utils.DataSource
import java.util.concurrent.CompletableFuture

interface NewsRepository {
    suspend fun clearOldCache()

    suspend fun getSources(filters: Filters): List<NewsSource>

    suspend fun getSavedNews(filters: Filters): List<News>

    suspend fun newsIsSaved(news: News): Flow<Boolean>

    fun addNewsToSaved(news: News): Flow<Boolean>

    fun removeNewsFromSaved(news: News): Flow<Boolean>

    suspend fun findSources(name: String, filters: Filters): List<NewsSource>

    suspend fun findSavedNews(searchCriteria: String, filters: Filters): List<News>

    suspend fun getNews(
        filters: Filters,
        source: NewsSource? = null,
        searchCriteria: String? = null,
        page: Int = ALL_PAGE,
        pageSize: Int = DEFAULT_PAGE_SIZE,
    ): List<News>

    suspend fun getNewsByCategory(
        filters: Filters,
        category: NewsCategory,
        searchCriteria: String? = null,
        page: Int = ALL_PAGE,
        pageSize: Int = DEFAULT_PAGE_SIZE,
    ): List<News>

    fun getNewsByCategoryFuture(
        filters: Filters,
        category: NewsCategory,
        searchCriteria: String? = null,
        page: Int = ALL_PAGE,
        pageSize: Int = DEFAULT_PAGE_SIZE,
    ): CompletableFuture<List<News>>

    fun getNewsWithFiltersFuture(
        filters: Filters,
        searchCriteria: String? = null,
        page: Int = ALL_PAGE,
        pageSize: Int = DEFAULT_PAGE_SIZE,
    ): CompletableFuture<List<News>>

    fun dataGettingFrom(): DataSource

    companion object {
        const val ALL_PAGE = -1
        const val DEFAULT_PAGE_SIZE = 50
    }
}