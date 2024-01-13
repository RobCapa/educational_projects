package ru.aston.recycler.diff_utils

import androidx.recyclerview.widget.DiffUtil
import ru.aston.data.NewsSource

class SourceDiffUtil : DiffUtil.ItemCallback<NewsSource>() {
    override fun areItemsTheSame(oldItem: NewsSource, newItem: NewsSource) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: NewsSource,
        newItem: NewsSource
    ) = oldItem == newItem
}