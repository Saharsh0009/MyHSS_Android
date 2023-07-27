package com.uk.myhss.Utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.preference.PreferenceManager
import android.view.View
import android.view.animation.TranslateAnimation
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.myhss.Utils.SharedPreferences_String_Name
import com.uk.myhss.Login_Registration.LoginActivity
import com.uk.myhss.R

@SuppressLint("CommitPrefEdits")
class SessionManager(private val _context: Context) {
    private val pref: SharedPreferences
    private val editor: SharedPreferences.Editor
    var PRIVATE_MODE = 0

    lateinit var firebaseAnalytics: FirebaseAnalytics

    private var prefs: SharedPreferences = _context.getSharedPreferences(
        _context.getString(R.string.app_name), Context.MODE_PRIVATE
    )

    @SuppressLint("ObsoleteSdkInt")
    fun logoutUser() {  //Boolean flag
//        if (flag) {
        editor.clear()
        editor.apply()
        val settings = PreferenceManager.getDefaultSharedPreferences(_context)
        settings.edit().clear().apply()
        //        }
        val i = Intent(_context, LoginActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        _context.startActivity(i)
        (_context as AppCompatActivity).finish()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            _context.finishAffinity()
        }
    }

    val isLoggedIn: Boolean
        get() = pref.getBoolean(IS_LOGIN, false)

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: SessionManager? = null
        private const val PREF_NAME = "UserLoginSession"
        private const val IS_LOGIN = "IsLoggedIn"
        private const val API_KEY = "API_KEY"
        const val DEVICE_TOKEN = "device_token"
        const val FCMDEVICE_TOKEN = "fcmdevice_token"
        const val USER_TOKEN = "user_token"
        const val PASSWORD = "user_password"
        const val FIRSTNAME = "frist_name"
        const val SURNAME = "sur_name"
        const val USERNAME = "user_name"
        const val USERID = "user_id"
        const val USEREMAIL = "user_email"
        const val USERROLE = "user_role"
        const val MEMBERID = "member_id"
        const val SECURITYKEY = "security_key"
        const val MIDDLENAME = "middle_name"
        const val SHAKHANAME = "shakha_name"
        const val SHAKHAID = "shakha_id"
        const val NAGARID = "nagar_id"
        const val VIBHAGID = "vibhag_id"
        const val ADDRESS = "address"
        const val LineOne = "lineone"
        const val RELATIONSHIPNAME = "relationship"
        const val RELATIONSHIPNAME_OTHER = "other_relationship"
        const val OCCUPATIONNAME = "occupation_name"
        const val SPOKKENLANGUAGE = "spoken_language"
        const val SPOKKENLANGUAGEID = "spoken_language_id"
        const val DOB = "dob"
        const val NAGARNAME = "nagar_name"
        const val VIBHAGNAME = "vibhag_name"
        const val MOBILENO = "mobile_no"
        const val SECMOBILENO = "sec_mobile_no"
        const val SECEMAIL = "secondary_email"
        const val GUAEMRNAME = "guaemr_name"
        const val GUAEMRPHONE = "guaemr_phone"
        const val GUAEMREMAIL = "guaemr_email"
        const val GUAEMRRELATIONSHIP = "guaemr_relation"
        const val GUAEMRRELATIONSHIP_OTHER = "guaemr_relation_other"
        const val DOHAVEMEDICAL = "do_have_medical"

        const val AGE = "age"
        const val GENDER = "gender"
        const val CITY = "city"
        const val COUNTRY = "country"
        const val POSTCODE = "post_code"
        const val SHAKHA_TAB = "shakha_tab"
        const val SHAKHA_SANKHYA_AVG = "shakha_sankhya_avg"
        const val SELECTED_ALL = "selected_all"
        const val SELECTED_TRUE = "selected_true"
        const val SELECTED_FALSE = "selected_false"

        //        const val MEDICAL_INFO = "medical_info"
        const val MEDICAL_OTHER_INFO = "medical_other_info"
        const val QUALIFICATION_INFO = "qualification_info"
        const val QUALIFICATIONAID = "qualification_aid"// first aid yes or no
        const val QUALIFICATION_VALUE = "qualification_value" // first aid type
        const val QUALIFICATION_DATE = "qualification_date" // first aid
        const val QUALIFICATION_FILE = "qualification_file" // first aid
        const val QUALIFICATION_PRO_BODY_REG_NUMBER = "qualification_pro_body_reg_number" // first aid
        const val QUALIFICATION_VALUE_NAME = "first_aid_qualification_name" // first aid name
        const val QUALIFICATION_IS_DOC = "first_aid_qualification_is_doc" // first aid is doc true

        const val DIETARY = "dietary_txt"
        const val DIETARYID = "dietary_id"
        const val STATE_IN_INDIA = "state_in_india"
        const val NOTIFICATION_READ = "1"
        const val SEARCHVALURE = "1"
        const val PROFILE_WORD = "profile_word"

        fun getSession(context: Context?): SessionManager? {
            if (instance == null) {
                instance = context?.let { SessionManager(it) }
            }
            return instance
        }
    }

    init {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = this.pref.edit()
    }

    fun isConnectingToInternet(mContext: Context): Boolean {
        val connectivity =
            mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivity != null) {
            val info = connectivity.allNetworkInfo
            if (info != null) for (i in info.indices) if (info[i].state == NetworkInfo.State.CONNECTED) {
                return true
            }
        }
        return false
    }

    // slide the view from below itself to the current position
    fun slideUp(view: View) {
        view.visibility = View.VISIBLE
        val animate = TranslateAnimation(
            0F,  // fromXDelta
            0F,  // toXDelta
            view.height.toFloat(),  // fromYDelta
            0F
        ) // toYDelta
        animate.duration = 500
        animate.fillAfter = true
        view.startAnimation(animate)
    }

    // slide the view from its current position to below itself
    fun slideDown(view: View) {
        view.visibility = View.GONE
        val animate = TranslateAnimation(
            0F,  // fromXDelta
            0F,  // toXDelta
            0F,  // fromYDelta
            view.height.toFloat()
        ) // toYDelta
        animate.duration = 500
        animate.fillAfter = true
        view.startAnimation(animate)
    }

    /*TOKEN*/
    fun saveDEVICE_TOKEN(token: String) {
        val editor = prefs.edit()
        editor.putString(DEVICE_TOKEN, token)
        editor.apply()
    }

    fun fetchDEVICE_TOKEN(): String? {
        return prefs.getString(DEVICE_TOKEN, null)
    }

    /*FCMDEVICE_TOKEN*/
    fun saveFCMDEVICE_TOKEN(token: String) {
        val editor = prefs.edit()
        editor.putString(FCMDEVICE_TOKEN, token)
        editor.apply()
    }

    fun fetchFCMDEVICE_TOKEN(): String? {
        return prefs.getString(FCMDEVICE_TOKEN, null)
    }

    /*TOKEN*/
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    /*PASSWORD*/
    fun savePASSWORD(token: String) {
        val editor = prefs.edit()
        editor.putString(PASSWORD, token)
        editor.apply()
    }

    fun fetchPASSWORD(): String? {
        return prefs.getString(PASSWORD, null)
    }

    @JvmName("isLoggedIn1")
    fun isLoggedIn(): Boolean {
        return pref.getBoolean(SessionManager.IS_LOGIN, false)
    }


    fun getDEVICE_FACEID(): String? {
        return SharedPreferences_String_Name.getShared(
            _context, SharedPreferences_String_Name.DEVICE_FACEID, ""
        )
    }

    fun setDEVICE_FACEID(DEVICE_FACEID: String?) {
        SharedPreferences_String_Name.setShared(
            _context, SharedPreferences_String_Name.DEVICE_FACEID, DEVICE_FACEID
        )
    }

    fun getDEVICE_FINGERID(): String? {
        return SharedPreferences_String_Name.getShared(
            _context, SharedPreferences_String_Name.DEVICE_FINGERID, ""
        )
    }

    fun setDEVICE_FINGERID(DEVICE_FINGERID: String?) {
        SharedPreferences_String_Name.setShared(
            _context, SharedPreferences_String_Name.DEVICE_FINGERID, DEVICE_FINGERID
        )
    }

    /*USERID*/
    fun saveUserID(userid: String) {
        val editor = prefs.edit()
        editor.putString(USERID, userid)
        editor.apply()
    }

    fun fetchUserID(): String? {
        return prefs.getString(USERID, null)
    }

    /*FIRSTNAME*/
    fun saveFIRSTNAME(firstname: String) {
        val editor = prefs.edit()
        editor.putString(FIRSTNAME, firstname)
        editor.apply()
    }

    fun fetchFIRSTNAME(): String? {
        return prefs.getString(FIRSTNAME, null)
    }

    /*SURNAME*/
    fun saveSURNAME(surname: String) {
        val editor = prefs.edit()
        editor.putString(SURNAME, surname)
        editor.apply()
    }

    fun fetchSURNAME(): String? {
        return prefs.getString(SURNAME, null)
    }

    /*USERNAME*/
    fun saveUSERNAME(username: String) {
        val editor = prefs.edit()
        editor.putString(USERNAME, username)
        editor.apply()
    }

    fun fetchUSERNAME(): String? {
        return prefs.getString(USERNAME, null)
    }

    /*USEREMAIL*/
    fun saveUSEREMAIL(email: String) {
        val editor = prefs.edit()
        editor.putString(USEREMAIL, email)
        editor.apply()
    }

    fun fetchUSEREMAIL(): String? {
        return prefs.getString(USEREMAIL, null)
    }

    /*USERROLE*/
    fun saveUSERROLE(role: String) {
        val editor = prefs.edit()
        editor.putString(USERROLE, role)
        editor.apply()
    }

    fun fetchUSERROLE(): String? {
        return prefs.getString(USERROLE, null)
    }

    /*MEMBERID*/
    fun saveMEMBERID(memberid: String) {
        val editor = prefs.edit()
        editor.putString(MEMBERID, memberid)
        editor.apply()
    }

    fun fetchMEMBERID(): String? {
        return prefs.getString(MEMBERID, null)
    }

    /*SECURITYKEY*/
    fun saveSECURITYKEY(skey: String) {
        val editor = prefs.edit()
        editor.putString(SECURITYKEY, skey)
        editor.apply()
    }

    fun fetchSECURITYKEY(): String? {
        return prefs.getString(SECURITYKEY, null)
    }


    /*For Profile*/

    /*Middle Name*/
    fun saveMIDDLENAME(skey: String) {
        val editor = prefs.edit()
        editor.putString(MIDDLENAME, skey)
        editor.apply()
    }

    fun fetchMIDDLENAME(): String? {
        return prefs.getString(MIDDLENAME, null)
    }

    /*DOB*/
    fun saveDOB(skey: String) {
        val editor = prefs.edit()
        editor.putString(DOB, skey)
        editor.apply()
    }

    fun fetchDOB(): String? {
        return prefs.getString(DOB, null)
    }

    /*Shakha ID*/
    fun saveSHAKHAID(skey: String) {
        val editor = prefs.edit()
        editor.putString(SHAKHAID, skey)
        editor.apply()
    }

    fun fetchSHAKHAID(): String? {
        return prefs.getString(SHAKHAID, null)
    }

    /*NAGARID ID*/
    fun saveNAGARID(skey: String) {
        val editor = prefs.edit()
        editor.putString(NAGARID, skey)
        editor.apply()
    }

    fun fetchNAGARID(): String? {
        return prefs.getString(NAGARID, null)
    }

    /*VIBHAGID ID*/
    fun saveVIBHAGID(skey: String) {
        val editor = prefs.edit()
        editor.putString(VIBHAGID, skey)
        editor.apply()
    }

    fun fetchVIBHAGID(): String? {
        return prefs.getString(VIBHAGID, null)
    }

    /*Shakha Name*/
    fun saveSHAKHANAME(skey: String) {
        val editor = prefs.edit()
        editor.putString(SHAKHANAME, skey)
        editor.apply()
    }

    fun fetchSHAKHANAME(): String? {
        return prefs.getString(SHAKHANAME, null)
    }

    /*Nagar Name*/
    fun saveNAGARNAME(skey: String) {
        val editor = prefs.edit()
        editor.putString(NAGARNAME, skey)
        editor.apply()
    }

    fun fetchNAGARNAME(): String? {
        return prefs.getString(NAGARNAME, null)
    }

    /*Vibhag Name*/
    fun saveVIBHAGNAME(skey: String) {
        val editor = prefs.edit()
        editor.putString(VIBHAGNAME, skey)
        editor.apply()
    }

    fun fetchVIBHAGNAME(): String? {
        return prefs.getString(VIBHAGNAME, null)
    }

    /*Address Name*/
    fun saveADDRESS(skey: String) {
        val editor = prefs.edit()
        editor.putString(ADDRESS, skey)
        editor.apply()
    }

    fun fetchADDRESS(): String? {
        return prefs.getString(ADDRESS, null)
    }

    /*Address LineOne*/
    fun saveLineOne(skey: String) {
        val editor = prefs.edit()
        editor.putString(LineOne, skey)
        editor.apply()
    }

    fun fetchLineOne(): String? {
        return prefs.getString(LineOne, null)
    }

    /*Address CITY*/
    fun saveCITY(skey: String) {
        val editor = prefs.edit()
        editor.putString(CITY, skey)
        editor.apply()
    }

    fun fetchCITY(): String? {
        return prefs.getString(CITY, null)
    }

    /*Address COUNTRY*/
    fun saveCOUNTRY(skey: String) {
        val editor = prefs.edit()
        editor.putString(COUNTRY, skey)
        editor.apply()
    }

    fun fetchCOUNTRY(): String? {
        return prefs.getString(COUNTRY, null)
    }

    /*Address POSTCODE*/
    fun savePOSTCODE(skey: String) {
        val editor = prefs.edit()
        editor.putString(POSTCODE, skey)
        editor.apply()
    }

    fun fetchPOSTCODE(): String? {
        return prefs.getString(POSTCODE, null)
    }

    /*RELATIONSHIPNAME Name*/
    fun saveRELATIONSHIPNAME(skey: String) {
        val editor = prefs.edit()
        editor.putString(RELATIONSHIPNAME, skey)
        editor.apply()
    }

    fun fetchRELATIONSHIPNAME(): String? {
        return prefs.getString(RELATIONSHIPNAME, null)
    }

    /*RELATIONSHIPNAME_OTHER Name*/
    fun saveRELATIONSHIPNAME_OTHER(skey: String) {
        val editor = prefs.edit()
        editor.putString(RELATIONSHIPNAME_OTHER, skey)
        editor.apply()
    }

    fun fetchRELATIONSHIPNAME_OTHER(): String? {
        return prefs.getString(RELATIONSHIPNAME_OTHER, null)
    }

    /*Occupation Name*/
    fun saveOCCUPATIONNAME(skey: String) {
        val editor = prefs.edit()
        editor.putString(OCCUPATIONNAME, skey)
        editor.apply()
    }

    fun fetchOCCUPATIONNAME(): String? {
        return prefs.getString(OCCUPATIONNAME, null)
    }

    /*Spoken language Name*/
    fun saveSPOKKENLANGUAGE(skey: String) {
        val editor = prefs.edit()
        editor.putString(SPOKKENLANGUAGE, skey)
        editor.apply()
    }

    fun fetchSPOKKENLANGUAGE(): String? {
        return prefs.getString(SPOKKENLANGUAGE, null)
    }

    /*Spoken language ID*/
    fun saveSPOKKENLANGUAGEID(skey: String) {
        val editor = prefs.edit()
        editor.putString(SPOKKENLANGUAGEID, skey)
        editor.apply()
    }

    fun fetchSPOKKENLANGUAGEID(): String? {
        return prefs.getString(SPOKKENLANGUAGEID, null)
    }

    /*Mobile Name*/
    fun saveMOBILENO(skey: String) {
        val editor = prefs.edit()
        editor.putString(MOBILENO, skey)
        editor.apply()
    }

    fun fetchMOBILENO(): String? {
        return prefs.getString(MOBILENO, null)
    }

    /*Secondary mobile Name*/
    fun saveSECMOBILENO(skey: String) {
        val editor = prefs.edit()
        editor.putString(SECMOBILENO, skey)
        editor.apply()
    }

    fun fetchSECMOBILENO(): String? {
        return prefs.getString(SECMOBILENO, null)
    }

    /*Secondary SECEMAIL*/
    fun saveSECEMAIL(skey: String) {
        val editor = prefs.edit()
        editor.putString(SECEMAIL, skey)
        editor.apply()
    }

    fun fetchSECEMAIL(): String? {
        return prefs.getString(SECEMAIL, null)
    }

    /*Guardian Name*/
    fun saveGUAEMRNAME(skey: String) {
        val editor = prefs.edit()
        editor.putString(GUAEMRNAME, skey)
        editor.apply()
    }

    fun fetchGUAEMRNAME(): String? {
        return prefs.getString(GUAEMRNAME, null)
    }

    /*Guardian phone Number*/
    fun saveGUAEMRPHONE(skey: String) {
        val editor = prefs.edit()
        editor.putString(GUAEMRPHONE, skey)
        editor.apply()
    }

    fun fetchGUAEMRPHONE(): String? {
        return prefs.getString(GUAEMRPHONE, null)
    }

    /*Guardian email*/
    fun saveGUAEMREMAIL(skey: String) {
        val editor = prefs.edit()
        editor.putString(GUAEMREMAIL, skey)
        editor.apply()
    }

    fun fetchGUAEMREMAIL(): String? {
        return prefs.getString(GUAEMREMAIL, null)
    }

    /*Guardian relation*/
    fun saveGUAEMRRELATIONSHIP(skey: String) {
        val editor = prefs.edit()
        editor.putString(GUAEMRRELATIONSHIP, skey)
        editor.apply()
    }

    fun fetchGUAEMRRELATIONSHIP(): String? {
        return prefs.getString(GUAEMRRELATIONSHIP, null)
    }

    /*Guardian GUAEMRRELATIONSHIP_OTHER*/
    fun saveGUAEMRRELATIONSHIP_OTHER(skey: String) {
        val editor = prefs.edit()
        editor.putString(GUAEMRRELATIONSHIP_OTHER, skey)
        editor.apply()
    }

    fun fetchGUAEMRRELATIONSHIP_OTHER(): String? {
        return prefs.getString(GUAEMRRELATIONSHIP_OTHER, null)
    }

    /*Do have medical Name*/
    fun saveDOHAVEMEDICAL(skey: String) {
        val editor = prefs.edit()
        editor.putString(DOHAVEMEDICAL, skey)
        editor.apply()
    }

    fun fetchDOHAVEMEDICAL(): String? {
        return prefs.getString(DOHAVEMEDICAL, null)
    }

    /*Qualification aid Name*/
    fun saveQUALIFICATIONAID(skey: String) {
        val editor = prefs.edit()
        editor.putString(QUALIFICATIONAID, skey)
        editor.apply()
    }

    fun fetchQUALIFICATIONAID(): String? {
        return prefs.getString(QUALIFICATIONAID, null)
    }

    /*Age Name*/
    fun saveAGE(skey: String) {
        val editor = prefs.edit()
        editor.putString(AGE, skey)
        editor.apply()
    }

    fun fetchAGE(): String? {
        return prefs.getString(AGE, null)
    }

    /*Gender Name*/
    fun saveGENDER(skey: String) {
        val editor = prefs.edit()
        editor.putString(GENDER, skey)
        editor.apply()
    }

    fun fetchGENDER(): String? {
        return prefs.getString(GENDER, null)
    }

    /*SHAKHA_TAB Name*/
    fun saveSHAKHA_TAB(skey: String) {
        val editor = prefs.edit()
        editor.putString(SHAKHA_TAB, skey)
        editor.apply()
    }

    fun fetchSHAKHA_TAB(): String? {
        return prefs.getString(SHAKHA_TAB, null)
    }

    /*SHAKHA_SANKHYA_AVG Name*/
    fun saveSHAKHA_SANKHYA_AVG(skey: String) {
        val editor = prefs.edit()
        editor.putString(SHAKHA_SANKHYA_AVG, skey)
        editor.apply()
    }

    fun fetchSHAKHA_SANKHYA_AVG(): String? {
        return prefs.getString(SHAKHA_SANKHYA_AVG, null)
    }

    /*SELECTED_ALL Name*/
    fun saveSELECTED_ALL(skey: String) {
        val editor = prefs.edit()
        editor.putString(SELECTED_ALL, skey)
        editor.apply()
    }

    fun fetchSELECTED_ALL(): String? {
        return prefs.getString(SELECTED_ALL, null)
    }

    /*SELECTED_TRUE Name*/
    fun saveSELECTED_TRUE(skey: String) {
        val editor = prefs.edit()
        editor.putString(SELECTED_TRUE, skey)
        editor.apply()
    }

    fun fetchSELECTED_TRUE(): String? {
        return prefs.getString(SELECTED_TRUE, null)
    }

    /*SELECTED_FALSE Name*/
    fun saveSELECTED_FALSE(skey: String) {
        val editor = prefs.edit()
        editor.putString(SELECTED_FALSE, skey)
        editor.apply()
    }

    fun fetchSELECTED_FALSE(): String? {
        return prefs.getString(SELECTED_FALSE, null)
    }

    /*MEDICAL_OTHER_INFO Name*/
    fun saveMEDICAL_OTHER_INFO(skey: String) {
        val editor = prefs.edit()
        editor.putString(MEDICAL_OTHER_INFO, skey)
        editor.apply()
    }

    fun fetchMEDICAL_OTHER_INFO(): String? {
        return prefs.getString(MEDICAL_OTHER_INFO, null)
    }

    /*QUALIFICATION_INFO Name*/
    fun saveQUALIFICATION_INFO(skey: String) {
        val editor = prefs.edit()
        editor.putString(QUALIFICATION_INFO, skey)
        editor.apply()
    }

    fun fetchQUALIFICATION_INFO(): String? {
        return prefs.getString(QUALIFICATION_INFO, null)
    }

    /*QUALIFICATION_VALUE Name*/
    fun saveQUALIFICATION_VALUE(skey: String) {
        val editor = prefs.edit()
        editor.putString(QUALIFICATION_VALUE, skey)
        editor.apply()
    }

    fun fetchQUALIFICATION_VALUE(): String? {
        return prefs.getString(QUALIFICATION_VALUE, null)
    }

    /*QUALIFICATION_PRO BOY TYPE Name*/
    fun saveQUALIFICATION_PRO_BODY_RED_NO(skey: String) {
        val editor = prefs.edit()
        editor.putString(QUALIFICATION_PRO_BODY_REG_NUMBER, skey)
        editor.apply()
    }

    fun fetchQUALIFICATION_PRO_BODY_RED_NO(): String? {
        return prefs.getString(QUALIFICATION_PRO_BODY_REG_NUMBER, null)
    }

    /*QUALIFICATION_value Name*/
    fun saveQUALIFICATION_VALUE_NAME(skey: String) {
        val editor = prefs.edit()
        editor.putString(QUALIFICATION_VALUE_NAME, skey)
        editor.apply()
    }

    fun fetchQUALIFICATION_VALUE_NAME(): String? {
        return prefs.getString(QUALIFICATION_VALUE_NAME, null)
    }

    /*QUALIFICATION_ is doc available*/
    fun saveQUALIFICATION_IS_DOC(skey: String) {
        val editor = prefs.edit()
        editor.putString(QUALIFICATION_IS_DOC, skey)
        editor.apply()
    }

    fun fetchQUALIFICATION_IS_DOC(): String? {
        return prefs.getString(QUALIFICATION_IS_DOC, null)
    }

    /*QUALIFICATION_DATE Name*/
    fun saveQUALIFICATION_DATE(skey: String) {
        val editor = prefs.edit()
        editor.putString(QUALIFICATION_DATE, skey)
        editor.apply()
    }

    fun fetchQUALIFICATION_DATE(): String? {
        return prefs.getString(QUALIFICATION_DATE, null)
    }

    /*QUALIFICATION_FILE Name*/
    fun saveQUALIFICATION_FILE(skey: String) {
        val editor = prefs.edit()
        editor.putString(QUALIFICATION_FILE, skey)
        editor.apply()
    }

    fun fetchQUALIFICATION_FILE(): String? {
        return prefs.getString(QUALIFICATION_FILE, null)
    }

    /*DIETARY Name*/
    fun saveDIETARY(skey: String) {
        val editor = prefs.edit()
        editor.putString(DIETARY, skey)
        editor.apply()
    }

    fun fetchDIETARY(): String? {
        return prefs.getString(DIETARY, null)
    }

    /*DIETARY ID*/
    fun saveDIETARYID(skey: String) {
        val editor = prefs.edit()
        editor.putString(DIETARYID, skey)
        editor.apply()
    }

    fun fetchDIETARYID(): String? {
        return prefs.getString(DIETARYID, null)
    }

    /*STATE_IN_INDIA Name*/
    fun saveSTATE_IN_INDIA(skey: String) {
        val editor = prefs.edit()
        editor.putString(STATE_IN_INDIA, skey)
        editor.apply()
    }

    fun fetchSTATE_IN_INDIA(): String? {
        return prefs.getString(STATE_IN_INDIA, null)
    }

    /*NOTIFICATION_READ */
    fun saveNOTIFICATION_READ(skey: String) {
        val editor = prefs.edit()
        editor.putString(NOTIFICATION_READ, skey)
        editor.apply()
    }

    fun fetchNOTIFICATION_READ(): String? {
        return prefs.getString(NOTIFICATION_READ, null)
    }

    /*SEARCHVALURE */
    fun saveSEARCHVALURE(skey: String) {
        val editor = prefs.edit()
        editor.putString(SEARCHVALURE, skey)
        editor.apply()
    }

    fun fetchSEARCHVALURE(): String? {
        return prefs.getString(SEARCHVALURE, null)
    }

    /*PROFILE_WORD */
    fun savePROFILE_WORD(skey: String) {
        val editor = prefs.edit()
        editor.putString(PROFILE_WORD, skey)
        editor.apply()
    }

    fun fetchPROFILE_WORD(): String? {
        return prefs.getString(PROFILE_WORD, null)
    }

}