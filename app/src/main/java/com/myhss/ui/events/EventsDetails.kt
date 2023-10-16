package com.myhss.ui.events

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.util.Util
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.UtilCommon
import com.myhss.ui.events.model.Eventdata
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
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
        if (intent.getStringExtra("sEveType_") == "2") { // past event or upcoming event
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

        if (eventData.event_mode == "0") {
            binding.eventAddress.text = "Online Event"
        } else {
            binding.eventAddress.text =
                "${eventData.building_name}\n${eventData.address_line_1}\n${eventData.address_line_2}\n" +
                        "${eventData.city}, ${eventData.country}\n" +
                        "${eventData.postal_code}"
        }

        when (eventData.event_attendee_gender) {
            "M" -> {
                binding.eventGender.text = "Male"
            }

            "F" -> {
                binding.eventGender.text = "Female"
            }

            "A" -> {
                binding.eventGender.text = "All"
            }

            else -> {
                binding.eventGender.text = "All"
            }
        }


        if (eventData.event_chargable_or_not.isNullOrEmpty() || eventData.event_chargable_or_not.isNullOrBlank() || eventData.event_chargable_or_not == "1") {
            binding.eventFees.text = "Free"
        } else {
            var payinfo = ""
            for (i in 0 until eventData.event_chargable_info!!.size) {
                if (payinfo.length == 0) {
                    payinfo =
                        eventData.event_chargable_info[i].event_charge_category_label + "  :  £ " + eventData.event_chargable_info[i].event_charge_value
                } else {
                    payinfo =
                        payinfo + "\n" + eventData.event_chargable_info[i].event_charge_category_label + "  :  £ " + eventData.event_chargable_info[i].event_charge_value
                }
            }
            binding.eventFees.text = payinfo
        }


        if (!eventData.event_contact.isNullOrEmpty()) {
            binding.eventContacts.text =
                eventData.event_contact[0]?.eve_cont_name + "\n" + eventData.event_contact[0]?.eve_cont_email + "\n" + eventData.event_contact[0]?.eve_cont_no
        }



        binding.eventDetail.text = UtilCommon.htmlToText(eventData.event_detailed_description)

        Glide.with(applicationContext)
//            .load(eventData.event_img_detail)
            .load(MyHssApplication.IMAGE_URL_EVENT + eventData.event_img_detail)
            .transition(DrawableTransitionOptions.withCrossFade())
            .transform(RoundedCorners(30))
            .placeholder(R.drawable.splash) // Placeholder image while loading
            .error(R.drawable.splash) // Image to display on error
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