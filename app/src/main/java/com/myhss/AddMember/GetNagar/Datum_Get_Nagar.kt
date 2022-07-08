package com.uk.myhss.AddMember.GetNagar

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class Datum_Get_Nagar {
    @SerializedName("org_chapter_id")
    @Expose
    var orgChapterId: String? = null

    @SerializedName("chapter_name")
    @Expose
    var chapterName: String? = null
}