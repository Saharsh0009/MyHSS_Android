package com.uk.myhss.AddMember.Get_Indianstates

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class Datum_Get_Indianstates {
    @SerializedName("indian_state_list_id")
    @Expose
    var indianStateListId: String? = null

    @SerializedName("state_name")
    @Expose
    var stateName: String? = null
}