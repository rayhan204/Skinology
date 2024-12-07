package com.example.skinology.ui.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.skinology.data.SkinologyRepository

class ArticleViewModel(private val repository: SkinologyRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Article Fragment"
    }
    val text: LiveData<String> = _text
}