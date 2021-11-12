package com.recordlab.dailyscoop.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseQuotation {
    @SerializedName("result")
    @Expose
    var result: String? = null

    @SerializedName("respond")
    @Expose
    var respond: String? = null
}