package com.example.animationsamles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieDrawable
import com.example.animationsamles.databinding.FragmentLottieBinding

class LottieFragment:Fragment() {
    private lateinit var binding:FragmentLottieBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLottieBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lottieView.setAnimation("logo.json")
        binding.lottieView.playAnimation()
        binding.lottieView.repeatCount = LottieDrawable.INFINITE
    }
}