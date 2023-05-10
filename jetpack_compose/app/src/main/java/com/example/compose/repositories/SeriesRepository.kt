package com.example.compose.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.compose.data.*
import kotlinx.coroutines.flow.Flow

object SeriesRepository {
fun getEpisodesFlow(
    seriesId: Int,
    seasonNumber: Int,
    pagingConfig: PagingConfig? = null,
): Flow<PagingData<Episode>> {
    return getPagingDataFlow(pagingConfig) { page, size ->
        // page, counts are not used because a mock database placeholder is used
        getEpisodes(seriesId, seasonNumber)
    }
}

    fun getSeasonsFlow(
        seriesId: Int,
        pagingConfig: PagingConfig? = null,
    ): Flow<PagingData<Season>> {
        return getPagingDataFlow(pagingConfig) { page, size ->
            // page, counts are not used because a mock database placeholder is used
            getSeasons(seriesId)
        }
    }

    fun getSeasonMomentsFlow(
        seriesId: Int,
        seasonNumber: Int,
        pagingConfig: PagingConfig? = null,
    ): Flow<PagingData<SeasonMoment>> {
        return getPagingDataFlow(pagingConfig) { page, size ->
            // page, counts are not used because a mock database placeholder is used
            getSeasonMoments(seriesId, seasonNumber)
        }
    }

    fun getPersonagesFlow(
        seriesId: Int,
        pagingConfig: PagingConfig? = null,
    ): Flow<PagingData<Personage>> {
        return getPagingDataFlow(pagingConfig) { page, size ->
            // page, counts are not used because a mock database placeholder is used
            getPersonages(seriesId)
        }
    }
    fun getSeriesFlow(
        pagingConfig: PagingConfig? = null,
    ): Flow<PagingData<Series>> {
        return getPagingDataFlow(pagingConfig) { page, size ->
            // page, counts are not used because a mock database placeholder is used
            getListSeries()
        }
    }

    fun getSeason(
        seriesId: Int,
        seasonNumber: Int,
    ): Season? {
        if (seriesId == -1 || seasonNumber == -1) return null
        return Database.seasons.firstOrNull {
            it.seriesId == seriesId && it.number == seasonNumber
        }
    }

    fun getSeries(id: Int): Series? {
        return Database.series.firstOrNull { it.id == id }
    }

    private fun <T : Any> getPagingDataFlow(
        pagingConfig: PagingConfig?,
        getItems: suspend (Int, Int) -> List<T>
    ): Flow<PagingData<T>> {
        return Pager(
            pagingConfig ?: getDefaultPageConfig(),
            pagingSourceFactory = {
                Source(getItems)
            }
        ).flow
    }

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = 30, enablePlaceholders = false)
    }

    private fun getEpisodes(
        seriesId: Int,
        seasonNumber: Int,
    ): List<Episode> {
        if (seriesId == -1 || seasonNumber == -1) return emptyList()
        return Database.episodes.filter {
            it.seriesId == seriesId && it.seasonNumber == seasonNumber
        }
    }

    private fun getSeasons(seriesId: Int): List<Season> {
        if (seriesId == -1) return emptyList()
        return Database.seasons.filter { it.seriesId == seriesId }
    }

    private fun getPersonages(seriesId: Int): List<Personage> {
        if (seriesId == -1) return emptyList()
        return Database.personages.filter { it.seriesId == seriesId }
    }

    private fun getSeasonMoments(
        seriesId: Int,
        seasonNumber: Int,
    ): List<SeasonMoment> {
        if (seriesId == -1 || seasonNumber == -1) return emptyList()
        return Database.seasonMoments.filter {
            it.seriesId == seriesId && it.seasonNumber == seasonNumber
        }
    }

    private fun getListSeries(): List<Series> {
        return Database.series
    }
}