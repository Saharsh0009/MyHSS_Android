package com.uk.myhss.AddMember.Get_Shakha

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class Get_Shakha_Response {

    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Datum_Get_Shakha>? = null
}