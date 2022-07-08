package com.uk.myhss.AddMember.Pincode

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Data_Get_Pincode {
    @SerializedName("Summaries")
    @Expose
    var summaries: List<Summary_Address>? = null
}