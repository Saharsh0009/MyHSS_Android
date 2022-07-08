package com.uk.myhss.Main.Family_Member

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class Family_Member_Response {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Datum_Family_Member>? = null
}