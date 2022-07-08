package com.uk.myhss.Main.Get_Prpfile

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class Member_Get_Profile  {
    @SerializedName("member_id")
    @Expose
    var memberId: String? = null

    @SerializedName("first_name")
    @Expose
    var firstName: String? = null

    @SerializedName("middle_name")
    @Expose
    var middleName: String? = null

    @SerializedName("last_name")
    @Expose
    var lastName: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("secondary_email")
    @Expose
    var secondaryEmail: String? = null

    @SerializedName("land_line")
    @Expose
    var landLine: String? = null

    @SerializedName("mobile")
    @Expose
    var mobile: String? = null

    @SerializedName("whatsapp")
    @Expose
    var whatsapp: String? = null

    @SerializedName("address_id")
    @Expose
    var addressId: String? = null

    @SerializedName("gender")
    @Expose
    var gender: String? = null

    @SerializedName("dob")
    @Expose
    var dob: String? = null

    @SerializedName("relationship")
    @Expose
    var relationship: String? = null

    @SerializedName("other_relationship")
    @Expose
    var otherRelationship: String? = null

    @SerializedName("occupation")
    @Expose
    var occupation: String? = null

    @SerializedName("university_id")
    @Expose
    var universityId: String? = null

    @SerializedName("family_group_id")
    @Expose
    var familyGroupId: String? = null

    @SerializedName("special_med_dietry_info")
    @Expose
    var specialMedDietryInfo: String? = null

    @SerializedName("is_qualified_in_first_aid")
    @Expose
    var isQualifiedInFirstAid: String? = null

    @SerializedName("date_of_first_aid_qualification")
    @Expose
    var dateOfFirstAidQualification: Any? = null

    @SerializedName("first_aid_qualification_file")
    @Expose
    var firstAidQualificationFile: String? = null

    @SerializedName("medical_information_declare")
    @Expose
    var medicalInformationDeclare: String? = null

    @SerializedName("medical_details")
    @Expose
    var medicalDetails: Any? = null

    @SerializedName("emergency_name")
    @Expose
    var emergencyName: String? = null

    @SerializedName("emergency_phone")
    @Expose
    var emergencyPhone: String? = null

    @SerializedName("emergency_email")
    @Expose
    var emergencyEmail: String? = null

    @SerializedName("emergency_address_id")
    @Expose
    var emergencyAddressId: String? = null

    @SerializedName("emergency_relatioship")
    @Expose
    var emergencyRelatioship: String? = null

    @SerializedName("other_emergency_relationship")
    @Expose
    var otherEmergencyRelationship: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("rejection_msg")
    @Expose
    var rejectionMsg: String? = null

    @SerializedName("is_email_verified")
    @Expose
    var isEmailVerified: String? = null

    @SerializedName("email_verified_at")
    @Expose
    var emailVerifiedAt: String? = null

    @SerializedName("is_parent_approved")
    @Expose
    var isParentApproved: String? = null

    @SerializedName("parent_approved_at")
    @Expose
    var parentApprovedAt: String? = null

    @SerializedName("is_guardian_approved")
    @Expose
    var isGuardianApproved: String? = null

    @SerializedName("guardian_approved_at")
    @Expose
    var guardianApprovedAt: String? = null

    @SerializedName("is_admin_approved")
    @Expose
    var isAdminApproved: String? = null

    @SerializedName("admin_approved_at")
    @Expose
    var adminApprovedAt: String? = null

    @SerializedName("dbs_certificate_number")
    @Expose
    var dbsCertificateNumber: String? = null

    @SerializedName("dbs_certificate_date")
    @Expose
    var dbsCertificateDate: String? = null

    @SerializedName("dbs_certificate_file")
    @Expose
    var dbsCertificateFile: String? = null

    @SerializedName("safeguarding_certificate")
    @Expose
    var safeguardingCertificate: String? = null

    @SerializedName("safeguarding_training_complete")
    @Expose
    var safeguardingTrainingComplete: String? = null

    @SerializedName("safeguarding_training_completed_on")
    @Expose
    var safeguardingTrainingCompletedOn: String? = null

    @SerializedName("root_language")
    @Expose
    var rootLanguage: String? = null

    @SerializedName("indian_connection_state")
    @Expose
    var indianConnectionState: String? = null

    @SerializedName("email_verification_code")
    @Expose
    var emailVerificationCode: String? = null

    @SerializedName("user_id")
    @Expose
    var userId: String? = null

    @SerializedName("created_by")
    @Expose
    var createdBy: String? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("update_by")
    @Expose
    var updateBy: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null

    @SerializedName("parse_dob")
    @Expose
    var parseDob: String? = null

    @SerializedName("building_name")
    @Expose
    var buildingName: String? = null

    @SerializedName("address_line_1")
    @Expose
    var addressLine1: String? = null

    @SerializedName("address_line_2")
    @Expose
    var addressLine2: String? = null

    @SerializedName("city")
    @Expose
    var city: String? = null

    @SerializedName("county")
    @Expose
    var county: String? = null

    @SerializedName("postal_code")
    @Expose
    var postalCode: String? = null

    @SerializedName("country")
    @Expose
    var country: Any? = null

    @SerializedName("age")
    @Expose
    var age: String? = null

    @SerializedName("member_age")
    @Expose
    var memberAge: Int? = null

    @SerializedName("first_aid_date")
    @Expose
    var firstAidDate: String? = null

    @SerializedName("vibhag")
    @Expose
    var vibhag: String? = null

    @SerializedName("nagar")
    @Expose
    var nagar: String? = null

    @SerializedName("shakha")
    @Expose
    var shakha: String? = null

    @SerializedName("shakha_id")
    @Expose
    var shakhaId: String? = null

    @SerializedName("vibhag_id")
    @Expose
    var vibhagId: String? = null

    @SerializedName("nagar_id")
    @Expose
    var nagarId: String? = null

    @SerializedName("age_category")
    @Expose
    var ageCategory: String? = null

    @SerializedName("shakha_tab")
    @Expose
    var shakha_tab: String? = null

    @SerializedName("shakha_sankhya_avg")
    @Expose
    var shakha_sankhya_avg: String? = null

    @SerializedName("special_med_dietry_info_id")
    @Expose
    var special_med_dietry_info_id: String? = null

    @SerializedName("root_language_id")
    @Expose
    var root_language_id: String? = null

    @SerializedName("other_occupation")
    @Expose
    var other_occupation: String? = null

}