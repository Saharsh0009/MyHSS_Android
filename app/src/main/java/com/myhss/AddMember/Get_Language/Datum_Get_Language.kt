package com.uk.myhss.AddMember.Get_Language

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class Datum_Get_Language {
    @SerializedName("language_id")
    @Expose
    var languageId: String? = null

    @SerializedName("language_name")
    @Expose
    var languageName: String? = null
}