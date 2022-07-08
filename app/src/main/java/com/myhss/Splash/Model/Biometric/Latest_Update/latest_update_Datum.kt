package com.myhss.Splash.Model.Biometric.Latest_Update

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class latest_update_Datum {
    @SerializedName("ForceUpdateRequired")
    @Expose
    var forceUpdateRequired: String? = null

    @SerializedName("lastestAppVersion")
    @Expose
    var lastestAppVersion: String? = null
}