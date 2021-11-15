package com.recordlab.dailyscoop.network.response

import com.google.gson.annotations.SerializedName

data class ResponseImageUrl(
    @SerializedName("url")
    val data: String
)
