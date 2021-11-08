package com.recordlab.dailyscoop.network.request

data class RequestWriteDiary(
    val content: String,
    val image: String,
    val emotions: List<String>,
    val theme: String
)
