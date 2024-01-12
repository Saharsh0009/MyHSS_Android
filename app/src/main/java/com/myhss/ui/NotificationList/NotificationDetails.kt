package com.myhss.ui.SuchanaBoard

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.Utils.DebouncedClickListener
import com.uk.myhss.R
import com.uk.myhss.Utils.SessionManager

class NotificationDetails : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    lateinit var back_arrow: ImageView
    lateinit var header_title: TextView
    lateinit var suchna_title: TextView
    lateinit var suchna_discription: TextView
    lateinit var suchna_time: TextView

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.suchana_details)

        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("Notification")
        sessionManager.firebaseAnalytics.setUserProperty("Notification", "SuchanaDetails")

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        back_arrow = findViewById(R.id.back_arrow)
        header_title = findViewById(R.id.header_title)
        suchna_title = findViewById(R.id.suchna_title)
        suchna_discription = findViewById(R.id.suchna_discription)
        suchna_time = findViewById(R.id.txtTime)
        back_arrow.setOnClickListener(DebouncedClickListener { finish() })

        header_title.text = intent.getStringExtra("Suchana_Type")
        suchna_title.text = intent.getStringExtra("Suchana_Title")
        suchna_discription.text = intent.getStringExtra("Suchana_DiscriptionNew")
        suchna_time.text = intent.getStringExtra("Suchana_time")
    }
}