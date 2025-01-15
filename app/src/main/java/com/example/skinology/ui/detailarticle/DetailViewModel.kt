package com.example.skinology.ui.detailarticle

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.skinology.data.SkinologyRepository
import com.example.skinology.data.local.entity.ArticleEntity
import com.example.skinology.data.Result

class DetailViewModel(private val repository: SkinologyRepository): ViewModel() {

    fun getArticleId(articleId: String): LiveData<Result<ArticleEntity>> {
        Log.d("DetailViewModel", "Fetching article with ID: $articleId")
        return repository.getArticleId(articleId)
    }
}