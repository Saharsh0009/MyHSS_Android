package com.uk.myhss.AddMember.Dietaries_Model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class Datum {

    @SerializedName("dietary_requirements_id")
    @Expose
    private var dietaryRequirementsId: String? = null

    @SerializedName("dietary_requirements_name")
    @Expose
    private var dietaryRequirementsName: String? = null

    fun getDietaryRequirementsId(): String? {
        return dietaryRequirementsId
    }

    fun setDietaryRequirementsId(dietaryRequirementsId: String?) {
        this.dietaryRequirementsId = dietaryRequirementsId
    }

    fun getDietaryRequirementsName(): String? {
        return dietaryRequirementsName
    }

    fun setDietaryRequirementsName(dietaryRequirementsName: String?) {
        this.dietaryRequirementsName = dietaryRequirementsName
    }
}