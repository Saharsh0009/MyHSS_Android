package com.myhss.ui.SuchanaBoard.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Get_Suchana_Seen_Response {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}