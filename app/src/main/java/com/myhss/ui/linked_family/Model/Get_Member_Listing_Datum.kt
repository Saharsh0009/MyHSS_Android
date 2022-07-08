package com.uk.myhss.ui.linked_family.Model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class Get_Member_Listing_Datum {
    @SerializedName("member_id")
    @Expose
    var memberId: String? = null

    @SerializedName("first_name")
    @Expose
    var firstName: String? = null

    @SerializedName("middle_name")
    @Expose
    var middleName: String? = null

    @SerializedName("last_name")
    @Expose
    var lastName: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("mobile")
    @Expose
    var mobile: String? = null

    @SerializedName("dob")
    @Expose
    var dob: String? = null

    @SerializedName("gender")
    @Expose
    var gender: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("is_email_verified")
    @Expose
    var isEmailVerified: String? = null

    @SerializedName("is_guardian_approved")
    @Expose
    var isGuardianApproved: String? = null

    @SerializedName("is_admin_approved")
    @Expose
    var isAdminApproved: String? = null

    @SerializedName("rejection_msg")
    @Expose
    var rejectionMsg: String? = null

    @SerializedName("chapter_name")
    @Expose
    var chapterName: String? = null

    @SerializedName("org_name")
    @Expose
    var orgName: String? = null

    @SerializedName("member_age")
    @Expose
    var memberAge: Int? = null

    @SerializedName("age_categories")
    @Expose
    var ageCategories: String? = null
}