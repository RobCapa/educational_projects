package com.example.recyclerview.concat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview.R
import com.example.recyclerview.api.Character
import com.example.recyclerview.databinding.FragmentConcatBinding
import com.example.recyclerview.diffutil.Worker
import com.example.recyclerview.diffutil.ElementAdapter
import com.example.recyclerview.diffutil.ElementDiffUtilCallback

class ConcatFragment : Fragment() {
    private lateinit var binding: FragmentConcatBinding
    private var counter = 0L
    private var elementsList = emptyList<Worker>()
    private var elements: ElementAdapter? = null

    private var characters = CharacterAdapter(
        listOf(
            Character(id = 1, name = "Peter", species = "Human"),
            Character(id = 2, name = "Liza", species = "Human")
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConcatBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        elements = ElementAdapter(emptyList())
        binding.recycler.adapter = ConcatAdapter(elements, characters)

        binding.addButton.setOnClickListener {
            counter++
            val addedElements = listOf(
                Worker(name = "Peter$counter", position = "Programmer"),
                Worker(name = "Helga$counter", position = "Manager"),
                Worker(name = "Tom$counter", position = "CEO"),
                Worker(name = "Jerry$counter", position = "CTO")
            )
            updateList(elementsList.plus(addedElements))

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
                oldElements = elementsList,
                newElements = newElements
            )
        )
        elements?.let { adapter ->
            adapter.elements = newElements
            result.dispatchUpdatesTo(adapter)
        }
        elementsList = newElements
    }
}

class CharacterAdapter(
    private val items: List<Character>
) : RecyclerView.Adapter<CharacterAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.view_character, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(character: Character) {
            (itemView as? ViewGroup)?.findViewById<TextView>(R.id.name)?.text =
                "${character.name}  ${character.species}"
        }
    }
}