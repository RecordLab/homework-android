package com.recordlab.dailyscoop.network.response

import com.google.gson.annotations.SerializedName

data class ResponseSignup(
    @SerializedName("data") val data: TokenData? = null
)

data class TokenData(
    @SerializedName("nickname") val nickname: String,
    @SerializedName("token") val token: String
)
