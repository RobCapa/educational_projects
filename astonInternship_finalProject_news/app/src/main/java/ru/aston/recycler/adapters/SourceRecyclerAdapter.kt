package ru.aston.recycler.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import ru.aston.data.NewsSource
import ru.aston.databinding.ItemSourceBinding
import ru.aston.recycler.diff_utils.SourceDiffUtil
import ru.aston.recycler.holders.SourceHolder
import ru.aston.recycler.holders.SourceSearchHolder
import ru.aston.recycler.holders.StubViewHolder

class SourceRecyclerAdapter(
    private val onClickOnItemCallback: (NewsSource) -> Unit,
    private val mode: Mode = Mode.STANDARD_MODE,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val differ = AsyncListDiffer(this, SourceDiffUtil())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Mode.STANDARD_MODE.viewType -> {
                ItemSourceBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ).let { SourceHolder(it, onClickOnItemCallback) }
            }

            Mode.SEARCH_MODE.viewType -> {
                ItemSourceBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ).let { SourceSearchHolder(it, onClickOnItemCallback) }
            }

            else -> StubViewHolder(View(parent.context))
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = differ.currentList[position]

        when (holder.itemViewType) {
            Mode.STANDARD_MODE.viewType -> {
                (holder as SourceHolder).bind(item)
            }

            Mode.SEARCH_MODE.viewType -> {
                (holder as SourceSearchHolder).bind(item)
            }
        }
    }

    override fun getItemViewType(position: Int) = mode.viewType

    fun updateItems(items: List<NewsSource>) {
        differ.submitList(items)
    }

    enum class Mode(val viewType: Int) {
        STANDARD_MODE(0),
        SEARCH_MODE(1),
    }
}