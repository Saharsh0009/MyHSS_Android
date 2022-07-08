package com.myhss.ui.Barchat.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class save_suryanamaskarResponse {

    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}