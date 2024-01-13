package ru.aston.fragments.sources

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
import ru.aston.data.NewsSource
import ru.aston.databinding.FragmentSourcesBinding
import ru.aston.fragments.exception.ExceptionHandlerFragment
import ru.aston.interfaces.actions.GetFiltersHash
import ru.aston.interfaces.navigation.SourcesContainerNavigation
import ru.aston.interfaces.ui_managing.RefreshUI
import ru.aston.interfaces.ui_managing.SearchManaging
import ru.aston.interfaces.ui_managing.StatusBarManaging
import ru.aston.interfaces.ui_managing.ToolbarManaging
import ru.aston.recycler.adapters.SourceRecyclerAdapter
import ru.aston.utils.ViewModelFactory
import javax.inject.Inject

class SourcesFragment :
    Fragment(R.layout.fragment_sources),
    RefreshUI {

    private val binding: FragmentSourcesBinding by viewBinding()

    private val viewModel: SourceViewModel by viewModels { viewModelFactory }

    private val recyclerAdapter = SourceRecyclerAdapter(::navToSourceNewsFrag)

    private val recyclerAdapterSearchResult = SourceRecyclerAdapter(
        ::navToSourceNewsFrag,
        SourceRecyclerAdapter.Mode.SEARCH_MODE,
    )

    private var oldFiltersHash = -1
    private var needScrollRecyclerViewToStart = false

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<SourceViewModel>

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as? StatusBarManaging)?.showStatusBar(this)
        observeToLiveData()
        configureToolbar()
        configureRecyclerView()
        configureSearch()
        configureSwipeRefreshLayout()

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
            binding.fragSourcesProgressIndicatorLoading.isVisible = true
            oldFiltersHash = currentFiltersHash
            viewModel.loadSources()
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
            viewModel::findSources,
            viewModel::clearSearchResult,
        )
    }

    private fun observeToLiveData() {
        viewModel.sources.observe(viewLifecycleOwner) { items ->
            viewModel.clearException()
            recyclerAdapter.updateItems(items)
            binding.fragSourcesProgressIndicatorLoading.isVisible = false
            binding.fragSourcesIncludeMissingItems.root.isVisible = items.isEmpty()
        }

        viewModel.searchResult.observe(viewLifecycleOwner) { items ->
            recyclerAdapterSearchResult.updateItems(items)
        }

        viewModel.exception.observe(viewLifecycleOwner, ::handleException)
    }

    private fun handleException(ex: Exception?) {
        if (ex != null) showException(ex)
        else hideException()
    }

    private fun showException(ex: Exception) {
        binding.fragSourcesSwipeRefreshLayout.isVisible = false
        binding.fragSourcesProgressIndicatorLoading.isVisible = false
        binding.fragSourcesIncludeMissingItems.root.isVisible = false
        binding.fragSourcesFragmentViewException.isVisible = true

        childFragmentManager.commit {
            replace(
                binding.fragSourcesFragmentViewException.id,
                ExceptionHandlerFragment.newInstance(ex),
                EXCEPTION_TAG,
            )
        }
    }

    private fun hideException() {
        binding.fragSourcesSwipeRefreshLayout.isVisible = true
        binding.fragSourcesFragmentViewException.isVisible = false

        childFragmentManager.findFragmentByTag(EXCEPTION_TAG)?.let {
            childFragmentManager.commit {
                remove(it)
            }
        }
    }

    private fun navigation(navigate: SourcesContainerNavigation.() -> Unit) {
        (parentFragment as? SourcesContainerNavigation)?.navigate()
    }

    private fun configureToolbar() {
        with(requireActivity() as ToolbarManaging) {
            val toolbarActions: Map<ToolbarManaging.ToolbarItem, () -> Unit> = mapOf(
                ToolbarManaging.ToolbarItem.FILTERS_ITEM to { navigation { navToFiltersFrag() } },
            )

            setTitleToolbar(
                this@SourcesFragment,
                null,
                getString(R.string.frag_sources),
            )

            addActionsToToolbar(
                this@SourcesFragment,
                toolbarActions,
                true,
                null,
            )
        }
    }

    private fun navToSourceNewsFrag(source: NewsSource) {
        viewModel.clearSearchResult()
        (requireActivity() as SearchManaging).hideSearch()
        navigation { navToSourceNewsFrag(source) }
    }

    private fun configureRecyclerView() {
        with(binding.fragSourcesRecyclerView) {

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

    private fun configureSwipeRefreshLayout() {
        with(binding.fragSourcesSwipeRefreshLayout) {
            setOnRefreshListener {
                isRefreshing = false
                loadItems(true)
            }
        }
    }

    companion object {
        private const val EXCEPTION_TAG = "EXCEPTION_TAG"

        fun newInstance(): SourcesFragment {
            return SourcesFragment()
        }
    }
}