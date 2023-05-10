package com.example.animationsamles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.animationsamles.databinding.FragmentTextSampleBinding

class MotionLayoutFragment : Fragment(R.layout.fragment_text_sample) {
    private lateinit var binding: FragmentTextSampleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTextSampleBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.animateButton.setOnClickListener { binding.motionLayout.transitionToEnd() }
    }
}