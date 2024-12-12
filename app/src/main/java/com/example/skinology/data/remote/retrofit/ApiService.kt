package com.example.skinology.data.remote.retrofit

import com.example.skinology.data.remote.response.AcneItem
import com.example.skinology.data.remote.response.ArticleResponse
import com.example.skinology.data.remote.response.DryItem
import com.example.skinology.data.remote.response.NormalItem
import com.example.skinology.data.remote.response.OilyItem
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("skinTypes")
    suspend fun getAllSkinTypes(): ArticleResponse

    @GET("skinTypes/{type}")
    suspend fun getSkinTypeOily(
        @Path("type") skinType: String
    ): List<OilyItem>

    @GET("skinTypes/{type}")
    suspend fun getSkinTypeNormal(
        @Path("type") skinType: String
    ): List<NormalItem>

    @GET("skinTypes/{type}")
    suspend fun getSkinTypeAcne(
        @Path("type") skinType: String
    ): List<AcneItem>

    @GET("skinTypes/{type}")
    suspend fun getSkinTypeDry(
        @Path("type") skinType: String
    ): List<DryItem>

}