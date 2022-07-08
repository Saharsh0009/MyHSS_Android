package com.uk.myhss.Guru_Dakshina_OneTime.Model.Get_Onetime

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class Get_Create_Onetime {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Datum_Create_Onetime>? = null
}