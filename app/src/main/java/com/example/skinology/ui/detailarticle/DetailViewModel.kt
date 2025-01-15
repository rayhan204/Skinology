package com.example.skinology.ui.detailarticle

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skinology.data.SkinologyRepository
import com.example.skinology.data.local.entity.ArticleEntity
import com.example.skinology.data.Result
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: SkinologyRepository): ViewModel() {
    fun getArticleId(articleId: String): LiveData<Result<ArticleEntity>> {
        Log.d("DetailViewModel", "Fetching article with ID: $articleId")
        return repository.getArticleId(articleId)
    }

    fun updateArticleDetails(articleId: String, newName: String, newPhoto: String, newDescription: String) {
        viewModelScope.launch {
            try {
                repository.updateArticleDetails(articleId, newName, newPhoto, newDescription)
                Log.d("DetailViewModel", "Updated article details for ID: $articleId")
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Error updating article details: ${e.message}")
            }
        }
    }
    fun deleteArticlesById(id: String) {
        viewModelScope.launch {
            repository.deleteArticlesById(id)
        }
    }

}