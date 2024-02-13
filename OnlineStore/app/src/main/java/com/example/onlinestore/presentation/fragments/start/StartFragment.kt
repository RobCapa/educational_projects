package com.example.onlinestore.presentation.fragments.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.onlinestore.R
import com.example.onlinestore.app.OnlineStoreApp
import com.example.onlinestore.presentation.utils.ViewModelFactory
import javax.inject.Inject

class StartFragment : Fragment(R.layout.frag_start) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<StartViewModel>

    private val viewModel: StartViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as OnlineStoreApp).appComponent.inject(this)

        observeToViewModelResults()

        viewModel.isSavedUserExists()
    }

    private fun observeToViewModelResults() {
        viewModel.isSavedUserExistsLive.observe(this) { isSavedUserExists ->
            if (isSavedUserExists) {
                StartFragmentDirections.actionStartFragmentToMainFragment()
            } else {
                StartFragmentDirections.actionStartFragmentToRegistrationFragment()
            }.let(findNavController()::navigate)
        }
    }
}