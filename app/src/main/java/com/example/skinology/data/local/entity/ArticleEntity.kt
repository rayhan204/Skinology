package com.example.skinology.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article")
data class ArticleEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = "",

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "photo")
    var photo: String = "",

    @ColumnInfo(name = "description")
    var description: String = "",

    @ColumnInfo(name = "category")
    var category: String = "",

)