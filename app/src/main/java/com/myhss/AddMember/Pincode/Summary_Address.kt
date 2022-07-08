package com.uk.myhss.AddMember.Pincode

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class Summary_Address {
    @SerializedName("Id")
    @Expose
    var id: Int? = null

    @SerializedName("StreetAddress")
    @Expose
    var streetAddress: String? = null

    @SerializedName("Place")
    @Expose
    var place: String? = null
}