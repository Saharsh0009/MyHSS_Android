package com.uk.myhss.Restful

import com.google.gson.JsonObject
import com.myhss.AddMember.FirstAidInfo.FirstAidInfo
import com.myhss.AllShakha.Model.Get_Shakha_Details_Response
import com.myhss.Guru_Dakshina_OneTime.Model.Get_Onetime.OneTimeSuccess
import com.myhss.Guru_Dakshina_OneTime.Model.StripeDataModel
import com.myhss.Splash.Model.Biometric.Biometric_response
import com.myhss.Splash.Model.Biometric.Latest_Update.latest_update_response
import com.myhss.ui.suryanamaskar.Model.Get_SuryaNamaskar_ModelResponse
import com.myhss.ui.suryanamaskar.Model.save_suryanamaskarResponse
import com.myhss.ui.ChangePassword.Model.ChangePasswordResponse
import com.myhss.ui.NotificationList.Model.NotificationDatum
import com.myhss.ui.SuchanaBoard.Model.Get_Suchana_Response
import com.myhss.ui.SuchanaBoard.Model.Get_Suchana_Seen_Response
import com.uk.myhss.AddMember.Address_Model.Find_Address_By_Pincode
import com.uk.myhss.AddMember.Dietaries_Model.Find_Address_dietaries
import com.uk.myhss.AddMember.GetNagar.Get_Nagar_Response
import com.uk.myhss.AddMember.Get_CreateMember.Get_CreateMembership_Response
import com.uk.myhss.AddMember.Get_Dietaries.Get_Dietaries_Response
import com.uk.myhss.AddMember.Get_Indianstates.Get_Indianstates_Response
import com.uk.myhss.AddMember.Get_Language.Get_Language_Response
import com.uk.myhss.AddMember.Get_Occupation.Get_Occupation_Response
import com.uk.myhss.AddMember.Get_Relationship.Get_Relationship_Response
import com.uk.myhss.AddMember.Get_Shakha.Get_Shakha_Response
import com.uk.myhss.AddMember.Get_Vibhag.Get_Vibhag_Response
import com.uk.myhss.AddMember.Pincode.Get_Pincode_Response
import com.uk.myhss.AddMember.PincodeAddress.Get_PincodeAddress_Response
import com.uk.myhss.Guru_Dakshina_OneTime.Model.Get_Regular.Get_Create_Regular
import com.uk.myhss.Login_Registration.Model.ForgotPasswordResponse
import com.uk.myhss.Login_Registration.Model.LoginResponse
import com.uk.myhss.Login_Registration.Model.RegistrationResponse
import com.uk.myhss.Main.Family_Member.Family_Member_Response
import com.uk.myhss.Main.Get_Privileges.Get_Privileges_Response
import com.uk.myhss.Main.Get_Prpfile.Get_Profile_Response
import com.uk.myhss.Welcome.WelcomeModel.WelcomeResponse
import com.uk.myhss.ui.guru_dakshina.Model.Get_Sankhya_Add_Response
import com.uk.myhss.ui.linked_family.Model.*
import com.uk.myhss.ui.my_family.Model.guru_dakshina_response
import com.uk.myhss.ui.my_family.Model.my_family_response
import com.uk.myhss.ui.sankhya_report.Model.Get_Sankhya_Utsav_Response
import com.uk.myhss.ui.sankhya_report.Model.Sankhya_List_Response
import com.uk.myhss.ui.sankhya_report.Model.Sankhya_details_Response
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {

    /*App Forcefully Update*/
    @FormUrlEncoded
    @POST("api/v1/User/latest_update")
    fun latestupdate(
        @Field("OSName") OSName: String,
        @Field("app_version") app_version: String,
        @Field("app_name") app_name: String
    ): Call<latest_update_response>

    /*biometric_key API*/
    @FormUrlEncoded
    @POST("api/v1/User/biometric_key")
    fun biometric_key(
//        @Header("Authorization") token: String,
        @Field("user_id") user_id: String,
        @Field("biometric_key") biometric_key: String,
        @Field("biometric_key_update") biometric_key_update: String
    ): Call<Biometric_response>

    @FormUrlEncoded
    @POST("api/v1/auth/login")
    fun userLogin(
        @Field("username") user: String,
        @Field("password") password: String,
        @Field("device_token") m_deviceId: String,
        @Field("device") device_type: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("api/v1/User/change_password")
    fun userChangePassword(
        @Field("user_id") user_id: String,
        @Field("old_password") old_password: String,
        @Field("new_password") new_password: String
    ): Call<ChangePasswordResponse>

    @FormUrlEncoded
    @POST("api/v1/auth/register")
    fun userRegistration(
        @Field("first_name") firstname: String,
        @Field("last_name") surname: String,
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("device_token") m_deviceId: String
    ): Call<RegistrationResponse>

    /*forgot password*/
    @FormUrlEncoded
    @POST("api/v1/auth/forgot_password")
    fun userForgot(
        @Field("username") forgotuser: String
    ): Call<ForgotPasswordResponse>

    /*Address find_address_by_pincode*/
    @POST("api/v1/chapter/find_address_by_pincode")
    fun userfind_add_by_pincode(
        @Field("pincode") pincode: String
    ): Call<Find_Address_By_Pincode>

    /*Address get_dietaries*/
    @GET("api/v1/member/get_dietaries")
    fun userfind_add_dietaries(): Call<Find_Address_dietaries>

    /*Welcome API*/
//    @Headers("Content-Type:application/json; charset=UTF-8")
    @FormUrlEncoded
    @POST("api/v1/member/welcome")
//    @Header(HeadersContract.HEADER_AUTHONRIZATION) String token,
    fun welcome_api(
        @Field("user_id") userid: String
//        @Field("Authorization Basic") token:String,
//        @Header(UUID.randomUUID().toString()) authToken: String?,
//        @Query("user_id") user_id: User_id,
//        @Header("Authorization") token: String,
//        @Body user_id: User_id
    ): Call<WelcomeResponse>

    /*My Family List*//*
    @Headers("Content-Type:application/json; charset=UTF-8")
    @POST("api/v1/karyakarta/get_members")
    fun get_members(
        @Field("user_id") userid: String,
        @Header("Authorization") String auth
    ):Call<my_family_response>*/

    //    @Headers("Content-Type: application/json;charset=UTF-8")
    @FormUrlEncoded
    @POST("api/v1/karyakarta/get_members")
    fun get_members(
        @Field("user_id") user_id: String
//        @Query("user_id") user_id: String,
//        @Header("Authorization") token: String,
//        @Body user_id: User_id
//        @Query("user_id") user_id: String,
//        @Header("Authorization") auth: String
    ): Call<my_family_response>

    /*Guru Dakshina*/
    @FormUrlEncoded
    @POST("api/v1/guru_dakshina/listing")
    fun get_guru_dakshina(
        @Field("user_id") user_id: String,
        @Field("length") length: String,
        @Field("start") start: String,
        @Field("search") search: String,
        @Field("chapter_id") chapter_id: String
    ): Call<guru_dakshina_response>

    /*Get Relationship*/
    @GET("api/v1/member/get_relationship")
    fun get_relationship(): Call<Get_Relationship_Response>

    /*Get Occupation*/
    @GET("api/v1/member/get_occupations")
    fun get_occupation(): Call<Get_Occupation_Response>

    /*Get Vibhag*/
    @GET("api/v1/member/get_vibhag")
    fun get_vibhag(): Call<Get_Vibhag_Response>

    /*Get Nagar*/
    @FormUrlEncoded
    @POST("api/v1/member/get_nagar_by_vibhag")
    fun get_nagar(
        @Field("vibhag_id") vibhag_id: String, @Field("user_id") user_id: String
    ): Call<Get_Nagar_Response>

    /*Get Shakha*/
    @FormUrlEncoded
    @POST("api/v1/member/get_shakha_by_nagar")
    fun get_shakha(
        @Field("nagar_id") nagar_id: String, @Field("user_id") user_id: String
    ): Call<Get_Shakha_Response>

    /*Get Pincode*/
    @FormUrlEncoded
    @POST("api/v1/member/find_address_by_pincode")
    fun get_pincode(
        @Field("pincode") pincode: String
    ): Call<Get_Pincode_Response>

    /*Get find_address_info_by_id*/
    @FormUrlEncoded
    @POST("api/v1/member/find_address_info_by_id")
    fun get_pincodea_ddress(
        @Field("id") pincode: String
    ): Call<Get_PincodeAddress_Response>

    /*Get Dietaries*/
    @GET("api/v1/member/get_dietaries")
    fun get_dietaries(): Call<Get_Dietaries_Response>

    /*Get Language*/
    @GET("api/v1/member/get_languages")
    fun get_languages(): Call<Get_Language_Response>

    /*Get Indianstates*/
    @GET("api/v1/member/get_indianstates")
    fun get_indianstates(): Call<Get_Indianstates_Response>

    /*Get create_membership*/
    @FormUrlEncoded
    @POST("api/v1/member/create_membership")
    fun get_create_membership(
        @Field("user_id") user_id: String,
        @Field("first_name") first_name: String,
        @Field("middle_name") middle_name: String,
        @Field("last_name") last_name: String,
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("gender") gender: String,
        @Field("dob") dob: String,
        @Field("age") age: String,
        @Field("relationship") relationship: String,
        @Field("other_relationship") other_relationship: String,
        @Field("occupation") occupation: String,
        @Field("occupation_name") occupation_name: String,
        @Field("shakha") shakha: String,
        @Field("mobile") mobile: String,
        @Field("land_line") land_line: String,
        @Field("secondary_email") secondary_email: String,
        @Field("post_code") post_code: String,
        @Field("building_name") building_name: String,
        @Field("address_line_1") address_line_1: String,
        @Field("address_line_2") address_line_2: String,
        @Field("post_town") post_town: String,
        @Field("county") county: String,
        @Field("emergency_name") emergency_name: String,
        @Field("emergency_phone") emergency_phone: String,
        @Field("emergency_email") emergency_email: String,
        @Field("emergency_relationship") emergency_relatioship: String,
        @Field("other_emergency_relationship") other_emergency_relationship: String,
        @Field("medical_information") medical_information: String,
        @Field("provide_details") provide_details: String,
        @Field("is_qualified_in_first_aid") is_qualified_in_first_aid: String,
        @Field("date_of_first_aid_qualification") date_of_first_aid_qualification: String,
        @Field("qualification_file") qualification_file: String,
        @Field("special_med_dietry_info") special_med_dietry_info: String,
        @Field("language") language: String,
        @Field("state") state: String,
        @Field("parent_member_id") parent_member_id: String,
        @Field("type") type: String,
        @Field("is_linked") is_linked: String,
        @Field("is_self") is_self: String,
        @Field("app") app: String
    ): Call<Get_CreateMembership_Response>


    /*Get create_self*/
    @FormUrlEncoded
    @POST("api/v1/member/create_membership")
    fun get_create_self(
        @Field("user_id") user_id: String,
        @Field("first_name") first_name: String,
        @Field("middle_name") middle_name: String,
        @Field("last_name") last_name: String,
        @Field("email") email: String,
        @Field("gender") gender: String,
        @Field("dob") dob: String,
        @Field("age") age: String,
        @Field("relationship") relationship: String,
        @Field("other_relationship") other_relationship: String,
        @Field("occupation") occupation: String,
        @Field("occupation_name") occupation_name: String,
        @Field("shakha") shakha: String,
        @Field("mobile") mobile: String,
        @Field("land_line") land_line: String,
        @Field("secondary_email") secondary_email: String,
        @Field("post_code") post_code: String,
        @Field("building_name") building_name: String,
        @Field("address_line_1") address_line_1: String,
        @Field("address_line_2") address_line_2: String,
        @Field("post_town") post_town: String,
        @Field("county") county: String,
        @Field("emergency_name") emergency_name: String,
        @Field("emergency_phone") emergency_phone: String,
        @Field("emergency_email") emergency_email: String,
        @Field("emergency_relationship") emergency_relatioship: String,
        @Field("other_emergency_relationship") other_emergency_relationship: String,
        @Field("medical_information") medical_information: String,
        @Field("provide_details") provide_details: String,
        @Field("is_qualified_in_first_aid") is_qualified_in_first_aid: String,
        @Field("date_of_first_aid_qualification") date_of_first_aid_qualification: String,
        @Field("qualification_file") qualification_file: String,
        @Field("special_med_dietry_info") special_med_dietry_info: String,
        @Field("language") language: String,
        @Field("state") state: String,
        @Field("parent_member_id") parent_member_id: String,
        @Field("type") type: String,
        @Field("is_linked") is_linked: String,
        @Field("is_self") is_self: String,
        @Field("app") app: String
    ): Call<Get_CreateMembership_Response>

    /*Get profile*/
    @FormUrlEncoded
    @POST("api/v1/member/profile")
    fun get_profile(
        @Field("user_id") user_id: String,
        @Field("member_id") member_id: String,
        @Field("device") device: String,
        @Field("device_token") device_token: String
    ): Call<Get_Profile_Response>

    /*Get privileges*/
    @FormUrlEncoded
    @POST("api/v1/member/privileges")
    fun get_privileges(
        @Field("user_id") user_id: String,
        @Field("menu_id") menu_id: String,
        @Field("action") action: String
    ): Call<Get_Privileges_Response>

//    /*Post create_onetime*/
//    @FormUrlEncoded
//    @POST("api/v1/guru_dakshina/create_onetime")
//    fun get_create_onetime(
//        @Field("user_id") user_id: String,
//        @Field("member_id") member_id: String,
//        @Field("amount") amount: String,
//        @Field("is_linked_member") is_linked_member: String,
//        @Field("gift_aid") gift_aid: String,
//        @Field("is_purnima_dakshina") is_purnima_dakshina: String,
//        @Field("line1") line1: String,
//        @Field("city") city: String,
//        @Field("country") country: String,
//        @Field("postal_code") postal_code: String,
//        @Field("dakshina") dakshina: String,
//        @Field("card_number") card_number: String,
//        @Field("name") name: String,
//        @Field("card_expiry") card_expiry: String,
//        @Field("card_cvv") card_cvv: String
//    ): Call<Get_Create_Onetime>

    /*Post create_regular*/
    @FormUrlEncoded
    @POST("api/v1/guru_dakshina/create_regular")
    fun get_create_regular(
        @Field("user_id") user_id: String,
        @Field("member_id") member_id: String,
        @Field("amount") amount: String,
        @Field("start_date") start_date: String,
        @Field("recurring") recurring: String,
        @Field("is_linked_member") is_linked_member: String,
        @Field("gift_aid") gift_aid: String,
//        @Field("is_purnima_dakshina") is_purnima_dakshina: String,
        @Field("line1") line1: String,
        @Field("city") city: String,
        @Field("country") country: String,
        @Field("postal_code") postal_code: String,
        @Field("dakshina") dakshina: String,
        @Field("app") app: String
    ): Call<Get_Create_Regular>

    /*Post family_members*/
    @FormUrlEncoded
    @POST("api/v1/guru_dakshina/family_members")
    fun get_family_members(
        @Field("user_id") user_id: String, @Field("member_id") member_id: String
    ): Call<Family_Member_Response>

    /*Post sankhya add*/
    @FormUrlEncoded
    @POST("api/v1/sankhya/add")
    fun get_sankhya_add(
        @Field("user_id") user_id: String,
        @Field("member_id") member_id: String,
        @Field("org_chapter_id") org_chapter_id: String,
        @Field("event_date") event_date: String,
        @Field("utsav") utsav: String,
        @Field("shishu_male") shishu_male: String,
        @Field("shishu_female") shishu_female: String,
        @Field("baal") baal: String,
        @Field("baalika") baalika: String,
        @Field("kishore") kishore: String,
        @Field("kishori") kishori: String,
        @Field("tarun") tarun: String,
        @Field("taruni") taruni: String,
        @Field("yuva") yuva: String,
        @Field("yuvati") yuvati: String,
        @Field("proudh") proudh: String,
        @Field("proudha") proudha: String,
        @Field("api") api: String
    ): Call<Get_Sankhya_Add_Response>

    /*Post sankhya add*/
    @FormUrlEncoded
    @POST("api/v1/sankhya/listing")
    fun get_sankhya_listing(
        @Field("user_id") user_id: String,
        @Field("chapter_id") chapter_id: String,
        @Field("length") length: String,
        @Field("start") start: String,
        @Field("search") search: String,
        @Field("start_date") start_date: String,
        @Field("end_date") end_date: String
    ): Call<Sankhya_List_Response>

    /*Post sankhya add*/
    @FormUrlEncoded
    @POST("api/v1/sankhya/get_record")
    fun get_sankhya_get_record(
        @Field("user_id") user_id: String, @Field("id") sankhya_id: String
    ): Call<Sankhya_details_Response>

    /*Post Single Member*/
    @FormUrlEncoded
    @POST("v1/member/record")
    fun get_single_member_record(
        @Field("user_id") user_id: String, @Field("member_id") member_id: String
    ): Call<Get_Single_Member_Record_Response>


    /*Post update_membership*/
    @FormUrlEncoded
    @POST("api/v1/member/record")
    fun get_update_membership(
        @Field("user_id") user_id: String,
        @Field("first_name") first_name: String,
        @Field("middle_name") middle_name: String,
        @Field("last_name") last_name: String,
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("gender") gender: String,
        @Field("dob") dob: String,
        @Field("age") age: String,
        @Field("relationship") relationship: String,
        @Field("other_relationship") other_relationship: String,
        @Field("occupation") occupation: String,
        @Field("occupation_name") occupation_name: String,
        @Field("shakha") shakha: String,
        @Field("mobile") mobile: String,
        @Field("land_line") land_line: String,
        @Field("post_code") post_code: String,
        @Field("building_name") building_name: String,
        @Field("address_line_1") address_line_1: String,
        @Field("address_line_2") address_line_2: String,
        @Field("post_town") post_town: String,
        @Field("county") county: String,
        @Field("emergency_name") emergency_name: String,
        @Field("emergency_phone") emergency_phone: String,
        @Field("emergency_email") emergency_email: String,
        @Field("emergency_relationship") emergency_relatioship: String,
        @Field("other_emergency_relationship") other_emergency_relationship: String,
        @Field("medical_information") medical_information: String,
        @Field("provide_details") provide_details: String,
        @Field("is_qualified_in_first_aid") is_qualified_in_first_aid: String,
        @Field("date_of_first_aid_qualification") date_of_first_aid_qualification: String,
        @Field("qualification_file") qualification_file: String,
        @Field("special_med_dietry_info") special_med_dietry_info: String,
        @Field("language") language: String,
        @Field("state") state: String,
        @Field("parent_member_id") parent_member_id: String,
        @Field("type") type: String,
        @Field("is_linked") is_linked: String,
        @Field("is_self") is_self: String,
        @Field("app") app: String
    ): Call<Get_Update_Membership_Response>

    /*Post member listing*/
    @FormUrlEncoded
    @POST("api/v1/member/listing")
    fun get_member_listing(
        @Field("user_id") user_id: String,
        @Field("tab") tab: String,
        @Field("member_id") member_id: String,
        @Field("status") status: String,
        @Field("length") length: String,
        @Field("start") start: String,
        @Field("search") search: String,
        @Field("chapter_id") chapter_id: String
    ): Call<Get_Member_Listing_Response>

    @FormUrlEncoded
    @POST("api/v1/member/memberlist")
    fun get_member_listing_shakha(
        @Field("user_id") user_id: String,
        @Field("tab") tab: String,
        @Field("member_id") member_id: String,
        @Field("status") status: String,
        @Field("length") length: String,
        @Field("start") start: String,
        @Field("search") search: String
    ): Call<Get_Member_Listing_Response>

    /*Post member delete*/
    @FormUrlEncoded
    @POST("api/v1/member/delete")
    fun get_member_delete(
        @Field("user_id") user_id: String, @Field("member_id") member_id: String
    ): Call<Get_Member_Delete_Response>

    /*Post member inactive*/
    @FormUrlEncoded
    @POST("api/v1/member/inactive")
    fun get_member_inactive(
        @Field("user_id") user_id: String, @Field("member_id") member_id: String
    ): Call<Get_Member_Inactive_Response>

    /*Post member active*/
    @FormUrlEncoded
    @POST("api/v1/member/active")
    fun get_member_active(
        @Field("user_id") user_id: String, @Field("member_id") member_id: String
    ): Call<Get_Member_Active_Response>

    /*Post member approve*/
    @FormUrlEncoded
    @POST("api/v1/member/approve")
    fun get_member_approve(
        @Field("user_id") user_id: String, @Field("member_id") member_id: String
    ): Call<Get_Member_Approve_Response>

    /*Post member reject*/
    @FormUrlEncoded
    @POST("api/v1/member/reject")
    fun get_member_reject(
        @Field("user_id") user_id: String, @Field("member_id") member_id: String
    ): Call<Get_Member_Reject_Response>

    /*Post member check_username_exist*/
    @FormUrlEncoded
    @POST("api/v1/member/check_username_exist")
    fun get_member_check_username_exist(
        @Field("user_id") user_id: String,
        @Field("username") username: String,
        @Field("id") id: String
    ): Call<Get_Member_Check_Username_Exist_Response>

    /*Post sankhya utsav*/
    @GET("api/v1/sankhya/utsav")
    fun get_sankhya_utsav(): Call<Get_Sankhya_Utsav_Response>

    /*Post Suchana*/
    @FormUrlEncoded
//    @GET("api/v1/Member/member_suchana")
    @POST("api/v1/Member/get_suchana_by_member")
    fun get_suchana_board(
        @Field("user_id") user_id: String,
        @Field("member_id") member_id: String,
//        @Field("start_date") start_date: String,
//        @Field("end_date") end_date: String
//        @Field("device_id") device_id: String
    ): Call<Get_Suchana_Response>

    /*Post shakha List*/
    @GET("api/v1/Member/get_shakha_list")
    fun get_shakha_info(): Call<Get_Shakha_Response>

    /*Post get_shakha_detail*/
    @FormUrlEncoded
    @POST("api/v1/Member/get_shakha_detail")
    fun get_shakha_details(
        @Field("shakha_id") shakha_id: String
    ): Call<Get_Shakha_Details_Response>

    /*Post get_shakha_detail*/
    @FormUrlEncoded
    @POST("api/v1/Member/seen_suchana_by_member")
    fun get_seen_suchana_by_member(
        @Field("suchana_id") suchana_id: String, @Field("member_id") member_id: String
    ): Call<Get_Suchana_Seen_Response>

    /*Post SuryaNamaskar Add*/
    @FormUrlEncoded
//    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8")
//    @Headers("Content-Type: application/json; charset=UTF-8")
    @POST("api/v1/suryanamaskar/save_suryanamaskar_count")
    fun save_suryanamasakar_count(
//        @Body body: String
        @Field("member_id") member_id: String,
//        @Body() surynamaskar: JsonObject
        @Field("surynamaskar") surynamaskar: String,
        @Field("date") date: String,
        @Field("count") count: String
//        @Field("surynamaskar") surynamaskar: ArrayList<String>
    ): Call<save_suryanamaskarResponse>

    //    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("api/v1/suryanamaskar/save_suryanamaskar_count")
    fun postsuryanamasakar_count(@Body logs: String): Call<save_suryanamaskarResponse>

//    @FormUrlEncoded
//    @POST("api/v1/suryanamaskar/save_suryanamaskar_count")
//    fun save_suryanamasakar_count(@Body save_suryanamaskarResponse: HashMap<String, String>): Call<save_suryanamaskarResponse>
//    fun save_suryanamasakar_count(@Body MultipartTypedOutput multipartTypedOutput)//: Call<save_suryanamaskarResponse>

    /*Post SuryaNamasakar List*/
    @FormUrlEncoded
    @POST("api/v1/suryanamaskar/get_suryanamaskar_count")
    fun get_suryanamaskar_count(
        @Field("member_id") member_id: String, @Field("is_api") is_api: String
    ): Call<Get_SuryaNamaskar_ModelResponse>

    //Nikunj
    @POST("api/v1/member/create_membership")
    fun postAddOrCreateMembership(@Body body: MultipartBody): Call<JsonObject>

    //Nikunj
    @POST("api/v1/member/update_membership")
    fun postUpdateMembership(@Body body: MultipartBody): Call<JsonObject>

    /*Get First Aid Info*/
    @GET("api/v1/member/firstaidinfo")
    fun getFirstAidInfor(): Call<FirstAidInfo>

    @POST("api/v1/guru_dakshina/onetime_dakshina")
    fun postOneTimeDakshinaStripe(@Body body: MultipartBody): Call<StripeDataModel>

    @POST("api/v1/guru_dakshina/payment_response")
    fun postSaveStripePaymentData(@Body body: MultipartBody): Call<OneTimeSuccess>

    @POST("api/v1/notification/notificationlist")
    fun postNotificationListData(@Body body: MultipartBody): Call<NotificationDatum>

    @POST("api/v1/notification/seen_notification")
    fun postSeenNotification(@Body body: MultipartBody): Call<Get_Suchana_Seen_Response>

}
