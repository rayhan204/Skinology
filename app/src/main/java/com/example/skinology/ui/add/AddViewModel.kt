package com.example.skinology.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skinology.data.SkinologyRepository
import com.example.skinology.data.local.entity.ArticleEntity
import kotlinx.coroutines.launch

class AddViewModel(private val repository: SkinologyRepository): ViewModel() {

    fun addArticles(articles: List<ArticleEntity>) = viewModelScope.launch {
        repository.addArticles(articles)
    }

}