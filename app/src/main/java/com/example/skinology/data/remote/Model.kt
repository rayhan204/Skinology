package com.example.skinology.data.remote

import com.example.skinology.data.local.entity.ArticleEntity
import com.example.skinology.data.remote.response.AcneItem
import com.example.skinology.data.remote.response.DryItem
import com.example.skinology.data.remote.response.NormalItem
import com.example.skinology.data.remote.response.OilyItem

fun DryItem.toArticleEntity(): ArticleEntity {
    return ArticleEntity(
        id = this.id.toString(),                // Sesuaikan dengan field dari DryItem
        name = this.name,
        photo = this.photo,
        description = this.description,
        category = "Dry"             // Tambahkan kategori secara manual
    )
}

fun OilyItem.toArticleEntity(): ArticleEntity {
    return ArticleEntity(
        id = this.id.toString(),
        name = this.name,
        photo = this.photo,
        description = this.description,
        category = "Oily"
    )
}

fun NormalItem.toArticleEntity(): ArticleEntity {
    return ArticleEntity(
        id = this.id.toString(),
        name = this.name,
        photo = this.photo,
        description = this.description,
        category = "Normal"
    )
}

fun AcneItem.toArticleEntity(): ArticleEntity {
    return ArticleEntity(
        id = this.id.toString(),
        name = this.name,
        photo = this.photo,
        description = this.description,
        category = "Acne"
    )
}


