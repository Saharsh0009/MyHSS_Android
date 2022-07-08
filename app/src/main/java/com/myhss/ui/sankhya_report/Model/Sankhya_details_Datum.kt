package com.uk.myhss.ui.sankhya_report.Model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class Sankhya_details_Datum {

    @SerializedName("id")
    @Expose
    val id: String? = null

    @SerializedName("event_date")
    @Expose
    val eventDate: String? = null

    @SerializedName("org_chapter_id")
    @Expose
    val orgChapterId: String? = null

    @SerializedName("utsav")
    @Expose
    val utsav: String? = null

    @SerializedName("shishu_male")
    @Expose
    val shishuMale: String? = null

    @SerializedName("shishu_female")
    @Expose
    val shishuFemale: String? = null

    @SerializedName("baal")
    @Expose
    val baal: String? = null

    @SerializedName("baalika")
    @Expose
    val baalika: String? = null

    @SerializedName("kishore")
    @Expose
    val kishore: String? = null

    @SerializedName("kishori")
    @Expose
    val kishori: String? = null

    @SerializedName("tarun")
    @Expose
    val tarun: String? = null

    @SerializedName("taruni")
    @Expose
    val taruni: String? = null

    @SerializedName("yuva")
    @Expose
    val yuva: String? = null

    @SerializedName("yuvati")
    @Expose
    val yuvati: String? = null

    @SerializedName("proudh")
    @Expose
    val proudh: String? = null

    @SerializedName("proudha")
    @Expose
    val proudha: String? = null

    @SerializedName("created_by")
    @Expose
    val createdBy: String? = null

    @SerializedName("created_at")
    @Expose
    val createdAt: String? = null

    @SerializedName("update_by")
    @Expose
    val updateBy: String? = null

    @SerializedName("updated_at")
    @Expose
    val updatedAt: String? = null

    @SerializedName("status")
    @Expose
    val status: String? = null

    @SerializedName("member")
    @Expose
    val member: List<Sankhya_details_member>? = null

}