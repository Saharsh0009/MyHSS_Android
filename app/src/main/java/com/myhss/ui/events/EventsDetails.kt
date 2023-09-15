package com.myhss.ui.events

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.Utils.DebouncedClickListener
import com.uk.myhss.R
import com.uk.myhss.Utils.SessionManager

class EventsDetails : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var rootLayout: RelativeLayout
    private lateinit var btnRegister: AppCompatButton

    @SuppressLint("NewApi", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events_details)

        sessionManager = SessionManager(this)
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
        header_title.text = getString(R.string.view_event)
        rootLayout = findViewById(R.id.rootLayout)
        btnRegister = findViewById(R.id.btnRegister)

        back_arrow.setOnClickListener(DebouncedClickListener {
            finish()
        })

        btnRegister.setOnClickListener(DebouncedClickListener {
            val i = Intent(this@EventsDetails, EventRegistrationStepOneActivity::class.java)
            startActivity(i)
        })

//        events_list_layout.setOnClickListener(DebouncedClickListener {
//            val i = Intent(this@EventsDetails, QRCodeFragment::class.java)
//            i.putExtra("EVENT", event_name_txt.text.toString())
//            i.putExtra("DISCRIPTION", discription_txt.text.toString())
//            i.putExtra("INFO", info_txt.text.toString())
//            startActivity(i)
//        })
    }
}