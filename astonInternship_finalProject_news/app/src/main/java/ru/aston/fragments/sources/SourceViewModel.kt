package ru.aston.fragments.sources

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ru.aston.data.NewsSource
import ru.aston.interfaces.repositories.FiltersRepository
import ru.aston.interfaces.repositories.NewsRepository
import ru.aston.utils.SingleLiveEvent
import javax.inject.Inject

class SourceViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val filtersRepository: FiltersRepository,
) : ViewModel() {

    private val _sources = MutableLiveData<List<NewsSource>>()
    val sources: LiveData<List<NewsSource>>
        get() = _sources

    private val _searchResult = MutableLiveData<List<NewsSource>>()
    val searchResult: LiveData<List<NewsSource>>
        get() = _searchResult

    private val _exception = SingleLiveEvent<Exception?>()
    val exception: LiveData<Exception?>
        get() = _exception

    private val handler = CoroutineExceptionHandler { _, ex ->
        _exception.value = ex as Exception
    }

    fun loadSources() {
        viewModelScope.launch(handler) {
            val filters = filtersRepository.filters
            _sources.value = newsRepository.getSources(filters)
        }
    }

    fun findSources(name: String) {
        viewModelScope.launch(handler) {
            val filters = filtersRepository.filters
            _searchResult.value = newsRepository.findSources(name, filters)
        }
    }

    fun clearSearchResult() {
        _searchResult.value = emptyList()
    }

    fun clearException() {
        if (_exception.value != null) _exception.value = null
    }
}