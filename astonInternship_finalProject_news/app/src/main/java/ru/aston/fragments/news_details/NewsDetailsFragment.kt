package ru.aston.fragments.news_details

import android.content.Context
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.aston.NewsApp
import ru.aston.R
import ru.aston.data.News
import ru.aston.databinding.FragmentNewsDetailsBinding
import ru.aston.interfaces.ui_managing.StatusBarManaging
import ru.aston.interfaces.ui_managing.ToolbarManaging
import ru.aston.utils.DateConverterHelper
import ru.aston.utils.ViewModelFactory
import javax.inject.Inject


class NewsDetailsFragment : Fragment(R.layout.fragment_news_details) {

    private val binding: FragmentNewsDetailsBinding by viewBinding()

    private val viewModel: NewsDetailsViewModel by viewModels { viewModelFactory }

    private lateinit var news: News

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<NewsDetailsViewModel>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as NewsApp).appComponent.inject(this)

        news = arguments?.getParcelable(NEWS_EXTRA, News::class.java)
            ?: throw RuntimeException("Missing value by NEWS_EXTRA")
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (!hidden) {
            (requireActivity() as? StatusBarManaging)?.hideStatusBar(this)
            configureToolbar()
            viewModel.checkNewsIsSaved(news)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as? StatusBarManaging)?.hideStatusBar(this)

        observeToLiveData()
        configureToolbar()
        bind()
        viewModel.checkNewsIsSaved(news)
    }

    private fun configureToolbar() {
        with(requireActivity() as ToolbarManaging) {
            val toolbarActions = mapOf(
                ToolbarManaging.ToolbarItem.NOT_SAVED_ITEM to {
                    viewModel.newsIsSaved.value?.let { isSaved ->
                        if (isSaved) viewModel.removeNews(news)
                        else viewModel.saveNews(news)
                    }
                },
            )

            setTitleToolbar(
                this@NewsDetailsFragment,
                news.imageUrl,
                news.title,
            )

            addActionsToToolbar(
                this@NewsDetailsFragment,
                toolbarActions,
                false,
            ) { requireActivity().onBackPressed() }
        }
    }

    private fun observeToLiveData() {
        viewModel.newsIsSaved.observe(viewLifecycleOwner) { isSaved ->
            val oldToolbarItem = if (isSaved) ToolbarManaging.ToolbarItem.NOT_SAVED_ITEM
            else ToolbarManaging.ToolbarItem.SAVED_ITEM

            val newToolbarItem = if (!isSaved) ToolbarManaging.ToolbarItem.NOT_SAVED_ITEM
            else ToolbarManaging.ToolbarItem.SAVED_ITEM

            val action = if (isSaved) {
                { viewModel.removeNews(news) }
            } else {
                { viewModel.saveNews(news) }
            }

            (requireActivity() as ToolbarManaging).replaceToolbarItem(
                this,
                oldToolbarItem,
                newToolbarItem,
                action
            )
        }
    }

    private fun bind() {
        with(news) {
            with(binding) {
                fragNewsDetailsTextViewTitle.text = title
                fragNewsDetailsTextViewDate.text = date?.let { originalDate ->
                    DateConverterHelper.convertFromFormatsToFormat(
                        listOf(
                            DateConverterHelper.Formatter.NEWS_API_DATE_TIME_1,
                            DateConverterHelper.Formatter.NEWS_API_DATE_TIME_2,
                        ),
                        DateConverterHelper.Formatter.APP_DATE_TIME,
                        originalDate,
                    )
                }

                with(fragNewsDetailsTextViewContent) {
                    val contentWithUrl = if (content != null) {
                        val regex = """\[\+\d+ chars]""".toRegex()

                        if (content.contains(regex)) {
                            content.replace(regex) {
                                "<a href='$newsUrl'>${it.value}</a>"
                            }
                        } else {
                            "<a href='$newsUrl'>$content</a>"
                        }

                    } else {
                        "<a href='$newsUrl'>$newsUrl</a>"
                    }

                    movementMethod = LinkMovementMethod.getInstance()
                    text = Html.fromHtml(contentWithUrl, HtmlCompat.FROM_HTML_MODE_LEGACY)
                }

                fragNewsDetailsTextViewSourceName.text = sourceName
            }
        }
    }

    companion object {
        private const val NEWS_EXTRA = "news_extra"

        fun newInstance(news: News): NewsDetailsFragment {
            return NewsDetailsFragment().apply {
                arguments = bundleOf(
                    NEWS_EXTRA to news,
                )
            }
        }
    }
}