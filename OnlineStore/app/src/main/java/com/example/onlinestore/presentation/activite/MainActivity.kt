package com.example.onlinestore.presentation.activite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.onlinestore.R
import com.example.onlinestore.databinding.ActivityMainBinding
import com.example.onlinestore.presentation.interfaces.BackAction
import com.example.onlinestore.presentation.interfaces.NavigateToStartScreen


class MainActivity : AppCompatActivity(), NavigateToStartScreen {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun navToStartDestination() {
        with(binding.activityMainFragmentContainerView.findNavController()) {
            repeat((0 until currentBackStack.value.size).count()) {
                popBackStack()
            }
            navigate(R.id.startFragment)
        }
    }

    override fun onBackPressed() {
        val currentFragmentId = binding.activityMainFragmentContainerView
            .findNavController()
            .currentDestination
            ?.id

        if (currentFragmentId != null) {
            val currentFragment = supportFragmentManager.findFragmentById(currentFragmentId)

            if (currentFragment != null
                && currentFragment is BackAction
                && currentFragment.onBackPressed()
            ) return
        }

        super.onBackPressed()
    }
}