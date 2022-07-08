package com.myhss.Splash.Model.Biometric

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class Biometric_Datum {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("biometric_key")
    @Expose
    var biometricKey: String? = null
}