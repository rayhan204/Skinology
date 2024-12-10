package com.example.skinology.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.skinology.data.Result
import com.example.skinology.data.SkinologyRepository
import com.example.skinology.data.local.entity.ArticleEntity

class HomeViewModel(private val repository: SkinologyRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    val getAllSkinTypes : LiveData<Result<List<ArticleEntity>>> = repository.getAllSkinTypes()
}