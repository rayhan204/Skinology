package com.example.skinology.ui.article2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skinology.R
import com.example.skinology.ViewModelFactory
import com.example.skinology.adapter.ArticleAdapter
import com.example.skinology.data.Result
import com.example.skinology.databinding.ActivityArticel2Binding
import com.example.skinology.ui.detailarticle.DetailArticleActivity


class Articel2Activity : AppCompatActivity(), ButtonsFragment.ButtonSelectionListener {
    private lateinit var binding: ActivityArticel2Binding
    private lateinit var articleAdapter: ArticleAdapter

    private lateinit var articleViewModel: ArticleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticel2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val selectedButton = intent.getStringExtra("SELECTED_BUTTON")
        updateTitle(selectedButton)
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }
        if (savedInstanceState == null) {
            val fragment = ButtonsFragment.newInstance(selectedButton)
            supportFragmentManager.beginTransaction()
                .replace(R.id.buttonContainerFragment, fragment)
                .commit()
        }

        setupViewModel()
        setupRecyclerView()
    }

    private fun setupViewModel() {
        val factory = ViewModelFactory.getInstance(this)
        articleViewModel = ViewModelProvider(this, factory)[ArticleViewModel::class.java]
    }

    private fun setupRecyclerView() {
        articleAdapter = ArticleAdapter { article ->
            val intent = Intent(this, DetailArticleActivity::class.java)
            intent.putExtra("ARTICLE_ID", article.id)
            startActivity(intent)
        }

        binding.rvArticle.apply {
            layoutManager = LinearLayoutManager(this@Articel2Activity)
            adapter = articleAdapter
        }
    }

    private fun observeViewModel(category: String) {
        articleViewModel.getArticlesByCategory(category).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val articles = result.data
                    if (articles.isNullOrEmpty()) {
                        Toast.makeText(this, "No articles found for $category", Toast.LENGTH_SHORT).show()
                    } else {
                        articleAdapter.submitList(articles.toMutableList())
                    }
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



    override fun onButtonSelected(buttonType: String) {
        // Tangani tombol yang dipilih
        updateTitle(buttonType)
        Toast.makeText(this, "Selected: $buttonType", Toast.LENGTH_SHORT).show()
        observeViewModel(buttonType) // Contoh fungsi untuk memperbarui data
    }



    private fun updateTitle(selectedButton: String?) {
        val title = when {
            selectedButton.equals("DRY", ignoreCase = true) -> "Know Dry Skin"
            selectedButton.equals("OILY", ignoreCase = true) -> "Know Oily Skin"
            selectedButton.equals("NORMAL", ignoreCase = true) -> "Know Normal Skin"
            selectedButton.equals("ACNE", ignoreCase = true) -> "Know Acne Skin"
            else -> "Know Your Skin"
        }
        binding.text2.text = title
        Log.d("Articel2Activity", "Title updated to: $title for selectedButton: $selectedButton")
    }


}

