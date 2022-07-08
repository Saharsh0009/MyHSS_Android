package com.myhss.AllShakha.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Get_Shakha_Response {
    @SerializedName("status")
    @Expose
    val status: Boolean? = null

    @SerializedName("message")
    @Expose
    val message: String? = null

    @SerializedName("data")
    @Expose
    val data: List<Get_Shakha_Datum>? = null
}