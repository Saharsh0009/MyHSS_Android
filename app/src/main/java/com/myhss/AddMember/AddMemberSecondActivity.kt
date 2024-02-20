package com.uk.myhss.AddMember

//import com.uk.myhss.Login_Registration.Model.RetrofitClient

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.uk.myhss.AddMember.Pincode.Get_Pincode_Response
import com.uk.myhss.AddMember.Pincode.Summary_Address
import com.uk.myhss.AddMember.PincodeAddress.Get_PincodeAddress_Response
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.DebugLog
import com.myhss.Utils.Functions
import com.myhss.appConstants.AppParam
import com.myhss.dialog.DialogSearchableSpinner
import com.myhss.dialog.iDialogSearchableSpinner
import com.uk.myhss.Utils.SessionManager
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class AddMemberSecondActivity : AppCompatActivity(), iDialogSearchableSpinner {

    private lateinit var sessionManager: SessionManager
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var edit_find_address: TextInputEditText
    private lateinit var edit_secondary_contact_number: TextInputEditText
    private lateinit var edit_secondary_email: TextInputEditText
    private lateinit var edit_primary_contact_number: TextInputEditText
    private lateinit var edit_town_city: TextInputEditText
    private lateinit var edit_address_line2: TextInputEditText
    private lateinit var edit_address_line1: TextInputEditText
    private lateinit var edit_building_name: TextInputEditText
    private lateinit var til_primary_number: TextInputLayout
    private lateinit var til_second_number: TextInputLayout
    private lateinit var til_second_email: TextInputLayout
    private lateinit var til_postcode: TextInputLayout
    private lateinit var til_edit_building_name: TextInputLayout
    private lateinit var til_edit_address_line1: TextInputLayout
    private lateinit var til_edit_address_line2: TextInputLayout
    private lateinit var til_edit_town_city: TextInputLayout
    private lateinit var find_address: ImageView

    private lateinit var edit_select_address: TextView

//    private lateinit var select_address: RelativeLayout

    private lateinit var next_layout: LinearLayout
    private lateinit var back_layout: LinearLayout
    private lateinit var rootLayout: LinearLayout
    private lateinit var full_address_view: LinearLayout

    var Pincode: List<String> = ArrayList<String>()
    var PincodeID: List<String> = ArrayList<String>()
    private var County: String = ""
    private var PINCODE_ID: String = ""

    @SuppressLint("NewApi", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addmembersecond)

        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("AddMemberStep2VC")
        sessionManager.firebaseAnalytics.setUserProperty(
            "AddMemberStep2VC", "AddMemberSecondActivity"
        )

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        sharedPreferences = getSharedPreferences("production", Context.MODE_PRIVATE)

        val back_arrow = findViewById<ImageView>(R.id.back_arrow)
        val header_title = findViewById<TextView>(R.id.header_title)

        edit_find_address = findViewById(R.id.edit_find_address)
        edit_secondary_contact_number = findViewById(R.id.edit_secondary_contact_number)
        edit_secondary_email = findViewById(R.id.edit_secondary_email)
        edit_primary_contact_number = findViewById(R.id.edit_primary_contact_number)

        rootLayout = findViewById(R.id.rootLayout)
        next_layout = findViewById(R.id.next_layout)
        back_layout = findViewById(R.id.back_layout)
        full_address_view = findViewById(R.id.full_address_view)

        edit_town_city = findViewById(R.id.edit_town_city)
        edit_address_line2 = findViewById(R.id.edit_address_line2)
        edit_address_line1 = findViewById(R.id.edit_address_line1)
        edit_building_name = findViewById(R.id.edit_building_name)

        edit_select_address = findViewById(R.id.edit_select_address)
//        select_address = findViewById(R.id.select_address)

        find_address = findViewById(R.id.find_address)
        til_primary_number = findViewById(R.id.til_primary_number)
        til_second_number = findViewById(R.id.til_second_number)
        til_second_email = findViewById(R.id.til_second_email)
        til_postcode = findViewById(R.id.til_postcode)
        til_edit_building_name = findViewById(R.id.til_edit_building_name)
        til_edit_address_line1 = findViewById(R.id.til_edit_address_line1)
        til_edit_address_line2 = findViewById(R.id.til_edit_address_line2)
        til_edit_town_city = findViewById(R.id.til_edit_town_city)

        header_title.text = intent.getStringExtra("TITLENAME")
        edit_primary_contact_number.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                val text: String = edit_primary_contact_number.getText().toString()
                val textLength: Int = edit_primary_contact_number.text!!.length
                if (text.endsWith(" ") || text.endsWith(" ") || text.endsWith(" ")) return
                if (textLength == 1) {
                    edit_primary_contact_number.setSelection(edit_primary_contact_number.text!!.length)
                } else if (textLength == 4) {
                    edit_primary_contact_number.setSelection(edit_primary_contact_number.text!!.length)
                } else if (textLength == 5) {
                    edit_primary_contact_number.setText(
                        StringBuilder(text).insert(
                            text.length - 1, " "
                        ).toString()
                    )
                    edit_primary_contact_number.setSelection(edit_primary_contact_number.text!!.length)
                } else if (textLength == 8) {
                    if (!text.contains(" ")) {
                        edit_primary_contact_number.setText(
                            StringBuilder(text).insert(text.length - 1, " ").toString()
                        )
                        edit_primary_contact_number.setSelection(edit_primary_contact_number.text!!.length)
                    }
                } else if (textLength == 9) {
                    if (text.contains(" ")) {
                        edit_primary_contact_number.setText(
                            StringBuilder(text).insert(text.length - 1, " ").toString()
                        )
                        edit_primary_contact_number.setSelection(edit_primary_contact_number.text!!.length)
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

        edit_secondary_contact_number.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // TODO Auto-generated method stub
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
                val text: String = edit_secondary_contact_number.getText().toString()
                val textLength: Int = edit_secondary_contact_number.text!!.length
                if (text.endsWith(" ") || text.endsWith(" ") || text.endsWith(" ")) return
                if (textLength == 1) {

                    edit_secondary_contact_number.setSelection(edit_secondary_contact_number.text!!.length)
                } else if (textLength == 4) {
                    edit_secondary_contact_number.setSelection(edit_secondary_contact_number.text!!.length)
                } else if (textLength == 5) {
                    edit_secondary_contact_number.setText(
                        StringBuilder(text).insert(
                            text.length - 1, " "
                        ).toString()
                    )
                    edit_secondary_contact_number.setSelection(edit_secondary_contact_number.text!!.length)
                } else if (textLength == 8) {
                    if (!text.contains(" ")) {
                        edit_secondary_contact_number.setText(
                            StringBuilder(text).insert(text.length - 1, " ").toString()
                        )
                        edit_secondary_contact_number.setSelection(edit_secondary_contact_number.text!!.length)
                    }
                } else if (textLength == 9) {
                    if (text.contains(" ")) {
                        edit_secondary_contact_number.setText(
                            StringBuilder(text).insert(text.length - 1, " ").toString()
                        )
                        edit_secondary_contact_number.setSelection(edit_secondary_contact_number.text!!.length)
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

        back_arrow.setOnClickListener(DebouncedClickListener {
            finish()
        })

        back_layout.setOnClickListener(DebouncedClickListener {
            finish()
        })

        if (intent.getStringExtra("IS_SELF") != "self") {
            if (sessionManager.fetchPOSTCODE() != "") {
                edit_find_address.setText(sessionManager.fetchPOSTCODE())
                Handler().postDelayed({
                    if (Functions.isConnectingToInternet(this@AddMemberSecondActivity)) {
                        lifecycleScope.launch {
                            val pd = CustomProgressBar(this@AddMemberSecondActivity)
                            pd.show()
                            val job1 = async { myPincode(edit_find_address.text.toString(), "1") }
                            DebugLog.d("Coro : Step 1 :::: ${job1.await()}")
                            pd.dismiss()
                        }
                    } else {
                        Toast.makeText(
                            this@AddMemberSecondActivity,
                            resources.getString(R.string.no_connection),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, 500)
            }
            if (intent.getStringExtra("TITLENAME") == "Profile") {
                edit_primary_contact_number.setText(sessionManager.fetchMOBILENO())
                edit_secondary_contact_number.setText(sessionManager.fetchSECMOBILENO())
                edit_secondary_email.setText(sessionManager.fetchSECEMAIL())
            }
        }

        edit_primary_contact_number.doOnTextChanged { text, start, before, count ->
            til_primary_number.isErrorEnabled = false
        }

        edit_secondary_email.doOnTextChanged { text, start, before, count ->
            til_second_email.isErrorEnabled = false
        }

        edit_find_address.doOnTextChanged { text, start, before, count ->
            til_postcode.isErrorEnabled = false
        }

        edit_address_line1.doOnTextChanged { text, start, before, count ->
            til_edit_address_line1.isErrorEnabled = false
        }

        edit_town_city.doOnTextChanged { text, start, before, count ->
            til_edit_town_city.isErrorEnabled = false
        }

        next_layout.setOnClickListener(DebouncedClickListener {
            if (edit_primary_contact_number.text.toString().isEmpty()) {
                til_primary_number.error = "Please Enter Primary Contact"
                til_primary_number.isErrorEnabled = true
                edit_primary_contact_number.requestFocus()
                return@DebouncedClickListener
            } else if (!edit_secondary_email.text.toString()
                    .isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(edit_secondary_email.text.toString())
                    .matches()
            ) {
                til_second_email.error = "Please Enter Valid Email"
                til_second_email.isErrorEnabled = true
                edit_secondary_email.requestFocus()
                return@DebouncedClickListener
            } else if (edit_find_address.text.toString().isEmpty()) {
                til_postcode.error = "Please Enter Post Code"
                til_postcode.isErrorEnabled = true
                edit_find_address.requestFocus()
                return@DebouncedClickListener
            } else if (edit_address_line1.text.toString().isEmpty()) {
                til_edit_address_line1.error = "Please Enter Address Line 1"
                til_edit_address_line1.isErrorEnabled = true
                edit_address_line1.requestFocus()
                return@DebouncedClickListener
            } else if (edit_town_city.text.toString().isEmpty()) {
                til_edit_town_city.error = "Please Enter Town/City"
                til_edit_town_city.isErrorEnabled = true
                edit_town_city.requestFocus()
                return@DebouncedClickListener
            } else {
                if (intent.getStringExtra("IS_SELF") != "self") { // Profile or Add family
                    val i = Intent(this@AddMemberSecondActivity, AddMemberThirdActivity::class.java)
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
                    i.putExtra("MOBILE", edit_primary_contact_number.text.toString())
                    i.putExtra("LAND_LINE", edit_secondary_contact_number.text.toString())
                    i.putExtra("SECOND_EMAIL", edit_secondary_email.text.toString())
                    i.putExtra("POST_CODE", edit_find_address.text.toString())
                    i.putExtra("BUILDING_NAME", edit_building_name.text.toString())
                    i.putExtra("ADDRESS_ONE", edit_address_line1.text.toString())
                    i.putExtra("ADDRESS_TWO", edit_address_line2.text.toString())
                    i.putExtra("POST_TOWN", edit_town_city.text.toString())
                    i.putExtra("COUNTY", County)
                    i.putExtra("FAMILY", intent.getStringExtra("FAMILY"))
                    startActivity(i)
                } else { // Add self
                    val i = Intent(this@AddMemberSecondActivity, AddMemberThirdActivity::class.java)
                    i.putExtra("TITLENAME", header_title.text.toString())
                    i.putExtra("FIRST_NAME", intent.getStringExtra("FIRST_NAME"))
                    i.putExtra("MIDDLE_NAME", intent.getStringExtra("MIDDLE_NAME"))
                    i.putExtra("LAST_NAME", intent.getStringExtra("LAST_NAME"))
                    i.putExtra("EMAIL", intent.getStringExtra("EMAIL"))
                    i.putExtra("GENDER", intent.getStringExtra("GENDER"))
                    i.putExtra("DOB", intent.getStringExtra("DOB"))
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
                    i.putExtra("MOBILE", edit_primary_contact_number.text.toString())
                    i.putExtra("LAND_LINE", edit_secondary_contact_number.text.toString())
                    i.putExtra("SECOND_EMAIL", edit_secondary_email.text.toString())
                    i.putExtra("POST_CODE", edit_find_address.text.toString())
                    i.putExtra("BUILDING_NAME", edit_building_name.text.toString())
                    i.putExtra("ADDRESS_ONE", edit_address_line1.text.toString())
                    i.putExtra("ADDRESS_TWO", edit_address_line2.text.toString())
                    i.putExtra("POST_TOWN", edit_town_city.text.toString())
                    i.putExtra("COUNTY", County)
                    i.putExtra("FAMILY", intent.getStringExtra("FAMILY"))
                    startActivity(i)
                }
            }
        })

        edit_select_address.text = "Select Address"
        find_address.setOnClickListener(DebouncedClickListener {
            if (edit_find_address.text.toString().isEmpty()) {
                Snackbar.make(rootLayout, "Please enter pin code", Snackbar.LENGTH_SHORT).show()
            } else {
                if (Functions.isConnectingToInternet(this@AddMemberSecondActivity)) {
                    lifecycleScope.launch {
                        val pd = CustomProgressBar(this@AddMemberSecondActivity)
                        pd.show()
                        val job1 = async { myPincode(edit_find_address.text.toString(), "2") }
                        DebugLog.d("Coro : Step 1 :::: ${job1.await()}")
                        pd.dismiss()

                    }
                } else {
                    Toast.makeText(
                        this@AddMemberSecondActivity,
                        resources.getString(R.string.no_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    /*Pincode API*/
    private suspend fun myPincode(pincode: String, sType: String) {
        try {
            val response = MyHssApplication.instance?.api?.getPincode(pincode)
            if (response?.status == true) {
                edit_select_address.text = "Select Address"
                edit_address_line1.setText("")
                edit_address_line2.setText("")
                edit_building_name.setText("")
                edit_town_city.setText("")
                val dataPinCodeList = response.data?.summaries.orEmpty()
                full_address_view.visibility = View.VISIBLE

//                Pincode = dataPinCodeList.map { "${it.streetAddress}, ${it.place}, $pincode" }
                Pincode = dataPinCodeList.map { "${it.streetAddress}" }
                PincodeID = dataPinCodeList.map { it.id.toString() }
                if (intent.getStringExtra("IS_SELF") != "self") {
                    if (sType == "1") {
                        if (sessionManager.fetchLineOne().toString() in Pincode) {
                            val index = Pincode.indexOf(sessionManager.fetchLineOne().toString())
                            edit_select_address.text = sessionManager.fetchLineOne().toString()
                            callMyPinCodeAdressApi(PincodeID[index])
                        }
                    }
                }
                edit_select_address.setOnClickListener(DebouncedClickListener {
                    openSearchableSpinnerDialog("1", "Select Address", Pincode, PincodeID)
                })
            } else {
                Functions.displayMessage(
                    this@AddMemberSecondActivity,
                    response?.message ?: "Unknown error"
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Functions.showAlertMessageWithOK(
                this@AddMemberSecondActivity,
                "Message",
                getString(R.string.some_thing_wrong)
            )
        }
    }

    /*Pincode API*/
    private suspend fun myPincodeAddress(pincode: String) {
        try {
            val response = MyHssApplication.instance!!.api.getPincodeAddress(pincode)
            if (response.status == true) {
                val dataPinCodeAddress = response.data
                edit_building_name.setText(dataPinCodeAddress?.get(0)?.buildingName.toString())
                edit_address_line1.setText(dataPinCodeAddress?.get(0)?.line1.toString())
                edit_address_line2.setText(dataPinCodeAddress?.get(0)?.line2.toString())
                edit_town_city.setText(dataPinCodeAddress?.get(0)?.postTown.toString())
                County = dataPinCodeAddress?.get(0)?.county.toString()
            } else {
                Functions.displayMessage(this@AddMemberSecondActivity, response.message)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Functions.showAlertMessageWithOK(
                this@AddMemberSecondActivity,
                "Message",
                getString(R.string.some_thing_wrong)
            )
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
            val exitFragment = DialogSearchableSpinner.newInstance(
                this,
                sType,
                sTitle,
                ItemName,
                ItemID
            )
            exitFragment.show(supportFragmentManager, "DialogSearchableSpinner")
        }
    }

    override fun searchableItemSelectedData(stype: String, sItemName: String, sItemID: Int) {
        DebugLog.e("Type : $stype , ItemName : $sItemName , ItemID : $sItemID")
        PINCODE_ID = sItemID.toString()
        edit_select_address.text = sItemName
        callMyPinCodeAdressApi(PINCODE_ID)
    }


    fun callMyPinCodeAdressApi(sPinCode: String) {
        if (Functions.isConnectingToInternet(this@AddMemberSecondActivity)) {
            lifecycleScope.launch {
                myPincodeAddress(sPinCode)
            }

        } else {
            Toast.makeText(
                this@AddMemberSecondActivity,
                resources.getString(R.string.no_connection),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}