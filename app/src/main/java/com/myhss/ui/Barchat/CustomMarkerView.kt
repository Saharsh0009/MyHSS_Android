//package com.myhss.ui.Barchat
//
//import android.content.Context
//import android.widget.TextView
//import com.github.mikephil.charting.components.MarkerView
//import com.github.mikephil.charting.data.Entry
//import com.github.mikephil.charting.highlight.Highlight
//import com.github.mikephil.charting.utils.MPPointF
//import com.uk.myhss.R
//
//class CustomMarkerView(
//    context: Context,
//    layout: Int,
//    private val dataToDisplay: MutableList<String>
//) : MarkerView(context, layout) {
//
//    private var txtViewData: TextView? = null
//
//    init {
//        txtViewData = findViewById(R.id.tvContent)
//    }
//
//    override fun refreshContent(e: Entry?, highlight: Highlight?) {
//        try {
//            val xAxis = e?.x?.toInt() ?: 0
//            txtViewData?.text = dataToDisplay[xAxis].toString()
//        } catch (e: IndexOutOfBoundsException) { }
//
//        super.refreshContent(e, highlight)
//    }
//
//    override fun getOffset(): MPPointF {
//        return MPPointF(-(width / 2f), -height.toFloat())
//    }
//}