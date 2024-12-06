package com.example.skinology.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.skinology.data.local.entity.HistoryEntity

@Database(entities = [HistoryEntity::class], version = 1, exportSchema = false)
abstract class SkinologyDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: SkinologyDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): SkinologyDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    SkinologyDatabase::class.java, "skinology_db"
                ).build()
            }
    }
}