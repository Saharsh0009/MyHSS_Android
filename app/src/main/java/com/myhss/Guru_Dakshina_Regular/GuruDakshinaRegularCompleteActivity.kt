package com.uk.myhss.Guru_Dakshina_Regular

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.Utils.DebouncedClickListener
import com.uk.myhss.Main.HomeActivity
import com.uk.myhss.R
import com.uk.myhss.Utils.SessionManager


class GuruDakshinaRegularCompleteActivity() : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    private lateinit var rootLayout: LinearLayout

    private lateinit var payment_complete_image: ImageView
    private lateinit var donate_amount_txt: TextView
    private lateinit var success_txt: TextView
    private lateinit var success_message_txt: TextView
    private lateinit var reference_number_txt: TextView
    private lateinit var frequency_txt: TextView
    private lateinit var sort_code_txt: TextView
    private lateinit var account_name_txt: TextView
    private lateinit var account_number_txt: TextView
    private lateinit var message_txt: TextView

    private lateinit var next_layout: LinearLayout

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gurudakshina_complete_regular)

        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("RegularGuruDakshinaSuccessVC")
        sessionManager.firebaseAnalytics.setUserProperty("RegularGuruDakshinaSuccessVC", "GuruDakshinaRegularCompleteActivity")

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        val back_arrow = findViewById<ImageView>(R.id.back_arrow)
        val header_title = findViewById<TextView>(R.id.header_title)
        back_arrow.visibility = View.INVISIBLE

        header_title.text = getString(R.string.regular_dakshina)

        rootLayout = findViewById(R.id.rootLayout)

        payment_complete_image = findViewById(R.id.payment_complete_image)
        next_layout = findViewById(R.id.next_layout)
        donate_amount_txt = findViewById(R.id.donate_amount_txt)
        success_txt = findViewById(R.id.success_txt)
        success_message_txt = findViewById(R.id.success_message_txt)
        reference_number_txt = findViewById(R.id.reference_number_txt)
        frequency_txt = findViewById(R.id.frequency_txt)
        sort_code_txt = findViewById(R.id.sort_code_txt)
        account_name_txt = findViewById(R.id.account_name_txt)
        account_number_txt = findViewById(R.id.account_number_txt)
        message_txt = findViewById(R.id.message_txt)

//        donate_amount_txt.text = getString(R.string.pound_icon) + " " + intent.getStringExtra("paidAmount")
        donate_amount_txt.text = getString(R.string.pound_icon) + " " + intent.getStringExtra("paidAmount")  // intent.getStringExtra("Amount")
        reference_number_txt.text = intent.getStringExtra("referenceNo")
        frequency_txt.text = intent.getStringExtra("frequency")
        sort_code_txt.text = intent.getStringExtra("sortCode")
        account_name_txt.text = intent.getStringExtra("accountName")
        account_number_txt.text = intent.getStringExtra("accountNumber")
        message_txt.text = intent.getStringExtra("giftAid")

        next_layout.setOnClickListener(DebouncedClickListener {
            val i =
                Intent(this@GuruDakshinaRegularCompleteActivity, HomeActivity::class.java)
//                    MainActivity::class.java)
            startActivity(i)
            finishAffinity()
        })

        if (intent.getStringExtra("Amount") != "") {
            donate_amount_txt.text = intent.getStringExtra("Amount")
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
//        super.onBackPressed()
    }
}