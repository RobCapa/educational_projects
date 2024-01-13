package ru.aston.activities.main

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import ru.aston.NewsApp
import ru.aston.R
import ru.aston.databinding.ActivityMainBinding
import ru.aston.fragments.containers.CategoryNewsContainerFragment
import ru.aston.fragments.containers.SavedNewsContainerFragment
import ru.aston.fragments.containers.SourcesContainerFragment
import ru.aston.interfaces.actions.BackAction
import ru.aston.interfaces.actions.GetFiltersHash
import ru.aston.interfaces.ui_managing.SearchManaging
import ru.aston.interfaces.ui_managing.StatusBarManaging
import ru.aston.interfaces.ui_managing.ToolbarManaging
import ru.aston.utils.OnUpDawnOffsetListener
import ru.aston.utils.ViewModelFactory
import javax.inject.Inject


class MainActivity :
    AppCompatActivity(),
    StatusBarManaging,
    ToolbarManaging,
    SearchManaging,
    GetFiltersHash {

    private val currentVisibleFragment: Fragment?
        get() = currentContainer?.childFragmentManager?.fragments?.first()

    private val currentContainer: Fragment?
        get() = supportFragmentManager.findFragmentByTag(currentContainerTag)

    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    private var currentContainerTag: String? = null
    private var lastContainerClass: String? = null
    private var lastFragClass: String? = null

    private lateinit var binding: ActivityMainBinding

    private lateinit var badge: BadgeDrawable
    private var lastFiltersHash: Int = -1

    private lateinit var onUpDawnOffsetListener: OnUpDawnOffsetListener

    private val expandedContainerStates = mutableMapOf<Fragment, Boolean>()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<MainViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (application as NewsApp).appComponent.inject(this)

        initFiltersBadge()
        initOnUpDawnOffsetListener()
        linkSearchViewWithSearchBar()
        viewModel.clearOldCache()
        binding.activityMainBottomNav.setOnItemSelectedListener(::processTabSelection)

        if (savedInstanceState == null) {
            navToContainer(HEADLINES_TAG)
        } else {
            currentContainerTag = savedInstanceState.getString(LAST_CONTAINER_TAG_EXTRA, null)
            lastFragClass = savedInstanceState.getString(LAST_FRAG_CLASS_EXTRA, null)
            lastContainerClass = savedInstanceState.getString(LAST_CONTAINER_CLASS_EXTRA, null)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_CONTAINER_TAG_EXTRA, currentContainerTag)
        outState.putString(LAST_FRAG_CLASS_EXTRA, lastFragClass)
        outState.putString(LAST_CONTAINER_CLASS_EXTRA, lastContainerClass)
    }

    override fun onDestroy() {
        super.onDestroy()

        BadgeUtils.detachBadgeDrawable(
            badge,
            binding.activityMainToolbar,
            R.id.menuItem_filter,
        )
    }

    override fun onBackPressed() {
        currentContainer?.let { container ->
            if (container is BackAction && container.onBackPressed()) return
        }
        super.onBackPressed()
    }

    override fun getCurrentFiltersHash(): Int {
        val newFiltersHash = viewModel.getCurrentFiltersHash()
        val filtersWasChanged = newFiltersHash != lastFiltersHash

        if (filtersWasChanged) {
            lastFiltersHash = newFiltersHash
        }

        showFiltersCount()

        return lastFiltersHash
    }

    override fun hideStatusBar(callingFragment: Fragment) {
        if (!fragHasRightToMakeChanges(callingFragment)) return
        window?.statusBarColor = Color.TRANSPARENT
    }

    override fun showStatusBar(callingFragment: Fragment) {
        if (!fragHasRightToMakeChanges(callingFragment)) return
        window?.statusBarColor = getColor(R.color.blue_2)
    }

    override fun addActionsToToolbar(
        callingFragment: Fragment,
        toolbarActions: Map<ToolbarManaging.ToolbarItem, () -> Unit?>,
        showSearchItem: Boolean,
        navigationOnClickListener: (() -> Unit)?,
    ) {
        if (!fragHasRightToMakeChanges(callingFragment)) return

        with(binding.activityMainToolbar) {
            if (navigationOnClickListener == null) {
                navigationIcon = null
            } else {
                setNavigationIcon(com.google.android.material.R.drawable.ic_arrow_back_black_24)
                setNavigationOnClickListener { navigationOnClickListener() }
            }
        }

        with(binding.activityMainToolbar.menu) {
            setGroupVisible(R.id.menuToolbar_group_all, false)
            toolbarActions.entries.forEach { entry ->
                findItem(entry.key.id).apply {
                    isVisible = true
                    setOnMenuItemClickListener { entry.value.invoke() != null }
                }
            }
            findItem(R.id.menuItem_search).isVisible = showSearchItem
        }
    }

    override fun setTitleToolbar(
        callingFragment: Fragment,
        imageUrl: String?,
        title: String,
    ) {
        if (!fragHasRightToMakeChanges(callingFragment)) return

        with(binding) {
            activityMainToolbar.title = title
            activityMainImageViewPoster.isVisible = true
            activityMainCollapsingToolbar.layoutParams.height =
                resources.getDimension(R.dimen.mainCollapsingToolbar_height).toInt()

            Glide.with(root)
                .load(imageUrl)
                .placeholder(R.drawable.image_not_found)
                .error(R.drawable.image_not_found)
                .listener(object : RequestListener<Drawable> {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        activityMainCollapsingToolbar.isTitleEnabled = false
                        activityMainTopAppBar.removeOnOffsetChangedListener(onUpDawnOffsetListener)

                        activityMainImageViewPoster.isVisible = false
                        activityMainCollapsingToolbar.layoutParams.height =
                            LayoutParams.WRAP_CONTENT

                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        activityMainToolbar.title = null
                        activityMainCollapsingToolbar.isTitleEnabled = true
                        activityMainTopAppBar.addOnOffsetChangedListener(onUpDawnOffsetListener.apply {
                            onTopScrollPosition = { activityMainCollapsingToolbar.title = title }
                            reset()
                        })

                        activityMainTopAppBar.setExpanded(
                            expandedContainerStates[currentContainer] ?: true,
                            true,
                        )

                        activityMainImageViewPoster.isVisible = true
                        activityMainCollapsingToolbar.layoutParams.height =
                            resources.getDimension(R.dimen.mainCollapsingToolbar_height).toInt()

                        return false
                    }

                })
                .into(binding.activityMainImageViewPoster)
        }
    }

    override fun replaceToolbarItem(
        callingFragment: Fragment,
        oldToolbarItem: ToolbarManaging.ToolbarItem,
        newToolbarItem: ToolbarManaging.ToolbarItem,
        onClickListener: () -> Unit?
    ) {
        if (!fragHasRightToMakeChanges(callingFragment)) return

        binding.activityMainToolbar.menu.findItem(oldToolbarItem.id).apply {
            isVisible = false
        }
        binding.activityMainToolbar.menu.findItem(newToolbarItem.id).apply {
            isVisible = true
            setOnMenuItemClickListener { onClickListener() != null }
        }
        onUpDawnOffsetListener.reset()
    }

    override fun configureSearch(
        callingFragment: Fragment,
        recyclerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
        layoutManager: LayoutManager,
        itemDecorations: List<ItemDecoration>,
        onSearchRequest: (String) -> Unit,
        onHideSearchView: () -> Unit,
    ) {
        if (!fragHasRightToMakeChanges(callingFragment)) return

        with(binding) {
            with(activityMainRecyclerViewSearchResult) {
                for (i in 0 until itemDecorationCount) {
                    removeItemDecorationAt(i)
                }
                this.adapter = recyclerAdapter
                this.layoutManager = layoutManager

                itemDecorations.forEach { addItemDecoration(it) }
            }

            with(activityMainSearchView) {
                if (recyclerAdapter.itemCount > 0) show()

                toolbar.setNavigationOnClickListener {
                    hideStatusBar()
                    onHideSearchView()
                    hide()
                }

                editText.setOnEditorActionListener { textView, _, _ ->
                    onSearchRequest(textView.text.toString())
                    textView.text = ""
                    true
                }
            }
        }
    }

    override fun hideSearch() {
        binding.activityMainSearchView.hide()
    }

    private fun initFiltersBadge() {
        badge = BadgeDrawable.create(this)
        showFiltersCount()
    }

    private fun fragHasRightToMakeChanges(callingFragment: Fragment): Boolean {
        val hasRight = (currentVisibleFragment == null && lastFragClass == null)
                || callingFragment.hashCode() == currentVisibleFragment?.hashCode()
                || (currentVisibleFragment == null
                && currentContainer!!::class.toString() == lastContainerClass
                && lastFragClass == callingFragment::class.toString())

        if (hasRight) {
            lastFragClass = callingFragment::class.toString()
            lastContainerClass = currentContainer!!::class.toString()
        }

        return hasRight
    }

    private fun showFiltersCount() {
        val countFilters = viewModel.getCountFilters()

        BadgeUtils.detachBadgeDrawable(
            badge,
            binding.activityMainToolbar,
            R.id.menuItem_filter,
        )

        if (countFilters != 0) {
            badge.number = countFilters

            BadgeUtils.attachBadgeDrawable(
                badge,
                binding.activityMainToolbar,
                R.id.menuItem_filter,
            )
        }
    }

    private fun processTabSelection(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.menuItem_headlines -> {
                navToContainer(HEADLINES_TAG)
                true
            }

            R.id.menuItem_saved -> {
                navToContainer(SAVED_TAG)
                true
            }

            R.id.menuItem_sources -> {
                navToContainer(SOURCES_TAG)
                true
            }

            else -> false
        }
    }

    private fun showStatusBar() {
        window?.statusBarColor = getColor(R.color.blue_2)
    }

    private fun hideStatusBar() {
        window?.statusBarColor = Color.TRANSPARENT
    }

    private fun linkSearchViewWithSearchBar() {
        binding.activityMainToolbar
            .menu
            .findItem(R.id.menuItem_search)
            .setOnMenuItemClickListener {
                showStatusBar()
                binding.activityMainSearchView.show()
                true
            }
    }

    private fun saveExpandedContainerState() {
        currentContainer?.let { container ->
            with(binding.activityMainTopAppBar) {
                val isExpanded = height - bottom == 0
                expandedContainerStates.put(container, isExpanded)
            }
        }
    }

    private fun navToContainer(tag: String) {
        if (currentContainerTag == tag) {
            (currentContainer as BackAction).popBackStackToFirst()
            return
        }

        saveExpandedContainerState()

        val container = supportFragmentManager.findFragmentByTag(tag) ?: createContainer(tag)

        supportFragmentManager.commit {
            currentContainer?.let { hide(it) }
            currentContainerTag = tag
            show(container)
        }

        binding.activityMainSearchView.hide()
    }

    private fun createContainer(tag: String): Fragment {
        val container = when (tag) {
            HEADLINES_TAG -> CategoryNewsContainerFragment.newInstance()
            SAVED_TAG -> SavedNewsContainerFragment.newInstance()
            SOURCES_TAG -> SourcesContainerFragment.newInstance()
            else -> throw RuntimeException()
        }
        addFrag(container, tag)
        return container
    }

    private fun addFrag(frag: Fragment, tag: String) {
        supportFragmentManager.commit {
            add(
                binding.activityMainFragmentView.id,
                frag,
                tag,
            )
        }
    }

    private fun initOnUpDawnOffsetListener() {
        onUpDawnOffsetListener = OnUpDawnOffsetListener(
            { binding.activityMainCollapsingToolbar.title = "" },
            { binding.activityMainCollapsingToolbar.title = null },
        )
    }

    companion object {
        private const val HEADLINES_TAG = "HEADLINES_TAG"
        private const val SAVED_TAG = "SAVED_TAG"
        private const val SOURCES_TAG = "SOURCES_TAG"

        private const val LAST_CONTAINER_TAG_EXTRA = "LAST_CONTAINER_TAG_EXTRA"
        private const val LAST_FRAG_CLASS_EXTRA = "LAST_FRAG_CLASS_EXTRA"
        private const val LAST_CONTAINER_CLASS_EXTRA = "LAST_CONTAINER_CLASS_EXTRA"

        @JvmStatic
        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}
