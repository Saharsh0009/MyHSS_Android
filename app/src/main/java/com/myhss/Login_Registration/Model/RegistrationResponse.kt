package com.uk.myhss.Login_Registration.Model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class RegistrationResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("error")
    @Expose
    val error: Map<String, String>? = null

}