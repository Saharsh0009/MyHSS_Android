package com.uk.myhss.ui.sankhya_report.Model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class Sankhya_details_Response {

    @SerializedName("status")
    @Expose
    val status: Boolean? = null

    @SerializedName("message")
    @Expose
    val message: String? = null

    @SerializedName("data")
    @Expose
    val data: List<Sankhya_details_Datum>? = null
}