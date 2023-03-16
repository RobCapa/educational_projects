package com.example.myunsplash.fragments.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.myunsplash.R
import com.example.myunsplash.databinding.FragMainBinding
import com.example.myunsplash.fragments.collections.CollectionsFragmentContainer
import com.example.myunsplash.fragments.photos.PhotosFragmentContainer
import com.example.myunsplash.fragments.profile.ProfileFragmentContainer
import com.example.myunsplash.interfaces.PressBackButton

class MainFragment : Fragment(R.layout.frag_main), PressBackButton {
    private val binding: FragMainBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragMainBottomNav.setOnItemSelectedListener {
            openFragment(it.itemId)
            true
        }
        openFragment(R.id.nav_graph_photos)
    }

    private fun openFragment(itemId: Int) {
        val newFragmentTag = itemId.toString()
        val transaction = childFragmentManager.beginTransaction()
        val newFragment = childFragmentManager.findFragmentByTag(newFragmentTag)
        var currentFragment: Fragment? = null

        for (fragment in childFragmentManager.fragments) {
            if (fragment.isVisible) currentFragment = fragment
        }

        if (currentFragment != null && currentFragment === newFragment) return

        if (newFragment == null) {
            transaction.add(
                binding.fragMainFragmentContainerView.id,
                getFragment(itemId),
                newFragmentTag,
            )
        }

        currentFragment?.let { transaction.hide(it) }
        newFragment?.let { transaction.show(it) }
        transaction.commit()
    }

    override fun pressBackButton(): Boolean {
        val frag = childFragmentManager
            .fragments
            .first { it.isVisible }
        return (frag != null && frag is PressBackButton && frag.pressBackButton())
    }

    private fun getFragment(itemId: Int): Fragment {
        return when (itemId) {
            R.id.nav_graph_photos -> PhotosFragmentContainer()
            R.id.nav_graph_collections -> CollectionsFragmentContainer()
            R.id.nav_graph_profile -> ProfileFragmentContainer()
            else -> error("unknown itemId")
        }
    }
}
