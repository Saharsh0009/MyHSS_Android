package com.uk.myhss.ui.policies

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.uk.myhss.R
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.ui.linked_family.MemberShipActivity

class ShakhaFragment : Fragment() {
    private lateinit var sessionManager: SessionManager

    lateinit var membership_view: RelativeLayout
    lateinit var active_membership_view: RelativeLayout
    lateinit var inactive_member: RelativeLayout
    lateinit var rejected_member: RelativeLayout
    lateinit var sankhya_layout: RelativeLayout
    lateinit var root_view: LinearLayout
    lateinit var shakha_layout: RelativeLayout

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_shakha, container, false)

        sessionManager = SessionManager(requireContext())

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
        sessionManager.firebaseAnalytics.setUserId("MyShakhaVC")
        sessionManager.firebaseAnalytics.setUserProperty("MyShakhaVC", "ShakhaFragment")

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        membership_view = root.findViewById(R.id.membership_view)
        active_membership_view = root.findViewById(R.id.active_membership_view)
        inactive_member = root.findViewById(R.id.inactive_member)
        rejected_member = root.findViewById(R.id.rejected_member)
        sankhya_layout = root.findViewById(R.id.sankhya_layout)
        root_view = root.findViewById(R.id.root_view)
        shakha_layout = root.findViewById(R.id.shakha_layout)

        rejected_member.visibility = View.VISIBLE

        membership_view.setOnClickListener {
//            Snackbar.make(root_view, "Member Ship", Snackbar.LENGTH_SHORT).show()
            val i = Intent(context, MemberShipActivity::class.java)
            i.putExtra("MEMBERS", "ALL_MEMBERS")
            startActivity(i)
        }

        active_membership_view.setOnClickListener {
//            Snackbar.make(root_view, "Active Member", Snackbar.LENGTH_SHORT).show()
            val i = Intent(context, MemberShipActivity::class.java)
            i.putExtra("MEMBERS", "ACTIVE_MEMBERS")
            startActivity(i)
        }

        inactive_member.setOnClickListener {
            val i = Intent(context, MemberShipActivity::class.java)
            i.putExtra("MEMBERS", "INACTIVE_MEMBERS")
            startActivity(i)
        }

        rejected_member.setOnClickListener {
//            Snackbar.make(root_view, "Rejected Member", Snackbar.LENGTH_SHORT).show()
            val i = Intent(context, MemberShipActivity::class.java)
            i.putExtra("MEMBERS", "REJECTED_MEMBERS")
            startActivity(i)
        }

        sankhya_layout.setOnClickListener {
            startActivity(Intent(context, SankhyaActivity::class.java))
        }

        return root
    }
}