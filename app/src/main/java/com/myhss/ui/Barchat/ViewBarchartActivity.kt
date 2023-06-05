package com.myhss.ui.Barchat

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.myhss.Utils.DebugLog
import com.myhss.ui.Barchat.Model.Datum_Get_SuryaNamaskar
import com.uk.myhss.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class ViewBarchartActivity : AppCompatActivity() {

    lateinit var barChart_surya: BarChart
    lateinit var barData_surya: BarData
    lateinit var barDataSet_surya: BarDataSet
    lateinit var barEntriesList_surya: ArrayList<BarEntry>
    lateinit var back_arrow: ImageView
    lateinit var header_title: TextView
    private lateinit var add_more: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_barchart)
        barChart_surya = findViewById(R.id.barChart_surya)
        back_arrow = findViewById(R.id.back_arrow)
        header_title = findViewById(R.id.header_title)
        add_more = findViewById(R.id.info_tooltip)
        add_more.setImageResource(R.drawable.ic_plus)
        val u_name = intent.getStringExtra("name")
        val u_listData =
            intent.getSerializableExtra("list_data") as ArrayList<Datum_Get_SuryaNamaskar>
        DebugLog.e("User Count =>>>  " + u_listData.size)
        DebugLog.e("NAme  =>>>  " + u_name)
        setBarChartDataForSuryanamaskar(u_listData)
        header_title.text = u_name
        back_arrow.setOnClickListener {
            this.finish()
        }
        add_more.visibility = View.VISIBLE
        add_more.setOnClickListener {
            val i = Intent(this@ViewBarchartActivity, AddSuryaNamaskarActivity::class.java)
            startActivity(i)
        }

    }

    private fun setBarChartDataForSuryanamaskar(suryaNamaskarlistData: ArrayList<Datum_Get_SuryaNamaskar>) {
        barEntriesList_surya = ArrayList()
        for (i in 0 until suryaNamaskarlistData.size) {
            barEntriesList_surya.add(
                BarEntry(
                    (i.toFloat()), (suryaNamaskarlistData.get(i).getcount())!!.toFloat()
                )
            )
        }
        setupBarChartSurya(suryaNamaskarlistData)
    }

    private fun setupBarChartSurya(suryaNamaskarlistData: ArrayList<Datum_Get_SuryaNamaskar>) {
        barDataSet_surya = BarDataSet(barEntriesList_surya, getString(R.string.surya_namaskar))
        barDataSet_surya.valueFormatter = DefaultValueFormatter(0)
        barDataSet_surya.valueTextColor = Color.BLACK
        barDataSet_surya.setColor(Color.parseColor("#ff9800"))
        barDataSet_surya.valueTextSize = 10f
        barData_surya = BarData(barDataSet_surya)
        barChart_surya.data = barData_surya
        barChart_surya.isHorizontalScrollBarEnabled = true
        barChart_surya.animateXY(1000, 1000)
        barChart_surya.isDoubleTapToZoomEnabled = true
        barChart_surya.setScaleEnabled(true)
        barChart_surya.setTouchEnabled(true)
        barChart_surya.setPinchZoom(true)
        barChart_surya.description = null
        barChart_surya.legend.isEnabled = false
        barChart_surya.setDrawBarShadow(false)
        barChart_surya.setVisibleXRangeMaximum(10f)
        barChart_surya.axisLeft.axisMinimum = 0f
        barChart_surya.axisRight.axisMinimum = 0f
        setXAxisDataForBarChart(suryaNamaskarlistData)
        barChart_surya.invalidate()
    }


    private fun setXAxisDataForBarChart(suryaNamaskarlistData: ArrayList<Datum_Get_SuryaNamaskar>) {
        val xAxisLabel: ArrayList<String> = ArrayList()
        for (i in 0 until suryaNamaskarlistData.size) {
            xAxisLabel.add(
                convertToDateMonthCode(
                    suryaNamaskarlistData.get(i).getcount_date().toString()
                )
            )
        }
        val xAxis: XAxis = barChart_surya.getXAxis()
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
        val yAxisRight: YAxis = barChart_surya.getAxisRight()
        yAxisRight.isEnabled = false
        val yAxisLeft: YAxis = barChart_surya.axisLeft
        yAxisLeft.isDrawBottomYLabelEntryEnabled
    }


    fun convertToDateMonthCode(dateString: String): String {
        val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter.ofPattern("dd-MMM", Locale.ENGLISH)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val date = LocalDate.parse(dateString)
        return date.format(formatter)
    }
}