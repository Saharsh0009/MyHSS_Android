package com.uk.myhss.Guru_Dakshina_OneTime

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.Functions
import com.myhss.Utils.InputFilterMinMax
import com.uk.myhss.R
import com.uk.myhss.Utils.SessionManager
import java.util.*


class GuruDakshinaOneTimeThirdActivity() : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    private var User_Id: String = ""

    private lateinit var edit_address: TextInputEditText
    private lateinit var edit_city: TextInputEditText
    private lateinit var edit_country: TextInputEditText
    private lateinit var edit_postcode: TextInputEditText

    private lateinit var rootLayout: LinearLayout
    private lateinit var back_layout: LinearLayout
    private lateinit var next_layout: LinearLayout
    private lateinit var donate_amount_txt: TextView
    private lateinit var edit_payment: ImageView
    var dialog: Dialog? = null

    @SuppressLint("NewApi", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gurudakshina_third)

        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("OneTimeDakshinaStep3VC")
        sessionManager.firebaseAnalytics.setUserProperty(
            "OneTimeDakshinaStep3VC",
            "GuruDakshinaOneTimeThirdActivity"
        )

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        val back_arrow = findViewById<ImageView>(R.id.back_arrow)
        val header_title = findViewById<TextView>(R.id.header_title)

        header_title.text = getString(R.string.one_time_dakshina)

        rootLayout = findViewById(R.id.rootLayout)
        back_layout = findViewById(R.id.back_layout)
        next_layout = findViewById(R.id.next_layout)

        edit_address = findViewById(R.id.edit_address)
        edit_city = findViewById(R.id.edit_city)
        edit_country = findViewById(R.id.edit_country)
        edit_postcode = findViewById(R.id.edit_postcode)
        donate_amount_txt = findViewById(R.id.donate_amount_txt)
        edit_payment = findViewById(R.id.edit_payment)


        if (intent.getStringExtra("Amount") != "") {
            donate_amount_txt.text =
                intent.getStringExtra("Amount")
        }


        edit_address.setText(sessionManager.fetchLineOne())
        edit_city.setText(sessionManager.fetchCITY())
        edit_country.setText(sessionManager.fetchCOUNTRY())
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



        next_layout.setOnClickListener(DebouncedClickListener {
            if (edit_address.text.toString() == "") {
                Snackbar.make(rootLayout, "Please enter address", Snackbar.LENGTH_SHORT).show()
            } else if (edit_city.text.toString() == "") {
                Snackbar.make(rootLayout, "Please enter city", Snackbar.LENGTH_SHORT).show()
            } else if (edit_country.text.toString() == "") {
                Snackbar.make(rootLayout, "Please enter country", Snackbar.LENGTH_SHORT).show()
            } else if (edit_postcode.text.toString() == "") {
                Snackbar.make(rootLayout, "Please enter post code", Snackbar.LENGTH_SHORT).show()
            } else if (intent.getStringExtra("Amount") != "") {
                val i =
                    Intent(
                        this@GuruDakshinaOneTimeThirdActivity,
                        GuruDakshinaOneTimeFourthActivity::class.java
                    )
                i.putExtra("Amount", donate_amount_txt.text.toString())
                i.putExtra("GIFTAID_ID", intent.getStringExtra("GIFTAID_ID"))
                i.putExtra("giving_dakshina", intent.getStringExtra("giving_dakshina"))
                i.putExtra("donating_dakshina", intent.getStringExtra("donating_dakshina"))
                startActivity(i)
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
            edit_amount.text = donate_amount_txt.text
        }

        btnOk.setOnClickListener(DebouncedClickListener {
            if (edit_amount.text.toString().isNotEmpty()) {
                edit_amount.filters = arrayOf<InputFilter>(
                    InputFilterMinMax(
                        "1",
                        "10000"
                    )
                )
                if (Integer.valueOf(edit_amount.text.toString()) > 0 && Integer.valueOf(edit_amount.text.toString()) <= 10000) {
                    donate_amount_txt.text =
                        edit_amount.text.toString()
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

}