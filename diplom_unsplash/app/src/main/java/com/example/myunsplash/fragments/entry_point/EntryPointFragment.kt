package com.example.myunsplash.fragments.entry_point

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.myunsplash.R
import com.example.myunsplash.databinding.FragEntryPointBinding
import com.example.myunsplash.utils.showSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EntryPointFragment : Fragment(R.layout.frag_entry_point) {
    private val binding: FragEntryPointBinding by viewBinding()
    private val viewModel: EntryPointViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeToLiveData()
        viewModel.defineFragmentForNavigate()
    }

    private fun observeToLiveData() {
        viewModel.directionLiveData.observe(viewLifecycleOwner, findNavController()::navigate)
        viewModel.notificationNoConnection.observe(viewLifecycleOwner) {
            binding.root.showSnackbar(R.string.notification_noConnection)
        }
    }
}