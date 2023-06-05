package com.myhss.ui.Barchat

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.DebugLog
import com.myhss.Utils.Functions
import com.myhss.Utils.InputFilterMinMax
import com.myhss.ui.Barchat.Model.Datum_Get_SuryaNamaskar
import com.myhss.ui.Barchat.Model.Get_SuryaNamaskar_ModelResponse
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import com.uk.myhss.Main.HomeActivity
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.ui.linked_family.Model.Get_Member_Listing_Datum
import com.uk.myhss.ui.linked_family.Model.Get_Member_Listing_Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToLong


class SuryaNamaskar : AppCompatActivity(), OnChartValueSelectedListener {

    private lateinit var sessionManager: SessionManager
    lateinit var back_arrow: ImageView
    lateinit var header_title: TextView
    lateinit var average_count: TextView
    private var Value = ""
    private var MEMBER_ID: String = ""

    var IDMEMBER: ArrayList<String> = ArrayList<String>()
    var UserName: ArrayList<String> = ArrayList<String>()
    var UserCategory: ArrayList<String> = ArrayList<String>()

    private var USER_NAME: String = ""
    private var USER_ID: String = ""
    lateinit var USERID: String
    lateinit var TAB: String
    lateinit var MEMBERID: String
    lateinit var STATUS: String
    lateinit var LENGTH: String
    lateinit var START: String
    lateinit var SEARCH: String
    lateinit var CHAPTERID: String

    private var athelets_Beans: List<Get_Member_Listing_Datum> =
        ArrayList<Get_Member_Listing_Datum>()

    var dialog: Dialog? = null
    lateinit var data_not_found_layout: RelativeLayout

    lateinit var mLayoutManager: LinearLayoutManager
    lateinit var linechart_layout: LinearLayout
    private lateinit var add_more: ImageView
    private var ddd = ArrayList<String>()
    private var surya_namaskarlist: List<Datum_Get_SuryaNamaskar> =
        ArrayList<Datum_Get_SuryaNamaskar>()

    var sum = 0.0
    var res = 0.0

    lateinit var pieChart_surya: PieChart

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barchart)

        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("SuryaNamaskarVC")
        sessionManager.firebaseAnalytics.setUserProperty("SuryaNamaskarVC", "SuryaNamaskar")

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        back_arrow = findViewById(R.id.back_arrow)
        header_title = findViewById(R.id.header_title)
        average_count = findViewById(R.id.average_count)
        header_title.text = getString(R.string.surya_namaskar)
        linechart_layout = findViewById(R.id.linechart_layout)
        pieChart_surya = findViewById(R.id.pieChart_surya)
        add_more = findViewById(R.id.info_tooltip)
        data_not_found_layout = findViewById(R.id.data_not_found_layout)
        add_more.setImageResource(R.drawable.ic_plus)
        back_arrow.setOnClickListener {
            val i = Intent(this@SuryaNamaskar, HomeActivity::class.java)
            startActivity(i)
            finishAffinity()
        }
        callMemberListApi()
        add_more.visibility = View.VISIBLE
        add_more.setOnClickListener {
            val i = Intent(this@SuryaNamaskar, AddSuryaNamaskarActivity::class.java)
            startActivity(i)
        }
    }

    private fun callMemberListApi() {
        if (Functions.isConnectingToInternet(this@SuryaNamaskar)) {
            val end: Int = 100
            val start: Int = 0
            USERID = sessionManager.fetchUserID()!!
            Log.d("USERID", USERID)
            TAB = "family"
            MEMBERID = sessionManager.fetchMEMBERID()!!
            STATUS = "all"
            LENGTH = end.toString()
            START = start.toString()
            SEARCH = ""
            CHAPTERID = ""
            myMemberList(USERID, TAB, MEMBERID, STATUS, LENGTH, START, SEARCH, CHAPTERID)
        } else {
            Toast.makeText(
                this@SuryaNamaskar, resources.getString(R.string.no_connection), Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun myMemberList(
        user_id: String,
        tab: String,
        member_id: String,
        status: String,
        length: String,
        start: String,
        search: String,
        chapter_id: String
    ) {
        val pd = CustomProgressBar(this@SuryaNamaskar)
        pd.show()
        val call: Call<Get_Member_Listing_Response> =
            MyHssApplication.instance!!.api.get_member_listing(
                user_id, tab, member_id, status, length, start, search, chapter_id
            )
        call.enqueue(object : Callback<Get_Member_Listing_Response> {
            override fun onResponse(
                call: Call<Get_Member_Listing_Response>,
                response: Response<Get_Member_Listing_Response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {
                        try {
                            athelets_Beans = response.body()!!.data!!
                            Log.d("atheletsBeans", athelets_Beans.toString())

                            val mStringList = ArrayList<String>()
                            mStringList.add("All")
                            mStringList.add(sessionManager.fetchFIRSTNAME()!! + " " + sessionManager.fetchSURNAME()!!)
                            for (i in 0 until athelets_Beans.size) {
                                if (athelets_Beans[i].firstName.toString() + " " + athelets_Beans[i].lastName.toString() != sessionManager.fetchFIRSTNAME() + " " + sessionManager.fetchSURNAME()) {
                                    mStringList.add(
                                        athelets_Beans[i].firstName.toString() + " " + athelets_Beans[i].lastName.toString()
                                    )
                                }
                            }

                            val mStringListnew = ArrayList<String>()
                            mStringListnew.add("-99")
                            mStringListnew.add(sessionManager.fetchMEMBERID()!!)
                            for (i in 0 until athelets_Beans.size) {
                                if (athelets_Beans[i].memberId != sessionManager.fetchMEMBERID()) {
                                    mStringListnew.add(
                                        athelets_Beans[i].memberId.toString()
                                    )
                                }
                            }

                            var mStringArray = mStringList.toArray()
                            var mStringArraynew = mStringListnew.toArray()

                            mStringArray = mStringList.toArray(mStringArray)
                            mStringArraynew = mStringListnew.toArray(mStringArraynew)

                            val list: java.util.ArrayList<String> = arrayListOf<String>()
                            val listnew: java.util.ArrayList<String> = arrayListOf<String>()

                            for (element in mStringArray) {
                                list.add(element.toString())
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                    UserName = list
                                }
                            }

                            for (element in mStringArraynew) {
                                listnew.add(element.toString())
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                    UserCategory = listnew
                                }
                            }
                            val distinct = UserCategory.toSet().toList();
                            if (Functions.isConnectingToInternet(this@SuryaNamaskar)) {
                                MEMBER_ID =
                                    distinct.toString().replace("[", "").replace("]", "").trim()
                                SuryanamaskarList(MEMBER_ID, "true")
                            } else {
                                Toast.makeText(
                                    this@SuryaNamaskar,
                                    resources.getString(R.string.no_connection),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } catch (e: ArithmeticException) {
                            println(e)
                        } finally {
                            println("Family")
                        }
                    } else {
                        USER_NAME = sessionManager.fetchUSERNAME()!!
                        USER_ID = sessionManager.fetchMEMBERID()!!
                        if (Functions.isConnectingToInternet(this@SuryaNamaskar)) {
                            sum = 0.0
                            res = 0.0
                            DebugLog.e("MEMBER_ID 3==>" + sessionManager.fetchMEMBERID()!!)
                            SuryanamaskarList(sessionManager.fetchMEMBERID()!!, "true")
                        } else {
                            Toast.makeText(
                                this@SuryaNamaskar,
                                resources.getString(R.string.no_connection),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@SuryaNamaskar, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Get_Member_Listing_Response>, t: Throwable) {
                Toast.makeText(this@SuryaNamaskar, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    private fun SuryanamaskarList(member_id: String, is_api: String) {
        val pd = CustomProgressBar(this)
        pd.show()
        val call: Call<Get_SuryaNamaskar_ModelResponse> =
            MyHssApplication.instance!!.api.get_suryanamaskar_count(member_id, is_api)
        call.enqueue(object : Callback<Get_SuryaNamaskar_ModelResponse> {
            @RequiresApi(Build.VERSION_CODES.N)
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<Get_SuryaNamaskar_ModelResponse>,
                response: Response<Get_SuryaNamaskar_ModelResponse>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    DebugLog.d("status => " + response.body()?.status.toString())
                    if (response.body()?.status!!) {
                        data_not_found_layout.visibility = View.GONE
                        try {
                            surya_namaskarlist = response.body()!!.data!!
                            val mStringList = ArrayList<String>()
                            for (i in 0 until surya_namaskarlist.size) {
                                mStringList.add(surya_namaskarlist.get(i).getmember_id().toString())
                            }
                            var mStringArray = mStringList.toArray()
                            mStringArray = mStringList.toArray(mStringArray)
                            val list: ArrayList<String> = arrayListOf<String>()
                            for (element in mStringArray) {
                                list.add(element.toString())
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    IDMEMBER = list
                                }
                            }
                            DebugLog.d("surya_namaskarlist=>111 : " + surya_namaskarlist.toString())
                            for (i in surya_namaskarlist.indices) {
                                surya_namaskarlist.filter { it.getmember_id() == surya_namaskarlist[i].getmember_id() }
                            }
                            DebugLog.d("surya_namaskarlist=> : " + surya_namaskarlist.toString())

                            //now draw bar chart with dynamic data
                            val entries: ArrayList<BarEntry> = ArrayList()
                            //you can replace this data object with  your custom object
                            for (i in surya_namaskarlist.indices) {
                                val score = surya_namaskarlist[i]
                                entries.add(BarEntry(i.toFloat(), score.getcount()!!.toFloat()))
                                Value = score.toString()
                            }

                            val mStringListnew = ArrayList<String>()
                            for (i in 0 until surya_namaskarlist.size) {
                                mStringListnew.add(
                                    surya_namaskarlist[i].getcount().toString()
                                )
                            }
                            val format = DecimalFormat("###.##")
                            for (i in 0 until mStringListnew.size) {
                                sum = mStringListnew.get(i).toInt() + sum
                            }
                            format.roundingMode = RoundingMode.CEILING
                            res = sum // / mStringListnew.size
                            DebugLog.e("The average is ==> : " + format.format(res))
                            average_count.text = format.format(res.roundToLong())
                            val dateee = ArrayList<String>()
                            for (i in 0 until surya_namaskarlist.size) {
                                dateee.add(surya_namaskarlist[i].getcount_date().toString())

                                val parts: Array<String> = dateee[i].split("-").toTypedArray()
                                ddd.add(parts[0] + "-" + parts[1])
                            }
                            pieChart_surya.visibility = View.VISIBLE
                            setPieChartForSuryanamaskar(surya_namaskarlist)
                        } catch (e: ArithmeticException) {
                            DebugLog.e(e.toString())
                        } finally {
                            DebugLog.e("Family")
                        }
                    } else {
                        data_not_found_layout.visibility = View.VISIBLE
                        pieChart_surya.visibility = View.GONE
                        average_count.text = "0.0"
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@SuryaNamaskar,
                        "Message",
                        getString(R.string.some_thing_wrong),
                    )
                    pieChart_surya.visibility = View.GONE
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Get_SuryaNamaskar_ModelResponse>, t: Throwable) {
                Toast.makeText(this@SuryaNamaskar, t.message, Toast.LENGTH_LONG).show()
                pieChart_surya.visibility = View.GONE
                pd.dismiss()
            }
        })
    }

    private fun setPieChartForSuryanamaskar(suryaNamaskarlistData: List<Datum_Get_SuryaNamaskar>) {
        pieChart_surya.setOnChartValueSelectedListener(this)
        pieChart_surya.setUsePercentValues(false)
        pieChart_surya.getDescription().setEnabled(false)
        pieChart_surya.setExtraOffsets(5f, 10f, 5f, 5f)
        pieChart_surya.setDragDecelerationFrictionCoef(0.95f)
        pieChart_surya.setDrawHoleEnabled(true)
        pieChart_surya.setHoleColor(Color.WHITE)
        pieChart_surya.setTransparentCircleColor(Color.WHITE)
        pieChart_surya.setTransparentCircleAlpha(110)
        pieChart_surya.setHoleRadius(50f)
        pieChart_surya.setTransparentCircleRadius(53f)
        pieChart_surya.setDrawCenterText(true)
        pieChart_surya.setRotationAngle(0f)
        pieChart_surya.setRotationEnabled(true)
        pieChart_surya.setHighlightPerTapEnabled(true)
        pieChart_surya.animateY(2000, Easing.EaseInOutQuad)
        pieChart_surya.legend.isEnabled = true
        pieChart_surya.legend.textSize = 16f
        pieChart_surya.legend.isWordWrapEnabled = true
        pieChart_surya.legend.setWordWrapEnabled(true)
        pieChart_surya.setEntryLabelColor(Color.BLACK)
        pieChart_surya.setEntryLabelTextSize(12f)
        val deDupedNodes = IDMEMBER.toSet()
        val pieEntries: ArrayList<PieEntry> = ArrayList()
        for (i in 0 until deDupedNodes.size) {
            var s_count = 0
            var s_name = ""
            for (j in 0 until suryaNamaskarlistData.size) {
                if (deDupedNodes.elementAt(i) == suryaNamaskarlistData.get(j).getmember_id()) {
                    s_count = s_count + suryaNamaskarlistData.get(j).getcount()!!.toInt()
                    s_name = suryaNamaskarlistData.get(j).getmember_name().toString()
                }
            }
            pieEntries.add(PieEntry(s_count.toFloat(), s_name))
        }
        val pieDataSet = PieDataSet(pieEntries, "")
        pieDataSet.setDrawIcons(false)
        pieDataSet.valueFormatter = DefaultValueFormatter(0)
        pieDataSet.sliceSpace = 3f
        pieDataSet.iconsOffset = MPPointF(0f, 40f)
        pieDataSet.selectionShift = 5f
        val colors: ArrayList<Int> = ArrayList()
        val piechart_color: IntArray = getResources().getIntArray(R.array.pieChart)
        for (i in 0 until piechart_color.size) {
            colors.add(piechart_color[i])
        }
        pieDataSet.colors = colors
        val data = PieData(pieDataSet)
        data.setValueTextSize(14f)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        data.setValueTextColor(Color.BLACK)
        pieChart_surya.setData(data)
        pieChart_surya.setDrawEntryLabels(false)
        pieChart_surya.invalidate()
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        val listData_surya: ArrayList<Datum_Get_SuryaNamaskar> = ArrayList()
        for (i in 0 until surya_namaskarlist.size) {
            if ((e as PieEntry).label.toString() == surya_namaskarlist.get(i).getmember_name()) {
                listData_surya.add(surya_namaskarlist.get(i))
            }
        }
        val intent = Intent(this@SuryaNamaskar, ViewBarchartActivity::class.java)
        intent.putExtra("name", (e as PieEntry).label.toString())
        intent.putExtra("list_data", listData_surya)
        startActivity(intent)
    }

    override fun onNothingSelected() {
    }
}

