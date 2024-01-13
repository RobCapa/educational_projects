package ru.aston.fragments.category_news.list;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import kotlin.Unit;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import ru.aston.NewsApp;
import ru.aston.R;
import ru.aston.data.News;
import ru.aston.data.NewsCategory;
import ru.aston.databinding.FragmentCategoryNewsListBinding;
import ru.aston.fragments.exception.ExceptionHandlerFragment;
import ru.aston.interfaces.actions.GetFiltersHash;
import ru.aston.interfaces.mvp_views.CategoryNewsListView;
import ru.aston.interfaces.navigation.CategoryNewsContainerNavigation;
import ru.aston.interfaces.ui_managing.RefreshUI;
import ru.aston.recycler.adapters.NewsRecyclerAdapter;
import ru.aston.recycler.utils.InfiniteScrollListener;

public class CategoryNewsListFragment
        extends MvpAppCompatFragment
        implements CategoryNewsListView, RefreshUI, InfiniteScrollListener.OnLoadMoreListener {

    static final String EXCEPTION_TAG = "EXCEPTION_TAG";
    static final String CATEGORY_EXTRA = "CATEGORY_EXTRA";

    int oldFiltersHash = -1;
    boolean needScrollRecyclerViewToStart = false;

    FragmentCategoryNewsListBinding binding;

    NewsCategory category;

    InfiniteScrollListener infiniteScrollListener;

    @InjectPresenter
    CategoryNewsListPresenter categoryNewsListPresenter;

    NewsRecyclerAdapter recyclerAdapter = new NewsRecyclerAdapter(
            news -> {
                navToNewsDetailsFrag(news);
                return Unit.INSTANCE;
            },
            NewsRecyclerAdapter.Mode.STANDARD_MODE
    );

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        category = requireArguments().getParcelable(CATEGORY_EXTRA);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            loadItems();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCategoryNewsListBinding.inflate(inflater);

        configureRecyclerView();
        configureSwipeRefreshLayout();
        loadItems();

        return binding.getRoot();
    }

    @Override
    public void refresh() {
        loadItems(true);
    }

    @Override
    public void showNews(List<News> news, boolean resetItems) {
        recyclerAdapter.removeProgress();

        List<News> newsToAdd;
        if (resetItems) {
            newsToAdd = news;
            needScrollRecyclerViewToStart = true;
        } else {
            newsToAdd = Stream.of(recyclerAdapter.getCurrentList(), news)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
        }

        infiniteScrollListener.setHasMoreItems(!news.isEmpty());

        if (newsToAdd.isEmpty()) {
            binding.fragCategoryNewsListIncludeMissingItems.getRoot().setVisibility(View.VISIBLE);
        } else {
            binding.fragCategoryNewsListIncludeMissingItems.getRoot().setVisibility(View.INVISIBLE);
            recyclerAdapter.updateItems(newsToAdd);
        }

        infiniteScrollListener.setLoadingCompleted();
    }

    @Override
    public void onLoadMore() {
        loadItems(true);
    }

    @Override
    public void handleException(Exception ex) {
        if (ex != null) showException(ex);
        else hideException();
    }

    void showException(Exception ex) {
        recyclerAdapter.removeProgress();
        binding.fragCategoryNewsListSwipeRefreshLayout.setVisibility(View.GONE);
        binding.fragCategoryNewsListIncludeMissingItems.getRoot().setVisibility(View.GONE);
        binding.fragCategoryNewsListFragmentViewException.setVisibility(View.VISIBLE);

        getChildFragmentManager()
                .beginTransaction()
                .replace(
                        binding.fragCategoryNewsListFragmentViewException.getId(),
                        ExceptionHandlerFragment.newInstance(ex),
                        EXCEPTION_TAG
                )
                .commit();
    }

    void hideException() {
        binding.fragCategoryNewsListSwipeRefreshLayout.setVisibility(View.VISIBLE);
        binding.fragCategoryNewsListFragmentViewException.setVisibility(View.GONE);

        Fragment fragment = getChildFragmentManager().findFragmentByTag(EXCEPTION_TAG);

        if (fragment != null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .remove(fragment)
                    .commit();
        }
    }

    CategoryNewsContainerNavigation getCategoryNewsNavigation() {
        CategoryNewsContainerNavigation categoryNewsContainerNavigation;

        try {
            categoryNewsContainerNavigation = (CategoryNewsContainerNavigation) getParentFragmentManager()
                    .getFragments()
                    .stream()
                    .filter(frag -> frag instanceof CategoryNewsContainerNavigation)
                    .findFirst()
                    .get();
        } catch (NoSuchElementException ex) {
            categoryNewsContainerNavigation = (CategoryNewsContainerNavigation) getParentFragment().getParentFragment();
        }

        return categoryNewsContainerNavigation;
    }

    void navToNewsDetailsFrag(News news) {
        getCategoryNewsNavigation().navToNewsDetailsFrag(news);
    }

    void configureSwipeRefreshLayout() {
        binding.fragCategoryNewsListSwipeRefreshLayout.setOnRefreshListener(() -> {
            binding.fragCategoryNewsListSwipeRefreshLayout.setRefreshing(false);
            categoryNewsListPresenter.resetCurrentPage();
            loadItems(true);
        });
    }

    void configureRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        infiniteScrollListener = new InfiniteScrollListener(layoutManager, this);

        binding.fragCategoryNewsListRecyclerView.addOnScrollListener(infiniteScrollListener);
        binding.fragCategoryNewsListRecyclerView.setAdapter(recyclerAdapter);
        binding.fragCategoryNewsListRecyclerView.setLayoutManager(layoutManager);

        recyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                if (needScrollRecyclerViewToStart) {
                    binding.fragCategoryNewsListRecyclerView.scrollToPosition(0);
                    needScrollRecyclerViewToStart = false;
                }
            }
        });

        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
        );
        Drawable divider = ContextCompat.getDrawable(requireContext(), R.drawable.item_divider);
        itemDecoration.setDrawable(divider);

        binding.fragCategoryNewsListRecyclerView.addItemDecoration(itemDecoration);
    }

    void loadItems(Boolean ignoreFilters) {
        try {
            int currentFiltersHash = ((GetFiltersHash) requireActivity()).getCurrentFiltersHash();
            boolean filtersWasChanged = currentFiltersHash != oldFiltersHash;

            if (filtersWasChanged) {
                needScrollRecyclerViewToStart = true;
                categoryNewsListPresenter.resetCurrentPage();
                infiniteScrollListener.setHasMoreItems(true);
                oldFiltersHash = currentFiltersHash;
            }

            if (ignoreFilters || filtersWasChanged) {
                hideException();
                recyclerAdapter.addProgress();
                categoryNewsListPresenter.loadCategoryNews(category);
            }
        } catch (Exception ignored) {
        }
    }

    void loadItems() {
        loadItems(false);
    }

    @ProvidePresenter
    CategoryNewsListPresenter providePresenter() {
        return ((NewsApp) requireActivity().getApplication())
                .getAppComponent()
                .provideCategoryNewsListPresenter();
    }

    public static CategoryNewsListFragment newInstance(NewsCategory category) {
        Bundle args = new Bundle();

        args.putParcelable(CATEGORY_EXTRA, category);

        CategoryNewsListFragment fragment = new CategoryNewsListFragment();
        fragment.setArguments(args);

        return fragment;
    }
}
