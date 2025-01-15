package com.example.skinology.ui.detailarticle

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.skinology.ViewModelFactory
import com.example.skinology.data.Result
import com.example.skinology.data.local.entity.ArticleEntity
import com.example.skinology.databinding.ActivityDetailArticelBinding
import java.util.regex.Pattern

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
            observeViewModel(articleId)
        } else {
            Toast.makeText(this, "Article id tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
        }

        if (articleId != null) {
            viewModel.getArticleId(articleId)
        }

        setupToolbar()
        setButton()
    }

    private fun setButton() {
        binding.fabEdit.setOnClickListener {
            val articleId = intent.getStringExtra("ARTICLE_ID")
            val intent = Intent(this, EditArticleActivity::class.java)
            intent.putExtra("ARTICLE_ID", articleId)
            startActivity(intent)
            finish()
        }
    }

    private fun setupToolbar() {
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun observeViewModel(articleId: String) {
        viewModel.getArticleId(articleId).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    showLoading(false)
                    updateUI(result.data)
                }
                is Result.Error -> {
                    showLoading(false)
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
            loadImage(this@DetailArticleActivity,binding.imageArticle,article.photo)
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
}

