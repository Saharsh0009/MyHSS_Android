package com.uk.myhss.ui.sankhya_report.Model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class Sankhya_List_Response {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Sankhya_Datum>? = null
}