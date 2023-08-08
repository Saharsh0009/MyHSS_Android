package com.uk.myhss.ui.policies

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.Placeholder
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.Utils.DebugLog
import com.myhss.Utils.Functions
import com.myhss.ui.profile.DialogViewCertificate
import com.uk.myhss.AddMember.AddMemberFirstActivity
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import java.io.File


class OtherInfoFragment : Fragment() {
    private lateinit var sessionManager: SessionManager
//    private val sharedPrefFile = "MyHss"

    private var FILETYPE: String = ""

    lateinit var medical_info_yes_no_txt: TextView
    lateinit var qualification_first_yes_no_txt: TextView
    lateinit var dbs_certification_number_txt: TextView
    lateinit var dbs_certification_date_txt: TextView
    lateinit var safeguarding_training_txt: TextView
    lateinit var medical_information_details: TextView
    lateinit var qualification_first_date: TextView
    lateinit var qualification_first_file_view: TextView
    lateinit var qualification_first_file_download: TextView
    lateinit var txt_first_aid_qua_type: TextView
    lateinit var txt_pro_body_reg_no: TextView
    lateinit var txt_dietary_req: TextView
    lateinit var txt_originating: TextView
    lateinit var image_file_view: ImageView
    lateinit var mainedit_layout: LinearLayout
    lateinit var medical_information_yesview: LinearLayout
    lateinit var medical_info_yes_no_layout: LinearLayout
    lateinit var qualification_first_view: LinearLayout
    lateinit var qualification_first_yes_no_layout: LinearLayout
    lateinit var layout_first_aid_qualification_file: LinearLayout
    lateinit var layout_first_aid_pro_body: LinearLayout
    var isImageFitToScreen = false

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_other_info, container, false)
        sessionManager = SessionManager(requireContext())

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
        sessionManager.firebaseAnalytics.setUserId("ProfileVC_OtherInfoView")
        sessionManager.firebaseAnalytics.setUserProperty(
            "ProfileVC_OtherInfoView", "OtherInfoFragment"
        )

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        medical_info_yes_no_txt = root.findViewById(R.id.medical_info_yes_no_txt)
        qualification_first_yes_no_txt = root.findViewById(R.id.qualification_first_yes_no_txt)
        dbs_certification_number_txt = root.findViewById(R.id.dbs_certification_number_txt)
        dbs_certification_date_txt = root.findViewById(R.id.dbs_certification_date_txt)
        safeguarding_training_txt = root.findViewById(R.id.safeguarding_training_txt)
        medical_information_details = root.findViewById(R.id.medical_information_details)
        mainedit_layout = root.findViewById(R.id.mainedit_layout)
        medical_information_yesview = root.findViewById(R.id.medical_information_yesview)
        medical_info_yes_no_layout = root.findViewById(R.id.medical_info_yes_no_layout)
        qualification_first_view = root.findViewById(R.id.qualification_first_view)
        qualification_first_yes_no_layout =
            root.findViewById(R.id.qualification_first_yes_no_layout)
        qualification_first_date = root.findViewById(R.id.qualification_first_date)
        qualification_first_file_view = root.findViewById(R.id.qualification_first_file_view)
        qualification_first_file_download =
            root.findViewById(R.id.qualification_first_file_download)
        txt_first_aid_qua_type = root.findViewById(R.id.txt_first_aid_qua_type)
        image_file_view = root.findViewById(R.id.image_file_view)
        layout_first_aid_qualification_file =
            root.findViewById(R.id.layout_first_aid_qualification_file)
        layout_first_aid_pro_body = root.findViewById(R.id.layout_first_aid_pro_body)
        txt_pro_body_reg_no = root.findViewById(R.id.txt_pro_body_reg_no)
        txt_dietary_req = root.findViewById(R.id.txt_dietary_req)
        txt_originating = root.findViewById(R.id.txt_originating)

        if (sessionManager.fetchDOHAVEMEDICAL() == "1") {
            medical_info_yes_no_txt.text = getString(R.string.yes)
            medical_information_yesview.visibility = View.VISIBLE
            medical_information_details.text = sessionManager.fetchMEDICAL_OTHER_INFO()
        } else {
            medical_info_yes_no_txt.text = getString(R.string.no)
            medical_information_yesview.visibility = View.GONE
        }

        if (sessionManager.fetchQUALIFICATIONAID() == "1") {
            qualification_first_yes_no_txt.text = getString(R.string.yes)
            qualification_first_view.visibility = View.VISIBLE
            txt_first_aid_qua_type.text = sessionManager.fetchQUALIFICATION_VALUE_NAME()

            if (sessionManager.fetchQUALIFICATION_IS_DOC().toString() == "1") {
                layout_first_aid_qualification_file.visibility = View.VISIBLE
                qualification_first_date.text = sessionManager.fetchQUALIFICATION_DATE()
                layout_first_aid_pro_body.visibility = View.GONE

//                Glide.with(requireContext())
//                    .load(MyHssApplication.IMAGE_PDF_URL + sessionManager.fetchQUALIFICATION_FILE())
//                    .apply(Placeholder(R.drawable.ic_logout))
//                    .into(image_file_view)

                Glide.with(requireContext())
                    .load(MyHssApplication.IMAGE_PDF_URL + sessionManager.fetchQUALIFICATION_FILE())
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading_img)
                            .error(R.drawable.ic_error)
                    )
                    .into(image_file_view)

            } else {
                layout_first_aid_qualification_file.visibility = View.GONE
                layout_first_aid_pro_body.visibility = View.VISIBLE
                txt_pro_body_reg_no.text = sessionManager.fetchQUALIFICATION_PRO_BODY_RED_NO()
            }
        } else {
            qualification_first_yes_no_txt.text = getString(R.string.no)
            qualification_first_view.visibility = View.GONE
        }

        if (sessionManager.fetchQUALIFICATION_FILE() != "") {
            val f = File(sessionManager.fetchQUALIFICATION_FILE()!!)
            print(f.extension)
            DebugLog.e(" F==> " + f.toString())

            val fileName: String = f.toString()
            val extension = fileName.substring(fileName.lastIndexOf("."))
            if (extension.equals(".pdf", ignoreCase = true)) {
                image_file_view.visibility = View.GONE
                qualification_first_file_view.visibility = View.GONE
            } else {
                image_file_view.visibility = View.VISIBLE
                qualification_first_file_view.visibility = View.VISIBLE
            }
        }

        if (sessionManager.fetchDIETARY() != "") {
            txt_dietary_req.text = sessionManager.fetchDIETARY().toString()
        } else {
            txt_dietary_req.text = "-"
        }
        if (sessionManager.fetchSTATE_IN_INDIA() != "") {
            txt_originating.text = sessionManager.fetchSTATE_IN_INDIA().toString()
        } else {
            txt_originating.text = "-"
        }

        qualification_first_file_view.setOnClickListener {
            DialogViewCertificate().show(childFragmentManager, "DialogViewCertificate.TAG")
        }

        qualification_first_file_download.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(MyHssApplication.IMAGE_PDF_URL + sessionManager.fetchQUALIFICATION_FILE())
            )
            startActivity(browserIntent)
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