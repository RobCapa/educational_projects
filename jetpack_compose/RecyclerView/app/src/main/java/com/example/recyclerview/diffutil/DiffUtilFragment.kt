package com.example.recyclerview.diffutil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import com.example.recyclerview.databinding.FragmentDiffUtilBinding

class DiffUtilFragment : Fragment() {
    private lateinit var binding: FragmentDiffUtilBinding

    private var counter = 0L
    private var elements = emptyList<Worker>()
    private var adapter: ElementAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDiffUtilBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ElementAdapter(emptyList())
        binding.recycler.adapter = adapter

        binding.addButton.setOnClickListener {
            counter++
            val addedElements = listOf(
                Worker(name = "Peter$counter", position = "Programmer"),
                Worker(name = "Helga$counter", position = "Manager"),
                Worker(name = "Tom$counter", position = "CEO"),
                Worker(name = "Jerry$counter", position = "CTO")
            )
            updateList(elements.plus(addedElements))

        }

        binding.changeButton.setOnClickListener {
            val newElements = listOf(
                Worker(name = "Peter$counter", position = "Programmer"),
                Worker(name = "Arnold$counter", position = "Manager"),
                Worker(name = "Helga$counter", position = "Sales"),
            )
            updateList(newElements)
        }
    }

    private fun updateList(newElements: List<Worker>) {
        val result = DiffUtil.calculateDiff(
            ElementDiffUtilCallback(
                oldElements = elements,
                newElements = newElements
            )
        )
        adapter?.let { adapter ->
            adapter.elements = newElements
            result.dispatchUpdatesTo(adapter)
        }
        elements = newElements
    }
}

