package ru.aston.recycler.holders

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.aston.R
import ru.aston.data.NewsSource
import ru.aston.databinding.ItemSourceBinding

class SourceSearchHolder(
    private val binding: ItemSourceBinding,
    private val onClickOnItemCallback: (NewsSource) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: NewsSource) {
        with(item) {
            with(binding) {
                val backgroundColor = root.context.getColor(R.color.blue_2)
                val textColor = root.context.getColor(R.color.white)

                root.setBackgroundColor(backgroundColor)
                itemSourceTextViewSourceName.setTextColor(textColor)
                itemSourceTextViewInfo.setTextColor(textColor)

                root.setOnClickListener { onClickOnItemCallback(item) }

                itemSourceTextViewSourceName.text = name
                itemSourceTextViewInfo.text = "%s | %s".format(
                    category.run {
                        substring(0, 1).uppercase() + substring(1).lowercase()
                    },
                    country.uppercase()
                )

                val logoUrl = root.context.getString(R.string.icon_search_url_template, siteUrl)
                Glide.with(root)
                    .load(logoUrl)
                    .placeholder(R.drawable.logo_placeholder)
                    .error(R.drawable.logo_placeholder)
                    .into(itemSourceImageViewIcon)
            }
        }
    }
}