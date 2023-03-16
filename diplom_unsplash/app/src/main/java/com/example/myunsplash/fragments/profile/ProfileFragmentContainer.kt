package com.example.myunsplash.fragments.profile

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.myunsplash.R
import com.example.myunsplash.databinding.ProfileFragmentContainerBinding
import com.example.myunsplash.interfaces.PressBackButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragmentContainer : Fragment(R.layout.profile_fragment_container), PressBackButton {
    private val binding: ProfileFragmentContainerBinding by viewBinding()
    override fun pressBackButton(): Boolean {
        return childFragmentManager
            .findFragmentById(binding.fragmentContainerView.id)
            ?.findNavController()
            ?.popBackStack() ?: false
    }
}