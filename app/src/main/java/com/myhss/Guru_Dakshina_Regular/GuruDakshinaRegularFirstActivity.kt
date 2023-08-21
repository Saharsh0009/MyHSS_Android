package com.uk.myhss.Guru_Dakshina_Regular

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.Functions
import com.uk.myhss.R
import com.myhss.Utils.InputFilterMinMax
import com.uk.myhss.Utils.SessionManager
import java.util.*


class GuruDakshinaRegularFirstActivity() : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    var dialog: Dialog? = null
    private var Amount: String = ""
    private var MESSAGE: String = ""

    private lateinit var edit_amount: EditText

    private lateinit var user_name: TextView
    private lateinit var shakha_name: TextView

    private lateinit var next_layout: LinearLayout
    private lateinit var rootLayout: LinearLayout

    @SuppressLint("NewApi", "Range", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gurudakshina_first)

        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("RegularGuruDakshinaStep1VC")
        sessionManager.firebaseAnalytics.setUserProperty(
            "RegularGuruDakshinaStep1VC",
            "GuruDakshinaRegularFirstActivity"
        )

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        val back_arrow = findViewById<ImageView>(R.id.back_arrow)
        val info_tooltip = findViewById<ImageView>(R.id.info_tooltip)
        val header_title = findViewById<TextView>(R.id.header_title)

        info_tooltip.visibility = View.VISIBLE

        header_title.text = getString(R.string.regular_dakshina)

        MESSAGE = "Namaste,\n" +
                "\n" +
                "After the form is completed and submitted, you will receive an email with the HSS (UK) bank details and your reference number. Use the details you received via email to setup your Standing Order with your Bank. This can be done online by logging into your Bank Account.\n" +
                "\n" +
                "Dhanyavaad"
        depositDialog(MESSAGE)

        info_tooltip.setOnClickListener(DebouncedClickListener {
            MESSAGE = "Namaste,\n" +
                    "\n" +
                    "After the form is completed and submitted, you will receive an email with the HSS (UK) bank details and your reference number. Use the details you received via email to setup your Standing Order with your Bank. This can be done online by logging into your Bank Account.\n" +
                    "\n" +
                    "Dhanyavaad"
            depositDialog(MESSAGE)
        })

        user_name = findViewById(R.id.user_name)
        shakha_name = findViewById(R.id.shakha_name)
        next_layout = findViewById(R.id.next_layout)
        edit_amount = findViewById(R.id.edit_amount)
        rootLayout = findViewById(R.id.rootLayout)

        user_name.text = sessionManager.fetchUSERNAME()!!.capitalize(Locale.ROOT)
        shakha_name.text = sessionManager.fetchSHAKHANAME()!!.capitalize(Locale.ROOT)

        back_arrow.setOnClickListener(DebouncedClickListener {
            finish()
        })

        next_layout.setOnClickListener(DebouncedClickListener {
            Amount = edit_amount.text.toString()
            if (edit_amount.text.toString().isNotEmpty()) {
                edit_amount.filters = arrayOf<InputFilter>(
                    InputFilterMinMax(
                        "1",
                        "10000"
                    )
                )
                if (Integer.valueOf(edit_amount.text.toString()) > 0 && Integer.valueOf(edit_amount.text.toString()) <= 1000) {
                    val i =
                        Intent(
                            this@GuruDakshinaRegularFirstActivity,
                            GuruDakshinaRegularSecondActivity::class.java
                        )
                    i.putExtra("Amount", Amount)
                    startActivity(i)
                } else {
                    Snackbar.make(rootLayout, "Please enter amount 1-10000", Snackbar.LENGTH_SHORT)
                        .show()
                }

            } else {
                Snackbar.make(rootLayout, "Please enter amount", Snackbar.LENGTH_SHORT).show()
            }
        })
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

        btnOk.setOnClickListener(DebouncedClickListener {
            dialog?.dismiss()
        })
    }
}