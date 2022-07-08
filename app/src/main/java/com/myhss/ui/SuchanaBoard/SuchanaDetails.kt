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
import com.uk.myhss.R
import com.uk.myhss.Utils.SessionManager

class SuchanaDetails: AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    lateinit var back_arrow: ImageView
    lateinit var header_title: TextView
    lateinit var suchna_title: TextView
    lateinit var suchna_discription: TextView

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.suchana_details)

        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("MyShakhaVC")
        sessionManager.firebaseAnalytics.setUserProperty("MyShakhaVC", "SuchanaDetails")

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        back_arrow = findViewById(R.id.back_arrow)
        header_title = findViewById(R.id.header_title)
        suchna_title = findViewById(R.id.suchna_title)
        suchna_discription = findViewById(R.id.suchna_discription)

        header_title.text = intent.getStringExtra("Suchana_Type")

        back_arrow.setOnClickListener {
            finish()
        }

        suchna_title.text = intent.getStringExtra("Suchana_Title")
        suchna_discription.text = intent.getStringExtra("Suchana_Discription")

    }
}