package com.uk.myhss.AddMember.Pincode

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class Get_Pincode_Response {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data_Get_Pincode? = null
}