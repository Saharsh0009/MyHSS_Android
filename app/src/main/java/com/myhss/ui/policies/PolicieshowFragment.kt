package com.uk.myhss.ui.policies

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.ui.policies.WebViewPolicies
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager

class PolicieshowFragment : Fragment() {

    private lateinit var sessionManager: SessionManager

    private lateinit var code_of_content_layout: LinearLayout
    private lateinit var data_protection_layout: LinearLayout
    private lateinit var membership_agreement_layout: LinearLayout
    private lateinit var terms_condition_layout: LinearLayout
    private lateinit var privacy_policy_layout: LinearLayout

    private var CODE_CONTENT: String = MyHssApplication.BaseURL+"page/code-of-conduct/6"
    private var DATA_PROTECTION: String = MyHssApplication.BaseURL+"page/data-protection-policy/4"
    private var MEMBERSHIP: String = MyHssApplication.BaseURL+"page/hss-uk-membership-agreement/7"
    private var TERMS_CONDITION: String = MyHssApplication.BaseURL+"page/myhss-terms-conditions/2"
    private var PRIVACY_POLICY: String = MyHssApplication.BaseURL+"page/privacy-policy/1"

    @SuppressLint("MissingPermission")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_policies, container, false)

        sessionManager = SessionManager(requireContext())
        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
        sessionManager.firebaseAnalytics.setUserId("PoliciesVC")
        sessionManager.firebaseAnalytics.setUserProperty("PoliciesVC", "PolicieshowFragment")

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        code_of_content_layout = root.findViewById(R.id.code_of_content_layout)
        data_protection_layout = root.findViewById(R.id.data_protection_layout)
        membership_agreement_layout = root.findViewById(R.id.membership_agreement_layout)
        terms_condition_layout = root.findViewById(R.id.terms_condition_layout)
        privacy_policy_layout = root.findViewById(R.id.privacy_policy_layout)

        code_of_content_layout.setOnClickListener {
            val i = Intent(requireContext(), WebViewPolicies::class.java)
            i.putExtra("CODECONTENT", "Code of Conduct")
//            i.putExtra("CODE_CONTENT", CODE_CONTENT)
            startActivity(i)
        }

        data_protection_layout.setOnClickListener {
            val i = Intent(requireContext(), WebViewPolicies::class.java)
            i.putExtra("DATAPROTECTION", "Data Protection Policy")
//            i.putExtra("DATA_PROTECTION", DATA_PROTECTION)
            startActivity(i)
        }

        membership_agreement_layout.setOnClickListener {
            val i = Intent(requireContext(), WebViewPolicies::class.java)
            i.putExtra("MEMBERSHIP_A", "Membership Agreement")
//            i.putExtra("MEMBERSHIP", MEMBERSHIP)
            startActivity(i)
        }

        terms_condition_layout.setOnClickListener {
            val i = Intent(requireContext(), WebViewPolicies::class.java)
            i.putExtra("TERMSCONDITION", "Terms & Conditions")
//            i.putExtra("TERMS_CONDITION", TERMS_CONDITION)
            startActivity(i)
        }

        privacy_policy_layout.setOnClickListener {
            val i = Intent(requireContext(), WebViewPolicies::class.java)
            i.putExtra("PRIVACYPOLICY", "Privacy Policy")
//            i.putExtra("PRIVACY_POLICY", PRIVACY_POLICY)
            startActivity(i)
        }

        return root
    }
}