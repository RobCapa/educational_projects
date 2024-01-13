package ru.aston.fragments.saved_news

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
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
import ru.aston.databinding.FragmentSavedNewsBinding
import ru.aston.fragments.exception.ExceptionHandlerFragment
import ru.aston.interfaces.actions.GetFiltersHash
import ru.aston.interfaces.navigation.SavedNewsContainerNavigation
import ru.aston.interfaces.ui_managing.RefreshUI
import ru.aston.interfaces.ui_managing.SearchManaging
import ru.aston.interfaces.ui_managing.StatusBarManaging
import ru.aston.interfaces.ui_managing.ToolbarManaging
import ru.aston.recycler.adapters.NewsRecyclerAdapter
import ru.aston.utils.ViewModelFactory
import javax.inject.Inject

class SavedNews : Fragment(R.layout.fragment_saved_news), RefreshUI {

    private val binding: FragmentSavedNewsBinding by viewBinding()

    private val viewModel: SavedNewsViewModel by viewModels { viewModelFactory }

    private val recyclerAdapter = NewsRecyclerAdapter(::navToNewsDetailsFrag)

    private val recyclerAdapterSearchResult = NewsRecyclerAdapter(
        ::navToNewsDetailsFrag,
        NewsRecyclerAdapter.Mode.SEARCH_MODE,
    )

    private var oldFiltersHash = -1
    private var needScrollRecyclerViewToStart = false

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<SavedNewsViewModel>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as NewsApp).appComponent.inject(this)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (!hidden) {
            (requireActivity() as? StatusBarManaging)?.showStatusBar(this)
            configureToolbar()
            configureSearch()
            loadItems()
        }
    }

    private fun loadItems(ignoreFilters: Boolean = false) {
        val currentFiltersHash = (requireActivity() as GetFiltersHash).getCurrentFiltersHash()
        val filtersWasChanged = currentFiltersHash != oldFiltersHash

        needScrollRecyclerViewToStart = filtersWasChanged

        if (ignoreFilters || filtersWasChanged) {
            hideException()
            binding.fragSavedNewsProgressIndicatorLoading.isVisible = true
            oldFiltersHash = currentFiltersHash
            viewModel.loadSavedNews()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as? StatusBarManaging)?.showStatusBar(this)

        configureToolbar()
        configureSearch()
        configureRecyclerView()
        configureSwipeRefreshLayout()
        observeToLiveData()

        loadItems(true)
    }

    override fun refresh() {
        loadItems(true)
    }

    private fun navToNewsDetailsFrag(news: News) {
        viewModel.clearSearchResult()
        (requireActivity() as SearchManaging).hideSearch()
        navigation { navToNewsDetailsFrag(news) }
    }

    private fun navigation(navigate: SavedNewsContainerNavigation.() -> Unit) {
        (parentFragment as? SavedNewsContainerNavigation)?.navigate()
    }

    private fun handleException(ex: Exception?) {
        if (ex != null) showException(ex)
        else hideException()
    }

    private fun showException(ex: Exception) {
        binding.fragSavedNewsSwipeRefreshLayout.isVisible = false
        binding.fragSavedNewsIncludeMissingItems.root.isVisible = false
        binding.fragSavedNewsProgressIndicatorLoading.isVisible = false
        binding.fragSavedNewsFragmentViewException.isVisible = true

        childFragmentManager.commit {
            replace(
                binding.fragSavedNewsFragmentViewException.id,
                ExceptionHandlerFragment.newInstance(ex),
                EXCEPTION_TAG,
            )
        }
    }

    private fun hideException() {
        binding.fragSavedNewsSwipeRefreshLayout.isVisible = true
        binding.fragSavedNewsFragmentViewException.isVisible = false

        childFragmentManager.findFragmentByTag(EXCEPTION_TAG)?.let {
            childFragmentManager.commit {
                remove(it)
            }
        }
    }

    private fun observeToLiveData() {
        viewModel.savedNews.observe(viewLifecycleOwner) { items ->
            viewModel.clearException()
            recyclerAdapter.updateItems(items)
            binding.fragSavedNewsProgressIndicatorLoading.isVisible = false
            binding.fragSavedNewsIncludeMissingItems.root.isVisible = items.isEmpty()
        }
        viewModel.searchResult.observe(viewLifecycleOwner) { items ->
            recyclerAdapterSearchResult.updateItems(items)
        }
        viewModel.exception.observe(viewLifecycleOwner, ::handleException)
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
            viewModel::findSavedNews,
            viewModel::clearSearchResult,
        )
    }

    private fun configureSwipeRefreshLayout() {
        with(binding.fragSavedNewsSwipeRefreshLayout) {
            setOnRefreshListener {
                isRefreshing = false
                viewModel.loadSavedNews()
            }
        }
    }

    private fun configureToolbar() {
        with(requireActivity() as ToolbarManaging) {
            val toolbarActions = mapOf(
                ToolbarManaging.ToolbarItem.FILTERS_ITEM to { navigation { navToFiltersFrag() } },
            )

            setTitleToolbar(
                this@SavedNews,
                null,
                getString(R.string.frag_saved),
            )

            addActionsToToolbar(
                this@SavedNews,
                toolbarActions,
                true,
                null,
            )
        }
    }

    private fun configureRecyclerView() {
        with(binding.fragSavedNewsRecyclerView) {

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

    companion object {
        private const val EXCEPTION_TAG = "EXCEPTION_TAG"

        fun newInstance(): SavedNews {
            return SavedNews()
        }
    }
}