package com.uk.myhss.Guru_Dakshina_OneTime.Model.Get_Onetime

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class Datum_Create_Onetime {
    @SerializedName("order_id")
    @Expose
    var orderId: String? = null

    @SerializedName("paid_amount")
    @Expose
    var paidAmount: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("gift_aid")
    @Expose
    var giftAid: String? = null
}