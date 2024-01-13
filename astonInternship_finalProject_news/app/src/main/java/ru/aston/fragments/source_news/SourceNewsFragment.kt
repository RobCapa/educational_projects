package ru.aston.fragments.source_news

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.aston.NewsApp
import ru.aston.R
import ru.aston.data.News
import ru.aston.data.NewsSource
import ru.aston.databinding.FragmentSourceNewsBinding
import ru.aston.fragments.exception.ExceptionHandlerFragment
import ru.aston.interfaces.actions.GetFiltersHash
import ru.aston.interfaces.navigation.SourcesContainerNavigation
import ru.aston.interfaces.ui_managing.RefreshUI
import ru.aston.interfaces.ui_managing.SearchManaging
import ru.aston.interfaces.ui_managing.StatusBarManaging
import ru.aston.interfaces.ui_managing.ToolbarManaging
import ru.aston.recycler.adapters.NewsRecyclerAdapter
import ru.aston.utils.ViewModelFactory
import javax.inject.Inject


class SourceNewsFragment : Fragment(R.layout.fragment_source_news), RefreshUI {

    private val binding: FragmentSourceNewsBinding by viewBinding()

    private val viewModel: SourceNewsViewModel by viewModels { viewModelFactory }

    private val recyclerAdapter = NewsRecyclerAdapter(::navToNewsDetailsFrag)

    private val recyclerAdapterSearchResult = NewsRecyclerAdapter(
        ::navToNewsDetailsFrag,
        NewsRecyclerAdapter.Mode.SEARCH_MODE,
    )

    private lateinit var source: NewsSource

    private var oldFiltersHash = -1
    private var needScrollRecyclerViewToStart = false

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<SourceNewsViewModel>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as NewsApp).appComponent.inject(this)

        source = arguments?.getParcelable(SOURCE_EXTRA, NewsSource::class.java)
            ?: throw RuntimeException("Missing value by SOURCE_EXTRA")
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            (requireActivity() as? StatusBarManaging)?.hideStatusBar(this)
            configureToolbar()
            configureSearch()
            loadItems()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as? StatusBarManaging)?.hideStatusBar(this)
        configureRecyclerView()
        configureSwipeRefreshLayout()
        observeToLiveData()
        configureToolbar()
        configureSearch()

        loadItems()
    }

    override fun refresh() {
        loadItems(true)
    }

    private fun loadItems(ignoreFilters: Boolean = false) {
        val currentFiltersHash = (requireActivity() as GetFiltersHash).getCurrentFiltersHash()
        val filtersWasChanged = currentFiltersHash != oldFiltersHash

        needScrollRecyclerViewToStart = filtersWasChanged

        if (ignoreFilters || filtersWasChanged) {
            hideException()
            binding.fragSourceNewsProgressIndicatorLoading.isVisible = true
            oldFiltersHash = currentFiltersHash
            viewModel.loadSourceNews(source)
        }
    }

    private fun configureSwipeRefreshLayout() {
        with(binding.fragSourceNewsSwipeRefreshLayout) {
            setOnRefreshListener {
                isRefreshing = false
                viewModel.loadSourceNews(source)
            }
        }
    }

    private fun configureToolbar() {
        with(requireActivity() as ToolbarManaging) {
            setTitleToolbar(
                this@SourceNewsFragment,
                null,
                source.name,
            )

            val toolbarActions = mapOf(
                ToolbarManaging.ToolbarItem.FILTERS_ITEM to { navigation { navToFiltersFrag() } },
            )

            addActionsToToolbar(
                this@SourceNewsFragment,
                toolbarActions,
                true,
            ) { requireActivity().onBackPressed() }
        }
    }

    private fun navigation(navigate: SourcesContainerNavigation.() -> Unit) {
        (parentFragment as? SourcesContainerNavigation)?.navigate()
    }

    private fun handleException(ex: Exception?) {
        if (ex != null) showException(ex)
        else hideException()
    }

    private fun showException(ex: Exception) {
        (requireActivity() as? StatusBarManaging)?.showStatusBar(this)

        binding.fragSourceNewsSwipeRefreshLayout.isVisible = false
        binding.fragSourceNewsIncludeMissingItems.root.isVisible = false
        binding.fragSourceNewsProgressIndicatorLoading.isVisible = false
        binding.fragSourceNewsFragmentViewException.isVisible = true

        childFragmentManager.commit {
            replace(
                binding.fragSourceNewsFragmentViewException.id,
                ExceptionHandlerFragment.newInstance(ex),
                EXCEPTION_TAG,
            )
        }
    }

    private fun hideException() {
        (requireActivity() as? StatusBarManaging)?.hideStatusBar(this)

        binding.fragSourceNewsSwipeRefreshLayout.isVisible = true
        binding.fragSourceNewsFragmentViewException.isVisible = false

        childFragmentManager.findFragmentByTag(EXCEPTION_TAG)?.let {
            childFragmentManager.commit {
                remove(it)
            }
        }
    }

    private fun observeToLiveData() {
        viewModel.sourceNews.observe(viewLifecycleOwner) { items ->
            viewModel.clearException()
            recyclerAdapter.updateItems(items)
            binding.fragSourceNewsProgressIndicatorLoading.isVisible = false
            binding.fragSourceNewsIncludeMissingItems.root.isVisible = items.isEmpty()
        }
        viewModel.searchResult.observe(viewLifecycleOwner) { items ->
            recyclerAdapterSearchResult.updateItems(items)
        }
        viewModel.exception.observe(viewLifecycleOwner, ::handleException)
    }

    private fun navToNewsDetailsFrag(news: News) {
        viewModel.clearSearchResult()
        (requireActivity() as SearchManaging).hideSearch()
        navigation { navToNewsDetailsFrag(news) }
    }

    private fun configureRecyclerView() {
        with(binding.fragSourceNewsRecyclerView) {

            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(requireContext())

            recyclerAdapter.registerAdapterDataObserver(object :
                RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(
                    positionStart: Int,
                    itemCount: Int
                ) {
                    if (needScrollRecyclerViewToStart) {
                        scrollToPosition(0)
                        needScrollRecyclerViewToStart = false
                    }
                }
            })

            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                ).apply {
                    ContextCompat
                        .getDrawable(requireContext(), R.drawable.item_divider)
                        ?.let { setDrawable(it) }
                }
            )

        }
    }

    private fun configureSearch() {
        val itemDecoration = DividerItemDecoration(
            requireContext(),
            DividerItemDecoration.VERTICAL
        ).apply {
            ContextCompat
                .getDrawable(requireContext(), R.drawable.item_divider)
                ?.let { setDrawable(it) }
        }

        (requireActivity() as SearchManaging).configureSearch(
            this,
            recyclerAdapterSearchResult,
            LinearLayoutManager(requireContext()),
            listOf(itemDecoration),
            { searchCriteria ->
                viewModel.findSourceNews(
                    searchCriteria,
                    source
                )
            },
            viewModel::clearSearchResult,
        )
    }

    companion object {
        private const val EXCEPTION_TAG = "EXCEPTION_TAG"
        private const val SOURCE_EXTRA = "SOURCE_EXTRA"

        fun newInstance(source: NewsSource): SourceNewsFragment {
            return SourceNewsFragment().apply {
                arguments = bundleOf(
                    SOURCE_EXTRA to source,
                )
            }
        }
    }
}