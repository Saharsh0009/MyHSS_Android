package com.uk.myhss.ui.sankhya_report.Model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class Sankhya_details_member {
    @SerializedName("member_id")
    @Expose
    var memberId: String? = null

    @SerializedName("first_name")
    @Expose
    var firstName: String? = null

    @SerializedName("last_name")
    @Expose
    var lastName: String? = null

    @SerializedName("middle_name")
    @Expose
    var middleName: String? = null

    @SerializedName("gender")
    @Expose
    var gender: String? = null

    @SerializedName("dob")
    @Expose
    var dob: String? = null

    @SerializedName("age_category")
    @Expose
    var ageCategory: String? = null
}