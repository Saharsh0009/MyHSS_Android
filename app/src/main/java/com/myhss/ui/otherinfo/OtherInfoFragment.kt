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
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.Utils.Functions
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
    lateinit var qualification_first_file: TextView
    lateinit var image_file_view: ImageView
    lateinit var mainedit_layout: LinearLayout
    lateinit var medical_information_yesview: LinearLayout
    lateinit var medical_info_yes_no_layout: LinearLayout
    lateinit var qualification_first_view: LinearLayout
    lateinit var qualification_first_yes_no_layout: LinearLayout
    var isImageFitToScreen = false

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_other_info, container, false)
        sessionManager = SessionManager(requireContext())

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
        sessionManager.firebaseAnalytics.setUserId("ProfileVC_OtherInfoView")
        sessionManager.firebaseAnalytics.setUserProperty("ProfileVC_OtherInfoView", "OtherInfoFragment")

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
        qualification_first_yes_no_layout = root.findViewById(R.id.qualification_first_yes_no_layout)
        qualification_first_date = root.findViewById(R.id.qualification_first_date)
        qualification_first_file = root.findViewById(R.id.qualification_first_file)
        image_file_view = root.findViewById(R.id.image_file_view)

        if (sessionManager.fetchDOHAVEMEDICAL() == "1") {
            medical_info_yes_no_txt.text = getString(R.string.yes)
            medical_information_yesview.visibility = View.VISIBLE
            medical_info_yes_no_layout.visibility = View.GONE
            medical_information_details.text = sessionManager.fetchMEDICAL_OTHER_INFO()
        } else {
            medical_info_yes_no_txt.text = getString(R.string.no)
            medical_info_yes_no_layout.visibility = View.VISIBLE
            medical_information_yesview.visibility = View.GONE
        }

        if (sessionManager.fetchQUALIFICATIONAID() == "1") {
            qualification_first_yes_no_txt.text = getString(R.string.yes)
            qualification_first_view.visibility = View.VISIBLE
            qualification_first_yes_no_layout.visibility = View.GONE
            qualification_first_date.text = sessionManager.fetchQUALIFICATION_DATE()
//            qualification_first_file.text = sessionManager.fetchQUALIFICATION_FILE()
        } else {
            qualification_first_yes_no_txt.text = getString(R.string.no)
            qualification_first_view.visibility = View.GONE
            qualification_first_yes_no_layout.visibility = View.VISIBLE
        }

        if (sessionManager.fetchQUALIFICATION_FILE() != "") {
            val f = File(sessionManager.fetchQUALIFICATION_FILE()!!)
            print(f.extension)
            Functions.printLog("==>", f.toString())

            val fileName: String = f.toString()
            val extension = fileName.substring(fileName.lastIndexOf("."))
            if (extension.equals(".pdf", ignoreCase = true)) {
                Functions.printLog("FILETYPE", "PDF")
                image_file_view.visibility = View.GONE
                qualification_first_file.visibility = View.VISIBLE
            } else {
                Functions.printLog("FILETYPE", "IMAGE")
                image_file_view.visibility = View.VISIBLE
                qualification_first_file.visibility = View.GONE

                Glide.with(requireContext())
                    .load(MyHssApplication.IMAGE_PDF_URL + sessionManager.fetchQUALIFICATION_FILE()).into(
                        image_file_view
                    )
            }
        }

        image_file_view.setOnClickListener(View.OnClickListener {
            if (isImageFitToScreen) {
                isImageFitToScreen = false
                image_file_view.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
//                image_file_view.setScaleType(ImageView.ScaleType.FIT_CENTER)
                image_file_view.adjustViewBounds = true
            } else {
                isImageFitToScreen = true
                image_file_view.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                image_file_view.scaleType = ImageView.ScaleType.FIT_CENTER
            }
        })

        qualification_first_file.setOnClickListener {

            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(MyHssApplication.IMAGE_PDF_URL + sessionManager.fetchQUALIFICATION_FILE()))
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