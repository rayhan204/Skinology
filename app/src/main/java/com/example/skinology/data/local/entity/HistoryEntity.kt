package com.example.skinology.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_table")
data class HistoryEntity (
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = "",

    @ColumnInfo(name = "prediction")
    var prediction: String = "",

    @ColumnInfo(name = "image")
    var image: String = "",

    @ColumnInfo(name = "date")
    var date: String = ""
)