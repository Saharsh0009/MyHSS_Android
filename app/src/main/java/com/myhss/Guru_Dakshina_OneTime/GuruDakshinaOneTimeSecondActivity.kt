package com.uk.myhss.Guru_Dakshina_OneTime

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputFilter
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
import com.myhss.Utils.InputFilterMinMax
import com.uk.myhss.Utils.SessionManager
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class GuruDakshinaOneTimeSecondActivity() : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    var dialog: Dialog? = null
    var dialog_p: Dialog? = null
    private var gift_aid = arrayOf(
        "I am a UK Tax Payer and want to donate as Gift Aid",
        "I do not wish to donate as Gift Aid on this Occasion"
    )

    var FamilyName: List<String> = ArrayList<String>()
    var FamilyID: List<String> = ArrayList<String>()

    //    private lateinit var gift_aid_select_txt: SearchableSpinner
//    private lateinit var gift_aid_select_view: RelativeLayout
    var gift_aid_select_txt: Spinner? = null

    private var giving_dakshina: String = ""
    private var donating_dakshina: String = ""
    private var GIFTAID_ID: String = ""
    private var MESSAGE: String = ""

    private lateinit var tooltip_view: ImageView

    private lateinit var back_layout: LinearLayout
    private lateinit var next_layout: LinearLayout

    private lateinit var giving_dakshina_yes_view: LinearLayout
    private lateinit var giving_dakshina_no_view: LinearLayout

    private lateinit var donating_individual_family_yes_view: LinearLayout
    private lateinit var donating_individual_family_no_view: LinearLayout

    private lateinit var giving_dakshina_yes_txt: TextView
    private lateinit var giving_dakshina_no_txt: TextView
    private lateinit var giving_dakshina_yes_icon: ImageView
    private lateinit var giving_dakshina_no_img: ImageView

    private lateinit var donating_individual_family_yes_txt: TextView
    private lateinit var donating_individual_family_no_txt: TextView
    private lateinit var giving_dakshina_layout: TextView
    private lateinit var donating_individual_family_yes_icon: ImageView
    private lateinit var donating_individual_family_no_img: ImageView

    private lateinit var donate_amount_txt: TextView
    private lateinit var edit_payment: ImageView

    private lateinit var rootLayout: LinearLayout

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gurudakshina_second)

        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("OneTimeDakshinaStep2VC")
        sessionManager.firebaseAnalytics.setUserProperty("OneTimeDakshinaStep2VC", "GuruDakshinaOneTimeSecondActivity")

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        val back_arrow = findViewById<ImageView>(R.id.back_arrow)
        val header_title = findViewById<TextView>(R.id.header_title)

        header_title.text = getString(R.string.one_time_dakshina)

        donate_amount_txt = findViewById(R.id.donate_amount_txt)
        giving_dakshina_yes_view = findViewById(R.id.giving_dakshina_yes_view)
        giving_dakshina_no_view = findViewById(R.id.giving_dakshina_no_view)
        giving_dakshina_no_txt = findViewById(R.id.giving_dakshina_no_txt)
        giving_dakshina_yes_txt = findViewById(R.id.giving_dakshina_yes_txt)
        giving_dakshina_yes_icon = findViewById(R.id.giving_dakshina_yes_icon)
        giving_dakshina_no_img = findViewById(R.id.giving_dakshina_no_img)

        donating_individual_family_yes_view = findViewById(R.id.donating_individual_family_yes_view)
        donating_individual_family_no_view = findViewById(R.id.donating_individual_family_no_view)
        donating_individual_family_yes_txt = findViewById(R.id.donating_individual_family_yes_txt)
        donating_individual_family_no_txt = findViewById(R.id.donating_individual_family_no_txt)
        donating_individual_family_yes_icon = findViewById(R.id.donating_individual_family_yes_icon)
        donating_individual_family_no_img = findViewById(R.id.donating_individual_family_no_img)
        giving_dakshina_layout = findViewById(R.id.giving_dakshina_layout)
        rootLayout = findViewById(R.id.rootLayout)
        edit_payment = findViewById(R.id.edit_payment)

//        giving_dakshina_layout.text = getString(R.string.are_you_giving_dakshina)

//        gift_aid_select_txt = findViewById(R.id.gift_aid_select_txt)
//        gift_aid_select_view = findViewById(R.id.gift_aid_select_view)

        tooltip_view = findViewById(R.id.tooltip_view)

        if (intent.getStringExtra("Amount") != "") {
            donate_amount_txt.text = intent.getStringExtra("Amount")
        }

//        gift_aid_select_txt.onItemSelectedListener = mOnItemSelectedListener_gift_aid

        gift_aid_select_txt = findViewById(R.id.gift_aid_select_txt)

        val spinnerArrayAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, gift_aid)
        //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        gift_aid_select_txt!!.adapter = spinnerArrayAdapter

        gift_aid_select_txt!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
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

        /*For Selection Dasgina drop down*/
        /*SearchSpinner(gift_aid, gift_aid_select_txt)

        gift_aid_select_view.setOnClickListener {
            SearchSpinner(gift_aid, gift_aid_select_txt)
        }*/

        edit_payment.setOnClickListener {
            depositDialogForEditPayment()
        }

        giving_dakshina_yes_view.setOnClickListener {
            giving_dakshina_yes_view.setBackgroundResource(R.drawable.edit_primery_color_round)
            giving_dakshina_no_view.setBackgroundResource(R.drawable.edittext_round)

            giving_dakshina_yes_txt.setTextColor(getColor(R.color.primaryColor))
            giving_dakshina_yes_icon.setImageResource(R.drawable.righttikmark)
//            giving_dakshina_yes_icon.setColorFilter(ContextCompat.getColor(this, R.color.primaryColor), android.graphics.PorterDuff.Mode.MULTIPLY)

            giving_dakshina_no_txt.setTextColor(getColor(R.color.grayColorColor))
            giving_dakshina_no_img.setImageResource(R.drawable.righttikmark_gray_icon)
//            giving_dakshina_no_img.setColorFilter(ContextCompat.getColor(this, R.color.grayColorColor), android.graphics.PorterDuff.Mode.MULTIPLY)

//            medical_information_details_view.visibility = View.VISIBLE

            giving_dakshina = "1"
        }

        giving_dakshina_no_view.setOnClickListener {
            giving_dakshina_no_view.setBackgroundResource(R.drawable.edit_primery_color_round)
            giving_dakshina_yes_view.setBackgroundResource(R.drawable.edittext_round)

            giving_dakshina_yes_txt.setTextColor(getColor(R.color.grayColorColor))
            giving_dakshina_yes_icon.setImageResource(R.drawable.righttikmark_gray_icon)
//            giving_dakshina_yes_icon.setColorFilter(ContextCompat.getColor(this, R.color.grayColorColor), android.graphics.PorterDuff.Mode.MULTIPLY)

            giving_dakshina_no_txt.setTextColor(getColor(R.color.primaryColor))
            giving_dakshina_no_img.setImageResource(R.drawable.righttikmark)
//            giving_dakshina_no_img.setColorFilter(ContextCompat.getColor(this, R.color.primaryColor), android.graphics.PorterDuff.Mode.MULTIPLY)

//            medical_information_details_view.visibility = View.GONE

            giving_dakshina = "0"
        }

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

            if (Functions.isConnectingToInternet(this@GuruDakshinaOneTimeSecondActivity)) {
                val User_id = sessionManager.fetchUserID()
                val Member_id = sessionManager.fetchMEMBERID()
                myFamilyList(User_id!!, Member_id!!)
            } else {
                Toast.makeText(
                    this@GuruDakshinaOneTimeSecondActivity,
                    resources.getString(R.string.no_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        tooltip_view.setOnClickListener {
            MESSAGE =
                "Gift Aid Declaration: I want the above charity to treat the entered sum as a Gift Aid donation. \n\nI have paid sufficient UK Income Tax and/or capital gains tax to cover all my charitable donations equal to the tax that the charity will claim from HMRC and I am aware that other taxes such as council tax and VAT do not qualify. \n\n\nI understand that I am liable for the difference if the income tax or the capital gains tax payable by me for the tax year, is less than the amount of tax that all the charities and CASCs that I donate to will reclaim on my gifts made or deemed to be made in that year."
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

        back_arrow.setOnClickListener {
            finish()
        }

        back_layout.setOnClickListener {
            finish()
        }

        next_layout.setOnClickListener {
            if (GIFTAID_ID == "") {
                Snackbar.make(rootLayout, "Please select gift aid", Snackbar.LENGTH_SHORT).show()
            } else if (giving_dakshina == "") {
                Snackbar.make(rootLayout, "Please select given dakshina", Snackbar.LENGTH_SHORT)
                    .show()
            } else if (donating_dakshina == "") {
                Snackbar.make(rootLayout, "Please donate dakshina", Snackbar.LENGTH_SHORT).show()
            } else if (intent.getStringExtra("Amount") != "") {
                val i =
                    Intent(
                        this@GuruDakshinaOneTimeSecondActivity,
                        GuruDakshinaOneTimeThirdActivity::class.java
                    )
                i.putExtra("Amount",donate_amount_txt.text.toString())
                i.putExtra("GIFTAID_ID", GIFTAID_ID)
                i.putExtra("giving_dakshina", giving_dakshina)
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
    private fun myFamilyList(user_id: String, member_id: String) {
        val pd =
            CustomProgressBar(this@GuruDakshinaOneTimeSecondActivity)
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
                                this@GuruDakshinaOneTimeSecondActivity,
                                getString(R.string.payment_error_title),
                                getString(R.string.payment_error_message)
                            )

                            donating_individual_family_yes_view.setBackgroundResource(R.drawable.edit_primery_color_round)
                            donating_individual_family_no_view.setBackgroundResource(R.drawable.edittext_round)

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                donating_individual_family_yes_txt.setTextColor(
                                    this@GuruDakshinaOneTimeSecondActivity.getColor(
                                        R.color.primaryColor
                                    )
                                )
                            }
                            donating_individual_family_yes_icon.setImageResource(R.drawable.righttikmark)

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                donating_individual_family_no_txt.setTextColor(
                                    this@GuruDakshinaOneTimeSecondActivity.getColor(
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
                                    this@GuruDakshinaOneTimeSecondActivity,
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
                                this@GuruDakshinaOneTimeSecondActivity,
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
                            this@GuruDakshinaOneTimeSecondActivity,
                            getString(R.string.payment_error_title),
                            getString(R.string.payment_error_message)
                        )

                        donating_individual_family_yes_view.setBackgroundResource(R.drawable.edit_primery_color_round)
                        donating_individual_family_no_view.setBackgroundResource(R.drawable.edittext_round)

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            donating_individual_family_yes_txt.setTextColor(
                                this@GuruDakshinaOneTimeSecondActivity.getColor(
                                    R.color.primaryColor
                                )
                            )
                        }
                        donating_individual_family_yes_icon.setImageResource(R.drawable.righttikmark)

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            donating_individual_family_no_txt.setTextColor(
                                this@GuruDakshinaOneTimeSecondActivity.getColor(
                                    R.color.grayColorColor
                                )
                            )
                        }
                        donating_individual_family_no_img.setImageResource(R.drawable.righttikmark_gray_icon)

                        donating_dakshina = "Individual"
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@GuruDakshinaOneTimeSecondActivity, "Message",
                        this@GuruDakshinaOneTimeSecondActivity.getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Family_Member_Response>, t: Throwable) {
                Toast.makeText(this@GuruDakshinaOneTimeSecondActivity, t.message, Toast.LENGTH_LONG)
                    .show()
                pd.dismiss()
            }
        })
    }

    fun depositDialogForEditPayment() {
        // Deposit Dialog
        if (dialog_p == null) {
            dialog_p = Dialog(this, R.style.StyleCommonDialog)
        }
        dialog_p?.setContentView(R.layout.edit_dialog_diposit_money)
        dialog_p?.setCanceledOnTouchOutside(true)
        dialog_p?.show()

        val edit_amount = dialog_p!!.findViewById(R.id.edit_amount) as TextView
        val btnOk = dialog_p!!.findViewById(R.id.btnOk) as TextView

        if (intent.getStringExtra("Amount") != "") {
            edit_amount.text = intent.getStringExtra("Amount")
        }

        btnOk.setOnClickListener {
            if (edit_amount.text.toString().isNotEmpty()) {
                edit_amount.filters = arrayOf<InputFilter>(
                    InputFilterMinMax(
                        "1",
                        "10000"
                    )
                )
                if (Integer.valueOf(edit_amount.text.toString()) > 0 && Integer.valueOf(edit_amount.text.toString()) <= 10000) {
                    donate_amount_txt.text = edit_amount.text.toString()
                    dialog_p?.dismiss()
                } else {
                    Snackbar.make(rootLayout, "Please enter amount 1-10000", Snackbar.LENGTH_SHORT)
                        .show()
                }

            } else {
                Snackbar.make(rootLayout, "Please enter amount", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}