package com.uk.myhss.ui.my_family.Model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class Datum {

    private var checked = false

    fun isChecked(): Boolean {
        return checked
    }

    fun setChecked(checked: Boolean) {
        this.checked = checked
    }

    @SerializedName("member_id")
    @Expose
    val memberId: String? = null

    @SerializedName("first_name")
    @Expose
    val firstName: String? = null

    @SerializedName("middle_name")
    @Expose
    val middleName: String? = null

    @SerializedName("last_name")
    @Expose
    val lastName: String? = null

    @SerializedName("gender")
    @Expose
    val gender: String? = null

    //This private field to maintain to every row's state...!
    private var isSelected = false

    fun isSelected(): Boolean {
        return isSelected
    }

    fun setSelected(selected: Boolean) {
        isSelected = selected
    }

    //This private field to maintain to every row's state...!
    var position: Int = 0

    fun getposition(): Int {
        return position
    }

    fun setposition(selected: Int) {
        position = selected
    }

    /*fun getMemberId(): String? {
        return memberId
    }

    fun setMemberId(memberId: String?) {
        this.memberId = memberId
    }

    fun getFirstName(): String? {
        return firstName
    }

    fun setFirstName(firstName: String?) {
        this.firstName = firstName
    }

    fun getMiddleName(): String? {
        return middleName
    }

    fun setMiddleName(middleName: String?) {
        this.middleName = middleName
    }

    fun getLastName(): String? {
        return lastName
    }

    fun setLastName(lastName: String?) {
        this.lastName = lastName
    }

    fun getGender(): String? {
        return gender
    }

    fun setGender(gender: String?) {
        this.gender = gender
    }*/
}