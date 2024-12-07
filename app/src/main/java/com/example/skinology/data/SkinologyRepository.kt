package com.example.skinology.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.skinology.data.local.room.SkinologyDao
import com.example.skinology.data.remote.retrofit.ApiService
import com.example.skinology.data.local.entity.ArticleEntity
import com.example.skinology.data.remote.response.AcneItem
import com.example.skinology.data.remote.response.DryItem
import com.example.skinology.data.remote.response.NormalItem
import com.example.skinology.data.remote.response.OilyItem
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

            Log.d("SkinologyRepository", "Inserting articles into database: $articles")
            skinologyDao.insertArticles(articles)
            emit(Result.Success(articles))

        } catch (e: Exception) {
            emit(Result.Error("Error: ${e.message}"))
        }
    }

    fun getArticleId(articleId: Int): LiveData<Result<ArticleEntity>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val article = skinologyDao.getArticleId(articleId)
            Log.d("SkinologyRepository", "Article retrieved: $article")
            emit(Result.Success(article))
        } catch (e: Exception) {
            Log.d("SkinologyRepository", "Error fetching article: ${e.message}")
            emit(Result.Error("Error: ${e.message}"))
        }
    }


    fun getSkinTypeOily(): LiveData<Result<List<OilyItem>>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.getSkinTypeOily("oily")
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error("Error: ${e.message}"))
        }
    }

    fun getSkinTypeNormal(): LiveData<Result<List<NormalItem>>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.getSkinTypeNormal("normal")
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error("Error: ${e.message}"))
        }
    }

    fun getSkinTypeAcne(): LiveData<Result<List<AcneItem>>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.getSkinTypeAcne("acne")
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error("Error: ${e.message}"))
        }
    }

    fun getSkinTypeDry(): LiveData<Result<List<DryItem>>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.getSkinTypeDry("dry")
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