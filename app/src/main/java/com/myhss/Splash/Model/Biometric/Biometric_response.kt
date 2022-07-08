package com.myhss.Splash.Model.Biometric

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class Biometric_response {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Biometric_Datum? = null
}