package com.recordlab.dailyscoop.network.response

import com.google.gson.annotations.SerializedName

data class ResponseUserProfile(
    @SerializedName("id") val id: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("profile_image") val image: String,
)

data class ResponseUserProfileImage(
    @SerializedName("message") val message: String
)