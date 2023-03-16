package com.example.myunsplash.repositories

import androidx.paging.PagingSource
import androidx.paging.PagingState

class Source<T : Any>(
    private val getItems: suspend (Int, Int) -> List<T>
) : PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: 1
        return try {
            val items = getItems(page, params.loadSize)
            LoadResult.Page(
                items,
                if (page == 1) null else page - 1,
                if (items.isNotEmpty()) page + 1 else null,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}