package com.example.recyclerview.paging

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.example.recyclerview.databinding.FragmentPaginationBinding

class PaginationFragment : Fragment() {
    private lateinit var binding: FragmentPaginationBinding
    private val throwable = MutableLiveData<Throwable?>(null)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaginationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        throwable.observe(viewLifecycleOwner) {
            val showError = it != null
            binding.recycler.isVisible = !showError
            binding.reloadButton.isVisible = showError
            if (it != null)
                Toast
                    .makeText(
                        requireContext(),
                        it.localizedMessage,
                        Toast.LENGTH_LONG
                    ).show()
        }

        binding.reloadButton.setOnClickListener { reloadList() }

        reloadList()
    }

    private fun reloadList() {
        throwable.postValue(null)
        val adapter = CharacterAdapter()
        adapter.submitList(PagedList.createCharactersList(throwable))
        binding.recycler.adapter = adapter
    }
}