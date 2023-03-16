package com.example.myunsplash.fragments.list_collections_photo

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.myunsplash.R
import com.example.myunsplash.data.UnsplashCollection
import com.example.myunsplash.databinding.FragListCollectionPhotosBinding
import com.example.myunsplash.recycler.adapters.ItemLoadStateAdapter
import com.example.myunsplash.recycler.adapters.PhotosRecyclerAdapter
import com.example.myunsplash.recycler.decorations.PhotoItemDecoration
import com.example.myunsplash.utils.getIntFromTheme
import com.example.myunsplash.utils.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@AndroidEntryPoint
class ListCollectionPhotos : Fragment(R.layout.frag_list_collection_photos) {
    private val binding: FragListCollectionPhotosBinding by viewBinding()
    private val viewModel: ListCollectionPhotosViewModel by viewModels()
    private val args: ListCollectionPhotosArgs by navArgs()
    private val recyclerViewAdapter = PhotosRecyclerAdapter(
        ::likeOrUnlikePhoto,
        ::processClickOnItem,
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTransitionName()
        observeToLiveData()
        configureRecyclerView()
        configureSwipeRefreshLayout()
        configureToolbar()
        viewModel.getCollection(args.collectionId)
        if (recyclerViewAdapter.snapshot().isEmpty()) {
            viewModel.getCollectionPhotos(args.collectionId)
        }
    }

    private fun setTransitionName() {
        ViewCompat.setTransitionName(
            binding.fragListCollectionPhotosIncludeCollection.itemCollectionImageViewPhoto,
            "collection_${args.collectionId}"
        )
    }

    private fun configureSwipeRefreshLayout() {
        with(binding.fragListCollectionPhotosSwipeRefreshLayout) {
            setOnRefreshListener {
                isRefreshing = false
                viewModel.getCollectionPhotos(args.collectionId)
            }
        }
    }

    private fun configureToolbar() {
        with(binding.fragCollectionPhotosIncludeToolbar.toolbar.menu) {
            setGroupVisible(R.id.menuToolbar_group_all, false)
        }
        with(binding.fragCollectionPhotosIncludeToolbar.toolbar) {
            title = getString(R.string.page_collections)
        }
    }

    private fun configureRecyclerView() {
        with(binding.fragListCollectionPhotosRecyclerView) {
            val spanCount = 2
            adapter = recyclerViewAdapter.withLoadStateFooter(
                footer = ItemLoadStateAdapter { recyclerViewAdapter.retry() }
            )
            layoutManager = StaggeredGridLayoutManager(
                spanCount,
                StaggeredGridLayoutManager.VERTICAL
            )
            postponeEnterTransition()
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

    private fun observeToLiveData() {
        viewModel.exception.observe(viewLifecycleOwner, ::handleException)
        viewModel.unsplashCollectionLiveData.observe(viewLifecycleOwner, ::bindCollection)
        viewModel.collectionPhotosLiveDataFlow.observe(viewLifecycleOwner) { flow ->
            lifecycleScope.launch {
                flow.distinctUntilChanged().collectLatest { data ->
                    recyclerViewAdapter.submitData(data)
                }
            }
        }
        viewModel.likeInfoLiveData.observe(viewLifecycleOwner) {
            recyclerViewAdapter.updateLikeStatusForPhoto(it.first, it.second)
        }
    }

    private fun bindCollection(unsplashCollection: UnsplashCollection) {
        with(binding.fragListCollectionPhotosIncludeCollection) {
            itemCollectionTextViewPhotosNumber.text =
                getString(R.string.collectionHolder_photosNumber)
                    .format(unsplashCollection.totalPhotos)
            itemCollectionTextViewTitle.text = unsplashCollection.title

            Glide.with(binding.root)
                .load(unsplashCollection.coverPhotoUrl)
                .into(itemCollectionImageViewPhoto)
        }

        with(binding.fragListCollectionPhotosIncludeCollection.itemCollectionIncludeInformationPlate) {
            informationPlateGroupLike.isVisible = false

            informationPlateTextViewName.text = unsplashCollection.author.name
            informationPlateTextViewUserName.text = "@${unsplashCollection.author.username}"
            Glide.with(binding.root)
                .load(unsplashCollection.author.avatarUrl)
                .into(informationPlateImageViewAuthorAvatar)
        }
    }

    private fun processClickOnItem(position: Int, photoView: View) {
        val extras = FragmentNavigatorExtras(
            photoView to (ViewCompat.getTransitionName(photoView) ?: "")
        )
        val photo = recyclerViewAdapter.snapshot()[position]
        val direction = ListCollectionPhotosDirections
            .actionListCollectionPhotosToPhotoDetailsFragment(photo?.id ?: error("Invalid index"))
        findNavController().navigate(direction, extras)
    }

    private fun likeOrUnlikePhoto(
        photoId: String,
        like: Boolean,
        positionInAdapter: Int
    ) {
        viewModel.likeOrUnlikePhoto(photoId, like, positionInAdapter)
    }

    private fun handleException(ex: Throwable) {
        when (ex) {
            is SocketTimeoutException, is UnknownHostException -> {
                binding.root.showSnackbar(R.string.notification_noConnection)
            }
        }
    }
}