package com.uk.myhss.ui.policies

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.uk.myhss.Guru_Dakshina_OneTime.GuruDakshinaOneTimeFirstActivity
import com.uk.myhss.Guru_Dakshina_Regular.GuruDakshinaRegularFirstActivity
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.Functions
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.ui.my_family.Adapter.GuruCustomAdapter
import com.uk.myhss.ui.my_family.Model.Datum_guru_dakshina
import com.uk.myhss.ui.my_family.Model.guru_dakshina_response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList
import android.widget.RelativeLayout
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.DebugLog
import com.myhss.Utils.UtilCommon


class GuruDakshinaFragment : Fragment() {
    private lateinit var sessionManager: SessionManager
    lateinit var one_time_layout: LinearLayout
    lateinit var regular_layout: LinearLayout
    lateinit var guru_dakshina_layout: RelativeLayout
    lateinit var USERID: String
    lateinit var mLayoutManager: LinearLayoutManager
    private var guruDakshinaDataList: List<Datum_guru_dakshina> = ArrayList<Datum_guru_dakshina>()
    lateinit var pieChart_guru: PieChart
    var end: Int = 100
    var start: Int = 0

    @SuppressLint("WrongConstant", "NewApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_guru_dakshina, container, false)

        sessionManager = SessionManager(requireContext())

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
        sessionManager.firebaseAnalytics.setUserId("GuruDakshinaaVC")
        sessionManager.firebaseAnalytics.setUserProperty("GuruDakshinaaVC", "GuruDakshinaFragment")

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        one_time_layout = root.findViewById(R.id.one_time_layout)
        regular_layout = root.findViewById(R.id.regular_layout)
        guru_dakshina_layout = root.findViewById(R.id.guru_dakshina_layout)
        pieChart_guru = root.findViewById(R.id.pieChart_guru)

        if (UtilCommon.isUserUnder18(sessionManager.fetchDOB().toString())) {
            one_time_layout.visibility = View.GONE
            regular_layout.visibility = View.GONE
        } else {
            one_time_layout.visibility = View.VISIBLE
            regular_layout.visibility = View.VISIBLE
        }

        CallAPI(start)
        one_time_layout.setOnClickListener(DebouncedClickListener {
            startActivity(Intent(context, GuruDakshinaOneTimeFirstActivity::class.java))
        })

        regular_layout.setOnClickListener(DebouncedClickListener {
            startActivity(Intent(context, GuruDakshinaRegularFirstActivity::class.java))
        })
        return root
    }

    private fun CallAPI(start: Int) {
        if (Functions.isConnectingToInternet(context)) {
            USERID = sessionManager.fetchUserID()!!
            Log.d("USERID", USERID)
            val LENGTH: String = end.toString()
            val START: String = start.toString()
            val SEARCH: String = ""
            val CHAPTER_ID: String = sessionManager.fetchSHAKHAID()!!   // "227"
            myGuruDakshina(USERID, LENGTH, START, SEARCH, CHAPTER_ID)
        } else {
            Toast.makeText(
                context,
                resources.getString(R.string.no_connection),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun myGuruDakshina(
        user_id: String,
        length: String,
        start: String,
        search: String,
        chapter_id: String
    ) {
        val pd = CustomProgressBar(context)
        pd.show()
        val call: Call<guru_dakshina_response> = MyHssApplication.instance!!.api.get_guru_dakshina(
            user_id,
            length,
            start,
            search,
            chapter_id
        )
        call.enqueue(object : Callback<guru_dakshina_response> {
            override fun onResponse(
                call: Call<guru_dakshina_response>,
                response: Response<guru_dakshina_response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {

                        try {
                            guruDakshinaDataList = response.body()!!.data!!
                            pieChart_guru.visibility = View.VISIBLE
                            setPieChartForGuruDakshina(guruDakshinaDataList)

                        } catch (e: ArithmeticException) {
                            println(e)
                        } finally {
                            println("Family")
                        }
                    } else {
                        Functions.displayMessage(context, response.body()?.message)
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        requireContext(), "Message",
                        getString(R.string.some_thing_wrong),
                    )
                    pieChart_guru.visibility = View.GONE
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<guru_dakshina_response>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                pieChart_guru.visibility = View.GONE
                pd.dismiss()
            }
        })
    }

    private fun setPieChartForGuruDakshina(guruDakshinaDataList: List<Datum_guru_dakshina>) {
//        pieChart_guru.setOnChartValueSelectedListener(this.context)
        pieChart_guru.setUsePercentValues(false)
        pieChart_guru.getDescription().setEnabled(false)
        pieChart_guru.setExtraOffsets(5f, 10f, 5f, 5f)
        pieChart_guru.setDragDecelerationFrictionCoef(0.95f)
        pieChart_guru.setDrawHoleEnabled(true)
        pieChart_guru.setHoleColor(Color.WHITE)
        pieChart_guru.setTransparentCircleColor(Color.WHITE)
        pieChart_guru.setTransparentCircleAlpha(110)
        pieChart_guru.setHoleRadius(50f)
        pieChart_guru.setTransparentCircleRadius(53f)
        pieChart_guru.setDrawCenterText(true)
        pieChart_guru.setRotationAngle(0f)
        pieChart_guru.setRotationEnabled(true)
        pieChart_guru.setHighlightPerTapEnabled(true)
        pieChart_guru.animateY(2000, Easing.EaseInOutQuad)
        pieChart_guru.legend.isEnabled = true
        pieChart_guru.legend.textSize = 16f
        pieChart_guru.legend.isWordWrapEnabled = true
        pieChart_guru.legend.setWordWrapEnabled(true)
        pieChart_guru.setEntryLabelColor(Color.BLACK)
        pieChart_guru.setEntryLabelTextSize(12f)
        val IDMEMBER: ArrayList<String> = ArrayList()
        for (k in 0 until guruDakshinaDataList.size) {
            IDMEMBER.add(guruDakshinaDataList.get(k).memberId!!)
        }
        DebugLog.e("Size of IDMEMBER : " + IDMEMBER.size)
        val member_list = IDMEMBER.toSet()
        val pieEntries: ArrayList<PieEntry> = ArrayList()
        var t_count = 0
        for (i in 0 until member_list.size) {
            var s_count = 0
            var s_name = ""
            for (j in 0 until guruDakshinaDataList.size) {
                if (member_list.elementAt(i) == guruDakshinaDataList.get(j).memberId) {
                    s_count = s_count + guruDakshinaDataList.get(j).paidAmount!!.toInt()
                    s_name = guruDakshinaDataList.get(j).firstName.toString()
                }
            }
            t_count = t_count + s_count
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
        pieChart_guru.setData(data)
        pieChart_guru.setDrawEntryLabels(false)
        pieChart_guru.centerText = "Total\n" + t_count
        pieChart_guru.setCenterTextColor(Color.parseColor("#ff9800"))
        pieChart_guru.setCenterTextSize(30f)
        pieChart_guru.invalidate()
    }
}