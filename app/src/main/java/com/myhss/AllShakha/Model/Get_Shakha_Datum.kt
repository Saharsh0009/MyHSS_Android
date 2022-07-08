package com.myhss.AllShakha.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Get_Shakha_Datum {
    @SerializedName("org_chapter_id")
    @Expose
    val org_chapter_id: String? = null

    @SerializedName("chapter_name")
    @Expose
    val chapter_name: String? = null

    @SerializedName("latitude")
    @Expose
    val latitude: String? = null

    @SerializedName("longitude")
    @Expose
    val longitude: String? = null
}