package com.myhss.ui.SuchanaBoard.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Get_Suchana_Datum {
    @SerializedName("suchana_title")
    @Expose
    val suchana_title: String? = null

    @SerializedName("suchana_text")
    @Expose
    val suchana_text: String? = null

    @SerializedName("id")
    @Expose
    val id: String? = null

    @SerializedName("created_date")
    @Expose
    val created_date: String? = null

    @SerializedName("is_read")
    @Expose
    var is_read: String? = null
}