package com.uk.myhss.AddMember

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.DebugLog
import com.myhss.Utils.Functions
import com.myhss.dialog.DialogSearchableSpinner
import com.myhss.dialog.iDialogSearchableSpinner
import com.uk.myhss.Utils.SessionManager
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class AddMemberThirdActivity : AppCompatActivity(), iDialogSearchableSpinner {

    private lateinit var sessionManager: SessionManager
    var relationshipName: List<String> = ArrayList<String>()
    var relationshipID: List<String> = ArrayList<String>()
    private var REALTIONSHIP_ID: String = ""
    private var OTHER_EMERGENCY_RELATIONSHIP: String = ""
    private lateinit var edit_guardian_full_name: TextInputEditText
    private lateinit var edit_guardian_contact_number: TextInputEditText
    private lateinit var edit_emergency_realationship_name: TextInputEditText
    private lateinit var edit_guardian_email: TextInputEditText
    private lateinit var edit_guardian_relationship: TextView
    private lateinit var emergency_realationship_other_view: LinearLayout
    private lateinit var back_layout: LinearLayout
    private lateinit var next_layout: LinearLayout
    private lateinit var til_guardian_full_name: TextInputLayout
    private lateinit var til_guardian_number: TextInputLayout
    private lateinit var til_guardian_email: TextInputLayout
    private lateinit var til_guardian_relatives_name: TextInputLayout
    private lateinit var rootLayout: LinearLayout

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addmemberthird)

        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("AddMemberStep3VC")
        sessionManager.firebaseAnalytics.setUserProperty(
            "AddMemberStep3VC", "AddMemberThirdActivity"
        )

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        val back_arrow = findViewById<ImageView>(R.id.back_arrow)
        val header_title = findViewById<TextView>(R.id.header_title)

        edit_guardian_full_name = findViewById(R.id.edit_guardian_full_name)
        edit_guardian_contact_number = findViewById(R.id.edit_guardian_contact_number)
        edit_emergency_realationship_name = findViewById(R.id.edit_emergency_realationship_name)
        edit_guardian_email = findViewById(R.id.edit_guardian_email)

        edit_guardian_relationship = findViewById(R.id.edit_guardian_relationship)
        emergency_realationship_other_view = findViewById(R.id.emergency_realationship_other_view)
        back_layout = findViewById(R.id.back_layout)
        next_layout = findViewById(R.id.next_layout)
        til_guardian_full_name = findViewById(R.id.til_guardian_full_name)
        til_guardian_number = findViewById(R.id.til_guardian_number)
        til_guardian_email = findViewById(R.id.til_guardian_email)
        til_guardian_relatives_name = findViewById(R.id.til_guardian_relatives_name)
        rootLayout = findViewById(R.id.rootLayout)

        header_title.text = intent.getStringExtra("TITLENAME")

        val guardian_full_title = findViewById<TextView>(R.id.guardian_full_title)
        val guardian_full_name = findViewById<TextView>(R.id.guardian_full_name)
        val guardian_contact_number = findViewById<TextView>(R.id.guardian_contact_number)
        val guardian_email = findViewById<TextView>(R.id.guardian_email)
        val guardian_relationship = findViewById<TextView>(R.id.guardian_relationship)

        edit_guardian_full_name.hint = "Contact Name"
        guardian_full_title.text = "Emergency Information"
        guardian_full_name.text = "Contact Name"
        guardian_contact_number.text = "Contact Phone Number"
        guardian_email.text = "Contact Email"
        guardian_relationship.text = "Contact Relationship"

        if (intent.getStringExtra("AGE") != "1") {
            edit_guardian_full_name.hint = "Emergency Name"
            guardian_full_title.text = "Emergency Information"
            guardian_full_name.text = "Emergency Name"
            guardian_contact_number.text = "Emergency Phone Number"
            guardian_email.text = "Emergency Email"
            guardian_relationship.text = "Emergency Relationship"
            edit_guardian_relationship.text = "Select Emergency Relationship"

        } else {
            edit_guardian_full_name.hint = "Guardian name"
            guardian_full_title.text = "Guardian Information"
            guardian_full_name.text = "Guardian Name"
            guardian_contact_number.text = "Guardian Contact"
            guardian_email.text = "Guardian Email"
            guardian_relationship.text = "Guardian Relationship"
            edit_guardian_relationship.text = "Select Guardian Relationship"
        }

        back_arrow.setOnClickListener(DebouncedClickListener {
            finish()
        })

        back_layout.setOnClickListener(DebouncedClickListener {
            finish()
        })
        DebugLog.e("TEST =>>>  "+intent.getStringExtra("IS_SELF"))
        DebugLog.e("TEST 2=>>>  "+intent.getStringExtra("FAMILY"))
        DebugLog.e("TEST 3=>>>  "+intent.getStringExtra("TITLENAME"))
        if (intent.getStringExtra("IS_SELF") != "self") {
            if (intent.getStringExtra("FAMILY") == "PROFILE" && intent.getStringExtra("TITLENAME") == "Profile") {
                edit_guardian_full_name.setText(sessionManager.fetchGUAEMRNAME())
                edit_guardian_contact_number.setText(sessionManager.fetchGUAEMRPHONE())
                edit_guardian_email.setText(sessionManager.fetchGUAEMREMAIL())
                edit_guardian_relationship.text = relationshipName.indexOf(sessionManager.fetchGUAEMRRELATIONSHIP()).toString()
                if (sessionManager.fetchGUAEMRRELATIONSHIP() == "Other") {
                    emergency_realationship_other_view.visibility = View.VISIBLE
                    edit_emergency_realationship_name.setText(sessionManager.fetchGUAEMRRELATIONSHIP_OTHER())
                    OTHER_EMERGENCY_RELATIONSHIP = edit_emergency_realationship_name.text.toString()
                }
            }else{
                edit_guardian_full_name.setText(sessionManager.fetchFIRSTNAME()+" "+ sessionManager.fetchMIDDLENAME() + " "+ sessionManager.fetchSURNAME())
                edit_guardian_contact_number.setText(sessionManager.fetchMOBILENO())
                edit_guardian_email.setText(sessionManager.fetchUSEREMAIL())
            }
        }

        edit_guardian_contact_number.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                val text: String = edit_guardian_contact_number.getText().toString()
                val textLength: Int = edit_guardian_contact_number.text!!.length
                if (text.endsWith(" ") || text.endsWith(" ") || text.endsWith(" ")) return
                if (textLength == 1) {
                    edit_guardian_contact_number.setSelection(edit_guardian_contact_number.text!!.length)
                } else if (textLength == 4) {
                    edit_guardian_contact_number.setSelection(edit_guardian_contact_number.text!!.length)
                } else if (textLength == 5) {
                    edit_guardian_contact_number.setText(
                        StringBuilder(text).insert(
                            text.length - 1, " "
                        ).toString()
                    )
                    edit_guardian_contact_number.setSelection(edit_guardian_contact_number.text!!.length)
                } else if (textLength == 8) {
                    if (!text.contains(" ")) {
                        edit_guardian_contact_number.setText(
                            StringBuilder(text).insert(text.length - 1, " ").toString()
                        )
                        edit_guardian_contact_number.setSelection(edit_guardian_contact_number.text!!.length)
                    }
                } else if (textLength == 9) {
                    if (text.contains(" ")) {
                        edit_guardian_contact_number.setText(
                            StringBuilder(text).insert(text.length - 1, " ").toString()
                        )
                        edit_guardian_contact_number.setSelection(edit_guardian_contact_number.text!!.length)
                    }
                } /*else if (textLength == 14) {
                    if (text.contains(" ")) {
                        edit_primary_contact_number.setText(
                            StringBuilder(text).insert(text.length - 1, " ").toString()
                        )
                        edit_primary_contact_number.setSelection(edit_primary_contact_number.text!!.length)
                    }
                }*/
            }
        })

        edit_guardian_full_name.doOnTextChanged { text, start, before, count ->
            til_guardian_full_name.isErrorEnabled = false
        }

        edit_guardian_contact_number.doOnTextChanged { text, start, before, count ->
            til_guardian_number.isErrorEnabled = false
        }
        edit_guardian_email.doOnTextChanged { text, start, before, count ->
            til_guardian_email.isErrorEnabled = false
        }
        edit_guardian_full_name.doOnTextChanged { text, start, before, count ->
            til_guardian_full_name.isErrorEnabled = false
        }
        edit_emergency_realationship_name.doOnTextChanged { text, start, before, count ->
            til_guardian_relatives_name.isErrorEnabled = false
        }

        next_layout.setOnClickListener(DebouncedClickListener {

            if (REALTIONSHIP_ID == "5") {
                OTHER_EMERGENCY_RELATIONSHIP = edit_emergency_realationship_name.text.toString()
            } else {
                OTHER_EMERGENCY_RELATIONSHIP = ""
            }

            if (intent.getStringExtra("AGE") == "1") {// age under 18

                if (edit_guardian_full_name.text.toString().isEmpty()) {
                    til_guardian_full_name.error = "Please Enter the Guardian Name"
                    til_guardian_full_name.isErrorEnabled = true
                    edit_guardian_full_name.requestFocus()
                    return@DebouncedClickListener
                } else if (edit_guardian_contact_number.text.toString().isEmpty()) {
                    til_guardian_number.error = "Please Enter the Guardian Contact number"
                    til_guardian_number.isErrorEnabled = true
                    edit_guardian_contact_number.requestFocus()
                    return@DebouncedClickListener
                } else if (edit_guardian_email.text.toString().isEmpty()) {
                    til_guardian_email.error = "Please Enter the Guardian Contact Email"
                    til_guardian_email.isErrorEnabled = true
                    edit_guardian_email.requestFocus()
                    return@DebouncedClickListener
                } else if (!Patterns.EMAIL_ADDRESS.matcher(edit_guardian_email.text.toString())
                        .matches()
                ) {
                    til_guardian_email.error = "Please Enter the valid Guardian Email"
                    til_guardian_email.isErrorEnabled = true
                    edit_guardian_email.requestFocus()
                    return@DebouncedClickListener
                } else if (REALTIONSHIP_ID == "") {
                    Snackbar.make(rootLayout, "Please select the Guardian relationship name", Snackbar.LENGTH_SHORT).show()
                    return@DebouncedClickListener
                } else if (REALTIONSHIP_ID == "5" && edit_emergency_realationship_name.text.toString()
                        .isEmpty()
                ) {
                    til_guardian_relatives_name.error =
                        "Please Enter the Guardian relationship name"
                    til_guardian_relatives_name.isErrorEnabled = true
                    edit_emergency_realationship_name.requestFocus()
                    return@DebouncedClickListener
                } else {
                    if (intent.getStringExtra("IS_SELF") != "self") { // Profile or Add Family(under age 18)
                        val i =
                            Intent(this@AddMemberThirdActivity, AddMemberForthActivity::class.java)
                        i.putExtra("TITLENAME", header_title.text.toString())
                        i.putExtra("FIRST_NAME", intent.getStringExtra("FIRST_NAME"))
                        i.putExtra("MIDDLE_NAME", intent.getStringExtra("MIDDLE_NAME"))
                        i.putExtra("LAST_NAME", intent.getStringExtra("LAST_NAME"))
                        i.putExtra("USERNAME", intent.getStringExtra("USERNAME"))
                        i.putExtra("EMAIL", intent.getStringExtra("EMAIL"))
                        i.putExtra("PASSWORD", intent.getStringExtra("PASSWORD"))
                        i.putExtra("GENDER", intent.getStringExtra("GENDER"))
                        i.putExtra("DOB", intent.getStringExtra("DOB"))

                        if (intent.getStringExtra("OTHER_RELATIONSHIP") == "") {
                            i.putExtra("RELATIONSHIP", intent.getStringExtra("RELATIONSHIP"))
                        } else {
                            i.putExtra("RELATIONSHIP", intent.getStringExtra("RELATIONSHIP"))
                            i.putExtra(
                                "OTHER_RELATIONSHIP", intent.getStringExtra("OTHER_RELATIONSHIP")
                            )
                        }

                        if (intent.getStringExtra("OCCUPATION_NAME") == "") {
                            i.putExtra("OCCUPATION", intent.getStringExtra("OCCUPATION"))
                        } else {
                            i.putExtra("OCCUPATION", intent.getStringExtra("OCCUPATION"))
                            i.putExtra("OCCUPATION_NAME", intent.getStringExtra("OCCUPATION_NAME"))
                        }

                        i.putExtra("SHAKHA", intent.getStringExtra("SHAKHA"))
                        i.putExtra("AGE", intent.getStringExtra("AGE"))
                        i.putExtra("IS_LINKED", intent.getStringExtra("IS_LINKED"))
                        i.putExtra("IS_SELF", intent.getStringExtra("IS_SELF"))
                        i.putExtra("TYPE", intent.getStringExtra("TYPE"))
                        i.putExtra("PARENT_MEMBER", intent.getStringExtra("PARENT_MEMBER"))
                        i.putExtra("MOBILE", intent.getStringExtra("MOBILE"))
                        i.putExtra("LAND_LINE", intent.getStringExtra("LAND_LINE"))
                        i.putExtra("SECOND_EMAIL", intent.getStringExtra("SECOND_EMAIL"))
                        i.putExtra("POST_CODE", intent.getStringExtra("POST_CODE"))
                        i.putExtra("BUILDING_NAME", intent.getStringExtra("BUILDING_NAME"))
                        i.putExtra("ADDRESS_ONE", intent.getStringExtra("ADDRESS_ONE"))
                        i.putExtra("ADDRESS_TWO", intent.getStringExtra("ADDRESS_TWO"))
                        i.putExtra("POST_TOWN", intent.getStringExtra("POST_TOWN"))
                        i.putExtra("COUNTY", intent.getStringExtra("COUNTY"))
                        i.putExtra("EMERGENCY_NAME", edit_guardian_full_name.text.toString())
                        i.putExtra("EMERGENCY_PHONE", edit_guardian_contact_number.text.toString())
                        i.putExtra("EMERGENCY_EMAIL", edit_guardian_email.text.toString())
//                        i.putExtra("EMERGENCY_RELATIONSHIP", REALTIONSHIP_ID)
//                        i.putExtra("OTHER_EMERGENCY_RELATIONSHIP", REALTIONSHIP_ID)

                        if (REALTIONSHIP_ID == "5") {

                            i.putExtra("EMERGENCY_RELATIONSHIP", REALTIONSHIP_ID)
                            i.putExtra(
                                "OTHER_EMERGENCY_RELATIONSHIP", OTHER_EMERGENCY_RELATIONSHIP
                            )
                        } else {
                            i.putExtra("EMERGENCY_RELATIONSHIP", REALTIONSHIP_ID)
                        }

                        i.putExtra("FAMILY", intent.getStringExtra("FAMILY"))
                        startActivity(i)
                    } else { // Add self(under age 18)
                        val i =
                            Intent(this@AddMemberThirdActivity, AddMemberForthActivity::class.java)
                        i.putExtra("TITLENAME", header_title.text.toString())
                        i.putExtra("FIRST_NAME", intent.getStringExtra("FIRST_NAME"))
                        i.putExtra("MIDDLE_NAME", intent.getStringExtra("MIDDLE_NAME"))
                        i.putExtra("LAST_NAME", intent.getStringExtra("LAST_NAME"))
//                i.putExtra("USERNAME", intent.getStringExtra("USERNAME"))
                        i.putExtra("EMAIL", intent.getStringExtra("EMAIL"))
//                i.putExtra("PASSWORD", intent.getStringExtra("PASSWORD"))
                        i.putExtra("GENDER", intent.getStringExtra("GENDER"))
                        i.putExtra("DOB", intent.getStringExtra("DOB"))

//                        if (intent.getStringExtra("OTHER_RELATIONSHIP") == "") {
//                            i.putExtra("RELATIONSHIP", intent.getStringExtra("RELATIONSHIP"))
//                        } else {
//                            i.putExtra("RELATIONSHIP", intent.getStringExtra("RELATIONSHIP"))
//                            i.putExtra(
//                                "OTHER_RELATIONSHIP",
//                                intent.getStringExtra("OTHER_RELATIONSHIP")
//                            )
//                        }

                        if (intent.getStringExtra("OCCUPATION_NAME") == "") {
                            i.putExtra("OCCUPATION", intent.getStringExtra("OCCUPATION"))
                        } else {
                            i.putExtra("OCCUPATION", intent.getStringExtra("OCCUPATION"))
                            i.putExtra("OCCUPATION_NAME", intent.getStringExtra("OCCUPATION_NAME"))
                        }

                        i.putExtra("SHAKHA", intent.getStringExtra("SHAKHA"))
                        i.putExtra("AGE", intent.getStringExtra("AGE"))
                        i.putExtra("IS_LINKED", intent.getStringExtra("IS_LINKED"))
                        i.putExtra("IS_SELF", intent.getStringExtra("IS_SELF"))
                        i.putExtra("TYPE", intent.getStringExtra("TYPE"))
                        i.putExtra("PARENT_MEMBER", intent.getStringExtra("PARENT_MEMBER"))
                        i.putExtra("MOBILE", intent.getStringExtra("MOBILE"))
                        i.putExtra("LAND_LINE", intent.getStringExtra("LAND_LINE"))
                        i.putExtra("SECOND_EMAIL", intent.getStringExtra("SECOND_EMAIL"))
                        i.putExtra("POST_CODE", intent.getStringExtra("POST_CODE"))
                        i.putExtra("BUILDING_NAME", intent.getStringExtra("BUILDING_NAME"))
                        i.putExtra("ADDRESS_ONE", intent.getStringExtra("ADDRESS_ONE"))
                        i.putExtra("ADDRESS_TWO", intent.getStringExtra("ADDRESS_TWO"))
                        i.putExtra("POST_TOWN", intent.getStringExtra("POST_TOWN"))
                        i.putExtra("COUNTY", intent.getStringExtra("COUNTY"))
                        i.putExtra("EMERGENCY_NAME", edit_guardian_full_name.text.toString())
                        i.putExtra("EMERGENCY_PHONE", edit_guardian_contact_number.text.toString())
                        i.putExtra("EMERGENCY_EMAIL", edit_guardian_email.text.toString())
//                        i.putExtra("EMERGENCY_RELATIONSHIP", REALTIONSHIP_ID)
//                        i.putExtra("OTHER_EMERGENCY_RELATIONSHIP", REALTIONSHIP_ID)

                        if (REALTIONSHIP_ID == "5") {
                            i.putExtra("EMERGENCY_RELATIONSHIP", REALTIONSHIP_ID)
                            i.putExtra(
                                "OTHER_EMERGENCY_RELATIONSHIP", OTHER_EMERGENCY_RELATIONSHIP
                            )
                        } else {
                            i.putExtra("EMERGENCY_RELATIONSHIP", REALTIONSHIP_ID)
                        }

                        i.putExtra("FAMILY", intent.getStringExtra("FAMILY"))
                        startActivity(i)
                    }
                }
            } else { // age above 18
                if (edit_guardian_full_name.text.toString().isEmpty()) {
                    til_guardian_full_name.error = "Please Enter the Emergency Contact Name"
                    til_guardian_full_name.isErrorEnabled = true
                    edit_guardian_full_name.requestFocus()
                    return@DebouncedClickListener
                } else if (edit_guardian_contact_number.text.toString().isEmpty()) {
                    til_guardian_number.error = "Please Enter the Emergency Contact Number"
                    til_guardian_number.isErrorEnabled = true
                    edit_guardian_contact_number.requestFocus()
                    return@DebouncedClickListener
                } else if (edit_guardian_email.text.toString().isEmpty()) {
                    til_guardian_email.error = "Please Enter the Emergency Contact Email"
                    til_guardian_email.isErrorEnabled = true
                    edit_guardian_email.requestFocus()
                    return@DebouncedClickListener
                } else if (!Patterns.EMAIL_ADDRESS.matcher(edit_guardian_email.text.toString())
                        .matches()
                ) {
                    til_guardian_email.error = "Please Enter the Valid Emergency Contact Email"
                    til_guardian_email.isErrorEnabled = true
                    edit_guardian_email.requestFocus()
                    return@DebouncedClickListener
                }else if (REALTIONSHIP_ID == "") {
                    Snackbar.make(rootLayout, "Please select the Guardian relationship name", Snackbar.LENGTH_SHORT).show()
                    return@DebouncedClickListener
                } else if (REALTIONSHIP_ID == "5" && edit_emergency_realationship_name.text.toString()
                        .isEmpty()
                ) {
                    til_guardian_relatives_name.error =
                        "Please Enter the Emergency relationship name"
                    til_guardian_relatives_name.isErrorEnabled = true
                    edit_emergency_realationship_name.requestFocus()
                    return@DebouncedClickListener
                } else {
                    if (intent.getStringExtra("IS_SELF") != "self") { // profile or Add family(age above 18)
                        val i =
                            Intent(this@AddMemberThirdActivity, AddMemberForthActivity::class.java)
                        i.putExtra("TITLENAME", header_title.text.toString())
                        i.putExtra("FIRST_NAME", intent.getStringExtra("FIRST_NAME"))
                        i.putExtra("MIDDLE_NAME", intent.getStringExtra("MIDDLE_NAME"))
                        i.putExtra("LAST_NAME", intent.getStringExtra("LAST_NAME"))
                        i.putExtra("USERNAME", intent.getStringExtra("USERNAME"))
                        i.putExtra("EMAIL", intent.getStringExtra("EMAIL"))
                        i.putExtra("PASSWORD", intent.getStringExtra("PASSWORD"))
                        i.putExtra("GENDER", intent.getStringExtra("GENDER"))
                        i.putExtra("DOB", intent.getStringExtra("DOB"))
//                        i.putExtra("RELATIONSHIP", intent.getStringExtra("RELATIONSHIP"))
//                        i.putExtra("OTHER_RELATIONSHIP", intent.getStringExtra("OTHER_RELATIONSHIP"))
//                        i.putExtra("OCCUPATION", intent.getStringExtra("OCCUPATION"))
//                        i.putExtra("OCCUPATION_NAME", intent.getStringExtra("OCCUPATION_NAME"))

                        if (intent.getStringExtra("OTHER_RELATIONSHIP") == "") {
                            i.putExtra("RELATIONSHIP", intent.getStringExtra("RELATIONSHIP"))
                        } else {
                            i.putExtra("RELATIONSHIP", intent.getStringExtra("RELATIONSHIP"))
                            i.putExtra(
                                "OTHER_RELATIONSHIP",
                                intent.getStringExtra("OTHER_RELATIONSHIP")
                            )
                        }

                        if (intent.getStringExtra("OCCUPATION_NAME") == "") {
                            i.putExtra("OCCUPATION", intent.getStringExtra("OCCUPATION"))
                        } else {
                            i.putExtra("OCCUPATION", intent.getStringExtra("OCCUPATION"))
                            i.putExtra("OCCUPATION_NAME", intent.getStringExtra("OCCUPATION_NAME"))
                        }

                        i.putExtra("SHAKHA", intent.getStringExtra("SHAKHA"))
                        i.putExtra("AGE", intent.getStringExtra("AGE"))
                        i.putExtra("IS_LINKED", intent.getStringExtra("IS_LINKED"))
                        i.putExtra("IS_SELF", intent.getStringExtra("IS_SELF"))
                        i.putExtra("TYPE", intent.getStringExtra("TYPE"))
                        i.putExtra("PARENT_MEMBER", intent.getStringExtra("PARENT_MEMBER"))
                        i.putExtra("MOBILE", intent.getStringExtra("MOBILE"))
                        i.putExtra("LAND_LINE", intent.getStringExtra("LAND_LINE"))
                        i.putExtra("POST_CODE", intent.getStringExtra("POST_CODE"))
                        i.putExtra("BUILDING_NAME", intent.getStringExtra("BUILDING_NAME"))
                        i.putExtra("ADDRESS_ONE", intent.getStringExtra("ADDRESS_ONE"))
                        i.putExtra("ADDRESS_TWO", intent.getStringExtra("ADDRESS_TWO"))
                        i.putExtra("POST_TOWN", intent.getStringExtra("POST_TOWN"))
                        i.putExtra("COUNTY", intent.getStringExtra("COUNTY"))
                        i.putExtra("EMERGENCY_NAME", edit_guardian_full_name.text.toString())
                        i.putExtra("EMERGENCY_PHONE", edit_guardian_contact_number.text.toString())
                        i.putExtra("EMERGENCY_EMAIL", edit_guardian_email.text.toString())
//                        i.putExtra("EMERGENCY_RELATIONSHIP", REALTIONSHIP_ID)
//                        i.putExtra("OTHER_EMERGENCY_RELATIONSHIP", REALTIONSHIP_ID)

                        if (REALTIONSHIP_ID == "5") {
                            i.putExtra("EMERGENCY_RELATIONSHIP", REALTIONSHIP_ID)
                            i.putExtra(
                                "OTHER_EMERGENCY_RELATIONSHIP", OTHER_EMERGENCY_RELATIONSHIP
                            )
                        } else {
                            i.putExtra("EMERGENCY_RELATIONSHIP", REALTIONSHIP_ID)
                        }

                        i.putExtra("FAMILY", intent.getStringExtra("FAMILY"))
                        startActivity(i)
                    } else { // add self(age above 18)
                        val i =
                            Intent(this@AddMemberThirdActivity, AddMemberForthActivity::class.java)
                        i.putExtra("TITLENAME", header_title.text.toString())
                        i.putExtra("FIRST_NAME", intent.getStringExtra("FIRST_NAME"))
                        i.putExtra("MIDDLE_NAME", intent.getStringExtra("MIDDLE_NAME"))
                        i.putExtra("LAST_NAME", intent.getStringExtra("LAST_NAME"))
//                i.putExtra("USERNAME", intent.getStringExtra("USERNAME"))
                        i.putExtra("EMAIL", intent.getStringExtra("EMAIL"))
//                i.putExtra("PASSWORD", intent.getStringExtra("PASSWORD"))
                        i.putExtra("GENDER", intent.getStringExtra("GENDER"))
                        i.putExtra("DOB", intent.getStringExtra("DOB"))

                        if (intent.getStringExtra("OTHER_RELATIONSHIP") == "") {
                            i.putExtra("RELATIONSHIP", intent.getStringExtra("RELATIONSHIP"))
                        } else {
                            i.putExtra("RELATIONSHIP", intent.getStringExtra("RELATIONSHIP"))
                            i.putExtra(
                                "OTHER_RELATIONSHIP", intent.getStringExtra("OTHER_RELATIONSHIP")
                            )
                        }

                        if (intent.getStringExtra("OCCUPATION_NAME") == "") {
                            i.putExtra("OCCUPATION", intent.getStringExtra("OCCUPATION"))
                        } else {
                            i.putExtra("OCCUPATION", intent.getStringExtra("OCCUPATION"))
                            i.putExtra("OCCUPATION_NAME", intent.getStringExtra("OCCUPATION_NAME"))
                        }

                        i.putExtra("SHAKHA", intent.getStringExtra("SHAKHA"))
                        i.putExtra("AGE", intent.getStringExtra("AGE"))
                        i.putExtra("IS_LINKED", intent.getStringExtra("IS_LINKED"))
                        i.putExtra("IS_SELF", intent.getStringExtra("IS_SELF"))
                        i.putExtra("TYPE", intent.getStringExtra("TYPE"))
                        i.putExtra("PARENT_MEMBER", intent.getStringExtra("PARENT_MEMBER"))
                        i.putExtra("MOBILE", intent.getStringExtra("MOBILE"))
                        i.putExtra("LAND_LINE", intent.getStringExtra("LAND_LINE"))
                        i.putExtra("POST_CODE", intent.getStringExtra("POST_CODE"))
                        i.putExtra("BUILDING_NAME", intent.getStringExtra("BUILDING_NAME"))
                        i.putExtra("ADDRESS_ONE", intent.getStringExtra("ADDRESS_ONE"))
                        i.putExtra("ADDRESS_TWO", intent.getStringExtra("ADDRESS_TWO"))
                        i.putExtra("POST_TOWN", intent.getStringExtra("POST_TOWN"))
                        i.putExtra("COUNTY", intent.getStringExtra("COUNTY"))
                        i.putExtra("EMERGENCY_NAME", edit_guardian_full_name.text.toString())
                        i.putExtra("EMERGENCY_PHONE", edit_guardian_contact_number.text.toString())
                        i.putExtra("EMERGENCY_EMAIL", edit_guardian_email.text.toString())
//                        i.putExtra("EMERGENCY_RELATIONSHIP", REALTIONSHIP_ID)
//                        i.putExtra("OTHER_EMERGENCY_RELATIONSHIP", REALTIONSHIP_ID)

                        if (REALTIONSHIP_ID == "5") {
                            i.putExtra("EMERGENCY_RELATIONSHIP", REALTIONSHIP_ID)
                            i.putExtra(
                                "OTHER_EMERGENCY_RELATIONSHIP", OTHER_EMERGENCY_RELATIONSHIP
                            )
                        } else {
                            i.putExtra("EMERGENCY_RELATIONSHIP", REALTIONSHIP_ID)
                        }

                        i.putExtra("FAMILY", intent.getStringExtra("FAMILY"))
                        startActivity(i)
                    }
                }
            }
        })

        if (Functions.isConnectingToInternet(this@AddMemberThirdActivity)) {
            lifecycleScope.launch {
                val job1 = async { myRelationship() }
                DebugLog.e("Coro 1 :::: $${job1.await()}")
            }
        } else {
            Toast.makeText(
                this@AddMemberThirdActivity,
                resources.getString(R.string.no_connection),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /*Relationship API*/
    private suspend fun myRelationship() {
        val pd = CustomProgressBar(this@AddMemberThirdActivity)
        pd.show()
        try {
            val response = MyHssApplication.instance?.api?.getRelationship()
            if (response?.status == true) {
                val data_relationship = response.data.orEmpty()
                relationshipName = data_relationship.map { it.relationshipName.toString() }
                relationshipID = data_relationship.map { it.memberRelationshipId.toString() }
                if (intent.getStringExtra("IS_SELF") != "self" && intent.getStringExtra("FAMILY") == "PROFILE") {
                    if (sessionManager.fetchGUAEMRRELATIONSHIP().toString() in relationshipName) {
                        val index = relationshipName.indexOf(
                            sessionManager.fetchGUAEMRRELATIONSHIP().toString()
                        )
                        searchableItemSelectedData(
                            "1",
                            relationshipName[index],
                            relationshipID[index]
                        )
                    }
                }
                edit_guardian_relationship.setOnClickListener(DebouncedClickListener {
                    openSearchableSpinnerDialog(
                        "1",
                        "Select Guardian Relationship",
                        relationshipName,
                        relationshipID
                    )
                })
            } else {
                Functions.displayMessage(
                    this@AddMemberThirdActivity,
                    response?.message ?: "Unknown error"
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Functions.showAlertMessageWithOK(
                this@AddMemberThirdActivity,
                "Message",
                getString(R.string.some_thing_wrong)
            )
        } finally {
            pd.dismiss()
        }
    }

    override fun searchableItemSelectedData(stype: String, sItemName: String, sItemID: String) {
        DebugLog.e("Type : $stype , ItemName : $sItemName , ItemID : $sItemID")
        edit_guardian_relationship.text = sItemName
        REALTIONSHIP_ID = sItemID
        if (REALTIONSHIP_ID == "5") {
            emergency_realationship_other_view.visibility = View.VISIBLE
            OTHER_EMERGENCY_RELATIONSHIP = edit_emergency_realationship_name.text.toString()
        } else {
            emergency_realationship_other_view.visibility = View.GONE
            OTHER_EMERGENCY_RELATIONSHIP = ""
        }
    }
    private fun openSearchableSpinnerDialog(
        sType: String,
        sTitle: String,
        ItemName: List<String>,
        ItemID: List<String>
    ) {
        val fragment = supportFragmentManager.findFragmentByTag("DialogSearchableSpinner")
        if (fragment == null) {
            val dialogSearch = DialogSearchableSpinner.newInstance(
                this,
                sType,
                sTitle,
                ItemName,
                ItemID
            )
            dialogSearch.show(supportFragmentManager, "DialogSearchableSpinner")
        }
    }
}