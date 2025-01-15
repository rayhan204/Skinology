package com.example.skinology.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skinology.data.local.entity.ArticleEntity
import com.example.skinology.databinding.ItemArticleBinding
import java.util.regex.Pattern

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
            loadImage(itemView.context, binding.imageView, article.photo)


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
fun loadImage(context: Context, imageView: ImageView, imageSource: String) {
    if (isUrl(imageSource)) {

        Glide.with(context)
            .load(imageSource)
            .into(imageView)
    } else{
        val bitmap = base64ToBitmap(imageSource)
        imageView.setImageBitmap(bitmap)
    }
}

fun isUrl(string: String): Boolean {

    val urlPattern = "^(http|https)://.*$"
    return Pattern.matches(urlPattern, string.trim())
}

fun base64ToBitmap(base64String: String): Bitmap? {
    return try {
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
        null
    }
}
