package com.uk.myhss.ui.sankhya_report.Model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class Sankhya_Datum {
//    @SerializedName("org_chapter_id")
//    @Expose
//    var orgChapterId: String? = null

    @SerializedName("chapter_name")
    @Expose
    var chapterName: String? = null

    @SerializedName("id")
    @Expose
    val id: String? = null

    @SerializedName("utsav")
    @Expose
    val utsav: String? = null

    @SerializedName("event_date")
    @Expose
    val eventDate: String? = null

    @SerializedName("phone")
    @Expose
    val phone: String? = null

    @SerializedName("email")
    @Expose
    val email: String? = null

}