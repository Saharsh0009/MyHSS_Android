package com.uk.myhss.AddMember.Get_CreateMember

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class Get_CreateMembership_Response {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}