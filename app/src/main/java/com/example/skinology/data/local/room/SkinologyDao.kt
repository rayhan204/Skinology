package com.example.skinology.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.skinology.data.local.entity.HistoryEntity

@Dao
interface SkinologyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(historyEntity: HistoryEntity)
    
    @Query("SELECT * from history_table")
    fun getAllHistory(): LiveData<List<HistoryEntity>>
}