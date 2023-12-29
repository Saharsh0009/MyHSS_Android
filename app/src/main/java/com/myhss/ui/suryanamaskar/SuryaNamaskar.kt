package com.myhss.ui.suryanamaskar

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
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
import com.myhss.Utils.CustomProgressDialog
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.DebugLog
import com.myhss.Utils.Functions
import com.myhss.Utils.UtilCommon
import com.myhss.ui.suryanamaskar.Model.BarchartDataModel
import com.myhss.ui.suryanamaskar.Model.Datum_Get_SuryaNamaskar
import com.uk.myhss.Main.HomeActivity
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.ui.linked_family.Model.Get_Member_Listing_Datum
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class SuryaNamaskar : AppCompatActivity(), OnChartValueSelectedListener {

    private lateinit var sessionManager: SessionManager
    lateinit var back_arrow: ImageView
    lateinit var header_title: TextView
    lateinit var layout_pieChart_lable: LinearLayout
    var IDMEMBER: ArrayList<String> = ArrayList<String>()
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
    private lateinit var imgFrmDate: ImageView
    private lateinit var imgToDate: ImageView
    private lateinit var from_date: TextView
    private lateinit var to_date: TextView
    lateinit var pieChart_surya: PieChart
    private var memberDataList: List<Get_Member_Listing_Datum> =
        ArrayList<Get_Member_Listing_Datum>()
    private var surya_namaskarlist: List<Datum_Get_SuryaNamaskar> =
        ArrayList<Datum_Get_SuryaNamaskar>()

    private var fromDate: Date? = null
    private var toDate: Date? = null
    private var MEMBER_ID_temp = ""

    private lateinit var chipGroup: ChipGroup


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
        imgFrmDate = findViewById(R.id.imgFrmDate)
        imgToDate = findViewById(R.id.imgToDate)
        from_date = findViewById(R.id.from_date)
        to_date = findViewById(R.id.to_date)
        data_not_found_layout = findViewById(R.id.data_not_found_layout)
        layout_pieChart_lable = findViewById(R.id.layout_pieChart_lable)
        add_more.setImageResource(R.drawable.ic_plus)
        pieChart_surya.visibility = View.GONE

        toDate = Calendar.getInstance().time
        setToDate()
        val pastDateCalendar = Calendar.getInstance()
        pastDateCalendar.time = toDate
        pastDateCalendar.add(Calendar.MONTH, -3)
        fromDate = pastDateCalendar.time
        setFromDate()

        callApis()

        back_arrow.setOnClickListener(DebouncedClickListener {
            val i = Intent(this@SuryaNamaskar, HomeActivity::class.java)
            startActivity(i)
            finishAffinity()
        })
        add_more.visibility = View.VISIBLE
        add_more.setOnClickListener(DebouncedClickListener {
            val i = Intent(this@SuryaNamaskar, AddSuryaNamaskarActivity::class.java)
            startActivity(i)
        })

        from_date.setOnClickListener(DebouncedClickListener {
            showFromDateRangePicker()
        })

        to_date.setOnClickListener(DebouncedClickListener {
            showToDateRangePicker()
        })
        imgFrmDate.setOnClickListener(DebouncedClickListener {
            showFromDateRangePicker()
        })

        imgToDate.setOnClickListener(DebouncedClickListener {
            showToDateRangePicker()
        })
    }

    private fun showFromDateRangePicker() {

        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)


        val defaultDate =
            SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(from_date.text.toString())

        val fromDatePickerDialog = DatePickerDialog(
            this, { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)

                // Validate that the selected date is up to yesterday
                if (selectedDate.timeInMillis <= getYesterday().time) {
                    fromDate = selectedDate.time
                    setFromDate()
                    if (isDateRangeValid(from_date.text.toString(), to_date.text.toString())) {
                        lifecycleScope.launch {
                            val job2 = async {
                                SuryanamaskarList(
                                    MEMBER_ID_temp,
                                    from_date.text.toString(),
                                    to_date.text.toString(),
                                    "true",
                                    true
                                )
                            }
                            val result2 = job2.await()
                        }
                    }
                } else {
                    // Show an error message or handle the invalid selection
                    showToast("Please select a date up to yesterday.")
                }
            },
            currentYear,
            currentMonth,
            currentDay
        )

        // Set the "from" date as a past day
//        val pastDateCalendar = Calendar.getInstance()
//        pastDateCalendar.add(Calendar.YEAR, -1) // Set one day ago
//        fromDatePickerDialog.datePicker.minDate = pastDateCalendar.timeInMillis
        fromDatePickerDialog.datePicker.maxDate =
            System.currentTimeMillis() - 86400000 // Set max date to (current date - 1 day)

        defaultDate?.let {
            fromDatePickerDialog.datePicker.updateDate(
                it.year + 1900,
                it.month,
                it.date
            )
        }

        fromDatePickerDialog.show()


    }

    private fun showToDateRangePicker() {
        if (fromDate == null) {
            showToast("Please select 'From Date' first.")
            return
        }
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val toDateMinDate = fromDate?.time ?: System.currentTimeMillis()

        val defaultDate =
            SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(to_date.text.toString())

        val toDatePickerDialog = DatePickerDialog(
            this, { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                if (selectedDate.timeInMillis <= System.currentTimeMillis()) {
                    toDate = selectedDate.time
                    setToDate()
                    if (isDateRangeValid(from_date.text.toString(), to_date.text.toString())) {
                        lifecycleScope.launch {
                            val job2 = async {
                                SuryanamaskarList(
                                    MEMBER_ID_temp,
                                    from_date.text.toString(),
                                    to_date.text.toString(),
                                    "true",
                                    true
                                )
                            }
                            val result2 = job2.await()
                        }
                    }
                } else {
                    showToast("Please select a date up to today.")
                }
            },
            currentYear,
            currentMonth,
            currentDay
        )
        toDatePickerDialog.datePicker.minDate = toDateMinDate
        toDatePickerDialog.datePicker.maxDate = System.currentTimeMillis()

        defaultDate?.let {
            toDatePickerDialog.datePicker.updateDate(
                it.year + 1900,
                it.month,
                it.date
            )
        }

        toDatePickerDialog.show()
    }

    private fun isDateRangeValid(sfromDate: String, stoDate: String): Boolean {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.UK)
        try {
            val fromDateParsed: Date? = dateFormat.parse(sfromDate)
            val toDateParsed: Date? = dateFormat.parse(stoDate)
            if (fromDateParsed!!.before(toDateParsed) || fromDateParsed == toDateParsed) {
                return true
            } else {
                showToast("Invalid date range. 'From Date' must be less than to 'To Date'.")
                return false
            }
        } catch (e: Exception) {
            showToast("Please Select Proper From and To Date")
            return false
        }
    }

    private fun setToDate() {
        to_date.text = UtilCommon.convertDateFormatUK(toDate)
    }

    private fun setFromDate() {
        from_date.text = UtilCommon.convertDateFormatUK(fromDate)
    }

    private fun getYesterday(): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        return calendar.time
    }

    private fun showToast(message: String) {
        Toast.makeText(this@SuryaNamaskar, message, Toast.LENGTH_LONG).show()
    }

    private fun callApis() {
        val pd = CustomProgressDialog(this@SuryaNamaskar)
        pd.show()
        if (Functions.isConnectingToInternet(this@SuryaNamaskar)) {
            lifecycleScope.launch {
                val job2 = async {
                    USERID = sessionManager.fetchUserID()!!
                    TAB = "family"
                    MEMBERID = sessionManager.fetchMEMBERID()!!
                    STATUS = "all"// all
                    LENGTH = "100"
                    START = "0"
                    SEARCH = ""
                    CHAPTERID = ""
                    myMemberList(USERID, TAB, MEMBERID, STATUS, LENGTH, START, SEARCH, CHAPTERID)
                }
                val result2 = job2.await()
                DebugLog.d("CORO - Results await 5: $result2")
                pd.dismiss()
            }
        } else {
            Toast.makeText(
                this@SuryaNamaskar,
                resources.getString(R.string.no_connection),
                Toast.LENGTH_SHORT
            ).show()
            pd.dismiss()
        }
    }

    private suspend fun myMemberList(
        user_id: String,
        tab: String,
        member_id: String,
        status: String,
        length: String,
        start: String,
        search: String,
        chapter_id: String
    ) {
        val mStringListnew = mutableListOf(sessionManager.fetchMEMBERID() ?: "")
        try {
            val response = MyHssApplication.instance!!.api.getMemberListing(
                user_id,
                tab,
                member_id,
                status,
                length,
                start,
                search,
                chapter_id
            )
            if (response.status == true) {
                memberDataList = response.data!!
                for (member in memberDataList) {
                    if (member.memberId != sessionManager.fetchMEMBERID()) {
                        mStringListnew.add(member.memberId.toString())
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (Functions.isConnectingToInternet(this@SuryaNamaskar)) {
                val listnew = mStringListnew.map { it.toString() }
                if (listnew.size > 1) {
                    val UserCategory = listnew.toSet().toList() as ArrayList<String>
                    MEMBER_ID_temp = UserCategory.joinToString(",")
                } else {
                    MEMBER_ID_temp = listnew.firstOrNull() ?: ""
                }
                SuryanamaskarList(
                    MEMBER_ID_temp,
                    from_date.text.toString(),
                    to_date.text.toString(),
                    "true",
                    false
                )
            } else {
                Toast.makeText(
                    this@SuryaNamaskar,
                    resources.getString(R.string.no_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private suspend fun SuryanamaskarList(
        member_id: String,
        from_date: String,
        to_date: String,
        is_api: String,
        isProgressBar: Boolean
    ) {

        var pd = CustomProgressDialog(this@SuryaNamaskar)
        isProgressBar?.let { if (it) pd.show() }

        DebugLog.d("member_id => $member_id")
        try {
            val response =
                MyHssApplication.instance!!.api.get_suryanamaskar_count(
                    member_id,
                    UtilCommon.convertDateFormatUS(from_date)!!,
                    UtilCommon.convertDateFormatUS(to_date)!!,
                    is_api
                )
            if (response.status == true) {

                data_not_found_layout.visibility = View.GONE
                surya_namaskarlist = response.data!!
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
                layout_pieChart_lable.visibility = View.VISIBLE
                setPieChartForSuryanamaskar(surya_namaskarlist)
            } else {
                Functions.displayMessage(this@SuryaNamaskar, response.message)
                data_not_found_layout.visibility = View.VISIBLE
                data_not_found_layout.setBackgroundColor(resources.getColor(R.color.surya_namaskar_bk))
                pieChart_surya.visibility = View.GONE
                layout_pieChart_lable.visibility = View.GONE
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Functions.showAlertMessageWithOK(
                this@SuryaNamaskar,
                "Message",
                getString(R.string.some_thing_wrong)
            )
            data_not_found_layout.visibility = View.VISIBLE
            data_not_found_layout.setBackgroundColor(resources.getColor(R.color.surya_namaskar_bk))
            pieChart_surya.visibility = View.GONE
            layout_pieChart_lable.visibility = View.GONE
        } finally {
            isProgressBar?.let { if (it) pd.dismiss() }
        }
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
        chipGroup = ChipGroup(this)
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

        layout_pieChart_lable.removeAllViews()
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
        chip.setOnClickListener(DebouncedClickListener {
            openBarChart(label.toString())
        })
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
                barchartDataModel.setValue_ID(
                    surya_namaskarlist.get(i).getsurya_namaskar_count_id()
                )
                listData_surya.add(barchartDataModel)
            }
        }
        val intent = Intent(this@SuryaNamaskar, ViewBarchartActivity::class.java)
        intent.putExtra("case", "1")
        intent.putExtra("list_data", listData_surya)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val i = Intent(this@SuryaNamaskar, HomeActivity::class.java)
        startActivity(i)
        finishAffinity()
    }
}