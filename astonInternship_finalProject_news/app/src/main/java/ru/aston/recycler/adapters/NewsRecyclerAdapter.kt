package ru.aston.recycler.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.aston.data.News
import ru.aston.databinding.ItemNewsBinding
import ru.aston.databinding.ItemProgressBinding
import ru.aston.recycler.diff_utils.NewsDiffUtil
import ru.aston.recycler.holders.NewsHolder
import ru.aston.recycler.holders.NewsSearchHolder
import ru.aston.recycler.holders.ProgressHolder
import ru.aston.recycler.holders.StubViewHolder

class NewsRecyclerAdapter(
    private val onClickOnItemCallback: (News) -> Unit,
    private val mode: Mode = Mode.STANDARD_MODE,
) : RecyclerView.Adapter<ViewHolder>() {

    private val differ = AsyncListDiffer(this, NewsDiffUtil())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            Mode.STANDARD_MODE.viewType -> {
                ItemNewsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ).let { NewsHolder(it, onClickOnItemCallback) }
            }

            Mode.SEARCH_MODE.viewType -> {
                ItemNewsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ).let { NewsSearchHolder(it, onClickOnItemCallback) }
            }

            VIEW_TYPE_PROGRESS -> {
                ItemProgressBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ).let { ProgressHolder(it) }
            }

            else -> StubViewHolder(View(parent.context))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]

        when (holder.itemViewType) {
            Mode.STANDARD_MODE.viewType -> {
                (holder as NewsHolder).bind(item)
            }

            Mode.SEARCH_MODE.viewType -> {
                (holder as NewsSearchHolder).bind(item)
            }

            VIEW_TYPE_PROGRESS -> {
                (holder as ProgressHolder).bind(differ.currentList.size == 1)
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun getItemViewType(position: Int): Int {
        val item = differ.currentList.getOrNull(position)

        return if (item != null) mode.viewType
        else VIEW_TYPE_PROGRESS
    }

    fun updateItems(items: List<News>) {
        differ.submitList(items)
    }

    fun getCurrentList(): List<News> = differ.currentList.filterNotNull()

    fun addProgress() {
        if (differ.currentList.contains(null)) return

        differ.currentList
            .toMutableList()
            .apply { add(null) }
            .let { items -> updateItems(items) }
    }

    fun removeProgress() {
        differ.currentList
            .toMutableList()
            .apply { remove(null) }
            .let { items -> updateItems(items) }
    }

    enum class Mode(val viewType: Int) {
        STANDARD_MODE(0),
        SEARCH_MODE(1),
    }

    companion object {
        private const val VIEW_TYPE_PROGRESS = -1
    }
}
