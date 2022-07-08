package com.uk.myhss.AddMember.Get_Occupation

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class Datum_Get_Occupation {
    @SerializedName("occupation_id")
    @Expose
    var occupationId: String? = null

    @SerializedName("occupation_name")
    @Expose
    var occupationName: String? = null
}