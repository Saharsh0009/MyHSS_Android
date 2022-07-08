package com.uk.myhss.AddMember.Get_Relationship

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class Get_Relationship_Response {

    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Datum_Relationship>? = null
}