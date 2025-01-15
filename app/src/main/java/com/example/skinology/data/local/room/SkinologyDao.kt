package com.example.skinology.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.skinology.data.local.entity.ArticleEntity
import com.example.skinology.data.local.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SkinologyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(historyEntity: HistoryEntity)

    @Query("SELECT * FROM history_table")
    fun getAllHistory(): Flow<List<HistoryEntity>>

    @Query("DELETE FROM history_table WHERE id = :id")
    suspend fun deleteHistory(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<ArticleEntity>)

    @Query("SELECT * FROM article")
    suspend fun getAllArticles(): List<ArticleEntity>

    @Query("SELECT * FROM article WHERE id = :articleId LIMIT 1")
    suspend fun getArticleId(articleId: String): ArticleEntity

    @Query("SELECT * FROM article WHERE category = :category")
    suspend fun getArticlesByCategoryDirect(category: String): List<ArticleEntity>

    @Query("UPDATE article SET name = :newName, photo = :newPhoto, description = :newDescription WHERE id = :id")
    suspend fun updateArticleDetails(id: String, newName: String, newPhoto: String, newDescription: String)

}