package com.uk.myhss.ui.guru_dakshina

import android.annotation.SuppressLint
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
import com.uk.myhss.R
import com.uk.myhss.Utils.SessionManager
import java.util.*

class GuruDakshinaRegularDetail : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    private lateinit var user_name_txt: TextView
    private lateinit var shakha_name_txt: TextView
    private lateinit var frequency_of_donation_txt: TextView
    private lateinit var gift_aid_txt: TextView
    private lateinit var date_txt: TextView
    private lateinit var status_txt: TextView
    private lateinit var reference_txt: TextView
    private lateinit var contribution_type_txt: TextView
    private lateinit var guru_dakshina_txt: TextView

    private lateinit var close_layout: LinearLayout
    private lateinit var rootLayout: LinearLayout

    @SuppressLint("NewApi", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guru_dakshina_details)

        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("GuruDakshinaaVC")
        sessionManager.firebaseAnalytics.setUserProperty("GuruDakshinaaVC", "GuruDakshinaRegularDetail")

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        val back_arrow = findViewById<ImageView>(R.id.back_arrow)
        val header_title = findViewById<TextView>(R.id.header_title)

        header_title.text = getString(R.string.guru_dakshina)

        user_name_txt = findViewById(R.id.user_name_txt)
        shakha_name_txt = findViewById(R.id.shakha_name_txt)
        frequency_of_donation_txt = findViewById(R.id.frequency_of_donation_txt)
        gift_aid_txt = findViewById(R.id.gift_aid_txt)
        date_txt = findViewById(R.id.date_txt)
        status_txt = findViewById(R.id.status_txt)
        reference_txt = findViewById(R.id.reference_txt)
        contribution_type_txt = findViewById(R.id.contribution_type_txt)
        guru_dakshina_txt = findViewById(R.id.guru_dakshina_txt)
        close_layout = findViewById(R.id.close_layout)

        rootLayout = findViewById(R.id.rootLayout)

        frequency_of_donation_txt.visibility = View.GONE

        user_name_txt.text = intent.getStringExtra("username_name")!!.capitalize(Locale.ROOT)
        shakha_name_txt.text = intent.getStringExtra("user_shakha_type")!!.capitalize(Locale.ROOT)
        frequency_of_donation_txt.text = intent.getStringExtra("recurring")
        gift_aid_txt.text = intent.getStringExtra("giftAid")!!.capitalize(Locale.ROOT)
        date_txt.text = intent.getStringExtra("date_txt")
        status_txt.text = intent.getStringExtra("status")
        reference_txt.text = intent.getStringExtra("order_id")
        contribution_type_txt.text = intent.getStringExtra("dakshina")
        guru_dakshina_txt.text = intent.getStringExtra("amount_txt")

        back_arrow.setOnClickListener {
            finish()
        }

        close_layout.setOnClickListener {
            finish()
        }
    }
}