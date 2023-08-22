package com.myhss.ui.SuchanaBoard

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.Functions
import com.myhss.ui.SuchanaBoard.Adapter.NotificationAdapter
import com.uk.myhss.Main.HomeActivity
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.ui.linked_family.Model.Get_Member_Listing_Datum

import com.uk.myhss.ui.linked_family.Model.Get_Member_Listing_Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NotificationList : AppCompatActivity() {  // Fragment()

    private lateinit var sessionManager: SessionManager

    lateinit var data_not_found_layout: RelativeLayout
    lateinit var notification_list: RecyclerView
    lateinit var rootLayout: RelativeLayout
//    lateinit var suchana_cancel_layout: RelativeLayout

    lateinit var notification_layout: LinearLayout
    lateinit var back_arrow: ImageView
    lateinit var header_title: TextView

    lateinit var USERID: String
    lateinit var TAB: String
    lateinit var MEMBERID: String
    lateinit var STATUS: String
    lateinit var LENGTH: String
    lateinit var START: String
    lateinit var SEARCH: String
    lateinit var CHAPTERID: String
    lateinit var Tab_Type: String

    private var atheletsBeans: List<Get_Member_Listing_Datum> =
        ArrayList<Get_Member_Listing_Datum>()
    private var suchana_adapter: NotificationAdapter? = null

    lateinit var mLayoutManager: LinearLayoutManager

    /*override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_suchana_board, container, false)*/

    @SuppressLint("SetTextI18n", "CutPasteId", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_suchana_board)

        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("MyShakhaVC")
        sessionManager.firebaseAnalytics.setUserProperty("MyShakhaVC", "SuchanaBoardFragment")

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        back_arrow = findViewById(R.id.back_arrow)
        header_title = findViewById(R.id.header_title)

        header_title.text = getString(R.string.my_notification)

        back_arrow.setOnClickListener(DebouncedClickListener {
            val i = Intent(this, HomeActivity::class.java)
            startActivity(i)
            finishAffinity()
//            (context as Activity).finishAffinity()
        })

        data_not_found_layout = findViewById(R.id.data_not_found_layout)
        notification_list = findViewById(R.id.notification_list)
        rootLayout = findViewById(R.id.rootLayout)
//        suchana_cancel_layout = findViewById(R.id.suchana_cancel_layout)

//        notification_list.layoutManager = LinearLayoutManager(this)
        mLayoutManager = LinearLayoutManager(this@NotificationList)
        notification_list.layoutManager = mLayoutManager

        val end: Int = 100
        val start: Int = 0

        if (sessionManager.fetchSHAKHAID() != "") {
            if (Functions.isConnectingToInternet(this)) {
                USERID = sessionManager.fetchUserID()!!
                Log.d("USERID", USERID)
                TAB = "family"
                MEMBERID = sessionManager.fetchMEMBERID()!!
                STATUS = "all"
                LENGTH = end.toString()
                START = start.toString()
                SEARCH = ""
                CHAPTERID = ""
                Tab_Type = "Suchana Board"
                myMemberList(
                    USERID,
                    TAB,
                    MEMBERID,
                    STATUS,
                    LENGTH,
                    START,
                    SEARCH,
                    CHAPTERID,
                    Tab_Type
                )
            } else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.no_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        /*notification_list.setOnScrollListener(object : EndLessScroll(mLayoutManager) {
            override fun loadMore(current_page: Int) {
                var end:Int = 100
                var start:Int = 0

                start = end + 1
                end += 100

                if (sessionManager.fetchSHAKHAID() != "") {
                    if (Functions.isConnectingToInternet(this@NotificationList)) {
                        USERID = sessionManager.fetchUserID()!!
                        Log.d("USERID", USERID)
                        TAB = "family"
                        MEMBERID = sessionManager.fetchMEMBERID()!!
                        STATUS = "all"
                        LENGTH = end.toString()
                        START = start.toString()
                        SEARCH = ""
                        CHAPTERID = ""
                        Tab_Type = "Suchana Board"
                        myMemberList(USERID, TAB, MEMBERID, STATUS, LENGTH, START, SEARCH, CHAPTERID, Tab_Type)
                    } else {
                        Toast.makeText(
                            this@NotificationList,
                            resources.getString(R.string.no_connection),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })*/

        /*suchana_cancel_layout.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    HomeActivity::class.java
                )
            )
            (context as Activity).finishAffinity()
        }*/
    }

    private fun myMemberList(
        user_id: String,
        tab: String,
        member_id: String,
        status: String,
        length: String,
        start: String,
        search: String,
        chapter_id: String,
        tab_type: String
    ) {
        val pd = CustomProgressBar(this)
        pd.show()
        val call: Call<Get_Member_Listing_Response> =
            MyHssApplication.instance!!.api.get_member_listing(
                user_id, tab, member_id,
                status, length, start, search, chapter_id
            )
        call.enqueue(object : Callback<Get_Member_Listing_Response> {
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
                            /*for (i in 1 until atheletsBeans.size) {
                                Log.d("firstName", atheletsBeans[i].firstName.toString())
                            }*/
                            Log.d("count=>", atheletsBeans.size.toString())
                            Log.d("tab_type=>", tab_type)

                            var dietaryName: List<String> = java.util.ArrayList<String>()

                            dietaryName = listOf(arrayOf(atheletsBeans).toString())

                            val mStringList = java.util.ArrayList<String>()
                            /*for (i in 0 until atheletsBeans.size) {
                                mStringList.add("Guru Puja")
                            }*/

                            mStringList.add("New membership added")
                            mStringList.add("Update membership application")
                            mStringList.add("One-Time Dakshina")
                            mStringList.add("Add Sankhya")

                            var mStringArray = mStringList.toArray()

                            for (i in mStringArray.indices) {
                                Log.d("string is", mStringArray[i] as String)
                            }

                            mStringArray = mStringList.toArray(mStringArray)

                            val list: java.util.ArrayList<String> = arrayListOf<String>()

                            for (element in mStringArray) {
                                Log.d("LIST==>", element.toString())
                                list.add(element.toString())
                                Log.d("list==>", list.toString())

                                val listn = arrayOf(element)
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                    dietaryName =
                                        list//listOf(listn.toCollection(ArrayList()).toString())
                                }
                            }


                            suchana_adapter = NotificationAdapter(dietaryName, tab_type)

                            notification_list.adapter = suchana_adapter
                            suchana_adapter!!.notifyDataSetChanged()

                        } catch (e: ArithmeticException) {
                            println(e)
                        } finally {
                            println("Family")
                        }
                    } else {
                        data_not_found_layout.visibility = View.VISIBLE
//                        Functions.displayMessage(this@NotificationList,response.body()?.message)
//                        Functions.showAlertMessageWithOK(
//                            this@NotificationList, "",
////                        "Message",
//                            response.body()?.message
//                        )
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@NotificationList, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Get_Member_Listing_Response>, t: Throwable) {
                Toast.makeText(this@NotificationList, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }
}