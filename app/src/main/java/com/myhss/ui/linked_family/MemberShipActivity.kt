package com.uk.myhss.ui.linked_family

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.Functions

import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.ui.dashboard.LinkedFamilyFragment
import com.uk.myhss.ui.linked_family.Model.*
import com.uk.myhss.ui.sankhya_report.AddSankhyaActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class MemberShipActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    lateinit var my_family_list: RecyclerView

    lateinit var data_not_found_layout: RelativeLayout
    lateinit var add_family_layout: LinearLayout
    lateinit var registration_success_btn: TextView
    lateinit var rootview: LinearLayout
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
    private var mAdapterGuru: MembersCustomAdapter? = null
    lateinit var First_name: String
    lateinit var SHOW_HIDE: String

    lateinit var mLayoutManager: LinearLayoutManager


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_sankhya_report)

        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("MembershipApplicationsVC")
        sessionManager.firebaseAnalytics.setUserProperty(
            "MembershipApplicationsVC", "MemberShipActivity"
        )

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        val back_arrow = findViewById<ImageView>(R.id.back_arrow)
        val header_title = findViewById<TextView>(R.id.header_title)

        if (intent.getStringExtra("MEMBERS") == "ALL_MEMBERS") {
            header_title.text = getString(R.string.membership_applications_new)
            SHOW_HIDE = "ALLSHOW"
            sessionManager.firebaseAnalytics.setUserId("MembershipApplicationsVC")
            sessionManager.firebaseAnalytics.setUserProperty(
                "MembershipApplicationsVC", "MemberShipActivity"
            )
        } else if (intent.getStringExtra("MEMBERS") == "ACTIVE_MEMBERS") {
            header_title.text = "Active " + getString(R.string.members)
            SHOW_HIDE = "ACTIVE"
            sessionManager.firebaseAnalytics.setUserId("ActiveMembersVC")
            sessionManager.firebaseAnalytics.setUserProperty(
                "ActiveMembersVC", "MemberShipActivity"
            )
        } else if (intent.getStringExtra("MEMBERS") == "INACTIVE_MEMBERS") {
            header_title.text = "Inactive " + getString(R.string.members)
            SHOW_HIDE = "INACTIVE"
            sessionManager.firebaseAnalytics.setUserId("InactiveMembersVC")
            sessionManager.firebaseAnalytics.setUserProperty(
                "InactiveMembersVC", "MemberShipActivity"
            )
        } else if (intent.getStringExtra("MEMBERS") == "REJECTED_MEMBERS") {
            header_title.text = "Rejected " + getString(R.string.members)
            SHOW_HIDE = "REJECT"
            sessionManager.firebaseAnalytics.setUserId("RejectedMembersVC")
            sessionManager.firebaseAnalytics.setUserProperty(
                "RejectedMembersVC", "MemberShipActivity"
            )
        }

        back_arrow.setOnClickListener {
            finish()
        }

        data_not_found_layout = findViewById(R.id.data_not_found_layout)
        add_family_layout = findViewById(R.id.add_family_layout)
        rootview = findViewById(R.id.rootview)
        search_fields = findViewById(R.id.search_fields)
        registration_success_btn = findViewById(R.id.registration_success_btn)

        registration_success_btn.text = getString(R.string.add_sankhya)

        my_family_list = findViewById(R.id.my_family_list)

        /*my_family_list.layoutManager = LinearLayoutManager(
            this,
            LinearLayout.VERTICAL,
            false
        )*/

        mLayoutManager = LinearLayoutManager(this@MemberShipActivity)
        my_family_list.layoutManager = mLayoutManager

        if (intent.getStringExtra("MEMBERS") == "ALL_MEMBERS") {

            val end: Int = 100
            val start: Int = 0

            if (Functions.isConnectingToInternet(this@MemberShipActivity)) {
                USERID = sessionManager.fetchUserID()!!
                Log.d("USERID", USERID)
                TAB = "family"
                MEMBERID = sessionManager.fetchMEMBERID()!!
//                STATUS = "all"
                STATUS = "0"
                LENGTH = end.toString()
                START = start.toString()
                SEARCH = ""
                CHAPTERID = ""
                myMemberList(USERID, TAB, MEMBERID, STATUS, LENGTH, START, SEARCH, CHAPTERID)
            } else {
                Toast.makeText(
                    this@MemberShipActivity,
                    resources.getString(R.string.no_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else if (intent.getStringExtra("MEMBERS") == "ACTIVE_MEMBERS") {

            val end: Int = 100
            val start: Int = 0

            if (Functions.isConnectingToInternet(this@MemberShipActivity)) {
                USERID = sessionManager.fetchUserID()!!
                Log.d("USERID", USERID)
                TAB = "family"
                MEMBERID = sessionManager.fetchMEMBERID()!!
                STATUS = "1"
                LENGTH = end.toString()
                START = start.toString()
                SEARCH = ""
                CHAPTERID = ""
                myMemberList(USERID, TAB, MEMBERID, STATUS, LENGTH, START, SEARCH, CHAPTERID)
            } else {
                Toast.makeText(
                    this@MemberShipActivity,
                    resources.getString(R.string.no_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else if (intent.getStringExtra("MEMBERS") == "INACTIVE_MEMBERS") {

            val end: Int = 100
            val start: Int = 0

            if (Functions.isConnectingToInternet(this@MemberShipActivity)) {
                USERID = sessionManager.fetchUserID()!!
                Log.d("USERID", USERID)
                TAB = "family"
                MEMBERID = sessionManager.fetchMEMBERID()!!
                STATUS = "4"
                LENGTH = end.toString()
                START = start.toString()
                SEARCH = ""
                CHAPTERID = ""
                myMemberList(USERID, TAB, MEMBERID, STATUS, LENGTH, START, SEARCH, CHAPTERID)
            } else {
                Toast.makeText(
                    this@MemberShipActivity,
                    resources.getString(R.string.no_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else if (intent.getStringExtra("MEMBERS") == "REJECTED_MEMBERS") {

            val end: Int = 100
            val start: Int = 0

            if (Functions.isConnectingToInternet(this@MemberShipActivity)) {
                USERID = sessionManager.fetchUserID()!!
                Log.d("USERID", USERID)
                TAB = "family"
                MEMBERID = sessionManager.fetchMEMBERID()!!
                STATUS = "3"
                LENGTH = end.toString()
                START = start.toString()
                SEARCH = ""
                CHAPTERID = ""
                myMemberList(USERID, TAB, MEMBERID, STATUS, LENGTH, START, SEARCH, CHAPTERID)
            } else {
                Toast.makeText(
                    this@MemberShipActivity,
                    resources.getString(R.string.no_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        /*my_family_list.setOnScrollListener(object : EndLessScroll(mLayoutManager) {
            override fun loadMore(current_page: Int) {

                if (intent.getStringExtra("MEMBERS") == "ALL_MEMBERS") {

                    var end:Int = 100
                    var start:Int = 0

                    start = end + 1
                    end += 100

                    if (Functions.isConnectingToInternet(this@MemberShipActivity)) {
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
                            this@MemberShipActivity,
                            resources.getString(R.string.no_connection),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else if (intent.getStringExtra("MEMBERS") == "ACTIVE_MEMBERS") {

                    var end:Int = 100
                    var start:Int = 0

                    start = end + 1
                    end += 100

                    if (Functions.isConnectingToInternet(this@MemberShipActivity)) {
                        USERID = sessionManager.fetchUserID()!!
                        Log.d("USERID", USERID)
                        TAB = "family"
                        MEMBERID = sessionManager.fetchMEMBERID()!!
                        STATUS = "1"
                        LENGTH = end.toString()
                        START = start.toString()
                        SEARCH = ""
                        CHAPTERID = ""
                        myMemberList(USERID, TAB, MEMBERID, STATUS, LENGTH, START, SEARCH, CHAPTERID)
                    } else {
                        Toast.makeText(
                            this@MemberShipActivity,
                            resources.getString(R.string.no_connection),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else if (intent.getStringExtra("MEMBERS") == "INACTIVE_MEMBERS") {

                    var end:Int = 100
                    var start:Int = 0

                    start = end + 1
                    end += 100

                    if (Functions.isConnectingToInternet(this@MemberShipActivity)) {
                        USERID = sessionManager.fetchUserID()!!
                        Log.d("USERID", USERID)
                        TAB = "family"
                        MEMBERID = sessionManager.fetchMEMBERID()!!
                        STATUS = "4"
                        LENGTH = end.toString()
                        START = start.toString()
                        SEARCH = ""
                        CHAPTERID = ""
                        myMemberList(USERID, TAB, MEMBERID, STATUS, LENGTH, START, SEARCH, CHAPTERID)
                    } else {
                        Toast.makeText(
                            this@MemberShipActivity,
                            resources.getString(R.string.no_connection),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else if (intent.getStringExtra("MEMBERS") == "REJECTED_MEMBERS") {

                    var end:Int = 100
                    var start:Int = 0

                    start = end + 1
                    end += 100

                    if (Functions.isConnectingToInternet(this@MemberShipActivity)) {
                        USERID = sessionManager.fetchUserID()!!
                        Log.d("USERID", USERID)
                        TAB = "family"
                        MEMBERID = sessionManager.fetchMEMBERID()!!
                        STATUS = "3"
                        LENGTH = end.toString()
                        START = start.toString()
                        SEARCH = ""
                        CHAPTERID = ""
                        myMemberList(USERID, TAB, MEMBERID, STATUS, LENGTH, START, SEARCH, CHAPTERID)
                    } else {
                        Toast.makeText(
                            this@MemberShipActivity,
                            resources.getString(R.string.no_connection),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })*/

        val bestCities =
            listOf("Lahore", "Berlin", "Lisbon", "Tokyo", "Toronto", "Sydney", "Osaka", "Istanbul")
        val adapter = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, bestCities
        )

        search_fields.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (intent.getStringExtra("MEMBERS") == "ALL_MEMBERS") {

                    val end: Int = 100
                    val start: Int = 0

                    if (Functions.isConnectingToInternet(this@MemberShipActivity)) {
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
                            USERID, TAB, MEMBERID, STATUS, LENGTH, START, SEARCH, CHAPTERID
                        )
                    } else {
                        Toast.makeText(
                            this@MemberShipActivity,
                            resources.getString(R.string.no_connection),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else if (intent.getStringExtra("MEMBERS") == "ACTIVE_MEMBERS") {

                    val end: Int = 100
                    val start: Int = 0

                    if (Functions.isConnectingToInternet(this@MemberShipActivity)) {
                        USERID = sessionManager.fetchUserID()!!
                        Log.d("USERID", USERID)
                        TAB = "family"
                        MEMBERID = sessionManager.fetchMEMBERID()!!
                        STATUS = "1"
                        LENGTH = end.toString()
                        START = start.toString()
                        SEARCH = s.toString()
                        CHAPTERID = ""
                        mySearchMemberList(
                            USERID, TAB, MEMBERID, STATUS, LENGTH, START, SEARCH, CHAPTERID
                        )
                    } else {
                        Toast.makeText(
                            this@MemberShipActivity,
                            resources.getString(R.string.no_connection),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else if (intent.getStringExtra("MEMBERS") == "INACTIVE_MEMBERS") {

                    val end: Int = 100
                    val start: Int = 0

                    if (Functions.isConnectingToInternet(this@MemberShipActivity)) {
                        USERID = sessionManager.fetchUserID()!!
                        Log.d("USERID", USERID)
                        TAB = "family"
                        MEMBERID = sessionManager.fetchMEMBERID()!!
                        STATUS = "4"
                        LENGTH = end.toString()
                        START = start.toString()
                        SEARCH = s.toString()
                        CHAPTERID = ""
                        mySearchMemberList(
                            USERID, TAB, MEMBERID, STATUS, LENGTH, START, SEARCH, CHAPTERID
                        )
                    } else {
                        Toast.makeText(
                            this@MemberShipActivity,
                            resources.getString(R.string.no_connection),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else if (intent.getStringExtra("MEMBERS") == "REJECTED_MEMBERS") {

                    val end: Int = 100
                    val start: Int = 0

                    if (Functions.isConnectingToInternet(this@MemberShipActivity)) {
                        USERID = sessionManager.fetchUserID()!!
                        Log.d("USERID", USERID)
                        TAB = "family"
                        MEMBERID = sessionManager.fetchMEMBERID()!!
                        STATUS = "3"
                        LENGTH = end.toString()
                        START = start.toString()
                        SEARCH = s.toString()
                        CHAPTERID = ""
                        mySearchMemberList(
                            USERID, TAB, MEMBERID, STATUS, LENGTH, START, SEARCH, CHAPTERID
                        )
                    } else {
                        Toast.makeText(
                            this@MemberShipActivity,
                            resources.getString(R.string.no_connection),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        add_family_layout.setOnClickListener {
//            Snackbar.make(rootview, "Add Sankhya", Snackbar.LENGTH_SHORT).show()
            startActivity(Intent(this@MemberShipActivity, AddSankhyaActivity::class.java))
            /*val i = Intent(this, AddMemberFirstActivity::class.java)
            i.putExtra("TYPE_SELF", "family");
            startActivity(i)*/
        }

    }

    private fun myMemberList(
        user_id: String,
        tab: String,
        member_id: String,
        status: String,
        length: String,
        start: String,
        search: String,
        chapter_id: String
    ) {
        val pd = CustomProgressBar(this@MemberShipActivity)
        pd.show()
        val call: Call<Get_Member_Listing_Response> =
            MyHssApplication.instance!!.api.get_member_listing(
                user_id, tab, member_id, status, length, start, search, chapter_id
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

                            mAdapterGuru = MembersCustomAdapter(atheletsBeans, SHOW_HIDE)

                            my_family_list.adapter = mAdapterGuru
                            mAdapterGuru!!.notify(atheletsBeans)
                            mAdapterGuru!!.notifyDataSetChanged()

                        } catch (e: ArithmeticException) {
                            println(e)
                        } finally {
                            println("Family")
                        }
                    } else {
                        data_not_found_layout.visibility = View.VISIBLE
//                        Functions.displayMessage(this@MemberShipActivity,response.body()?.message)
//                        Functions.showAlertMessageWithOK(
//                            this@MemberShipActivity, "",
////                        "Message",
//                            response.body()?.message
//                        )
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@MemberShipActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Get_Member_Listing_Response>, t: Throwable) {
                Toast.makeText(this@MemberShipActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    private fun mySearchMemberList(
        user_id: String,
        tab: String,
        member_id: String,
        status: String,
        length: String,
        start: String,
        search: String,
        chapter_id: String
    ) {
//        val pd = CustomProgressBar(this@MemberShipActivity)
//        pd.show()
        val call: Call<Get_Member_Listing_Response> =
            MyHssApplication.instance!!.api.get_member_listing(
                user_id, tab, member_id, status, length, start, search, chapter_id
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

                            mAdapterGuru = MembersCustomAdapter(atheletsBeans, SHOW_HIDE)

                            my_family_list.adapter = mAdapterGuru
                            mAdapterGuru!!.notify(atheletsBeans)
                            mAdapterGuru!!.notifyDataSetChanged()

                        } catch (e: ArithmeticException) {
                            println(e)
                        } finally {
                            println("Family")
                        }
                    } else {
                        Functions.displayMessage(this@MemberShipActivity, response.body()?.message)
//                        Functions.showAlertMessageWithOK(
//                            this@MemberShipActivity, "",
////                        "Message",
//                            response.body()?.message
//                        )
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@MemberShipActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
//                pd.dismiss()
            }

            override fun onFailure(call: Call<Get_Member_Listing_Response>, t: Throwable) {
                Toast.makeText(this@MemberShipActivity, t.message, Toast.LENGTH_LONG).show()
//                pd.dismiss()
            }
        })
    }

}

class MembersCustomAdapter(var userList: List<Get_Member_Listing_Datum>, val SHOW_HIDE: String) :
    RecyclerView.Adapter<MembersCustomAdapter.ViewHolder>() {

    lateinit var adapter_view: LinearLayout

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.members_list_layout, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(userList[position], SHOW_HIDE)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    fun notify(user_List: List<Get_Member_Listing_Datum>) {
        userList = user_List
        notifyDataSetChanged()
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var sessionManager: SessionManager
        lateinit var active_txt: TextView
        lateinit var reject_txt: TextView
        lateinit var reject_layout: LinearLayout
        lateinit var active_layout: LinearLayout

        @SuppressLint("SetTextI18n")
        fun bindItems(my_family_DatumGurudakshina: Get_Member_Listing_Datum, SHOW_HIDE: String) {

            val context: Context? = null
            sessionManager = SessionManager(itemView.context)

            val active_inactive_view =
                itemView.findViewById(R.id.active_inactive_view) as RelativeLayout
            active_txt = itemView.findViewById(R.id.active_txt)
            reject_txt = itemView.findViewById(R.id.reject_txt)
            active_layout = itemView.findViewById(R.id.active_layout)
            reject_layout = itemView.findViewById(R.id.reject_layout)
            val family_name = itemView.findViewById(R.id.family_name) as TextView
            val family_address = itemView.findViewById(R.id.family_address) as TextView
            val active_inactive_txt = itemView.findViewById(R.id.active_inactive_txt) as TextView
            val email_txt = itemView.findViewById(R.id.email_txt) as TextView
            val call_txt = itemView.findViewById(R.id.call_txt) as TextView
            val email_img = itemView.findViewById(R.id.email_img) as ImageView
            val call_img = itemView.findViewById(R.id.call_img) as ImageView
//            val active_inactive_img  = itemView.findViewById(R.id.active_inactive_img) as ImageView
            val righr_menu = itemView.findViewById(R.id.righr_menu) as ImageView
            val adapter_view = itemView.findViewById(R.id.adapter_view) as LinearLayout
            if (my_family_DatumGurudakshina.middleName != "") {
                family_name.text =
                    my_family_DatumGurudakshina.firstName!!.capitalize(Locale.ROOT) + " " + my_family_DatumGurudakshina.middleName!!.capitalize(
                        Locale.ROOT
                    ) + " " + my_family_DatumGurudakshina.lastName!!.capitalize(Locale.ROOT)
            } else {
                family_name.text =
                    my_family_DatumGurudakshina.firstName!!.capitalize(Locale.ROOT) + " " + my_family_DatumGurudakshina.lastName!!.capitalize(
                        Locale.ROOT
                    )
            }
            family_address.text = my_family_DatumGurudakshina.chapterName!!.capitalize(Locale.ROOT)
            email_txt.text = my_family_DatumGurudakshina.email!!
            call_txt.text = my_family_DatumGurudakshina.mobile

//            active_inactive_view.visibility = View.GONE
//            active_txt.visibility = View.GONE
//            reject_txt.visibility = View.GONE

            active_inactive_txt.text = my_family_DatumGurudakshina.ageCategories

            if (sessionManager.fetchUSERROLE()?.contains("Sankhya Pramukh", false) == true) {
//            if (true) {
            Log.e("Role : ", " Role : " + sessionManager.fetchUSERROLE())

                righr_menu.visibility = View.GONE
            } else {
                righr_menu.visibility = View.VISIBLE
            }

            if (SHOW_HIDE == "ALLSHOW") {
//                if (my_family_DatumGurudakshina.status.toString()!! == "0") {
//                    active_txt.visibility = View.VISIBLE
//                    reject_txt.visibility = View.VISIBLE
//                    reject_layout.visibility = View.VISIBLE
//                    active_layout.visibility = View.VISIBLE
//                } else {
//                    active_txt.visibility = View.GONE
//                    reject_txt.visibility = View.GONE
//                    reject_layout.visibility = View.GONE
//                    active_layout.visibility = View.GONE
//                }
                active_txt.visibility = View.VISIBLE
                reject_txt.visibility = View.VISIBLE
                reject_layout.visibility = View.VISIBLE
                active_layout.visibility = View.VISIBLE
            } else if (SHOW_HIDE == "ACTIVE") {
                active_txt.visibility = View.GONE
                reject_txt.visibility = View.VISIBLE
                reject_layout.visibility = View.VISIBLE
                active_layout.visibility = View.GONE
            } else if (SHOW_HIDE == "INACTIVE") {
                active_txt.visibility = View.VISIBLE
                reject_txt.visibility = View.GONE
                reject_layout.visibility = View.GONE
                active_layout.visibility = View.VISIBLE
            } else if (SHOW_HIDE == "REJECT") {
                active_txt.visibility = View.GONE
                reject_txt.visibility = View.GONE
                reject_layout.visibility = View.GONE
                active_layout.visibility = View.GONE
            }

            active_txt.setOnClickListener {
//                Toast.makeText(itemView.context, "Approved", Toast.LENGTH_SHORT).show()
                val alertDialog: AlertDialog.Builder = AlertDialog.Builder(itemView.context)
//            alertDialog.setTitle("Logout")
                alertDialog.setMessage("Are you sure you want to approve this member?")
                alertDialog.setPositiveButton(
                    "yes"
                ) { _, _ ->
                    if (Functions.isConnectingToInternet(itemView.context)) {
                        myApproved(
                            sessionManager.fetchUserID()!!, my_family_DatumGurudakshina.memberId!!
                        )
                    } else {
                        Toast.makeText(
                            itemView.context,
                            itemView.context.resources.getString(R.string.no_connection),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                alertDialog.setNegativeButton(
                    "No"
                ) { _, _ ->

                }
                val alert: AlertDialog = alertDialog.create()
                alert.setCanceledOnTouchOutside(false)
                alert.show()

                /*if (Functions.isConnectingToInternet(itemView.context)) {
                    myActive(sessionManager.fetchUserID()!!, my_family_DatumGurudakshina.memberId!!)
                } else {
                    Toast.makeText(
                        itemView.context,
                        itemView.context.resources.getString(R.string.no_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }*/
            }

            reject_txt.text = itemView.context.getString(R.string.inactive_txt)

            reject_txt.setOnClickListener {
//                Toast.makeText(itemView.context, "Rejected", Toast.LENGTH_SHORT).show()
                val alertDialog: AlertDialog.Builder = AlertDialog.Builder(itemView.context)
//            alertDialog.setTitle("Logout")
                alertDialog.setMessage("Are you sure you want to inactive this member?")
                alertDialog.setPositiveButton(
                    "yes"
                ) { _, _ ->
                    if (Functions.isConnectingToInternet(itemView.context)) {
                        myInactive(
                            sessionManager.fetchUserID()!!, my_family_DatumGurudakshina.memberId!!
                        )
//                    myRejected(
//                        sessionManager.fetchUserID()!!,
//                        my_family_DatumGurudakshina.memberId!!
//                    )
                    } else {
                        Toast.makeText(
                            itemView.context,
                            itemView.context.resources.getString(R.string.no_connection),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                alertDialog.setNegativeButton(
                    "No"
                ) { _, _ ->

                }
                val alert: AlertDialog = alertDialog.create()
                alert.setCanceledOnTouchOutside(false)
                alert.show()
            }

            righr_menu.setOnClickListener {
                val alertDialog: AlertDialog.Builder = AlertDialog.Builder(itemView.context)
//            alertDialog.setTitle("Logout")
                alertDialog.setMessage("Are you sure you want to delete this member?")
                alertDialog.setPositiveButton(
                    "yes"
                ) { _, _ ->
                    if (Functions.isConnectingToInternet(itemView.context)) {
                        myDelete(
                            sessionManager.fetchUserID()!!, my_family_DatumGurudakshina.memberId!!
                        )
                    } else {
                        Toast.makeText(
                            itemView.context,
                            itemView.context.resources.getString(R.string.no_connection),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                alertDialog.setNegativeButton(
                    "No"
                ) { _, _ ->

                }
                val alert: AlertDialog = alertDialog.create()
                alert.setCanceledOnTouchOutside(false)
                alert.show()
            }

//            righr_menu.setOnClickListener {
//                val popup = PopupMenu(itemView.context, itemView, Gravity.NO_GRAVITY)
//                popup.inflate(R.menu.shakha_header_menu)
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                    popup.gravity = Gravity.END
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
//                        popup.setForceShowIcon(true)
//                    }
//                }
//                popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
//                    when (item!!.itemId) {
//                        R.id.approve -> {
//                            Toast.makeText(itemView.context, item.title, Toast.LENGTH_SHORT).show()
//                            if (Functions.isConnectingToInternet(itemView.context)) {
//                                myApproved(sessionManager.fetchUserID()!!, my_family_DatumGurudakshina.memberId!!)
//                            } else {
//                                Toast.makeText(
//                                    itemView.context,
//                                    itemView.context.resources.getString(R.string.no_connection),
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        }
//                        R.id.edit -> {
//                            Toast.makeText(itemView.context, item.title, Toast.LENGTH_SHORT).show()
//                        }
//                        R.id.delete -> {
//                            Toast.makeText(itemView.context, item.title, Toast.LENGTH_SHORT).show()
//                            if (Functions.isConnectingToInternet(itemView.context)) {
//                                myDelete(sessionManager.fetchUserID()!!, my_family_DatumGurudakshina.memberId!!)
//                            } else {
//                                Toast.makeText(
//                                    itemView.context,
//                                    itemView.context.resources.getString(R.string.no_connection),
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        }
//                    }
//                    true
//                })
//                popup.show()
//            }

            adapter_view.setOnClickListener {
//                Toast.makeText(itemView.context, "Adapter", Toast.LENGTH_SHORT).show()
//                itemView.context.startActivity(Intent(itemView.context, SankhyaDetail::class.java))

//                val i = Intent(itemView.context, SankhyaDetail::class.java)
//                i.putExtra("SANKHYA", "SANKHYA")
//                i.putExtra("SANKHYA_ID", my_family_DatumGurudakshina.memberId)
//                itemView.context.startActivity(i)
            }

            call_img.setOnClickListener {
                val call: Uri = Uri.parse("tel:" + call_txt.text.toString())
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = call
                if (ActivityCompat.checkSelfPermission(
                        itemView.context, Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    itemView.context.startActivity(intent)
                }
            }
        }

        /*For Approved API*/
        private fun myApproved(
            user_id: String, member_id: String
        ) {
            val pd = CustomProgressBar(itemView.context)
            pd.show()
            val call: Call<Get_Member_Approve_Response> =
                MyHssApplication.instance!!.api.get_member_approve(
                    user_id, member_id
                )
            call.enqueue(object : Callback<Get_Member_Approve_Response> {
                override fun onResponse(
                    call: Call<Get_Member_Approve_Response>,
                    response: Response<Get_Member_Approve_Response>
                ) {
                    if (response.code() == 200 && response.body() != null) {
                        Log.d("status", response.body()?.status.toString())
                        if (response.body()?.status!!) {

                            try {
                                val alertBuilder =
                                    AlertDialog.Builder(itemView.context) // , R.style.dialog_custom

//                            alertBuilder.setTitle("Message")
                                alertBuilder.setMessage(response.body()?.message)
                                alertBuilder.setPositiveButton(
                                    "OK"
                                ) { dialog, which ->

                                    val i =
                                        Intent(itemView.context, LinkedFamilyFragment::class.java)
                                    i.putExtra("DashBoard", "SHAKHAVIEW")
                                    i.putExtra(
                                        "headerName", itemView.context.getString(R.string.my_shakha)
                                    )
                                    itemView.context.startActivity(i)

                                    (itemView.context as AppCompatActivity).finishAffinity()

//                                    itemView.context.startActivity(
//                                        Intent(
//                                            itemView.context,
//                                            HomeActivity::class.java
//                                        )
//                                    )
//                                    MainActivity::class.java))
                                }
                                val alertDialog = alertBuilder.create()
                                alertDialog.show()
                            } catch (e: ArithmeticException) {
                                println(e)
                            } finally {
                                println("Family")
                            }
                        } else {
                            Functions.displayMessage(itemView.context, response.body()?.message)
//                            Functions.showAlertMessageWithOK(
//                                itemView.context, "",
////                            "Message",
//                                response.body()?.message
//                            )
                        }
                    } else {
                        Functions.showAlertMessageWithOK(
                            itemView.context, "Message",
                            itemView.context.getString(R.string.some_thing_wrong),
                        )
                    }
                    pd.dismiss()
                }

                override fun onFailure(call: Call<Get_Member_Approve_Response>, t: Throwable) {
                    Toast.makeText(itemView.context, t.message, Toast.LENGTH_LONG).show()
                    pd.dismiss()
                }
            })
        }

        /*For Delete API*/
        private fun myDelete(
            user_id: String, member_id: String
        ) {
            val pd = CustomProgressBar(itemView.context)
            pd.show()
            val call: Call<Get_Member_Delete_Response> =
                MyHssApplication.instance!!.api.get_member_delete(
                    user_id, member_id
                )
            call.enqueue(object : Callback<Get_Member_Delete_Response> {
                override fun onResponse(
                    call: Call<Get_Member_Delete_Response>,
                    response: Response<Get_Member_Delete_Response>
                ) {
                    if (response.code() == 200 && response.body() != null) {
                        Log.d("status", response.body()?.status.toString())
                        if (response.body()?.status!!) {

                            try {
                                val alertBuilder =
                                    AlertDialog.Builder(itemView.context) // , R.style.dialog_custom

//                            alertBuilder.setTitle("Message")
                                alertBuilder.setMessage(response.body()?.message)
                                alertBuilder.setPositiveButton(
                                    "OK"
                                ) { dialog, which ->
                                    val i =
                                        Intent(itemView.context, LinkedFamilyFragment::class.java)
                                    i.putExtra("DashBoard", "SHAKHAVIEW")
                                    i.putExtra(
                                        "headerName", itemView.context.getString(R.string.my_shakha)
                                    )
                                    itemView.context.startActivity(i)
                                    (itemView.context as AppCompatActivity).finishAffinity()
//                                    itemView.context.startActivity(
//                                        Intent(
//                                            itemView.context,
//                                            HomeActivity::class.java
//                                        )
//                                    )
//                                    MainActivity::class.java))
                                }
                                val alertDialog = alertBuilder.create()
                                alertDialog.show()
                            } catch (e: ArithmeticException) {
                                println(e)
                            } finally {
                                println("Family")
                            }
                        } else {
                            Functions.displayMessage(itemView.context, response.body()?.message)
//                            Functions.showAlertMessageWithOK(
//                                itemView.context, "",
////                            "Message",
//                                response.body()?.message
//                            )
                        }
                    } else {
                        Functions.showAlertMessageWithOK(
                            itemView.context, "Message",
                            itemView.context.getString(R.string.some_thing_wrong),
                        )
                    }
                    pd.dismiss()
                }

                override fun onFailure(call: Call<Get_Member_Delete_Response>, t: Throwable) {
                    Toast.makeText(itemView.context, t.message, Toast.LENGTH_LONG).show()
                    pd.dismiss()
                }
            })
        }

        /*For Reject API*/
        private fun myRejected(
            user_id: String, member_id: String
        ) {
            val pd = CustomProgressBar(itemView.context)
            pd.show()
            val call: Call<Get_Member_Reject_Response> =
                MyHssApplication.instance!!.api.get_member_reject(
                    user_id, member_id
                )
            call.enqueue(object : Callback<Get_Member_Reject_Response> {
                override fun onResponse(
                    call: Call<Get_Member_Reject_Response>,
                    response: Response<Get_Member_Reject_Response>
                ) {
                    if (response.code() == 200 && response.body() != null) {
                        Log.d("status", response.body()?.status.toString())
                        if (response.body()?.status!!) {

                            try {
                                Functions.showAlertMessageWithOK(
                                    itemView.context, "",
//                                "Message",
                                    response.body()?.message
                                )
                            } catch (e: ArithmeticException) {
                                println(e)
                            } finally {
                                println("Family")
                            }
                        } else {
                            Functions.showAlertMessageWithOK(
                                itemView.context, "",
//                            "Message",
                                response.body()?.message
                            )
                        }
                    } else {
                        Functions.showAlertMessageWithOK(
                            itemView.context, "Message",
                            itemView.context.getString(R.string.some_thing_wrong),
                        )
                    }
                    pd.dismiss()
                }

                override fun onFailure(call: Call<Get_Member_Reject_Response>, t: Throwable) {
                    Toast.makeText(itemView.context, t.message, Toast.LENGTH_LONG).show()
                    pd.dismiss()
                }
            })
        }

        /*For Active API*/
        private fun myActive(
            user_id: String, member_id: String
        ) {
            val pd = CustomProgressBar(itemView.context)
            pd.show()
            val call: Call<Get_Member_Active_Response> =
                MyHssApplication.instance!!.api.get_member_active(
                    user_id, member_id
                )
            call.enqueue(object : Callback<Get_Member_Active_Response> {
                override fun onResponse(
                    call: Call<Get_Member_Active_Response>,
                    response: Response<Get_Member_Active_Response>
                ) {
                    if (response.code() == 200 && response.body() != null) {
                        Log.d("status", response.body()?.status.toString())
                        if (response.body()?.status!!) {

                            try {
                                Functions.showAlertMessageWithOK(
                                    itemView.context, "",
//                                "Message",
                                    response.body()?.message
                                )
                            } catch (e: ArithmeticException) {
                                println(e)
                            } finally {
                                println("Family")
                            }
                        } else {
                            Functions.showAlertMessageWithOK(
                                itemView.context, "",
//                            "Message",
                                response.body()?.message
                            )
                        }
                    } else {
                        Functions.showAlertMessageWithOK(
                            itemView.context, "Message",
                            itemView.context.getString(R.string.some_thing_wrong),
                        )
                    }
                    pd.dismiss()
                }

                override fun onFailure(call: Call<Get_Member_Active_Response>, t: Throwable) {
                    Toast.makeText(itemView.context, t.message, Toast.LENGTH_LONG).show()
                    pd.dismiss()
                }
            })
        }


        /*For Inactive API*/
        private fun myInactive(
            user_id: String, member_id: String
        ) {
            val pd = CustomProgressBar(itemView.context)
            pd.show()
            val call: Call<Get_Member_Inactive_Response> =
                MyHssApplication.instance!!.api.get_member_inactive(
                    user_id, member_id
                )
            call.enqueue(object : Callback<Get_Member_Inactive_Response> {
                override fun onResponse(
                    call: Call<Get_Member_Inactive_Response>,
                    response: Response<Get_Member_Inactive_Response>
                ) {
                    if (response.code() == 200 && response.body() != null) {
                        Log.d("status", response.body()?.status.toString())
                        if (response.body()?.status!!) {

                            try {
                                val alertBuilder =
                                    AlertDialog.Builder(itemView.context) // , R.style.dialog_custom

//                            alertBuilder.setTitle("Message")
                                alertBuilder.setMessage(response.body()?.message)
                                alertBuilder.setPositiveButton(
                                    "OK"
                                ) { dialog, which ->
                                    val i =
                                        Intent(itemView.context, LinkedFamilyFragment::class.java)
                                    i.putExtra("DashBoard", "SHAKHAVIEW")
                                    i.putExtra(
                                        "headerName", itemView.context.getString(R.string.my_shakha)
                                    )
                                    itemView.context.startActivity(i)
                                    (itemView.context as AppCompatActivity).finishAffinity()
//                                    itemView.context.startActivity(
//                                        Intent(
//                                            itemView.context,
//                                            HomeActivity::class.java
//                                        )
//                                    )
//                                    MainActivity::class.java))
                                }
                                val alertDialog = alertBuilder.create()
                                alertDialog.show()
                            } catch (e: ArithmeticException) {
                                println(e)
                            } finally {
                                println("Family")
                            }
                        } else {
                            Functions.displayMessage(itemView.context, response.body()?.message)
//                            Functions.showAlertMessageWithOK(
//                                itemView.context, "",
////                            "Message",
//                                response.body()?.message
//                            )
                        }
                    } else {
                        Functions.showAlertMessageWithOK(
                            itemView.context, "Message",
                            itemView.context.getString(R.string.some_thing_wrong),
                        )
                    }
                    pd.dismiss()
                }

                override fun onFailure(call: Call<Get_Member_Inactive_Response>, t: Throwable) {
                    Toast.makeText(itemView.context, t.message, Toast.LENGTH_LONG).show()
                    pd.dismiss()
                }
            })
        }
    }

}