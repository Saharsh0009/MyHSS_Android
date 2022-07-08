package com.myhss.ui.events

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
import com.myhss.QRCode.QRCodeFragment
import com.uk.myhss.R
import com.uk.myhss.Utils.SessionManager
import java.util.*

class EventsDetails : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    private lateinit var event_name_txt: TextView
    private lateinit var shakha_name_txt: TextView
    private lateinit var email_txt: TextView
    private lateinit var mobile_txt: TextView
    private lateinit var discription_txt: TextView
    private lateinit var info_txt: TextView

    private lateinit var events_list_layout: LinearLayout
    private lateinit var rootLayout: LinearLayout

    @SuppressLint("NewApi", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events_details)

        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("EventsDetailsVC")
        sessionManager.firebaseAnalytics.setUserProperty(
            "EventsDetailsVC",
            "EventsDetails"
        )

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        val back_arrow = findViewById<ImageView>(R.id.back_arrow)
        val header_title = findViewById<TextView>(R.id.header_title)

        header_title.text = getString(R.string.events)

        event_name_txt = findViewById(R.id.event_name_txt)
        shakha_name_txt = findViewById(R.id.shakha_name_txt)
        email_txt = findViewById(R.id.email_txt)
        mobile_txt = findViewById(R.id.mobile_txt)
        discription_txt = findViewById(R.id.discription_txt)
        info_txt = findViewById(R.id.info_txt)
        events_list_layout = findViewById(R.id.events_list_layout)

        rootLayout = findViewById(R.id.rootLayout)

        event_name_txt.text = intent.getStringExtra("EVENT")
        shakha_name_txt.text = sessionManager.fetchSHAKHANAME()!!.capitalize(Locale.ROOT)
        email_txt.text = sessionManager.fetchUSEREMAIL()
        mobile_txt.text = sessionManager.fetchMOBILENO()
        discription_txt.text = intent.getStringExtra("DISCRIPTION")
        info_txt.text = intent.getStringExtra("INFO")

        back_arrow.setOnClickListener {
            finish()
        }

        events_list_layout.setOnClickListener {
            val i = Intent(this@EventsDetails, QRCodeFragment::class.java)
            i.putExtra("EVENT", event_name_txt.text.toString())
            i.putExtra("DISCRIPTION", discription_txt.text.toString())
            i.putExtra("INFO", info_txt.text.toString())
            startActivity(i)
        }
    }
}