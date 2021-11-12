package com.recordlab.dailyscoop.network.response

import java.sql.Timestamp

data class ResponseDiaryDetail(
    val content: String,
    val image: String,
    val date: Timestamp,
    val emotions: List<String>?,
    val theme: String
)