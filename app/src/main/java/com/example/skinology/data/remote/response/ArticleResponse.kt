package com.example.skinology.data.remote.response

import com.google.gson.annotations.SerializedName

data class ArticleResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataItem(

	@field:SerializedName("skinType")
	val skinType: String? = null,

	@field:SerializedName("topics")
	val topics: List<String?>? = null
)
