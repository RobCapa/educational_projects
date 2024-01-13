package ru.aston.fragments.category_news.main.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import ru.aston.data.NewsCategory;
import ru.aston.fragments.category_news.list.CategoryNewsListFragment;

public class CategoryViewPagerAdapter extends FragmentStateAdapter {

    NewsCategory[] categories;

    Fragment[] fragments;

    public CategoryViewPagerAdapter(
            @NonNull FragmentActivity fragmentActivity,
            NewsCategory[] categories
    ) {
        super(fragmentActivity);
        this.categories = categories;
        fragments = new Fragment[categories.length];
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = fragments[position];

        if (fragment == null) {
            NewsCategory category = categories[position];
            fragment = CategoryNewsListFragment.newInstance(category);
            fragments[position] = fragment;
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return categories.length;
    }
}
