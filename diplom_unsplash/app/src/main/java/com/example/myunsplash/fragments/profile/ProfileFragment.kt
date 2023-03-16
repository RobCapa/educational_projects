package com.example.myunsplash.fragments.profile

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.myunsplash.R
import com.example.myunsplash.contracts.LogoutContract
import com.example.myunsplash.data.Photo
import com.example.myunsplash.data.UnsplashCollection
import com.example.myunsplash.data.User
import com.example.myunsplash.databinding.FragProfileBinding
import com.example.myunsplash.exceptions.WorkAlreadyExistsException
import com.example.myunsplash.recycler.adapters.ItemLoadStateAdapter
import com.example.myunsplash.recycler.adapters.ProfileRecyclerAdapter
import com.example.myunsplash.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment: Fragment(R.layout.frag_profile) {
    private val binding: FragProfileBinding by viewBinding()
    private val viewModel: ProfileViewModel by viewModels()
    @Inject lateinit var logoutContract: LogoutContract
    private var currentTabId = R.id.fragProfile_textView_photosTab
    private lateinit var currentUser: User
    private lateinit var logoutLauncher: ActivityResultLauncher<String?>
    private val recyclerViewAdapter = ProfileRecyclerAdapter(
        ::processClickOnItem,
        ::likeOrUnlikePhoto,
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureRecyclerView()
        configureToolbar()
        configureSwipeRefreshLayout()
        observeToLiveData()
        if (recyclerViewAdapter.snapshot().isEmpty()) {
            viewModel.getCurrentUser()
        } else {
            bind()
            configureTabSwitch()
        }

        logoutLauncher = registerForActivityResult(
            logoutContract,
        ) {
            viewModel.processResultLogoutRequest(it)
            requireActivity().restart()
        }
    }

    private fun configureSwipeRefreshLayout() {
        with(binding.fragProfileSwipeRefreshLayout) {
            setOnRefreshListener {
                isRefreshing = false
                loadSuitableFlow()
            }
        }
    }

    private fun configureToolbar() {
        with(binding.fragProfileIncludeToolbar.toolbar.menu) {
            setGroupVisible(R.id.menuToolbar_group_all, false)
            findItem(R.id.menuToolbar_exit).isVisible = true
        }
        with(binding.fragProfileIncludeToolbar.toolbar) {
            title = getString(R.string.page_profile)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menuToolbar_exit -> {
                        ExitDialog(::logout).show(
                            childFragmentManager,
                            ExitDialog.TAG
                        )
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun logout() {
        logoutLauncher.launch(null)
    }

    private fun processClickOnItem(position: Int, view: View) {
        val extras = FragmentNavigatorExtras(
            view to (ViewCompat.getTransitionName(view) ?: "null")
        )
        val direction = when (
            val item = recyclerViewAdapter.snapshot()[position] ?: error("Invalid index")
        ) {
            is Photo -> {
                ProfileFragmentDirections.actionProfileFragmentToPhotoDetailsFragment(item.id)
            }
            is UnsplashCollection -> {
                ProfileFragmentDirections.actionProfileFragmentToListCollectionPhotos(item.id)
            }
            else -> error("Invalid direction")
        }
        findNavController().navigate(direction, extras)
    }

    private fun configureRecyclerView() {
        recyclerViewAdapter.addLoadStateListener { loadState ->
            binding.fragProfileTextViewNoItems.isVisible = (loadState.source.refresh
                    is LoadState.NotLoading
                    && loadState.append.endOfPaginationReached
                    && recyclerViewAdapter.itemCount < 1)
        }

        with(binding.fragProfileRecyclerView) {
            adapter = recyclerViewAdapter.withLoadStateFooter(
                footer = ItemLoadStateAdapter { recyclerViewAdapter.retry() }
            )
            layoutManager = LinearLayoutManager(requireContext())
            postponeEnterTransition()
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            viewTreeObserver
                .addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
        }
    }

    private fun bind() {
        with(currentUser) {
            binding.fragProfileTextViewName.text = name
            binding.fragProfileTextViewUserName.text = "@$username"
            if (bio != null) {
                binding.fragProfileTextViewBio.isVisible = true
                binding.fragProfileTextViewLocation.text = bio
            }
            if (location != null) {
                binding.fragProfileImageViewLocation.isVisible = true
                binding.fragProfileTextViewLocation.isVisible = true
                binding.fragProfileTextViewLocation.text = location
            }
            if (email != null && email.isNotBlank()) {
                binding.fragProfileImageViewEmail.isVisible = true
                binding.fragProfileTextViewEmail.isVisible = true
                binding.fragProfileTextViewEmail.text = email
            }
            binding.fragProfileTextViewDownloads.text = downloads
            binding.fragProfileImageViewDownload.isVisible = true

            Glide.with(binding.root)
                .load(avatarUrl)
                .into(binding.fragProfileImageViewAvatar)
        }
        changeColorForClickedTab()
    }

    private fun configureTabSwitch() {
        binding.fragProfileTextViewPhotosTab.text =
            getString(R.string.fragProfile_photosTab).format(currentUser.totalPhotos)
        binding.fragProfileTextViewFavoritesTab.text =
            getString(R.string.fragProfile_favoritesTab).format(currentUser.totalLikes)
        binding.fragProfileTextViewCollectionsTab.text =
            getString(R.string.fragProfile_collectionsTab).format(currentUser.totalCollections)
        setOnClickOnTab()
    }

    private fun changeColorForClickedTab() {
        val accentColor = requireContext().getIntFromTheme(R.attr.accentColor)
        val normalColor = requireContext().getIntFromTheme(R.attr.mainTextColor)
        getAllTabs().forEach {
            it.setTextColor(if (currentTabId == it.id) accentColor else normalColor)
        }
    }

    private fun getAllTabs(): List<TextView> {
        return binding.fragProfileGroupMenuTabs
            .getViews()
            .map { it as TextView }
    }

    private fun setOnClickOnTab() {
        getAllTabs().forEach { view ->
            view.setOnClickListener { clickedView ->
                currentTabId = clickedView.id
                loadSuitableFlow()
            }
        }
    }

    private fun loadSuitableFlow() {
        when (currentTabId) {
            R.id.fragProfile_textView_photosTab -> {
                viewModel.getUserPhotosFlow(currentUser.username)
            }
            R.id.fragProfile_textView_favoritesTab -> {
                viewModel.getFavoritesPhotosFlow(currentUser.username)
            }
            R.id.fragProfile_textView_collectionsTab -> {
                viewModel.getCollectionsFlow(currentUser.username)
            }
        }
        changeColorForClickedTab()
    }

    private fun likeOrUnlikePhoto(
        photoId: String,
        like: Boolean,
        positionInAdapter: Int
    ) {
        viewModel.likeOrUnlikePhoto(photoId, like, positionInAdapter)
    }

    private fun observeToLiveData() {
        viewModel.currentUser.observe(viewLifecycleOwner) { user ->
            currentUser = user
            bind()
            configureTabSwitch()
            loadSuitableFlow()
        }
        viewModel.itemsLiveDataFlow.observe(viewLifecycleOwner) { flow ->
            lifecycleScope.launch {
                flow.distinctUntilChanged().collectLatest { data ->
                    recyclerViewAdapter.submitData(data.map { it })
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
            is WorkAlreadyExistsException -> {
                binding.root.showSnackbar(R.string.notification_photoIsAlreadyBeingDownloaded)
            }
        }
    }
}