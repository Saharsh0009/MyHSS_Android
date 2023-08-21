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
import com.myhss.ui.SuchanaBoard.Adapter.SuchnaAdapter
import com.myhss.ui.SuchanaBoard.Model.Get_Suchana_Datum
import com.myhss.ui.SuchanaBoard.Model.Get_Suchana_Response
import com.uk.myhss.Main.HomeActivity
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class SuchanaBoardActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    lateinit var data_not_found_layout: RelativeLayout
    lateinit var notification_list: RecyclerView
    lateinit var rootLayout: RelativeLayout
//    lateinit var suchana_cancel_layout: RelativeLayout

    lateinit var USERID: String
    lateinit var TAB: String
    lateinit var MEMBERID: String
    lateinit var DEVICE_TOKEN: String
    lateinit var Tab_Type: String

    lateinit var mLayoutManager: LinearLayoutManager

    private var suchana_data: List<Get_Suchana_Datum> =
        ArrayList<Get_Suchana_Datum>()
    private var suchana_adapter: SuchnaAdapter? = null

    lateinit var back_arrow: ImageView
    lateinit var header_title: TextView

    private var year = 0
    private var month = 0
    private var day = 0
    private lateinit var calendar: Calendar
    private var strCurrentDate: String = ""

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

        header_title.text = getString(R.string.my_suchna_board)

        back_arrow.setOnClickListener(DebouncedClickListener {
            val i = Intent(this@SuchanaBoardActivity, HomeActivity::class.java)
            startActivity(i)
            finishAffinity()
        })

        data_not_found_layout = findViewById(R.id.data_not_found_layout)
        notification_list = findViewById(R.id.notification_list)
        rootLayout = findViewById(R.id.rootLayout)
//        suchana_cancel_layout = findViewById(R.id.suchana_cancel_layout)

        mLayoutManager = LinearLayoutManager(this@SuchanaBoardActivity)
        notification_list.layoutManager = mLayoutManager

        var calendar = Calendar.getInstance(Locale.getDefault())
        val simpledateFormat = SimpleDateFormat("yyyy-MM-dd")
        strCurrentDate = simpledateFormat.format(calendar.time)
        Functions.printLog("currentDate", strCurrentDate)


        if (sessionManager.fetchSHAKHAID() != "") {
            if (Functions.isConnectingToInternet(this)) {
                USERID = sessionManager.fetchUserID()!!
                Log.d("USERID", USERID)
                MEMBERID = sessionManager.fetchMEMBERID()!!
                DEVICE_TOKEN = sessionManager.fetchDEVICE_TOKEN()!!
                Tab_Type = "Suchana Board"

                calendar = Calendar.getInstance()
                calendar.add(Calendar.MONTH, -1)
                year = calendar.get(Calendar.YEAR)
                month = calendar.get(Calendar.MONTH)
                day = calendar.get(Calendar.DAY_OF_MONTH)

                val simpledateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val strCurrentDatenew = simpledateFormat.format(calendar.time)
                Functions.printLog("strCurrentDatenew", strCurrentDatenew)

                mySuchanaBoard(
                    USERID,
                    MEMBERID,
                    Tab_Type
                ) // strCurrentDate.toString(), strCurrentDatenew.toString(),
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
                    if (Functions.isConnectingToInternet(this@SuchanaBoardActivity)) {
                        USERID = sessionManager.fetchUserID()!!
                        Log.d("USERID", USERID)
                        MEMBERID = sessionManager.fetchMEMBERID()!!
                        DEVICE_TOKEN = sessionManager.fetchDEVICE_TOKEN()!!
                        Tab_Type = "Suchana Board"
                        mySuchanaBoard(USERID, MEMBERID, DEVICE_TOKEN, Tab_Type)
                    } else {
                        Toast.makeText(
                            this@SuchanaBoardActivity,
                            resources.getString(R.string.no_connection),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })*/
    }

    private fun mySuchanaBoard(
        user_id: String,
        member_id: String,
//        strCurrentDate: String,
//        strCurrentDatenew: String,
        tab_type: String
    ) {
        val pd = CustomProgressBar(this)
        pd.show()
        val call: Call<Get_Suchana_Response> =
            MyHssApplication.instance!!.api.get_suchana_board(
                user_id, member_id
            )  // , strCurrentDate, strCurrentDatenew
        call.enqueue(object : Callback<Get_Suchana_Response> {
            override fun onResponse(
                call: Call<Get_Suchana_Response>,
                response: Response<Get_Suchana_Response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {
                        data_not_found_layout.visibility = View.GONE
                        try {
                            suchana_data = response.body()!!.data!!
                            Log.d("suchana_data", suchana_data.toString())

                            Log.d("count=>", suchana_data.size.toString())
                            Log.d("tab_type=>", tab_type)

                            var dietaryName: List<String> = java.util.ArrayList<String>()

                            dietaryName = listOf(arrayOf(suchana_data).toString())

                            val mStringList = java.util.ArrayList<String>()
                            /*for (i in 0 until atheletsBeans.size) {
                                mStringList.add("Guru Puja")
                            }*/

                            mStringList.add("Guru Purnima")
                            mStringList.add("Raksha Bhadhan")
                            mStringList.add("Makar Sankranti")
                            mStringList.add("Vijay Dashmi")

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

//                            val distinct: List<Get_Suchana_Datum> = LinkedHashSet(suchana_data).toMutableList()

                            suchana_adapter = SuchnaAdapter(suchana_data, dietaryName, tab_type)

                            notification_list.adapter = suchana_adapter
                            suchana_adapter!!.notifyDataSetChanged()

                        } catch (e: ArithmeticException) {
                            println(e)
                        } finally {
                            println("Family")
                        }
                    } else {
                        data_not_found_layout.visibility = View.VISIBLE
//                        Functions.displayMessage(this@SuchanaBoardActivity,response.body()?.message)
//                        Functions.showAlertMessageWithOK(
//                            this@SuchanaBoardActivity, "",
////                        "Message",
//                            response.body()?.message
//                        )
                    }
                } else {
//                    Functions.showAlertMessageWithOK(this@SuchanaBoardActivity, "Message", getString(R.string.some_thing_wrong),)
                    data_not_found_layout.visibility = View.VISIBLE
                }
                pd.dismiss()

            }

            override fun onFailure(call: Call<Get_Suchana_Response>, t: Throwable) {
                Toast.makeText(this@SuchanaBoardActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
                data_not_found_layout.visibility = View.VISIBLE
            }
        })
    }
}