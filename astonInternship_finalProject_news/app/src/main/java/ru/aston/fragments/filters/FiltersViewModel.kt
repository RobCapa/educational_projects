package ru.aston.fragments.filters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.aston.data.Filters
import ru.aston.interfaces.repositories.FiltersRepository
import javax.inject.Inject

class FiltersViewModel @Inject constructor(
    private val filtersRepository: FiltersRepository
) : ViewModel() {

    private val _currentFilters: MutableLiveData<Filters> = MutableLiveData()
    val currentFilters: LiveData<Filters> = _currentFilters

    fun setFilters(filters: Filters) {
        viewModelScope.launch {
            filtersRepository.filters = filters
        }
    }

    fun getCurrentFilters() {
        viewModelScope.launch {
            _currentFilters.value = filtersRepository.filters
        }
    }
}