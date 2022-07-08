package com.uk.myhss.ui.my_family.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class my_family_response {

    @SerializedName("status")
    @Expose
    val status: Boolean? = null

    @SerializedName("message")
    @Expose
    val message: String? = null

    @SerializedName("data")
    @Expose
    val data: List<Datum>? = null

    /*fun getStatus(): Boolean? {
        return status
    }

    fun setStatus(status: Boolean?) {
        this.status = status
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String?) {
        this.message = message
    }

    fun getData(): List<my_family_Datum?>? {
        return data
    }

    fun setData(data: List<my_family_Datum?>?) {
        this.data = data
    }*/
}