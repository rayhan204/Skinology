package com.example.skinology.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.skinology.data.SkinologyRepository

class HistoryViewModel(private val repository: SkinologyRepository): ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is History Fragment"
    }
    val text: LiveData<String> = _text
}