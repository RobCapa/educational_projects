package com.example.myunsplash.fragments.collections

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.myunsplash.R
import com.example.myunsplash.databinding.FragCollectionsBinding
import com.example.myunsplash.recycler.adapters.CollectionsRecyclerAdapter
import com.example.myunsplash.recycler.adapters.ItemLoadStateAdapter
import com.example.myunsplash.utils.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@AndroidEntryPoint
class CollectionsFragment : Fragment(R.layout.frag_collections) {
    private val binding: FragCollectionsBinding by viewBinding()
    private val viewModel: CollectionsViewModel by viewModels()
    private var returningAfterPopBackStack = false
    private val recyclerViewAdapter = CollectionsRecyclerAdapter(::processClickOnItem)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureRecyclerView()
        configureToolbar()
        configureSwipeRefreshLayout()
        observeToLiveData()
        if (recyclerViewAdapter.snapshot().isEmpty()) {
            viewModel.getCollectionsFlow()
        }
    }

    private fun configureSwipeRefreshLayout() {
        with(binding.fragCollectionsSwipeRefreshLayout) {
            setOnRefreshListener {
                isRefreshing = false
                viewModel.getCollectionsFlow()
            }
        }
    }

    private fun configureToolbar() {
        with(binding.fragCollectionsIncludeToolbar.toolbar.menu) {
            setGroupVisible(R.id.menuToolbar_group_all, false)
            findItem(R.id.menuToolbar_search).isVisible = true
        }
        with(binding.fragCollectionsIncludeToolbar.toolbar) {
            title = getString(R.string.page_collections)
            findViewById<SearchView>(R.id.menuToolbar_search)
                .setOnQueryTextListener(getSearchTextListener())
        }
    }

    private fun getSearchTextListener(): SearchView.OnQueryTextListener {
        return object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(newText: String?): Boolean {
                if (newText != null && newText.isNotBlank()) {
                    viewModel.searchCollections(newText)
                } else {
                    viewModel.getCollectionsFlow()
                }
                binding.fragCollectionsRecyclerView.scrollToPosition(0)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (returningAfterPopBackStack) {
                    returningAfterPopBackStack = false
                    return true
                }
                if (newText == null || newText.isBlank()) {
                    this.onQueryTextSubmit("")
                }
                return true
            }
        }
    }

    private fun configureRecyclerView() {
        with(binding.fragCollectionsRecyclerView) {
            adapter = recyclerViewAdapter.withLoadStateFooter(
                footer = ItemLoadStateAdapter { recyclerViewAdapter.retry() }
            )
            layoutManager = LinearLayoutManager(requireContext())
            viewTreeObserver
                .addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
        }
    }

    private fun observeToLiveData() {
        viewModel.collectionsLiveDataFlow.observe(viewLifecycleOwner) { flow ->
            lifecycleScope.launch {
                flow.distinctUntilChanged().collectLatest { data ->
                    recyclerViewAdapter.submitData(data)
                }
            }
        }
        viewModel.exception.observe(viewLifecycleOwner, ::handleException)
    }

    private fun processClickOnItem(position: Int, view: View) {
        returningAfterPopBackStack = true
        val item = recyclerViewAdapter.snapshot()[position] ?: error("Invalid index")
        val extras = FragmentNavigatorExtras(
            view to (ViewCompat.getTransitionName(view) ?: "null")
        )
        val direction = CollectionsFragmentDirections
            .actionFavoritesFragmentToListCollectionPhotos(item.id)
        findNavController().navigate(direction, extras)
    }

    private fun handleException(ex: Throwable) {
        when (ex) {
            is SocketTimeoutException, is UnknownHostException -> {
                binding.root.showSnackbar(R.string.notification_noConnection)
            }
        }
    }
}