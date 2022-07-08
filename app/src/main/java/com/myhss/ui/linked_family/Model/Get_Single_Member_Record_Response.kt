package com.uk.myhss.ui.linked_family.Model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class Get_Single_Member_Record_Response {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Get_Single_Member_Record_Datum>? = null
}