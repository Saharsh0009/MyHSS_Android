package com.uk.myhss.ui.my_family.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class guru_dakshina_response {

    @SerializedName("status")
    @Expose
    val status: Boolean? = null

    @SerializedName("message")
    @Expose
    val message: String? = null

    @SerializedName("data")
    @Expose
    val data: List<Datum_guru_dakshina>? = null
}