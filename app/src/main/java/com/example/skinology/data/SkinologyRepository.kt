package com.example.skinology.data

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
            // Mengecek data lokal terlebih dahulu
            val localArticles = skinologyDao.getAllArticles()  // This returns LiveData<List<ArticleEntity>>

            // Observe LiveData (blocking call in this context)
            val localData = localArticles.value // Access the data directly (works because we are emitting this result in liveData builder)

            // Emit data from local database if available
            if (!localData.isNullOrEmpty()) {
                emit(Result.Success(localData))
            }

            // Mengambil data dari API
            val response = apiService.getAllSkinTypes()

            // Menggabungkan semua data jenis kulit menjadi satu daftar artikel
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

            // Menyimpan data artikel ke dalam database lokal
            skinologyDao.insertArticles(articles)

            // Mengirimkan hasilnya
            emit(Result.Success(articles))

        } catch (e: Exception) {
            emit(Result.Error("Error: ${e.message}"))
        }
    }

    fun getArticleId(articleId: Int): LiveData<Result<ArticleEntity>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val article = skinologyDao.getArticleId(articleId)
            emit(Result.Success(article))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An error occurred"))
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