package com.example.skinology.ui.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skinology.data.SkinologyRepository
import com.example.skinology.data.local.entity.HistoryEntity
import kotlinx.coroutines.launch

class ResultViewModel(private val repository: SkinologyRepository) : ViewModel()  {
    fun insertHistory(historyEntity: HistoryEntity) {
        viewModelScope.launch {
            repository.insertHistory(historyEntity)
        }
    }
}