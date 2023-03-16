package com.example.myunsplash.fragments.photos

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.myunsplash.R
import com.example.myunsplash.databinding.FragPhotosBinding
import com.example.myunsplash.interfaces.PressBackButton
import com.example.myunsplash.recycler.adapters.ItemLoadStateAdapter
import com.example.myunsplash.recycler.adapters.PhotosRecyclerAdapter
import com.example.myunsplash.recycler.decorations.PhotoItemDecoration
import com.example.myunsplash.utils.getIntFromTheme
import com.example.myunsplash.utils.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@AndroidEntryPoint
class PhotosFragment : Fragment(R.layout.frag_photos) {
    private val binding: FragPhotosBinding by viewBinding()
    private val viewModel: PhotosViewModel by viewModels()
    private var returningAfterPopBackStack = false
    private val recyclerViewAdapter = PhotosRecyclerAdapter(
        ::likeOrUnlikePhoto,
        ::navToPhotoDetails
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureRecyclerView()
        observeToLiveData()
        configureToolbar()
        configureSwipeRefreshLayout()
        if (recyclerViewAdapter.snapshot().isEmpty()) {
            viewModel.getPhotosFlow()
        }
    }

    private fun configureSwipeRefreshLayout() {
        with(binding.fragPhotosSwipeRefreshLayout) {
            setOnRefreshListener {
                isRefreshing = false
                viewModel.getPhotosFlow()
            }
        }
    }

    private fun configureToolbar() {
        with(binding.fragPhotosIncludeToolbar.toolbar.menu) {
            setGroupVisible(R.id.menuToolbar_group_all, false)
            findItem(R.id.menuToolbar_search).isVisible = true
        }
        with(binding.fragPhotosIncludeToolbar.toolbar) {
            title = getString(R.string.page_home)
            findViewById<SearchView>(R.id.menuToolbar_search).apply {
                setOnQueryTextListener(getSearchTextListener())
            }
        }
    }

    private fun getSearchTextListener(): SearchView.OnQueryTextListener {
        return object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(newText: String?): Boolean {
                if (newText != null && newText.isNotBlank()) {
                    viewModel.searchPhotos(newText)
                } else {
                    viewModel.getPhotosFlow()
                }
                binding.fragPhotosRecyclerView.scrollToPosition(0)
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
        with(binding.fragPhotosRecyclerView) {
            val spanCount = 2
            adapter = recyclerViewAdapter.withLoadStateFooter(
                footer = ItemLoadStateAdapter { recyclerViewAdapter.retry() }
            )
            layoutManager = StaggeredGridLayoutManager(
                spanCount,
                StaggeredGridLayoutManager.VERTICAL
            )
            postponeEnterTransition()
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            viewTreeObserver
                .addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
            val spacing = requireContext().getIntFromTheme(R.attr.paddingPhotosInFeed)
            setPadding(spacing, spacing, spacing, spacing)
            clipToPadding = false
            clipChildren = false
            addItemDecoration(PhotoItemDecoration(spacing))
        }
    }

    private fun navToPhotoDetails(position: Int, photoView: View) {
        returningAfterPopBackStack = true
        val extras = FragmentNavigatorExtras(
            photoView to (ViewCompat.getTransitionName(photoView) ?: "")
        )
        val photo = recyclerViewAdapter.snapshot()[position]
        val direction = PhotosFragmentDirections
            .actionListPhotosFragmentToPhotoDetailsFragment(photo?.id ?: error("Invalid index"))

        findNavController().navigate(direction, extras)
    }

    private fun likeOrUnlikePhoto(
        photoId: String,
        like: Boolean,
        positionInAdapter: Int
    ) {
        viewModel.likeOrUnlikePhoto(photoId, like, positionInAdapter)
    }

    private fun observeToLiveData() {
        viewModel.photosLiveDataFlow.observe(viewLifecycleOwner) { flow ->
            lifecycleScope.launch {
                flow.distinctUntilChanged().collectLatest { data ->
                    recyclerViewAdapter.submitData(data)
                }
            }
        }
        viewModel.likeInfoLiveData.observe(viewLifecycleOwner) {
            recyclerViewAdapter.updateLikeStatusForPhoto(it.first, it.second)
        }
        viewModel.exception.observe(viewLifecycleOwner, ::handleException)
    }

    private fun handleException(ex: Throwable) {
        when (ex) {
            is SocketTimeoutException, is UnknownHostException -> {
                binding.root.showSnackbar(R.string.notification_noConnection)
            }
        }
    }
}