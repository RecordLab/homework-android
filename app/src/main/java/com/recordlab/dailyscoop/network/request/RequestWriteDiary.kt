package com.recordlab.dailyscoop.network.request

import android.graphics.Bitmap

data class RequestWriteDiary(
    val content: String,
    val image: String,
    val emotions: List<String>,
    val theme: String,
    val date: String?
)
