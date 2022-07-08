package com.uk.myhss.AddMember.Get_Dietaries

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class Datum_Get_Dietaries {
    @SerializedName("dietary_requirements_id")
    @Expose
    var dietaryRequirementsId: String? = null

    @SerializedName("dietary_requirements_name")
    @Expose
    var dietaryRequirementsName: String? = null
}