package com.uk.myhss.ui.guru_dakshina.Model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class Get_Sankhya_Add_Response {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}