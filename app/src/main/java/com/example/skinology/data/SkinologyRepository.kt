package com.example.skinology.data

import androidx.camera.core.CameraEffect
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.skinology.data.local.room.SkinologyDao
import com.example.skinology.data.remote.retrofit.ApiService
import com.example.skinology.data.local.entity.ArticleEntity
import kotlinx.coroutines.Dispatchers

class SkinologyRepository private constructor(
    private val skinologyDao: SkinologyDao,
    private val apiService: ApiService
){

    fun getAllSkinTypes(): LiveData<Result<List<ArticleEntity>>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val localArticles = skinologyDao.getAllArticles()  // This returns LiveData<List<ArticleEntity>>

            val localData = localArticles.value // Access the data directly (works because we are emitting this result in liveData builder)

            if (!localData.isNullOrEmpty()) {
                emit(Result.Success(localData))
            }

            val response = apiService.getAllSkinTypes()

            val articles = mutableListOf<ArticleEntity>()

            response.normal.let { normalItems ->
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

        } catch (e: Exception) {
            emit(Result.Error("Error: ${e.message}"))
        }
    }

    fun getArticleId(articleId: String): LiveData<Result<ArticleEntity>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val article = skinologyDao.getArticleId(articleId)
            emit(Result.Success(article))
        } catch (e: Exception) {
            emit(Result.Error("Error: ${e.message}"))
        }
    }

    //berdarkan jenis di article
    fun getSkinTypeByCategory(category: String): LiveData<Result<List<ArticleEntity>>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val localArticles = skinologyDao.getArticlesByCategoryDirect(category)
            if (localArticles.isNotEmpty()) {
                emit(Result.Success(localArticles))
            }

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
        } catch (e: Exception) {
            emit(Result.Error("Error: ${e.message}"))
        }
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