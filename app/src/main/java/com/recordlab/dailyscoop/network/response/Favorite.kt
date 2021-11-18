package com.recordlab.dailyscoop.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Favorite {
    @SerializedName("quote")
    @Expose
    var quote: String? = null
}