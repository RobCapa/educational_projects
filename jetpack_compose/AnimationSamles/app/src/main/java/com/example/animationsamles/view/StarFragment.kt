package com.example.animationsamles.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.animationsamles.databinding.FragmentStarBinding

class StarFragment : Fragment() {
    private lateinit var binding: FragmentStarBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStarBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.counter.counter = 2
        binding.counter.addListener(binding.starView::setNumberOfPoint)
    }
}