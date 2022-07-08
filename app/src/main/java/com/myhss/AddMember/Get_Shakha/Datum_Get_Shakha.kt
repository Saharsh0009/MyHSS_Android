package com.uk.myhss.AddMember.Get_Shakha

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class Datum_Get_Shakha {

    @SerializedName("org_chapter_id")
    @Expose
    private var orgChapterId: String? = null

    @SerializedName("chapter_name")
    @Expose
    private var chapterName: String? = null

    @SerializedName("contact_person_name")
    @Expose
    private var contactPersonName: String? = null

    @SerializedName("building_name")
    @Expose
    private var buildingName: String? = null

    @SerializedName("address_line_1")
    @Expose
    private var addressLine1: String? = null

    @SerializedName("address_line_2")
    @Expose
    private var addressLine2: String? = null

    @SerializedName("city")
    @Expose
    private var city: String? = null

    @SerializedName("county")
    @Expose
    private var county: String? = null

    @SerializedName("postal_code")
    @Expose
    private var postalCode: String? = null

    @SerializedName("country")
    @Expose
    private var country: String? = null

    @SerializedName("latitude")
    @Expose
    private var latitude: String? = null

    @SerializedName("longitude")
    @Expose
    private var longitude: String? = null

    @SerializedName("phone")
    @Expose
    private var phone: String? = null

    @SerializedName("email")
    @Expose
    private var email: String? = null

    @SerializedName("day")
    @Expose
    private var day: String? = null

    @SerializedName("start_time")
    @Expose
    private var startTime: String? = null

    @SerializedName("end_time")
    @Expose
    private var endTime: String? = null

    @SerializedName("distanceInMiles")
    @Expose
    private var distanceInMiles: Float? = null

    fun getOrgChapterId(): String? {
        return orgChapterId
    }

    fun setOrgChapterId(orgChapterId: String?) {
        this.orgChapterId = orgChapterId
    }

    fun getChapterName(): String? {
        return chapterName
    }

    fun setChapterName(chapterName: String?) {
        this.chapterName = chapterName
    }

    fun getContactPersonName(): String? {
        return contactPersonName
    }

    fun setContactPersonName(contactPersonName: String?) {
        this.contactPersonName = contactPersonName
    }

    fun getBuildingName(): String? {
        return buildingName
    }

    fun setBuildingName(buildingName: String?) {
        this.buildingName = buildingName
    }

    fun getAddressLine1(): String? {
        return addressLine1
    }

    fun setAddressLine1(addressLine1: String?) {
        this.addressLine1 = addressLine1
    }

    fun getAddressLine2(): String? {
        return addressLine2
    }

    fun setAddressLine2(addressLine2: String?) {
        this.addressLine2 = addressLine2
    }

    fun getCity(): String? {
        return city
    }

    fun setCity(city: String?) {
        this.city = city
    }

    fun getCounty(): String? {
        return county
    }

    fun setCounty(county: String?) {
        this.county = county
    }

    fun getPostalCode(): String? {
        return postalCode
    }

    fun setPostalCode(postalCode: String?) {
        this.postalCode = postalCode
    }

    fun getCountry(): String? {
        return country
    }

    fun setCountry(country: String?) {
        this.country = country
    }

    fun getLatitude(): String? {
        return latitude
    }

    fun setLatitude(latitude: String?) {
        this.latitude = latitude
    }

    fun getLongitude(): String? {
        return longitude
    }

    fun setLongitude(longitude: String?) {
        this.longitude = longitude
    }

    fun getPhone(): String? {
        return phone
    }

    fun setPhone(phone: String?) {
        this.phone = phone
    }

    fun getEmail(): String? {
        return email
    }

    fun setEmail(email: String?) {
        this.email = email
    }

    fun getDay(): String? {
        return day
    }

    fun setDay(day: String?) {
        this.day = day
    }

    fun getStartTime(): String? {
        return startTime
    }

    fun setStartTime(startTime: String?) {
        this.startTime = startTime
    }

    fun getEndTime(): String? {
        return endTime
    }

    fun setEndTime(endTime: String?) {
        this.endTime = endTime
    }

    fun getdistanceInMiles(): Float? {
        return distanceInMiles
    }

    fun setdistanceInMiles(distanceInMiles: Float?) {
        this.distanceInMiles = distanceInMiles
    }
}