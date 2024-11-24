package com.example.skinology.data

import com.example.skinology.data.local.room.SkinologyDao

class SkinologyRepository private constructor(
    private val skinologyDao: SkinologyDao
){

    companion object {
        @Volatile
        private var instance: SkinologyRepository? = null

        fun getInstance(skinologyDao: SkinologyDao): SkinologyRepository =
            instance ?: synchronized(this) {
                instance ?: SkinologyRepository(skinologyDao)
            }.also { instance = it }
    }
}