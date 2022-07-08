package com.uk.myhss.Main.Get_Prpfile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Get_Profile_Response {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Datum_Get_Profile>? = null

    @SerializedName("member")
    @Expose
    var member: List<Member_Get_Profile>? = null
}