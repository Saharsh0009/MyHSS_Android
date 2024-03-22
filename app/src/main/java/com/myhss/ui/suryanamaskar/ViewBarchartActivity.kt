package com.myhss.ui.suryanamaskar

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import com.myhss.Utils.CustomProgressDialog
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.DebugLog
import com.myhss.Utils.Functions
import com.myhss.ui.suryanamaskar.Model.BarchartDataModel
import com.myhss.ui.suryanamaskar.Model.DeleteSnCount
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.ui.guru_dakshina.GuruDakshinaRegularDetail
import com.uk.myhss.ui.my_family.Model.Datum_guru_dakshina
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class ViewBarchartActivity : AppCompatActivity(), OnChartValueSelectedListener,
    iEditOrDeleteSNDialog, View.OnClickListener {

    lateinit var barChart: BarChart
    lateinit var barchartData: BarData
    lateinit var barchartDataSet: BarDataSet
    lateinit var barchartEntriesList: ArrayList<BarEntry>
    lateinit var guruDakshinaData: ArrayList<Datum_guru_dakshina>
    lateinit var back_arrow: ImageView
    var colorCode: String = "#ff9800"
    lateinit var header_title: TextView
    private lateinit var add_more: ImageView
    private var isBarClickable = 1
    var chartDigit: Int = 0
    private lateinit var sessionManager: SessionManager
    var screenName: String = "SuryaNamaskar"
    var screenNameID: String = "BarChartSuryaNamaskarVC"
    lateinit var u_listData: ArrayList<BarchartDataModel>
    lateinit var u_case: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_barchart)
        sessionManager = SessionManager(this)
        barChart = findViewById(R.id.barChart_surya)
        back_arrow = findViewById(R.id.back_arrow)
        header_title = findViewById(R.id.header_title)
        add_more = findViewById(R.id.info_tooltip)
        add_more.setImageResource(R.drawable.ic_plus)

        u_case = intent.getStringExtra("case").toString()
        u_listData = intent.getSerializableExtra("list_data") as ArrayList<BarchartDataModel>
        header_title.text = u_listData.get(0).getValue_user()
        back_arrow.setOnClickListener(DebouncedClickListener {
            this.finish()
        })
        when (u_case) {
            "1" -> {
                add_more.visibility = View.VISIBLE
                add_more.setOnClickListener(DebouncedClickListener {
                    val i = Intent(this@ViewBarchartActivity, AddSuryaNamaskarActivity::class.java)
                    startActivity(i)
                    isBarClickable = 1
                })
                colorCode = "#ff9800"
                chartDigit = 0
                screenName = "SuryaNamaskar"
                screenNameID = "BarChartSuryaNamaskarVC"
            }

            "2" -> {
                isBarClickable = 2
                add_more.visibility = View.GONE
                guruDakshinaData =
                    intent.getSerializableExtra("list_guruDakshina") as ArrayList<Datum_guru_dakshina>
                colorCode = "#0080ff"
                chartDigit = 2
                screenName = "GuruDakshina"
                screenNameID = "BarChartGuruDakshinaVC"
            }
        }
        setBarChartDataForSuryanamaskar(u_listData)

        //Fireabse
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId(screenNameID)
        sessionManager.firebaseAnalytics.setUserProperty(screenNameID, screenName)

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)


    }

    private fun setBarChartDataForSuryanamaskar(barchartData: ArrayList<BarchartDataModel>) {
        barchartEntriesList = ArrayList()
        for (i in 0 until barchartData.size) {
            barchartEntriesList.add(
                BarEntry(
                    (i.toFloat()),
                    (barchartData.get(i).getValue_y())!!.toFloat()
                )
            )
            DebugLog.e(
                "BAr Data : " + (i.toFloat()) + "   ||||  Values  " + (barchartData.get(i)
                    .getValue_y())!!.toFloat()
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
        barChart.setOnClickListener(this)
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
        val xAxisLabel = barChart.xAxis.valueFormatter.getFormattedValue(e!!.x, barChart.xAxis)
        val yAxisValue = e?.y

        when (isBarClickable) {
            1 -> { // open SN Edit or Delete Dialog
                for (i in 0 until u_listData.size) {
                    if (xAxisLabel.toString() == convertToDateMonthCode(
                            u_listData[i].getValue_x().toString()
                        ) && (yAxisValue.toString()).toFloat() == u_listData[i].getValue_y()!!
                            .toFloat()
                    ) {
                        barChart.setTouchEnabled(false)
                        openEditOrDeleteSNDialog(u_listData[i])
                        break
                    }
                }
            }

            2 -> { // Open Receipt Screen
                for (i in 0 until guruDakshinaData.size) {
                    if (xAxisLabel.toString() == guruDakshinaData[i].startDate.toString() && (yAxisValue.toString()).toFloat() == guruDakshinaData[i].paidAmount!!.toFloat()) {
                        barChart.setTouchEnabled(false)
                        openguruDakshinaDetails(guruDakshinaData[i])
                        break
                    }
                }
            }
        }
    }


    private fun openEditOrDeleteSNDialog(barchartDataModel: BarchartDataModel) {
        val fragment = supportFragmentManager.findFragmentByTag("EditOrDeleteSNDialog")
        if (fragment == null) {
            val edSNCountDialog = EditOrDeleteSNDialog.newInstance(this, barchartDataModel)
            edSNCountDialog.show(supportFragmentManager, "EditOrDeleteSNDialog")
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

    override fun editSNCount(snID: String, snCount: String) {
        callEditSnCountApi(snID, snCount)
    }

    override fun deleteSNCount(snID: String) {
        val alertDialog: AlertDialog.Builder =
            AlertDialog.Builder(this@ViewBarchartActivity)
        alertDialog.setMessage(getString(R.string.are_you_sure_you_would_like_to_delete_the_surya_namaskar_count))
        alertDialog.setPositiveButton(
            "yes"
        ) { _, _ ->
            callDeleteSNCountApi(snID)
        }
        alertDialog.setNegativeButton(
            "No"
        ) { _, _ ->
            barChart.setTouchEnabled(true)
        }
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }

    override fun closeDialog() {
        barChart.setTouchEnabled(true)
    }

    private fun callEditSnCountApi(snID: String, snCount: String) {
        barChart.setTouchEnabled(false)
        val pd = CustomProgressDialog(this)
        pd.show()
        val call: Call<DeleteSnCount> =
            MyHssApplication.instance!!.api.edit_suryanamasakar_count(
                snID, snCount
            )
        call.enqueue(object : Callback<DeleteSnCount> {
            override fun onResponse(
                call: Call<DeleteSnCount>,
                response: Response<DeleteSnCount>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {
                        val alertDialog: android.app.AlertDialog.Builder =
                            android.app.AlertDialog.Builder(this@ViewBarchartActivity)
                        alertDialog.setTitle("MyHSS")
                        alertDialog.setMessage(response.body()?.message)
                        alertDialog.setCancelable(false)
                        alertDialog.setPositiveButton(
                            "OK"
                        ) { _, _ ->
                            val i = Intent(this@ViewBarchartActivity, SuryaNamaskar::class.java)
                            startActivity(i)
                            finishAffinity()
                        }
                        val alert: android.app.AlertDialog = alertDialog.create()
                        alert.setCanceledOnTouchOutside(false)
                        alert.show()
                    } else {
                        Functions.showAlertMessageWithOK(
                            this@ViewBarchartActivity, "Error",
                            response.body()?.message,
                        )
                        barChart.setTouchEnabled(true)
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@ViewBarchartActivity, "Error",
                        getString(R.string.some_thing_wrong),
                    )
                    barChart.setTouchEnabled(true)
                }
                pd.dismiss()

            }

            override fun onFailure(call: Call<DeleteSnCount>, t: Throwable) {
                Toast.makeText(this@ViewBarchartActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
                barChart.setTouchEnabled(true)
            }
        })
    }


    private fun callDeleteSNCountApi(snID: String) {
        barChart.setTouchEnabled(false)
        val pd = CustomProgressDialog(this)
        pd.show()
        val call: Call<DeleteSnCount> =
            MyHssApplication.instance!!.api.delete_suryanamasakar_count(
                snID
            )
        call.enqueue(object : Callback<DeleteSnCount> {
            override fun onResponse(
                call: Call<DeleteSnCount>,
                response: Response<DeleteSnCount>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {
                        val alertDialog: android.app.AlertDialog.Builder =
                            android.app.AlertDialog.Builder(this@ViewBarchartActivity)
                        alertDialog.setTitle("MyHSS")
                        alertDialog.setMessage(response.body()?.message)
                        alertDialog.setCancelable(false)
                        alertDialog.setPositiveButton(
                            "OK"
                        ) { _, _ ->
                            val i = Intent(this@ViewBarchartActivity, SuryaNamaskar::class.java)
                            startActivity(i)
                            finishAffinity()
                        }
                        val alert: android.app.AlertDialog = alertDialog.create()
                        alert.setCanceledOnTouchOutside(false)
                        alert.show()
                    } else {
                        Functions.showAlertMessageWithOK(
                            this@ViewBarchartActivity, "Error",
                            response.body()?.message,
                        )
                        barChart.setTouchEnabled(true)
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@ViewBarchartActivity, "Error",
                        getString(R.string.some_thing_wrong),
                    )
                    barChart.setTouchEnabled(true)
                }
                pd.dismiss()

            }

            override fun onFailure(call: Call<DeleteSnCount>, t: Throwable) {
                Toast.makeText(this@ViewBarchartActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
                barChart.setTouchEnabled(true)
            }
        })
    }

    override fun onClick(v: View?) {
    }

    override fun onResume() {
        super.onResume()
        if (u_case == "2")
            barChart.setTouchEnabled(true)
    }
}