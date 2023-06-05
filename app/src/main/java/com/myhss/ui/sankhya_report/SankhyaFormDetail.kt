package com.uk.myhss.ui.sankhya_report

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.Functions
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.ui.guru_dakshina.Model.Get_Sankhya_Add_Response
import com.uk.myhss.ui.policies.SankhyaActivity
import com.uk.myhss.ui.sankhya_report.Model.Sankhya_details_Datum
import com.uk.myhss.ui.sankhya_report.Model.Sankhya_details_Response
import com.uk.myhss.ui.sankhya_report.Model.Sankhya_details_member
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class SankhyaFormDetail : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    var dialog: Dialog? = null
    private lateinit var active_inactive_view: RelativeLayout
    private lateinit var active_inactive_txt: TextView
    private lateinit var total_member_count: TextView
    private lateinit var user_name_txt: TextView
    private lateinit var shakha_name_txt: TextView
    private lateinit var shishu_type_txt: TextView
    private lateinit var baal_type_txt: TextView
    private lateinit var kishore_type_txt: TextView
    private lateinit var tarun_type_txt: TextView
    private lateinit var yuva_type_txt: TextView
    private lateinit var jyeshta_type_txt: TextView

    private lateinit var female_shishu_type_txt: TextView
    private lateinit var balika_type_txt: TextView
    private lateinit var kishori_type_txt: TextView
    private lateinit var taruni_type_txt: TextView
    private lateinit var yuvati_type_txt: TextView
    private lateinit var jyeshtaa_type_txt: TextView

    private lateinit var shishu_decriese_btn: ImageView
    private lateinit var shishu_increse_btn: ImageView
    private lateinit var baal_decriese_btn: ImageView
    private lateinit var baal_increse_btn: ImageView
    private lateinit var kishore_decriese_btn: ImageView
    private lateinit var kishore_increse_btn: ImageView
    private lateinit var tarun_decriese_btn: ImageView
    private lateinit var tarun_increse_btn: ImageView
    private lateinit var yuva_decriese_btn: ImageView
    private lateinit var yuva_increse_btn: ImageView
    private lateinit var jyeshta_decriese_btn: ImageView
    private lateinit var jyeshta_increse_btn: ImageView

    private lateinit var female_shishu_decriese_btn: ImageView
    private lateinit var female_shishu_increse_btn: ImageView
    private lateinit var balika_decriese_btn: ImageView
    private lateinit var balika_increse_btn: ImageView
    private lateinit var kishori_decriese_btn: ImageView
    private lateinit var kishori_increse_btn: ImageView
    private lateinit var taruni_decriese_btn: ImageView
    private lateinit var taruni_increse_btn: ImageView
    private lateinit var yuvati_decriese_btn: ImageView
    private lateinit var yuvati_increse_btn: ImageView
    private lateinit var jyeshtaa_decriese_btn: ImageView
    private lateinit var jyeshtaa_increse_btn: ImageView

    private lateinit var txt_guest_count: TextView
    private lateinit var txt_sankhya_count: TextView
    private lateinit var txt_total_count: TextView

    private lateinit var rootLayout: LinearLayout
    lateinit var reportview_layout: LinearLayout
    lateinit var submit_layout: LinearLayout

    lateinit var members_listview: TextView
    lateinit var sankhya_list_view: ListView

    lateinit var USERID: String
    lateinit var SANKHYA_ID: String

    private var IsVisible = true

    var MemberListName: ArrayList<String> = ArrayList<String>()
    var MemberListId: ArrayList<String> = ArrayList<String>()

    private var USER_ID: String = ""
    private var MEMBER_ID: String = ""
    private var ORG_CHAPTER_ID: String = ""
    private var EVENT_DATE: String = ""
    private var EVENTDATE: String = ""
    private var UTSAV_NAME: String = ""
    private var SHISHU_MALE: String = "0"
    private var SHISHU_FEMALE: String = "0"
    private var BAAL: String = "0"
    private var BAALIKA: String = "0"
    private var KISHOR: String = "0"
    private var KISHORI: String = "0"
    private var TARUN: String = "0"
    private var TARUNI: String = "0"
    private var YUVA: String = "0"
    private var YUVTI: String = "0"
    private var PRODH: String = "0"
    private var PRODHA: String = "0"
    private var API: String = ""


    @SuppressLint("NewApi", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sankhya_formdetails)

        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("SankhyaDetailVC")
        sessionManager.firebaseAnalytics.setUserProperty("SankhyaDetailVC", "SankhyaFormDetail")

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        val back_arrow = findViewById<ImageView>(R.id.back_arrow)
        val header_title = findViewById<TextView>(R.id.header_title)

        header_title.text = "Guest " + getString(R.string.sankhya)

        active_inactive_view = findViewById(R.id.active_inactive_view)
        active_inactive_txt = findViewById(R.id.active_inactive_txt)
        total_member_count = findViewById(R.id.total_member_count)
        user_name_txt = findViewById(R.id.user_name_txt)
        shakha_name_txt = findViewById(R.id.shakha_name_txt)

        shishu_type_txt = findViewById(R.id.shishu_type_txt)
        baal_type_txt = findViewById(R.id.baal_type_txt)
        kishore_type_txt = findViewById(R.id.kishore_type_txt)
        tarun_type_txt = findViewById(R.id.tarun_type_txt)
        yuva_type_txt = findViewById(R.id.yuva_type_txt)
        jyeshta_type_txt = findViewById(R.id.jyeshta_type_txt)

        female_shishu_type_txt = findViewById(R.id.female_shishu_type_txt)
        balika_type_txt = findViewById(R.id.balika_type_txt)
        kishori_type_txt = findViewById(R.id.kishori_type_txt)
        taruni_type_txt = findViewById(R.id.taruni_type_txt)
        yuvati_type_txt = findViewById(R.id.yuvati_type_txt)
        jyeshtaa_type_txt = findViewById(R.id.jyeshtaa_type_txt)

        shishu_decriese_btn = findViewById(R.id.shishu_decriese_btn)
        shishu_increse_btn = findViewById(R.id.shishu_increse_btn)
        baal_decriese_btn = findViewById(R.id.baal_decriese_btn)
        baal_increse_btn = findViewById(R.id.baal_increse_btn)
        kishore_decriese_btn = findViewById(R.id.kishore_decriese_btn)
        kishore_increse_btn = findViewById(R.id.kishore_increse_btn)
        tarun_decriese_btn = findViewById(R.id.tarun_decriese_btn)
        tarun_increse_btn = findViewById(R.id.tarun_increse_btn)
        yuva_decriese_btn = findViewById(R.id.yuva_decriese_btn)
        yuva_increse_btn = findViewById(R.id.yuva_increse_btn)
        jyeshta_decriese_btn = findViewById(R.id.jyeshta_decriese_btn)
        jyeshta_increse_btn = findViewById(R.id.jyeshta_increse_btn)

        female_shishu_decriese_btn = findViewById(R.id.female_shishu_decriese_btn)
        female_shishu_increse_btn = findViewById(R.id.female_shishu_increse_btn)
        balika_decriese_btn = findViewById(R.id.balika_decriese_btn)
        balika_increse_btn = findViewById(R.id.balika_increse_btn)
        kishori_decriese_btn = findViewById(R.id.kishori_decriese_btn)
        kishori_increse_btn = findViewById(R.id.kishori_increse_btn)
        taruni_decriese_btn = findViewById(R.id.taruni_decriese_btn)
        taruni_increse_btn = findViewById(R.id.taruni_increse_btn)
        yuvati_decriese_btn = findViewById(R.id.yuvati_decriese_btn)
        yuvati_increse_btn = findViewById(R.id.yuvati_increse_btn)
        jyeshtaa_decriese_btn = findViewById(R.id.jyeshtaa_decriese_btn)
        jyeshtaa_increse_btn = findViewById(R.id.jyeshtaa_increse_btn)

        rootLayout = findViewById(R.id.rootLayout)
        reportview_layout = findViewById(R.id.reportview_layout)
        submit_layout = findViewById(R.id.submit_layout)
        members_listview = findViewById(R.id.members_listview)
        sankhya_list_view = findViewById(R.id.sankhya_list_view)

        txt_guest_count = findViewById(R.id.txt_guest_count)
        txt_sankhya_count = findViewById(R.id.txt_sankhya_count)
        txt_total_count = findViewById(R.id.txt_total_count)

        if (intent.getStringExtra("SANKHYA_ID") != "") {
            if (Functions.isConnectingToInternet(this@SankhyaFormDetail)) {
                USERID = sessionManager.fetchUserID()!!
                SANKHYA_ID = intent.getStringExtra("SANKHYA_ID")!!
                Log.d("USERID", USERID)
                Log.d("SANKHYA_ID", SANKHYA_ID)
                mySankhya_Record(USERID, SANKHYA_ID)
            } else {
                Toast.makeText(
                    this@SankhyaFormDetail,
                    resources.getString(R.string.no_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        back_arrow.setOnClickListener {
            finish()
        }

        user_name_txt.text = intent.getStringExtra("UTSAVE_ID")!!
            .capitalize(Locale.ROOT) + " " + intent.getStringExtra("CURRENT_DATE")

        shakha_name_txt.text = intent.getStringExtra("SHAKHA_NAME")!!.capitalize(Locale.ROOT)

        Log.d("CURRENT_DATE", intent.getStringExtra("CURRENT_DATE").toString())
        Log.d("MEMBER_NAME", intent.getStringArrayListExtra("MEMBER_NAME").toString())
//        Log.d("USER_MEMBER", intent.getStringExtra("USER_MEMBER").toString())
        Log.d(
            "USERNAME_LIST",
            intent.getSerializableExtra("USERNAME_LIST").toString().replace("[", "")
                .replace("]", "")
        )
//        Log.d("USERID_LIST", intent.getSerializableExtra("USERID_LIST").toString())

//        var demooo: ArrayList<String> = ArrayList<String>()
//        demooo.add(intent.getSerializableExtra("USERNAME_LIST").toString()).re

        MemberListName = intent.getStringArrayListExtra("MEMBER_NAME")!! //as Array<String>
//        MemberListId.add(intent.getSerializableExtra("USERID_LIST")!!.toString()) //as Array<String>

        total_member_count.visibility = View.GONE
        total_member_count.text = intent.getStringExtra("MEMBER_ID").toString()
        Functions.printLog(
            "MEMBER_ID==>",
            intent.getStringExtra("MEMBER_ID")!!  //.replace("[", "").replace("]", "")
        )

//        user_name_txt.text = sessionManager.fetchUSERNAME()!!.capitalize(Locale.ROOT)
//
//        shakha_name_txt.text = sessionManager.fetchSHAKHANAME()!!.capitalize(Locale.ROOT)

        val adapter = ArrayAdapter(
            this@SankhyaFormDetail, android.R.layout.simple_list_item_1, MemberListName
        )
        sankhya_list_view.adapter = adapter

        setSankhyaCountView()

//        members_listview.setOnClickListener {
//            if (IsVisible) {
//                sankhya_list_view.visibility = View.VISIBLE
//                IsVisible = false
//            } else if (!IsVisible) {
//                sankhya_list_view.visibility = View.GONE
//                IsVisible = true
//            }
//        }

        justifyListViewHeightBasedOnChildren(sankhya_list_view)

        reportview_layout.setOnClickListener {
            ReviewDetailsDialog()
        }

        submit_layout.setOnClickListener {
//            val i = Intent(this@SankhyaFormDetail, SankhyaDetail::class.java)
//            i.putExtra("SANKHYA", "SANKHYA")
//            i.putExtra("SANKHYA_ID", "")
//            startActivity(i)

            USER_ID = sessionManager.fetchUserID()!!
            MEMBER_ID = intent.getStringExtra("MEMBER_ID")!!  //.replace("[", "").replace("]", "")!!
            ORG_CHAPTER_ID = sessionManager.fetchSHAKHAID()!!
            UTSAV_NAME = intent.getStringExtra("UTSAV_NAME")!!
            EVENTDATE = intent.getStringExtra("EVENT_DATE")!!
//            SHISHU_MALE = ""
//            SHISHU_FEMALE = ""
//            BAAL = ""
//            BAALIKA = ""
//            KISHOR = ""
//            KISHORI = ""
//            TARUN = ""
//            TARUNI = ""
//            YUVA = ""
//            YUVTI = ""
//            PRODH = ""
//            PRODHA = ""
            API = "yes"

            if (Functions.isConnectingToInternet(this@SankhyaFormDetail)) {
                AddSankhya(
                    USER_ID,
                    MEMBER_ID,
                    ORG_CHAPTER_ID,
                    UTSAV_NAME,
                    EVENTDATE,
                    SHISHU_MALE,
                    SHISHU_FEMALE,
                    BAAL,
                    BAALIKA,
                    KISHOR,
                    KISHORI,
                    TARUN,
                    TARUNI,
                    YUVA,
                    YUVTI,
                    PRODH,
                    PRODHA,
                    API
                )
            } else {
                Toast.makeText(
                    this@SankhyaFormDetail,
                    resources.getString(R.string.no_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        shishu_decriese_btn.setOnClickListener {
//            decreaseInteger(shishu_type_txt.text.toString())
            if (shishu_type_txt.text.toString().toInt() != 0) {
                shishu_type_txt.text = (shishu_type_txt.text.toString().toInt() - 1).toString()
                SHISHU_MALE = shishu_type_txt.text.toString()
                setSankhyaCountView()
            }
        }

        shishu_increse_btn.setOnClickListener {
//            increaseInteger(shishu_type_txt.text.toString())
            if (shishu_type_txt.text.toString().toInt() <= 99) {
                shishu_type_txt.text = (shishu_type_txt.text.toString().toInt() + 1).toString()
                SHISHU_MALE = shishu_type_txt.text.toString()
                setSankhyaCountView()
            }
        }

        baal_decriese_btn.setOnClickListener {
//            decreaseInteger(baal_type_txt.text.toString())
            if (baal_type_txt.text.toString().toInt() != 0) {
                baal_type_txt.text = (baal_type_txt.text.toString().toInt() - 1).toString()
                BAAL = baal_type_txt.text.toString()
                setSankhyaCountView()
            }
        }

        baal_increse_btn.setOnClickListener {
//            increaseInteger(baal_type_txt.text.toString())
            if (baal_type_txt.text.toString().toInt() <= 99) {
                baal_type_txt.text = (baal_type_txt.text.toString().toInt() + 1).toString()
                BAAL = baal_type_txt.text.toString()
                setSankhyaCountView()
            }
        }

        kishore_decriese_btn.setOnClickListener {
//            decreaseInteger(kishore_type_txt.text.toString())
            if (kishore_type_txt.text.toString().toInt() != 0) {
                kishore_type_txt.text = (kishore_type_txt.text.toString().toInt() - 1).toString()
                KISHOR = kishore_type_txt.text.toString()
                setSankhyaCountView()
            }
        }

        kishore_increse_btn.setOnClickListener {
//            increaseInteger(kishore_type_txt.text.toString())
            if (kishore_type_txt.text.toString().toInt() <= 99) {
                kishore_type_txt.text = (kishore_type_txt.text.toString().toInt() + 1).toString()
                KISHOR = kishore_type_txt.text.toString()
                setSankhyaCountView()
            }
        }

        tarun_decriese_btn.setOnClickListener {
//            decreaseInteger(tarun_type_txt.text.toString())
            if (tarun_type_txt.text.toString().toInt() != 0) {
                tarun_type_txt.text = (tarun_type_txt.text.toString().toInt() - 1).toString()
                TARUN = tarun_type_txt.text.toString()
                setSankhyaCountView()
            }
        }

        tarun_increse_btn.setOnClickListener {
//            increaseInteger(tarun_type_txt.text.toString())
            if (tarun_type_txt.text.toString().toInt() <= 99) {
                tarun_type_txt.text = (tarun_type_txt.text.toString().toInt() + 1).toString()
                TARUN = tarun_type_txt.text.toString()
                setSankhyaCountView()
            }
        }

        yuva_decriese_btn.setOnClickListener {
//            decreaseInteger(yuva_type_txt.text.toString())
            if (yuva_type_txt.text.toString().toInt() != 0) {
                yuva_type_txt.text = (yuva_type_txt.text.toString().toInt() - 1).toString()
                YUVA = yuva_type_txt.text.toString()
                setSankhyaCountView()
            }
        }

        yuva_increse_btn.setOnClickListener {
//            increaseInteger(yuvati_type_txt.text.toString())
            if (yuva_type_txt.text.toString().toInt() <= 99) {
                yuva_type_txt.text = (yuva_type_txt.text.toString().toInt() + 1).toString()
                YUVA = yuva_type_txt.text.toString()
                setSankhyaCountView()
            }
        }

        jyeshta_decriese_btn.setOnClickListener {
//            decreaseInteger(jyeshta_type_txt.text.toString())
            if (jyeshta_type_txt.text.toString().toInt() != 0) {
                jyeshta_type_txt.text = (jyeshta_type_txt.text.toString().toInt() - 1).toString()
                PRODH = jyeshta_type_txt.text.toString()
                setSankhyaCountView()
            }
        }

        jyeshta_increse_btn.setOnClickListener {
//            increaseInteger(jyeshta_type_txt.text.toString())
            if (jyeshta_type_txt.text.toString().toInt() <= 99) {
                jyeshta_type_txt.text = (jyeshta_type_txt.text.toString().toInt() + 1).toString()
                PRODH = jyeshta_type_txt.text.toString()
                setSankhyaCountView()
            }
        }

        female_shishu_decriese_btn.setOnClickListener {
//            decreaseInteger(female_shishu_type_txt.text.toString())
            if (female_shishu_type_txt.text.toString().toInt() != 0) {
                female_shishu_type_txt.text =
                    (female_shishu_type_txt.text.toString().toInt() - 1).toString()
                SHISHU_FEMALE = female_shishu_type_txt.text.toString()
                setSankhyaCountView()
            }
        }

        female_shishu_increse_btn.setOnClickListener {
//            increaseInteger(female_shishu_type_txt.text.toString())
            if (female_shishu_type_txt.text.toString().toInt() <= 99) {
                female_shishu_type_txt.text =
                    (female_shishu_type_txt.text.toString().toInt() + 1).toString()
                SHISHU_FEMALE = female_shishu_type_txt.text.toString()
                setSankhyaCountView()
            }
        }

        balika_decriese_btn.setOnClickListener {
//            decreaseInteger(balika_type_txt.text.toString())
            if (balika_type_txt.text.toString().toInt() != 0) {
                balika_type_txt.text = (balika_type_txt.text.toString().toInt() - 1).toString()
                BAALIKA = balika_type_txt.text.toString()
                setSankhyaCountView()
            }
        }

        balika_increse_btn.setOnClickListener {
//            increaseInteger(balika_type_txt.text.toString())
            if (balika_type_txt.text.toString().toInt() <= 99) {
                balika_type_txt.text = (balika_type_txt.text.toString().toInt() + 1).toString()
                BAALIKA = balika_type_txt.text.toString()
                setSankhyaCountView()
            }
        }

        kishori_decriese_btn.setOnClickListener {
//            decreaseInteger(kishori_type_txt.text.toString())
            if (kishori_type_txt.text.toString().toInt() != 0) {
                kishori_type_txt.text = (kishori_type_txt.text.toString().toInt() - 1).toString()
                KISHORI = kishori_type_txt.text.toString()
                setSankhyaCountView()
            }
        }

        kishori_increse_btn.setOnClickListener {
//            increaseInteger(kishori_type_txt.text.toString())
            if (kishori_type_txt.text.toString().toInt() <= 99) {
                kishori_type_txt.text = (kishori_type_txt.text.toString().toInt() + 1).toString()
                KISHORI = kishori_type_txt.text.toString()
                setSankhyaCountView()
            }
        }

        taruni_decriese_btn.setOnClickListener {
//            decreaseInteger(taruni_type_txt.text.toString())
            if (taruni_type_txt.text.toString().toInt() != 0) {
                taruni_type_txt.text = (taruni_type_txt.text.toString().toInt() - 1).toString()
                TARUNI = taruni_type_txt.text.toString()
                setSankhyaCountView()
            }
        }

        taruni_increse_btn.setOnClickListener {
//            increaseInteger(taruni_type_txt.text.toString())
            if (taruni_type_txt.text.toString().toInt() <= 99) {
                taruni_type_txt.text = (taruni_type_txt.text.toString().toInt() + 1).toString()
                TARUNI = taruni_type_txt.text.toString()
                setSankhyaCountView()
            }
        }

        yuvati_decriese_btn.setOnClickListener {
//            decreaseInteger(yuvati_type_txt.text.toString())
            if (yuvati_type_txt.text.toString().toInt() != 0) {
                yuvati_type_txt.text = (yuvati_type_txt.text.toString().toInt() - 1).toString()
                YUVTI = yuvati_type_txt.text.toString()
                setSankhyaCountView()
            }
        }

        yuvati_increse_btn.setOnClickListener {
//            increaseInteger(yuvati_type_txt.text.toString())
            if (yuvati_type_txt.text.toString().toInt() <= 99) {
                yuvati_type_txt.text = (yuvati_type_txt.text.toString().toInt() + 1).toString()
                YUVTI = yuvati_type_txt.text.toString()
                setSankhyaCountView()
            }
        }

        jyeshtaa_decriese_btn.setOnClickListener {
//            decreaseInteger(jyeshtaa_type_txt.text.toString())
            if (jyeshtaa_type_txt.text.toString().toInt() != 0) {
                jyeshtaa_type_txt.text = (jyeshtaa_type_txt.text.toString().toInt() - 1).toString()
                PRODHA = jyeshtaa_type_txt.text.toString()
                setSankhyaCountView()
            }
        }

        jyeshtaa_increse_btn.setOnClickListener {
//            increaseInteger(jyeshtaa_type_txt.text.toString())
            if (jyeshtaa_type_txt.text.toString().toInt() <= 99) {
                jyeshtaa_type_txt.text = (jyeshtaa_type_txt.text.toString().toInt() + 1).toString()
                PRODHA = jyeshtaa_type_txt.text.toString()
                setSankhyaCountView()
            }
        }
    }

    private fun setSankhyaCountView() {
        val ct_guest: Int =
            (Integer.parseInt(SHISHU_MALE) + Integer.parseInt(SHISHU_FEMALE) + Integer.parseInt(BAAL)
                    + Integer.parseInt(BAALIKA) + Integer.parseInt(KISHOR) + Integer.parseInt(
                KISHORI
            ) + Integer.parseInt(TARUN)
                    + Integer.parseInt(TARUNI) + Integer.parseInt(YUVA) + Integer.parseInt(YUVTI) + Integer.parseInt(
                PRODH
            ) + Integer.parseInt(PRODHA))
        txt_guest_count.text = " : $ct_guest"
        txt_sankhya_count.text = " : " + MemberListName.size.toString()
        txt_total_count.text = " : " + (ct_guest + MemberListName.size).toString()
    }

    fun increaseInteger(increasetoString: String) {
        shishu_type_txt.text = (increasetoString.toInt() + 1).toString()
        baal_type_txt.text = (increasetoString.toInt() + 1).toString()
        kishore_type_txt.text = (increasetoString.toInt() + 1).toString()
        tarun_type_txt.text = (increasetoString.toInt() + 1).toString()
        yuva_type_txt.text = (increasetoString.toInt() + 1).toString()
        jyeshta_type_txt.text = (increasetoString.toInt() + 1).toString()
        female_shishu_type_txt.text = (increasetoString.toInt() + 1).toString()
        balika_type_txt.text = (increasetoString.toInt() + 1).toString()
        kishori_type_txt.text = (increasetoString.toInt() + 1).toString()
        taruni_type_txt.text = (increasetoString.toInt() + 1).toString()
        yuvati_type_txt.text = (increasetoString.toInt() + 1).toString()
        jyeshtaa_type_txt.text = (increasetoString.toInt() + 1).toString()
//        display(increasetoString.toInt() + 1)
    }

    fun decreaseInteger(decreasetoString: String) {
        shishu_type_txt.text = (decreasetoString.toInt() - 1).toString()
        baal_type_txt.text = (decreasetoString.toInt() - 1).toString()
        kishore_type_txt.text = (decreasetoString.toInt() - 1).toString()
        tarun_type_txt.text = (decreasetoString.toInt() - 1).toString()
        yuva_type_txt.text = (decreasetoString.toInt() - 1).toString()
        jyeshta_type_txt.text = (decreasetoString.toInt() - 1).toString()
        female_shishu_type_txt.text = (decreasetoString.toInt() - 1).toString()
        balika_type_txt.text = (decreasetoString.toInt() - 1).toString()
        kishori_type_txt.text = (decreasetoString.toInt() - 1).toString()
        taruni_type_txt.text = (decreasetoString.toInt() - 1).toString()
        yuvati_type_txt.text = (decreasetoString.toInt() - 1).toString()
        jyeshtaa_type_txt.text = (decreasetoString.toInt() - 1).toString()
//        display(decreasetoString.toInt() - 1)
    }

    /*private fun display(number: Int) {
//        integer_number.setText("$number")
        shishu_type_txt.text = number.toString()
    }*/


    /*Add Sankhya API*/
    private fun AddSankhya(
        user_id: String,
        member_id: String,
        org_chapter_id: String,
        utsav: String,
        event_date: String,
        shishu_male: String,
        shishu_female: String,
        baal: String,
        baalika: String,
        kishore: String,
        kishori: String,
        tarun: String,
        taruni: String,
        yuva: String,
        yuvati: String,
        proudh: String,
        proudha: String,
        api: String
    ) {
        val pd = CustomProgressBar(this@SankhyaFormDetail)
        pd.show()
        val call: Call<Get_Sankhya_Add_Response> = MyHssApplication.instance!!.api.get_sankhya_add(
            user_id,
            member_id,
            org_chapter_id,
            event_date,
            utsav,
            shishu_male,
            shishu_female,
            baal,
            baalika,
            kishore,
            kishori,
            tarun,
            taruni,
            yuva,
            yuvati,
            proudh,
            proudha,
            api
        )
        call.enqueue(object : Callback<Get_Sankhya_Add_Response> {
            override fun onResponse(
                call: Call<Get_Sankhya_Add_Response>, response: Response<Get_Sankhya_Add_Response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {

                        val alertBuilder =
                            AlertDialog.Builder(this@SankhyaFormDetail) // , R.style.dialog_custom

//                    alertBuilder.setTitle("Message")
                        alertBuilder.setMessage(response.body()?.message)
                        alertBuilder.setPositiveButton(
                            "OK"
                        ) { dialog, which ->
                            startActivity(
                                Intent(
                                    this@SankhyaFormDetail, SankhyaActivity::class.java
                                )
                            )
                            finish()
                        }
                        val alertDialog = alertBuilder.create()
                        alertDialog.show()

                    } else {
                        Functions.showAlertMessageWithOK(
                            this@SankhyaFormDetail, "",
//                        "Message",
                            response.body()?.message
                        )
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@SankhyaFormDetail, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Get_Sankhya_Add_Response>, t: Throwable) {
                Toast.makeText(this@SankhyaFormDetail, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    /*Sankhya_Record API*/
    private fun mySankhya_Record(user_id: String, sankhya_id: String) {
        val pd = CustomProgressBar(this@SankhyaFormDetail)
        pd.show()
        val call: Call<Sankhya_details_Response> =
            MyHssApplication.instance!!.api.get_sankhya_get_record(user_id, sankhya_id)
        call.enqueue(object : Callback<Sankhya_details_Response> {
            override fun onResponse(
                call: Call<Sankhya_details_Response>, response: Response<Sankhya_details_Response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {
                        var sankhya_datum: List<Sankhya_details_Datum> =
                            ArrayList<Sankhya_details_Datum>()
                        var sankhya_detail: List<Sankhya_details_member> =
                            ArrayList<Sankhya_details_member>()

                        Log.d("sankhya_detail", sankhya_detail.size.toString())

                        total_member_count.text = sankhya_detail.size.toString()

                        try {
                            sankhya_datum = response.body()!!.data!!
                            Log.d("sankhya_datum", sankhya_datum.toString())

                            if (sankhya_detail[0].middleName == "") {
                                user_name_txt.text =
                                    sankhya_detail[0].firstName!!.capitalize(Locale.ROOT) + " " + sankhya_detail[0].lastName!!.capitalize(
                                        Locale.ROOT
                                    )
                            } else {
                                user_name_txt.text =
                                    sankhya_detail[0].firstName!!.capitalize(Locale.ROOT) + " " + sankhya_detail[0].middleName!!.capitalize(
                                        Locale.ROOT
                                    ) + " " + sankhya_detail[0].lastName!!.capitalize(Locale.ROOT)
                            }
                            shakha_name_txt.text =
                                sessionManager.fetchSHAKHANAME()!!.capitalize(Locale.ROOT)

                            /*if (sankhya_detail[0].firstName == getString(R.string.tarun) && sankhya_detail[0].firstName == getString(R.string.taruni)) {
                                active_inactive_view.setBackgroundResource(R.drawable.baal_background)
                            }*/

                            if (sankhya_datum[0].baal == getString(R.string.baal)) {
                                active_inactive_view.setBackgroundResource(R.drawable.baal_background)
                            } else if (sankhya_datum[0].baalika == getString(R.string.baalika)) {
                                active_inactive_view.setBackgroundResource(R.drawable.baalika_background)
                            } else if (sankhya_datum[0].shishuMale == getString(R.string.male_shishu)) {
                                active_inactive_view.setBackgroundResource(R.drawable.male_shishu_background)
                            } else if (sankhya_datum[0].shishuFemale == getString(R.string.female_shishu)) {
                                active_inactive_view.setBackgroundResource(R.drawable.female_shishu_background)
                            } else if (sankhya_datum[0].kishore == getString(R.string.kishore)) {
                                active_inactive_view.setBackgroundResource(R.drawable.kishor_background)
                            } else if (sankhya_datum[0].kishori == getString(R.string.kishori)) {
                                active_inactive_view.setBackgroundResource(R.drawable.kishori_background)
                            } else if (sankhya_datum[0].tarun == getString(R.string.tarun)) {
                                active_inactive_view.setBackgroundResource(R.drawable.tarun_background)
                            } else if (sankhya_datum[0].taruni == getString(R.string.taruni)) {
                                active_inactive_view.setBackgroundResource(R.drawable.taruni_background)
                            } else if (sankhya_datum[0].yuva == getString(R.string.yuva)) {
                                active_inactive_view.setBackgroundResource(R.drawable.yuva_background)
                            } else if (sankhya_datum[0].yuvati == getString(R.string.yuvati)) {
                                active_inactive_view.setBackgroundResource(R.drawable.yuvati_background)
                            } else if (sankhya_datum[0].proudh == getString(R.string.proudh)) {
                                active_inactive_view.setBackgroundResource(R.drawable.proudh_background)
                            } else if (sankhya_datum[0].proudha == getString(R.string.proudha)) {
                                active_inactive_view.setBackgroundResource(R.drawable.proudha_background)
                            }

                            active_inactive_txt.text = sankhya_detail[0].ageCategory

                            shishu_type_txt.text = sankhya_datum[0].shishuMale
                            baal_type_txt.text = sankhya_datum[0].baal
                            kishore_type_txt.text = sankhya_datum[0].kishore
                            tarun_type_txt.text = sankhya_datum[0].tarun
                            yuva_type_txt.text = sankhya_datum[0].yuva
                            jyeshta_type_txt.text = sankhya_datum[0].proudh

                            female_shishu_type_txt.text = sankhya_datum[0].shishuFemale
                            balika_type_txt.text = sankhya_datum[0].baalika
                            kishori_type_txt.text = sankhya_datum[0].kishori
                            taruni_type_txt.text = sankhya_datum[0].taruni
                            yuvati_type_txt.text = sankhya_datum[0].yuvati
                            jyeshtaa_type_txt.text = sankhya_datum[0].proudha

                        } catch (e: ArithmeticException) {
                            println(e)
                        } finally {
                            println("Family")
                        }
                    } else {
                        Functions.showAlertMessageWithOK(
                            this@SankhyaFormDetail, "",
//                        "Message",
                            response.body()?.message
                        )
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@SankhyaFormDetail, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Sankhya_details_Response>, t: Throwable) {
                Toast.makeText(this@SankhyaFormDetail, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    fun ReviewDetailsDialog() {
        // Deposit Dialog
        if (dialog == null) {
            dialog = Dialog(this, R.style.StyleCommonDialog)
        }
        dialog?.setContentView(R.layout.sankhya_review_layout)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.show()

        var MemberList_Name: ArrayList<String> = ArrayList<String>()

        val active_inactive_view =
            dialog!!.findViewById(R.id.active_inactive_view) as RelativeLayout
        val sankhya_list_view = dialog!!.findViewById(R.id.sankhya_list_view) as ListView
        val members_listview = dialog!!.findViewById(R.id.members_listview) as TextView
        val dialog_active_inactive_txt = dialog!!.findViewById(R.id.active_inactive_txt) as TextView
        val dialog_total_member_count = dialog!!.findViewById(R.id.total_member_count) as TextView
        val dialog_user_name_txt = dialog!!.findViewById(R.id.user_name_txt) as TextView
        val dialog_shakha_name_txt = dialog!!.findViewById(R.id.shakha_name_txt) as TextView
        val dialog_shishu_type_txt = dialog!!.findViewById(R.id.shishu_type_txt) as TextView
        val dialog_baal_type_txt = dialog!!.findViewById(R.id.baal_type_txt) as TextView
        val dialog_kishore_type_txt = dialog!!.findViewById(R.id.kishore_type_txt) as TextView
        val dialog_tarun_type_txt = dialog!!.findViewById(R.id.tarun_type_txt) as TextView
        val dialog_yuva_type_txt = dialog!!.findViewById(R.id.yuva_type_txt) as TextView
        val dialog_jyeshta_type_txt = dialog!!.findViewById(R.id.jyeshta_type_txt) as TextView

        val dialog_female_shishu_type_txt =
            dialog!!.findViewById(R.id.female_shishu_type_txt) as TextView
        val dialog_balika_type_txt = dialog!!.findViewById(R.id.baalika_type_txt) as TextView
        val dialog_kishori_type_txt = dialog!!.findViewById(R.id.kishori_type_txt) as TextView
        val dialog_taruni_type_txt = dialog!!.findViewById(R.id.taruni_type_txt) as TextView
        val dialog_yuvati_type_txt = dialog!!.findViewById(R.id.yuvati_type_txt) as TextView
        val dialog_jyeshtaa_type_txt = dialog!!.findViewById(R.id.jyeshtaa_type_txt) as TextView
        val dialog_txt_guest_count = dialog!!.findViewById(R.id.txt_guest_count) as TextView
        val dialog_txt_sankhya_count = dialog!!.findViewById(R.id.txt_sankhya_count) as TextView
        val dialog_txt_total_count = dialog!!.findViewById(R.id.txt_total_count) as TextView

        dialog_active_inactive_txt.text = active_inactive_txt.text.toString()

        dialog_total_member_count.visibility = View.GONE
        dialog_total_member_count.text = total_member_count.text.toString()

        dialog_shishu_type_txt.text = shishu_type_txt.text.toString()
        dialog_baal_type_txt.text = baal_type_txt.text.toString()
        dialog_kishore_type_txt.text = kishore_type_txt.text.toString()
        dialog_tarun_type_txt.text = tarun_type_txt.text.toString()
        dialog_yuva_type_txt.text = yuva_type_txt.text.toString()
        dialog_jyeshta_type_txt.text = jyeshta_type_txt.text.toString()

        dialog_female_shishu_type_txt.text = female_shishu_type_txt.text.toString()
        dialog_balika_type_txt.text = balika_type_txt.text.toString()
        dialog_kishori_type_txt.text = kishori_type_txt.text.toString()
        dialog_taruni_type_txt.text = taruni_type_txt.text.toString()
        dialog_yuvati_type_txt.text = yuvati_type_txt.text.toString()
        dialog_jyeshtaa_type_txt.text = jyeshtaa_type_txt.text.toString()

        dialog_user_name_txt.text = user_name_txt.text.toString()
        dialog_shakha_name_txt.text = sessionManager.fetchSHAKHANAME()

        dialog_txt_guest_count.text = txt_guest_count.text.toString()
        dialog_txt_sankhya_count.text = txt_sankhya_count.text.toString()
        dialog_txt_total_count.text = txt_total_count.text.toString()

        val btnOk = dialog!!.findViewById(R.id.btnOk) as TextView

//        val mStringList = java.util.ArrayList<String>()
//        for (i in 0 until intent.getSerializableExtra("USERNAME_LIST").toString().length) {
//            mStringList.add(intent.getSerializableExtra("USERNAME_LIST").toString())
//        }
//
//        var mStringArray = mStringList.toArray()
//        mStringArray = mStringList.toArray(mStringArray)
//
//        val list: java.util.ArrayList<String> = arrayListOf<String>()
//
//        for (element in mStringArray) {
//            Log.d("LIST==>", element.toString())
//            list.add(element.toString())
//            Log.d("list==>", list.toString())
//
//            val listn = arrayOf(element)
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//                MemberListName = list
//            }
//        }

        val adapter = ArrayAdapter(
            this@SankhyaFormDetail, android.R.layout.simple_list_item_1, MemberListName
        )
        sankhya_list_view.adapter = adapter

//        members_listview.setOnClickListener {
//            if (IsVisible) {
//                sankhya_list_view.visibility = View.VISIBLE
//                IsVisible = false
//            } else if (!IsVisible) {
//                sankhya_list_view.visibility = View.GONE
//                IsVisible = true
//            }
//        }
        justifyListViewHeightBasedOnChildren(sankhya_list_view)
        btnOk.setOnClickListener {
            dialog?.dismiss()
        }
    }

    fun justifyListViewHeightBasedOnChildren(listView: ListView) {
        val adapter = listView.adapter ?: return
        val vg: ViewGroup = listView
        var totalHeight = 0
        for (i in 0 until adapter.count) {
            val listItem = adapter.getView(i, null, vg)
            listItem.measure(0, 0)
            totalHeight += listItem.measuredHeight
        }
        val par = listView.layoutParams
        par.height = totalHeight + listView.dividerHeight * (adapter.count - 1)
        listView.layoutParams = par
        listView.requestLayout()
    }
}