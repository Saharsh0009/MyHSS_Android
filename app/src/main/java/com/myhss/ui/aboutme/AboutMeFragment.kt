package com.uk.myhss.ui.policies

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.uk.myhss.AddMember.AddMemberFirstActivity
import com.uk.myhss.R
import com.uk.myhss.Utils.SessionManager

class AboutMeFragment : Fragment() {
    private lateinit var sessionManager: SessionManager
    lateinit var root_view: LinearLayout

    lateinit var my_shakha_icon_view: RelativeLayout
    lateinit var address_icon_view: RelativeLayout
    lateinit var spoken_lang_icon_view: RelativeLayout
    lateinit var occupation_icon_view: RelativeLayout
    lateinit var mainedit_layout: LinearLayout

    lateinit var shakha__address: TextView
    lateinit var address_txt: TextView
    lateinit var spoken_lang_txt: TextView
    lateinit var occupation_txt: TextView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_about_me, container, false)
        sessionManager = SessionManager(requireContext())

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
        sessionManager.firebaseAnalytics.setUserId("ProfileVC_AboutMeView")
        sessionManager.firebaseAnalytics.setUserProperty("ProfileVC_AboutMeView", "AboutMeFragment")

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        root_view = root.findViewById(R.id.root_view)

        my_shakha_icon_view = root.findViewById(R.id.my_shakha_icon_view)
        address_icon_view = root.findViewById(R.id.address_icon_view)
        spoken_lang_icon_view = root.findViewById(R.id.spoken_lang_icon_view)
        occupation_icon_view = root.findViewById(R.id.occupation_icon_view)
        mainedit_layout = root.findViewById(R.id.mainedit_layout)

        shakha__address = root.findViewById(R.id.shakha__address)
        address_txt = root.findViewById(R.id.address_txt)
        spoken_lang_txt = root.findViewById(R.id.spoken_lang_txt)
        occupation_txt = root.findViewById(R.id.occupation_txt)

        shakha__address.text = sessionManager.fetchSHAKHANAME()
        address_txt.text = sessionManager.fetchADDRESS()+", "+sessionManager.fetchCITY()+
                ", "+sessionManager.fetchPOSTCODE()+", "+sessionManager.fetchCOUNTRY()
        spoken_lang_txt.text = sessionManager.fetchSPOKKENLANGUAGE()
        occupation_txt.text = sessionManager.fetchOCCUPATIONNAME()

        my_shakha_icon_view.setOnClickListener {
//            Snackbar.make(root_view, "My Shakha", Snackbar.LENGTH_SHORT).show()
        }

        address_icon_view.setOnClickListener {
//            Snackbar.make(root_view, "My Address", Snackbar.LENGTH_SHORT).show()
        }

        spoken_lang_icon_view.setOnClickListener {
//            Snackbar.make(root_view, "Spoken Language", Snackbar.LENGTH_SHORT).show()
        }

        occupation_icon_view.setOnClickListener {
//            Snackbar.make(root_view, "Occupation", Snackbar.LENGTH_SHORT).show()
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