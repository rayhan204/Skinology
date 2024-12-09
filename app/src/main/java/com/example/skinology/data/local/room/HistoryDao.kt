package com.example.skinology.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.skinology.data.local.entity.HistoryEntity

@Dao
interface HistoryDao {
    @Insert
    suspend fun InsertHistory(history: HistoryEntity)

    @Query("SELECT * FROM history_table ORDER BY date DESC")
    suspend fun getAllHistory(): List<HistoryEntity>

    @Query("DELETE FROM history_table WHERE id = :id")
    suspend fun deleteHistory(id: String)
}