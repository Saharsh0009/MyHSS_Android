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
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.Functions
import com.myhss.ui.events.model.Data
import com.myhss.ui.events.model.EventListModel
import com.myhss.ui.events.model.Eventdata
import com.uk.myhss.Main.HomeActivity
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.ui.my_family.Adapter.EventsAdapter
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class EventsFragment : AppCompatActivity() { //Fragment() {
    private lateinit var sessionManager: SessionManager

    //    var tabLayout: TabLayout? = null
//    var viewPager: ViewPager? = null
    lateinit var total_count: TextView
    lateinit var home_layout: RelativeLayout

    //    lateinit var allevents_layout: LinearLayout
    lateinit var upcoming_events_layout: LinearLayout
    lateinit var completed_events_layout: LinearLayout
    lateinit var events_list_layout: LinearLayout

    lateinit var mLayoutManager: LinearLayoutManager
    lateinit var data_not_found_layout: RelativeLayout

    var dialog: Dialog? = null
    lateinit var back_arrow: ImageView
    lateinit var header_title: TextView

    //    lateinit var allevents_view: TextView
//    lateinit var allevents_line: ImageView
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
    private var mAdapterEvents: EventsAdapter? = null
    /*End All Events*/

    lateinit var eventListApiData: List<Data>

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


        upcoming_events_view = findViewById(R.id.upcoming_events_view)
        upcoming_events_line = findViewById(R.id.upcoming_events_line)
        completed_events_view = findViewById(R.id.completed_events_view)
        completed_events_line = findViewById(R.id.completed_events_line)

        upcoming_events_layout = findViewById(R.id.upcoming_events_layout)
        completed_events_layout = findViewById(R.id.completed_events_layout)


        CallAPI("", "1")

        mLayoutManager = LinearLayoutManager(this@EventsFragment)
        events_list.layoutManager = mLayoutManager
        upcoming_events_line.visibility = View.VISIBLE
        completed_events_line.visibility = View.INVISIBLE


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
        upcoming_events_view.setOnClickListener(DebouncedClickListener {
//            allevents_line.visibility = View.INVISIBLE
            upcoming_events_line.visibility = View.VISIBLE
            completed_events_line.visibility = View.INVISIBLE

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
        })

        completed_events_view.setOnClickListener(DebouncedClickListener {
//            allevents_line.visibility = View.INVISIBLE
            upcoming_events_line.visibility = View.INVISIBLE
            completed_events_line.visibility = View.VISIBLE

//            allevents_view.setTextColor(
//                ContextCompat.getColor(
//                    this@EventsFragment,
//                    R.color.grayColorColor
//                )
//            )
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
//                setEventListAdapter(eventListApiData.)
        })
        events_list_layout.visibility = View.GONE
//        events_list_layout.setOnClickListener(DebouncedClickListener {
////            val i = Intent(this@EventsFragment, AddMemberFirstActivity::class.java)
////            startActivity(i)
//        })

    }

    fun CallAPI(timeLine: String, cuurentPage: String) {
        if (Functions.isConnectingToInternet(this@EventsFragment)) {
            callEventListApi(timeLine, cuurentPage)
        } else {
            Toast.makeText(
                this@EventsFragment,
                resources.getString(R.string.no_connection),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun callEventListApi(timeLine: String, cuurentPage: String) {
        val pd = CustomProgressBar(this@EventsFragment)
        pd.show()
        val builderData: MultipartBody.Builder =
            MultipartBody.Builder().setType(MultipartBody.FORM)
        builderData.addFormDataPart("timeline", timeLine)
        builderData.addFormDataPart("current_page", cuurentPage)
        val requestBody: MultipartBody = builderData.build()

        val call: Call<EventListModel> =
            MyHssApplication.instance!!.api.postEventListData(requestBody)
        call.enqueue(object : Callback<EventListModel> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<EventListModel>,
                response: Response<EventListModel>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {
                        data_not_found_layout.visibility = View.GONE
                        try {
                            val eventData = response.body()!!.data!!
                            setEventListAdapter(eventData.upcoming.eventdata)

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

            override fun onFailure(call: Call<EventListModel>, t: Throwable) {
                Toast.makeText(this@EventsFragment, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    private fun setEventListAdapter(eventData_: List<Eventdata>) {
        mAdapterEvents = EventsAdapter(eventData_)
        events_list.adapter = mAdapterEvents
        mAdapterEvents!!.notifyDataSetChanged()
    }
}