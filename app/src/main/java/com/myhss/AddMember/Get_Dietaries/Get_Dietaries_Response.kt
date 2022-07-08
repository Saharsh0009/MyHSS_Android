package com.uk.myhss.AddMember.Get_Dietaries

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class Get_Dietaries_Response {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Datum_Get_Dietaries>? = null
}