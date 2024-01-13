package ru.aston.fragments.category_news.main;

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
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import ru.aston.NewsApp;
import ru.aston.R;
import ru.aston.data.News;
import ru.aston.data.NewsCategory;
import ru.aston.databinding.FragmentCategoryNewsBinding;
import ru.aston.fragments.category_news.list.CategoryNewsListFragment;
import ru.aston.fragments.category_news.main.utils.CategoryViewPagerAdapter;
import ru.aston.interfaces.mvp_views.CategoryNewsView;
import ru.aston.interfaces.navigation.CategoryNewsContainerNavigation;
import ru.aston.interfaces.ui_managing.SearchManaging;
import ru.aston.interfaces.ui_managing.StatusBarManaging;
import ru.aston.interfaces.ui_managing.ToolbarManaging;
import ru.aston.recycler.adapters.NewsRecyclerAdapter;
import ru.aston.utils.ExtensionsKt;

public class CategoryNewsFragment
        extends MvpAppCompatFragment
        implements CategoryNewsView {

    private static final String NEWS_WITH_FILTERS_TAG = "NEWS_WITH_FILTERS_TAG";

    FragmentCategoryNewsBinding binding;

    @InjectPresenter
    CategoryNewsPresenter categoryNewsPresenter;

    NewsRecyclerAdapter recyclerAdapterSearchResult = new NewsRecyclerAdapter(
            news -> {
                navToNewsDetailsFrag(news);
                return Unit.INSTANCE;
            },
            NewsRecyclerAdapter.Mode.SEARCH_MODE
    );

    Map<NewsCategory, Integer> tabs = new TreeMap<>() {
        {
            Arrays.stream(NewsCategory.values()).forEach(newsCategory -> {
                switch (newsCategory) {
                    case GENERAL -> put(newsCategory, R.drawable.ic_general);
                    case BUSINESS -> put(newsCategory, R.drawable.ic_business);
                    case ENTERTAINMENT -> put(newsCategory, R.drawable.ic_entertainment);
                    case SCIENCE -> put(newsCategory, R.drawable.ic_science);
                    case HEALTH -> put(newsCategory, R.drawable.ic_health);
                    case TECHNOLOGY -> put(newsCategory, R.drawable.ic_technology);
                    case SPORT -> put(newsCategory, R.drawable.ic_sport);
                }
            });
        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ((StatusBarManaging) requireActivity()).showStatusBar(this);
            categoryNewsPresenter.checkIfFiltersSuitableForCategory();
            configureToolbar();
            configureSearch();
            notifyChildFragOnHiddenChanged();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCategoryNewsBinding.inflate(inflater);

        categoryNewsPresenter.checkIfFiltersSuitableForCategory();
        configureToolbar();
        configureSearch();
        configureTabs();

        binding.fragCategoryNewsViewPager.setOffscreenPageLimit(tabs.size());

        binding.fragCategoryNewsViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                notifyChildFragOnHiddenChanged();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void hideOrShowNewsWithFilters(Boolean show) {
        if (show) addNewsWithFilters();
        else removeNewsWithFilters();
    }

    @Override
    public void showFoundNews(List<News> news) {
        recyclerAdapterSearchResult.updateItems(news);
    }

    void notifyChildFragOnHiddenChanged() {
        Fragment fragment = getChildFragmentManager().findFragmentByTag(NEWS_WITH_FILTERS_TAG);
        if (fragment == null) fragment = getCurrentFragInViewPager();

        if (fragment != null) fragment.onHiddenChanged(false);
    }

    Fragment getCurrentFragInViewPager() {
        Fragment fragment = null;

        try {
            int currentItemPosition = binding.fragCategoryNewsViewPager.getCurrentItem();
            FragmentStateAdapter adapter = (FragmentStateAdapter) binding.fragCategoryNewsViewPager.getAdapter();
            fragment = ExtensionsKt.getItem(adapter, currentItemPosition);
        } catch (Exception ignore) {
        }

        return fragment;
    }

    void removeNewsWithFilters() {
        binding.fragCategoryNewsViewPager.setVisibility(View.VISIBLE);
        binding.fragCategoryNewsTabLayoutCategories.setVisibility(View.VISIBLE);
        binding.fragCategoryNewsFragmentViewNewsWithFilters.setVisibility(View.GONE);

        Fragment fragment = getChildFragmentManager().findFragmentByTag(NEWS_WITH_FILTERS_TAG);

        if (fragment != null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .remove(fragment)
                    .commit();
        }
    }

    void addNewsWithFilters() {
        binding.fragCategoryNewsViewPager.setVisibility(View.GONE);
        binding.fragCategoryNewsTabLayoutCategories.setVisibility(View.GONE);
        binding.fragCategoryNewsFragmentViewNewsWithFilters.setVisibility(View.VISIBLE);

        Fragment fragment = getChildFragmentManager().findFragmentByTag(NEWS_WITH_FILTERS_TAG);

        if (fragment == null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .add(
                            binding.fragCategoryNewsFragmentViewNewsWithFilters.getId(),
                            CategoryNewsListFragment.newInstance(null),
                            NEWS_WITH_FILTERS_TAG
                    )
                    .commit();
        }
    }

    void configureTabs() {
        CategoryViewPagerAdapter viewPageAdapter = new CategoryViewPagerAdapter(
                requireActivity(),
                NewsCategory.values()
        );

        binding.fragCategoryNewsViewPager.setAdapter(viewPageAdapter);

        new TabLayoutMediator(
                binding.fragCategoryNewsTabLayoutCategories,
                binding.fragCategoryNewsViewPager,
                (tab, position) -> {
                    NewsCategory newsCategory = tabs.keySet().toArray(new NewsCategory[0])[position];
                    tab.setIcon(tabs.get(newsCategory));
                    tab.setText(newsCategory.getTitle());
                }).attach();
    }

    void configureToolbar() {
        ToolbarManaging toolbarManaging = (ToolbarManaging) requireActivity();
        CategoryNewsContainerNavigation categoryNewsContainerNavigation = (CategoryNewsContainerNavigation) getParentFragment();

        toolbarManaging.setTitleToolbar(
                this,
                null,
                getString(R.string.frag_headlines)
        );

        Map<ToolbarManaging.ToolbarItem, ? extends Function0<Unit>> toolbarActions = new HashMap<>() {
            {
                put(ToolbarManaging.ToolbarItem.FILTERS_ITEM, () -> {
                    categoryNewsContainerNavigation.navToFiltersFrag();
                    return Unit.INSTANCE;
                });
            }
        };

        toolbarManaging.addActionsToToolbar(
                this,
                toolbarActions,
                true,
                null
        );
    }

    void configureSearch() {
        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
        );
        Drawable divider = ContextCompat.getDrawable(requireContext(), R.drawable.item_divider);
        itemDecoration.setDrawable(divider);

        ((SearchManaging) requireActivity()).configureSearch(
                this,
                recyclerAdapterSearchResult,
                new LinearLayoutManager(requireContext()),
                List.of(itemDecoration),
                searchCriteria -> {
                    int currentItem = binding.fragCategoryNewsViewPager.getCurrentItem();
                    NewsCategory category = NewsCategory.values()[currentItem];
                    categoryNewsPresenter.findCategoryNews(searchCriteria, category);
                    return Unit.INSTANCE;
                },
                () -> {
                    categoryNewsPresenter.clearSearchResult();
                    return Unit.INSTANCE;
                }
        );
    }

    void navToNewsDetailsFrag(News news) {
        categoryNewsPresenter.clearSearchResult();
        ((SearchManaging) requireActivity()).hideSearch();
        ((CategoryNewsContainerNavigation) getParentFragment()).navToNewsDetailsFrag(news);
    }

    @ProvidePresenter
    CategoryNewsPresenter providePresenter() {
        return ((NewsApp) requireActivity().getApplication())
                .getAppComponent()
                .provideCategoryNewsPresenter();
    }

    public static CategoryNewsFragment newInstance() {
        Bundle args = new Bundle();

        CategoryNewsFragment fragment = new CategoryNewsFragment();
        fragment.setArguments(args);

        return fragment;
    }
}
