package com.myhss.ui.Barchat

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.Utils.DebugLog
import com.myhss.Utils.SwipeleftToRightBack
import com.myhss.ui.Barchat.Model.BarchartDataModel
import com.uk.myhss.R
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.ui.guru_dakshina.GuruDakshinaRegularDetail
import com.uk.myhss.ui.my_family.Model.Datum_guru_dakshina
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class ViewBarchartActivity : AppCompatActivity(), OnChartValueSelectedListener {

    lateinit var barChart: BarChart
    lateinit var barchartData: BarData
    lateinit var barchartDataSet: BarDataSet
    lateinit var barchartEntriesList: ArrayList<BarEntry>
    lateinit var guruDakshinaData: ArrayList<Datum_guru_dakshina>
    lateinit var back_arrow: ImageView
    var colorCode: String = "#ff9800"
    lateinit var header_title: TextView
    private lateinit var add_more: ImageView
    private var isBarClickable = false
    var chartDigit: Int = 0
    private lateinit var sessionManager: SessionManager
    var screenName: String = "SuryaNamaskar"
    var screenNameID: String = "BarChartSuryaNamaskarVC"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_barchart)
        sessionManager = SessionManager(this)
        barChart = findViewById(R.id.barChart_surya)
        back_arrow = findViewById(R.id.back_arrow)
        header_title = findViewById(R.id.header_title)
        add_more = findViewById(R.id.info_tooltip)
        add_more.setImageResource(R.drawable.ic_plus)

        val u_case = intent.getStringExtra("case")
        val u_listData = intent.getSerializableExtra("list_data") as ArrayList<BarchartDataModel>
        header_title.text = u_listData.get(0).getValue_user()
        back_arrow.setOnClickListener {
            this.finish()
        }
        when (u_case) {
            "1" -> {
                add_more.visibility = View.VISIBLE
                add_more.setOnClickListener {
                    val i = Intent(this@ViewBarchartActivity, AddSuryaNamaskarActivity::class.java)
                    startActivity(i)
                    isBarClickable = false
                }
                colorCode = "#ff9800"
                chartDigit = 0
                screenName = "SuryaNamaskar"
                screenNameID = "BarChartSuryaNamaskarVC"
            }

            "2" -> {
                isBarClickable = true
                add_more.visibility = View.GONE
                guruDakshinaData =
                    intent.getSerializableExtra("list_guruDakshina") as ArrayList<Datum_guru_dakshina>

//                DebugLog.e("guruDakshinaData => " + guruDakshinaData.size)
                colorCode = "#0080ff"
                chartDigit = 2
                screenName = "GuruDakshina"
                screenNameID = "BarChartGuruDakshinaVC"
            }
        }
        setBarChartDataForSuryanamaskar(u_listData)
//        SwipeleftToRightBack.enableSwipeBack(this)
//        SwipeleftToRightBack.enableSwipeBackFullView(this)

        //Fireabse
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId(screenNameID)
        sessionManager.firebaseAnalytics.setUserProperty(screenNameID, screenName)

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)

    }
//    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
//        return SwipeleftToRightBack.dispatchTouchEvent(this, event) || super.dispatchTouchEvent(event)
//    }

    private fun setBarChartDataForSuryanamaskar(barchartData: ArrayList<BarchartDataModel>) {
        barchartEntriesList = ArrayList()
        for (i in 0 until barchartData.size) {
            barchartEntriesList.add(
                BarEntry(
                    (i.toFloat()), (barchartData.get(i).getValue_y())!!.toFloat()
                )
            )
        }
        setupBarChartSurya(barchartData)
    }

    private fun setupBarChartSurya(barchartDataList: ArrayList<BarchartDataModel>) {
        barchartDataSet = BarDataSet(barchartEntriesList, getString(R.string.surya_namaskar))
        barchartDataSet.valueFormatter = DefaultValueFormatter(chartDigit)
        barchartDataSet.valueTextColor = Color.BLACK
        barchartDataSet.setColor(Color.parseColor(colorCode))
        barchartDataSet.valueTextSize = 10f
        barchartData = BarData(barchartDataSet)
        barchartData.barWidth = 0.4f
        barChart.data = barchartData
        barChart.setOnChartValueSelectedListener(this)
        barChart.isHorizontalScrollBarEnabled = true
        barChart.animateXY(1000, 1000)
        barChart.isDoubleTapToZoomEnabled = true
        barChart.setScaleEnabled(true)
        barChart.setTouchEnabled(true)
        barChart.setPinchZoom(true)
        barChart.description = null
        barChart.legend.isEnabled = false
        barChart.setDrawBarShadow(false)
        barChart.setVisibleXRangeMaximum(10f)
        barChart.axisLeft.axisMinimum = 0f
        barChart.axisRight.axisMinimum = 0f
        setXAxisDataForBarChart(barchartDataList)
        barChart.invalidate()
    }


    private fun setXAxisDataForBarChart(barchartDataist: ArrayList<BarchartDataModel>) {
        val xAxisLabel: ArrayList<String> = ArrayList()
        for (i in 0 until barchartDataist.size) {
            xAxisLabel.add(convertToDateMonthCode(barchartDataist.get(i).getValue_x().toString()))
//            xAxisLabel.add(barchartDataist.get(i).getValue_x().toString())
        }
        val xAxis: XAxis = barChart.getXAxis()
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 10f
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)
        xAxis.setCenterAxisLabels(false)
        xAxis.labelRotationAngle = -16f
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabel)
        xAxis.setGranularity(1f)
        xAxis.granularity
        xAxis.spaceMax = 0.4f
        val yAxisRight: YAxis = barChart.getAxisRight()
        yAxisRight.isEnabled = false
        val yAxisLeft: YAxis = barChart.axisLeft
        yAxisLeft.isDrawBottomYLabelEntryEnabled
    }


    fun convertToDateMonthCode(dateString: String): String {
        val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter.ofPattern("dd-MMM", Locale.ENGLISH)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        try {
            val date = LocalDate.parse(dateString)
            return date.format(formatter)
        } catch (e: Exception) {
            return dateString
        }
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        if (isBarClickable) {
            val xAxisLabel = barChart.xAxis.valueFormatter.getFormattedValue(e!!.x, barChart.xAxis)
            val yAxisValue = e?.y
            for (i in 0 until guruDakshinaData.size) {
//                DebugLog.e("Y Axis : $yAxisValue And Value from BAr chart : ${guruDakshinaData[i].paidAmount!!}")
//                DebugLog.e("X Axis : $xAxisLabel And Value from BAr chart : ${guruDakshinaData[i].startDate!!}")
                if (xAxisLabel.toString() == guruDakshinaData[i].startDate.toString() && (yAxisValue.toString()).toFloat() == guruDakshinaData[i].paidAmount!!.toFloat()) {
//                    DebugLog.e("click button ${guruDakshinaData[i].paidAmount}")
                    openguruDakshinaDetails(guruDakshinaData[i])
                    break
                }
            }
        }
    }

    private fun openguruDakshinaDetails(datumGuruDakshina: Datum_guru_dakshina) {
        val i = Intent(this@ViewBarchartActivity, GuruDakshinaRegularDetail::class.java)
        i.putExtra("username_name", datumGuruDakshina.firstName + datumGuruDakshina.lastName)
        i.putExtra("user_shakha_type", datumGuruDakshina.chapterName)
        i.putExtra("amount_txt", datumGuruDakshina.paidAmount)
        i.putExtra("regular_onetime_type", datumGuruDakshina.dakshina)
        i.putExtra("regular_onetime_id", datumGuruDakshina.orderId)
        i.putExtra("date_txt", datumGuruDakshina.startDate)
        i.putExtra("status", datumGuruDakshina.status)
        i.putExtra("txnId", datumGuruDakshina.txnId)
        i.putExtra("giftAid", datumGuruDakshina.giftAid)
        i.putExtra("order_id", datumGuruDakshina.orderId)
        i.putExtra("dakshina", datumGuruDakshina.dakshina)
        i.putExtra("recurring", datumGuruDakshina.recurring)
        startActivity(i)
    }

    override fun onNothingSelected() {

    }


}