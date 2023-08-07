package com.uk.myhss.Guru_Dakshina_Regular

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.uk.myhss.Main.Family_Member.Datum_Family_Member
import com.uk.myhss.Main.Family_Member.Family_Member_Response
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.Functions
import com.uk.myhss.Utils.SessionManager
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class GuruDakshinaRegularSecondActivity() : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    var dialog: Dialog? = null
    private var gift_aid = arrayOf(
        "I am a UK Tax Payer and want to Gift Aid all donations that I make now and in the future to Hindu Swayamsevak Sangh (UK)",
        "I do not wish to donate as Gift Aid on this occasion and on all future occasions"
    )

    private var donate_aid = arrayOf("Monthly", "Quarterly", "Annually")

    var FamilyName: List<String> = ArrayList<String>()
    var FamilyID: List<String> = ArrayList<String>()

//    private lateinit var gift_aid_select_txt: SearchableSpinner
//    private lateinit var gift_aid_select_view: RelativeLayout
    var gift_aid_select_txt : Spinner ?= null
    var how_often_like_to_donate_txt : Spinner ?= null

    private var donating_dakshina: String = ""
    private var GIFTAID_ID: String = ""
    private var DONATE_ID: String = ""
    private var MESSAGE: String = ""
    private var AGE: String = ""

    private lateinit var tooltip_view: ImageView

    private lateinit var back_layout: LinearLayout
    private lateinit var next_layout: LinearLayout
    private lateinit var rootLayout: LinearLayout

    private lateinit var donating_individual_family_yes_view: LinearLayout
    private lateinit var donating_individual_family_no_view: LinearLayout

    private lateinit var donating_individual_family_yes_txt: TextView
    private lateinit var donating_individual_family_no_txt: TextView
    private lateinit var donating_individual_family_yes_icon: ImageView
    private lateinit var donating_individual_family_no_img: ImageView

    private lateinit var donate_amount_txt: TextView
    private lateinit var edit_dateofbirth: TextView

    private var year = 0
    private var month = 0
    private var day = 0
    private lateinit var calendar: Calendar
    val sdf: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    @SuppressLint("NewApi", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gurudakshina_second_regular)

        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("RegularGuruDakshinaStep2VC")
        sessionManager.firebaseAnalytics.setUserProperty("RegularGuruDakshinaStep2VC", "GuruDakshinaRegularSecondActivity")

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        val back_arrow = findViewById<ImageView>(R.id.back_arrow)
        val header_title = findViewById<TextView>(R.id.header_title)

        header_title.text = getString(R.string.regular_dakshina)

        donate_amount_txt = findViewById(R.id.donate_amount_txt)
        edit_dateofbirth = findViewById(R.id.edit_dateofbirth)

        donating_individual_family_yes_view = findViewById(R.id.donating_individual_family_yes_view)
        donating_individual_family_no_view = findViewById(R.id.donating_individual_family_no_view)
        donating_individual_family_yes_txt = findViewById(R.id.donating_individual_family_yes_txt)
        donating_individual_family_no_txt = findViewById(R.id.donating_individual_family_no_txt)
        donating_individual_family_yes_icon = findViewById(R.id.donating_individual_family_yes_icon)
        donating_individual_family_no_img = findViewById(R.id.donating_individual_family_no_img)

//        gift_aid_select_txt = findViewById(R.id.gift_aid_select_txt)
//        gift_aid_select_view = findViewById(R.id.gift_aid_select_view)

        tooltip_view = findViewById(R.id.tooltip_view)

        if (intent.getStringExtra("Amount") != "") {
            donate_amount_txt.text = /*getString(R.string.donate_amount) +
                    " "+ */getString(R.string.pound_icon) + " " + intent.getStringExtra("Amount")
        }

//        gift_aid_select_txt.onItemSelectedListener = mOnItemSelectedListener_gift_aid

        gift_aid_select_txt = findViewById(R.id.gift_aid_select_txt)
        how_often_like_to_donate_txt = findViewById(R.id.how_often_like_to_donate_txt)

        val spinnerArrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, gift_aid)
        //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        gift_aid_select_txt!!.adapter = spinnerArrayAdapter

        gift_aid_select_txt!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                Log.d("selectedItem", selectedItem)
                if (position == 0) {
                    GIFTAID_ID = "yes"
                } else {
                    GIFTAID_ID = "no"
                }
                Log.d("GIFTAID_ID", GIFTAID_ID)
                if (selectedItem == "Add new category") {
                    // do your stuff
                }
            } // to close the onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        val spinnerArrayAdapternew = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, donate_aid)
        //selected item will look like a spinner set from XML
        spinnerArrayAdapternew.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        how_often_like_to_donate_txt!!.adapter = spinnerArrayAdapternew

        how_often_like_to_donate_txt!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                Log.d("selectedItem", selectedItem)
                DONATE_ID = selectedItem
                /*if (position == 0) {
                    DONATE_ID = "yes"
                } else {
                    DONATE_ID = "no"
                }*/
                Log.d("DONATE_ID", DONATE_ID)
                if (selectedItem == "Add new category") {
                    // do your stuff
                }
            } // to close the onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        edit_dateofbirth.setOnClickListener {
            calendar = Calendar.getInstance()
//            calendar.add(Calendar.YEAR, +1)
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH)
            day = calendar.get(Calendar.DAY_OF_MONTH)

            val dialog = DatePickerDialog(this, { _, year, month, day_of_month ->
                calendar[Calendar.YEAR] = year
                calendar[Calendar.MONTH] = month + 1
                calendar[Calendar.DAY_OF_MONTH] = day_of_month
//                val myFormat = "dd/MM/yyyy"
//                val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
                edit_dateofbirth.text = sdf.format(calendar.time)

                val today = Calendar.getInstance()
                val age: Int = today.get(Calendar.YEAR) - calendar.get(Calendar.YEAR)

                /*if (age < 18) {
                    //do something
                    Functions.showAlertMessageWithOK(
                        this@GuruDakshinaRegularSecondActivity,
                        "As your age is below 18",
                        "We will send an email to your guardian to allow your HSS (UK) membership to be recorded on MyHSS. If you are below 13, it is our policy to use the parent/guardian`s email address only for all communications."
                    )
                    AGE = "1"
                } else {
                    Log.d("", "Age in year= " + age);
                    AGE = ""
                }*/

            }, year, month, day)
//            dialog.datePicker.minDate = calendar.timeInMillis
//            calendar.add(Calendar.YEAR, 0)
//            dialog.datePicker.minDate = calendar.set(day, month, year)
//            dialog.datePicker.maxDate = calendar.timeInMillis
            dialog.show()
        }

        /*For Selection Dasgina drop down*/
        /*SearchSpinner(gift_aid, gift_aid_select_txt)

        gift_aid_select_view.setOnClickListener {
            SearchSpinner(gift_aid, gift_aid_select_txt)
        }*/

        donating_individual_family_yes_view.setOnClickListener {
            donating_individual_family_yes_view.setBackgroundResource(R.drawable.edit_primery_color_round)
            donating_individual_family_no_view.setBackgroundResource(R.drawable.edittext_round)

            donating_individual_family_yes_txt.setTextColor(getColor(R.color.primaryColor))
            donating_individual_family_yes_icon.setImageResource(R.drawable.righttikmark)
//            donating_individual_family_yes_icon.setColorFilter(ContextCompat.getColor(this, R.color.primaryColor), android.graphics.PorterDuff.Mode.MULTIPLY)

            donating_individual_family_no_txt.setTextColor(getColor(R.color.grayColorColor))
            donating_individual_family_no_img.setImageResource(R.drawable.righttikmark_gray_icon)
//            donating_individual_family_no_img.setColorFilter(ContextCompat.getColor(this, R.color.grayColorColor), android.graphics.PorterDuff.Mode.MULTIPLY)

//            medical_information_details_view.visibility = View.VISIBLE

            donating_dakshina = "Individual"
        }

        donating_individual_family_no_view.setOnClickListener {
            donating_individual_family_no_view.setBackgroundResource(R.drawable.edit_primery_color_round)
            donating_individual_family_yes_view.setBackgroundResource(R.drawable.edittext_round)

            donating_individual_family_yes_txt.setTextColor(getColor(R.color.grayColorColor))
            donating_individual_family_yes_icon.setImageResource(R.drawable.righttikmark_gray_icon)
//            donating_individual_family_yes_icon.setColorFilter(ContextCompat.getColor(this, R.color.grayColorColor), android.graphics.PorterDuff.Mode.MULTIPLY)

            donating_individual_family_no_txt.setTextColor(getColor(R.color.primaryColor))
            donating_individual_family_no_img.setImageResource(R.drawable.righttikmark)
//            donating_individual_family_no_img.setColorFilter(ContextCompat.getColor(this, R.color.primaryColor), android.graphics.PorterDuff.Mode.MULTIPLY)

//            medical_information_details_view.visibility = View.GONE

            donating_dakshina = "Family"

            if (Functions.isConnectingToInternet(this@GuruDakshinaRegularSecondActivity)) {
                val User_id = sessionManager.fetchUserID()
                val Member_id = sessionManager.fetchMEMBERID()
                myFamilyList(User_id!!, Member_id!!)
            } else {
                Toast.makeText(
                    this@GuruDakshinaRegularSecondActivity,
                    resources.getString(R.string.no_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        tooltip_view.setOnClickListener {
            MESSAGE = "Gift Aid Declaration: I want the above charity to treat the entered sum as a Gift Aid donation. \n\nI have paid sufficient UK Income Tax and/or capital gains tax to cover all my charitable donations equal to the tax that the charity will claim from HMRC and I am aware that other taxes such as council tax and VAT do not qualify. \n\n\nI understand that I am liable for the difference if the income tax or the capital gains tax payable by me for the tax year, is less than the amount of tax that all the charities and CASCs that I donate to will reclaim on my gifts made or deemed to be made in that year."
            depositDialog(MESSAGE)
            /*Tooltip.on(tooltip_view)
                .text(R.string.ageTipText)
//                .iconStart(android.R.drawable.ic_dialog_info)
//                .iconStartSize(30, 30)
                .color(resources.getColor(R.color.orangeColor))
//                .overlay(resources.getColor(R.color.orangeColor))
//                .iconEnd(android.R.drawable.ic_dialog_info)
//                .iconEndSize(30, 30)
                .border(resources.getColor(R.color.orangeColor), 5f)
                .clickToHide(true)
                .corner(10)
                .shadowPadding(10f)
                .position(Position.BOTTOM)
                .clickToHide(true)
                .show()*/
        }

        back_layout = findViewById(R.id.back_layout)
        next_layout = findViewById(R.id.next_layout)
        rootLayout = findViewById(R.id.rootLayout)

        back_arrow.setOnClickListener {
            finish()
        }

        back_layout.setOnClickListener {
            finish()
        }

        next_layout.setOnClickListener {
            if (GIFTAID_ID == "") {
                Snackbar.make(rootLayout, "Please select gift aid", Snackbar.LENGTH_SHORT).show()
            } else if (edit_dateofbirth.text.toString() == "") {
                Snackbar.make(rootLayout, "Please select start payment date", Snackbar.LENGTH_SHORT).show()
            } else if (donating_dakshina == "") {
                Snackbar.make(rootLayout, "Please donate dakshina", Snackbar.LENGTH_SHORT).show()
            } else if (intent.getStringExtra("Amount") != "") {
                val i =
                    Intent(this@GuruDakshinaRegularSecondActivity, GuruDakshinaRegularThirdActivity::class.java)
                i.putExtra("Amount", intent.getStringExtra("Amount"))
                i.putExtra("GIFTAID_ID", GIFTAID_ID)
                i.putExtra("DONATE_ID", DONATE_ID)
//                i.putExtra("AGE", AGE)
                i.putExtra("DATE", edit_dateofbirth.text.toString())
                i.putExtra("donating_dakshina", donating_dakshina)
                startActivity(i)
            }
        }
    }

    private fun SearchSpinner(
        spinner_search: Array<String>,
        edit_txt: SearchableSpinner
    ) {
        val searchmethod = ArrayAdapter(
            this, android.R.layout.simple_spinner_item,
            spinner_search
        )
        searchmethod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        edit_txt.adapter = searchmethod
    }

    private val mOnItemSelectedListener_gift_aid: AdapterView.OnItemSelectedListener = object :
        AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//            TODO("Not yet implemented")
            Log.d("Name", gift_aid[position])
//            GIFTAID_ID = gift_aid[position]

            if (position == 0) {
                GIFTAID_ID = "yes"
            } else {
                GIFTAID_ID = "no"
            }
            Log.d("GIFTAID_ID", GIFTAID_ID)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
//            TODO("Not yet implemented")
        }
    }

    fun depositDialog(message: String) {
        // Deposit Dialog
        if (dialog == null) {
            dialog = Dialog(this, R.style.StyleCommonDialog)
        }
        dialog?.setContentView(R.layout.dialog_diposit_money)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.show()

        val tvTitle = dialog!!.findViewById(R.id.tvTitle) as TextView
        val btnOk = dialog!!.findViewById(R.id.btnOk) as TextView

        tvTitle.text = message

        btnOk.setOnClickListener {
            dialog?.dismiss()
        }
    }

    /*FamilyList API*/
    private fun myFamilyList(user_id:String, member_id: String) {
        val pd =
            CustomProgressBar(this@GuruDakshinaRegularSecondActivity)
        pd.show()
        val call: Call<Family_Member_Response> =
            MyHssApplication.instance!!.api.get_family_members(user_id, member_id)
        call.enqueue(object : Callback<Family_Member_Response> {
            override fun onResponse(
                call: Call<Family_Member_Response>,
                response: Response<Family_Member_Response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                Log.d("status", response.body()?.status.toString())
                if (response.body()?.status!!) {

                    if (response.body()!!.data!!.isEmpty()) {

                        Functions.showAlertMessageWithOK(
                            this@GuruDakshinaRegularSecondActivity,
                            getString(R.string.payment_error_title),
                            getString(R.string.payment_error_message)
                        )

                            donating_individual_family_yes_view.setBackgroundResource(R.drawable.edit_primery_color_round)
                            donating_individual_family_no_view.setBackgroundResource(R.drawable.edittext_round)

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                donating_individual_family_yes_txt.setTextColor(
                                    this@GuruDakshinaRegularSecondActivity.getColor(
                                        R.color.primaryColor
                                    )
                                )
                            }
                            donating_individual_family_yes_icon.setImageResource(R.drawable.righttikmark)

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                donating_individual_family_no_txt.setTextColor(
                                    this@GuruDakshinaRegularSecondActivity.getColor(
                                        R.color.grayColorColor
                                    )
                                )
                            }
                            donating_individual_family_no_img.setImageResource(R.drawable.righttikmark_gray_icon)

                            donating_dakshina = "Individual"

                        } else {

                        var data_relationship: List<Datum_Family_Member> =
                            ArrayList<Datum_Family_Member>()
                        data_relationship = response.body()!!.data!!
                        Log.d("atheletsBeans", data_relationship.toString())

                        FamilyName = listOf(arrayOf(data_relationship).toString())
                        FamilyID = listOf(arrayOf(data_relationship).toString())

                        val mStringList = ArrayList<String>()
                        for (i in 0 until data_relationship.size) {
                            mStringList.add(
                                data_relationship[i].firstName.toString().capitalize(Locale.ROOT)
                                        + " " + data_relationship[i].lastName.toString()
                                    .capitalize(Locale.ROOT)
//                                    + " " + data_relationship[i].firstName.toString().capitalize(Locale.ROOT)
                            )
                        }

                        var mStringArray = mStringList.toArray()

                        for (i in mStringArray.indices) {
                            Log.d("string is", mStringArray[i] as String)
                        }

                        mStringArray = mStringList.toArray(mStringArray)

                        val list: ArrayList<String> = arrayListOf<String>()

                        for (element in mStringArray) {
                            Log.d("LIST==>", element.toString())
                            list.add(element.toString())
                            Log.d("list==>", list.toString())

                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                FamilyName = list
                            }
                        }

                        if (dialog == null) {
                            dialog = Dialog(
                                this@GuruDakshinaRegularSecondActivity,
                                R.style.StyleCommonDialog
                            )
                        }
                        dialog?.setContentView(R.layout.dialog_family_list)
                        dialog?.setCanceledOnTouchOutside(false)
                        dialog?.show()

                        val btnOk = dialog!!.findViewById(R.id.btnOk) as TextView
                        val family_list_view =
                            dialog!!.findViewById(R.id.family_list_view) as ListView

                        val adapter = ArrayAdapter(
                            this@GuruDakshinaRegularSecondActivity,
                            android.R.layout.simple_list_item_1,
                            FamilyName
                        )
                        family_list_view.adapter = adapter

                        btnOk.setOnClickListener {
                            dialog?.dismiss()
                        }
                    }

                } else {
                    Functions.showAlertMessageWithOK(
                        this@GuruDakshinaRegularSecondActivity,
                        getString(R.string.payment_error_title),
                        getString(R.string.payment_error_message)
                    )

                    donating_individual_family_yes_view.setBackgroundResource(R.drawable.edit_primery_color_round)
                    donating_individual_family_no_view.setBackgroundResource(R.drawable.edittext_round)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        donating_individual_family_yes_txt.setTextColor(
                            this@GuruDakshinaRegularSecondActivity.getColor(
                                R.color.primaryColor
                            )
                        )
                    }
                    donating_individual_family_yes_icon.setImageResource(R.drawable.righttikmark)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        donating_individual_family_no_txt.setTextColor(
                            this@GuruDakshinaRegularSecondActivity.getColor(
                                R.color.grayColorColor
                            )
                        )
                    }
                    donating_individual_family_no_img.setImageResource(R.drawable.righttikmark_gray_icon)

                    donating_dakshina = "Individual"
                }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@GuruDakshinaRegularSecondActivity, "Message",
                        this@GuruDakshinaRegularSecondActivity.getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Family_Member_Response>, t: Throwable) {
                Toast.makeText(this@GuruDakshinaRegularSecondActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }
}