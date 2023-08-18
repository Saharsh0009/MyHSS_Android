package com.uk.myhss.ui.policies

import android.annotation.SuppressLint
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
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.Functions
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.ui.linked_family.Model.Get_Member_Listing_Datum
import com.uk.myhss.ui.linked_family.Model.Get_Member_Listing_Response
import com.uk.myhss.ui.my_family.Adapter.CustomAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFamilyFragment : Fragment() {
    private lateinit var sessionManager: SessionManager

    //    private val sharedPrefFile = "MyHss"
//    lateinit var membership_view: RelativeLayout
    lateinit var my_family_list: RecyclerView

    lateinit var add_family_layout: LinearLayout
    lateinit var family_layout: RelativeLayout
    lateinit var search_fields: AppCompatEditText
    lateinit var USERID: String
    lateinit var TAB: String
    lateinit var MEMBERID: String
    lateinit var STATUS: String
    lateinit var LENGTH: String
    lateinit var START: String
    lateinit var SEARCH: String
    lateinit var CHAPTERID: String

    private var atheletsBeans: List<Get_Member_Listing_Datum> =
        ArrayList<Get_Member_Listing_Datum>()
    private var mAdapterGuru: CustomAdapter? = null
    lateinit var First_name: String

    lateinit var mLayoutManager: LinearLayoutManager

    @SuppressLint("WrongConstant", "NewApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_my_family, container, false)

        sessionManager = SessionManager(requireContext())

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
        sessionManager.firebaseAnalytics.setUserId("MyFamilyVC")
        sessionManager.firebaseAnalytics.setUserProperty("MyFamilyVC", "MyFamilyFragment")

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

//        membership_view = root.findViewById(R.id.membership_view)

        add_family_layout = root.findViewById(R.id.add_family_layout)
        family_layout = root.findViewById(R.id.family_layout)
        search_fields = root.findViewById(R.id.search_fields)

        my_family_list = root.findViewById(R.id.my_family_list)

        /*my_family_list.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayout.VERTICAL,
            false
        )*/

        mLayoutManager = LinearLayoutManager(requireContext())
        my_family_list.layoutManager = mLayoutManager

//        my_family_list.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)

        val end: Int = 100
        val start: Int = 0

        if (Functions.isConnectingToInternet(context)) {
            USERID = sessionManager.fetchUserID()!!
            Log.d("USERID", USERID)
            TAB = "family"
            MEMBERID = sessionManager.fetchMEMBERID()!!
            STATUS = "all"
            LENGTH = end.toString()
            START = start.toString()
            SEARCH = ""
            CHAPTERID = ""
            myMemberList(USERID, TAB, MEMBERID, STATUS, LENGTH, START, SEARCH, CHAPTERID)
        } else {
            Toast.makeText(
                context,
                resources.getString(R.string.no_connection),
                Toast.LENGTH_SHORT
            ).show()
        }

        /*my_family_list.setOnScrollListener(object : EndLessScroll(mLayoutManager) {
            override fun loadMore(current_page: Int) {

                var end:Int = 100
                var start:Int = 0

                start = end + 1
                end += 100

                if (Functions.isConnectingToInternet(context)) {
                    USERID = sessionManager.fetchUserID()!!
                    Log.d("USERID", USERID)
                    TAB = "family"
                    MEMBERID = sessionManager.fetchMEMBERID()!!
                    STATUS = "all"
                    LENGTH = end.toString()
                    START = start.toString()
                    SEARCH = ""
                    CHAPTERID = ""
                    myMemberList(USERID, TAB, MEMBERID, STATUS, LENGTH, START, SEARCH, CHAPTERID)
                } else {
                    Toast.makeText(
                        context,
                        resources.getString(R.string.no_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })*/

        search_fields.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                val end: Int = 100
                val start: Int = 0

                if (Functions.isConnectingToInternet(context)) {
                    USERID = sessionManager.fetchUserID()!!
                    Log.d("USERID", USERID)
                    TAB = "family"
                    MEMBERID = sessionManager.fetchMEMBERID()!!
                    STATUS = "all"
                    LENGTH = end.toString()
                    START = start.toString()
                    SEARCH = s.toString()
                    CHAPTERID = ""
                    mySearchMemberList(
                        USERID,
                        TAB,
                        MEMBERID,
                        STATUS,
                        LENGTH,
                        START,
                        SEARCH,
                        CHAPTERID
                    )
                } else {
                    Toast.makeText(
                        context,
                        resources.getString(R.string.no_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        val bestCities =
            listOf("Lahore", "Berlin", "Lisbon", "Tokyo", "Toronto", "Sydney", "Osaka", "Istanbul")
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            bestCities
        )

        add_family_layout.visibility = View.GONE

//        add_family_layout.setOnClickListener {
//            val i = Intent(requireContext(), AddMemberFirstActivity::class.java)
//            i.putExtra("TYPE_SELF", "family")
//            startActivity(i)
//        }

        /*membership_view.setOnClickListener {
            showPopup()
        }*/

        return root
    }

    private fun myMemberList(
        user_id: String, tab: String, member_id: String, status: String,
        length: String, start: String, search: String, chapter_id: String
    ) {
        val pd = CustomProgressBar(context)
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

                        try {
                            atheletsBeans = response.body()!!.data!!
                            Log.d("atheletsBeans", atheletsBeans.toString())
                            for (i in 1 until atheletsBeans.size) {
                                Log.d("firstName", atheletsBeans[i].firstName.toString())
                            }

                            mAdapterGuru = CustomAdapter(atheletsBeans)

                            my_family_list.adapter = mAdapterGuru
                            mAdapterGuru!!.notifyDataSetChanged()

                        } catch (e: ArithmeticException) {
                            println(e)
                        } finally {
                            println("Family")
                        }
                    } else {
                        Functions.showAlertMessageWithOK(
                            context, "",
//                        "Message",
                            response.body()?.message
                        )
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        context, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Get_Member_Listing_Response>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    private fun mySearchMemberList(
        user_id: String, tab: String, member_id: String, status: String,
        length: String, start: String, search: String, chapter_id: String
    ) {
//        val pd = CustomProgressBar(context)
//        pd.show()
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

                        try {
                            atheletsBeans = response.body()!!.data!!
                            Log.d("atheletsBeans", atheletsBeans.toString())
                            for (i in 1 until atheletsBeans.size) {
                                Log.d("firstName", atheletsBeans[i].firstName.toString())
                            }

                            mAdapterGuru = CustomAdapter(atheletsBeans)

                            my_family_list.adapter = mAdapterGuru
                            mAdapterGuru!!.notifyDataSetChanged()

                        } catch (e: ArithmeticException) {
                            println(e)
                        } finally {
                            println("Family")
                        }
                    } else {
//                        Functions.showAlertMessageWithOK(
//                            context, "",
////                        "Message",
//                            response.body()?.message
//                        )
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        context, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
//                pd.dismiss()
            }

            override fun onFailure(call: Call<Get_Member_Listing_Response>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
//                pd.dismiss()
            }
        })
    }

}