package com.uk.myhss.Guru_Dakshina_Regular

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.uk.myhss.Guru_Dakshina_OneTime.Model.Get_Regular.Get_Create_Regular
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.Functions
import com.myhss.Utils.InputFilterMinMax
import com.uk.myhss.Utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class GuruDakshinaRegularThirdActivity() : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    private var User_Id: String = ""
    private var Member_Id: String = ""
    private var Amount: String = ""
    private var Start_date: String = ""
    private var Recurring: String = ""
    private var Is_Linked_Member: String = ""
    private var Gift_aid: String = ""
    private var Is_prunima_Dashina: String = ""
    private var Line_one: String = ""
    private var City: String = ""
    private var Country: String = ""
    private var Post_Code: String = ""
    private var Dashina: String = ""
    private var AMOUNT_PAID: String = ""

    var dialog: Dialog? = null

    private lateinit var edit_address: TextInputEditText
    private lateinit var edit_city: TextInputEditText
    private lateinit var edit_country: TextInputEditText
    private lateinit var edit_postcode: TextInputEditText

    private lateinit var donate_amount_txt: TextView
    private lateinit var edit_payment: ImageView

    private lateinit var rootLayout: LinearLayout
    private lateinit var back_layout: LinearLayout
    private lateinit var next_layout: LinearLayout

    @SuppressLint("NewApi", "Range", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gurudakshina_third_regular)

        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("RegularGuruDakshinaStep3VC")
        sessionManager.firebaseAnalytics.setUserProperty(
            "RegularGuruDakshinaStep3VC",
            "GuruDakshinaRegularThirdActivity"
        )

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        val back_arrow = findViewById<ImageView>(R.id.back_arrow)
        val header_title = findViewById<TextView>(R.id.header_title)

        header_title.text = getString(R.string.regular_dakshina)

        rootLayout = findViewById(R.id.rootLayout)
        back_layout = findViewById(R.id.back_layout)
        next_layout = findViewById(R.id.next_layout)

        edit_address = findViewById(R.id.edit_address)
        edit_city = findViewById(R.id.edit_city)
        edit_country = findViewById(R.id.edit_country)
        edit_postcode = findViewById(R.id.edit_postcode)

        donate_amount_txt = findViewById(R.id.donate_amount_txt)
        edit_payment = findViewById(R.id.edit_payment)

        edit_address.setText(sessionManager.fetchLineOne())
        edit_city.setText(sessionManager.fetchCITY())

        if(sessionManager.fetchCOUNTRY().isNullOrEmpty() || sessionManager.fetchCOUNTRY().equals("null")){
            edit_country.setText("NA")
        }else{
            edit_country.setText(sessionManager.fetchCOUNTRY())
        }



        edit_postcode.setText(sessionManager.fetchPOSTCODE())

        back_arrow.setOnClickListener(DebouncedClickListener {
            finish()
        })

        back_layout.setOnClickListener(DebouncedClickListener {
            finish()
        })

        edit_payment.setOnClickListener(DebouncedClickListener {
            depositDialog()
        })

        if (intent.getStringExtra("Amount") != "") {
            AMOUNT_PAID = intent.getStringExtra("Amount")!!
            donate_amount_txt.text = getString(R.string.pound_icon) + " " + AMOUNT_PAID
        }

        next_layout.setOnClickListener(DebouncedClickListener {
            if (edit_address.text.toString() == "") {
                Snackbar.make(rootLayout, "Please enter address", Snackbar.LENGTH_SHORT).show()
            } else if (edit_city.text.toString() == "") {
                Snackbar.make(rootLayout, "Please enter city", Snackbar.LENGTH_SHORT).show()
            } else if (edit_country.text.toString() == "" || edit_country.text.toString().isNullOrEmpty()) {
                Snackbar.make(rootLayout, "Please enter country", Snackbar.LENGTH_SHORT).show()
            } else if (edit_postcode.text.toString() == "") {
                Snackbar.make(rootLayout, "Please enter post code", Snackbar.LENGTH_SHORT).show()
            } else if (Functions.isConnectingToInternet(this@GuruDakshinaRegularThirdActivity)) {
                User_Id = sessionManager.fetchUserID()!!
                Member_Id = sessionManager.fetchMEMBERID()!!
                Amount = AMOUNT_PAID //intent.getStringExtra("Amount")!!
                Start_date = intent.getStringExtra("DATE")!!
                Recurring = intent.getStringExtra("DONATE_ID")!!
                Is_Linked_Member = intent.getStringExtra("donating_dakshina")!!  //"Individual"
                Gift_aid = intent.getStringExtra("GIFTAID_ID")!!
//                Is_prunima_Dashina = "1"
                Line_one = sessionManager.fetchLineOne()!!
                City = sessionManager.fetchCITY()!!
                Country = sessionManager.fetchCOUNTRY()!!
                Post_Code = sessionManager.fetchPOSTCODE()!!
                Dashina = getString(R.string.regular)

                myDonate(
                    User_Id,
                    Member_Id,
                    Amount,
                    Start_date,
                    Recurring,
                    Is_Linked_Member,
                    Gift_aid,
//                    Is_prunima_Dashina,
                    Line_one,
                    City,
                    Country,
                    Post_Code,
                    Dashina
                )
            } else {
                Toast.makeText(
                    this@GuruDakshinaRegularThirdActivity,
                    resources.getString(R.string.no_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun depositDialog() {
        // Deposit Dialog
        if (dialog == null) {
            dialog = Dialog(this, R.style.StyleCommonDialog)
        }
        dialog?.setContentView(R.layout.edit_dialog_diposit_money)
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.show()

        val edit_amount = dialog!!.findViewById(R.id.edit_amount) as TextView
        val btnOk = dialog!!.findViewById(R.id.btnOk) as TextView

        if (intent.getStringExtra("Amount") != "") {
            edit_amount.text = AMOUNT_PAID // intent.getStringExtra("Amount")
        }

        btnOk.setOnClickListener(DebouncedClickListener {
            if (edit_amount.text.toString().isNotEmpty()) {
                edit_amount.filters = arrayOf<InputFilter>(
                    InputFilterMinMax(
                        "1",
                        "10000"
                    )
                )
                if (Integer.valueOf(edit_amount.text.toString()) > 0 && Integer.valueOf(edit_amount.text.toString()) <= 1000) {
                    donate_amount_txt.text =
                        getString(R.string.pound_icon) + " " + edit_amount.text.toString()
                    AMOUNT_PAID = edit_amount.text.toString()
                    dialog?.dismiss()
                } else {
                    Snackbar.make(edit_amount, "Please enter amount 1-10000", Snackbar.LENGTH_SHORT)
                        .show()
                }

            } else {
                Snackbar.make(edit_amount, "Please enter amount", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    /*Guru_Payment_Donate API*/
    private fun myDonate(
        user_id: String,
        member_id: String,
        amount: String,
        start_date: String,
        recurring: String,
        is_linked_member: String,
        gift_aid: String,
//        is_purnima_dakshina: String,
        line1: String,
        city: String,
        country: String,
        postal_code: String,
        dakshina: String
    ) {
        next_layout.isEnabled = false
        back_layout.isEnabled = false
        val pd = CustomProgressBar(this@GuruDakshinaRegularThirdActivity)
        pd.show()
        val call: Call<Get_Create_Regular> =
            MyHssApplication.instance!!.api.get_create_regular(
                user_id,
                member_id,
                amount,
                start_date,
                recurring,
                is_linked_member,
                gift_aid,
//                is_purnima_dakshina,
                line1,
                city,
                country,
                postal_code,
                dakshina,
                "1"
            )
        call.enqueue(object : Callback<Get_Create_Regular> {
            override fun onResponse(
                call: Call<Get_Create_Regular>,
                response: Response<Get_Create_Regular>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {

                        val data_get = response.body()!!.data!![0]
//                        Log.d("accountName", data_get.accountName.toString())
//                        Log.d("accountNumber", data_get.accountNumber.toString())
//                        Log.d("frequency", data_get.frequency.toString())
//                        Log.d("giftAid", data_get.giftAid.toString())
//                        Log.d("paid_amount", data_get.paidAmount.toString())
//                        Log.d("referenceNo", data_get.referenceNo.toString())
//                        Log.d("sortCode", data_get.sortCode.toString())

                        val alertBuilder =
                            AlertDialog.Builder(this@GuruDakshinaRegularThirdActivity) // , R.style.dialog_custom

                        alertBuilder.setTitle(getString(R.string.payment_method))
                        alertBuilder.setMessage(response.body()?.message)
                        alertBuilder.setPositiveButton(
                            "OK"
                        ) { dialog, which ->
                            val i =
                                Intent(
                                    this@GuruDakshinaRegularThirdActivity,
                                    GuruDakshinaRegularCompleteActivity::class.java
                                )
                            i.putExtra("accountName", data_get.accountName.toString())
                            i.putExtra("accountNumber", data_get.accountNumber.toString())
                            i.putExtra("frequency", data_get.frequency.toString())
                            i.putExtra("giftAid", data_get.giftAid.toString())
                            i.putExtra("referenceNo", data_get.referenceNo.toString())
                            i.putExtra("paid_amount", data_get.paidAmount.toString())
                            i.putExtra("sortCode", data_get.sortCode.toString())
                            i.putExtra("Amount", Amount)
                            startActivity(i)
                            finishAffinity()
                            next_layout.isEnabled = true
                            back_layout.isEnabled = true
                        }
                        val alertDialog = alertBuilder.create()
                        alertDialog.setCanceledOnTouchOutside(false)
                        alertDialog.setCancelable(false)
                        alertDialog.show()

                    } else {
                        Functions.displayMessage(
                            this@GuruDakshinaRegularThirdActivity,
                            response.body()?.message
                        )
                        next_layout.isEnabled = true
                        back_layout.isEnabled = true
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@GuruDakshinaRegularThirdActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                    next_layout.isEnabled = true
                    back_layout.isEnabled = true
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Get_Create_Regular>, t: Throwable) {
                Toast.makeText(this@GuruDakshinaRegularThirdActivity, t.message, Toast.LENGTH_LONG)
                    .show()
                pd.dismiss()
                next_layout.isEnabled = true
                back_layout.isEnabled = true
            }
        })
    }
}