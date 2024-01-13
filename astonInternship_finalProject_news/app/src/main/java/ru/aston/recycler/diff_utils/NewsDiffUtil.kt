package ru.aston.recycler.diff_utils

import androidx.recyclerview.widget.DiffUtil
import ru.aston.data.News

class NewsDiffUtil : DiffUtil.ItemCallback<News>() {
    override fun areItemsTheSame(oldItem: News, newItem: News) =
        oldItem.newsUrl == newItem.newsUrl

    override fun areContentsTheSame(
        oldItem: News,
        newItem: News
    ) = oldItem == newItem
}