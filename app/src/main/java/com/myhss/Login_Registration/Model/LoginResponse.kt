package com.uk.myhss.Login_Registration.Model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class LoginResponse {

    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("user_id")
    @Expose
    var userId: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    /*@SerializedName("fullname")
    @Expose
    var fullname: String? = null*/

    @SerializedName("first_name")
    @Expose
    var firstname: String? = null

    @SerializedName("last_name")
    @Expose
    var lastname: String? = null

    @SerializedName("username")
    @Expose
    var username: String? = null

    @SerializedName("role")
    @Expose
    var role: String? = null

    @SerializedName("member_id")
    @Expose
    var memberId: String? = null

    @SerializedName("member_status")
    @Expose
    var memberStatus: String? = null

    @SerializedName("security_key")
    @Expose
    var securityKey: String? = null

    @SerializedName("token")
    @Expose
    var token: String? = null
}