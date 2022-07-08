package com.uk.myhss.ui.linked_family.Model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class Get_Member_Check_Username_Exist_Response {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}