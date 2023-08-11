package com.uk.myhss.ui.policies

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.uk.myhss.AddMember.AddMemberFirstActivity
import com.uk.myhss.R
import com.uk.myhss.Utils.SessionManager

class ContactInfoFragment : Fragment() {
    private lateinit var sessionManager: SessionManager
//    private val sharedPrefFile = "MyHss"

    lateinit var mobile_txt: TextView
    lateinit var sec_contact_txt: TextView
    lateinit var name_txt: TextView
    lateinit var phone_no_txt: TextView
    lateinit var email_txt: TextView
    lateinit var relation_ship_txt: TextView
    lateinit var mainedit_layout: LinearLayout

    @SuppressLint("Range", "MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_contact_info, container, false)
        sessionManager = SessionManager(requireContext())

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
        sessionManager.firebaseAnalytics.setUserId("ProfileVC_ContactInfoView")
        sessionManager.firebaseAnalytics.setUserProperty(
            "ProfileVC_ContactInfoView",
            "ContactInfoFragment"
        )

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        mobile_txt = root.findViewById(R.id.mobile_txt)
        sec_contact_txt = root.findViewById(R.id.sec_contact_txt)
        name_txt = root.findViewById(R.id.name_txt)
        phone_no_txt = root.findViewById(R.id.phone_no_txt)
        email_txt = root.findViewById(R.id.email_txt)
        relation_ship_txt = root.findViewById(R.id.relation_ship_txt)
        mainedit_layout = root.findViewById(R.id.mainedit_layout)

        mobile_txt.text = sessionManager.fetchMOBILENO()
        sec_contact_txt.text = sessionManager.fetchSECMOBILENO()
        name_txt.text = sessionManager.fetchGUAEMRNAME()
        phone_no_txt.text = sessionManager.fetchGUAEMRPHONE()
        email_txt.text = sessionManager.fetchGUAEMREMAIL()

        relation_ship_txt.text = sessionManager.fetchGUAEMRRELATIONSHIP()
        if (!sessionManager.fetchGUAEMRRELATIONSHIP_OTHER().isNullOrBlank()) {
            if (!sessionManager.fetchGUAEMRRELATIONSHIP_OTHER().toString().equals("null")) {
                relation_ship_txt.text =
                    sessionManager.fetchGUAEMRRELATIONSHIP() + " | " + sessionManager.fetchGUAEMRRELATIONSHIP_OTHER()
            }
        }
        mainedit_layout.setOnClickListener {
            val i = Intent(requireContext(), AddMemberFirstActivity::class.java)
            i.putExtra("TYPE_SELF", "family")
            i.putExtra("FAMILY", "PROFILE")
            startActivity(i)
        }
        return root
    }
}