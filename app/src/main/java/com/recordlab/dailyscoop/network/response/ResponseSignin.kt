package com.recordlab.dailyscoop.network.response

data class ResponseSignIn(
    val data: UserInfoData
)

data class UserInfoData(
    val nickname: String,
    val token: String
)

data class ResponseUserInfo(
    val id: String,
    val nickname: String
)

data class ResponseChange(
    val message: String,
)