package ru.aston.recycler.holders

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.aston.R
import ru.aston.data.News
import ru.aston.databinding.ItemNewsBinding
import ru.aston.utils.changeColorRandomly
import java.net.URI

class NewsHolder(
    private val binding: ItemNewsBinding,
    private val onClickOnItemCallback: (News) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(news: News) {
        with(binding) {
            with(news) {
                root.setOnClickListener { onClickOnItemCallback(news) }

                itemNewsTextViewSourceName.text = sourceName
                itemNewsTextViewDescription.text = description

                newsUrl?.let { url ->
                    val logoUrl =
                        root.context.getString(R.string.icon_search_url_template, URI(url).host)

                    Glide.with(root)
                        .load(logoUrl)
                        .placeholder(R.drawable.logo_placeholder)
                        .error(R.drawable.logo_placeholder)
                        .into(itemNewsImageViewSourceLogo)
                }

                val color = root.context.changeColorRandomly(listOf(R.color.blue_4), (0..40 step 5))
                binding.itemNewsImageViewCover.setBackgroundColor(color)

                Glide.with(root)
                    .load(imageUrl)
                    .placeholder(R.drawable.news_placeholder)
                    .error(R.drawable.news_placeholder)
                    .into(itemNewsImageViewCover)
            }
        }
    }
}