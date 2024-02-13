package com.example.onlinestore.presentation.fragments.profile

import androidx.fragment.app.Fragment
import com.example.onlinestore.R
import com.example.onlinestore.presentation.interfaces.BackAction

class ProfileRootFragment : Fragment(R.layout.frag_root_profile), BackAction {

    override fun onBackPressed(): Boolean {
        if (childFragmentManager.backStackEntryCount == 0) return false
        childFragmentManager.popBackStack()
        return true
    }
}