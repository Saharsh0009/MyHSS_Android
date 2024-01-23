package com.uk.myhss.ui.dashboard

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.DebugLog
import com.myhss.Utils.Functions
import com.myhss.ui.events.model.Data
import com.myhss.ui.events.model.EventListModel
import com.myhss.ui.events.model.Eventdata
import com.myhss.ui.events.model.Past
import com.myhss.ui.events.model.Upcoming
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


class EventsFragment : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    lateinit var home_layout: RelativeLayout
    lateinit var upcoming_events_layout: LinearLayout
    lateinit var completed_events_layout: LinearLayout
    lateinit var events_list_layout: LinearLayout
    lateinit var mLayoutManager: LinearLayoutManager
    lateinit var data_not_found_layout: RelativeLayout
    lateinit var back_arrow: ImageView
    lateinit var header_title: TextView
    lateinit var upcoming_events_view: TextView
    lateinit var upcoming_events_line: ImageView
    lateinit var completed_events_view: TextView
    lateinit var completed_events_line: ImageView
    lateinit var rcv_events_list: RecyclerView
    lateinit var search_fields: AppCompatEditText

    private var mAdapterEvents: EventsAdapter? = null
    lateinit var upComingEventData: Upcoming
    lateinit var pastEventData: Past
    var pg_tot_page = 0
    var pg_next_page = 1
    var isLoading = false
    var eventType = "1"
    var isSerch = false
    var pg_next_up = 1
    var pg_next_past = 1

    @RequiresApi(Build.VERSION_CODES.M)
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

        rcv_events_list = findViewById(R.id.events_list)
        data_not_found_layout = findViewById(R.id.data_not_found_layout)
        events_list_layout = findViewById(R.id.events_list_layout)
        upcoming_events_view = findViewById(R.id.upcoming_events_view)
        upcoming_events_line = findViewById(R.id.upcoming_events_line)
        completed_events_view = findViewById(R.id.completed_events_view)
        completed_events_line = findViewById(R.id.completed_events_line)
        upcoming_events_layout = findViewById(R.id.upcoming_events_layout)
        completed_events_layout = findViewById(R.id.completed_events_layout)
        search_fields = findViewById(R.id.search_fields)


        mLayoutManager = LinearLayoutManager(this@EventsFragment)
        rcv_events_list.layoutManager = mLayoutManager
        upcoming_events_line.visibility = View.VISIBLE
        completed_events_line.visibility = View.INVISIBLE

        mAdapterEvents = EventsAdapter(ArrayList(), eventType)
        rcv_events_list.adapter = mAdapterEvents
        pg_next_page = pg_next_up
        CallAPI("", pg_next_page.toString(), false)

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
            eventType = "1"
            pg_tot_page = upComingEventData.paginate.total_pages
            pg_next_page = pg_next_up
            search_fields.setText("")
            isSerch = false
            isLoading = false
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
            mAdapterEvents?.setData(upComingEventData.eventdata.toMutableList(), eventType)
        })

        completed_events_view.setOnClickListener(DebouncedClickListener {
            eventType = "2"
            pg_tot_page = pastEventData.paginate.total_pages
            pg_next_page = pg_next_past
            search_fields.setText("")
            isSerch = false
            isLoading = false
            upcoming_events_line.visibility = View.INVISIBLE
            completed_events_line.visibility = View.VISIBLE
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
            mAdapterEvents?.setData(pastEventData.eventdata.toMutableList(), eventType)
        })
        events_list_layout.visibility = View.GONE

        rcv_events_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                    ) {
                        if (pg_next_page < pg_tot_page && !isSerch) {
                            pg_next_page += 1
                            if (eventType == "1") {
                                pg_next_up = pg_next_page
                            } else {
                                pg_next_past = pg_next_page
                            }
                            isLoading = true
                            DebugLog.e("SCROLL : pg_tot_page : $pg_tot_page  == pg_next_page : $pg_next_page")
                            CallAPI(eventType, pg_next_page.toString(), true)
                        }
                    }
                }
            }
        })

        search_fields.doOnTextChanged { text, start, before, count ->
            if (eventType == "1") {
                var eventList = upComingEventData.eventdata
                if (count > 0) {
                    isSerch = true
                    val filteredList = eventList.filter { item ->
                        item.event_title.contains(text!!, ignoreCase = true)
                    }
                    mAdapterEvents!!.setData(filteredList, eventType)
                } else {
                    isSerch = false
                    mAdapterEvents!!.setData(upComingEventData.eventdata, eventType)
                }
            } else {
                var eventList = pastEventData.eventdata
                if (count > 0) {
                    isSerch = true
                    val filteredList = eventList.filter { item ->
                        item.event_title.contains(text!!, ignoreCase = true)
                    }
                    mAdapterEvents!!.setData(filteredList, eventType)
                } else {
                    isSerch = false
                    mAdapterEvents!!.setData(pastEventData.eventdata, eventType)
                }
            }
        }
    }

    fun CallAPI(timeLine: String, cuurentPage: String, bFlag: Boolean) {
        if (Functions.isConnectingToInternet(this@EventsFragment)) {
            callEventListApi(timeLine, cuurentPage, bFlag)
        } else {
            Toast.makeText(
                this@EventsFragment,
                resources.getString(R.string.no_connection),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun callEventListApi(timeLine: String, curentPage: String, bFlag: Boolean) {
        DebugLog.e("timeLine => $timeLine :: pg_next_page => $curentPage  :: bFlag => $bFlag")
        val pd = CustomProgressBar(this@EventsFragment)
        pd.show()
        val builderData: MultipartBody.Builder =
            MultipartBody.Builder().setType(MultipartBody.FORM)
        builderData.addFormDataPart("timeline", timeLine)
        builderData.addFormDataPart("current_page", curentPage)
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
                            var eventListApiData = response.body()!!.data!!

                            when (timeLine) {
                                "" -> {
                                    upComingEventData = eventListApiData.upcoming
                                    pastEventData = eventListApiData.past

                                    if (bFlag) {
                                        mAdapterEvents?.addData(
                                            eventListApiData.upcoming.eventdata.toMutableList(),
                                            eventType
                                        )
                                    } else {
                                        mAdapterEvents?.setData(
                                            eventListApiData.upcoming.eventdata.toMutableList(),
                                            eventType
                                        )
                                    }
                                    pg_tot_page = eventListApiData.upcoming.paginate.total_pages
                                }

                                "1" -> {
                                    upComingEventData.eventdata += eventListApiData.upcoming.eventdata

                                    if (bFlag) {
                                        mAdapterEvents?.addData(
                                            eventListApiData.upcoming.eventdata.toMutableList(),
                                            eventType
                                        )
                                    } else {
                                        mAdapterEvents?.setData(
                                            eventListApiData.upcoming.eventdata.toMutableList(),
                                            eventType
                                        )
                                    }
                                    pg_tot_page = eventListApiData.upcoming.paginate.total_pages
                                }
                                "2" -> {
                                    pastEventData.eventdata += eventListApiData.past.eventdata

                                    if (bFlag) {
                                        mAdapterEvents?.addData(
                                            eventListApiData.past.eventdata.toMutableList(),
                                            eventType
                                        )
                                    } else {
                                        mAdapterEvents?.setData(
                                            eventListApiData.past.eventdata.toMutableList(),
                                            eventType
                                        )
                                    }
                                    pg_tot_page = eventListApiData.past.paginate.total_pages
                                }
                            }
                            isLoading = false
                        } catch (e: ArithmeticException) {
                            DebugLog.e("Error : $e")
                        }
                    } else {
                        data_not_found_layout.visibility = View.VISIBLE
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
}