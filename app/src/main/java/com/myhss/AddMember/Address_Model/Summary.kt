package com.uk.myhss.AddMember.Address_Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Summary {
    @SerializedName("Id")
    @Expose
    private var id: Int? = null

    @SerializedName("StreetAddress")
    @Expose
    private var streetAddress: String? = null

    @SerializedName("Place")
    @Expose
    private var place: String? = null

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?) {
        this.id = id
    }

    fun getStreetAddress(): String? {
        return streetAddress
    }

    fun setStreetAddress(streetAddress: String?) {
        this.streetAddress = streetAddress
    }

    fun getPlace(): String? {
        return place
    }

    fun setPlace(place: String?) {
        this.place = place
    }

}