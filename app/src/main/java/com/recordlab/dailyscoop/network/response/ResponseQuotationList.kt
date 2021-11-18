package com.recordlab.dailyscoop.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseQuotationList {
    @SerializedName("favorite")
    @Expose
    var favorite: List<Favorite>? = null
}