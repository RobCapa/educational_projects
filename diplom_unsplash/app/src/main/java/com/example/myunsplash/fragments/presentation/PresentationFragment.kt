package com.example.myunsplash.fragments.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.myunsplash.R
import com.example.myunsplash.data.IntroCard
import com.example.myunsplash.databinding.FragPresentationBinding
import com.example.myunsplash.recycler.adapters.IntroductoryCardRecyclerAdapter
import com.example.myunsplash.repositories.SharedPreferencesRepository
import com.littlemango.stacklayoutmanager.StackLayoutManager
import kotlinx.coroutines.runBlocking

class PresentationFragment : Fragment(R.layout.frag_presentation) {
    private val binding: FragPresentationBinding by viewBinding()
    private val recyclerViewAdapter = IntroductoryCardRecyclerAdapter(
        listOf(
            IntroCard(R.drawable.camera, R.string.fragPresentation_text1),
            IntroCard(R.drawable.friends, R.string.fragPresentation_text2),
            IntroCard(R.drawable.statistics, R.string.fragPresentation_text3),
        )
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener {
            setIsFirstRunAppFalse()
            findNavController().navigate(
                PresentationFragmentDirections.actionPresentationFragmentToLoginFragment()
            )
        }
        configureRecyclerView()
    }

    private fun setIsFirstRunAppFalse() {
        runBlocking {
            SharedPreferencesRepository.sharedPreferences
                .edit()
                .putBoolean(SharedPreferencesRepository.Keys.IS_FIRST_RUN_KEY, false)
                .apply()
        }
    }

    private fun configureRecyclerView() {
        with(binding.fragPresentetionRecyclerView) {
            adapter = recyclerViewAdapter
            layoutManager = StackLayoutManager()
            setHasFixedSize(true)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val currentPage =
                        (recyclerView.layoutManager as StackLayoutManager).getFirstVisibleItemPosition()
                    if (currentPage == recyclerViewAdapter.itemCount - 1) {
                        binding.floatingActionButton.visibility = View.VISIBLE
                        binding.fragPresentetionRecyclerView.removeOnScrollListener(this)
                    }
                }
            })
        }
    }
}