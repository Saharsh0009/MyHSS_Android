package com.myhss.ui.suryanamaskar.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Get_SuryaNamaskar_ModelResponse {

        @SerializedName("status")
        @Expose
        var status: Boolean? = null

        @SerializedName("message")
        @Expose
        var message: String? = null

        @SerializedName("data")
        @Expose
        var data: List<Datum_Get_SuryaNamaskar>? = null
}