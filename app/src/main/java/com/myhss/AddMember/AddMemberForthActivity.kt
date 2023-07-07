package com.uk.myhss.AddMember

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentUris
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.Settings
import android.text.Spannable
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.google.gson.JsonObject
import com.myhss.AddMember.FirstAidInfo.DataFirstAidInfo
import com.myhss.AddMember.FirstAidInfo.FirstAidInfo
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.DebugLog
import com.myhss.Utils.Functions
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import com.uk.myhss.AddMember.Get_Dietaries.Datum_Get_Dietaries
import com.uk.myhss.AddMember.Get_Dietaries.Get_Dietaries_Response
import com.uk.myhss.AddMember.Get_Indianstates.Datum_Get_Indianstates
import com.uk.myhss.AddMember.Get_Indianstates.Get_Indianstates_Response
import com.uk.myhss.AddMember.Get_Language.Datum_Get_Language
import com.uk.myhss.AddMember.Get_Language.Get_Language_Response
import com.uk.myhss.Main.HomeActivity
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.Welcome.WelcomeActivity
import mabbas007.tagsedittext.TagsEditText
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.Buffer
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class AddMemberForthActivity : AppCompatActivity(), TagsEditText.TagsEditListener {
    private lateinit var sessionManager: SessionManager
    private var year = 0
    private var month = 0
    private var day = 0
    private lateinit var calendar: Calendar

    private var medical_info: String = ""
    private var qualified_info: String = ""

    var dietaryName: List<String> = ArrayList<String>()
    var dietaryID: List<String> = ArrayList<String>()
    var spokenName: List<String> = ArrayList<String>()
    var spokenID: List<String> = ArrayList<String>()
    var originName: List<String> = ArrayList<String>()
    var originID: List<String> = ArrayList<String>()
    var firstAidInfoName: List<String> = ArrayList<String>()
    var firstAidInfoID: List<String> = ArrayList<String>()

    private var DIETARY_ID: String = ""
    private var SPOKEN_ID: String = ""
    private var ORIGIN_ID: String = ""
    private var FIRSTAID_ID: String = ""

    private lateinit var edit_medical_information_details: TextInputEditText
    private lateinit var edit_date_of_first_aid_qualification: TextView
    private lateinit var edit_qualification_file: TextView

    private lateinit var edit_special_dietary_requirements: SearchableSpinner
    private lateinit var edit_spoken_language: SearchableSpinner
    private lateinit var edit_originating_state_in_india: SearchableSpinner

    private lateinit var originating_state_in_india_view: RelativeLayout
    private lateinit var spoken_language_view: RelativeLayout
    private lateinit var special_dietary_requirements_view: RelativeLayout

    private lateinit var medical_information_view: LinearLayout
    private lateinit var medical_information_no_view: LinearLayout
    private lateinit var qualification_First_view: LinearLayout
    private lateinit var qualification_First_no_view: LinearLayout

    private lateinit var medical_information_details_view: LinearLayout
    private lateinit var date_of_first_aid_qualification_view: LinearLayout
    private lateinit var qualification_file_view: LinearLayout

    private lateinit var back_layout: LinearLayout
    private lateinit var next_layout: LinearLayout

    private lateinit var dietary_txt_view: LinearLayout
    private lateinit var dietary_txt_layout: RelativeLayout
    private lateinit var dietary_txt: TextView
    private lateinit var cross_item: ImageView
    private lateinit var qualification_file: ImageView
    private lateinit var qualification_file_image: ImageView

    private lateinit var spoken_language_txt_view: LinearLayout
    private lateinit var spoken_language_txt_layout: RelativeLayout
    private lateinit var spoken_language_txt: TextView
    private lateinit var spoken_languagecross_item: ImageView
    private lateinit var checkbox: CheckBox
    private lateinit var agreement_txt: TextView

    private lateinit var check_box_layout: RelativeLayout

    private lateinit var relative_aid_type: RelativeLayout
    private lateinit var edit_profe_body_regis_num: TextInputEditText
    private lateinit var professionl_body_regi_view: LinearLayout
    private lateinit var edit_aid_type: SearchableSpinner
    private lateinit var first_aid_type_view: LinearLayout
    private lateinit var data_firstaidInfo: List<DataFirstAidInfo>

    private lateinit var mTagsEditText: TagsEditText

    //PDF OR Image upload
    val MEDIA_TYPE_IMAGE = 1
    val MEDIA_TYPE_PDF = 2
    var mediaFile: File? = null
    private var fileUri: Uri? = null
    private var File_name: String = ""
    var Check_value: String = ""
    private val IMAGE_DIRECTORY = "/demonuts_upload_gallery"

    //Pdf request code
    private val PICK_PDF_REQUEST = 3
    private var filePath: Uri? = null
    private var ImagefilePath: Uri? = null
    var bitmap: Bitmap? = null

    //image pick code
    private val IMAGE_PICK_CODE = 1000
    var Upload_file: String = ""
    var mediaFileDoc: File? = null
    private val PERMISSION_REQUEST_CODE = 200

    @SuppressLint("NewApi", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addmemberforuth)

        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("AddMemberStep4VC")
        sessionManager.firebaseAnalytics.setUserProperty(
            "AddMemberStep4VC", "AddMemberForthActivity"
        )
        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        val back_arrow = findViewById<ImageView>(R.id.back_arrow)
        val header_title = findViewById<TextView>(R.id.header_title)
        edit_medical_information_details = findViewById(R.id.edit_medical_information_details)
        edit_special_dietary_requirements = findViewById(R.id.edit_special_dietary_requirements)
        edit_spoken_language = findViewById(R.id.edit_spoken_language)
        edit_originating_state_in_india = findViewById(R.id.edit_originating_state_in_india)
        originating_state_in_india_view = findViewById(R.id.originating_state_in_india_view)
        spoken_language_view = findViewById(R.id.spoken_language_view)
        special_dietary_requirements_view = findViewById(R.id.special_dietary_requirements_view)
        medical_information_view = findViewById(R.id.medical_information_view)
        medical_information_no_view = findViewById(R.id.medical_information_no_view)
        qualification_First_view = findViewById(R.id.qualification_First_view)
        qualification_First_no_view = findViewById(R.id.qualification_First_no_view)
        medical_information_details_view = findViewById(R.id.medical_information_details_view)
        date_of_first_aid_qualification_view =
            findViewById(R.id.date_of_first_aid_qualification_view)
        qualification_file_view = findViewById(R.id.qualification_file_view)
        dietary_txt_layout = findViewById(R.id.dietary_txt_layout)
        dietary_txt_view = findViewById(R.id.dietary_txt_view)
        dietary_txt = findViewById(R.id.dietary_txt)
        cross_item = findViewById(R.id.cross_item)
        qualification_file = findViewById(R.id.qualification_file)
        qualification_file_image = findViewById(R.id.qualification_file_image)
        checkbox = findViewById(R.id.checkbox)
        agreement_txt = findViewById(R.id.agreement_txt)
        check_box_layout = findViewById(R.id.check_box_layout)
        spoken_language_txt_view = findViewById(R.id.spoken_language_txt_view)
        spoken_language_txt_layout = findViewById(R.id.spoken_language_txt_layout)
        spoken_language_txt = findViewById(R.id.spoken_language_txt)
        spoken_languagecross_item = findViewById(R.id.spoken_languagecross_item)
        back_layout = findViewById(R.id.back_layout)
        next_layout = findViewById(R.id.next_layout)
        edit_date_of_first_aid_qualification =
            findViewById(R.id.edit_date_of_first_aid_qualification)
        edit_qualification_file = findViewById(R.id.edit_qualification_file)
        // first aid
        first_aid_type_view = findViewById(R.id.first_air_type_view)
        edit_aid_type = findViewById(R.id.edit_aid_type)
        relative_aid_type = findViewById(R.id.relative_aid_type)
        professionl_body_regi_view = findViewById(R.id.professionl_body_regi_view)
        edit_profe_body_regis_num = findViewById<TextInputEditText>(R.id.edit_profe_body_regis_num)

        mTagsEditText = findViewById<View>(R.id.tagsEditText) as TagsEditText

        mTagsEditText.visibility = View.GONE
        mTagsEditText.hint = "Enter names of fruits"
        mTagsEditText.setTagsListener(this@AddMemberForthActivity)
        mTagsEditText.setTagsWithSpacesEnabled(true)
        mTagsEditText.setAdapter(
            ArrayAdapter(
                this, android.R.layout.simple_dropdown_item_1line, dietaryName
            )
        )
        mTagsEditText.threshold = 1

        val rootLayout = findViewById<RelativeLayout>(R.id.rootLayout)
        val edit_date_of_first_aid_qualification =
            findViewById<TextView>(R.id.edit_date_of_first_aid_qualification)
        val medical_information_yes_txt = findViewById<TextView>(R.id.medical_information_yes_txt)
        val medical_information_no_txt = findViewById<TextView>(R.id.medical_information_no_txt)
        val medical_information_yes_icon =
            findViewById<ImageView>(R.id.medical_information_yes_icon)
        val medical_information_no_img = findViewById<ImageView>(R.id.medical_information_no_img)
        val qualification_First_yes_txt = findViewById<TextView>(R.id.qualification_First_yes_txt)
        val qualification_First_no_txt = findViewById<TextView>(R.id.qualification_First_no_txt)
        val qualification_First_yes_icon =
            findViewById<ImageView>(R.id.qualification_First_yes_icon)
        val qualification_First_no_img = findViewById<ImageView>(R.id.qualification_First_no_img)

        checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            Check_value = isChecked.toString()
        }
        header_title.text = intent.getStringExtra("TITLENAME")


        if (Functions.isConnectingToInternet(this@AddMemberForthActivity)) {
            myDietaryRequirements()
            mySpokenLanguage()
            OriginatingState()
            firstAidInforApi()
        } else {
            Toast.makeText(
                this@AddMemberForthActivity,
                resources.getString(R.string.no_connection),
                Toast.LENGTH_SHORT
            ).show()
        }

        back_arrow.setOnClickListener {
            finish()
        }

        back_layout.setOnClickListener {
            finish()
        }

        qualification_file.setOnClickListener {
            if (!checkPermission()) {
                requestPermission()
            } else {
                openFileUploadDialog()
                Log.d("File_name", File_name)
            }
        }

        if (intent.getStringExtra("IS_SELF") != "self") { //  profile or add family
            if (intent.getStringExtra("TITLENAME") == "Profile") { //  only profile

                check_box_layout.visibility = View.GONE
                edit_medical_information_details.setText(sessionManager.fetchMEDICAL_OTHER_INFO())
                edit_date_of_first_aid_qualification.text = sessionManager.fetchQUALIFICATION_DATE()
                edit_qualification_file.text = sessionManager.fetchQUALIFICATION_FILE()
                dietary_txt.text = sessionManager.fetchDIETARY()
                spoken_language_txt.text = sessionManager.fetchSPOKKENLANGUAGE()
                edit_originating_state_in_india.setSelection(originName.indexOf(sessionManager.fetchSTATE_IN_INDIA()))
//                edit_aid_type.setSelection(firstAidInfoName.indexOf(sessionManager.fetchQUALIFICATION_VALUE())) // nik


                Functions.printLog("MEDICAL_OTHER_INFO", sessionManager.fetchMEDICAL_OTHER_INFO())
                Functions.printLog("QUALIFICATION_DATE", sessionManager.fetchQUALIFICATION_DATE())
                Functions.printLog("QUALIFICATION_FILE", sessionManager.fetchQUALIFICATION_FILE())
                Functions.printLog("STATE_IN_INDIA", sessionManager.fetchSTATE_IN_INDIA())

                if (sessionManager.fetchDOHAVEMEDICAL() == "1") {
                    medical_information_view.setBackgroundResource(R.drawable.edit_primery_color_round)
                    medical_information_no_view.setBackgroundResource(R.drawable.edittext_round)
                    medical_information_yes_txt.setTextColor(getColor(R.color.primaryColor))
                    medical_information_yes_icon.setImageResource(R.drawable.righttikmark)
//            medical_information_yes_icon.setColorFilter(ContextCompat.getColor(this, R.color.primaryColor), android.graphics.PorterDuff.Mode.MULTIPLY)
                    medical_information_no_txt.setTextColor(getColor(R.color.grayColorColor))
                    medical_information_no_img.setImageResource(R.drawable.righttikmark_gray_icon)
//            medical_information_no_img.setColorFilter(ContextCompat.getColor(this, R.color.grayColorColor), android.graphics.PorterDuff.Mode.MULTIPLY)
                    medical_information_details_view.visibility = View.VISIBLE
                    medical_info = "1"
                } else {
                    medical_information_no_view.setBackgroundResource(R.drawable.edit_primery_color_round)
                    medical_information_view.setBackgroundResource(R.drawable.edittext_round)
                    medical_information_yes_txt.setTextColor(getColor(R.color.grayColorColor))
                    medical_information_yes_icon.setImageResource(R.drawable.righttikmark_gray_icon)
//            medical_information_yes_icon.setColorFilter(ContextCompat.getColor(this, R.color.grayColorColor), android.graphics.PorterDuff.Mode.MULTIPLY)
                    medical_information_no_txt.setTextColor(getColor(R.color.primaryColor))
                    medical_information_no_img.setImageResource(R.drawable.righttikmark)
//            medical_information_no_img.setColorFilter(ContextCompat.getColor(this, R.color.primaryColor), android.graphics.PorterDuff.Mode.MULTIPLY)
                    medical_information_details_view.visibility = View.GONE
                    medical_info = "0"
                }

                if (sessionManager.fetchQUALIFICATIONAID() == "1") {
                    qualification_First_view.setBackgroundResource(R.drawable.edit_primery_color_round)
                    qualification_First_no_view.setBackgroundResource(R.drawable.edittext_round)
                    qualification_First_yes_txt.setTextColor(getColor(R.color.primaryColor))
                    qualification_First_yes_icon.setImageResource(R.drawable.righttikmark)
                    qualification_First_no_txt.setTextColor(getColor(R.color.grayColorColor))
                    qualification_First_no_img.setImageResource(R.drawable.righttikmark_gray_icon)
//            qualification_First_no_img.setColorFilter(ContextCompat.getColor(this, R.color.grayColorColor), android.graphics.PorterDuff.Mode.MULTIPLY)

                    first_aid_type_view.visibility = View.VISIBLE

                    date_of_first_aid_qualification_view.visibility = View.VISIBLE
                    qualification_file_view.visibility = View.VISIBLE

//                    setFirstAidInfo(sessionManager.fetchQUALIFICATION_VALUE().toString())

                    qualified_info = "1"
                } else {
                    qualification_First_no_view.setBackgroundResource(R.drawable.edit_primery_color_round)
                    qualification_First_view.setBackgroundResource(R.drawable.edittext_round)
                    qualification_First_yes_txt.setTextColor(getColor(R.color.grayColorColor))
                    qualification_First_yes_icon.setImageResource(R.drawable.righttikmark_gray_icon)
//            qualification_First_yes_icon.setColorFilter(ContextCompat.getColor(this, R.color.grayColorColor), android.graphics.PorterDuff.Mode.MULTIPLY)
                    qualification_First_no_txt.setTextColor(getColor(R.color.primaryColor))
                    qualification_First_no_img.setImageResource(R.drawable.righttikmark)

                    date_of_first_aid_qualification_view.visibility = View.GONE
                    qualification_file_view.visibility = View.GONE
                    first_aid_type_view.visibility = View.GONE
                    professionl_body_regi_view.visibility = View.GONE

                    qualified_info = "0"
                }
//                PASSWORD = sessionManager.fetchPASSWORD()!!
            }
//            PASSWORD = intent.getStringExtra("PASSWORD").toString()
        }

        val membership = " HSS (UK) Membership AgreementHSS."
        val span = Spannable.Factory.getInstance().newSpannable("I am agreeing to the $membership")
        val webClickSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(v: View) {
                val uriUrl: Uri =
                    Uri.parse(MyHssApplication.BaseURL + "page/hss-uk-membership-agreement/7")
                val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
                startActivity(launchBrowser)
            }
        }
        span.setSpan(webClickSpan, 21, 21 + membership.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        agreement_txt.text = span
        agreement_txt.movementMethod = LinkMovementMethod.getInstance()

        medical_information_view.setOnClickListener {
            medical_information_view.setBackgroundResource(R.drawable.edit_primery_color_round)
            medical_information_no_view.setBackgroundResource(R.drawable.edittext_round)

            medical_information_yes_txt.setTextColor(getColor(R.color.primaryColor))
            medical_information_yes_icon.setImageResource(R.drawable.righttikmark)
//            medical_information_yes_icon.setColorFilter(ContextCompat.getColor(this, R.color.primaryColor), android.graphics.PorterDuff.Mode.MULTIPLY)

            medical_information_no_txt.setTextColor(getColor(R.color.grayColorColor))
            medical_information_no_img.setImageResource(R.drawable.righttikmark_gray_icon)
//            medical_information_no_img.setColorFilter(ContextCompat.getColor(this, R.color.grayColorColor), android.graphics.PorterDuff.Mode.MULTIPLY)

            medical_information_details_view.visibility = View.VISIBLE

            medical_info = "1"
        }

        medical_information_no_view.setOnClickListener {
            medical_information_no_view.setBackgroundResource(R.drawable.edit_primery_color_round)
            medical_information_view.setBackgroundResource(R.drawable.edittext_round)

            medical_information_yes_txt.setTextColor(getColor(R.color.grayColorColor))
            medical_information_yes_icon.setImageResource(R.drawable.righttikmark_gray_icon)
//            medical_information_yes_icon.setColorFilter(ContextCompat.getColor(this, R.color.grayColorColor), android.graphics.PorterDuff.Mode.MULTIPLY)

            medical_information_no_txt.setTextColor(getColor(R.color.primaryColor))
            medical_information_no_img.setImageResource(R.drawable.righttikmark)
//            medical_information_no_img.setColorFilter(ContextCompat.getColor(this, R.color.primaryColor), android.graphics.PorterDuff.Mode.MULTIPLY)

            medical_information_details_view.visibility = View.GONE

            medical_info = "0"
        }

        qualification_First_view.setOnClickListener {
            qualification_First_view.setBackgroundResource(R.drawable.edit_primery_color_round)
            qualification_First_no_view.setBackgroundResource(R.drawable.edittext_round)

            qualification_First_yes_txt.setTextColor(getColor(R.color.primaryColor))
            qualification_First_yes_icon.setImageResource(R.drawable.righttikmark)

            qualification_First_no_txt.setTextColor(getColor(R.color.grayColorColor))
            qualification_First_no_img.setImageResource(R.drawable.righttikmark_gray_icon)
//            qualification_First_no_img.setColorFilter(ContextCompat.getColor(this, R.color.grayColorColor), android.graphics.PorterDuff.Mode.MULTIPLY)

            first_aid_type_view.visibility = View.VISIBLE
            //  if profile pass session id, if add self or family pass 1

            var visible_value = "1"
            if (intent.getStringExtra("TYPE_SELF") != "self") {
                if (intent.getStringExtra("FAMILY") == "PROFILE" && intent.getStringExtra("TITLENAME") == "Profile") {
                    visible_value = sessionManager.fetchQUALIFICATION_VALUE().toString()
                }
            }
            DebugLog.e("visibile value 1   " + visible_value)
            setFirstAidInfo(visible_value)


            edit_aid_type.setTitle("Select First Aid Qualification Type")
            edit_aid_type.setSelection(0)

//            }
//            date_of_first_aid_qualification_view.visibility = View.VISIBLE
//            qualification_file_view.visibility = View.VISIBLE
//            professionl_body_regi_view.visibility = View.VISIBLE
            qualified_info = "1"
        }

        qualification_First_no_view.setOnClickListener {
            qualification_First_no_view.setBackgroundResource(R.drawable.edit_primery_color_round)
            qualification_First_view.setBackgroundResource(R.drawable.edittext_round)

            qualification_First_yes_txt.setTextColor(getColor(R.color.grayColorColor))
            qualification_First_yes_icon.setImageResource(R.drawable.righttikmark_gray_icon)
//            qualification_First_yes_icon.setColorFilter(ContextCompat.getColor(this, R.color.grayColorColor), android.graphics.PorterDuff.Mode.MULTIPLY)

            qualification_First_no_txt.setTextColor(getColor(R.color.primaryColor))
            qualification_First_no_img.setImageResource(R.drawable.righttikmark)

            first_aid_type_view.visibility = View.GONE
            date_of_first_aid_qualification_view.visibility = View.GONE
            qualification_file_view.visibility = View.GONE
            professionl_body_regi_view.visibility = View.GONE

            qualified_info = "0"
        }

        Log.d(
            "Fetch_Data three-->",
            "  --FIRST_NAME : " + intent.getStringExtra("FIRST_NAME") + "  --MIDDLE_NAME : " + intent.getStringExtra(
                "MIDDLE_NAME"
            ) + "  --LAST_NAME : " + intent.getStringExtra("LAST_NAME") + "  --USERNAME : " + intent.getStringExtra(
                "USERNAME"
            ) + "  --EMAIL : " + intent.getStringExtra("EMAIL") + "  --PASSWORD : " + intent.getStringExtra(
                "PASSWORD"
            ) + "  --GENDER: " + intent.getStringExtra("GENDER") + "  --DOB: " + intent.getStringExtra(
                "DOB"
            ) + "  --RELATIONSHIP: " + intent.getStringExtra("RELATIONSHIP") + "  --OTHER_RELATIONSHIP: " + intent.getStringExtra(
                "OTHER_RELATIONSHIP"
            ) + "  --OCCUPATION: " + intent.getStringExtra("OCCUPATION") + "  --OCCUPATION_NAME: " + intent.getStringExtra(
                "OCCUPATION_NAME"
            ) + "  --SHAKHA: " + intent.getStringExtra("SHAKHA") + "  --AGE: " + intent.getStringExtra(
                "AGE"
            ) + "  --IS_LINKED: " + intent.getStringExtra("IS_LINKED") + "  --IS_SELF: " + intent.getStringExtra(
                "IS_SELF"
            ) + "  --TYPE: " + intent.getStringExtra("TYPE") + "  --PARENT_MEMBER: " + intent.getStringExtra(
                "PARENT_MEMBER"
            ) + "  --MOBILE: " + intent.getStringExtra("MOBILE") + "  --SECOND_EMAIL: " + intent.getStringExtra(
                "SECOND_EMAIL"
            ) + "  --LAND_LINE: " + intent.getStringExtra("LAND_LINE") + "  --POST_CODE: " + intent.getStringExtra(
                "POST_CODE"
            ) + "  --BUILDING_NAME: " + intent.getStringExtra("BUILDING_NAME") + "  --ADDRESS_ONE: " + intent.getStringExtra(
                "ADDRESS_ONE"
            ) + "  --ADDRESS_TWO: " + intent.getStringExtra("ADDRESS_TWO") + "  --POST_TOWN: " + intent.getStringExtra(
                "POST_TOWN"
            ) + "  --COUNTY: " + intent.getStringExtra("COUNTY") + "  --EMERGENCY_NAME: " + intent.getStringExtra(
                "EMERGENCY_NAME"
            ) + "  --EMERGENCY_PHONE: " + intent.getStringExtra("EMERGENCY_PHONE") + "  --EMERGENCY_EMAIL: " + intent.getStringExtra(
                "EMERGENCY_EMAIL"
            ) + "  --EMERGENCY_RELATIONSHIP: " + intent.getStringExtra("EMERGENCY_RELATIONSHIP") + "  --OTHER_EMERGENCY_RELATIONSHIP: " + intent.getStringExtra(
                "OTHER_EMERGENCY_RELATIONSHIP"
            )
        )

        next_layout.setOnClickListener {
            if (filePath != null) {
//            val path: String
//                Upload_file = getpath(filePath.toString()).toString()
//                FilePath.getPath(this, filePath)
//                Upload_file = FilePath.getPath(this@AddMemberForthActivity, filePath)
                Functions.printLog("Upload_file==", Upload_file)
            } else if (ImagefilePath != null) {
//                    Upload_file = ImagefilePath.toString()
                Functions.printLog("Upload_file==", Upload_file)
            }
//            edit_qualification_file.text = Upload_file

            /*Add Member First*/
            val first_name = intent.getStringExtra("FIRST_NAME")
            val middle_name = intent.getStringExtra("MIDDLE_NAME")
            val last_name = intent.getStringExtra("LAST_NAME")
            val username = intent.getStringExtra("USERNAME")
            val email = intent.getStringExtra("EMAIL")
            val password = intent.getStringExtra("PASSWORD")
            val gender = intent.getStringExtra("GENDER")
            val dob = intent.getStringExtra("DOB")
            val age = intent.getStringExtra("AGE")
            val relationship = intent.getStringExtra("RELATIONSHIP")
            val other_relationship = intent.getStringExtra("OTHER_RELATIONSHIP")
            val occupation = intent.getStringExtra("OCCUPATION")
            val occupation_name = intent.getStringExtra("OCCUPATION_NAME")
            val shakha = intent.getStringExtra("SHAKHA")

            /*Add Member Second*/
            val mobile = intent.getStringExtra("MOBILE")
            val land_line = intent.getStringExtra("LAND_LINE")
            val second_email = intent.getStringExtra("SECOND_EMAIL")
            val post_code = intent.getStringExtra("POST_CODE")
            val building_name = intent.getStringExtra("BUILDING_NAME")
            val address_line_1 = intent.getStringExtra("ADDRESS_ONE")
            val address_line_2 = intent.getStringExtra("ADDRESS_TWO")
            val post_town = intent.getStringExtra("POST_TOWN")
            val county = intent.getStringExtra("COUNTY")

            /*Add Member Third*/
            val emergency_name = intent.getStringExtra("EMERGENCY_NAME")
            val emergency_phone = intent.getStringExtra("EMERGENCY_PHONE")
            val emergency_email = intent.getStringExtra("EMERGENCY_EMAIL")
            val emergency_relatioship = intent.getStringExtra("EMERGENCY_RELATIONSHIP")
            val other_emergency_relationship = intent.getStringExtra("OTHER_EMERGENCY_RELATIONSHIP")

            /*Add Member Fourth*/
            val medical_information = medical_info
            val provide_details = edit_medical_information_details.text.toString()
            val is_qualified_in_first_aid = qualified_info
            val date_of_first_aid_qualification =
                edit_date_of_first_aid_qualification.text.toString()
            val qualification_file = Upload_file  //edit_qualification_file.text.toString()
            val special_med_dietry_info = DIETARY_ID
            val language = SPOKEN_ID
            val state = ORIGIN_ID
            val firstAid: String = FIRSTAID_ID

            /*Default*/
            val parent_member_id = intent.getStringExtra("PARENT_MEMBER")
            val type = intent.getStringExtra("TYPE")
            val is_linked = intent.getStringExtra("IS_LINKED")
            val is_self = intent.getStringExtra("IS_SELF")
            val app = "yes"

            if (medical_info == "") {
                Snackbar.make(
                    rootLayout, "Please select medical information", Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            } else if (qualified_info == "") {
                Snackbar.make(
                    rootLayout, "Please select qualification information", Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
//            } else if (Check_value != "true") {
//                Snackbar.make(
//                    rootLayout,
//                    "Please check Membership AgreementHSS",
//                    Snackbar.LENGTH_SHORT
//                )
//                    .show()
//                return@setOnClickListener
            } else {
                DebugLog.e("qualified_info : " + qualified_info)
                if (qualified_info == "0") {// docs upload = no
                    if (intent.getStringExtra("IS_SELF") != "self") { // Profile or add family
                        if (Functions.isConnectingToInternet(this@AddMemberForthActivity)) {
                            if (intent.getStringExtra("FAMILY") == "PROFILE") {
                                //call Edit profile API here
                                callEditProfileApi(false)
                                DebugLog.e("Profile : Edit proile without Doc upload")
                            } else {
                                callMembershipApi("family", false)
                                DebugLog.e("Add Family : without doc upload")
                            }
                        } else {
                            Toast.makeText(
                                this@AddMemberForthActivity,
                                resources.getString(R.string.no_connection),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else { // Add Self
                        if (Check_value != "true") {
                            Snackbar.make(
                                rootLayout,
                                "Please check Membership AgreementHSS",
                                Snackbar.LENGTH_SHORT
                            ).show()
                            return@setOnClickListener
                        } else if (Functions.isConnectingToInternet(this@AddMemberForthActivity)) {
                            callMembershipApi("self", false)
                            DebugLog.e("Add Self =>  Without Image")
                        } else {
                            Toast.makeText(
                                this@AddMemberForthActivity,
                                resources.getString(R.string.no_connection),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                } else { // docs upload = yes
                    if (intent.getStringExtra("IS_SELF") != "self") {// Edit Profile or Add family
                        if (Functions.isConnectingToInternet(this@AddMemberForthActivity)) {
                            if (intent.getStringExtra("FAMILY") == "PROFILE") {
                                callEditProfileApi(true)
                                DebugLog.e("Profile : Edit profile with doc file")
                            } else {
                                callMembershipApi("family", true)
                                DebugLog.e("Add Family : with doc file")
                            }

                        } else {
                            Toast.makeText(
                                this@AddMemberForthActivity,
                                resources.getString(R.string.no_connection),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else { // Add self
                        if (Check_value != "true") {
                            Snackbar.make(
                                rootLayout,
                                "Please check Membership AgreementHSS",
                                Snackbar.LENGTH_SHORT
                            ).show()
                            return@setOnClickListener
                        } else if (Functions.isConnectingToInternet(this@AddMemberForthActivity)) {
                            callMembershipApi("self", true)
                            DebugLog.e("Add Self :  with doc file")
                        } else {
                            Toast.makeText(
                                this@AddMemberForthActivity,
                                resources.getString(R.string.no_connection),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        edit_date_of_first_aid_qualification.setOnClickListener {
            calendar = Calendar.getInstance()
//            calendar.add(Calendar.YEAR, -3)
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH)
            day = calendar.get(Calendar.DAY_OF_MONTH)

            val dialog = DatePickerDialog(this, { _, year, month, day_of_month ->
                calendar[Calendar.YEAR] = year
                calendar[Calendar.MONTH] = month + 1
                calendar[Calendar.DAY_OF_MONTH] = day_of_month
                val myFormat = "dd/MM/yyyy"
                val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
                edit_date_of_first_aid_qualification.text = sdf.format(calendar.time)
            }, year, month, day)
//            dialog.datePicker.minDate = calendar.timeInMillis
//            calendar.add(Calendar.YEAR, 0)
            dialog.datePicker.maxDate = calendar.timeInMillis
            dialog.show()
        }

        edit_special_dietary_requirements.onItemSelectedListener = mOnItemSelectedListener_dietary
        edit_spoken_language.onItemSelectedListener = mOnItemSelectedListener_spoken
        edit_originating_state_in_india.onItemSelectedListener = mOnItemSelectedListener_origin
        edit_aid_type.onItemSelectedListener = mOnItemSelectedListener_firstAid

        edit_special_dietary_requirements.setTitle("Select Special Dietary")
        edit_spoken_language.setTitle("Select Spoken Language")
        edit_originating_state_in_india.setTitle("Select Originationg State In India")
        edit_aid_type.setTitle("Select First Aid Qualification Type")

        originating_state_in_india_view.setOnClickListener {
//            SearchSpinner(dietaryName.toTypedArray(), edit_special_dietary_requirements)
            SearchSpinner(originName.toTypedArray(), edit_originating_state_in_india)
        }

        spoken_language_view.setOnClickListener {
            SearchSpinner(spokenName.toTypedArray(), edit_spoken_language)
        }

        special_dietary_requirements_view.setOnClickListener {
//            SearchSpinner(originName.toTypedArray(), edit_originating_state_in_india)
            SearchSpinner(dietaryName.toTypedArray(), edit_special_dietary_requirements)
        }

        first_aid_type_view.setOnClickListener {
            SearchSpinner(firstAidInfoName.toTypedArray(), edit_aid_type)
        }

    }

    private fun SearchSpinner(
        spinner_search: Array<String>, edit_txt: SearchableSpinner
    ) {
        val searchmethod = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, spinner_search
        )
        searchmethod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        edit_txt.adapter = searchmethod

        if (intent.getStringExtra("TYPE_SELF") != "self") {
            if (intent.getStringExtra("FAMILY") == "PROFILE" && intent.getStringExtra("TITLENAME") == "Profile") {
                edit_special_dietary_requirements.setSelection(dietaryName.indexOf(sessionManager.fetchDIETARY()))
                edit_spoken_language.setSelection(spokenName.indexOf(sessionManager.fetchSPOKKENLANGUAGE()))
                edit_originating_state_in_india.setSelection(originName.indexOf(sessionManager.fetchSTATE_IN_INDIA()))
            }
        }
    }

    private val mOnItemSelectedListener_dietary: AdapterView.OnItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
//            TODO("Not yet implemented")
                Log.d("Name", dietaryName[position])
                Log.d("Postion", dietaryID[position])
                DIETARY_ID = dietaryID[position]

                val N = dietaryID.size

                dietary_txt_layout.visibility = View.VISIBLE

                dietary_txt.text = dietaryName[position]

                for (i in 0..N) {

                    dietary_txt_view.removeView(dietary_txt)
                    dietary_txt_view.removeView(cross_item)
                    dietary_txt_view.addView(dietary_txt)
                    dietary_txt_view.addView(cross_item)

                    cross_item.setOnClickListener {
                        dietary_txt_view.removeView(dietary_txt)
                    }

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
//            TODO("Not yet implemented")
                dietary_txt_layout.visibility = View.GONE
            }
        }

    private val mOnItemSelectedListener_spoken: AdapterView.OnItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
//            TODO("Not yet implemented")
                Log.d("Name", spokenName[position])
                Log.d("Postion", spokenID[position])
                SPOKEN_ID = spokenID[position]

                spoken_language_txt_layout.visibility = View.VISIBLE

                spoken_language_txt.text = spokenName[position]

                spoken_language_txt_view.removeView(spoken_language_txt)
                spoken_language_txt_view.removeView(spoken_languagecross_item)
                spoken_language_txt_view.addView(spoken_language_txt)
                spoken_language_txt_view.addView(spoken_languagecross_item)

                spoken_languagecross_item.setOnClickListener {
                    spoken_language_txt_view.removeView(spoken_language_txt)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
//            TODO("Not yet implemented")
                spoken_language_txt_layout.visibility = View.GONE
            }
        }

    private val mOnItemSelectedListener_origin: AdapterView.OnItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
//            TODO("Not yet implemented")
                Log.d("Name", originName[position])
                Log.d("Postion", originID[position])
                ORIGIN_ID = originID[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
//            TODO("Not yet implemented")
            }
        }


    private val mOnItemSelectedListener_firstAid: AdapterView.OnItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
//            TODO("Not yet implemented")
                DebugLog.e("Name : " + firstAidInfoName[position])
                DebugLog.e("Postion : " + firstAidInfoID[position])
                FIRSTAID_ID = firstAidInfoID[position]
                setFirstAidInfo(FIRSTAID_ID)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
//            TODO("Not yet implemented")
            }
        }

    private fun setFirstAidInfo(firstaidId: String) {

        for (n in data_firstaidInfo.indices) {
            if (data_firstaidInfo.get(n).id == firstaidId) {
                when (data_firstaidInfo.get(n).is_doc) {
                    "1" -> {
                        date_of_first_aid_qualification_view.visibility = View.VISIBLE
                        qualification_file_view.visibility = View.VISIBLE
                        professionl_body_regi_view.visibility = View.GONE
                        edit_profe_body_regis_num.setText("")

                    }

                    "0" -> {
                        date_of_first_aid_qualification_view.visibility = View.GONE
                        qualification_file_view.visibility = View.GONE
                        professionl_body_regi_view.visibility = View.VISIBLE
                        edit_profe_body_regis_num.setText(
                            sessionManager.fetchQUALIFICATION_PRO_BODY_RED_NO().toString()
                        )
                    }
                }
                edit_aid_type.setSelection(n) // nik
                break
            }
        }
    }


    /*Relationship API*/
    private fun myDietaryRequirements() {
        val pd = CustomProgressBar(this@AddMemberForthActivity)
        pd.show()
        val call: Call<Get_Dietaries_Response> = MyHssApplication.instance!!.api.get_dietaries()
        call.enqueue(object : Callback<Get_Dietaries_Response> {
            override fun onResponse(
                call: Call<Get_Dietaries_Response>, response: Response<Get_Dietaries_Response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {

                        var data_relationship: List<Datum_Get_Dietaries> =
                            ArrayList<Datum_Get_Dietaries>()
                        data_relationship = response.body()!!.data!!
                        Log.d("atheletsBeans", data_relationship.toString())
                        for (i in 1 until data_relationship.size) {
                            Log.d(
                                "relationshipName",
                                data_relationship[i].dietaryRequirementsName.toString()
                            )
                        }
                        dietaryName = listOf(arrayOf(data_relationship).toString())
                        dietaryID = listOf(arrayOf(data_relationship).toString())

                        val mStringList = ArrayList<String>()
                        for (i in 0 until data_relationship.size) {
                            mStringList.add(
//                            data_relationship[i].memberRelationshipId.toString() +
                                data_relationship[i].dietaryRequirementsName.toString()
                            )
                        }

                        val mStringListnew = ArrayList<String>()
                        for (i in 0 until data_relationship.size) {
                            mStringListnew.add(
                                data_relationship[i].dietaryRequirementsId.toString()
                            )
                        }

                        var mStringArray = mStringList.toArray()
                        var mStringArraynew = mStringListnew.toArray()

                        for (i in mStringArray.indices) {
                            Log.d("string is", mStringArray[i] as String)
                        }

                        for (i in mStringArraynew.indices) {
                            Log.d("mStringArraynew is", mStringArraynew[i] as String)
                        }

                        mStringArray = mStringList.toArray(mStringArray)
                        mStringArraynew = mStringListnew.toArray(mStringArraynew)

                        val list: ArrayList<String> = arrayListOf<String>()
                        val listnew: ArrayList<String> = arrayListOf<String>()

                        for (element in mStringArray) {
                            Log.d("LIST==>", element.toString())
                            list.add(element.toString())
                            Log.d("list==>", list.toString())

                            val listn = arrayOf(element)
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                dietaryName =
                                    list//listOf(listn.toCollection(ArrayList()).toString())
                            }
                        }

                        for (element in mStringArraynew) {
                            Log.d("LIST==>", element.toString())
                            listnew.add(element.toString())
                            Log.d("list==>", listnew.toString())

                            val listn = arrayOf(element)
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                dietaryID =
                                    listnew//listOf(listn.toCollection(ArrayList()).toString())
                            }
                        }

                        Log.d("dietaryName==>", dietaryName.toString())
                        SearchSpinner(dietaryName.toTypedArray(), edit_special_dietary_requirements)

                    } else {
                        Functions.displayMessage(
                            this@AddMemberForthActivity, response.body()?.message
                        )
//                        Functions.showAlertMessageWithOK(
//                            this@AddMemberForthActivity, "",
////                        "Message",
//                            response.body()?.message
//                        )
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@AddMemberForthActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Get_Dietaries_Response>, t: Throwable) {
                Toast.makeText(this@AddMemberForthActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    /*Relationship API*/
    private fun mySpokenLanguage() {
        val pd = CustomProgressBar(this@AddMemberForthActivity)
        pd.show()
        val call: Call<Get_Language_Response> = MyHssApplication.instance!!.api.get_languages()
        call.enqueue(object : Callback<Get_Language_Response> {
            override fun onResponse(
                call: Call<Get_Language_Response>, response: Response<Get_Language_Response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {

                        var data_relationship: List<Datum_Get_Language> =
                            ArrayList<Datum_Get_Language>()
                        data_relationship = response.body()!!.data!!
                        Log.d("atheletsBeans", data_relationship.toString())
                        for (i in 1 until data_relationship.size) {
                            Log.d("relationshipName", data_relationship[i].languageName.toString())
                        }
                        spokenName = listOf(arrayOf(data_relationship).toString())
                        spokenID = listOf(arrayOf(data_relationship).toString())

                        val mStringList = ArrayList<String>()
                        for (i in 0 until data_relationship.size) {
                            mStringList.add(
//                            data_relationship[i].memberRelationshipId.toString() +
                                data_relationship[i].languageName.toString()
                            )
                        }

                        val mStringListnew = ArrayList<String>()
                        for (i in 0 until data_relationship.size) {
                            mStringListnew.add(
                                data_relationship[i].languageId.toString()
                            )
                        }

                        var mStringArray = mStringList.toArray()
                        var mStringArraynew = mStringListnew.toArray()

                        for (i in mStringArray.indices) {
                            Log.d("string is", mStringArray[i] as String)
                        }

                        for (i in mStringArraynew.indices) {
                            Log.d("mStringArraynew is", mStringArraynew[i] as String)
                        }

                        mStringArray = mStringList.toArray(mStringArray)
                        mStringArraynew = mStringListnew.toArray(mStringArraynew)

                        val list: ArrayList<String> = arrayListOf<String>()
                        val listnew: ArrayList<String> = arrayListOf<String>()

                        for (element in mStringArray) {
                            Log.d("LIST==>", element.toString())
                            list.add(element.toString())
                            Log.d("list==>", list.toString())

                            val listn = arrayOf(element)
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                spokenName =
                                    list//listOf(listn.toCollection(ArrayList()).toString())
                            }
                        }

                        for (element in mStringArraynew) {
                            Log.d("LIST==>", element.toString())
                            listnew.add(element.toString())
                            Log.d("list==>", listnew.toString())

                            val listn = arrayOf(element)
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                spokenID =
                                    listnew//listOf(listn.toCollection(ArrayList()).toString())
                            }
                        }

                        Log.d("spokenName==>", spokenName.toString())
                        SearchSpinner(spokenName.toTypedArray(), edit_spoken_language)

                    } else {
                        Functions.displayMessage(
                            this@AddMemberForthActivity, response.body()?.message
                        )
//                        Functions.showAlertMessageWithOK(
//                            this@AddMemberForthActivity, "",
////                        "Message",
//                            response.body()?.message
//                        )
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@AddMemberForthActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Get_Language_Response>, t: Throwable) {
                Toast.makeText(this@AddMemberForthActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    /*OriginatingState API*/
    private fun OriginatingState() {
        val pd = CustomProgressBar(this@AddMemberForthActivity)
        pd.show()
        val call: Call<Get_Indianstates_Response> =
            MyHssApplication.instance!!.api.get_indianstates()
        call.enqueue(object : Callback<Get_Indianstates_Response> {
            override fun onResponse(
                call: Call<Get_Indianstates_Response>, response: Response<Get_Indianstates_Response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {

                        var data_relationship: List<Datum_Get_Indianstates> =
                            ArrayList<Datum_Get_Indianstates>()
                        data_relationship = response.body()!!.data!!
                        Log.d("atheletsBeans", data_relationship.toString())
                        for (i in 1 until data_relationship.size) {
                            Log.d("relationshipName", data_relationship[i].stateName.toString())
                        }
                        originName = listOf(arrayOf(data_relationship).toString())
                        originID = listOf(arrayOf(data_relationship).toString())

                        val mStringList = ArrayList<String>()
                        for (i in 0 until data_relationship.size) {
                            mStringList.add(
//                            data_relationship[i].memberRelationshipId.toString() +
                                data_relationship[i].stateName.toString()
                            )
                        }

                        val mStringListnew = ArrayList<String>()
                        for (i in 0 until data_relationship.size) {
                            mStringListnew.add(
                                data_relationship[i].indianStateListId.toString()
                            )
                        }

                        var mStringArray = mStringList.toArray()
                        var mStringArraynew = mStringList.toArray()

                        for (i in mStringArray.indices) {
                            Log.d("string is", mStringArray[i] as String)
                        }

                        for (i in mStringArraynew.indices) {
                            Log.d("mStringArraynew is", mStringArraynew[i] as String)
                        }

                        mStringArray = mStringList.toArray(mStringArray)
                        mStringArraynew = mStringListnew.toArray(mStringArraynew)

                        val list: ArrayList<String> = arrayListOf<String>()
                        val listnew: ArrayList<String> = arrayListOf<String>()

                        for (element in mStringArray) {
                            Log.d("LIST==>", element.toString())
                            list.add(element.toString())
                            Log.d("list==>", list.toString())

                            val listn = arrayOf(element)
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                originName =
                                    list//listOf(listn.toCollection(ArrayList()).toString())
                            }
                        }

                        for (element in mStringArraynew) {
                            Log.d("LIST==>", element.toString())
                            listnew.add(element.toString())
                            Log.d("list==>", listnew.toString())

                            val listn = arrayOf(element)
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                originID =
                                    listnew//listOf(listn.toCollection(ArrayList()).toString())
                            }
                        }

                        Log.d("originName==>", originName.toString())
                        SearchSpinner(originName.toTypedArray(), edit_originating_state_in_india)

                    } else {
                        Functions.displayMessage(
                            this@AddMemberForthActivity, response.body()?.message
                        )
//                        Functions.showAlertMessageWithOK(
//                            this@AddMemberForthActivity, "",
////                        "Message",
//                            response.body()?.message
//                        )
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@AddMemberForthActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Get_Indianstates_Response>, t: Throwable) {
                Toast.makeText(this@AddMemberForthActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    private fun firstAidInforApi() {
        val pd = CustomProgressBar(this@AddMemberForthActivity)
        pd.show()
        val call: Call<FirstAidInfo> = MyHssApplication.instance!!.api.getFirstAidInfor()
        call.enqueue(object : Callback<FirstAidInfo> {
            override fun onResponse(
                call: Call<FirstAidInfo>, response: Response<FirstAidInfo>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    DebugLog.e("Status : " + response.body()?.status.toString())

                    if (response.body()?.status!!) {
                        data_firstaidInfo = ArrayList<DataFirstAidInfo>()
                        data_firstaidInfo = response.body()!!.data!!
                        DebugLog.e("atheletsBeans " + data_firstaidInfo.toString())
                        for (i in 1 until data_firstaidInfo.size) {
                            DebugLog.e("First aid info " + data_firstaidInfo[i].name.toString())
                        }
                        firstAidInfoName = listOf(arrayOf(data_firstaidInfo).toString())
                        firstAidInfoID = listOf(arrayOf(data_firstaidInfo).toString())

                        val mStringList = ArrayList<String>()
                        for (i in 0 until data_firstaidInfo.size) {
                            mStringList.add(
                                data_firstaidInfo[i].name.toString()
                            )
                        }

                        val mStringListnew = ArrayList<String>()
                        for (i in 0 until data_firstaidInfo.size) {
                            mStringListnew.add(
                                data_firstaidInfo[i].id.toString()
                            )
                        }

                        var mStringArray = mStringList.toArray()
                        var mStringArraynew = mStringList.toArray()

                        for (i in mStringArray.indices) {
                            DebugLog.e("string is " + mStringArray[i] as String)
                        }

                        for (i in mStringArraynew.indices) {
                            DebugLog.e("mStringArraynew is " + mStringArraynew[i] as String)
                        }

                        mStringArray = mStringList.toArray(mStringArray)
                        mStringArraynew = mStringListnew.toArray(mStringArraynew)

                        val list: ArrayList<String> = arrayListOf<String>()
                        val listnew: ArrayList<String> = arrayListOf<String>()

                        for (element in mStringArray) {
                            DebugLog.e("LIST==> " + element.toString())
                            list.add(element.toString())
                            DebugLog.e("list==> " + list.toString())
                            val listn = arrayOf(element)
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                firstAidInfoName =
                                    list//listOf(listn.toCollection(ArrayList()).toString())
                            }
                        }

                        for (element in mStringArraynew) {
                            DebugLog.e("LIST==> " + element.toString())
                            listnew.add(element.toString())
                            DebugLog.e("list==>" + listnew.toString())

                            val listn = arrayOf(element)
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                firstAidInfoID =
                                    listnew//listOf(listn.toCollection(ArrayList()).toString())
                            }
                        }

                        DebugLog.e("firstAidInfoName==> " + firstAidInfoName.toString())
                        SearchSpinner(firstAidInfoName.toTypedArray(), edit_aid_type)


                        if (intent.getStringExtra("TYPE_SELF") != "self") {
                            if (intent.getStringExtra("FAMILY") == "PROFILE" && intent.getStringExtra(
                                    "TITLENAME"
                                ) == "Profile"
                            ) {
                                setFirstAidInfo(
                                    sessionManager.fetchQUALIFICATION_VALUE().toString()
                                )
                            }
                        }


                    } else {
                        Functions.displayMessage(
                            this@AddMemberForthActivity, response.body()?.message
                        )
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@AddMemberForthActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<FirstAidInfo>, t: Throwable) {
                Toast.makeText(this@AddMemberForthActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    //handling the image chooser activity result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            DebugLog.e("filePath==> " + data?.data.toString())

            qualification_file_image.visibility = View.GONE

            val uriString: String = filePath.toString()
            val myFile = File(uriString)
            DebugLog.e("ioooo=>> " + myFile.toString())

            Upload_file = myFile.toString()

            val pdfPath: String = getPath(this@AddMemberForthActivity, filePath)
//            val pdfPath: String = getPDFPath(filePath)
            DebugLog.e("pdfPath Path=> " + pdfPath)
            if (pdfPath == "Not found" || pdfPath == "") {
                Functions.showAlertMessageWithOK(
                    this, "PDF Upload Error", "Please select the file from the File Manager."
                )
            } else {
                val pathA = File(pdfPath)
                var length: Long = pathA.length()
                length = length / 1024
                DebugLog.e("File size => $length")
                if (length >= 2000) {
                    Functions.showAlertMessageWithOK(
                        this@AddMemberForthActivity, "File Upload Error",
                        "Please select file size lessthan 2MB.",
                    )
                } else {
                    mediaFileDoc = File(pdfPath)
                }
            }
        }

        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            ImagefilePath = data?.data
            val picturePath: String = getPath(this@AddMemberForthActivity, ImagefilePath)
            DebugLog.e("Picture Path => " + picturePath)
            val pathA = File(picturePath)
            var length: Long = pathA.length()
            length = length / 1024
            DebugLog.e("File size => $length")
            if (length >= 2000) {
                Functions.showAlertMessageWithOK(
                    this@AddMemberForthActivity, "File Upload Error",
                    "Please select file size lessthan 2MB.",
                )
            } else {
                mediaFileDoc = pathA
                Upload_file = pathA.toString()
                qualification_file_image.visibility = View.VISIBLE
                qualification_file_image.setImageURI(data?.data)

                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursor: Cursor =
                    contentResolver.query(ImagefilePath!!, filePathColumn, null, null, null)!!
                cursor.moveToFirst()
//                DebugLog.e("cursor==> " + cursor.toString())
//            Upload_file = cursor.toString()
//            Functions.printLog("cursor==>Upload_file", Upload_file)
//            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE)
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, ImagefilePath)
//                imageView.setImageBitmap(bitmap)
//                Upload_file = convertToString()!!
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun getPDFPath(uri: Uri?): String {
        var result: String? = null
        val id = DocumentsContract.getDocumentId(uri)
        val contentUri = ContentUris.withAppendedId(
            Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
//            Uri.parse("content://downloads/my_downloads"), java.lang.Long.valueOf(id)
        )
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(contentUri, projection, null, null, null)
//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//                result = cursor.getString(column_index)
//            }
//            cursor.close()
//        }
//        if (result == null) {
//            result = "Not found"
//        }
//        return result
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    fun getPath(context: Context, uri: Uri?): String {
        var result: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = context.contentResolver.query(uri!!, proj, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(proj[0])
                result = cursor.getString(column_index)
            }
            cursor.close()
        }
        if (result == null) {
            result = "Not found"
        }
        return result
    }

    private fun convertToString(): String? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imgByte: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(imgByte, Base64.DEFAULT)
    }

    private fun getOutputMediaFileUri(type: Int): Uri? {
//        requestRuntimePermission()
        return Uri.fromFile(getOutputMediaFile(type))
    }

    @SuppressLint("LongLogTag")
    private fun getOutputMediaFile(type: Int): File? {
        val mediaStorageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//            IMAGE_DIRECTORY_NAME
            IMAGE_DIRECTORY
        )
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(
                    IMAGE_DIRECTORY, ("Oops! Failed create " + IMAGE_DIRECTORY) + " directory"
                )
                return null
            }
        }
        // Create a media file name
        val timeStamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss", Locale.getDefault()
        ).format(Date())
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = File(
                mediaStorageDir.path + File.separator.toString() + "IMG_" + timeStamp.toString() + ".jpg"
            )
        } else if (type == MEDIA_TYPE_PDF) {
            mediaFile = File(
                mediaStorageDir.path + File.separator.toString() + "PDF_" + timeStamp.toString() + ".pdf"
            )
        } else {
            return null
        }
        DebugLog.e("path media file=>>>>> :-$mediaFile")
        DebugLog.e("path media file=>>>>:-IMG_$timeStamp.jpg")
        if (type == MEDIA_TYPE_IMAGE) {
            edit_qualification_file.text = "IMG_$timeStamp.jpg"
        } else if (type == MEDIA_TYPE_PDF) {
            edit_qualification_file.text = "PDF_$timeStamp.pdf"
        }
//        Upload_file = mediaFile.toString()

        val uriString: String = mediaFile.toString()
        val myFile = File(uriString)
        DebugLog.e("myFile " + " media myFile:-$myFile")
//        Upload_file = myFile.toString()
//        getPath(fileUri)
        return mediaFile
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            mTagsEditText.showDropDown()
        }
    }

    override fun onTagsChanged(tags: Collection<String?>) {
        Log.d("TAG", "Tags changed: ")
        Log.d("TAG", tags.toTypedArray().contentToString())
        Log.d("TAG", tags.toString())
        dietary_txt_layout.visibility = View.VISIBLE
        dietary_txt.text = tags.toString()
        dietary_txt_view.addView(dietary_txt)
        dietary_txt_view.addView(cross_item)
    }

    override fun onEditingFinished() {
        Log.d("TAG", "OnEditing finished")
        cross_item.setOnClickListener {
            dietary_txt_view.removeView(dietary_txt)
        }
    }

    private fun callMembershipApi(sType: String, isDoc: Boolean) {
        val pd = CustomProgressBar(this@AddMemberForthActivity)
        pd.show()
        DebugLog.e(
            sType + " , " + " isDoc : " + isDoc + "  => " + intent.getStringExtra("IS_SELF")
                .toString()
        )

        var first_aid_date = edit_date_of_first_aid_qualification.text.toString()
        var first_aid_pro_body = edit_profe_body_regis_num.text.toString()

        when (qualified_info) {
            "0" -> {
                FIRSTAID_ID = ""
                first_aid_pro_body = ""
                first_aid_date = ""
                mediaFileDoc = null
            }

            "1" -> {
                for (n in data_firstaidInfo.indices) {
                    if (data_firstaidInfo.get(n).id == FIRSTAID_ID) {
                        when (data_firstaidInfo.get(n).is_doc) {
                            "1" -> {
                                first_aid_pro_body = ""
                            }

                            "0" -> {
                                first_aid_date = ""
                                mediaFileDoc = null
                            }
                        }
                        break
                    }
                }
            }
        }


        val builderData: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)

        builderData.addFormDataPart("user_id", sessionManager.fetchUserID().toString())
        builderData.addFormDataPart("first_name", intent.getStringExtra("FIRST_NAME").toString())
        builderData.addFormDataPart("middle_name", intent.getStringExtra("MIDDLE_NAME").toString())
        builderData.addFormDataPart("last_name", intent.getStringExtra("LAST_NAME").toString())
        builderData.addFormDataPart("username", intent.getStringExtra("USERNAME").toString())
        builderData.addFormDataPart("email", intent.getStringExtra("EMAIL").toString())
        builderData.addFormDataPart("password", intent.getStringExtra("PASSWORD").toString())
        builderData.addFormDataPart("gender", intent.getStringExtra("GENDER").toString())
        builderData.addFormDataPart("dob", intent.getStringExtra("DOB").toString())
        builderData.addFormDataPart("age", intent.getStringExtra("AGE").toString())
        builderData.addFormDataPart(
            "relationship", intent.getStringExtra("RELATIONSHIP").toString()
        )
        builderData.addFormDataPart(
            "other_relationship", intent.getStringExtra("OTHER_RELATIONSHIP").toString()
        )
        builderData.addFormDataPart("occupation", intent.getStringExtra("OCCUPATION").toString())
        builderData.addFormDataPart(
            "occupation_name", intent.getStringExtra("OCCUPATION_NAME").toString()
        )
        builderData.addFormDataPart("shakha", intent.getStringExtra("SHAKHA").toString())
        builderData.addFormDataPart("mobile", intent.getStringExtra("MOBILE").toString())
        builderData.addFormDataPart("land_line", intent.getStringExtra("LAND_LINE").toString())
        builderData.addFormDataPart(
            "secondary_email", intent.getStringExtra("SECOND_EMAIL").toString()
        )
        builderData.addFormDataPart("post_code", intent.getStringExtra("POST_CODE").toString())
        builderData.addFormDataPart(
            "building_name", intent.getStringExtra("BUILDING_NAME").toString()
        )
        builderData.addFormDataPart(
            "address_line_1", intent.getStringExtra("ADDRESS_ONE").toString()
        )
        builderData.addFormDataPart(
            "address_line_2", intent.getStringExtra("ADDRESS_TWO").toString()
        )
        builderData.addFormDataPart("post_town", intent.getStringExtra("POST_TOWN").toString())
        builderData.addFormDataPart("county", intent.getStringExtra("COUNTY").toString())
        builderData.addFormDataPart(
            "emergency_name", intent.getStringExtra("EMERGENCY_NAME").toString()
        )
        builderData.addFormDataPart(
            "emergency_phone", intent.getStringExtra("EMERGENCY_PHONE").toString()
        )
        builderData.addFormDataPart(
            "emergency_email", intent.getStringExtra("EMERGENCY_EMAIL").toString()
        )
        builderData.addFormDataPart(
            "emergency_relationship", intent.getStringExtra("EMERGENCY_RELATIONSHIP").toString()
        )
        builderData.addFormDataPart(
            "other_emergency_relationship",
            intent.getStringExtra("OTHER_EMERGENCY_RELATIONSHIP").toString()
        )
        builderData.addFormDataPart("medical_information", medical_info)
        builderData.addFormDataPart(
            "provide_details", edit_medical_information_details.text.toString()
        )
        builderData.addFormDataPart("special_med_dietry_info", DIETARY_ID)
        builderData.addFormDataPart("language", SPOKEN_ID).addFormDataPart("state", ORIGIN_ID)
        builderData.addFormDataPart(
            "parent_member_id", intent.getStringExtra("PARENT_MEMBER").toString()
        )
        builderData.addFormDataPart("type", intent.getStringExtra("TYPE").toString())
        builderData.addFormDataPart("is_linked", intent.getStringExtra("IS_LINKED").toString())
        builderData.addFormDataPart("is_self", sType).addFormDataPart("app", "yes")
        builderData.addFormDataPart("is_qualified_in_first_aid", qualified_info) // 1 or 0
        builderData.addFormDataPart("first_aid_qualification_val", FIRSTAID_ID) // value of list
        builderData.addFormDataPart("date_of_first_aid_qualification", first_aid_date) // date
        builderData.addFormDataPart(
            "professional_body_registartion_number", first_aid_pro_body
        )// pro body number
        if (mediaFileDoc != null) {
            builderData.addFormDataPart(
                "qualification_file",
                mediaFileDoc?.getName(),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), mediaFileDoc!!)
            )//File
        } else {
            builderData.addFormDataPart("qualification_file", "")
        }

        val requestBody: MultipartBody = builderData.build()
        for (i in 0 until requestBody.size) {
            val part = requestBody.part(i)
            val key = part.headers?.get("Content-Disposition")?.substringAfter("name=\"")
                ?.substringBefore("\"")
            val value = part.body?.let { requestBody ->
                val buffer = Buffer()
                requestBody.writeTo(buffer)
                buffer.readUtf8()
            } ?: ""
            DebugLog.e("$key: $value")
        }


        val call: Call<JsonObject> =
            MyHssApplication.instance!!.api.postAddOrCreateMembership(requestBody)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.isSuccessful) {
                        var resp = ""
                        if (response.body().toString().startsWith("{")) {
                            resp = "[" + response.body().toString() + "]"
                        }
                        DebugLog.e(resp)
                        val jsonObj = JSONArray(resp)
                        val jsonObject = jsonObj.getJSONObject(0)
                        val status = jsonObject.get("status")
                        val message = jsonObject.get("message")
                        if (status == true) {
                            when (sType) {
                                "family" -> {
                                    val i = Intent(
                                        this@AddMemberForthActivity, HomeActivity::class.java
                                    )
                                    i.putExtra("FAMILY", intent.getStringExtra("FAMILY"))
                                    startActivity(i)
                                    finishAffinity()
                                }

                                "self" -> {
                                    val i = Intent(
                                        this@AddMemberForthActivity, WelcomeActivity::class.java
                                    )
                                    startActivity(i)
                                    finishAffinity()
                                }
                            }
                        } else {
                            Functions.showAlertMessageWithOK(
                                this@AddMemberForthActivity, "", message.toString()
                            )
                        }
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@AddMemberForthActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@AddMemberForthActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    private fun callEditProfileApi(isDoc: Boolean) {
        DebugLog.e(
            "PROFILE, isDoc : " + isDoc + "  => " + intent.getStringExtra("IS_SELF").toString()
        )

        val pd = CustomProgressBar(this@AddMemberForthActivity)
        pd.show()

        var first_aid_date = edit_date_of_first_aid_qualification.text.toString()
        var first_aid_pro_body = edit_profe_body_regis_num.text.toString()

        when (qualified_info) {
            "0" -> {
                FIRSTAID_ID = ""
                first_aid_pro_body = ""
                first_aid_date = ""
                mediaFileDoc = null
            }

            "1" -> {
                for (n in data_firstaidInfo.indices) {
                    if (data_firstaidInfo.get(n).id == FIRSTAID_ID) {
                        when (data_firstaidInfo.get(n).is_doc) {
                            "1" -> {
                                first_aid_pro_body = ""
                            }

                            "0" -> {
                                first_aid_date = ""
                                mediaFileDoc = null
                            }
                        }
                        break
                    }
                }
            }
        }

        val builderData: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builderData.addFormDataPart("member_id", sessionManager.fetchMEMBERID().toString())
        builderData.addFormDataPart("first_name", intent.getStringExtra("FIRST_NAME").toString())
        builderData.addFormDataPart("middle_name", intent.getStringExtra("MIDDLE_NAME").toString())
        builderData.addFormDataPart("last_name", intent.getStringExtra("LAST_NAME").toString())
//            .addFormDataPart("username", intent.getStringExtra("USERNAME").toString())
//            .addFormDataPart("email", intent.getStringExtra("EMAIL").toString())
//            .addFormDataPart("password", intent.getStringExtra("PASSWORD").toString())
        builderData.addFormDataPart("gender", intent.getStringExtra("GENDER").toString())
        builderData.addFormDataPart("dob", intent.getStringExtra("DOB").toString())
        builderData.addFormDataPart("age", intent.getStringExtra("AGE").toString())
        builderData.addFormDataPart(
            "relationship", intent.getStringExtra("RELATIONSHIP").toString()
        )
        builderData.addFormDataPart(
            "other_relationship", intent.getStringExtra("OTHER_RELATIONSHIP").toString()
        )
        builderData.addFormDataPart("occupation", intent.getStringExtra("OCCUPATION").toString())
//        builderData.addFormDataPart("occupation_name", intent.getStringExtra("OCCUPATION_NAME").toString())
        builderData.addFormDataPart("shakha", intent.getStringExtra("SHAKHA").toString())
        builderData.addFormDataPart("mobile", intent.getStringExtra("MOBILE").toString())
        builderData.addFormDataPart("land_line", intent.getStringExtra("LAND_LINE").toString())
//            .addFormDataPart("secondary_email", intent.getStringExtra("SECOND_EMAIL").toString())
        builderData.addFormDataPart("post_code", intent.getStringExtra("POST_CODE").toString())
        builderData.addFormDataPart(
            "building_name", intent.getStringExtra("BUILDING_NAME").toString()
        )
        builderData.addFormDataPart(
            "address_line_1", intent.getStringExtra("ADDRESS_ONE").toString()
        )
        builderData.addFormDataPart(
            "address_line_2", intent.getStringExtra("ADDRESS_TWO").toString()
        )
        builderData.addFormDataPart("post_town", intent.getStringExtra("POST_TOWN").toString())
        builderData.addFormDataPart("county", intent.getStringExtra("COUNTY").toString())
        builderData.addFormDataPart(
            "emergency_name", intent.getStringExtra("EMERGENCY_NAME").toString()
        )
        builderData.addFormDataPart(
            "emergency_phone", intent.getStringExtra("EMERGENCY_PHONE").toString()
        )
        builderData.addFormDataPart(
            "emergency_email", intent.getStringExtra("EMERGENCY_EMAIL").toString()
        )
        builderData.addFormDataPart(
            "emergency_relationship", intent.getStringExtra("EMERGENCY_RELATIONSHIP").toString()
        )
        builderData.addFormDataPart(
            "other_emergency_relationship",
            intent.getStringExtra("OTHER_EMERGENCY_RELATIONSHIP").toString()
        )
        builderData.addFormDataPart("medical_information", medical_info)
        builderData.addFormDataPart(
            "provide_details", edit_medical_information_details.text.toString()
        )
        builderData.addFormDataPart("special_med_dietry_info", DIETARY_ID)
        builderData.addFormDataPart("language", SPOKEN_ID)
        builderData.addFormDataPart("state", ORIGIN_ID)
        builderData.addFormDataPart(
            "parent_member_id", intent.getStringExtra("PARENT_MEMBER").toString()
        )
        builderData.addFormDataPart("type", intent.getStringExtra("TYPE").toString())
        builderData.addFormDataPart("is_linked", intent.getStringExtra("IS_LINKED").toString())
        builderData.addFormDataPart("app", "true")
        builderData.addFormDataPart("update_by", sessionManager.fetchMEMBERID().toString())
        builderData.addFormDataPart("country", sessionManager.fetchCOUNTRY().toString())
        builderData.addFormDataPart("is_qualified_in_first_aid", qualified_info) // 1 or 0
        builderData.addFormDataPart("first_aid_qualification_val", FIRSTAID_ID) // value of list
        builderData.addFormDataPart("date_of_first_aid_qualification", first_aid_date) // date
        builderData.addFormDataPart(
            "professional_body_registartion_number", first_aid_pro_body
        )// pro body number
        if (mediaFileDoc != null) {
            builderData.addFormDataPart(
                "qualification_file",
                mediaFileDoc?.getName(),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), mediaFileDoc!!)
            )//File
        } else {
            builderData.addFormDataPart("qualification_file", "")
        }

        val requestBody: MultipartBody = builderData.build()
        for (i in 0 until requestBody.size) {
            val part = requestBody.part(i)
            val key = part.headers?.get("Content-Disposition")?.substringAfter("name=\"")
                ?.substringBefore("\"")
            val value = part.body?.let { requestBody ->
                val buffer = Buffer()
                requestBody.writeTo(buffer)
                buffer.readUtf8()
            } ?: ""
            DebugLog.e("$key: $value")
        }


        val call: Call<JsonObject> =
            MyHssApplication.instance!!.api.postUpdateMembership(requestBody)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.isSuccessful) {
                        var resp = ""
                        if (response.body().toString().startsWith("{")) {
                            resp = "[" + response.body().toString() + "]"
                        }
                        DebugLog.e(resp)
                        val jsonObj = JSONArray(resp)
                        val jsonObject = jsonObj.getJSONObject(0)
                        val status = jsonObject.get("status")
                        val message = jsonObject.get("message")
                        if (status == true) {
                            val i = Intent(
                                this@AddMemberForthActivity, HomeActivity::class.java
                            )
                            startActivity(i)
                            finishAffinity()

                        } else {
                            Functions.showAlertMessageWithOK(
                                this@AddMemberForthActivity, "", message.toString()
                            )
                        }
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@AddMemberForthActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@AddMemberForthActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    // Permission code
    private fun openFileUploadDialog() {
        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val view_d = layoutInflater.inflate(R.layout.dialog_select_galley_pdf, null)
        val btnClose = view_d.findViewById<ImageView>(R.id.close_layout)
        val btn_image = view_d.findViewById<LinearLayout>(R.id.select_gallery)
        val btn_pdf = view_d.findViewById<LinearLayout>(R.id.select_pdf)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            btn_pdf.visibility = View.GONE
        } else {
            btn_pdf.visibility = View.VISIBLE
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        btn_image.setOnClickListener {
            openGalleryForImage()
            dialog.dismiss()
        }
        btn_pdf.setOnClickListener {
            showFileChooserforPDF()
            dialog.dismiss()
        }
        dialog.setCancelable(true)
        dialog.setContentView(view_d)
        dialog.show()
    }

    fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE) // REQUEST_CODE)
    }

    fun showFileChooserforPDF() {
        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkPermission(): Boolean {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val result1 =
                ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.READ_MEDIA_IMAGES
                )
//            val result2 = ContextCompat.checkSelfPermission(applicationContext, permission.CALL_PHONE)
//            val result3 = ContextCompat.checkSelfPermission(applicationContext, permission.CAMERA)
//            val result4 = ContextCompat.checkSelfPermission(applicationContext, permission.WRITE_EXTERNAL_STORAGE)
            return result1 == PackageManager.PERMISSION_GRANTED
//                    && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED
//                    && result4 == PackageManager.PERMISSION_GRANTED
        } else {
            val result1 = ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
//            val result2 = ContextCompat.checkSelfPermission(applicationContext, permission.CALL_PHONE)
//            val result3 = ContextCompat.checkSelfPermission(applicationContext, permission.CAMERA)
//            val result4 = ContextCompat.checkSelfPermission(applicationContext, permission.WRITE_EXTERNAL_STORAGE)

            return result1 == PackageManager.PERMISSION_GRANTED
//                    && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED && result4 == PackageManager.PERMISSION_GRANTED
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES
//                    permission.CALL_PHONE, permission.CAMERA
//                    , permission.WRITE_EXTERNAL_STORAGE
                ), PERMISSION_REQUEST_CODE
            )
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
//                    permission.CALL_PHONE,
//                    permission.CAMERA,
//                    permission.WRITE_EXTERNAL_STORAGE
                ), PERMISSION_REQUEST_CODE
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {

                val readAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
//                val callAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED
//                val cameraAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                    if (readAccepted && callAccepted && cameraAccepted) {
                    if (readAccepted) {
                        openFileUploadDialog()
//                        Log.d("File_name", File_name)
                    } else {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES)) {
                            showMessageOKCancel("You need to allow permission, in order to upload Qualification File",
                                DialogInterface.OnClickListener { dialog, which ->
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                        requestPermissions(
                                            arrayOf(
                                                Manifest.permission.READ_MEDIA_IMAGES
//                                                permission.CALL_PHONE,
//                                                permission.CAMERA,
                                            ), PERMISSION_REQUEST_CODE
                                        )
                                    }
                                })
                            return
                        }/* else if (shouldShowRequestPermissionRationale(permission.CALL_PHONE)) {
                            showMessageOKCancel("You need to allow access to phone the permissions",
                                DialogInterface.OnClickListener { dialog, which ->
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                        requestPermissions(
                                            arrayOf(
                                                permission.READ_MEDIA_IMAGES,
                                                permission.CALL_PHONE,
                                                permission.CAMERA
                                            ), PERMISSION_REQUEST_CODE
                                        )
                                    }
                                })
                            return
                        } else if (shouldShowRequestPermissionRationale(permission.CAMERA)) {
                            showMessageOKCancel("You need to allow access to Camera the permissions",
                                DialogInterface.OnClickListener { dialog, which ->
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                        requestPermissions(
                                            arrayOf(
                                                permission.READ_MEDIA_IMAGES,
                                                permission.CALL_PHONE,
                                                permission.CAMERA
                                            ), PERMISSION_REQUEST_CODE
                                        )
                                    }
                                })
                            return
                        }*/
                    }
                } else {
//                    val writeAccepted = grantResults[3] == PackageManager.PERMISSION_GRANTED
//                    if (writeAccepted && readAccepted && callAccepted && cameraAccepted) {
                    if (readAccepted) {
                        openFileUploadDialog()
                    } else {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            /*if (shouldShowRequestPermissionRationale(permission.WRITE_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow access to Write the permissions",
                                    DialogInterface.OnClickListener { dialog, which ->
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                            requestPermissions(
                                                arrayOf(
                                                    permission.WRITE_EXTERNAL_STORAGE,
                                                    permission.READ_EXTERNAL_STORAGE,
                                                    permission.CALL_PHONE,
                                                    permission.CAMERA,
                                                ), PERMISSION_REQUEST_CODE
                                            )
                                        }
                                    })
                                return
                            } else*/
                            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow permission, in order to upload Qualification File",
                                    DialogInterface.OnClickListener { dialog, which ->
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                            requestPermissions(
                                                arrayOf(
//                                                    permission.WRITE_EXTERNAL_STORAGE,
                                                    Manifest.permission.READ_EXTERNAL_STORAGE
//                                                    permission.CALL_PHONE,
//                                                    permission.CAMERA,
                                                ), PERMISSION_REQUEST_CODE
                                            )
                                        }
                                    })
                                return
                            } /*else if (shouldShowRequestPermissionRationale(permission.CALL_PHONE)) {
                                showMessageOKCancel("You need to allow access to phone the permissions",
                                    DialogInterface.OnClickListener { dialog, which ->
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                            requestPermissions(
                                                arrayOf(
                                                    permission.WRITE_EXTERNAL_STORAGE,
                                                    permission.READ_EXTERNAL_STORAGE,
                                                    permission.CALL_PHONE,
                                                    permission.CAMERA
                                                ), PERMISSION_REQUEST_CODE
                                            )
                                        }
                                    })
                                return
                            } else if (shouldShowRequestPermissionRationale(permission.CAMERA)) {
                                showMessageOKCancel("You need to allow access to Camera the permissions",
                                    DialogInterface.OnClickListener { dialog, which ->
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                            requestPermissions(
                                                arrayOf(
                                                    permission.WRITE_EXTERNAL_STORAGE,
                                                    permission.READ_EXTERNAL_STORAGE,
                                                    permission.CALL_PHONE,
                                                    permission.CAMERA
                                                ), PERMISSION_REQUEST_CODE
                                            )
                                        }
                                    })
                                return
                            }*/
                        }
                    }
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    //  || !ActivityCompat.shouldShowRequestPermissionRationale(this, permission.CALL_PHONE)
                    //  || !ActivityCompat.shouldShowRequestPermissionRationale(this, permission.CAMERA)
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            this, Manifest.permission.READ_MEDIA_IMAGES
                        )
                    ) {
                        showPermissionDeniedDialogSetting()
                    } else {
                        reRequestPermissionAccessDialog()
                    }
                } else {
                    //|| !ActivityCompat.shouldShowRequestPermissionRationale(this, permission.CALL_PHONE)
                    // || !ActivityCompat.shouldShowRequestPermissionRationale(this, permission.CAMERA)
                    // || !ActivityCompat.shouldShowRequestPermissionRationale(this, permission.WRITE_EXTERNAL_STORAGE)
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            this, Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    ) {
                        showPermissionDeniedDialogSetting()
                    } else {
                        reRequestPermissionAccessDialog()
                    }
                }
            }
        }
    }

    private fun showPermissionDeniedDialogSetting() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("To upload Qualification File, please enable the required permission from the app settings. Without this permission, this feature may not be accessible. Thank you for your cooperation.")
            .setCancelable(false).setPositiveButton("Settings") { _, _ ->
                openAppSettings()
            }.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun reRequestPermissionAccessDialog() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@AddMemberForthActivity)
        alertDialog.setMessage("In order to upload Qualification File, please grant the requested permission. Without this permission, the application will not be able to upload file in MyHSS Application. Thank you for your understanding.")
        alertDialog.setPositiveButton(
            "yes"
        ) { _, _ ->
            requestPermission()
        }
        alertDialog.setNegativeButton(
            "No"
        ) { _, _ ->

        }
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this@AddMemberForthActivity).setMessage(message)
            .setPositiveButton("OK", okListener).setNegativeButton(
                "Cancel"
            ) { dialogInterface, i -> finishAffinity() }.create().show()
    }

}