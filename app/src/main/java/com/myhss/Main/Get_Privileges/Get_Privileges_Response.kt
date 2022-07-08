package com.uk.myhss.Main.Get_Privileges

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Get_Privileges_Response {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}