package com.example.skinology.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skinology.data.Result
import com.example.skinology.data.SkinologyRepository
import com.example.skinology.data.local.entity.HistoryEntity
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: SkinologyRepository): ViewModel() {

    fun getAllHistory(): LiveData<Result<List<HistoryEntity>>> {
        return repository.getAllHistory()
    }

    fun deleteHistoryById(id: String) {
        viewModelScope.launch {
            repository.deleteHistoryById(id)
        }
    }
}