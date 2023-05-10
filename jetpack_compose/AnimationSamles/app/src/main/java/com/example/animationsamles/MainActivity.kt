package com.example.animationsamles

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.animationsamles.databinding.ActivityMainBinding
import com.example.animationsamles.view.StarFragment
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val fragment = when (tab?.text) {
                    "MotionLayout" -> MotionLayoutFragment()
                    "Анимация" -> AnimationFragment()
                    "Lottie" -> LottieFragment()
                    "Custom View" -> StarFragment()
                    else -> return
                }

                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.mainContainer, fragment)
                    .commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
            override fun onTabReselected(tab: TabLayout.Tab?) = Unit
        })

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Анимация"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("MotionLayout"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Lottie"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Custom View"))
    }
}