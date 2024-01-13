package ru.aston.fragments.containers

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.aston.R
import ru.aston.data.News
import ru.aston.databinding.FragmentCategoryNewsContainerBinding
import ru.aston.fragments.category_news.main.CategoryNewsFragment
import ru.aston.fragments.filters.FiltersFragment
import ru.aston.fragments.news_details.NewsDetailsFragment
import ru.aston.interfaces.actions.BackAction
import ru.aston.interfaces.navigation.CategoryNewsContainerNavigation

class CategoryNewsContainerFragment : Fragment(R.layout.fragment_category_news_container),
    BackAction,
    CategoryNewsContainerNavigation {

    private val binding: FragmentCategoryNewsContainerBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (childFragmentManager.findFragmentByTag(FRAGMENT_TAG) == null) {
            navToCategoryNewsFrag()
        }
    }

    override fun navToCategoryNewsFrag() {
        navToFrag(CategoryNewsFragment.newInstance(), false)
    }

    override fun navToNewsDetailsFrag(news: News) {
        navToFrag(NewsDetailsFragment.newInstance(news), true)
    }

    override fun navToFiltersFrag() {
        navToFrag(FiltersFragment.newInstance(), true)
    }

    override fun onBackPressed(): Boolean {
        if (childFragmentManager.backStackEntryCount == 0) return false
        childFragmentManager.popBackStack()
        return true
    }

    override fun popBackStackToFirst() {
        with(childFragmentManager) {
            repeat(backStackEntryCount) { popBackStack() }
        }
    }

    private fun navToFrag(fragment: Fragment, addToBackStack: Boolean) {
        childFragmentManager.commit {
            replace(
                binding.fragCategoryNewsContainerFragmentView.id,
                fragment,
                FRAGMENT_TAG,
            )
            if (addToBackStack) addToBackStack(fragment.toString())
        }
    }

    companion object {
        private const val FRAGMENT_TAG = "CategoryNewsContainer"

        fun newInstance(): CategoryNewsContainerFragment {
            return CategoryNewsContainerFragment()
        }
    }
}