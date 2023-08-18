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
import com.myhss.Utils.Functions
import com.uk.myhss.Utils.SessionManager
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class AddMemberSecondActivity : AppCompatActivity() {

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

    private lateinit var edit_select_address: SearchableSpinner

    private lateinit var select_address: RelativeLayout

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
        select_address = findViewById(R.id.select_address)

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

//            header_title.text = getString(R.string.Add_family_member)

//        Log.d(
//            "Fetch_Data one-->",
//            intent.getStringExtra("FIRST_NAME") + "--" + intent.getStringExtra("MIDDLE_NAME") + "--" + intent.getStringExtra(
//                "LAST_NAME"
//            ) + "--" + intent.getStringExtra(
//                "USERNAME"
//            ) + "--" + intent.getStringExtra("EMAIL") + "--" + intent.getStringExtra("PASSWORD") + "--" + intent.getStringExtra(
//                "GENDER"
//            ) + "--" + intent.getStringExtra("DOB") + "--" + intent.getStringExtra("RELATIONSHIP") + "--" + intent.getStringExtra(
//                "OTHER_RELATIONSHIP"
//            ) + "--" + intent.getStringExtra(
//                "OCCUPATION"
//            ) + "--" + intent.getStringExtra("OCCUPATION_NAME") + "--" + intent.getStringExtra("SHAKHA") + "--" + intent.getStringExtra(
//                "AGE"
//            ) + "--" + intent.getStringExtra("IS_LINKED") + "--" + intent.getStringExtra("IS_SELF") + "--" + intent.getStringExtra(
//                "TYPE"
//            ) + "--" + intent.getStringExtra(
//                "PARENT_MEMBER"
//            )
//        )

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
//                    if (!text.contains("(")) {
//                        edit_primary_contact_number.setText(
//                            StringBuilder(text).insert(text.length - 1, "(").toString()
//                        )
                    edit_primary_contact_number.setSelection(edit_primary_contact_number.text!!.length)
//                    }
                } else if (textLength == 4) {
//                    if (!text.contains(")")) {
//                        edit_primary_contact_number.setText(
//                            StringBuilder(text).insert(text.length - 1, ")").toString()
//                        )
                    edit_primary_contact_number.setSelection(edit_primary_contact_number.text!!.length)
//                    }
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
//                    if (!text.contains("(")) {
//                        edit_primary_contact_number.setText(
//                            StringBuilder(text).insert(text.length - 1, "(").toString()
//                        )
                    edit_secondary_contact_number.setSelection(edit_secondary_contact_number.text!!.length)
//                    }
                } else if (textLength == 4) {
//                    if (!text.contains(")")) {
//                        edit_primary_contact_number.setText(
//                            StringBuilder(text).insert(text.length - 1, ")").toString()
//                        )
                    edit_secondary_contact_number.setSelection(edit_secondary_contact_number.text!!.length)
//                    }
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

        back_arrow.setOnClickListener {
//            Snackbar.make(rootLayout, "Back", Snackbar.LENGTH_SHORT).show()
            finish()
        }

        back_layout.setOnClickListener {
//            Snackbar.make(rootLayout, "Next", Snackbar.LENGTH_SHORT).show()
            finish()
        }

        if (intent.getStringExtra("IS_SELF") != "self") {
            if (sessionManager.fetchPOSTCODE() != "") {
                edit_find_address.setText(sessionManager.fetchPOSTCODE())
                Handler().postDelayed({
                    find_address.callOnClick()
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

        next_layout.setOnClickListener {
            if (edit_primary_contact_number.text.toString().isEmpty()) {
                til_primary_number.error = "Please Enter Primary Contact"
                til_primary_number.isErrorEnabled = true
                edit_primary_contact_number.requestFocus()
                return@setOnClickListener
            } else if (!edit_secondary_email.text.toString()
                    .isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(edit_secondary_email.text.toString())
                    .matches()
            ) {
                til_second_email.error = "Please Enter Valid Email"
                til_second_email.isErrorEnabled = true
                edit_secondary_email.requestFocus()
                return@setOnClickListener
            } else if (edit_find_address.text.toString().isEmpty()) {
                til_postcode.error = "Please Enter Post Code"
                til_postcode.isErrorEnabled = true
                edit_find_address.requestFocus()
                return@setOnClickListener
            } else if (edit_address_line1.text.toString().isEmpty()) {
                til_edit_address_line1.error = "Please Enter Address Line 1"
                til_edit_address_line1.isErrorEnabled = true
                edit_address_line1.requestFocus()
                return@setOnClickListener
            } else if (edit_town_city.text.toString().isEmpty()) {
                til_edit_town_city.error = "Please Enter Town/City"
                til_edit_town_city.isErrorEnabled = true
                edit_town_city.requestFocus()
                return@setOnClickListener
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
//                i.putExtra("USERNAME", intent.getStringExtra("USERNAME"))
                    i.putExtra("EMAIL", intent.getStringExtra("EMAIL"))
//                i.putExtra("PASSWORD", intent.getStringExtra("PASSWORD"))
                    i.putExtra("GENDER", intent.getStringExtra("GENDER"))
                    i.putExtra("DOB", intent.getStringExtra("DOB"))

//                    if (intent.getStringExtra("OTHER_RELATIONSHIP") == "") {
//                        i.putExtra("RELATIONSHIP", intent.getStringExtra("RELATIONSHIP"))
//                    } else {
//                        i.putExtra("RELATIONSHIP", intent.getStringExtra("RELATIONSHIP"))
//                        i.putExtra(
//                            "OTHER_RELATIONSHIP",
//                            intent.getStringExtra("OTHER_RELATIONSHIP")
//                        )
//                    }

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
        }

        edit_select_address.onItemSelectedListener = mOnItemSelectedListener_address

        edit_select_address.setTitle("Select Address")

        select_address.setOnClickListener {
//            SearchSpinner(relationship, edit_select_address)
            SearchSpinner(Pincode.toTypedArray(), edit_select_address)
        }

        find_address.setOnClickListener {
            if (edit_find_address.text.toString().isEmpty()) {
                Snackbar.make(rootLayout, "Please enter pin code", Snackbar.LENGTH_SHORT).show()
            } else {
                if (Functions.isConnectingToInternet(this@AddMemberSecondActivity)) {
                    myPincode(edit_find_address.text.toString())
                } else {
                    Toast.makeText(
                        this@AddMemberSecondActivity,
                        resources.getString(R.string.no_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        val pincode = edit_find_address.text.toString()
    }

    private fun SearchSpinner(
        spinner_search: Array<String>, edit_txt: SearchableSpinner
    ) {
        val searchmethod = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, spinner_search
        )
        searchmethod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        edit_txt.adapter = searchmethod
    }

    private val mOnItemSelectedListener_address: AdapterView.OnItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                PINCODE_ID = PincodeID[position]

                if (Functions.isConnectingToInternet(this@AddMemberSecondActivity)) {
                    myPincodeAddress(PINCODE_ID)
                } else {
                    Toast.makeText(
                        this@AddMemberSecondActivity,
                        resources.getString(R.string.no_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

    /*Pincode API*/
    private fun myPincode(pincode: String) {
        val pd = CustomProgressBar(this@AddMemberSecondActivity)
        pd.show()
        val call: Call<Get_Pincode_Response> = MyHssApplication.instance!!.api.get_pincode(pincode)
        call.enqueue(object : Callback<Get_Pincode_Response> {
            override fun onResponse(
                call: Call<Get_Pincode_Response>, response: Response<Get_Pincode_Response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {
                        full_address_view.visibility = View.VISIBLE
                        var data_relationship: List<Summary_Address> = ArrayList<Summary_Address>()
                        data_relationship = response.body()!!.data!!.summaries!!
                        Log.d("atheletsBeans", data_relationship.toString())
                        for (i in 1 until data_relationship.size) {
                            Log.d("relationshipName", data_relationship[i].place.toString())
                        }
                        Pincode = listOf(arrayOf(data_relationship).toString())
                        PincodeID = listOf(arrayOf(data_relationship).toString())

                        val mStringList = ArrayList<String>()
                        for (i in 0 until data_relationship.size) {
                            mStringList.add(
//                            data_relationship[i].occupationId.toString() +
                                data_relationship[i].streetAddress.toString() + ", " + data_relationship[i].place.toString() + ", " + pincode
                            )
                        }

                        val mStringListnew = ArrayList<String>()
                        for (i in 0 until data_relationship.size) {
                            mStringListnew.add(
                                data_relationship[i].id.toString()
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
                                Pincode = list//listOf(listn.toCollection(ArrayList()).toString())
                            }
                        }

                        for (element in mStringArraynew) {
                            Log.d("LIST==>", element.toString())
                            listnew.add(element.toString())
                            Log.d("list==>", listnew.toString())

                            val listn = arrayOf(element)
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                PincodeID =
                                    listnew//listOf(listn.toCollection(ArrayList()).toString())
                            }
                        }

                        Log.d("Pincode==>", Pincode.toString())
                        SearchSpinner(Pincode.toTypedArray(), edit_select_address)

                    } else {
                        full_address_view.visibility = View.VISIBLE
//                        edit_address_line1.setText("test") // temp
//                        edit_town_city.setText("test")// temp
                        Functions.showAlertMessageWithOK(
                            this@AddMemberSecondActivity, "",
//                        "Message",
                            response.body()?.message
                        )
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@AddMemberSecondActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Get_Pincode_Response>, t: Throwable) {
                Toast.makeText(this@AddMemberSecondActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    /*Pincode API*/
    private fun myPincodeAddress(pincode: String) {
        val pd = CustomProgressBar(this@AddMemberSecondActivity)
        pd.show()
        val call: Call<Get_PincodeAddress_Response> =
            MyHssApplication.instance!!.api.get_pincodea_ddress(
                pincode
            )
        call.enqueue(object : Callback<Get_PincodeAddress_Response> {
            override fun onResponse(
                call: Call<Get_PincodeAddress_Response>,
                response: Response<Get_PincodeAddress_Response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {

//                    var data_relationship: List<Datum_Get_PincodeAddress> = ArrayList<Datum_Get_PincodeAddress>()
//                    data_relationship = response.body()!!.data!!
//                    Log.d("atheletsBeans", data_relationship.toString())

                        edit_building_name.setText(response.body()!!.data!![0].buildingName.toString())
                        edit_address_line1.setText(response.body()!!.data!![0].line1.toString())
                        edit_address_line2.setText(response.body()!!.data!![0].line2.toString())
                        edit_town_city.setText(response.body()!!.data!![0].postTown.toString())

                        County = response.body()!!.data!![0].county.toString()
                        Log.d("county>>>", response.body()!!.data!![0].county.toString())

                    } else {
                        Functions.showAlertMessageWithOK(
                            this@AddMemberSecondActivity, "",
//                        "Message",
                            response.body()?.message
                        )
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@AddMemberSecondActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Get_PincodeAddress_Response>, t: Throwable) {
                Toast.makeText(this@AddMemberSecondActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }
}