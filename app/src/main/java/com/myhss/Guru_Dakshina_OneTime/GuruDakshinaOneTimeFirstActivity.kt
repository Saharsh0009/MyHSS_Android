package com.uk.myhss.Guru_Dakshina_OneTime

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
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


class GuruDakshinaOneTimeFirstActivity() : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    private var Amount: String = ""

    private lateinit var edit_amount: EditText

    private lateinit var user_name: TextView
    private lateinit var shakha_name: TextView
    private lateinit var img_payment_step_one: ImageView

    private lateinit var next_layout: LinearLayout
    private lateinit var rootLayout: LinearLayout

    @SuppressLint("NewApi", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gurudakshina_first)

        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("OneTimeDakshinaStep1VC")
        sessionManager.firebaseAnalytics.setUserProperty(
            "OneTimeDakshinaStep1VC", "GuruDakshinaOneTimeFirstActivity"
        )

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        val back_arrow = findViewById<ImageView>(R.id.back_arrow)
        val header_title = findViewById<TextView>(R.id.header_title)

        header_title.text = getString(R.string.one_time_dakshina)

        user_name = findViewById(R.id.user_name)
        shakha_name = findViewById(R.id.shakha_name)
        next_layout = findViewById(R.id.next_layout)
        edit_amount = findViewById(R.id.edit_amount)
        rootLayout = findViewById(R.id.rootLayout)
        img_payment_step_one = findViewById(R.id.img_payment_step_one)

        user_name.text = sessionManager.fetchUSERNAME()!!.capitalize(Locale.ROOT)
        shakha_name.text = sessionManager.fetchSHAKHANAME()!!.capitalize(Locale.ROOT)

        img_payment_step_one.setImageResource(R.drawable.addmember_step1)


        back_arrow.setOnClickListener(DebouncedClickListener {
            finish()
        })
        next_layout.setOnClickListener(DebouncedClickListener {
            Amount = edit_amount.text.toString()
            edit_amount.filters = arrayOf<InputFilter>(InputFilterMinMax("1", "10000"))
            if (edit_amount.text.toString().isNotEmpty()) {
                if (Integer.valueOf(edit_amount.text.toString()) > 0 && Integer.valueOf(edit_amount.text.toString()) <= 10000) {
                    val i = Intent(
                        this@GuruDakshinaOneTimeFirstActivity,
                        GuruDakshinaOneTimeSecondActivity::class.java
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
}