package com.example.skinology.ui.article2

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.skinology.ViewModelFactory
import com.example.skinology.data.Result
import com.example.skinology.data.local.entity.ArticleEntity
import com.example.skinology.databinding.ActivityDetailArticelBinding

class DetailArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailArticelBinding

    private val viewModel: DetailViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailArticelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val articleId = intent.getStringExtra("ARTICLE_ID")
        Log.d("DetailArticleActivity", "Received article ID: $articleId")
        if (articleId != null) {
            observeViewModel(articleId.toInt())
        } else {
            Toast.makeText(this, "Article id tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
        }

        setupToolbar()
    }

    private fun setupToolbar() {
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun observeViewModel(articleId: Int) {
        viewModel.getArticleId(articleId)

        viewModel.event.observe(this) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    Log.d("DetailArticleActivity", "Article data  received: ${result.data}")
                    updateUI(result.data)
                }
                is Result.Error -> {
                    showLoading(false)
                    Log.e("DetailArticleActivity", "Error: ${result.error}")
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun updateUI(article: ArticleEntity) {
        binding.apply {
            textArticleTitle.text = article.name
            textArticleDetail.text = article.description

            Glide.with(this@DetailArticleActivity)
                .load(article.photo)
                .centerCrop()
                .into(imageArticle)
        }
    }
}
