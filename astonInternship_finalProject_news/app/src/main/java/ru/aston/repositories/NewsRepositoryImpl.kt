package ru.aston.repositories

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.future.future
import kotlinx.coroutines.withContext
import ru.aston.R
import ru.aston.data.Filters
import ru.aston.data.News
import ru.aston.data.NewsCategory
import ru.aston.data.NewsSource
import ru.aston.databases.NewsDatabase
import ru.aston.exceptions.NoConnectionException
import ru.aston.interfaces.repositories.NewsRepository
import ru.aston.interfaces.repositories.NewsRepository.Companion.ALL_PAGE
import ru.aston.network.api.NewsApi
import ru.aston.repositories.utils.DataSource
import ru.aston.utils.DateConverterHelper
import java.util.Date
import java.util.concurrent.CompletableFuture
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi,
    private val appContext: Context,
    newsDatabase: NewsDatabase,
) : NewsRepository {

    private val sourceDao = newsDatabase.sourceDao()
    private val newsDao = newsDatabase.newsDao()

    override suspend fun getSources(filters: Filters): List<NewsSource> {
        return withContext(Dispatchers.IO) {
            with(filters) {
                try {

                    newsApi
                        .getSources(
                            language = language?.value,
                            sortBy = orderBy?.value,
                        )
                        .items
                        .also { items ->
                            sourceDao.addNewsSources(items.map {
                                it.copy(cashDate = Date().time)
                            })
                        }

                } catch (e: Exception) {
                    val items = sourceDao.getNewsSources(
                        language = language?.value,
                    )

                    if (items.isEmpty()) {
                        if (!checkConnection()) throw NoConnectionException()
                        else throw e
                    }

                    items
                }
            }
        }
    }

    override suspend fun findSources(
        name: String,
        filters: Filters,
    ): List<NewsSource> {
        return withContext(Dispatchers.IO) {
            getSources(filters).filter { source ->
                source.name.contains(name.trim(), true)
            }
        }
    }

    override suspend fun getNews(
        filters: Filters,
        source: NewsSource?,
        searchCriteria: String?,
        page: Int,
        pageSize: Int,
    ): List<News> {
        return withContext(Dispatchers.IO) {
            try {

                getNewsFromServer(
                    filters = filters,
                    source = source,
                    searchCriteria = searchCriteria,
                    page = page,
                    pageSize = pageSize,
                )

            } catch (e: Exception) {
                if (!checkConnection()) {

                    getNewsFromCash(
                        filters = filters,
                        source = source,
                        searchCriteria = searchCriteria,
                        page = page,
                        pageSize = pageSize,
                    ).apply { if (isEmpty()) throw NoConnectionException() }

                } else throw e
            }
        }
    }

    override suspend fun getNewsByCategory(
        filters: Filters,
        category: NewsCategory,
        searchCriteria: String?,
        page: Int,
        pageSize: Int,
    ): List<News> {
        return withContext(Dispatchers.IO) {
            try {
                getNewsByCategoryFromServer(
                    filters = filters,
                    category = category,
                    searchCriteria = searchCriteria,
                    page = page,
                    pageSize = pageSize,
                )

            } catch (e: Exception) {
                if (!checkConnection()) {

                    getNewsFromCash(
                        filters = filters,
                        category = category,
                        searchCriteria = searchCriteria,
                        page = page,
                        pageSize = pageSize,
                    ).apply { if (isEmpty()) throw NoConnectionException() }

                } else throw e
            }
        }
    }

    override fun getNewsByCategoryFuture(
        filters: Filters,
        category: NewsCategory,
        searchCriteria: String?,
        page: Int,
        pageSize: Int,
    ): CompletableFuture<List<News>> {
        return GlobalScope.future {
            getNewsByCategory(filters, category, searchCriteria, page, pageSize)
        }
    }

    override fun getNewsWithFiltersFuture(
        filters: Filters,
        searchCriteria: String?,
        page: Int,
        pageSize: Int,
    ): CompletableFuture<List<News>> {
        return GlobalScope.future {
            getNews(filters, null, searchCriteria, page, pageSize)
        }
    }

    override suspend fun clearOldCache() {
        withContext(Dispatchers.IO) {
            with(newsDao.getOldCache()) {
                newsDao.delete(this)
                forEach { news ->
                    news.imageUrl?.let { removeImgFromGlideCache(it) }
                }
            }

            with(sourceDao.getOldCache()) {
                sourceDao.delete(this)
                forEach { source ->
                    removeImgFromGlideCache(getSourceIconUrl(source.siteUrl))
                }
            }
        }
    }

    override suspend fun getSavedNews(filters: Filters): List<News> {
        return withContext(Dispatchers.IO) {
            with(filters) {
                newsDao.getNews(
                    language = language?.value,
                    fromDate = dates?.first,
                    toDate = dates?.second,
                    isSaved = true,
                )
            }
        }
    }

    override suspend fun findSavedNews(
        searchCriteria: String,
        filters: Filters,
    ): List<News> {
        return withContext(Dispatchers.IO) {
            getNewsFromCash(
                filters = filters,
                searchCriteria = searchCriteria,
                isSaved = true,
            )
        }
    }

    override suspend fun newsIsSaved(news: News): Flow<Boolean> {
        return flow {
            var isSaved: Boolean
            withContext(Dispatchers.IO) {
                isSaved = newsDao.newsIsSaved(news.title)
            }
            emit(isSaved)
        }
    }

    override fun addNewsToSaved(news: News): Flow<Boolean> {
        return flow {
            newsDao.updateNews(news.copy(isSaved = true))
            emit(true)
        }
    }

    override fun removeNewsFromSaved(news: News): Flow<Boolean> {
        return flow {
            newsDao.updateNews(news.copy(isSaved = false))
            emit(true)
        }
    }

    override fun dataGettingFrom(): DataSource {
        return if (checkConnection()) DataSource.SERVER
        else DataSource.CASH
    }

    private suspend fun getNewsFromServer(
        filters: Filters,
        source: NewsSource?,
        searchCriteria: String?,
        page: Int,
        pageSize: Int,
    ): List<News> {
        return with(filters) {
            newsApi
                .getNews(
                    language = language?.value,
                    sources = source?.id,
                    fromDate = DateConverterHelper.format(
                        DateConverterHelper.Formatter.NEWS_API_DATE,
                        dates?.first,
                    ),
                    toDate = DateConverterHelper.format(
                        DateConverterHelper.Formatter.NEWS_API_DATE,
                        dates?.second,
                    ),
                    sortBy = orderBy?.value,
                    searchCriteria = searchCriteria ?: "*",
                    pageSize = pageSize.takeIf { page != ALL_PAGE },
                    page = page.takeIf { page != ALL_PAGE },
                )
                .items
                .also { items ->
                    addNewsToCash(items, filters, null, source)
                }
        }
    }

    private suspend fun getNewsByCategoryFromServer(
        filters: Filters,
        category: NewsCategory,
        searchCriteria: String?,
        page: Int,
        pageSize: Int,
    ): List<News> {
        return with(filters) {
            newsApi
                .getNewsByCategory(
                    category = category.title,
                    language = language?.value,
                    fromDate = DateConverterHelper.format(
                        DateConverterHelper.Formatter.NEWS_API_DATE,
                        dates?.first,
                    ),
                    toDate = DateConverterHelper.format(
                        DateConverterHelper.Formatter.NEWS_API_DATE,
                        dates?.second,
                    ),
                    sortBy = orderBy?.value,
                    searchCriteria = searchCriteria,
                    pageSize = pageSize.takeIf { page != ALL_PAGE },
                    page = page.takeIf { page != ALL_PAGE },
                )
                .items
                .also { items ->
                    addNewsToCash(items, filters, category)
                }
        }
    }

    private suspend fun getNewsFromCash(
        filters: Filters,
        source: NewsSource? = null,
        category: NewsCategory? = null,
        searchCriteria: String? = null,
        isSaved: Boolean? = null,
        page: Int = ALL_PAGE,
        pageSize: Int = ALL_PAGE,
    ): List<News> {
        return with(filters) {
            if (page == ALL_PAGE) {
                newsDao.getNews(
                    source = source?.name,
                    category = category?.title,
                    searchCriteria = searchCriteria,
                    language = language?.value,
                    fromDate = dates?.first,
                    toDate = dates?.second,
                    isSaved = isSaved,
                )
            } else {
                newsDao.getNews(
                    source = source?.name,
                    category = category?.title,
                    searchCriteria = searchCriteria,
                    language = language?.value,
                    fromDate = dates?.first,
                    toDate = dates?.second,
                    isSaved = isSaved,
                    page = page,
                    pageSize = pageSize,
                )
            }
        }
    }

    private suspend fun addNewsToCash(
        news: List<News>,
        filters: Filters,
        category: NewsCategory? = null,
        source: NewsSource? = null,
    ) {
        with(filters) {
            val currentCash = newsDao.getNews(
                language?.value,
                category?.title,
                source?.name
            )

            val newsForCash = news.map { news ->
                val cashedNews = currentCash.firstOrNull { it.newsUrl == news.newsUrl }
                news.copy(
                    language = cashedNews?.language ?: language?.value,
                    category = cashedNews?.category ?: category?.title,
                    cashDate = Date().time,
                )
            }

            newsDao.addNews(newsForCash)
        }
    }

    private fun removeImgFromGlideCache(url: String) {
        try {
            Glide.with(appContext)
                .downloadOnly()
                .load(url)
                .submit()
                .get()
                .delete()
        } catch (_: Exception) {
        }
    }

    private fun checkConnection(): Boolean {
        val connectivityManager =
            appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        return capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false
                || capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false
                || capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ?: false
    }

    private fun getSourceIconUrl(siteUrl: String): String {
        return appContext.getString(R.string.icon_search_url_template, siteUrl)
    }
}