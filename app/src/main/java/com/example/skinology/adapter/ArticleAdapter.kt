package com.example.skinology.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skinology.data.local.entity.ArticleEntity
import com.example.skinology.databinding.ItemArticleBinding

class ArticleAdapter(private val onItemClick: (ArticleEntity) -> Unit) :
    ListAdapter<ArticleEntity, ArticleAdapter.ArticleViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article)
    }

    override fun submitList(list: MutableList<ArticleEntity>?) {
        super.submitList(list)
    }

    class ArticleViewHolder(
        private val binding: ItemArticleBinding,
        private val onItemClick: (ArticleEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(article: ArticleEntity) {
            binding.nameArticle.text = article.name
            binding.articleDesc.text = article.description

            Glide.with(binding.imageView.context)
                .load(article.photo)
                .into(binding.imageView)

            binding.root.setOnClickListener {
                onItemClick(article)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticleEntity>() {
            override fun areItemsTheSame(oldItem: ArticleEntity, newItem: ArticleEntity): Boolean {
                Log.d("DIFF_CALLBACK", "Comparing IDs: ${oldItem.id} vs ${newItem.id}")
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ArticleEntity, newItem: ArticleEntity): Boolean {
                Log.d("DIFF_CALLBACK", "Comparing Content: $oldItem vs $newItem")
                return oldItem == newItem
            }
        }
    }
}
