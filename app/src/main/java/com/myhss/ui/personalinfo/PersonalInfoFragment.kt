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
import com.myhss.Utils.DebouncedClickListener
import com.uk.myhss.AddMember.AddMemberFirstActivity
import com.uk.myhss.R
import com.uk.myhss.Utils.SessionManager

class PersonalInfoFragment : Fragment() {

    private lateinit var sessionManager: SessionManager
//    private val sharedPrefFile = "MyHss"

    lateinit var role_txt: TextView
    lateinit var first_name_txt: TextView
    lateinit var middle_name_txt: TextView
    lateinit var last_name_txt: TextView
    lateinit var email_txt: TextView
    lateinit var dob_txt: TextView
    lateinit var shakha_txt: TextView
    lateinit var nagar_txt: TextView
    lateinit var vibhag_txt: TextView
    lateinit var mainedit_layout: LinearLayout

    @SuppressLint("MissingPermission", "Range")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_personal_info, container, false)

        sessionManager = SessionManager(requireContext())

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
        sessionManager.firebaseAnalytics.setUserId("ProfileVC_PersonalInfoView")
        sessionManager.firebaseAnalytics.setUserProperty(
            "ProfileVC_PersonalInfoView",
            "PersonalInfoFragment"
        )

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        role_txt = root.findViewById(R.id.role_txt)
        first_name_txt = root.findViewById(R.id.first_name_txt)
        middle_name_txt = root.findViewById(R.id.middle_name_txt)
        last_name_txt = root.findViewById(R.id.last_name_txt)
        email_txt = root.findViewById(R.id.email_txt)
        dob_txt = root.findViewById(R.id.dob_txt)
        shakha_txt = root.findViewById(R.id.shakha_txt)
        nagar_txt = root.findViewById(R.id.nagar_txt)
        vibhag_txt = root.findViewById(R.id.vibhag_txt)
        mainedit_layout = root.findViewById(R.id.mainedit_layout)

        role_txt.text = sessionManager.fetchUSERROLE()
        first_name_txt.text = sessionManager.fetchFIRSTNAME()
        middle_name_txt.text = sessionManager.fetchMIDDLENAME()
        last_name_txt.text = sessionManager.fetchSURNAME()
        email_txt.text = sessionManager.fetchUSEREMAIL()
        dob_txt.text = sessionManager.fetchDOB()
        shakha_txt.text = sessionManager.fetchSHAKHANAME()
        nagar_txt.text = sessionManager.fetchNAGARNAME()
        vibhag_txt.text = sessionManager.fetchVIBHAGNAME()

        mainedit_layout.setOnClickListener(DebouncedClickListener {
            val i = Intent(requireContext(), AddMemberFirstActivity::class.java)
            i.putExtra("TYPE_SELF", "family")
            i.putExtra("FAMILY", "PROFILE")
            startActivity(i)
        })

        return root
    }
}