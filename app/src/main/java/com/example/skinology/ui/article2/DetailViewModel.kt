package com.example.skinology.ui.article2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.skinology.data.SkinologyRepository
import com.example.skinology.data.local.entity.ArticleEntity
import com.example.skinology.data.Result

class DetailViewModel(private val repository: SkinologyRepository): ViewModel() {

    private val _event = MutableLiveData<Result<ArticleEntity>>()
    val event: LiveData<Result<ArticleEntity>> = _event

    fun getArticleId(articleId: Int): LiveData<Result<ArticleEntity>> {
        return repository.getArticleId(articleId)
    }
}