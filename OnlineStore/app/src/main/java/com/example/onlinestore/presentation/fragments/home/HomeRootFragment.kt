package com.example.onlinestore.presentation.fragments.home

import androidx.fragment.app.Fragment
import com.example.onlinestore.R
import com.example.onlinestore.presentation.interfaces.BackAction

class HomeRootFragment : Fragment(R.layout.frag_root_home), BackAction {

    override fun onBackPressed(): Boolean {
        if (childFragmentManager.backStackEntryCount == 0) return false
        childFragmentManager.popBackStack()
        return true
    }
}