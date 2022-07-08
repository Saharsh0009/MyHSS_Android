package com.uk.myhss.ui.sankhya_report

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
import com.uk.myhss.ui.sankhya_report.Model.Sankhya_details_Datum
import com.uk.myhss.ui.sankhya_report.Model.Sankhya_details_Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class SankhyaDetail : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

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
    private lateinit var baalika_type_txt: TextView
    private lateinit var kishori_type_txt: TextView
    private lateinit var taruni_type_txt: TextView
    private lateinit var yuvati_type_txt: TextView
    private lateinit var jyeshtaa_type_txt: TextView
    private lateinit var members_listview: TextView
    private lateinit var sankhya_list_view: ListView

    var MemberListName: ArrayList<String> = ArrayList<String>()
    var MemberListId: ArrayList<String> = ArrayList<String>()

    private var IsVisible = true
    private lateinit var rootLayout: LinearLayout

    lateinit var USERID: String
    lateinit var SANKHYA_ID: String

    @SuppressLint("NewApi", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sankhya_details)

        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("SankhyaDetailVC")
        sessionManager.firebaseAnalytics.setUserProperty("SankhyaDetailVC", "SankhyaDetail")

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        val back_arrow = findViewById<ImageView>(R.id.back_arrow)
        val header_title = findViewById<TextView>(R.id.header_title)

        header_title.text = getString(R.string.sankhya) + " Details"

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
        baalika_type_txt = findViewById(R.id.baalika_type_txt)
        kishori_type_txt = findViewById(R.id.kishori_type_txt)
        taruni_type_txt = findViewById(R.id.taruni_type_txt)
        yuvati_type_txt = findViewById(R.id.yuvati_type_txt)
        jyeshtaa_type_txt = findViewById(R.id.jyeshtaa_type_txt)

        members_listview = findViewById(R.id.members_listview)
        sankhya_list_view = findViewById(R.id.sankhya_list_view)

        rootLayout = findViewById(R.id.rootLayout)

        if (intent.getStringExtra("SANKHYA_ID") != "") {
            if (Functions.isConnectingToInternet(this@SankhyaDetail)) {
                USERID = sessionManager.fetchUserID()!!
                SANKHYA_ID = intent.getStringExtra("SANKHYA_ID")!!
                Log.d("USERID", USERID)
                Log.d("SANKHYA_ID", SANKHYA_ID)
                mySankhya_Record(USERID, SANKHYA_ID)
            } else {
                Toast.makeText(
                    this@SankhyaDetail,
                    resources.getString(R.string.no_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        back_arrow.setOnClickListener {
            finish()
        }
    }

    private fun mySankhya_Record(user_id: String, sankhya_id: String) {
        val pd = CustomProgressBar(this@SankhyaDetail)
        pd.show()
        val call: Call<Sankhya_details_Response> =
            MyHssApplication.instance!!.api.get_sankhya_get_record(user_id, sankhya_id)
        call.enqueue(object : Callback<Sankhya_details_Response> {
            override fun onResponse(
                call: Call<Sankhya_details_Response>,
                response: Response<Sankhya_details_Response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {
                        var sankhya_datum: List<Sankhya_details_Datum> =
                            ArrayList<Sankhya_details_Datum>()
//                    var sankhya_detail: List<Sankhya_details_member> =
//                        ArrayList<Sankhya_details_member>()

                        Log.d("sankhya_datum", response.body()?.data!!.size.toString())
//                    Log.d("sankhya_datum", sankhya_datum[0].member!!.size.toString())

//                    if (sankhya_datum[0].member!!.isNotEmpty()) {
                        try {
                            sankhya_datum = response.body()!!.data!!
                            Log.d("sankhya_datum", sankhya_datum.toString())

//                            sankhya_detail = sankhya_datum[0].member!!

                            total_member_count.text = sankhya_datum[0].member!!.size.toString()

                            user_name_txt.visibility = View.VISIBLE
                            user_name_txt.text =
                                sankhya_datum[0].utsav + " " + sankhya_datum[0].eventDate

//                            user_name_txt.text = sankhya_datum[0].member!![0].firstName!!.capitalize(Locale.ROOT) + " " +
//                                    sankhya_datum[0].member!![0].lastName!!.capitalize(Locale.ROOT)

                            shakha_name_txt.text = sessionManager.fetchSHAKHANAME()!!.capitalize(
                                Locale.ROOT)

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

                            active_inactive_txt.text = sankhya_datum[0].member!![0].ageCategory

                            shishu_type_txt.text = sankhya_datum[0].shishuMale
                            baal_type_txt.text = sankhya_datum[0].baal
                            kishore_type_txt.text = sankhya_datum[0].kishore
                            tarun_type_txt.text = sankhya_datum[0].tarun
                            yuva_type_txt.text = sankhya_datum[0].yuva
                            jyeshta_type_txt.text = sankhya_datum[0].proudh

                            female_shishu_type_txt.text = sankhya_datum[0].shishuFemale
                            baalika_type_txt.text = sankhya_datum[0].baalika
                            kishori_type_txt.text = sankhya_datum[0].kishori
                            taruni_type_txt.text = sankhya_datum[0].taruni
                            yuvati_type_txt.text = sankhya_datum[0].yuvati
                            jyeshtaa_type_txt.text = sankhya_datum[0].proudha

                            val mStringList = java.util.ArrayList<String>()
                            for (i in 0 until sankhya_datum[0].member!!.size) {
                                if (sankhya_datum[0].member!![i].middleName == "") {
                                    mStringList.add(
                                        sankhya_datum[0].member!![i].firstName!!.capitalize(Locale.ROOT)
                                                + " " + sankhya_datum[0].member!![i].lastName!!.capitalize(Locale.ROOT)
                                    )
                                } else {
                                    mStringList.add(
                                        sankhya_datum[0].member!![i].firstName!!.capitalize(Locale.ROOT)
                                                + " " + sankhya_datum[0].member!![i].middleName!!.capitalize(Locale.ROOT)
                                                + " " + sankhya_datum[0].member!![i].lastName!!.capitalize(Locale.ROOT)
                                    )
                                }
                            }

                            var mStringArray = mStringList.toArray()
                            mStringArray = mStringList.toArray(mStringArray)

                            val list: java.util.ArrayList<String> = arrayListOf<String>()

                            for (element in mStringArray) {
                                Log.d("LIST==>", element.toString())
                                list.add(element.toString())
                                Log.d("list==>", list.toString())

                                val listn = arrayOf(element)
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                    MemberListName = list
                                }
                            }

                            val adapter = ArrayAdapter(
                                this@SankhyaDetail,
                                android.R.layout.simple_list_item_1,
                                MemberListName
                            )
                            sankhya_list_view.adapter = adapter

                            members_listview.setOnClickListener {
                                if (IsVisible) {
                                    sankhya_list_view.visibility = View.VISIBLE
                                    IsVisible = false
                                } else if (!IsVisible) {
                                    sankhya_list_view.visibility = View.GONE
                                    IsVisible = true
                                }
                            }

                            justifyListViewHeightBasedOnChildren(sankhya_list_view)

                        } catch (e: ArithmeticException) {
                            println(e)
                        } finally {
                            println("Family")
                        }
//                    }
                    } else {
                        Functions.displayMessage(this@SankhyaDetail,response.body()?.message)
//                        Functions.showAlertMessageWithOK(
//                            this@SankhyaDetail, "",
////                        "Message",
//                            response.body()?.message
//                        )
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@SankhyaDetail, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Sankhya_details_Response>, t: Throwable) {
                Toast.makeText(this@SankhyaDetail, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
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