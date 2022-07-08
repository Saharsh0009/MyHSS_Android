package com.uk.myhss.Main.Get_Prpfile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Datum_Get_Profile {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("first_name")
    @Expose
    var firstName: String? = null

    @SerializedName("middle_name")
    @Expose
    var middleName: String? = null

    @SerializedName("last_name")
    @Expose
    var lastName: String? = null

    @SerializedName("username")
    @Expose
    var username: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("phone")
    @Expose
    var phone: String? = null

    @SerializedName("member_id")
    @Expose
    var memberId: String? = null

    @SerializedName("facebook_id")
    @Expose
    var facebookId: String? = null

    @SerializedName("google_id")
    @Expose
    var googleId: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("email_verified_at")
    @Expose
    var emailVerifiedAt: String? = null

    @SerializedName("email_verification_code")
    @Expose
    var emailVerificationCode: Any? = null

    @SerializedName("otp")
    @Expose
    var otp: String? = null

    @SerializedName("role")
    @Expose
    var role: String? = null

    @SerializedName("privileges")
    @Expose
    var privileges: String? = null

    @SerializedName("password")
    @Expose
    var password: String? = null

    @SerializedName("token")
    @Expose
    var token: String? = null

    @SerializedName("device")
    @Expose
    var device: String? = null

    @SerializedName("device_token")
    @Expose
    var deviceToken: String? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("password_changed_at")
    @Expose
    var passwordChangedAt: String? = null

    @SerializedName("profile_word")
    @Expose
    var profileWord: String? = null

//    @SerializedName("device")
//    @Expose
//    var deviceType: String? = null
//
//    @SerializedName("device_token")
//    @Expose
//    var device_token: String? = null

//    @SerializedName("member")
//    @Expose
//    var member: Member_Get_Profile? = null
}