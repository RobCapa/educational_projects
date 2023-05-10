package com.example.compose.repositories

import androidx.paging.PagingSource
import androidx.paging.PagingState

class Source<T: Any>(
    private val getItems: suspend (Int, Int) -> List<T>
) : PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1)
            ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: 1
        val pageSize = params.loadSize

        return try {
            val items = getItems(page, pageSize)
            LoadResult.Page(
                items,
                if (page == 1) null else page - 1,
                if (items.size < pageSize) null else page + 1,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}