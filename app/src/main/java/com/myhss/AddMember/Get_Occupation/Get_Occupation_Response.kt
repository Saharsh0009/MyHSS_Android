package com.uk.myhss.AddMember.Get_Occupation

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class Get_Occupation_Response {
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Datum_Get_Occupation>? = null
}