package com.uk.myhss.Guru_Dakshina_OneTime

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
import com.myhss.Utils.DebugLog
import com.myhss.ui.events.EventRegistrationViewActivity
import com.uk.myhss.Main.HomeActivity
import com.uk.myhss.R
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.ui.dashboard.EventsFragment


class GuruDakshinaOneTimeCompleteActivity() : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var rootLayout: LinearLayout
    private lateinit var payment_complete_image: ImageView
    private lateinit var donate_amount_txt: TextView
    private lateinit var success_txt: TextView
    private lateinit var order_id_txt: TextView
    private lateinit var payment_status_txt: TextView
    private lateinit var message_txt: TextView
    private lateinit var label_message_txt: TextView
    private lateinit var txt_next_button: TextView
    private lateinit var next_layout: LinearLayout
    private lateinit var screenType: String

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gurudakshina_complete)

        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("OneTimeDakshinaSuccessVC")
        sessionManager.firebaseAnalytics.setUserProperty(
            "OneTimeDakshinaSuccessVC",
            "GuruDakshinaOneTimeCompleteActivity"
        )

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        val back_arrow = findViewById<ImageView>(R.id.back_arrow)
        val header_title = findViewById<TextView>(R.id.header_title)
        back_arrow.visibility = View.INVISIBLE
        rootLayout = findViewById(R.id.rootLayout)
        payment_complete_image = findViewById(R.id.payment_complete_image)
        next_layout = findViewById(R.id.next_layout)
        donate_amount_txt = findViewById(R.id.donate_amount_txt)
        success_txt = findViewById(R.id.success_txt)
        order_id_txt = findViewById(R.id.order_id_txt)
        payment_status_txt = findViewById(R.id.payment_status_txt)
        message_txt = findViewById(R.id.message_txt)
        txt_next_button = findViewById(R.id.txt_next_button)
        label_message_txt = findViewById(R.id.label_message_txt)


        DebugLog.e("Amoutnt  : " + intent.getStringExtra("paidAmount"))
        donate_amount_txt.text =
            getString(R.string.pound_icon) + " " + intent.getStringExtra("paidAmount")
        order_id_txt.text = intent.getStringExtra("orderId")
        payment_status_txt.text = intent.getStringExtra("status")
        message_txt.text = intent.getStringExtra("giftAid")
        screenType = intent.getStringExtra("screen")
            .toString() // 1-> One time GD Payment, 2-> Event Regis Payment

        when (screenType) {
            "1" -> {
                header_title.text = getString(R.string.one_time_dakshina)
                next_layout.setOnClickListener(DebouncedClickListener {
                    val i =
                        Intent(this@GuruDakshinaOneTimeCompleteActivity, HomeActivity::class.java)
                    startActivity(i)
                    finishAffinity()
                })
            }

            "2" -> {
                header_title.text = getString(R.string.event_registration)
                message_txt.visibility = View.GONE
                label_message_txt.visibility = View.GONE
                txt_next_button.text = getString(R.string.view_your_registration)

                next_layout.setOnClickListener(DebouncedClickListener {
                    val i =
                        Intent(
                            this@GuruDakshinaOneTimeCompleteActivity,
                            EventRegistrationViewActivity::class.java
                        )
                    startActivity(i)
                    finishAffinity()
                })
            }
        }


    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        when (screenType) {
            "1" -> {
                val i = Intent(this@GuruDakshinaOneTimeCompleteActivity, HomeActivity::class.java)
                startActivity(i)
                finishAffinity()
            }

            "2" -> {
                val i = Intent(this@GuruDakshinaOneTimeCompleteActivity, EventsFragment::class.java)
                startActivity(i)
                finishAffinity()
            }
        }

    }
}