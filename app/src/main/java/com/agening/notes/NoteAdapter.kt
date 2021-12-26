package com.agening.notes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agening.notes.databinding.NoteItemBinding

class NoteAdapter(private val notes: List<Note>) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private lateinit var binding: NoteItemBinding

    interface OnNoteClickListener {
        fun onNoteClick(position: Int)
        fun onLongClick(position: Int)
    }

    var setOnNoteClickListener: OnNoteClickListener? = null

    class ViewHolder(val binding: NoteItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (id, title, description, dayOfWeek, priority) = notes[position]
        binding.textViewTitle.text = title
        binding.textViewDescription.text = description
        binding.textViewDayOfWeek.text = dayOfWeek
        val colorId = when (priority) {
            1 -> {
                binding.root.resources.getColor(android.R.color.holo_red_light)
            }
            2 -> {
                binding.root.resources.getColor(android.R.color.holo_orange_light)
            }
            else -> {
                binding.root.resources.getColor(android.R.color.holo_green_light)
            }
        }
        binding.textViewTitle.setBackgroundColor(colorId)
        binding.root.setOnClickListener {
            setOnNoteClickListener?.onNoteClick(position)
        }
        binding.root.setOnLongClickListener {
            setOnNoteClickListener?.onLongClick(position)
            true
        }
    }

    override fun getItemCount(): Int = notes.size
}