package com.recordlab.dailyscoop.network.response

import com.google.gson.annotations.SerializedName

data class ResponseEmotionsCount(
    @SerializedName("emotions") val emotions: Emotions
)

data class Emotions(
    val angry: Int,
    val anxious: Int,
    val bored: Int,
    val excitement: Int,
    @SerializedName("fun") val funn: Int,
    val happy: Int,
    val joy: Int,
    val nervous: Int,
    val relax: Int,
    val sad: Int,
    val sound: Int,
    val tired: Int
)