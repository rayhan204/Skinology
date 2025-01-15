package com.example.skinology.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.map
import androidx.lifecycle.liveData
import com.example.skinology.data.local.room.SkinologyDao
import com.example.skinology.data.remote.retrofit.ApiService
import com.example.skinology.data.local.entity.ArticleEntity
import com.example.skinology.data.local.entity.HistoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SkinologyRepository private constructor(
    private val skinologyDao: SkinologyDao,
    private val apiService: ApiService
) {

    suspend fun insertHistory(historyEntity: HistoryEntity) {
        val history = HistoryEntity(
            id = historyEntity.id,
            prediction = historyEntity.prediction,
            image = historyEntity.image,
            date = historyEntity.date
        )
        skinologyDao.insertHistory(history)
    }

    fun getAllHistory(): LiveData<Result<List<HistoryEntity>>> {
        return skinologyDao.getAllHistory()
            .map { history ->
                Result.Success(history)
            }
            .asLiveData()
    }

    suspend fun deleteHistoryById(id: String) {
        withContext(Dispatchers.IO) {
            skinologyDao.deleteHistory(id)
        }
    }

    fun getAllSkinTypes(): LiveData<Result<List<ArticleEntity>>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val localArticles = skinologyDao.getAllArticles()

            if (localArticles.isNotEmpty()) {
                emit(Result.Success(localArticles))
            } else {
                val response = apiService.getAllSkinTypes()
                Log.d("repository", "Api response: $response")

                val articles = mutableListOf<ArticleEntity>()

                response.normal.let { normalItems ->
                    Log.d("repository", "Normal skin data: $normalItems")
                    articles.addAll(normalItems.map { normalItem ->
                        ArticleEntity(
                            id = normalItem.id.toString(),
                            name = normalItem.name,
                            photo = normalItem.photo,
                            description = normalItem.description,
                            category = "Normal"
                        )
                    })
                }

                response.oily.let { oilyItems ->
                    Log.d("repository", "Oily skin data: $oilyItems")
                    articles.addAll(oilyItems.map { oilyItem ->
                        ArticleEntity(
                            id = oilyItem.id.toString(),
                            name = oilyItem.name,
                            photo = oilyItem.photo,
                            description = oilyItem.description,
                            category = "Oily"
                        )
                    })
                }

                response.acne.let { acneItems ->
                    Log.d("repository", "acne skin data: $acneItems")
                    articles.addAll(acneItems.map { acneItem ->
                        ArticleEntity(
                            id = acneItem.id.toString(),
                            name = acneItem.name,
                            photo = acneItem.photo,
                            description = acneItem.description,
                            category = "Acne"
                        )
                    })
                }

                response.dry.let { dryItems ->
                    Log.d("repository", "dry skin data: $dryItems")
                    articles.addAll(dryItems.map { dryItem ->
                        ArticleEntity(
                            id = dryItem.id.toString(),
                            name = dryItem.name,
                            photo = dryItem.photo,
                            description = dryItem.description,
                            category = "Dry"
                        )
                    })
                }

                skinologyDao.insertArticles(articles)
                emit(Result.Success(articles))
            }

        } catch (e: Exception) {
            emit(Result.Error("Error: ${e.message}"))
        }
    }


    fun getArticleId(articleId: String): LiveData<Result<ArticleEntity>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val article = skinologyDao.getArticleId(articleId)
                emit(Result.Success(article))
            } catch (e: Exception) {
                emit(Result.Error("Error: ${e.message}"))
            }
        }

    //berdarkan jenis di article
    fun getSkinTypeByCategory(category: String): LiveData<Result<List<ArticleEntity>>> =
        liveData(Dispatchers.IO) {
            emit(Result.Loading)
            try {
                val localArticles = skinologyDao.getArticlesByCategoryDirect(category)
                if (localArticles.isNotEmpty()) {
                    emit(Result.Success(localArticles))
                } else {
                    val response: List<ArticleEntity> = when (category) {
                        "Normal" -> apiService.getSkinTypeNormal("normal").map { normalItem ->
                            ArticleEntity(
                                id = normalItem.id.toString(),
                                name = normalItem.name,
                                photo = normalItem.photo,
                                description = normalItem.description,
                                category = "Normal"
                            )
                        }

                        "Oily" -> apiService.getSkinTypeOily("oily").map { oilyItem ->
                            ArticleEntity(
                                id = oilyItem.id.toString(),
                                name = oilyItem.name,
                                photo = oilyItem.photo,
                                description = oilyItem.description,
                                category = "Oily"
                            )
                        }

                        "Acne" -> apiService.getSkinTypeAcne("acne").map { acneItem ->
                            ArticleEntity(
                                id = acneItem.id.toString(),
                                name = acneItem.name,
                                photo = acneItem.photo,
                                description = acneItem.description,
                                category = "Acne"
                            )
                        }

                        "Dry" -> apiService.getSkinTypeDry("dry").map { dryItem ->
                            ArticleEntity(
                                id = dryItem.id.toString(),
                                name = dryItem.name,
                                photo = dryItem.photo,
                                description = dryItem.description,
                                category = "Dry"
                            )
                        }

                        else -> emptyList()
                    }

                    if (response.isNotEmpty()) {
                        skinologyDao.insertArticles(response)
                    }

                    emit(Result.Success(response))
                }
            } catch (e: Exception) {
                emit(Result.Error("Error: ${e.message}"))
            }
        }

    suspend fun updateArticleDetails(id: String, newName: String,newPhoto: String, newDescription: String) {
        withContext(Dispatchers.IO) {
            skinologyDao.updateArticleDetails(id, newName, newPhoto, newDescription)
        }
    }

    suspend fun addArticles(articles: List<ArticleEntity>) {
        skinologyDao.insertArticles(articles)
    }


    companion object {
        @Volatile
        private var instance: SkinologyRepository? = null

        fun getInstance(
            skinologyDao: SkinologyDao,
            apiService: ApiService
        ): SkinologyRepository =
            instance ?: synchronized(this) {
                instance ?: SkinologyRepository(skinologyDao, apiService)
            }.also { instance = it }
    }
}