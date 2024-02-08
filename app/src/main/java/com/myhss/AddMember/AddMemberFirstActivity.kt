package com.uk.myhss.AddMember

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import cl.jesualex.stooltip.Position
import cl.jesualex.stooltip.Tooltip
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.DebugLog
import com.myhss.Utils.Functions
import com.myhss.Utils.UtilCommon
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.ui.linked_family.Model.Get_Member_Check_Username_Exist_Response
import com.uk.myhss.ui.linked_family.Model.Get_Single_Member_Record_Datum
import com.uk.myhss.ui.linked_family.Model.Get_Single_Member_Record_Response
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class AddMemberFirstActivity() : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private var year = 0
    private var month = 0
    private var day = 0
    private lateinit var calendar: Calendar

    private var AGE: String = ""
    private var GENDER: String = ""
    private var YEAR: String = ""

    var relationshipName: List<String> = ArrayList<String>()
    var relationshipID: List<String> = ArrayList<String>()
    var OccupationName: List<String> = ArrayList<String>()
    var OccupationID: List<String> = ArrayList<String>()
    var VibhagName: List<String> = ArrayList<String>()
    var VibhagID: List<String> = ArrayList<String>()
    var NagarName: List<String> = ArrayList<String>()
    var NagarID: List<String> = ArrayList<String>()
    var ShakhaName: List<String> = ArrayList<String>()
    var ShakhaID: List<String> = ArrayList<String>()

    private var RELATIONSHIP_ID: String = ""
    private var OTHER_RELATIONSHIP: String = ""
    private var OCCUPATION_ID: String = ""
    private var OCCUPATION_NAME: String = ""
    private var VIBHAG_ID: String = ""
    private var NAGAR_ID: String = ""
    private var SHAKHA_ID: String = ""
    private var strCurrentDate: String = ""

    private lateinit var edit_occupation_name: TextInputEditText
    private lateinit var tooltip_view: ImageView

    private lateinit var edit_realationship_txt: SearchableSpinner
    private lateinit var edit_occupation_select_other: SearchableSpinner
    private lateinit var edit_vibhag_region: SearchableSpinner
    private lateinit var edit_nagar_town: SearchableSpinner
    private lateinit var edit_shakha_branch: SearchableSpinner

    private lateinit var realationship_txt: RelativeLayout
    private lateinit var occupation_select_other: RelativeLayout
    private lateinit var vibhag_region: RelativeLayout
    private lateinit var nagar_town: RelativeLayout
    private lateinit var shakha_branch: RelativeLayout

    private lateinit var occupation_other_view: RelativeLayout
    private lateinit var realationship_other_view: RelativeLayout
    private lateinit var family_view: LinearLayout
    private lateinit var primary_member_layout: LinearLayout

    private lateinit var rootLayout: LinearLayout
    private lateinit var next_layout: LinearLayout
    private lateinit var password_layout: LinearLayout
    private lateinit var confirm_password_layout: LinearLayout
    private lateinit var edit_firstname: TextInputEditText
    private lateinit var edit_middlename: TextInputEditText
    private lateinit var edit_surname: TextInputEditText
    private lateinit var edit_username: TextInputEditText
    private lateinit var edit_email: TextInputEditText
    private lateinit var edit_password: TextInputEditText
    private lateinit var edit_confirm_password: TextInputEditText
    private lateinit var edit_realationship_name: TextInputEditText
    private lateinit var edit_dateofbirth: TextInputEditText
    private lateinit var til_firstName: TextInputLayout
    private lateinit var til_middleName: TextInputLayout
    private lateinit var til_surname: TextInputLayout
    private lateinit var til_username: TextInputLayout
    private lateinit var til_email: TextInputLayout
    private lateinit var til_password: TextInputLayout
    private lateinit var til_confirm_password: TextInputLayout
    private lateinit var til_dob: TextInputLayout
    private lateinit var til_relatioship_other_name: TextInputLayout


    private lateinit var male_view: LinearLayout
    private lateinit var male_txt: TextView
    private lateinit var male_right_img: ImageView
    private lateinit var male_icon: ImageView
    private lateinit var female_view: LinearLayout
    private lateinit var female_txt: TextView
    private lateinit var female_right: ImageView
    private lateinit var female_img: ImageView
    private lateinit var age_layout: LinearLayout
    private lateinit var age_text: TextView
    private lateinit var header_title: TextView

    private lateinit var vibhag_select_default: TextView
    private lateinit var nagar_select_default: TextView
    private lateinit var shakha_select_default: TextView

    private val apiHandler = Handler(Looper.getMainLooper())
    private var lastTypedText: String = ""
    private var isUserNameValid = false
    private lateinit var userNameValidError: String

    @SuppressLint("NewApi", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addmemberfirst)

        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("AddMemberStep1VC")
        sessionManager.firebaseAnalytics.setUserProperty(
            "AddMemberStep1VC", "AddMemberFirstActivity"
        )

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        val back_arrow = findViewById<ImageView>(R.id.back_arrow)
        header_title = findViewById<TextView>(R.id.header_title)

        edit_occupation_name = findViewById(R.id.edit_occupation_name)

        edit_realationship_txt = findViewById(R.id.edit_realationship_txt)
        edit_occupation_select_other = findViewById(R.id.edit_occupation_select_other)
        edit_vibhag_region = findViewById(R.id.edit_vibhag_region)
        edit_nagar_town = findViewById(R.id.edit_nagar_town)
        edit_shakha_branch = findViewById(R.id.edit_shakha_branch)

        realationship_txt = findViewById(R.id.realationship_txt)
        occupation_select_other = findViewById(R.id.occupation_select_other)
        vibhag_region = findViewById(R.id.vibhag_region)
        nagar_town = findViewById(R.id.nagar_town)
        shakha_branch = findViewById(R.id.shakha_branch)

        occupation_other_view = findViewById(R.id.occupation_other_view)
        realationship_other_view = findViewById(R.id.realationship_other_view)
        family_view = findViewById(R.id.family_view)
        primary_member_layout = findViewById(R.id.primary_member_layout)

        rootLayout = findViewById(R.id.rootLayout)
        next_layout = findViewById(R.id.next_layout)
        password_layout = findViewById(R.id.password_layout)
        confirm_password_layout = findViewById(R.id.confirm_password_layout)
        edit_firstname = findViewById(R.id.edit_firstname)
        edit_middlename = findViewById(R.id.edit_middlename)
        edit_surname = findViewById(R.id.edit_surname)
        edit_username = findViewById(R.id.edit_username)
        edit_email = findViewById(R.id.edit_email)
        edit_password = findViewById(R.id.edit_password)
        edit_confirm_password = findViewById(R.id.edit_confirm_password)
        edit_realationship_name = findViewById(R.id.edit_realationship_name)
        edit_dateofbirth = findViewById(R.id.edit_dateofbirth)
        male_view = findViewById(R.id.male_view)
        male_txt = findViewById(R.id.male_txt)
        male_right_img = findViewById(R.id.male_right_img)
        male_icon = findViewById(R.id.male_icon)
        female_view = findViewById(R.id.female_view)
        female_txt = findViewById(R.id.female_txt)
        female_right = findViewById(R.id.female_right)
        female_img = findViewById(R.id.female_img)
        tooltip_view = findViewById(R.id.tooltip_view)
        age_layout = findViewById(R.id.age_layout)
        age_text = findViewById(R.id.age_text)
        til_firstName = findViewById(R.id.til_firstName)
        til_middleName = findViewById(R.id.til_middleName)
        til_surname = findViewById(R.id.til_surname)
        til_username = findViewById(R.id.til_username)
        til_email = findViewById(R.id.til_email)
        til_password = findViewById(R.id.til_password)
        til_confirm_password = findViewById(R.id.til_confirm_password)
        til_dob = findViewById(R.id.til_dob)
        til_relatioship_other_name = findViewById(R.id.til_relatioship_other_name)
        vibhag_select_default = findViewById(R.id.vibhag_select_default)
        nagar_select_default = findViewById(R.id.nagar_select_default)
        shakha_select_default = findViewById(R.id.shakha_select_default)

        Log.d("TYPE_SELF", intent.getStringExtra("TYPE_SELF")!!)

        callApis()

        var calendar = Calendar.getInstance(Locale.getDefault())
        val simpledateFormat = SimpleDateFormat("dd/MM/yyyy")
        strCurrentDate = simpledateFormat.format(calendar.time)
        Functions.printLog("currentDate", strCurrentDate)

        if (intent.getStringExtra("TYPE_SELF") != "self") {
            header_title.text = getString(R.string.Add_family_member)
            family_view.visibility = View.VISIBLE
            primary_member_layout.visibility = View.VISIBLE
            if (intent.getStringExtra("FAMILY") == "PROFILE") {
                header_title.text = getString(R.string.profile)
                family_view.visibility = View.GONE
                primary_member_layout.visibility = View.GONE
                password_layout.visibility = View.GONE
                confirm_password_layout.visibility = View.GONE
                setSelfInfoFromSession()
                DebugLog.e("edit_email" + edit_email.text.toString())
                if (sessionManager.fetchGENDER() == "Male") {
                    male_right_img.visibility = View.VISIBLE
                    male_view.setBackgroundResource(R.drawable.edit_primery_color_round)
                    female_view.setBackgroundResource(R.drawable.edittext_round)
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        male_txt.setTextColor(getColor(R.color.primaryColor))
                    }
                    male_right_img.setImageResource(R.drawable.righttikmark)
                    male_icon.setColorFilter(
                        ContextCompat.getColor(this@AddMemberFirstActivity, R.color.primaryColor),
                        android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        female_txt.setTextColor(getColor(R.color.grayColorColor))
                    }
                    female_right.visibility = View.INVISIBLE
                    female_img.setColorFilter(
                        ContextCompat.getColor(this@AddMemberFirstActivity, R.color.grayColorColor),
                        android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                    GENDER = "M"
                } else {
                    female_right.visibility = View.VISIBLE
                    female_view.setBackgroundResource(R.drawable.edit_pink_color_round)
                    male_view.setBackgroundResource(R.drawable.edittext_round)
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        male_txt.setTextColor(getColor(R.color.grayColorColor))
                    }
                    male_right_img.visibility = View.INVISIBLE
                    male_icon.setColorFilter(
                        ContextCompat.getColor(this@AddMemberFirstActivity, R.color.grayColorColor),
                        android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        female_txt.setTextColor(getColor(R.color.pinkColor))
                    }
                    female_right.setImageResource(R.drawable.righttikmarkfemale)
                    female_img.setColorFilter(
                        ContextCompat.getColor(this@AddMemberFirstActivity, R.color.pinkColor),
                        android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                    GENDER = "F"
                }

                if (sessionManager.fetchRELATIONSHIPNAME_OTHER() != "") {
                    edit_realationship_name.visibility = View.VISIBLE
                    edit_realationship_name.setText(sessionManager.fetchRELATIONSHIPNAME_OTHER())
                }

                edit_occupation_select_other.setSelection(OccupationName.indexOf(sessionManager.fetchOCCUPATIONNAME()))
                edit_vibhag_region.setSelection(VibhagName.indexOf(sessionManager.fetchVIBHAGNAME()))
                edit_nagar_town.setSelection(NagarName.indexOf(sessionManager.fetchNAGARNAME()))
                edit_shakha_branch.setSelection(ShakhaName.indexOf(sessionManager.fetchSHAKHANAME()))

//                DebugLog.e("VIBHAG " + sessionManager.fetchVIBHAGNAME())
//                DebugLog.e("NAGAR " + sessionManager.fetchNAGARNAME())
//                DebugLog.e("SHAKHA " + sessionManager.fetchSHAKHANAME())
//                DebugLog.e("RELATIONSHIP " + sessionManager.fetchRELATIONSHIPNAME())
//                DebugLog.e("OTHER_RELATIONSHIP " + sessionManager.fetchRELATIONSHIPNAME_OTHER())
//                DebugLog.e("OCCUPATION " + sessionManager.fetchOCCUPATIONNAME())

            } else if (intent.getStringExtra("FAMILY") == "FAMILY") {
                header_title.text = getString(R.string.family_member)
                family_view.visibility = View.VISIBLE
                primary_member_layout.visibility = View.VISIBLE

                password_layout.visibility = View.GONE
                confirm_password_layout.visibility = View.GONE

                if (Functions.isConnectingToInternet(this@AddMemberFirstActivity)) {
                    val USER_ID = sessionManager.fetchUserID()!!
                    val MEMBER_ID = sessionManager.fetchMEMBERID()!!
                    myMemberView(USER_ID, MEMBER_ID)
                } else {
                    Toast.makeText(
                        this@AddMemberFirstActivity,
                        resources.getString(R.string.no_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            header_title.text = getString(R.string.Add_self)
            family_view.visibility = View.GONE
            primary_member_layout.visibility = View.GONE
            setSelfInfoFromSession()
        }

        male_view.setOnClickListener(DebouncedClickListener {
            male_right_img.visibility = View.VISIBLE

            male_view.setBackgroundResource(R.drawable.edit_primery_color_round)
            female_view.setBackgroundResource(R.drawable.edittext_round)

            male_txt.setTextColor(getColor(R.color.primaryColor))
            male_right_img.setImageResource(R.drawable.righttikmark)
            male_icon.setColorFilter(
                ContextCompat.getColor(this, R.color.primaryColor),
                PorterDuff.Mode.MULTIPLY
            )

            female_txt.setTextColor(getColor(R.color.grayColorColor))
            female_right.visibility = View.INVISIBLE
            female_img.setColorFilter(
                ContextCompat.getColor(this, R.color.grayColorColor),
                PorterDuff.Mode.MULTIPLY
            )

            GENDER = "M"
        })

        female_view.setOnClickListener(DebouncedClickListener {
            female_right.visibility = View.VISIBLE

            female_view.setBackgroundResource(R.drawable.edit_pink_color_round)
            male_view.setBackgroundResource(R.drawable.edittext_round)

            male_txt.setTextColor(getColor(R.color.grayColorColor))
            male_right_img.visibility = View.INVISIBLE
            male_icon.setColorFilter(
                ContextCompat.getColor(this, R.color.grayColorColor),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )

            female_txt.setTextColor(getColor(R.color.pinkColor))
            female_right.setImageResource(R.drawable.righttikmarkfemale)
            female_img.setColorFilter(
                ContextCompat.getColor(this, R.color.pinkColor),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )

            GENDER = "F"
        })

        back_arrow.setOnClickListener(DebouncedClickListener {
            finish()
        })

        edit_username.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val currentText = s.toString().trim()
                if (currentText.length >= 5 && currentText != lastTypedText) {
                    apiHandler.removeCallbacksAndMessages(null)
                    apiHandler.postDelayed(
                        { CallUserNameMethod(currentText) },
                        1000
                    )
                } else if (currentText.length < 5 && currentText != lastTypedText) {
                    apiHandler.removeCallbacksAndMessages(null)
                }
                lastTypedText = currentText

            }
        })


        edit_email.setText(sessionManager.fetchUSEREMAIL())

        edit_firstname.doOnTextChanged { text, start, before, count ->
            til_firstName.isErrorEnabled = false
        }
        edit_middlename.doOnTextChanged { text, start, before, count ->
            til_middleName.isErrorEnabled = false
        }
        edit_surname.doOnTextChanged { text, start, before, count ->
            til_surname.isErrorEnabled = false
        }
        edit_username.doOnTextChanged { text, start, before, count ->
            til_username.isErrorEnabled = false
        }
        edit_email.doOnTextChanged { text, start, before, count ->
            til_email.isErrorEnabled = false
        }
        edit_password.doOnTextChanged { text, start, before, count ->
            til_password.isErrorEnabled = false
        }
        edit_confirm_password.doOnTextChanged { text, start, before, count ->
            til_confirm_password.isErrorEnabled = false
        }
        edit_dateofbirth.doOnTextChanged { text, start, before, count ->
            til_dob.isErrorEnabled = false
        }
        edit_realationship_name.doOnTextChanged { text, start, before, count ->
            til_relatioship_other_name.isErrorEnabled = false
        }

        //Validation Start here
        next_layout.setOnClickListener(DebouncedClickListener {
            if (intent.getStringExtra("TYPE_SELF") != "self") { // profile or Family member
                if (intent.getStringExtra("FAMILY") != "PROFILE") {// Add family
                    if (edit_firstname.text.toString().isEmpty()) {
                        til_firstName.error = getString(R.string.first_name)
                        til_firstName.isErrorEnabled = true
                        til_firstName.setErrorIconDrawable(null)
                        edit_firstname.requestFocus()
                        return@DebouncedClickListener
                    } else if (!UtilCommon.isOnlyLetters(edit_firstname.text.toString())) {
                        til_firstName.error = getString(R.string.valid_first_name)
                        til_firstName.isErrorEnabled = true
                        til_firstName.setErrorIconDrawable(null)
                        edit_firstname.requestFocus()
                        return@DebouncedClickListener
                    } else if (!edit_middlename.text.toString()
                            .isEmpty() && !UtilCommon.isOnlyLetters(edit_middlename.text.toString())
                    ) {
                        til_middleName.error = getString(R.string.valid_middle_name)
                        til_middleName.isErrorEnabled = true
                        til_middleName.setErrorIconDrawable(null)
                        edit_middlename.requestFocus()
                        return@DebouncedClickListener
                    } else if (edit_surname.text.toString().isEmpty()) {
                        til_surname.error = getString(R.string.sur_name)
                        til_surname.isErrorEnabled = true
                        til_surname.setErrorIconDrawable(null)
                        edit_surname.requestFocus()
                        return@DebouncedClickListener
                    } else if (!UtilCommon.isOnlyLetters(edit_surname.text.toString())) {
                        til_surname.error = getString(R.string.valid_surname)
                        til_surname.isErrorEnabled = true
                        til_surname.setErrorIconDrawable(null)
                        edit_surname.requestFocus()
                        return@DebouncedClickListener
                    } else if (edit_username.text.toString().isEmpty()) {
                        til_username.error = getString(R.string.user_name)
                        til_username.isErrorEnabled = true
                        til_username.setErrorIconDrawable(null)
                        edit_username.requestFocus()
                        return@DebouncedClickListener
                    } else if (!isUserNameValid) {
                        til_username.error = userNameValidError
                        til_username.isErrorEnabled = true
                        til_username.setErrorIconDrawable(null)
                        edit_username.requestFocus()
                        return@DebouncedClickListener
                    } else if (!UtilCommon.isValidUserName(edit_username.text.toString())) {
                        til_username.error = getString(R.string.valid_user_name)
                        til_username.isErrorEnabled = true
                        til_username.setErrorIconDrawable(null)
                        edit_username.requestFocus()
                        return@DebouncedClickListener
                    } else if (edit_email.text.toString().isEmpty()) {
                        til_email.error = getString(R.string.email_id)
                        til_email.isErrorEnabled = true
                        til_email.setErrorIconDrawable(null)
                        edit_email.requestFocus()
                        return@DebouncedClickListener
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(edit_email.text.toString())
                            .matches()
                    ) {
                        til_email.error = getString(R.string.valid_email)
                        til_email.isErrorEnabled = true
                        til_email.setErrorIconDrawable(null)
                        edit_email.requestFocus()
                        return@DebouncedClickListener
                    } else if (edit_password.text.toString().isEmpty()) {
                        til_password.error = getString(R.string.enter_password)
                        til_password.isErrorEnabled = true
                        til_password.setErrorIconDrawable(null)
                        edit_password.requestFocus()
                        return@DebouncedClickListener
                    } else if (!UtilCommon.isValidPassword(edit_password.text.toString())) {
                        til_password.error = getString(R.string.valid_password)
                        til_password.isErrorEnabled = true
                        til_password.setErrorIconDrawable(null)
                        edit_password.requestFocus()
                        return@DebouncedClickListener
                    } else if (edit_confirm_password.text.toString().isEmpty()) {
                        til_confirm_password.error = getString(R.string.enter_confirm_password)
                        til_confirm_password.isErrorEnabled = true
                        til_confirm_password.setErrorIconDrawable(null)
                        edit_confirm_password.requestFocus()
                        return@DebouncedClickListener
                    } else if (!UtilCommon.isValidPassword(edit_confirm_password.text.toString())) {
                        til_confirm_password.error = getString(R.string.valid_confirm_password)
                        til_confirm_password.isErrorEnabled = true
                        til_confirm_password.setErrorIconDrawable(null)
                        edit_confirm_password.requestFocus()
                        return@DebouncedClickListener
                    } else if (edit_password.text.toString() != edit_confirm_password.text.toString()) {
                        til_confirm_password.error = getString(R.string.confirm_both_pass)
                        til_confirm_password.isErrorEnabled = true
                        til_confirm_password.setErrorIconDrawable(null)
                        edit_confirm_password.requestFocus()
                    } else if (edit_dateofbirth.text.toString().isEmpty()) {
                        til_dob.error = getString(R.string.enter_dob)
                        til_dob.isErrorEnabled = true
                        til_dob.setErrorIconDrawable(null)
                        edit_dateofbirth.requestFocus()
                    } else if (GENDER == "") {
                        Snackbar.make(rootLayout, "Please select gender", Snackbar.LENGTH_SHORT)
                            .show()
                    } else if (RELATIONSHIP_ID == "5" && edit_realationship_name.text.toString()
                            .isEmpty()
                    ) {
                        til_relatioship_other_name.error =
                            "Please enter the other relationship name"
                        til_relatioship_other_name.isErrorEnabled = true
                        til_relatioship_other_name.setErrorIconDrawable(null)
                        edit_realationship_name.requestFocus()
                    } else {
                        CallNextMethod()
                    }
                } else {
                    // Edit profile
                    if (edit_firstname.text.toString().isEmpty()) {
                        til_firstName.error = getString(R.string.first_name)
                        til_firstName.isErrorEnabled = true
                        til_firstName.setErrorIconDrawable(null)
                        edit_firstname.requestFocus()
                        return@DebouncedClickListener
                    } else if (!UtilCommon.isOnlyLetters(edit_firstname.text.toString())) {
                        til_firstName.error = getString(R.string.valid_first_name)
                        til_firstName.isErrorEnabled = true
                        til_firstName.setErrorIconDrawable(null)
                        edit_firstname.requestFocus()
                        return@DebouncedClickListener
                    } else if (!edit_middlename.text.toString()
                            .isEmpty() && !UtilCommon.isOnlyLetters(
                            edit_middlename.text.toString()
                        )
                    ) {
                        til_middleName.error = getString(R.string.valid_middle_name)
                        til_middleName.isErrorEnabled = true
                        til_middleName.setErrorIconDrawable(null)
                        edit_middlename.requestFocus()
                        return@DebouncedClickListener
                    } else if (edit_surname.text.toString().isEmpty()) {
                        til_surname.error = getString(R.string.sur_name)
                        til_surname.isErrorEnabled = true
                        til_surname.setErrorIconDrawable(null)
                        edit_surname.requestFocus()
                        return@DebouncedClickListener
                    } else if (!UtilCommon.isOnlyLetters(edit_surname.text.toString())) {
                        til_surname.error = getString(R.string.valid_surname)
                        til_surname.isErrorEnabled = true
                        til_surname.setErrorIconDrawable(null)
                        edit_surname.requestFocus()
                        return@DebouncedClickListener
                    }
                    /* else if (edit_username.text.toString().isEmpty()) {
                    til_username.error = getString(R.string.user_name)
                    til_username.isErrorEnabled = true
                    return@setOnClickListener
                } else if (!UtilCommon.isValidUserName(edit_username.text.toString())) {
                    til_username.error = getString(R.string.valid_user_name)
                    til_username.isErrorEnabled = true
                    return@setOnClickListener
                } else if (edit_email.text.toString().isEmpty()) {
                    til_email.error = getString(R.string.email_id)
                    til_email.isErrorEnabled = true
                    return@setOnClickListener
                } else if (!Patterns.EMAIL_ADDRESS.matcher(edit_email.text.toString()).matches()) {
                    til_email.error = getString(R.string.valid_email)
                    til_email.isErrorEnabled = true
                    return@setOnClickListener
                }*/ else if (GENDER == "") {
                        Snackbar.make(rootLayout, "Please select gender", Snackbar.LENGTH_SHORT)
                            .show()
                    } else if (edit_dateofbirth.text.toString().isEmpty()) {
                        Snackbar.make(rootLayout, "Please select DOB", Snackbar.LENGTH_SHORT).show()
                    } else {
                        CallNextMethod()
                    }
                }
            } else {
                // Add Self
                if (edit_firstname.text.toString().isEmpty()) {
                    til_firstName.error = getString(R.string.first_name)
                    til_firstName.isErrorEnabled = true
                    til_firstName.setErrorIconDrawable(null)
                    edit_firstname.requestFocus()
                    return@DebouncedClickListener
                } else if (!UtilCommon.isOnlyLetters(edit_firstname.text.toString())) {
                    til_firstName.error = getString(R.string.valid_first_name)
                    til_firstName.isErrorEnabled = true
                    til_firstName.setErrorIconDrawable(null)
                    edit_firstname.requestFocus()
                    return@DebouncedClickListener
                } else if (!edit_middlename.text.toString().isEmpty() && !UtilCommon.isOnlyLetters(
                        edit_middlename.text.toString()
                    )
                ) {
                    til_middleName.error = getString(R.string.valid_middle_name)
                    til_middleName.isErrorEnabled = true
                    til_middleName.setErrorIconDrawable(null)
                    edit_middlename.requestFocus()
                    return@DebouncedClickListener
                } else if (edit_surname.text.toString().isEmpty()) {
                    til_surname.error = getString(R.string.sur_name)
                    til_surname.isErrorEnabled = true
                    til_surname.setErrorIconDrawable(null)
                    edit_surname.requestFocus()
                    return@DebouncedClickListener
                } else if (!UtilCommon.isOnlyLetters(edit_surname.text.toString())) {
                    til_surname.error = getString(R.string.valid_surname)
                    til_surname.isErrorEnabled = true
                    til_surname.setErrorIconDrawable(null)
                    edit_surname.requestFocus()
                    return@DebouncedClickListener
                } else if (edit_email.text.toString().isEmpty()) {
                    til_email.error = getString(R.string.email_id)
                    til_email.isErrorEnabled = true
                    til_email.setErrorIconDrawable(null)
                    edit_email.requestFocus()
                    return@DebouncedClickListener
                } else if (!Patterns.EMAIL_ADDRESS.matcher(edit_email.text.toString()).matches()) {
                    til_email.error = getString(R.string.valid_email)
                    til_email.isErrorEnabled = true
                    til_email.setErrorIconDrawable(null)
                    edit_email.requestFocus()
                    return@DebouncedClickListener
                } else if (GENDER == "") {
                    Snackbar.make(rootLayout, "Please select gender", Snackbar.LENGTH_SHORT).show()
                } else if (edit_dateofbirth.text.toString().isEmpty()) {
                    Snackbar.make(rootLayout, "Please select DOB", Snackbar.LENGTH_SHORT).show()
                } else {
                    CallNextMethod()
                }
            }
        })
        //Validation End here

        edit_dateofbirth.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val isValid = true
                val working = s.toString()
                val text: String = edit_dateofbirth.getText().toString()
                val textLength: Int = edit_dateofbirth.text!!.length
                val currentYear = Calendar.getInstance()[Calendar.YEAR]

                if (text.endsWith("/") || text.endsWith("/") || text.endsWith("/")) return
                if (textLength == 1 /*|| Integer.parseInt(working) < 1 || Integer.parseInt(working) > 31*/) {
                    edit_dateofbirth.setSelection(edit_dateofbirth.text!!.length)
                } else if (textLength == 2 /*|| Integer.parseInt(working) < 1 || Integer.parseInt(
                        working
                    ) > 31*/) {
                    edit_dateofbirth.setSelection(edit_dateofbirth.text!!.length)
                } else if (textLength == 3) {
                    edit_dateofbirth.setText(
                        StringBuilder(text).insert(text.length - 1, "/").toString()
                    )
                    edit_dateofbirth.setSelection(edit_dateofbirth.text!!.length)
                } else if (textLength == 5 /*|| Integer.parseInt(working) < 1 || Integer.parseInt(
                        working
                    ) > 12*/) {
                    edit_dateofbirth.setSelection(edit_dateofbirth.text!!.length)
                } else if (textLength == 6) {
                    edit_dateofbirth.setText(
                        StringBuilder(text).insert(text.length - 1, "/").toString()
                    )
                    edit_dateofbirth.setSelection(edit_dateofbirth.text!!.length)
                } else if (textLength == 10 /*|| Integer.parseInt(edit_dateofbirth.setSelection(edit_dateofbirth.text!!.length)
                        .toString()) < currentYear*/) {
                    edit_dateofbirth.setSelection(edit_dateofbirth.text!!.length)
                    YEAR = edit_dateofbirth.setSelection(edit_dateofbirth.text!!.length).toString()
                }
            }
        })

        edit_dateofbirth.setOnEditorActionListener { _, actionId, _ ->
            age_layout.visibility = View.GONE
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Call your code here
//                CallDateValidation()
                calendar = Calendar.getInstance()

                if (edit_dateofbirth.text?.isNotEmpty() == true) {
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val initialDate =
                        dateFormat.parse(edit_dateofbirth.text.toString())
                    calendar.time = initialDate
                }

                calendar.add(Calendar.YEAR, -3)
                year = calendar.get(Calendar.YEAR)
                month = calendar.get(Calendar.MONTH)
                day = calendar.get(Calendar.DAY_OF_MONTH)

                val simpledateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val strCurrentDatenew = simpledateFormat.format(calendar.time)
                Functions.printLog("strCurrentDatenew", strCurrentDatenew)
                Functions.printLog("edit_dateofbirth", edit_dateofbirth.text.toString())
                if (edit_dateofbirth.text.toString() >= strCurrentDatenew) {
                    Functions.printLog("Date", "Current else Future Date")
                    Functions.showAlertMessageWithOK(
                        this@AddMemberFirstActivity,
                        "As your age is not 18 years",
                        "Please enter valid DOB"
                    )
                } else if (edit_dateofbirth.text.toString() > strCurrentDatenew) {
                    Functions.printLog("Date", "Current else Future Date")
                    Functions.showAlertMessageWithOK(
                        this@AddMemberFirstActivity,
                        "As your age is under 3 year",
                        "Please enter valid DOB"
                    )
                } else if (edit_dateofbirth.text.toString() == strCurrentDatenew) {
                    Functions.printLog("Date", "Under 18")
                    age_layout.visibility = View.VISIBLE
                    age_text.text =
                        "As your age is below 18 We will send an email to your guardian to allow your HSS (UK) membership to be recorded on MyHSS. If you are below 13, it is our policy to use the parent/guardian`s email address only for all communications."
                    AGE = "1"
                } else if (edit_dateofbirth.text.toString() <= strCurrentDatenew) {
                    age_layout.visibility = View.GONE
                    Functions.printLog("Date", "Above 18")
                    AGE = "0"
                }
                true
            }
            false
        }


        edit_dateofbirth.setOnClickListener(DebouncedClickListener {
            openDatePickerForDOB()
        })

        occupation_other_view.visibility = View.GONE

        edit_realationship_txt.setTitle("Select Relationship")
        edit_occupation_select_other.setTitle("Select Occupation")
        edit_vibhag_region.setTitle("Select Vibhag")
        edit_nagar_town.setTitle("Select Nagar")
        edit_shakha_branch.setTitle("Select Shakha")



        tooltip_view.setOnClickListener(DebouncedClickListener {

            Tooltip.on(tooltip_view).text(R.string.ageTipText)
//                .iconStart(android.R.drawable.ic_dialog_info)
//                .iconStartSize(30, 30)
                .color(resources.getColor(R.color.orangeColor))
//                .overlay(resources.getColor(R.color.orangeColor))
//                .iconEnd(android.R.drawable.ic_dialog_info)
//                .iconEndSize(30, 30)
                .border(resources.getColor(R.color.orangeColor), 5f).clickToHide(true).corner(10)
                .shadowPadding(10f).position(Position.BOTTOM).clickToHide(true).show(4000)
        })
    }

    private fun callApis() {
        val pd = CustomProgressBar(this@AddMemberFirstActivity)
        pd.show()
        if (Functions.isConnectingToInternet(this@AddMemberFirstActivity)) {

            lifecycleScope.launch {
                val job1 = async { myRelationship() }
                val job2 = async { myOccupation() }
                val job3 = async { myVibhag() }

                val result1 = job1.await()
                val result2 = job2.await()
                val result3 = job3.await()
                // Update the UI with the results
                DebugLog.d("CORO - Results await 5: $result1, $result2, $result3")
                pd.dismiss()
            }

        } else {
            Toast.makeText(
                this@AddMemberFirstActivity,
                resources.getString(R.string.no_connection),
                Toast.LENGTH_SHORT
            ).show()
            pd.dismiss()
        }
    }

    private fun openDatePickerForDOB() {
        calendar = Calendar.getInstance()
        if (edit_dateofbirth.text?.isNotEmpty() == true) {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val initialDate = dateFormat.parse(edit_dateofbirth.text.toString())
            calendar.time = initialDate
        } else {
            calendar.add(Calendar.YEAR, -3)
        }

        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)

        age_layout.visibility = View.GONE

        val dialog = DatePickerDialog(this, { _, year, month, day_of_month ->
            calendar[Calendar.YEAR] = year
            calendar[Calendar.MONTH] = month //+ 1
            calendar[Calendar.DAY_OF_MONTH] = day_of_month
            val myFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
            edit_dateofbirth.setText(sdf.format(calendar.time))

            val today = Calendar.getInstance()
            val age: Int = today.get(Calendar.YEAR) - calendar.get(Calendar.YEAR)

            if (age < 18) {
                //do something
                age_layout.visibility = View.VISIBLE
                age_text.text =
                    getString(R.string.under_age_18)
                AGE = "1"
            } else {
                age_layout.visibility = View.GONE
                Log.d("", "Age in year= " + age);
                AGE = "0"
            }

        }, (year), month, day)
        val maxDate = Calendar.getInstance()
        maxDate.add(Calendar.YEAR, -3)
        dialog.datePicker.maxDate = maxDate.timeInMillis
        dialog.show()
    }

    private fun setSelfInfoFromSession() {
        edit_firstname.setText(sessionManager.fetchFIRSTNAME())
        edit_middlename.setText(sessionManager.fetchMIDDLENAME())
        edit_surname.setText(sessionManager.fetchSURNAME())
        edit_username.setText(sessionManager.fetchUSERNAME())
        edit_email.setText(sessionManager.fetchUSEREMAIL())
        edit_dateofbirth.setText(sessionManager.fetchDOB())
    }

    /*Member Information View API*/
    private fun myMemberView(user_id: String, member_id: String) {
        val pd = CustomProgressBar(this@AddMemberFirstActivity)
        pd.show()
        val call: Call<Get_Single_Member_Record_Response> =
            MyHssApplication.instance!!.api.get_single_member_record(
                user_id, member_id
            )
        call.enqueue(object : Callback<Get_Single_Member_Record_Response> {
            override fun onResponse(
                call: Call<Get_Single_Member_Record_Response>,
                response: Response<Get_Single_Member_Record_Response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {

                        try {
                            var data_getprofile: List<Get_Single_Member_Record_Datum> =
                                ArrayList<Get_Single_Member_Record_Datum>()
                            data_getprofile = response.body()!!.data!!

                            sessionManager.saveFIRSTNAME(data_getprofile[0].firstName.toString())
                            sessionManager.saveMIDDLENAME(data_getprofile[0].middleName.toString())
                            sessionManager.saveSURNAME(data_getprofile[0].lastName.toString())
                            sessionManager.saveUSEREMAIL(data_getprofile[0].email.toString())
                            sessionManager.saveDOB(data_getprofile[0].dob.toString())
                            sessionManager.saveSHAKHANAME(data_getprofile[0].shakha.toString())
                            sessionManager.saveNAGARNAME(data_getprofile[0].nagar.toString())
                            sessionManager.saveVIBHAGNAME(data_getprofile[0].vibhag.toString())
                            sessionManager.saveADDRESS(
                                data_getprofile[0].buildingName.toString() + " " + data_getprofile[0].addressLine1.toString() + " " + data_getprofile[0].addressLine2.toString()
                            )
                            sessionManager.saveLineOne(data_getprofile[0].addressLine1.toString())
                            sessionManager.saveOCCUPATIONNAME(data_getprofile[0].occupation.toString())
                            sessionManager.saveSPOKKENLANGUAGE(data_getprofile[0].rootLanguage.toString())
                            sessionManager.saveMOBILENO(data_getprofile[0].mobile.toString())
                            if (!data_getprofile[0].landLine.toString().equals("null")) {
                                sessionManager.saveSECMOBILENO(data_getprofile[0].landLine.toString())
                            }
                            if (!data_getprofile[0].secondaryEmail.toString().equals("null")) {
                                sessionManager.saveSECEMAIL(data_getprofile[0].secondaryEmail.toString())
                            }
//                            sessionManager.saveSECMOBILENO(data_getprofile[0].landLine.toString())
                            sessionManager.saveGUAEMRNAME(data_getprofile[0].emergencyName.toString())
                            sessionManager.saveGUAEMRPHONE(data_getprofile[0].emergencyPhone.toString())
                            sessionManager.saveGUAEMREMAIL(data_getprofile[0].emergencyEmail.toString())
                            sessionManager.saveGUAEMRRELATIONSHIP(data_getprofile[0].emergencyRelatioship.toString())
                            sessionManager.saveDOHAVEMEDICAL(data_getprofile[0].medicalInformationDeclare.toString())

                            sessionManager.saveAGE(data_getprofile[0].memberAge.toString())
                            sessionManager.saveGENDER(data_getprofile[0].gender.toString())
                            sessionManager.saveCITY(data_getprofile[0].city.toString())
                            sessionManager.saveCOUNTRY(data_getprofile[0].country.toString())
                            sessionManager.savePOSTCODE(data_getprofile[0].postalCode.toString())

                            sessionManager.saveMEDICAL_OTHER_INFO(data_getprofile[0].medicalDetails.toString())
                            sessionManager.saveQUALIFICATIONAID(data_getprofile[0].isQualifiedInFirstAid.toString())
                            sessionManager.saveQUALIFICATION_VALUE(data_getprofile[0].first_aid_qualification_val.toString())
                            sessionManager.saveQUALIFICATION_VALUE_NAME(data_getprofile[0].first_aid_qualification_name.toString()) // value name
                            sessionManager.saveQUALIFICATION_PRO_BODY_RED_NO(data_getprofile[0].professional_body_registartion_number.toString())
                            sessionManager.saveQUALIFICATION_DATE(data_getprofile[0].dateOfFirstAidQualification.toString())
                            sessionManager.saveQUALIFICATION_FILE(data_getprofile[0].firstAidQualificationFile.toString())
                            sessionManager.saveQUALIFICATION_IS_DOC(data_getprofile[0].first_aid_qualification_is_doc.toString())
                            sessionManager.saveDIETARY(data_getprofile[0].firstAidQualificationFile.toString())
                            sessionManager.saveSTATE_IN_INDIA(data_getprofile[0].indianConnectionState.toString())

                            Log.d("Address", sessionManager.fetchADDRESS()!!)
                            Log.d("Username", sessionManager.fetchUSERNAME()!!)
                            Log.d("Shakha_tab", sessionManager.fetchSHAKHA_TAB()!!)

                            setSelfInfoFromSession()

                            if (sessionManager.fetchGENDER() == "Male") {
                                male_right_img.visibility = View.VISIBLE

                                male_view.setBackgroundResource(R.drawable.edit_primery_color_round)
                                female_view.setBackgroundResource(R.drawable.edittext_round)

                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                    male_txt.setTextColor(getColor(R.color.primaryColor))
                                }
                                male_right_img.setImageResource(R.drawable.righttikmark)
                                male_icon.setColorFilter(
                                    ContextCompat.getColor(
                                        this@AddMemberFirstActivity, R.color.primaryColor
                                    ), android.graphics.PorterDuff.Mode.MULTIPLY
                                )

                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                    female_txt.setTextColor(getColor(R.color.grayColorColor))
                                }
                                female_right.visibility = View.INVISIBLE
                                female_img.setColorFilter(
                                    ContextCompat.getColor(
                                        this@AddMemberFirstActivity, R.color.grayColorColor
                                    ), android.graphics.PorterDuff.Mode.MULTIPLY
                                )

                                GENDER = "M"
                            } else {
                                female_right.visibility = View.VISIBLE

                                female_view.setBackgroundResource(R.drawable.edit_pink_color_round)
                                male_view.setBackgroundResource(R.drawable.edittext_round)

                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                    male_txt.setTextColor(getColor(R.color.grayColorColor))
                                }
                                male_right_img.visibility = View.INVISIBLE
                                male_icon.setColorFilter(
                                    ContextCompat.getColor(
                                        this@AddMemberFirstActivity, R.color.grayColorColor
                                    ), android.graphics.PorterDuff.Mode.MULTIPLY
                                )

                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                    female_txt.setTextColor(getColor(R.color.pinkColor))
                                }
                                female_right.setImageResource(R.drawable.righttikmarkfemale)
                                female_img.setColorFilter(
                                    ContextCompat.getColor(
                                        this@AddMemberFirstActivity, R.color.pinkColor
                                    ), android.graphics.PorterDuff.Mode.MULTIPLY
                                )

                                GENDER = "F"
                            }

                            edit_realationship_txt.setSelection(
                                relationshipName.indexOf(
                                    data_getprofile[0].relationship
                                )
                            )

//                        edit_realationship_txt.setPositiveButton(data_getprofile[0].relationship)
                            if (data_getprofile[0].otherRelationship != "") {
                                edit_realationship_name.setText(data_getprofile[0].otherRelationship)
                            }
                            edit_occupation_select_other.setSelection(
                                OccupationName.indexOf(
                                    data_getprofile[0].occupation
                                )
                            )
//                        edit_occupation_name.setText(data_getprofile[0].o)
                            edit_vibhag_region.setSelection(VibhagName.indexOf(data_getprofile[0].vibhag))
                            edit_nagar_town.setSelection(NagarName.indexOf(data_getprofile[0].nagar))
                            edit_shakha_branch.setSelection(ShakhaName.indexOf(data_getprofile[0].shakha))


                        } catch (e: ArithmeticException) {
                            println(e)
                        } finally {
                            println("Profile")
                        }

                    } else {
                        Functions.displayMessage(
                            this@AddMemberFirstActivity, response.body()?.message
                        )
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@AddMemberFirstActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Get_Single_Member_Record_Response>, t: Throwable) {
                Toast.makeText(this@AddMemberFirstActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    /*Relationship API*/
    private suspend fun myRelationship() {
        try {
            val response = MyHssApplication.instance?.api?.getRelationship()

            if (response?.status == true) {
                val data_relationship = response.data.orEmpty()

                relationshipName = data_relationship.map { it.relationshipName.toString() }
                relationshipID = data_relationship.map { it.memberRelationshipId.toString() }

                val adapter = ArrayAdapter(
                    this@AddMemberFirstActivity,
                    android.R.layout.simple_spinner_dropdown_item,
                    relationshipName
                )
                edit_realationship_txt.adapter = adapter
                edit_realationship_txt.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            adapter: AdapterView<*>,
                            v: View,
                            position: Int,
                            id: Long
                        ) {
                            (adapter.getChildAt(0) as TextView).setTextColor(Color.BLACK)
                            RELATIONSHIP_ID = relationshipID[position]

                            if (RELATIONSHIP_ID == "5") {
                                realationship_other_view.visibility = View.VISIBLE
                                OTHER_RELATIONSHIP = edit_realationship_name.text.toString()
                            } else {
                                realationship_other_view.visibility = View.GONE
                                OTHER_RELATIONSHIP = ""
                            }
                        }

                        override fun onNothingSelected(arg0: AdapterView<*>?) {}
                    }
            } else {
                Functions.displayMessage(
                    this@AddMemberFirstActivity,
                    response?.message ?: "Unknown error"
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Functions.showAlertMessageWithOK(
                this@AddMemberFirstActivity,
                "Message",
                getString(R.string.some_thing_wrong)
            )
        }
    }

    /*Occupation API*/
    private suspend fun myOccupation() {
        try {
            val response = MyHssApplication.instance?.api?.get_occupation()
            if (response?.status == true) {
                val dataOccupationList = response.data.orEmpty()

                OccupationName = dataOccupationList.map { it.occupationName.toString() }
                OccupationID = dataOccupationList.map { it.occupationId.toString() }
                OccupationName += "Other"
                OccupationID += "-99"
                val adapter = ArrayAdapter(
                    this@AddMemberFirstActivity,
                    android.R.layout.simple_spinner_dropdown_item,
                    OccupationName
                )
                edit_occupation_select_other.adapter = adapter
                if (intent.getStringExtra("TYPE_SELF") != "self" && intent.getStringExtra("FAMILY") == "PROFILE") {
                    val selectedOccupation = sessionManager.fetchOCCUPATIONNAME()
                    val selectedOccupationIndex = OccupationName.indexOf(selectedOccupation)
                    if (selectedOccupationIndex != -1) {
                        edit_occupation_select_other.setSelection(selectedOccupationIndex)
                    }
                }
                edit_occupation_select_other.onItemSelectedListener =
                    object : OnItemSelectedListener {
                        override fun onItemSelected(
                            adapter: AdapterView<*>,
                            v: View,
                            position: Int,
                            id: Long
                        ) {
                            (adapter.getChildAt(0) as TextView).setTextColor(Color.BLACK)
                            Log.d("Name", OccupationName[position])
                            Log.d("Postion", OccupationID[position])
                            OCCUPATION_ID = OccupationID[position]
                            OCCUPATION_NAME = OccupationName[position]
                            occupation_other_view.visibility = View.GONE
                        }

                        override fun onNothingSelected(arg0: AdapterView<*>?) {
                        }
                    }
            } else {
                Functions.displayMessage(
                    this@AddMemberFirstActivity,
                    response?.message ?: "Unknown error"
                )
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Functions.showAlertMessageWithOK(
                this@AddMemberFirstActivity,
                "Message",
                getString(R.string.some_thing_wrong)
            )
        }
    }

    /*Vibhag API*/
    private suspend fun myVibhag() {
        try {
            val response = MyHssApplication.instance?.api?.get_vibhag()
            if (response?.status == true) {
                val dataVibhagList = response.data.orEmpty()

                VibhagName = dataVibhagList.map { it.chapterName.toString() }
                VibhagID = dataVibhagList.map { it.orgChapterId.toString() }

                val adapter = ArrayAdapter(
                    this@AddMemberFirstActivity,
                    android.R.layout.simple_spinner_dropdown_item,
                    VibhagName
                )
                edit_vibhag_region.adapter = adapter

                if (intent.getStringExtra("TYPE_SELF") != "self") {
                    val selectedVibhagName = sessionManager.fetchVIBHAGNAME()
                    val selectedVibhagIndex = VibhagName.indexOf(selectedVibhagName)
                    if (selectedVibhagIndex != -1) {
                        vibhag_select_default.visibility = View.VISIBLE
                        vibhag_select_default.text = selectedVibhagName
                        edit_vibhag_region.setSelection(selectedVibhagIndex)
                    }
                }

                edit_vibhag_region.onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onItemSelected(
                        adapter: AdapterView<*>,
                        v: View,
                        position: Int,
                        id: Long
                    ) {
                        (adapter.getChildAt(0) as TextView).setTextColor(Color.BLACK)
                        VIBHAG_ID = VibhagID[position]

                        if (Functions.isConnectingToInternet(this@AddMemberFirstActivity)) {
                            lifecycleScope.launch {
                                val job4 = async { myNagar(VIBHAG_ID) }
                                DebugLog.d("Coro for myVibhag: ${job4.await()}")

                            }
                        } else {
                            Toast.makeText(
                                this@AddMemberFirstActivity,
                                resources.getString(R.string.no_connection),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onNothingSelected(arg0: AdapterView<*>?) {
                    }
                }
            } else {
                Functions.displayMessage(
                    this@AddMemberFirstActivity,
                    response?.message ?: "Unknown error"
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Functions.showAlertMessageWithOK(
                this@AddMemberFirstActivity,
                "Message",
                getString(R.string.some_thing_wrong)
            )
        }
    }

    /*Nagar API*/
    private suspend fun myNagar(VIBHAG_ID: String) {
        try {
            val response =
                MyHssApplication.instance?.api?.get_nagar(VIBHAG_ID, sessionManager.fetchUserID()!!)

            if (response?.status == true) {
                val dataNagarList = response.data.orEmpty()

                NagarName = dataNagarList.map { it.chapterName.toString() }
                NagarID = dataNagarList.map { it.orgChapterId.toString() }

                val adapter = ArrayAdapter(
                    this@AddMemberFirstActivity,
                    android.R.layout.simple_spinner_dropdown_item,
                    NagarName
                )
                edit_nagar_town.adapter = adapter

                if (intent.getStringExtra("TYPE_SELF") != "self") {
                    val selectedNagarName = sessionManager.fetchNAGARNAME()
                    val selectedNagarIndex = NagarName.indexOf(selectedNagarName)
                    if (selectedNagarIndex != -1) {
                        nagar_select_default.visibility = View.VISIBLE
                        nagar_select_default.text = selectedNagarName
                        edit_nagar_town.setSelection(selectedNagarIndex)
                    }
                }

                edit_nagar_town.onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onItemSelected(
                        adapter: AdapterView<*>,
                        v: View,
                        position: Int,
                        id: Long
                    ) {
                        (adapter.getChildAt(0) as TextView).setTextColor(Color.BLACK)
                        NAGAR_ID = NagarID[position]

                        if (Functions.isConnectingToInternet(this@AddMemberFirstActivity)) {
                            lifecycleScope.launch {
//                                val job5 = async { myShakha(NAGAR_ID) }
//                                DebugLog.d("Coro myNagar ==>>  ${job5.await()}")
                                myShakha(NAGAR_ID)
                            }
                        } else {
                            Toast.makeText(
                                this@AddMemberFirstActivity,
                                resources.getString(R.string.no_connection),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }
            } else {
                Functions.displayMessage(
                    this@AddMemberFirstActivity,
                    response?.message ?: "Unknown error"
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Functions.showAlertMessageWithOK(
                this@AddMemberFirstActivity,
                "Message",
                getString(R.string.some_thing_wrong)
            )
        }


    }

    /*Shakha API*/
    private suspend fun myShakha(NAGAR_ID: String) {
        try {
            val response =
                MyHssApplication.instance?.api?.get_shakha(NAGAR_ID, sessionManager.fetchUserID()!!)
            if (response?.status == true) {
                val dataShakhaList = response.data.orEmpty()

                ShakhaName = dataShakhaList.map { it.getChapterName().toString() }
                ShakhaID = dataShakhaList.map { it.getOrgChapterId().toString() }

                val adapter = ArrayAdapter(
                    this@AddMemberFirstActivity,
                    android.R.layout.simple_spinner_dropdown_item,
                    ShakhaName
                )
                edit_shakha_branch.adapter = adapter

                if (intent.getStringExtra("TYPE_SELF") != "self") {
                    val selectedShakhaName = sessionManager.fetchSHAKHANAME()
                    val selectedShakhaIndex = ShakhaName.indexOf(selectedShakhaName)
                    if (selectedShakhaIndex != -1) {
                        shakha_select_default.visibility = View.VISIBLE
                        shakha_select_default.text = selectedShakhaName
                        SHAKHA_ID = sessionManager.fetchSHAKHAID()!!
                        edit_shakha_branch.setSelection(selectedShakhaIndex)
                    }
                }

                edit_shakha_branch.onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onItemSelected(
                        adapter: AdapterView<*>,
                        v: View,
                        position: Int,
                        id: Long
                    ) {
                        (adapter.getChildAt(0) as TextView).setTextColor(Color.BLACK)
                        SHAKHA_ID = ShakhaID[position]
                    }

                    override fun onNothingSelected(arg0: AdapterView<*>?) {
                    }
                }
            } else {
                Functions.displayMessage(
                    this@AddMemberFirstActivity,
                    response?.message ?: "Unknown error"
                )
            }


        } catch (e: Exception) {
            e.printStackTrace()
            Functions.showAlertMessageWithOK(
                this@AddMemberFirstActivity,
                "Message",
                getString(R.string.some_thing_wrong)
            )
        }
    }

    private fun UserNameCheck(username: String) {
        val call: Call<Get_Member_Check_Username_Exist_Response> =
            MyHssApplication.instance!!.api.get_member_check_username_exist(username)
        call.enqueue(object : Callback<Get_Member_Check_Username_Exist_Response> {
            override fun onResponse(
                call: Call<Get_Member_Check_Username_Exist_Response>,
                response: Response<Get_Member_Check_Username_Exist_Response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body()?.status!!) {
//                        til_username.isErrorEnabled = true
//                        til_username.error = response.body()?.message.toString()
//                        userNameValidError = response.body()?.message.toString()
//                        til_username.setErrorTextColor(ColorStateList.valueOf(Color.GREEN))
//                        til_username.setErrorIconDrawable(null)
                        isUserNameValid = true
                    } else {
                        til_username.error = response.body()?.message.toString()
                        userNameValidError = response.body()?.message.toString()
                        til_username.setErrorTextColor(ColorStateList.valueOf(Color.RED))
                        til_username.isErrorEnabled = true
                        til_username.setErrorIconDrawable(null)
                        edit_username.requestFocus()
                        isUserNameValid = false
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@AddMemberFirstActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
            }

            override fun onFailure(
                call: Call<Get_Member_Check_Username_Exist_Response>, t: Throwable
            ) {
                Toast.makeText(this@AddMemberFirstActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    fun CallUserNameMethod(userNameS: String) {
        UserNameCheck(userNameS)
    }

    fun CallNextMethod() {
        if (intent.getStringExtra("TYPE_SELF") != "self") { // profile or add family
            val i = Intent(this@AddMemberFirstActivity, AddMemberSecondActivity::class.java)
            i.putExtra("TITLENAME", header_title.text.toString())
            i.putExtra("FIRST_NAME", edit_firstname.text.toString())
            i.putExtra("MIDDLE_NAME", edit_middlename.text.toString())
            i.putExtra("LAST_NAME", edit_surname.text.toString())
            i.putExtra("USERNAME", edit_username.text.toString())
            i.putExtra("EMAIL", edit_email.text.toString())
            if (intent.getStringExtra("FAMILY") == "PROFILE") {
                i.putExtra("PASSWORD", sessionManager.fetchPASSWORD())
            } else {
                i.putExtra("PASSWORD", edit_password.text.toString())
            }
            i.putExtra("GENDER", GENDER)
            i.putExtra("DOB", edit_dateofbirth.text.toString())

            if (RELATIONSHIP_ID == "5") {
                i.putExtra("RELATIONSHIP", RELATIONSHIP_ID)
                i.putExtra("OTHER_RELATIONSHIP", OTHER_RELATIONSHIP)
            } else {
                i.putExtra("RELATIONSHIP", RELATIONSHIP_ID)
            }
            i.putExtra("OCCUPATION", OCCUPATION_ID)
            i.putExtra("OCCUPATION_NAME", OCCUPATION_NAME)
            i.putExtra("SHAKHA", SHAKHA_ID)
            i.putExtra("AGE", AGE)
            i.putExtra("IS_LINKED", "")
            i.putExtra("IS_SELF", "family")
            i.putExtra("TYPE", "family")
            i.putExtra("PARENT_MEMBER", sessionManager.fetchMEMBERID())
            i.putExtra("FAMILY", intent.getStringExtra("FAMILY"))
            startActivity(i)
        } else {
            val i = Intent(
                this@AddMemberFirstActivity, AddMemberSecondActivity::class.java
            )
            i.putExtra("TITLENAME", header_title.text.toString())
            i.putExtra("FIRST_NAME", edit_firstname.text.toString())
            i.putExtra("MIDDLE_NAME", edit_middlename.text.toString())
            i.putExtra("LAST_NAME", edit_surname.text.toString())
            i.putExtra("EMAIL", edit_email.text.toString())
            i.putExtra("GENDER", GENDER)
            i.putExtra("DOB", edit_dateofbirth.text.toString())
            i.putExtra("RELATIONSHIP", RELATIONSHIP_ID)
            i.putExtra("OCCUPATION", OCCUPATION_ID)
            i.putExtra("OCCUPATION_NAME", OCCUPATION_NAME)
            i.putExtra("SHAKHA", SHAKHA_ID)
            i.putExtra("AGE", AGE)
            i.putExtra("IS_LINKED", "")
            i.putExtra("IS_SELF", "self")
            i.putExtra("TYPE", "family")
            i.putExtra("PARENT_MEMBER", sessionManager.fetchMEMBERID())
            i.putExtra("FAMILY", intent.getStringExtra("FAMILY"))
            startActivity(i)
        }
    }
}