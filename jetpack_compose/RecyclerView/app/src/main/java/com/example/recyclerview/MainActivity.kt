package com.example.recyclerview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.recyclerview.concat.ConcatFragment
import com.example.recyclerview.databinding.MainActivityBinding
import com.example.recyclerview.diffutil.DiffUtilFragment
import com.example.recyclerview.paging.PaginationFragment
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    private lateinit var binding:MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
               val fragment = when(tab?.text){
                   "DiffUtil" -> DiffUtilFragment()
                   "Pagination" -> PaginationFragment()
                   "Concat" -> ConcatFragment()
                   else -> return
               }

                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commitNow()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
            override fun onTabReselected(tab: TabLayout.Tab?) = Unit
        })

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("DiffUtil"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Pagination"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Concat"))
    }
}