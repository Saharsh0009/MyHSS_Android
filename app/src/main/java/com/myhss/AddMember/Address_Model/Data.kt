package com.uk.myhss.AddMember.Address_Model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class Data {
    @SerializedName("Summaries")
    @Expose
    private var summaries: List<Summary?>? = null

    fun getSummaries(): List<Summary?>? {
        return summaries
    }

    fun setSummaries(summaries: List<Summary?>?) {
        this.summaries = summaries
    }
}