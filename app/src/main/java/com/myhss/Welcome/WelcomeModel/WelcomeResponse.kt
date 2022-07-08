package com.uk.myhss.Welcome.WelcomeModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WelcomeResponse {

    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("member_id")
    @Expose
    var member_id: String? = null

    @SerializedName("tooltip")
    @Expose
    var tooltip: String? = null

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

    fun getType(): String? {
        return type
    }

    fun setType(type: String?) {
        this.type = type
    }

    fun getTooltip(): String? {
        return tooltip
    }

    fun setTooltip(tooltip: String?) {
        this.tooltip = tooltip
    }*/
}