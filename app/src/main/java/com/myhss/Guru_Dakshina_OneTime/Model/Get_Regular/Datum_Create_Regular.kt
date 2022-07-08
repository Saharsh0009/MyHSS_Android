package com.uk.myhss.Guru_Dakshina_OneTime.Model.Get_Regular

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class Datum_Create_Regular {
    @SerializedName("account_name")
    @Expose
    var accountName: String? = null

    @SerializedName("account_number")
    @Expose
    var accountNumber: String? = null

    @SerializedName("sort_code")
    @Expose
    var sortCode: String? = null

    @SerializedName("reference_no")
    @Expose
    var referenceNo: String? = null

    @SerializedName("paid_amount")
    @Expose
    var paidAmount: String? = null

    @SerializedName("frequency")
    @Expose
    var frequency: String? = null

    @SerializedName("gift_aid")
    @Expose
    var giftAid: String? = null
}