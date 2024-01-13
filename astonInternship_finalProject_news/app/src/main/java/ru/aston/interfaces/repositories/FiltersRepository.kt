package ru.aston.interfaces.repositories

import ru.aston.data.Filters

interface FiltersRepository {
    var filters: Filters
    fun getCurrentFiltersHash(): Int
    fun getCountFilters(): Int
}