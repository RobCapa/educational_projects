package ru.aston.recycler.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class InfiniteScrollListener(
    private val linearLayoutManager: LinearLayoutManager,
    private val listener: OnLoadMoreListener,
) : RecyclerView.OnScrollListener() {

    var hasMoreItems = true

    private var isLoading = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dx == 0 && dy == 0) return

        val totalItemCount = linearLayoutManager.itemCount
        val lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()

        if (!isLoading
            && hasMoreItems
            && totalItemCount != 0
            && totalItemCount <= lastVisibleItem + VISIBLE_THRESHOLD
        ) {
            listener.onLoadMore()
            isLoading = true
        }
    }

    fun setLoadingCompleted() {
        isLoading = false
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    companion object {
        private const val VISIBLE_THRESHOLD = 2
    }
}