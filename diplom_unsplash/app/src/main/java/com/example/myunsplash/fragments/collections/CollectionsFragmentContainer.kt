package com.example.myunsplash.fragments.collections

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.myunsplash.R
import com.example.myunsplash.databinding.CollectionsFragmentContainerBinding
import com.example.myunsplash.interfaces.PressBackButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectionsFragmentContainer : Fragment(R.layout.collections_fragment_container), PressBackButton {
    private val binding: CollectionsFragmentContainerBinding by viewBinding()
    override fun pressBackButton(): Boolean {
        return childFragmentManager
            .findFragmentById(binding.fragmentContainerView.id)
            ?.findNavController()
            ?.popBackStack() ?: false
    }
}