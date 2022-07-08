package com.uk.myhss.AddMember.Get_Relationship

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class Datum_Relationship {

    @SerializedName("member_relationship_id")
    @Expose
    var memberRelationshipId: String? = null

    @SerializedName("relationship_name")
    @Expose
    var relationshipName: String? = null
}