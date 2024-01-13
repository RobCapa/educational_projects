package ru.aston.fragments.saved_news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ru.aston.data.News
import ru.aston.interfaces.repositories.FiltersRepository
import ru.aston.interfaces.repositories.NewsRepository
import ru.aston.utils.SingleLiveEvent
import javax.inject.Inject

class SavedNewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val filtersRepository: FiltersRepository,
) : ViewModel() {

    private val _savedNews = MutableLiveData<List<News>>()
    val savedNews: LiveData<List<News>>
        get() = _savedNews

    private val _searchResult = MutableLiveData<List<News>>()
    val searchResult: LiveData<List<News>>
        get() = _searchResult

    private val _exception = SingleLiveEvent<Exception?>()
    val exception: LiveData<Exception?>
        get() = _exception

    private val handler = CoroutineExceptionHandler { _, ex ->
        _exception.value = ex as Exception
    }

    fun loadSavedNews() {
        viewModelScope.launch(handler) {
            _savedNews.value = newsRepository.getSavedNews(filtersRepository.filters)
        }
    }

    fun findSavedNews(searchCriteria: String) {
        viewModelScope.launch(handler) {
            _searchResult.value = newsRepository.findSavedNews(
                searchCriteria,
                filtersRepository.filters,
            )
        }
    }

    fun clearSearchResult() {
        _searchResult.value = emptyList()
    }

    fun clearException() {
        if (_exception.value != null) _exception.value = null
    }
}