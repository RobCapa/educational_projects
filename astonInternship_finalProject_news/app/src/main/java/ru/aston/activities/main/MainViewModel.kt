package ru.aston.activities.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.aston.interfaces.repositories.FiltersRepository
import ru.aston.interfaces.repositories.NewsRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val filtersRepository: FiltersRepository,
) : ViewModel() {

    fun clearOldCache() {
        viewModelScope.launch {
            newsRepository.clearOldCache()
        }
    }

    fun getCurrentFiltersHash(): Int {
        return filtersRepository.getCurrentFiltersHash()
    }

    fun getCountFilters(): Int {
        return filtersRepository.getCountFilters()
    }
}