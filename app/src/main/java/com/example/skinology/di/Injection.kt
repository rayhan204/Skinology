package com.example.skinology.di

import android.content.Context
import com.example.skinology.data.SkinologyRepository
import com.example.skinology.data.local.room.SkinologyDatabase
import com.example.skinology.data.remote.retrofit.ApiConfig

object Injection {

    fun provideRepository(context: Context): SkinologyRepository {
        val database = SkinologyDatabase.getDatabase(context)
        val dao = database.skinologyDao()
        val apiService = ApiConfig.getApiService()

        return SkinologyRepository.getInstance(dao, apiService)
    }
}