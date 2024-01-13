package ru.aston.repositories

import android.content.SharedPreferences
import com.google.gson.Gson
import ru.aston.data.Filters
import ru.aston.interfaces.repositories.FiltersRepository
import javax.inject.Inject

class FiltersRepositoryImpl @Inject constructor(
    private val filtersSharedPref: SharedPreferences,
) : FiltersRepository {

    private val gson = Gson()

    override var filters: Filters = restoreFilters()
        get() = field.copy()
        set(newFilters) {
            if (newFilters != field) {
                field = newFilters
                saveFilters()
            }
        }

    override fun getCurrentFiltersHash(): Int {
        return filters.hashCode()
    }

    override fun getCountFilters(): Int {
        var count = 0
        if (filters.dates != null) count++
        if (filters.language != null) count++
        if (filters.orderBy != null) count++
        return count
    }

    private fun saveFilters() {
        filtersSharedPref.edit()
            .putString(FILTERS_KEY, gson.toJson(filters))
            .apply()
    }

    private fun restoreFilters(): Filters {
        val json = filtersSharedPref.getString(FILTERS_KEY, null)

        return if (json != null) gson.fromJson(json, Filters::class.java)
        else Filters()
    }

    companion object {
        private const val FILTERS_KEY = "filters_key"
    }
}