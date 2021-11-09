package com.recordlab.dailyscoop.network.response

import com.google.gson.annotations.SerializedName
import com.recordlab.dailyscoop.data.DiaryData

data class ResponseDiaryList(
    @SerializedName("diaries")
    val data: List<DiaryData>
)