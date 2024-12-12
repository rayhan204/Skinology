package com.example.skinology.ui.article2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.skinology.data.Result
import com.example.skinology.data.SkinologyRepository
import com.example.skinology.data.local.entity.ArticleEntity

class ArticleViewModel(private val repository: SkinologyRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Article Fragment"
    }
    val text: LiveData<String> = _text

    fun getArticlesByCategory(category: String): LiveData<Result<List<ArticleEntity>>> {
        return repository.getSkinTypeByCategory(category)
    }

}