package com.recordlab.dailyscoop.network.response

import com.google.gson.annotations.SerializedName

data class ResponseDiariesCount(
    @SerializedName("day_count") val dayCount: Int,
    @SerializedName("diary_count") val diaryCount: Int
)