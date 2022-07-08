package com.myhss.ui.Barchat

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.Functions
import com.myhss.Utils.InputFilterMinMax
import com.myhss.ui.Barchat.Adapter.SuryaAdapter
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
import kotlin.math.roundToLong


class SuryaNamaskar : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    lateinit var back_arrow: ImageView
    lateinit var header_title: TextView
    lateinit var average_count: TextView

    //    private val MAX_X_VALUE = 7
    private val MIN_VALUE = 0
//    private val MAX_VALUE = 100
//    private val MAX_Y_VALUE = 100
//    private val MIN_Y_VALUE = 5
    private val SET_LABEL = "Suryanamaskar"
//    private val DAYS = arrayOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")

    private var Value = ""
    private var MEMBER_ID: String = ""
    private lateinit var family_txt: SearchableSpinner
    lateinit var username: TextView
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
//    lateinit var parentLinearLayout: LinearLayout

    lateinit var add_listview: RecyclerView
    lateinit var mLayoutManager: LinearLayoutManager
    lateinit var linechart_layout: LinearLayout
    private var mAdapter: SuryaAdapter? = null

    /*LINE CHART ARRARY LIST*/
//    private val xVals = ArrayList<String>()
//    private val yVals = ArrayList<Int>()

//    private var chart: BarChart? = null

    private lateinit var barChart: BarChart
    private lateinit var barChartnew: BarChart
    private lateinit var add_more: TextView

//    private var scoreList = ArrayList<Score>()
    private var ddd = ArrayList<String>()
    private var temperatureList = ArrayList<Temperature>()
    private var barModelList = ArrayList<BarModel>()

    private var surya_namaskarlist: List<Datum_Get_SuryaNamaskar> =
        ArrayList<Datum_Get_SuryaNamaskar>()

    var sum = 0.0
    var res = 0.0

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

        back_arrow.setOnClickListener {
            val i = Intent(this@SuryaNamaskar, HomeActivity::class.java)
            startActivity(i)
            finishAffinity()
        }

        add_listview = findViewById(R.id.add_listview)
        add_listview.visibility = View.GONE
        family_txt = findViewById(R.id.family_txt)
        username = findViewById(R.id.username)

        mLayoutManager = LinearLayoutManager(this@SuryaNamaskar)
        add_listview.layoutManager = mLayoutManager

//        add_listview.layoutManager = LinearLayoutManager(this)
//        add_listview.setHasFixedSize(true)

//        init()

        CallAPI()

        linechart_layout = findViewById(R.id.linechart_layout)
        add_more = findViewById(R.id.add_more)
        data_not_found_layout = findViewById(R.id.data_not_found_layout)
        barChartnew = findViewById(R.id.barChartnew)
        barChart = findViewById(R.id.barChart)
        barChart.visibility = View.GONE
//        barChartnew.visibility = View.GONE

        family_txt.onItemSelectedListener = mOnItemSelectedListener_family
        family_txt.setTitle("Select Family Member")

//        parentLinearLayout = findViewById(R.id.parent_linear_layout)

        add_more.visibility = View.VISIBLE

        add_more.setOnClickListener {
//            AddSuryanamasakarDialog()
            val i = Intent(this@SuryaNamaskar, AddSuryaNamaskarActivity::class.java)
            startActivity(i)
        }

    }

//    private fun init() {
//        chart = findViewById(R.id.barChart)
//
//        val data: BarData = createChartData()
//        configureChartAppearance()
//        prepareChartData(data)
//    }
//
//    private fun prepareChartData(data: BarData) {
//        data.setValueTextSize(12f)
//        chart!!.data = data
//        chart!!.invalidate()
//    }

//    private fun configureChartAppearance() {
//        chart!!.setPinchZoom(true)
//        chart!!.setFitBars(true)
//        chart!!.setDrawBarShadow(true)
//        chart!!.setDrawGridBackground(false)
//        chart!!.description.isEnabled = false
//        val xAxis = chart!!.xAxis
//        xAxis.granularity = 1f
//        xAxis.setCenterAxisLabels(true)
//        val leftAxis = chart!!.axisLeft
//        leftAxis.setDrawGridLines(false)
//        leftAxis.spaceTop = 35f
//        leftAxis.axisMinimum = 0f
//        chart!!.axisRight.isEnabled = false
//        chart!!.xAxis.axisMinimum = 0f
//        chart!!.xAxis.axisMaximum = MAX_X_VALUE.toFloat()
//    }

//    private fun createChartData(): BarData {
//        val values: ArrayList<BarEntry> = ArrayList()
//        for (i in 0 until MAX_X_VALUE) {
//            val x = i.toFloat()
//            val y = Util()
//                .randomFloatBetween(MIN_Y_VALUE.toFloat(), MAX_Y_VALUE.toFloat())
//            values.add(BarEntry(x, y))
//        }
//        val set1 = BarDataSet(values, SET_LABEL)
//        val dataSets: ArrayList<IBarDataSet> = ArrayList()
//        dataSets.add(set1)
//        return BarData(dataSets)
//    }

    private fun CallAPI() {
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
                this@SuryaNamaskar,
                resources.getString(R.string.no_connection),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    @SuppressLint("InflateParams")
    @RequiresApi(Build.VERSION_CODES.N)
    fun AddSuryanamasakarDialog() {
        // Deposit Dialog
        if (dialog == null) {
            dialog = Dialog(this, R.style.StyleCommonDialog)
        }
        dialog?.setContentView(R.layout.edit_dialog_add_suryanamasakr)
//        dialog?.setCanceledOnTouchOutside(false)
        dialog?.show()

//        val c = Calendar.getInstance()
//        val mYear = c[Calendar.YEAR]
//        val mMonth = c[Calendar.MONTH]
//        val mDay = c[Calendar.DAY_OF_MONTH]

        val calendar = Calendar.getInstance(TimeZone.getDefault())

        val currentYear = calendar[Calendar.YEAR]
        val currentMonth = calendar[Calendar.MONTH] + 1
        val currentDay = calendar[Calendar.DAY_OF_MONTH]

        Log.e("CurrentDate", "" + currentDay + "-" + currentMonth + "-" + currentYear)
        Log.e("PreviousDate", "" + (currentDay - 1) + "-" + currentMonth + "-" + currentYear)
        Log.e("LastPrivousDate", "" + (currentDay - 2) + "-" + currentMonth + "-" + currentYear)

//        val currentDate: String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

        val parentLinearLayout = dialog!!.findViewById(R.id.parent_linear_layout) as LinearLayout
        val select_date = dialog!!.findViewById(R.id.select_date) as TextView
        val edit_count = dialog!!.findViewById(R.id.edit_count) as EditText
        val btnOk = dialog!!.findViewById(R.id.btnOk) as TextView
        val add_more_btn = dialog!!.findViewById(R.id.add_more_btn) as TextView
        val btnCancel = dialog!!.findViewById(R.id.btnCancel) as TextView
        val delete_button = dialog!!.findViewById(R.id.delete_button) as Button

        select_date.text = "" + currentDay + "/" + currentMonth + "/" + currentYear

        add_more_btn.setOnClickListener {
            val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val rowView: View = inflater.inflate(R.layout.field, null)
            // Add the new row before the add field button.
            parentLinearLayout.addView(rowView, parentLinearLayout.childCount - 1)
//            val inflater: LayoutInflater =
//                getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//            val rowView: View = inflater.inflate(R.layout.field, null)
//            parentLinearLayout.addView(rowView, parentLinearLayout.childCount - 1)
        }

        delete_button.setOnClickListener {
            parentLinearLayout.removeView(it)
        }


        btnOk.setOnClickListener {
            if (edit_count.text.toString().isNotEmpty()) {
                edit_count.filters = arrayOf<InputFilter>(
                    InputFilterMinMax(
                        "1",
                        "100"
                    )
                )
                if (Integer.valueOf(edit_count.text.toString()) > 0 && Integer.valueOf(edit_count.text.toString()) <= 1000) {
                    edit_count.text = edit_count.text
                    dialog?.dismiss()
                } else {
                    Toast.makeText(
                        this@SuryaNamaskar,
                        "Please enter count 1-100",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

            } else {
                Toast.makeText(this@SuryaNamaskar, "Please enter count", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            select_date.text = ""
            edit_count.setText("")
        }

    }

    private val mOnItemSelectedListener_family: AdapterView.OnItemSelectedListener = object :
        AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            Log.d("Name", UserName[position])
            Log.d("Postion", UserCategory[position])
            USER_NAME = UserName[position]
            USER_ID = UserCategory[position]

            if (USER_ID == "-99") {
                sum = 0.0
                res = 0.0
                SuryanamaskarList(MEMBER_ID, "true")
            } else {
                if (Functions.isConnectingToInternet(this@SuryaNamaskar)) {
                    println("MEMBER_ID==>$USER_ID")
                    sum = 0.0
                    res = 0.0
                    SuryanamaskarList(USER_ID, "true")
                } else {
                    Toast.makeText(
                        this@SuryaNamaskar,
                        resources.getString(R.string.no_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
//            TODO("Not yet implemented")
        }
    }


    private fun myMemberList(
        user_id: String, tab: String, member_id: String, status: String,
        length: String, start: String, search: String, chapter_id: String
    ) {
        val pd = CustomProgressBar(this@SuryaNamaskar)
        pd.show()
        val call: Call<Get_Member_Listing_Response> =
            MyHssApplication.instance!!.api.get_member_listing(
                user_id, tab, member_id,
                status, length, start, search, chapter_id
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
                                if(athelets_Beans[i].firstName.toString() + " " + athelets_Beans[i].lastName.toString() != sessionManager.fetchFIRSTNAME() + " "+ sessionManager.fetchSURNAME()) {
                                    mStringList.add(
                                        athelets_Beans[i].firstName.toString() + " " + athelets_Beans[i].lastName.toString()
                                    )
                                }
                            }

                            val mStringListnew = ArrayList<String>()
                            mStringListnew.add("-99")
                            mStringListnew.add(sessionManager.fetchMEMBERID()!!)
                            for (i in 0 until athelets_Beans.size) {
                                if(athelets_Beans[i].memberId != sessionManager.fetchMEMBERID()) {
                                    mStringListnew.add(
                                        athelets_Beans[i].memberId.toString()
                                    )
                                }
                            }

                            var mStringArray = mStringList.toArray()
                            var mStringArraynew = mStringListnew.toArray()

                            for (i in mStringArray.indices) {
                                Log.d("string is", mStringArray[i] as String)
                            }

                            for (i in mStringArraynew.indices) {
                                Log.d("mStringArraynew is", mStringArraynew[i] as String)
                            }

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

                            SearchSpinner(UserName.toTypedArray(), family_txt)

//                            val list: List<String> = listOf("One", "Two", "One")

                            val distinct = UserCategory.toSet().toList();

                            println(distinct)

                            if (Functions.isConnectingToInternet(this@SuryaNamaskar)) {
//                                val str = "A,B,C,D"
//                                val list: List<String> = str.split(",").toList()
                                MEMBER_ID =
                                    distinct.toString().replace("[", "").replace("]", "").trim()

                                println("MEMBER_ID==>$MEMBER_ID")
//                                sum = 0.0
//                                res = 0.0
//                                SuryanamaskarList(MEMBER_ID, "true")
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

                        family_txt.visibility = View.GONE
                        username.visibility = View.VISIBLE

                        USER_NAME = sessionManager.fetchUSERNAME()!!
                        USER_ID = sessionManager.fetchMEMBERID()!!
                        username.text = USER_NAME

                        if (Functions.isConnectingToInternet(this@SuryaNamaskar)) {
                            sum = 0.0
                            res = 0.0
                            SuryanamaskarList(sessionManager.fetchMEMBERID()!!, "true")
                        } else {
                            Toast.makeText(
                                this@SuryaNamaskar,
                                resources.getString(R.string.no_connection),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
//                        Functions.showAlertMessageWithOK(
//                            this@AddSuryaNamaskarActivity,
//                            "Message",
//                            response.body()?.message
//                        )
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

    private fun SearchSpinner(
        spinner_search: Array<String>,
        edit_txt: SearchableSpinner
    ) {
        val searchmethod = ArrayAdapter(
            this, android.R.layout.simple_spinner_item,
            spinner_search
        )
        searchmethod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        edit_txt.adapter = searchmethod
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
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {
                        data_not_found_layout.visibility = View.GONE
                        barChartnew.visibility = View.VISIBLE
//                        barChart.visibility = View.VISIBLE
//                        linechart_layout.visibility =View.VISIBLE
                        val list = ArrayList<String>()

                        try {
                            surya_namaskarlist = response.body()!!.data!!

                            Log.d("surya_namaskarlist", surya_namaskarlist[0].getcount_date()!!)
                            Log.d("count=>", surya_namaskarlist.size.toString())

                            list.add(surya_namaskarlist.toString())

                            mAdapter = SuryaAdapter(surya_namaskarlist)

                            add_listview.adapter = mAdapter
                            mAdapter!!.notifyDataSetChanged()

                            val mStringList = ArrayList<String>()
                            for (i in 0 until surya_namaskarlist.size) {
                                mStringList.add(
                                    surya_namaskarlist[i].getmember_id().toString()
                                )
                            }

                            var mStringArray = mStringList.toArray()

                            for (i in mStringArray.indices) {
                                Log.d("string is", mStringArray[i] as String)
                            }

                            mStringArray = mStringList.toArray(mStringArray)

                            val list: ArrayList<String> = arrayListOf<String>()

                            for (element in mStringArray) {
                                list.add(element.toString())
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    IDMEMBER = list
                                }
                            }

                            Log.d("surya_namaskarlist=>111", surya_namaskarlist.toString())
//                            val listnew = listOf(
//                                "orange",
//                                "apple",
//                                "apple",
//                                "banana",
//                                "water",
//                                "bread",
//                                "banana"
//                            )

                            println(
                                "Comman==" + IDMEMBER.groupingBy { it }.eachCount()
                                    .filter { it.value > 1 })

                            for (i in surya_namaskarlist.indices) {
                                surya_namaskarlist.filter { it.getmember_id() == surya_namaskarlist[i].getmember_id() }
                            }
                            Log.d("surya_namaskarlist=>", surya_namaskarlist.toString())

                            //now draw bar chart with dynamic data
                            val entries: ArrayList<BarEntry> = ArrayList()
//                            val entriesnew: ArrayList<BarEntry> = ArrayList()

                            //you can replace this data object with  your custom object
                            for (i in surya_namaskarlist.indices) {
                                val score = surya_namaskarlist[i]
                                entries.add(BarEntry(i.toFloat(), score.getcount()!!.toFloat()))
//                                entriesnew.add(BarEntry(i.toFloat(), score.getcount_date()!!.toFloat()))
//            entries.add(BarEntry(i.toFloat(), score.getcount()!!.toFloat()))
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
                            Log.e("==>", "The average is " + format.format(res))
//                                val rounded = number.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
//                            average_count.text = format.format(res).toDouble()
                            average_count.text = format.format(res.roundToLong())
//                            textViewRes.setText("The average is " + format.format(res))
//                            res = 0.0

//                            val entriesnew = ArrayList<BarEntry>()
//                            val labels = ArrayList<String>()
//                            val arr = JSONArray(surya_namaskarlist)
//                            for (i in 0 until arr.length()) {
//                                entriesnew.add(BarEntry(arr.getJSONObject(i).getInt("count").toFloat(), i))
//                                labels.add(arr.getJSONObject(i).getString("date"))
//                            }
                            val dateee = ArrayList<String>()
                            for (i in 0 until surya_namaskarlist.size) {
                                dateee.add(surya_namaskarlist[i].getcount_date().toString())

                                val parts: Array<String> = dateee[i].split("-").toTypedArray()
                                println("DD: " + parts[0])
                                println("MM: " + parts[1])
                                println("YY: " + parts[2])

                                ddd.add(parts[0] + "-" + parts[1])
                            }

                            Log.e("ddd===>", ddd.toString())

//                            scoreList = getScoreList()
                            temperatureList = getTemperatureList()
                            initBarChart()
                            setDataForBarChart()

//                            val datanew: BarData = createChartData(ddd)
//                            configureChartAppearance()
//                            prepareChartData(datanew)

                            val barDataSet = BarDataSet(entries, "Cells")
//                            val barDataSet1 = BarDataSet(entries, "Cells")
                            barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
//                            barDataSet1.setColors(*CollorTemplate.COLORFUL_COLORS)

//                            val data = BarData(labels, barDataSet)

                            val data = BarData(barDataSet)
                            barChart.data = data

                            data.barWidth = 0.5f
                            barChart.setFitBars(true)

                            barChart.invalidate()

                            //remove right y-axis
                            barChart.axisRight.isEnabled = false

                            //remove legend
                            barChart.legend.isEnabled = false

                            //remove description label
                            barChart.description.isEnabled = false

                            barChart.axisLeft.setDrawLabels(true);
                            barChart.axisRight.setDrawLabels(false);
                            barChart.xAxis.setDrawLabels(false);

                            barChart.legend.isEnabled = false
                            barChart.axisLeft.setDrawGridLines(false)
                            barChart.axisRight.setDrawGridLines(false)
                            barChart.legend.isEnabled = false

                            val xAxis: XAxis = barChart.xAxis
                            xAxis.setDrawGridLines(false)
                            xAxis.setDrawAxisLine(false)

                            //add animation
                            barChart.animateY(3000)
                            barChart.setVisibleXRangeMaximum(8f); // allow 5 values to be displayed
                            barChart.moveViewToX(1f);
                            barChart.isDoubleTapToZoomEnabled = false
                            barChart.setPinchZoom(false)
                            barChart.setTouchEnabled(true)


                            // to draw label on xAxis
                            xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
//                            xAxis.valueFormatter = MyAxisFormatter()
                            xAxis.position = XAxis.XAxisPosition.BOTTOM
                            xAxis.setDrawLabels(true)
                            xAxis.granularity = 1f
                            xAxis.labelRotationAngle = +90f
//                            barChart.setOnClickListener {
//                                Toast.makeText(this@SuryaNamaskar, "", Toast.LENGTH_SHORT).show()
//                            }

//                            for (i in 0 until surya_namaskarlist.size) {
//                                scoreList.add(Score("John", 56))
//                            }
//                            scoreList = surya_namaskarlist
                        } catch (e: ArithmeticException) {
                            println(e)
                        } finally {
                            println("Family")
                        }
                    } else {
                        data_not_found_layout.visibility = View.VISIBLE
                        barChart.visibility = View.GONE
                        barChartnew.visibility = View.GONE
//                        linechart_layout.visibility =View.GONE
//                        Functions.displayMessage(this@AllShakhaListActivity,response.body()?.message)
//                        Functions.showAlertMessageWithOK(
//                            this@AllShakhaListActivity, "",
////                        "Message",
//                            response.body()?.message
//                        )
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@SuryaNamaskar, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Get_SuryaNamaskar_ModelResponse>, t: Throwable) {
                Toast.makeText(this@SuryaNamaskar, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    inner class MyAxisFormatter : IndexAxisValueFormatter() {
        override fun getFormattedValue(value: Float, axis: AxisBase?): String {
//            TODO("Not yet implemented")
            val index = value.toInt()
            return if (index < temperatureList.size) {
                temperatureList[index].day
            } else {
                ""
            }
        }

        /*override fun getDecimalDigits(): Int {
//            TODO("Not yet implemented")
            return 0
        }*/

    }

    /*inner class MyAxisFormatter : IndexAxisValueFormatter() {
         fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            Log.d("TAG", "getAxisLabel: index $index")
            return if (index < temperatureList.size) {
                temperatureList[index].day
            } else {
                ""
            }
        }
    }*/

    private fun initBarChart() {
        val xAxis = barChartnew.xAxis
        val yAxisL = barChartnew.axisLeft
        val yAxisR = barChartnew.axisRight

        xAxis.granularity = 1f
        yAxisL.granularity = 1f
        yAxisL.axisMinimum = MIN_VALUE.toFloat() // start at zero
//        yAxisL.axisMaximum = surya_namaskarlist.size.toFloat()
        yAxisR.isEnabled = false
        barChartnew.setDrawValueAboveBar(true)
        barChartnew.setFitBars(true)
//        barChartnew.isDragEnabled = false
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawLabels(true)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = MyAxisFormatter()
        yAxisL.setDrawGridLines(true)
//        val description: Description = barChart.description
//        description.isEnabled = true

//        val legend: Legend = barChartnew.legend
//        legend.isEnabled = true

        // disable zoom , drag and highlight of bar
//        barChartnew.highlightValues(null)
        barChartnew.isDoubleTapToZoomEnabled = false
//        barChartnew.isDragEnabled = true
        barChartnew.legend.isEnabled = false
        barChartnew.setScaleEnabled(false)
        barChartnew.setPinchZoom(false)
        barChartnew.setTouchEnabled(true)
        barChartnew.setDrawBarShadow(false)
        barChartnew.isHighlightPerTapEnabled = false
        barChartnew.isHorizontalScrollBarEnabled = true

        //add listener
//        barChart.onChartGestureListener = this
    }


    // simulate api call
    // we are initialising it directly
    @SuppressLint("NewApi")
    private fun getTemperatureList(): ArrayList<Temperature> {
        temperatureList.clear()
        val dateee = ArrayList<String>()
        dateee.clear()
        for (i in 0 until surya_namaskarlist.size) {
//            val myFormat = "dd-MMM"
//            val sdf = SimpleDateFormat(myFormat, Locale.getDefault())

            val outputFormat: DateFormat = SimpleDateFormat("dd-MMM", Locale.US)
            val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

            val inputText = surya_namaskarlist[i].getcount_date().toString()
            val date: Date = inputFormat.parse(inputText)
            val outputText: String = outputFormat.format(date)

            temperatureList.add(Temperature(outputText, surya_namaskarlist[i].getcount()!!
                .toFloat()))
        }
//        scoreList.add(Score("Rey", "75"))
//        scoreList.add(Score("Steve", "85"))
//        scoreList.add(Score("Kevin", "45"))
//        scoreList.add(Score("Jeff", "63"))

        return temperatureList
    }

    private fun setDataForBarChart() {
//        temperatureList.clear()
        barModelList.clear()
        //you can replace this data object with  your custom object
        for (i in temperatureList.indices) {
            val temperature = temperatureList[i]
            barModelList.add(BarModel(i.toFloat(), temperature.temperatureVal))
        }

        drawBarChart(barModelList)

    }


    // to draw or redraw graph
    private fun drawBarChart(barModelList: ArrayList<BarModel>) {
        val barEntries = ArrayList<BarEntry>()
        val colors = ArrayList<Int>()

        for (barModel in barModelList) {
            barEntries.add(BarEntry(barModel.xValue, barModel.yValue))
            // specific colors
            colors.add(Color.parseColor("#ff9800"))
            /*if (barModel.yValue >= 0)
                colors.add(Color.GREEN)
            else
                colors.add(Color.RED)*/
        }

        val barDataSet = BarDataSet(barEntries, SET_LABEL)
        barDataSet.colors = colors
//        barDataSet.setDrawValues(false)
//        barDataSet.highLightAlpha = 0

        val theData = BarData(barDataSet)
//        theData.setDrawValues(false)
        barChartnew.data = theData
        barChartnew.invalidate()
        barChartnew.description.isEnabled = false
        barChartnew.setDrawValueAboveBar(false)
        barChartnew.setTouchEnabled(true)
        barChartnew.setDrawBarShadow(false)
        barChartnew.isHighlightPerTapEnabled = false
        barChartnew.isDoubleTapToZoomEnabled = false
        barChartnew.isHorizontalScrollBarEnabled = true
        barChartnew.setVisibleXRangeMaximum(8f)
//        barChartnew.moveViewToX(1f)
        barChartnew.data.dataSets[0].valueTextColor =
            ContextCompat.getColor(this@SuryaNamaskar, android.R.color.white)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun configureChartAppearance() {
        barChartnew.description.isEnabled = false
        barChartnew.setDrawValueAboveBar(false)
        barChartnew.setTouchEnabled(true)
        barChartnew.setDrawBarShadow(false)
        barChartnew.isHighlightPerTapEnabled = false
        barChartnew.isDoubleTapToZoomEnabled = false
        barChartnew.isHorizontalScrollBarEnabled = true
        val xAxis = barChartnew.xAxis

//        val mv = CustomMarkerView(this@SuryaNamaskar, R.layout.custom_marker_view_layout)
//        barChartnew.markerView = mv

//        val marker: IMarker = YourMarkerView(this@SuryaNamaskar, R.layout.custom_marker_view_layout)
//        chart!!.marker = marker
//        barChartnew.marker = marker

        barChartnew.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
//                if(surya_namaskarlist!=e){
                Toast.makeText(
                    this@SuryaNamaskar,
                    e!!.x.toString(),
                    Toast.LENGTH_SHORT
                )
                barChartnew.data.dataSets[0].setValueTextColors(getValColors(e.x))
//                }
            }

            override fun onNothingSelected() {
                barChartnew.data.dataSets[0].valueTextColor =
                    ContextCompat.getColor(this@SuryaNamaskar, android.R.color.white)
            }

        })

//        xAxis.valueFormatter = IAxisValueFormatter { value, axis -> xLabel[value.toInt()] }
//        xAxis.valueFormatter = IndexAxisValueFormatter(xLabel)

//        xAxis.valueFormatter = object : IAxisValueFormatter() {
//            fun getFormattedValue(value: Float): String {
//                return DAYS[value.toInt()]
//            }
//        }
        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
//                            xAxis.valueFormatter = MyAxisFormatter()
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = +90f

        val axisLeft: YAxis = barChartnew.axisLeft
        axisLeft.granularity = 10f
        axisLeft.axisMinimum = 0F
        val axisRight: YAxis = barChartnew.axisRight
        axisRight.granularity = 10f
        axisRight.axisMinimum = 0F
    }

    private fun getValColors(xAxis: Float): ArrayList<Int> {
        return arrayListOf<Int>().apply {
            for (i in 0 until (surya_namaskarlist.size)) {
                if (xAxis.toInt() == i) {
                    add(ContextCompat.getColor(this@SuryaNamaskar, android.R.color.white))
                } else {
                    add(ContextCompat.getColor(this@SuryaNamaskar, android.R.color.transparent))
                }
            }
        }
    }

//    private fun getValColors(x: Float): MutableList<Int>? {
////        TODO("Not yet implemented")
//        return getValColors(x)
//    }

//        private fun getValColors(xAxis: Float):ArrayList<Int>{
//            return arrayListOf<Int>().apply {
//                for(i in 0 until(23)){
//                    if(xAxis.toInt()==i){
//                        add(ContextCompat.getColor(requireContext(),android.R.color.white))
//                    }else{
//                        add(ContextCompat.getColor(requireContext(),android.R.color.transparent))
//                    }
//                }
//            }
//        }

    private fun createChartData(ddd: ArrayList<String>): BarData {
//        val barEntries = ArrayList<String>()
        val dateee = ArrayList<String>()
        val list = arrayListOf<Int>()
        val values: ArrayList<BarEntry> = ArrayList()
        Log.e("SIZE", surya_namaskarlist.size.toString())
        for (i in 0 until surya_namaskarlist.size) {

            dateee.add(surya_namaskarlist[i].getcount_date().toString())

            val parts: Array<String> = dateee[i].split("-").toTypedArray()
            println("DD: " + parts[0])
            println("MM: " + parts[1])
            println("YY: " + parts[2])

            ddd.add(parts[2] + "-" + parts[1])
            list.add(parts[2].toInt())

            Log.e("dddinn===>", ddd.toString())
            Log.e("list===>", list.toString())

//            var mindate = get min date (surya_namaskarlist)
//            val maxdate = get max date (surya_namaskarlist)
//            for (mindate <= maxdate){
//                if (mindate == surya_namaskarlist[i].getcount_date()){
//                    val x = calander(day same as mindate)
//                    val y = surya_namaskarlist[i].getcount()!!.toFloat()
//                } else {
//                    val x = calander(day same as mindate)
//                    val y = 0
//                }
//                mindate +=
//            }
//            val x = surya_namaskarlist[i].getcount_date()!!.toFloat()
            val x = list[i].toFloat()
            val y = surya_namaskarlist[i].getcount()!!.toFloat()
//                Util().randomFloatBetween(MIN_Y_VALUE.toFloat(), MAX_Y_VALUE.toFloat())
            values.add(BarEntry(x, y))

            barChartnew.setOnClickListener {
                Toast.makeText(
                    this@SuryaNamaskar,
                    x.toString(),
                    Toast.LENGTH_SHORT
                )
            }


            barChartnew.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
//                fun onValueSelected(e: Map.Entry<*, *>, h: Highlight?) {
//                    val x: Float = e.getX()
//                    val y: Float = e.getY()
//                }

                override fun onValueSelected(e: Entry?, h: Highlight?) {
//                    TODO("Not yet implemented")
                    Toast.makeText(this@SuryaNamaskar, h.toString(), Toast.LENGTH_SHORT).show()
                    Toast.makeText(this@SuryaNamaskar, e.toString(), Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected() {}
            })
        }
        val set1 =
            BarDataSet(values, SET_LABEL)
        set1.setColors(Color.parseColor("#ff9800"))
        val dataSets: ArrayList<IBarDataSet> = ArrayList()
        dataSets.add(set1)
        return BarData(dataSets)
    }

    private fun prepareChartData(data: BarData) {
        val dateee = ArrayList<Float>()
        dateee.add(10f)
        data.setValueTextSize(12f)

        barChartnew.data = data
        barChartnew.animateY(3000)
        barChartnew.isHorizontalScrollBarEnabled = true
        barChartnew.setVisibleXRangeMaximum(8f)
        barChartnew.invalidate()
        barChartnew.data.dataSets[0].valueTextColor =
            ContextCompat.getColor(this@SuryaNamaskar, android.R.color.white)


//        val marker = CustomMarkerView(this@SuryaNamaskar, R.layout.custom_marker_view_layout, dateee)
//        barChartnew.marker = marker


//        val intArray = ddd.map { it.toFloat() }

//        val marker1 = CustomMarkerView(
//            this@SuryaNamaskar, R.layout.custom_marker_view_layout,
//            ddd.toMutableList()
//        )
//        barChartnew.marker = marker1

//        val marker: IMarker = barChartnew.marker
//        for (i in 0 until surya_namaskarlist.size) {
//            barChartnew.setOnClickListener {
//                Toast.makeText(this@SuryaNamaskar, surya_namaskarlist[i].getcount_date(), Toast.LENGTH_SHORT)
//            }
//        }
    }

}
