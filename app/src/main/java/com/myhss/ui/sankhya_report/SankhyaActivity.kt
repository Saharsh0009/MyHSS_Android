package com.uk.myhss.ui.policies

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.cardview.widget.CardView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.Functions
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.ui.linked_family.Model.Get_Member_Listing_Datum
import com.uk.myhss.ui.my_family.Adapter.CustomAdapter
import com.uk.myhss.ui.my_family.Model.my_family_response
import com.uk.myhss.ui.sankhya_report.Adapter.SankhyaCustomAdapter
import com.uk.myhss.ui.sankhya_report.AddSankhyaActivity
import com.uk.myhss.ui.sankhya_report.Model.Sankhya_Datum
import com.uk.myhss.ui.sankhya_report.Model.Sankhya_List_Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SankhyaActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    lateinit var my_family_list: RecyclerView
    lateinit var view_view: CardView

    lateinit var data_not_found_layout: RelativeLayout
    lateinit var add_family_layout: LinearLayout
    lateinit var registration_success_btn: TextView
    lateinit var rootview: LinearLayout
    lateinit var search_fields: AppCompatEditText

    lateinit var USERID: String
    lateinit var MEMBERID: String
    lateinit var LENGTH: String
    lateinit var START: String
    lateinit var SEARCH: String
    lateinit var START_DATE: String
    lateinit var END_DATE: String
    private var TODAY_DATE: String = ""

    val sdf: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    var actualDate: Calendar = Calendar.getInstance()
    private var atheletsBeans: List<Sankhya_Datum> = ArrayList<Sankhya_Datum>()
    private var mAdapterGuru: SankhyaCustomAdapter? = null
    lateinit var mLayoutManager: LinearLayoutManager

    @SuppressLint("WrongConstant", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_sankhya_report)

        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("SankhyaPreviewVC")
        sessionManager.firebaseAnalytics.setUserProperty("SankhyaPreviewVC", "SankhyaActivity")

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        val back_arrow = findViewById<ImageView>(R.id.back_arrow)
        val header_title = findViewById<TextView>(R.id.header_title)

        header_title.text = getString(R.string.sankhya)

        back_arrow.setOnClickListener(DebouncedClickListener {
            finish()
        })

        actualDate = Calendar.getInstance()
        TODAY_DATE = sdf.format(actualDate.time)
        data_not_found_layout = findViewById(R.id.data_not_found_layout)
        view_view = findViewById(R.id.view_view)
        add_family_layout = findViewById(R.id.add_family_layout)
        rootview = findViewById(R.id.rootview)
        search_fields = findViewById(R.id.search_fields)
        registration_success_btn = findViewById(R.id.registration_success_btn)

        view_view.visibility = View.VISIBLE

        registration_success_btn.text = getString(R.string.add_sankhya)

        my_family_list = findViewById(R.id.my_family_list)
        mLayoutManager = LinearLayoutManager(this@SankhyaActivity)
        my_family_list.layoutManager = mLayoutManager

        val end: Int = 100
        val start: Int = 0

        if (Functions.isConnectingToInternet(this@SankhyaActivity)) {
            USERID = sessionManager.fetchUserID()!!
            USERID = sessionManager.fetchUserID()!!
            MEMBERID = sessionManager.fetchSHAKHAID()!!
            LENGTH = end.toString()
            START = start.toString()
            SEARCH = ""
            START_DATE = ""  //TODAY_DATE
            END_DATE = ""  //TODAY_DATE

            mySankhyaList(USERID, MEMBERID, LENGTH, START, SEARCH, START_DATE, END_DATE)
        } else {
            Toast.makeText(
                this@SankhyaActivity,
                resources.getString(R.string.no_connection),
                Toast.LENGTH_SHORT
            ).show()
        }

//        search_fields.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                val end: Int = 100
//                val start: Int = 0
//
//                if (Functions.isConnectingToInternet(this@SankhyaActivity)) {
//                    USERID = sessionManager.fetchUserID()!!
//                    USERID = sessionManager.fetchUserID()!!
//                    MEMBERID = sessionManager.fetchSHAKHAID()!!
//                    LENGTH = end.toString()
//                    START = start.toString()
//                    SEARCH = s.toString()
//                    START_DATE = ""  //TODAY_DATE
//                    END_DATE = ""  //TODAY_DATE
//
//                    mySearchSankhyaList(
//                        USERID,
//                        MEMBERID,
//                        LENGTH,
//                        START,
//                        SEARCH,
//                        START_DATE,
//                        END_DATE
//                    )
//                } else {
//                    Toast.makeText(
//                        this@SankhyaActivity,
//                        resources.getString(R.string.no_connection),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//            }
//        })

        search_fields.doOnTextChanged { text, start, before, count ->
            if (text?.toString()?.length!! > 0) {
                val filteredList = atheletsBeans.filter { item ->
                    item.chapterName!!.contains(text!!, ignoreCase = true)
                }
                updateAdapterData(filteredList)
            } else {
                updateAdapterData(atheletsBeans)
            }
        }

        add_family_layout.setOnClickListener(DebouncedClickListener {
            startActivity(Intent(this@SankhyaActivity, AddSankhyaActivity::class.java))
        })
    }

    private fun updateAdapterData(updateData: List<Sankhya_Datum>) {
        try {
            mAdapterGuru = SankhyaCustomAdapter(updateData)
            my_family_list.adapter = mAdapterGuru
            mAdapterGuru!!.notifyDataSetChanged()
        } catch (e: ArithmeticException) {
            println(e)
        }
    }

    private fun mySankhyaList(
        user_id: String, chapter_id: String, length: String, start: String, search: String,
        start_date: String, end_date: String
    ) {
        val pd = CustomProgressBar(this@SankhyaActivity)
        pd.show()
        val call: Call<Sankhya_List_Response> = MyHssApplication.instance!!.api.get_sankhya_listing(
            user_id, chapter_id, length,
            start, search, start_date, end_date
        )
        call.enqueue(object : Callback<Sankhya_List_Response> {
            override fun onResponse(
                call: Call<Sankhya_List_Response>,
                response: Response<Sankhya_List_Response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {
                        data_not_found_layout.visibility = View.GONE
                        try {
                            atheletsBeans = response.body()!!.data!!
                            mAdapterGuru = SankhyaCustomAdapter(atheletsBeans)
                            my_family_list.adapter = mAdapterGuru
                            mAdapterGuru!!.notifyDataSetChanged()

                        } catch (e: ArithmeticException) {
                            println(e)
                        } finally {
                            println("Family")
                        }
                    } else {
                        data_not_found_layout.visibility = View.VISIBLE
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@SankhyaActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Sankhya_List_Response>, t: Throwable) {
                Toast.makeText(this@SankhyaActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    private fun mySearchSankhyaList(
        user_id: String, chapter_id: String, length: String, start: String, search: String,
        start_date: String, end_date: String
    ) {
        val call: Call<Sankhya_List_Response> = MyHssApplication.instance!!.api.get_sankhya_listing(
            user_id, chapter_id, length,
            start, search, start_date, end_date
        )
        call.enqueue(object : Callback<Sankhya_List_Response> {
            override fun onResponse(
                call: Call<Sankhya_List_Response>,
                response: Response<Sankhya_List_Response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {

                        try {
                            atheletsBeans = response.body()!!.data!!
                            mAdapterGuru = SankhyaCustomAdapter(atheletsBeans)
                            my_family_list.adapter = mAdapterGuru
                            mAdapterGuru!!.notifyDataSetChanged()
                        } catch (e: ArithmeticException) {
                            println(e)
                        } finally {
                            println("Family")
                        }
                    } else {
                        Functions.displayMessage(this@SankhyaActivity, response.body()?.message)
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@SankhyaActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
            }

            override fun onFailure(call: Call<Sankhya_List_Response>, t: Throwable) {
                Toast.makeText(this@SankhyaActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

}