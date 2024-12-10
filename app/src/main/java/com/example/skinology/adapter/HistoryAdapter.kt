package com.example.skinology.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skinology.data.local.entity.HistoryEntity
import com.example.skinology.databinding.ItemHistoryBinding

class HistoryAdapter(
    private val onItemClick: (HistoryEntity) -> Unit,
    private val onDeleteClick: (HistoryEntity) -> Unit
) : ListAdapter<HistoryEntity, HistoryAdapter.HistoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding, onItemClick, onDeleteClick)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)
    }

    class HistoryViewHolder(
        private val binding: ItemHistoryBinding,
        private val onItemClick: (HistoryEntity) -> Unit,
        private val onDeleteClick: (HistoryEntity) -> Unit // Callback untuk delete
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(history: HistoryEntity) {
            binding.textResult.text = history.prediction
            binding.textDate.text = history.date
            Glide.with(binding.imageView.context)
                .load(history.image)
                .into(binding.imageView)

            binding.root.setOnClickListener {
                onItemClick(history)
            }

            binding.tvDelete.setOnClickListener {
                onDeleteClick(history)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryEntity>() {
            override fun areItemsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}
