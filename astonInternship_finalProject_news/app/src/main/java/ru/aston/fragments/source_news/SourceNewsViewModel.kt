package ru.aston.fragments.source_news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ru.aston.data.News
import ru.aston.data.NewsSource
import ru.aston.interfaces.repositories.FiltersRepository
import ru.aston.interfaces.repositories.NewsRepository
import ru.aston.utils.SingleLiveEvent
import javax.inject.Inject

class SourceNewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val filtersRepository: FiltersRepository,
) : ViewModel() {

    private val _sourceNews = MutableLiveData<List<News>>()
    val sourceNews: LiveData<List<News>>
        get() = _sourceNews

    private val _searchResult = MutableLiveData<List<News>>()
    val searchResult: LiveData<List<News>>
        get() = _searchResult

    private val _exception = SingleLiveEvent<Exception?>()
    val exception: LiveData<Exception?>
        get() = _exception

    private val handler = CoroutineExceptionHandler { _, ex ->
        _exception.value = ex as Exception
    }

    fun loadSourceNews(source: NewsSource) {
        viewModelScope.launch(handler) {
            _sourceNews.value = newsRepository.getNews(
                source = source,
                filters = filtersRepository.filters,
            )
        }
    }

    fun findSourceNews(
        searchCriteria: String,
        source: NewsSource,
    ) {
        viewModelScope.launch {
            _searchResult.value = try {
                newsRepository.getNews(
                    filtersRepository.filters,
                    source,
                    searchCriteria,
                )
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    fun clearSearchResult() {
        _searchResult.value = emptyList()
    }

    fun clearException() {
        if (_exception.value != null) _exception.value = null
    }
}