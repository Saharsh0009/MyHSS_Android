package com.myhss.AllShakha.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Datum_Get_Shakha_Details {
    @SerializedName("org_chapter_id")
    @Expose
    var org_chapter_id: String? = null

    @SerializedName("org_id")
    @Expose
    var org_id: String? = null

    @SerializedName("org_level_id")
    @Expose
    var org_level_id: String? = null

    @SerializedName("chapter_name")
    @Expose
    var chapter_name: String? = null

    @SerializedName("sangh_samiti")
    @Expose
    var sangh_samiti: String? = null

    @SerializedName("contact_person_name")
    @Expose
    var contact_person_name: String? = null

    @SerializedName("phone")
    @Expose
    var phone: String? = null

    @SerializedName("address_id")
    @Expose
    var address_id: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("day")
    @Expose
    var day: String? = null

    @SerializedName("start_time")
    @Expose
    var start_time: String? = null

    @SerializedName("end_time")
    @Expose
    var end_time: String? = null

    @SerializedName("second_day")
    @Expose
    var second_day: String? = null

    @SerializedName("second_start_time")
    @Expose
    var second_start_time: String? = null

    @SerializedName("second_end_time")
    @Expose
    var second_end_time: String? = null

    @SerializedName("effective_start_date")
    @Expose
    var effective_start_date: String? = null

    @SerializedName("effective_end_date")
    @Expose
    var effective_end_date: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("org_chapter_rel_id")
    @Expose
    var org_chapter_rel_id: String? = null

    @SerializedName("parent_org_chapter_id")
    @Expose
    var parent_org_chapter_id: String? = null

    @SerializedName("child_org_chapter_id")
    @Expose
    var child_org_chapter_id: String? = null

    @SerializedName("building_name")
    @Expose
    var building_name: String? = null

    @SerializedName("address_line_1")
    @Expose
    var address_line_1: String? = null

    @SerializedName("address_line_2")
    @Expose
    var address_line_2: String? = null

    @SerializedName("city")
    @Expose
    var city: String? = null

    @SerializedName("county")
    @Expose
    var county: String? = null

    @SerializedName("postal_code")
    @Expose
    var postal_code: String? = null

    @SerializedName("country")
    @Expose
    var country: String? = null

    @SerializedName("latitude")
    @Expose
    var latitude: String? = null

    @SerializedName("longitude")
    @Expose
    var longitude: String? = null
}