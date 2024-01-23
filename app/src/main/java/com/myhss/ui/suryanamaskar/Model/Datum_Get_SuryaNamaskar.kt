package com.myhss.ui.suryanamaskar.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Datum_Get_SuryaNamaskar : java.io.Serializable {

    @SerializedName("surya_namaskar_count_id")
    @Expose
    private var surya_namaskar_count_id: String? = null

    @SerializedName("member_id")
    @Expose
    private var member_id: String? = null

    @SerializedName("count")
    @Expose
    private var count: String? = null

    @SerializedName("count_date")
    @Expose
    private var count_date: String? = null

    @SerializedName("member_name")
    @Expose
    private var member_name: String? = null

    fun getsurya_namaskar_count_id(): String? {
        return surya_namaskar_count_id
    }

    fun setsurya_namaskar_count_id(surya_namaskar_count_id: String?) {
        this.surya_namaskar_count_id = surya_namaskar_count_id
    }

    fun getmember_id(): String? {
        return member_id
    }

    fun setmember_id(member_id: String?) {
        this.member_id = member_id
    }

    fun getcount(): String? {
        return count
    }

    fun setcount(count: String?) {
        this.count = count
    }

    fun getcount_date(): String? {
        return count_date
    }

    fun setcount_date(count_date: String?) {
        this.count_date = count_date
    }

    fun getmember_name(): String? {
        return member_name
    }

    fun setmember_name(member_name: String?) {
        this.member_name = member_name
    }

}