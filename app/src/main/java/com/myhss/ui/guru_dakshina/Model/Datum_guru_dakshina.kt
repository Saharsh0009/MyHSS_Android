package com.uk.myhss.ui.my_family.Model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class Datum_guru_dakshina : java.io.Serializable {

    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("first_name")
    @Expose
    var firstName: String? = null

    @SerializedName("last_name")
    @Expose
    var lastName: String? = null

    @SerializedName("middle_name")
    @Expose
    var middleName: String? = null

    @SerializedName("buyer_name")
    @Expose
    var buyerName: String? = null

    @SerializedName("buyer_email")
    @Expose
    var buyerEmail: String? = null

    @SerializedName("age_category")
    @Expose
    var ageCategory: String? = null

    @SerializedName("gift_aid")
    @Expose
    var giftAid: String? = null

    @SerializedName("shakha_id")
    @Expose
    var shakhaId: String? = null

    @SerializedName("member_id")
    @Expose
    var memberId: String? = null

    @SerializedName("address_line")
    @Expose
    var addressLine: String? = null

    @SerializedName("city")
    @Expose
    var city: String? = null

    @SerializedName("country")
    @Expose
    var country: String? = null

    @SerializedName("postcode")
    @Expose
    var postcode: String? = null

    @SerializedName("paid_amount")
    @Expose
    var paidAmount: String? = null

    @SerializedName("currency")
    @Expose
    var currency: String? = null

    @SerializedName("order_id")
    @Expose
    var orderId: String? = null

    @SerializedName("txn_id")
    @Expose
    var txnId: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("is_linked_member")
    @Expose
    var isLinkedMember: String? = null

    @SerializedName("dakshina")
    @Expose
    var dakshina: String? = null

    @SerializedName("recurring")
    @Expose
    var recurring: String? = null

    @SerializedName("nidhi_notes")
    @Expose
    var nidhiNotes: String? = null

    @SerializedName("is_purnima_dakshina")
    @Expose
    var isPurnimaDakshina: String? = null

    @SerializedName("start_date")
    @Expose
    var startDate: String? = null

    @SerializedName("cancel_date")
    @Expose
    var cancelDate: String? = null

    @SerializedName("created_by")
    @Expose
    var createdBy: String? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("chapter_name")
    @Expose
    var chapterName: String? = null
}