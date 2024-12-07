package com.example.skinology.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.skinology.data.local.entity.ArticleEntity
import com.example.skinology.data.local.entity.HistoryEntity

@Dao
interface SkinologyDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertHistory(historyEntity: HistoryEntity)
//
//    @Query("SELECT * from history_table")
//    fun getAllHistory(): LiveData<List<HistoryEntity>>
//
//    @Query("DELETE FROM history_table")
//    suspend fun deleteHistory()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<ArticleEntity>)

    @Query("SELECT * FROM article")
    fun getAllArticles(): LiveData<List<ArticleEntity>>

    @Query("SELECT * FROM article WHERE id = :articleId LIMIT 1")
    suspend fun getArticleId(articleId: String): ArticleEntity

    @Query("SELECT * FROM article WHERE category = :category")
    suspend fun getArticlesByCategoryDirect(category: String): List<ArticleEntity>
}