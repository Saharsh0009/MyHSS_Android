package com.myhss.Splash.Model.Biometric.Latest_Update

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class latest_update_response {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: latest_update_Datum? = null
}