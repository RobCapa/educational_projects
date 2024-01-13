package ru.aston.fragments.containers

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.aston.R
import ru.aston.data.News
import ru.aston.databinding.FragmentSavedNewsContainerBinding
import ru.aston.fragments.filters.FiltersFragment
import ru.aston.fragments.news_details.NewsDetailsFragment
import ru.aston.fragments.saved_news.SavedNews
import ru.aston.interfaces.actions.BackAction
import ru.aston.interfaces.navigation.SavedNewsContainerNavigation

class SavedNewsContainerFragment :
    Fragment(R.layout.fragment_saved_news_container),
    BackAction,
    SavedNewsContainerNavigation {

    private val binding: FragmentSavedNewsContainerBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (childFragmentManager.findFragmentByTag(FRAGMENT_TAG) == null) {
            navToSavedFrag()
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

    override fun navToSavedFrag() {
        navToFrag(SavedNews.newInstance(), false)
    }

    override fun navToFiltersFrag() {
        navToFrag(FiltersFragment.newInstance(), true)
    }

    override fun navToNewsDetailsFrag(news: News) {
        navToFrag(NewsDetailsFragment.newInstance(news), true)
    }

    private fun navToFrag(fragment: Fragment, addToBackStack: Boolean) {
        childFragmentManager.commit {
            replace(
                binding.fragSavedNewsContainerFragmentView.id,
                fragment,
                FRAGMENT_TAG
            )
            if (addToBackStack) addToBackStack(fragment.toString())
        }
    }

    companion object {
        private const val FRAGMENT_TAG = "SavedNewsFragmentContainer"

        fun newInstance(): SavedNewsContainerFragment {
            return SavedNewsContainerFragment()
        }
    }
}