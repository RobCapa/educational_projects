package ru.aston.fragments.containers

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.aston.R
import ru.aston.data.News
import ru.aston.data.NewsSource
import ru.aston.databinding.FragmentSourcesContainerBinding
import ru.aston.fragments.filters.FiltersFragment
import ru.aston.fragments.news_details.NewsDetailsFragment
import ru.aston.fragments.source_news.SourceNewsFragment
import ru.aston.fragments.sources.SourcesFragment
import ru.aston.interfaces.actions.BackAction
import ru.aston.interfaces.navigation.SourcesContainerNavigation

class SourcesContainerFragment : Fragment(R.layout.fragment_sources_container),
    BackAction,
    SourcesContainerNavigation {

    private val binding: FragmentSourcesContainerBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (childFragmentManager.findFragmentByTag(FRAGMENT_TAG) == null) {
            navToSourcesFrag()
        }
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

    override fun navToSourceNewsFrag(source: NewsSource) {
        navToFrag(SourceNewsFragment.newInstance(source), true)
    }

    override fun navToNewsDetailsFrag(news: News) {
        navToFrag(NewsDetailsFragment.newInstance(news), true)
    }

    override fun navToSourcesFrag() {
        navToFrag(SourcesFragment.newInstance(), false)
    }

    override fun navToFiltersFrag() {
        navToFrag(FiltersFragment.newInstance(), true)
    }

    private fun navToFrag(fragment: Fragment, addToBackStack: Boolean) {
        childFragmentManager.commit {
            replace(
                binding.fragSourcesContainerFragmentView.id,
                fragment,
                FRAGMENT_TAG
            )
            if (addToBackStack) addToBackStack(fragment.toString())
        }
    }

    companion object {
        private const val FRAGMENT_TAG = "SourcesFragmentContainer"

        fun newInstance(): SourcesContainerFragment {
            return SourcesContainerFragment()
        }
    }
}

