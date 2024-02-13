package com.example.onlinestore.presentation.fragments.main

import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.onlinestore.R
import com.example.onlinestore.databinding.FragMainBinding

class MainFragment : Fragment(R.layout.frag_main) {

    private val binding: FragMainBinding by viewBinding()

    override fun onStart() {
        super.onStart()

        val navController = binding.fragMainContainerView.findNavController()
        binding.fragMainBottomNav.setupWithNavController(navController)
    }
}