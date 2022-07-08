package com.uk.myhss.Main.Family_Member

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class Datum_Family_Member {
    @SerializedName("first_name")
    @Expose
    var firstName: String? = null

    @SerializedName("last_name")
    @Expose
    var lastName: String? = null

    @SerializedName("middle_name")
    @Expose
    var middleName: String? = null
}