package com.myhss.ui.events

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.DebugLog
import com.myhss.ui.events.model.Eventdata
import com.myhss.ui.suryanamaskar.Model.BarchartDataModel
import com.uk.myhss.R
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.databinding.ActivityEventsDetailsBinding

class EventsDetails : AppCompatActivity() {
    private lateinit var binding: ActivityEventsDetailsBinding
    private lateinit var sessionManager: SessionManager

    @SuppressLint("NewApi", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventsDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        binding.topbar.headerTitle.text = getString(R.string.view_event)
        val eventData = intent.getSerializableExtra("eventdata") as Eventdata
        if (intent.getStringExtra("sEveType_") == "2") {
            binding.btnRegister.visibility = View.GONE
        }
        binding.topbar.backArrow.setOnClickListener(DebouncedClickListener {
            finish()
        })
        binding.btnRegister.setOnClickListener(DebouncedClickListener {
            val i = Intent(this@EventsDetails, EventRegistrationStepOneActivity::class.java)
            startActivity(i)
        })

        binding.eventTitleNmae.text = eventData.event_title
        binding.eventTime.text = eventData.event_start_date + " TO \n" + eventData.event_end_date
        binding.eventAddress.text =
            "${eventData.building_name}\n${eventData.address_line_1}\n${eventData.address_line_2}\n" +
                    "${eventData.city}, ${eventData.country}\n" +
                    "${eventData.postal_code}"
        binding.eventContacts.text =
            Html.fromHtml(eventData.event_contact, Html.FROM_HTML_MODE_LEGACY)
//        binding.eventContacts.text = Html.fromHtml("Chintu Parekh<br>info@myhss.org<br>01255 9875585", Html.FROM_HTML_MODE_LEGACY)
        binding.eventDetail.text = eventData.event_detailed_description

        Glide.with(applicationContext)
            .load(eventData.event_img_detail)
            .transition(DrawableTransitionOptions.withCrossFade())
            .transform(RoundedCorners(30))
            .placeholder(R.drawable.app_logo) // Placeholder image while loading
            .error(R.drawable.app_logo) // Image to display on error
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.imgEventBanner)

//        events_list_layout.setOnClickListener(DebouncedClickListener {
//            val i = Intent(this@EventsDetails, QRCodeFragment::class.java)
//            i.putExtra("EVENT", event_name_txt.text.toString())
//            i.putExtra("DISCRIPTION", discription_txt.text.toString())
//            i.putExtra("INFO", info_txt.text.toString())
//            startActivity(i)
//        })
    }
}