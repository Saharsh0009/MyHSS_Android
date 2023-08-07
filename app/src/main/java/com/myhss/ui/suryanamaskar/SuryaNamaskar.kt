package com.myhss.ui.suryanamaskar

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
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
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.DebugLog
import com.myhss.Utils.Functions
import com.myhss.ui.suryanamaskar.Model.BarchartDataModel
import com.myhss.ui.suryanamaskar.Model.Datum_Get_SuryaNamaskar
import com.myhss.ui.suryanamaskar.Model.Get_SuryaNamaskar_ModelResponse
import com.uk.myhss.Main.HomeActivity
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.ui.linked_family.Model.Get_Member_Listing_Datum
import com.uk.myhss.ui.linked_family.Model.Get_Member_Listing_Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class SuryaNamaskar : AppCompatActivity(), OnChartValueSelectedListener {

    private lateinit var sessionManager: SessionManager
    lateinit var back_arrow: ImageView
    lateinit var header_title: TextView
    lateinit var layout_pieChart_lable: LinearLayout
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
    lateinit var data_not_found_layout: RelativeLayout
    lateinit var mLayoutManager: LinearLayoutManager
    lateinit var linechart_layout: LinearLayout
    private lateinit var add_more: ImageView
    lateinit var pieChart_surya: PieChart
    private var memberDataList: List<Get_Member_Listing_Datum> =
        ArrayList<Get_Member_Listing_Datum>()
    private var surya_namaskarlist: List<Datum_Get_SuryaNamaskar> =
        ArrayList<Datum_Get_SuryaNamaskar>()


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
        header_title.text = getString(R.string.surya_namaskar)
        linechart_layout = findViewById(R.id.linechart_layout)
        pieChart_surya = findViewById(R.id.pieChart_surya)
        add_more = findViewById(R.id.info_tooltip)
        data_not_found_layout = findViewById(R.id.data_not_found_layout)
        layout_pieChart_lable = findViewById(R.id.layout_pieChart_lable)
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

//        SwipeleftToRightBack.enableSwipeBack(this)
//        SwipeleftToRightBack.enableSwipeBackFullView(this)
    }
//    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
//        return SwipeleftToRightBack.dispatchTouchEvent(this, event) || super.dispatchTouchEvent(event)
//    }

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
                            memberDataList = response.body()!!.data!!
                            Log.d("atheletsBeans", memberDataList.toString())

                            val mStringList = ArrayList<String>()
                            mStringList.add("All")
                            mStringList.add(sessionManager.fetchFIRSTNAME()!! + " " + sessionManager.fetchSURNAME()!!)
                            for (i in 0 until memberDataList.size) {
                                if (memberDataList[i].firstName.toString() + " " + memberDataList[i].lastName.toString() != sessionManager.fetchFIRSTNAME() + " " + sessionManager.fetchSURNAME()) {
                                    mStringList.add(
                                        memberDataList[i].firstName.toString() + " " + memberDataList[i].lastName.toString()
                                    )
                                }
                            }

                            val mStringListnew = ArrayList<String>()
                            mStringListnew.add("-99")
                            mStringListnew.add(sessionManager.fetchMEMBERID()!!)
                            for (i in 0 until memberDataList.size) {
                                if (memberDataList[i].memberId != sessionManager.fetchMEMBERID()) {
                                    mStringListnew.add(
                                        memberDataList[i].memberId.toString()
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
                            val memberStringList = ArrayList<String>()
                            for (i in 0 until surya_namaskarlist.size) {
                                memberStringList.add(
                                    surya_namaskarlist.get(i).getmember_id().toString()
                                )
                            }
                            var memberStringArray = memberStringList.toArray()
                            memberStringArray = memberStringList.toArray(memberStringArray)
                            val list: ArrayList<String> = arrayListOf<String>()
                            for (element in memberStringArray) {
                                list.add(element.toString())
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    IDMEMBER = list
                                }
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
        pieChart_surya.legend.isEnabled = false
//        pieChart_surya.legend.textSize = 16f
//        pieChart_surya.legend.isWordWrapEnabled = true
//        pieChart_surya.legend.setWordWrapEnabled(true)
//        pieChart_surya.setEntryLabelColor(Color.BLACK)
        pieChart_surya.setEntryLabelTextSize(12f)
        val member_list = IDMEMBER.toSet()
        val pieEntries: ArrayList<PieEntry> = ArrayList()
        val labelsList: java.util.ArrayList<String> = java.util.ArrayList()
        var t_count = 0
        for (i in 0 until member_list.size) {
            var s_count = 0
            var s_name = ""
            for (j in 0 until suryaNamaskarlistData.size) {
                if (member_list.elementAt(i) == suryaNamaskarlistData.get(j).getmember_id()) {
                    s_count = s_count + suryaNamaskarlistData.get(j).getcount()!!.toInt()
                    s_name = suryaNamaskarlistData.get(j).getmember_name().toString()
                }
            }
            t_count = t_count + s_count
            labelsList.add(s_name)
            pieEntries.add(PieEntry(s_count.toFloat(), s_name))
        }
        val pieDataSet = PieDataSet(pieEntries, "")
        pieDataSet.setDrawIcons(false)
        pieDataSet.valueFormatter = DefaultValueFormatter(0)
        pieDataSet.sliceSpace = 2f
        pieDataSet.iconsOffset = MPPointF(0f, 40f)
        pieDataSet.selectionShift = 2f
        val colors: ArrayList<Int> = ArrayList()
        val piechart_color: IntArray = getResources().getIntArray(R.array.pieChart)
        for (i in 0 until piechart_color.size) {
            colors.add(piechart_color[i])
        }
        pieDataSet.colors = colors
        val data = PieData(pieDataSet)
        data.setValueTextSize(14f)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        data.setValueTextColor(Color.WHITE)
        pieChart_surya.setData(data)
        pieChart_surya.setDrawEntryLabels(false)
        pieChart_surya.centerText = "Total\n" + t_count
        pieChart_surya.setCenterTextColor(Color.parseColor("#ff9800"))
        pieChart_surya.setCenterTextSize(30f)
        pieChart_surya.invalidate()

        setupPieChartChipGroup(labelsList)
    }

    private fun setupPieChartChipGroup(labelsList: java.util.ArrayList<String>) {
        val chipGroup = ChipGroup(this)
        chipGroup.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        chipGroup.isSingleSelection = true
        chipGroup.isSingleLine = false
        for (i in 0 until labelsList.size) {
            chipGroup.addView(createChip(labelsList.get(i).toString()))
        }
        val colorArray = resources.obtainTypedArray(R.array.pieChart)
        var t_color = 0;
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            val chipColor = colorArray.getColor(t_color, 0)
            t_color = if (t_color < (colorArray.length() - 1)) t_color + 1 else 0
            chip.chipBackgroundColor = ColorStateList.valueOf(chipColor)
        }
        layout_pieChart_lable.addView(chipGroup)
    }

    private fun createChip(label: String): Chip {
        val chip = Chip(
            this, null, com.google.android.material.R.style.Widget_MaterialComponents_Chip_Entry
        )
        chip.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        chip.text = label
        chip.isCloseIconVisible = false
        chip.isChipIconVisible = false
        chip.isCheckable = false
        chip.isClickable = true
        chip.setTextColor(Color.WHITE)
        chip.setOnClickListener {
            openBarChart(label.toString())
        }
        return chip
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        openBarChart((e as PieEntry).label.toString())
    }

    override fun onNothingSelected() {
    }

    private fun openBarChart(sName: String) {
        val listData_surya: ArrayList<BarchartDataModel> = ArrayList()
        for (i in 0 until surya_namaskarlist.size) {
            if (sName == surya_namaskarlist.get(i).getmember_name()) {
                val barchartDataModel = BarchartDataModel()
                barchartDataModel.setValue_x(surya_namaskarlist.get(i).getcount_date())
                barchartDataModel.setValue_y(surya_namaskarlist.get(i).getcount())
                barchartDataModel.setValue_user(surya_namaskarlist.get(i).getmember_name())
                listData_surya.add(barchartDataModel)
            }
        }
        val intent = Intent(this@SuryaNamaskar, ViewBarchartActivity::class.java)
        intent.putExtra("case", "1")
        intent.putExtra("list_data", listData_surya)
        startActivity(intent)
    }
}