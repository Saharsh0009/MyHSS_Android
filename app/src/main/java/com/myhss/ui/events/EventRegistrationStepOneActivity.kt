package com.myhss.ui.events

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.DebugLog
import com.myhss.Utils.Functions
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.databinding.ActivityEventRegistrationStepOneBinding
import com.uk.myhss.ui.linked_family.Model.Get_Member_Listing_Datum
import com.uk.myhss.ui.linked_family.Model.Get_Member_Listing_Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventRegistrationStepOneActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var binding: ActivityEventRegistrationStepOneBinding
    private lateinit var sEventFor: String
    private lateinit var sEventEpipen: String
    private var familyList: List<Get_Member_Listing_Datum>? = null


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventRegistrationStepOneBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sessionManager = SessionManager(this)
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("EventsFragmentVC")
        sessionManager.firebaseAnalytics.setUserProperty(
            "EventsFragmentVC",
            "EventRegistrationStepOne"
        )
        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)

        binding.topbar.headerTitle.text = getString(R.string.event_registration)
        setSelfView()
        binding.topbar.backArrow.setOnClickListener(DebouncedClickListener {
            finish()
        })

        binding.backLayout.setOnClickListener(DebouncedClickListener {
            finish()
        })
        binding.nextLayout.setOnClickListener(DebouncedClickListener {
            if (isValidate()) {
                val i = Intent(
                    this@EventRegistrationStepOneActivity,
                    EventRegistrationStepTwoActivity::class.java
                )
                startActivity(i)
            }
        })

        binding.eventRegisSelfView.setOnClickListener(DebouncedClickListener {
            setSelfView()
        })

        binding.eventRegFamilyView.setOnClickListener(DebouncedClickListener {
            setFamilyView()
        })


        binding.eventEpipenYesView.setOnClickListener(DebouncedClickListener {
            binding.eventEpipenYesView.setBackgroundResource(R.drawable.edit_primery_color_round)
            binding.eventEpipenNoView.setBackgroundResource(R.drawable.edittext_round)
            binding.eventEpipenYesTxt.setTextColor(getColor(R.color.primaryColor))
            binding.eventEpipenYesIcon.setImageResource(R.drawable.righttikmark)
            binding.eventEpipenNoTxt.setTextColor(getColor(R.color.grayColorColor))
            binding.eventEpipenNoImg.setImageResource(R.drawable.righttikmark_gray_icon)
            sEventEpipen = "Yes"
        })

        binding.eventEpipenNoView.setOnClickListener(DebouncedClickListener {
            binding.eventEpipenNoView.setBackgroundResource(R.drawable.edit_primery_color_round)
            binding.eventEpipenYesView.setBackgroundResource(R.drawable.edittext_round)
            binding.eventEpipenNoTxt.setTextColor(getColor(R.color.primaryColor))
            binding.eventEpipenNoImg.setImageResource(R.drawable.righttikmark)
            binding.eventEpipenYesTxt.setTextColor(getColor(R.color.grayColorColor))
            binding.eventEpipenYesIcon.setImageResource(R.drawable.righttikmark_gray_icon)
            sEventEpipen = "No"
        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setFamilyView() {
        binding.eventRegFamilyView.setBackgroundResource(R.drawable.edit_primery_color_round)
        binding.eventRegisSelfView.setBackgroundResource(R.drawable.edittext_round)
        binding.eventRegSelfYesTxt.setTextColor(getColor(R.color.grayColorColor))
        binding.eventRegSelfYesIcon.setImageResource(R.drawable.righttikmark_gray_icon)
        binding.eventRegFamilyYesTxt.setTextColor(getColor(R.color.primaryColor))
        binding.eventRegFamilyYesImg.setImageResource(R.drawable.righttikmark)
        binding.linearSelfView.visibility = View.GONE
        binding.linearFamilyView.visibility = View.VISIBLE
        binding.eventLabelMedicalInfo.text = getString(R.string.specify_medical_info_family)
        sEventFor = "Family"

        if (Functions.isConnectingToInternet(this@EventRegistrationStepOneActivity)) {
            val USERID = sessionManager.fetchUserID()!!
            Log.d("USERID", USERID)
            val TAB = "family"
            val MEMBERID = sessionManager.fetchMEMBERID()!!
            val STATUS = "all"
            val LENGTH = "100"
            val START = "0"
            val SEARCH = ""
            val CHAPTERID = ""
            if (familyList == null) {
                myMemberList(USERID, TAB, MEMBERID, STATUS, LENGTH, START, SEARCH, CHAPTERID)
            } else {

            }
        } else {
            Toast.makeText(
                this@EventRegistrationStepOneActivity,
                resources.getString(R.string.no_connection),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setSelfView() {
        binding.eventRegisSelfView.setBackgroundResource(R.drawable.edit_primery_color_round)
        binding.eventRegFamilyView.setBackgroundResource(R.drawable.edittext_round)
        binding.eventRegSelfYesTxt.setTextColor(getColor(R.color.primaryColor))
        binding.eventRegSelfYesIcon.setImageResource(R.drawable.righttikmark)
        binding.eventRegFamilyYesTxt.setTextColor(getColor(R.color.grayColorColor))
        binding.eventRegFamilyYesImg.setImageResource(R.drawable.righttikmark_gray_icon)
        sEventFor = "Self"
        binding.linearSelfView.visibility = View.VISIBLE
        binding.linearFamilyView.visibility = View.GONE
        binding.eventLabelMedicalInfo.text =
            getString(R.string.specify_medical_info_if_different_from_your_profile)
    }

    private fun isValidate(): Boolean {

        return true
    }

    private fun myMemberList(
        user_id: String, tab: String, member_id: String, status: String,
        length: String, start: String, search: String, chapter_id: String
    ) {
        val pd = CustomProgressBar(this@EventRegistrationStepOneActivity)
        pd.show()
        val call: Call<Get_Member_Listing_Response> =
            MyHssApplication.instance!!.api.get_member_listing(
                user_id, tab, member_id,
                status, length, start, search, chapter_id
            )
        call.enqueue(object : Callback<Get_Member_Listing_Response> {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onResponse(
                call: Call<Get_Member_Listing_Response>,
                response: Response<Get_Member_Listing_Response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    DebugLog.e("status : " + response.body()?.status.toString())
                    if (response.body()?.status!!) {
                        familyList = response.body()!!.data!!
                        if (familyList!!.isNotEmpty()) {
                            setFamilyListSpinner()
                        } else {
                            setSelfView()
                        }
                    } else {
                        Functions.showAlertMessageWithOK(
                            this@EventRegistrationStepOneActivity, "",
//                        "Message",
                            response.body()?.message
                        )
                        setSelfView()
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@EventRegistrationStepOneActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                    setSelfView()
                }
                pd.dismiss()
            }

            @RequiresApi(Build.VERSION_CODES.M)
            override fun onFailure(call: Call<Get_Member_Listing_Response>, t: Throwable) {
                Toast.makeText(this@EventRegistrationStepOneActivity, t.message, Toast.LENGTH_LONG)
                    .show()
                pd.dismiss()
                setSelfView()
            }
        })
    }

    private fun setFamilyListSpinner() {
        val spinnerArrayAdapter = familyList?.let {
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                it.map { it.firstName + " " + it.middleName + " " + it.lastName }
            )
        }
        spinnerArrayAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFamily.adapter = spinnerArrayAdapter
        binding.spinnerFamily.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = familyList?.get(position)
                if (selectedItem != null) {
                    DebugLog.e("selectedItem : " + selectedItem.memberId)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }
}