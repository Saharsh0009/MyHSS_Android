package com.myhss.ui.SuchanaBoard.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.uk.myhss.ui.sankhya_report.Model.Get_Sankhya_Utsav_Datum

class Get_Suchana_Response {
    @SerializedName("status")
    @Expose
    val status: Boolean? = null

    @SerializedName("message")
    @Expose
    val message: String? = null

    @SerializedName("data")
    @Expose
    val data: List<Get_Suchana_Datum>? = null
}