package com.uk.myhss.ui.dashboard

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.Functions

import com.uk.myhss.AddMember.AddMemberFirstActivity
import com.uk.myhss.Main.HomeActivity
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.ui.linked_family.Model.Get_Member_Listing_Datum
import com.uk.myhss.ui.linked_family.Model.Get_Member_Listing_Response
import com.uk.myhss.ui.my_family.Adapter.EventsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class EventsFragment : AppCompatActivity() { //Fragment() {
    private lateinit var sessionManager: SessionManager
    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null
    lateinit var total_count: TextView
    lateinit var home_layout: RelativeLayout

    lateinit var allevents_layout: LinearLayout
    lateinit var upcoming_events_layout: LinearLayout
    lateinit var completed_events_layout: LinearLayout
    lateinit var events_list_layout: LinearLayout

    lateinit var mLayoutManager: LinearLayoutManager
    lateinit var data_not_found_layout: RelativeLayout

    var dialog: Dialog? = null
    lateinit var back_arrow: ImageView
    lateinit var header_title: TextView

    lateinit var allevents_view: TextView
    lateinit var allevents_line: ImageView
    lateinit var upcoming_events_view: TextView
    lateinit var upcoming_events_line: ImageView
    lateinit var completed_events_view: TextView
    lateinit var completed_events_line: ImageView

    lateinit var events_list: RecyclerView

    lateinit var USERID: String
    lateinit var TAB: String
    lateinit var MEMBERID: String
    lateinit var STATUS: String
    lateinit var LENGTH: String
    lateinit var START: String
    lateinit var SEARCH: String
    lateinit var CHAPTERID: String

    /*Start All Events*/
    private var atheletsBeans: List<Get_Member_Listing_Datum> =
        ArrayList<Get_Member_Listing_Datum>()
    private var mAdapterEvents: EventsAdapter? = null
    /*End All Events*/


    private var loading = true
    var pastVisiblesItems = 0
    var visibleItemCount: Int = 0
    var totalItemCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_events)

        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("EventsFragmentVC")
        sessionManager.firebaseAnalytics.setUserProperty("EventsFragmentVC", "EventsFragment")

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        back_arrow = findViewById(R.id.back_arrow)
        header_title = findViewById(R.id.header_title)

        header_title.text = getString(R.string.events)

        back_arrow.setOnClickListener(DebouncedClickListener {
            val i = Intent(this@EventsFragment, HomeActivity::class.java)
            startActivity(i)
            finishAffinity()
        })

        events_list = findViewById(R.id.events_list)
        data_not_found_layout = findViewById(R.id.data_not_found_layout)
        events_list_layout = findViewById(R.id.events_list_layout)

        allevents_view = findViewById(R.id.allevents_view)
        allevents_line = findViewById(R.id.allevents_line)
        upcoming_events_view = findViewById(R.id.upcoming_events_view)
        upcoming_events_line = findViewById(R.id.upcoming_events_line)
        completed_events_view = findViewById(R.id.completed_events_view)
        completed_events_line = findViewById(R.id.completed_events_line)

        allevents_layout = findViewById(R.id.allevents_layout)
        upcoming_events_layout = findViewById(R.id.upcoming_events_layout)
        completed_events_layout = findViewById(R.id.completed_events_layout)

//        if (intent.getStringExtra("DashBoard") == "SHAKHAVIEW") {
//            Handler().postDelayed({
//                myshakha_view.callOnClick()
//            }, 100)
//        } else {
        val end: Int = 100
        val start: Int = 0

        CallAPI(start, end)

        mLayoutManager = LinearLayoutManager(this@EventsFragment)
        events_list.layoutManager = mLayoutManager

        allevents_line.visibility = View.VISIBLE
        upcoming_events_line.visibility = View.INVISIBLE
        completed_events_line.visibility = View.INVISIBLE

        allevents_view.setTextColor(
            ContextCompat.getColor(
                this@EventsFragment,
                R.color.primaryColor
            )
        )
        upcoming_events_view.setTextColor(
            ContextCompat.getColor(
                this@EventsFragment,
                R.color.grayColorColor
            )
        )
        completed_events_view.setTextColor(
            ContextCompat.getColor(
                this@EventsFragment,
                R.color.grayColorColor
            )
        )

        allevents_view.setOnClickListener(DebouncedClickListener {
            allevents_line.visibility = View.VISIBLE
            upcoming_events_line.visibility = View.INVISIBLE
            completed_events_line.visibility = View.INVISIBLE

            allevents_view.setTextColor(
                ContextCompat.getColor(
                    this@EventsFragment,
                    R.color.primaryColor
                )
            )
            upcoming_events_view.setTextColor(
                ContextCompat.getColor(
                    this@EventsFragment,
                    R.color.grayColorColor
                )
            )
            completed_events_view.setTextColor(
                ContextCompat.getColor(
                    this@EventsFragment,
                    R.color.grayColorColor
                )
            )

            val end: Int = 100
            val start: Int = 0

            CallAPI(start, end)
        })

        upcoming_events_view.setOnClickListener(DebouncedClickListener {
            allevents_line.visibility = View.INVISIBLE
            upcoming_events_line.visibility = View.VISIBLE
            completed_events_line.visibility = View.INVISIBLE

            allevents_view.setTextColor(
                ContextCompat.getColor(
                    this@EventsFragment,
                    R.color.grayColorColor
                )
            )
            upcoming_events_view.setTextColor(
                ContextCompat.getColor(
                    this@EventsFragment,
                    R.color.primaryColor
                )
            )
            completed_events_view.setTextColor(
                ContextCompat.getColor(
                    this@EventsFragment,
                    R.color.grayColorColor
                )
            )

            val end: Int = 100
            val start: Int = 0

            CallAPI(start, end)
        })

        completed_events_view.setOnClickListener(DebouncedClickListener {
            allevents_line.visibility = View.INVISIBLE
            upcoming_events_line.visibility = View.INVISIBLE
            completed_events_line.visibility = View.VISIBLE

            allevents_view.setTextColor(
                ContextCompat.getColor(
                    this@EventsFragment,
                    R.color.grayColorColor
                )
            )
            upcoming_events_view.setTextColor(
                ContextCompat.getColor(
                    this@EventsFragment,
                    R.color.grayColorColor
                )
            )
            completed_events_view.setTextColor(
                ContextCompat.getColor(
                    this@EventsFragment,
                    R.color.primaryColor
                )
            )

            val end: Int = 100
            val start: Int = 0

            CallAPI(start, end)
        })
        events_list_layout.visibility = View.GONE
//        events_list_layout.setOnClickListener(DebouncedClickListener {
////            val i = Intent(this@EventsFragment, AddMemberFirstActivity::class.java)
////            startActivity(i)
//        })

    }

    fun CallAPI(PAGE: Int, END: Int) {
        if (Functions.isConnectingToInternet(this@EventsFragment)) {
            USERID = sessionManager.fetchUserID()!!
            Log.d("USERID", USERID)
            TAB = "family"
            MEMBERID = sessionManager.fetchMEMBERID()!!
            STATUS = "all"
            LENGTH = END.toString()
            START = PAGE.toString()
            SEARCH = ""
            CHAPTERID = ""
            myMemberList(USERID, TAB, MEMBERID, STATUS, LENGTH, START, SEARCH, CHAPTERID)
        } else {
            Toast.makeText(
                this@EventsFragment,
                resources.getString(R.string.no_connection),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun myMemberList(
        user_id: String, tab: String, member_id: String, status: String,
        length: String, start: String, search: String, chapter_id: String
    ) {
        val pd = CustomProgressBar(this@EventsFragment)
        pd.show()
        val call: Call<Get_Member_Listing_Response> =
            MyHssApplication.instance!!.api.get_member_listing(
                user_id, tab, member_id,
                status, length, start, search, chapter_id
            )
        call.enqueue(object : Callback<Get_Member_Listing_Response> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<Get_Member_Listing_Response>,
                response: Response<Get_Member_Listing_Response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {
                        data_not_found_layout.visibility = View.GONE
                        try {
                            atheletsBeans = response.body()!!.data!!
                            Log.d("atheletsBeans", atheletsBeans.toString())
                            for (i in 1 until atheletsBeans.size) {
                                Log.d("firstName", atheletsBeans[i].firstName.toString())
                            }
                            Log.d("count=>", atheletsBeans.size.toString())

                            mAdapterEvents = EventsAdapter(atheletsBeans)

                            events_list.adapter = mAdapterEvents
                            mAdapterEvents!!.notifyDataSetChanged()

                        } catch (e: ArithmeticException) {
                            println(e)
                        } finally {
                            println("Family")
                        }
                    } else {
                        data_not_found_layout.visibility = View.VISIBLE
//                        Functions.displayMessage(this@LinkedFamilyFragment,response.body()?.message)
//                        Functions.showAlertMessageWithOK(
//                            this@LinkedFamilyFragment, "",
////                        "Message",
//                            response.body()?.message
//                        )
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@EventsFragment, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Get_Member_Listing_Response>, t: Throwable) {
                Toast.makeText(this@EventsFragment, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }
}