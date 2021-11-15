package com.recordlab.dailyscoop.network.request

data class RequestSignIn(
    val id: String,
    val password: String
)

data class RequestChangeNickname(
    val newNickname: String,
)

data class RequestChangePassword(
    val password: String,
    val newPassword: String
)