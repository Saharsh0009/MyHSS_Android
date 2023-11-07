package com.uk.myhss.ui.dashboard

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.DebugLog
import com.myhss.Utils.Functions
import com.myhss.Utils.UtilCommon
import com.myhss.ui.suryanamaskar.Model.BarchartDataModel
import com.myhss.ui.suryanamaskar.ViewBarchartActivity
import com.uk.myhss.AddMember.AddMemberFirstActivity
import com.uk.myhss.Guru_Dakshina_OneTime.GuruDakshinaOneTimeFirstActivity
import com.uk.myhss.Guru_Dakshina_Regular.GuruDakshinaRegularFirstActivity
import com.uk.myhss.Main.HomeActivity
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.ui.linked_family.MemberShipActivity

import com.uk.myhss.ui.linked_family.Model.Get_Member_Listing_Datum
import com.uk.myhss.ui.linked_family.Model.Get_Member_Listing_Response
import com.uk.myhss.ui.my_family.Adapter.CustomAdapter
import com.uk.myhss.ui.my_family.Model.Datum_guru_dakshina
import com.uk.myhss.ui.my_family.Model.guru_dakshina_response
import com.uk.myhss.ui.policies.SankhyaActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class LinkedFamilyFragment : AppCompatActivity(), OnChartValueSelectedListener {
    private lateinit var sessionManager: SessionManager
    lateinit var home_layout: RelativeLayout

    lateinit var myfamily_layout: LinearLayout
    lateinit var myshakha_layout: LinearLayout
    lateinit var gurudakshina_layout: LinearLayout
    lateinit var layout_pieChart_lable: LinearLayout

    lateinit var mLayoutManager: LinearLayoutManager
    lateinit var data_not_found_layout: RelativeLayout

    var dialog: Dialog? = null
    lateinit var back_arrow: ImageView
    lateinit var header_title: TextView
    lateinit var shakha_count_txt: TextView
    lateinit var shakha_count_layout: RelativeLayout
    lateinit var info_tooltip: ImageView

    lateinit var myfamily_view: TextView
    lateinit var myfamily_line: ImageView
    lateinit var myshakha_view: TextView
    lateinit var myshakha_line: ImageView
    lateinit var gurudakshina_view: TextView
    lateinit var gurudakshina_line: ImageView

    lateinit var member_count_layout: RelativeLayout
    lateinit var member_count: TextView

    /*Start My Family*/
    lateinit var my_family_list: RecyclerView

    lateinit var add_family_layout: LinearLayout
    lateinit var rootview: LinearLayout
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
    private var mAdapterGuru: CustomAdapter? = null/*End My Family*/


    /*Start My Shakha*/
    lateinit var membership_view: RelativeLayout
    lateinit var active_membership_view: RelativeLayout
    lateinit var inactive_member: RelativeLayout
    lateinit var rejected_member: RelativeLayout
    lateinit var sankhya_layout: RelativeLayout
    lateinit var root_view: LinearLayout
    lateinit var shakha_layout: RelativeLayout/*End My Shakha*/

    lateinit var one_time_layout: LinearLayout
    lateinit var regular_layout: LinearLayout
    lateinit var guru_dakshina_layout: RelativeLayout

    private var guru_dakshinaBeans: List<Datum_guru_dakshina> = ArrayList<Datum_guru_dakshina>()
    lateinit var chipGroup: ChipGroup
    lateinit var pieChart_guru: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_dashboard)
        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("MembershipApplicationsVC")
        sessionManager.firebaseAnalytics.setUserProperty(
            "MembershipApplicationsVC", "LinkedFamilyFragment"
        )
        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)

        back_arrow = findViewById(R.id.back_arrow)
        header_title = findViewById(R.id.header_title)
        header_title.text = intent.getStringExtra("headerName")
//        DebugLog.e("Linked Family fragment")
        back_arrow.setOnClickListener(
            DebouncedClickListener {
                val i = Intent(this@LinkedFamilyFragment, HomeActivity::class.java)
                startActivity(i)
                finishAffinity()
            })
        data_not_found_layout = findViewById(R.id.data_not_found_layout)
        shakha_count_layout = findViewById(R.id.shakha_count_layout)
        shakha_count_txt = findViewById(R.id.shakha_count_txt)
        info_tooltip = findViewById(R.id.info_tooltip)
        myfamily_view = findViewById(R.id.myfamily_view)
        myfamily_line = findViewById(R.id.myfamily_line)
        myshakha_view = findViewById(R.id.myshakha_view)
        myshakha_line = findViewById(R.id.myshakha_line)
        gurudakshina_view = findViewById(R.id.gurudakshina_view)
        gurudakshina_line = findViewById(R.id.gurudakshina_line)
        myfamily_layout = findViewById(R.id.myfamily_layout)
        myshakha_layout = findViewById(R.id.myshakha_layout)
        gurudakshina_layout = findViewById(R.id.gurudakshina_layout)
        member_count_layout = findViewById(R.id.member_count_layout)
        member_count = findViewById(R.id.member_count)
        pieChart_guru = findViewById(R.id.pieChart_guru)
        layout_pieChart_lable = findViewById(R.id.layout_pieChart_lable)
        shakha_count_layout.setOnClickListener(DebouncedClickListener {
            val shakha_count = sessionManager.fetchSHAKHA_SANKHYA_AVG()!!
            val shakha_name = sessionManager.fetchSHAKHANAME()!!
            val nagar_name = sessionManager.fetchNAGARNAME()!!
            val vibhag_name = sessionManager.fetchVIBHAGNAME()!!
            ShakhaCountDialog(shakha_count, shakha_name, nagar_name, vibhag_name)
        })

        /*My Family*/
        add_family_layout = findViewById(R.id.add_family_layout)
        rootview = findViewById(R.id.rootview)
        family_layout = findViewById(R.id.family_layout)
        search_fields = findViewById(R.id.search_fields)

        my_family_list = findViewById(R.id.my_family_list)
        chipGroup = ChipGroup(this)

        mLayoutManager = LinearLayoutManager(this@LinkedFamilyFragment)
        my_family_list.layoutManager = mLayoutManager

        DebugLog.d("Dashboard => " + intent.getStringExtra("DashBoard").toString())
        if (intent.getStringExtra("DashBoard") == "SHAKHAVIEW") {
            Handler().postDelayed({ myshakha_view.callOnClick() }, 100)
            val end: Int = 100
            val start: Int = 0
            CallAPI(start, end, true)
        } else {
            val end: Int = 100
            val start: Int = 0
            CallAPI(start, end, false)
        }
        search_fields.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val end: Int = 100
                val start: Int = 0

                if (Functions.isConnectingToInternet(this@LinkedFamilyFragment)) {
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
                        this@LinkedFamilyFragment,
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

//        add_family_layout.visibility = View.GONE

        add_family_layout.setOnClickListener(DebouncedClickListener {
            val i = Intent(this@LinkedFamilyFragment, AddMemberFirstActivity::class.java)
            i.putExtra("TYPE_SELF", "family")
//                i.putExtra("FAMILY", "FAMILY")
            startActivity(i)
        })/*End*/

        /*My Shakha*/
        membership_view = findViewById(R.id.membership_view)
        active_membership_view = findViewById(R.id.active_membership_view)
        inactive_member = findViewById(R.id.inactive_member)
        rejected_member = findViewById(R.id.rejected_member)
        sankhya_layout = findViewById(R.id.sankhya_layout)
        root_view = findViewById(R.id.root_view)
        shakha_layout = findViewById(R.id.shakha_layout)

        rejected_member.visibility = View.VISIBLE

        membership_view.setOnClickListener(DebouncedClickListener {
//            Snackbar.make(root_view, "Member Ship", Snackbar.LENGTH_SHORT).show()
            val i = Intent(this@LinkedFamilyFragment, MemberShipActivity::class.java)
            i.putExtra("MEMBERS", "ALL_MEMBERS")
            startActivity(i)
        })

        active_membership_view.setOnClickListener(DebouncedClickListener {
//            Snackbar.make(root_view, "Active Member", Snackbar.LENGTH_SHORT).show()
            val i = Intent(this@LinkedFamilyFragment, MemberShipActivity::class.java)
            i.putExtra("MEMBERS", "ACTIVE_MEMBERS")
            startActivity(i)
        })

        inactive_member.setOnClickListener(DebouncedClickListener {
            val i = Intent(this@LinkedFamilyFragment, MemberShipActivity::class.java)
            i.putExtra("MEMBERS", "INACTIVE_MEMBERS")
            startActivity(i)
        })

        rejected_member.setOnClickListener(DebouncedClickListener {
//            Snackbar.make(root_view, "Rejected Member", Snackbar.LENGTH_SHORT).show()
            val i = Intent(this@LinkedFamilyFragment, MemberShipActivity::class.java)
            i.putExtra("MEMBERS", "REJECTED_MEMBERS")
            startActivity(i)
        })

        sankhya_layout.setOnClickListener(DebouncedClickListener {
            startActivity(Intent(this@LinkedFamilyFragment, SankhyaActivity::class.java))
        })/*End My Shakha*/

        /*Guru Dakshina*/
        one_time_layout = findViewById(R.id.one_time_layout)
        regular_layout = findViewById(R.id.regular_layout)
        guru_dakshina_layout = findViewById(R.id.guru_dakshina_layout)

        if (UtilCommon.isUserUnder18(sessionManager.fetchDOB().toString())) {
            one_time_layout.visibility = View.GONE
            regular_layout.visibility = View.GONE
        } else {
            one_time_layout.visibility = View.VISIBLE
            regular_layout.visibility = View.VISIBLE
        }

        one_time_layout.setOnClickListener(DebouncedClickListener {
            startActivity(
                Intent(
                    this@LinkedFamilyFragment, GuruDakshinaOneTimeFirstActivity::class.java
                )
            )
        })

        regular_layout.setOnClickListener(DebouncedClickListener {
            startActivity(
                Intent(
                    this@LinkedFamilyFragment, GuruDakshinaRegularFirstActivity::class.java
                )
            )
        })/*End*/

        if (sessionManager.fetchSHAKHA_TAB() == "yes") {
            myshakha_layout.visibility = View.VISIBLE
            shakha_count_layout.visibility = View.VISIBLE
            if (sessionManager.fetchSHAKHA_SANKHYA_AVG() != "") {
                shakha_count_txt.text = sessionManager.fetchSHAKHA_SANKHYA_AVG()!!
            } else {
                shakha_count_txt.visibility = View.GONE
            }
        } else {
            myshakha_layout.visibility = View.GONE
        }
        myfamily_line.visibility = View.VISIBLE
        myshakha_line.visibility = View.INVISIBLE
        gurudakshina_line.visibility = View.INVISIBLE

        myfamily_view.setTextColor(
            ContextCompat.getColor(
                this@LinkedFamilyFragment, R.color.primaryColor
            )
        )
        myshakha_view.setTextColor(
            ContextCompat.getColor(
                this@LinkedFamilyFragment, R.color.grayColorColor
            )
        )
        gurudakshina_view.setTextColor(
            ContextCompat.getColor(
                this@LinkedFamilyFragment, R.color.grayColorColor
            )
        )

        family_layout.visibility = View.VISIBLE
        shakha_layout.visibility = View.INVISIBLE
        guru_dakshina_layout.visibility = View.INVISIBLE

        myfamily_view.setOnClickListener(DebouncedClickListener {
            myfamily_line.visibility = View.VISIBLE
            myshakha_line.visibility = View.INVISIBLE
            gurudakshina_line.visibility = View.INVISIBLE

            family_layout.visibility = View.VISIBLE
            shakha_layout.visibility = View.INVISIBLE
            guru_dakshina_layout.visibility = View.INVISIBLE

            myfamily_view.setTextColor(
                ContextCompat.getColor(
                    this@LinkedFamilyFragment, R.color.primaryColor
                )
            )
            myshakha_view.setTextColor(
                ContextCompat.getColor(
                    this@LinkedFamilyFragment, R.color.grayColorColor
                )
            )
            gurudakshina_view.setTextColor(
                ContextCompat.getColor(
                    this@LinkedFamilyFragment, R.color.grayColorColor
                )
            )
            val end: Int = 100
            val start: Int = 0
            CallAPI(start, end, false)
        })

        myshakha_view.setOnClickListener(DebouncedClickListener {
            data_not_found_layout.visibility = View.GONE

            myfamily_line.visibility = View.INVISIBLE
            myshakha_line.visibility = View.VISIBLE
            gurudakshina_line.visibility = View.INVISIBLE

            family_layout.visibility = View.INVISIBLE
            shakha_layout.visibility = View.VISIBLE
            guru_dakshina_layout.visibility = View.INVISIBLE

//            shakha_count_layout.visibility = View.VISIBLE

            myfamily_view.setTextColor(
                ContextCompat.getColor(
                    this@LinkedFamilyFragment, R.color.grayColorColor
                )
            )
            myshakha_view.setTextColor(
                ContextCompat.getColor(
                    this@LinkedFamilyFragment, R.color.primaryColor
                )
            )
            gurudakshina_view.setTextColor(
                ContextCompat.getColor(
                    this@LinkedFamilyFragment, R.color.grayColorColor
                )
            )

            val end: Int = 100
            val start: Int = 0
            CallAPI(start, end, true)

        })

        gurudakshina_view.setOnClickListener(DebouncedClickListener {
            myfamily_line.visibility = View.INVISIBLE
            myshakha_line.visibility = View.INVISIBLE
            gurudakshina_line.visibility = View.VISIBLE

            family_layout.visibility = View.INVISIBLE
            shakha_layout.visibility = View.INVISIBLE
            guru_dakshina_layout.visibility = View.VISIBLE

//            shakha_count_layout.visibility = View.INVISIBLE
            
            myfamily_view.setTextColor(
                ContextCompat.getColor(
                    this@LinkedFamilyFragment, R.color.grayColorColor
                )
            )
            myshakha_view.setTextColor(
                ContextCompat.getColor(
                    this@LinkedFamilyFragment, R.color.grayColorColor
                )
            )
            gurudakshina_view.setTextColor(
                ContextCompat.getColor(
                    this@LinkedFamilyFragment, R.color.primaryColor
                )
            )

            if (sessionManager.fetchSHAKHAID() != "") {
                val end: Int = 100
                val start: Int = 0

                CallGuruDakshinaAPI(start, end)
            }
        })
    }

    private fun CallGuruDakshinaAPI(start: Int, end: Int) {
        if (Functions.isConnectingToInternet(this@LinkedFamilyFragment)) {
            USERID = sessionManager.fetchUserID()!!
            Log.d("USERID", USERID)
            val LENGTH: String = end.toString()
            val START: String = start.toString()
            val SEARCH: String = ""
            val CHAPTER_ID: String = sessionManager.fetchSHAKHAID()!!   // "227"
            myGuruDakshina(USERID, LENGTH, START, SEARCH, CHAPTER_ID)
        } else {
            Toast.makeText(
                this@LinkedFamilyFragment,
                resources.getString(R.string.no_connection),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun CallAPI(PAGE: Int, END: Int, isSankhya: Boolean) {
        if (Functions.isConnectingToInternet(this@LinkedFamilyFragment)) {
            if (isSankhya) {
                USERID = sessionManager.fetchUserID()!!
                TAB = "shakha"
                MEMBERID = sessionManager.fetchMEMBERID()!!
                STATUS = "0"
                LENGTH = END.toString()
                START = PAGE.toString()
                SEARCH = ""
                shakhaMemberList(USERID, TAB, MEMBERID, STATUS, LENGTH, START, SEARCH)
            } else {
                USERID = sessionManager.fetchUserID()!!
                TAB = "family"
                MEMBERID = sessionManager.fetchMEMBERID()!!
                STATUS = "1"
                LENGTH = END.toString()
                START = PAGE.toString()
                SEARCH = ""
                CHAPTERID = ""
                myMemberList(USERID, TAB, MEMBERID, STATUS, LENGTH, START, SEARCH, CHAPTERID)
            }
        } else {
            Toast.makeText(
                this@LinkedFamilyFragment,
                resources.getString(R.string.no_connection),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun shakhaMemberList(
        userid: String,
        tab: String,
        memberid: String,
        status: String,
        length: String,
        start: String,
        search: String

    ) {
        val pd = CustomProgressBar(this@LinkedFamilyFragment)
        pd.show()
        val call: Call<Get_Member_Listing_Response> =
            MyHssApplication.instance!!.api.get_member_listing_shakha(
                userid,
                tab,
                memberid,
                status,
                length,
                start,
                search
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
                            if (atheletsBeans.isNotEmpty()) {
                                member_count_layout.visibility = View.VISIBLE
                                member_count.text = atheletsBeans.size.toString()
                            }
                        } catch (e: ArithmeticException) {
                            println(e)
                        } finally {
                            println("Family")
                        }
                    } else {
                        data_not_found_layout.visibility = View.GONE
                        member_count_layout.visibility = View.GONE
                    }
                } else {
                    member_count_layout.visibility = View.GONE
                    Functions.showAlertMessageWithOK(
                        this@LinkedFamilyFragment, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Get_Member_Listing_Response>, t: Throwable) {
                member_count_layout.visibility = View.GONE
                Toast.makeText(this@LinkedFamilyFragment, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    fun ShakhaCountDialog(
        count: String, shakha_name: String, nagar_name: String, vibhag_name: String
    ) {
        // Deposit Dialog
        if (dialog == null) {
            dialog = Dialog(this, R.style.StyleCommonDialog)
        }
        dialog?.setContentView(R.layout.dialog_shakha_nagar_vibhag)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.show()

        val total_count = dialog!!.findViewById(R.id.total_count) as TextView
        val close_button = dialog!!.findViewById(R.id.close_button) as ImageView
        val my_shakha_name = dialog!!.findViewById(R.id.my_shakha_name) as TextView
        val my_nagar_name = dialog!!.findViewById(R.id.my_nagar_name) as TextView
        val my_vibhag_name = dialog!!.findViewById(R.id.my_vibhag_name) as TextView

        total_count.text = count.capitalize(Locale.ROOT)
        my_shakha_name.text = shakha_name.capitalize(Locale.ROOT)
        my_nagar_name.text = nagar_name.capitalize(Locale.ROOT)
        my_vibhag_name.text = vibhag_name.capitalize(Locale.ROOT)

        close_button.setOnClickListener(DebouncedClickListener {
            dialog?.dismiss()
        })
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
        val pd = CustomProgressBar(this@LinkedFamilyFragment)
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
                            mAdapterGuru = CustomAdapter(atheletsBeans)
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
                        this@LinkedFamilyFragment, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Get_Member_Listing_Response>, t: Throwable) {
                Toast.makeText(this@LinkedFamilyFragment, t.message, Toast.LENGTH_LONG).show()
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
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@LinkedFamilyFragment, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
            }

            override fun onFailure(call: Call<Get_Member_Listing_Response>, t: Throwable) {
                Toast.makeText(this@LinkedFamilyFragment, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    /*Guru Dakshina*/
    private fun myGuruDakshina(
        user_id: String, length: String, start: String, search: String, chapter_id: String
    ) {
        val pd = CustomProgressBar(this@LinkedFamilyFragment)
        pd.show()
        val call: Call<guru_dakshina_response> = MyHssApplication.instance!!.api.get_guru_dakshina(
            user_id, length, start, search, chapter_id
        )
        call.enqueue(object : Callback<guru_dakshina_response> {
            override fun onResponse(
                call: Call<guru_dakshina_response>, response: Response<guru_dakshina_response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {
                        data_not_found_layout.visibility = View.GONE
                        try {
                            guru_dakshinaBeans = response.body()!!.data!!
                            Log.d("guru_dakshinaBeans", guru_dakshinaBeans.toString())
                            setPieChartForGuruDakshina(guru_dakshinaBeans)

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
                        this@LinkedFamilyFragment, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<guru_dakshina_response>, t: Throwable) {
                Toast.makeText(this@LinkedFamilyFragment, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    private fun setPieChartForGuruDakshina(guruDakshinabeans: List<Datum_guru_dakshina>) {
        pieChart_guru.setOnChartValueSelectedListener(this)
        pieChart_guru.setUsePercentValues(false)
        pieChart_guru.getDescription().setEnabled(false)
        pieChart_guru.setExtraOffsets(5f, 10f, 5f, 5f)
        pieChart_guru.setDragDecelerationFrictionCoef(0.95f)
        pieChart_guru.setDrawHoleEnabled(true)
        pieChart_guru.setHoleColor(Color.WHITE)
        pieChart_guru.setTransparentCircleColor(Color.WHITE)
        pieChart_guru.setTransparentCircleAlpha(110)
        pieChart_guru.setHoleRadius(27f)
        pieChart_guru.setTransparentCircleRadius(20f)
        pieChart_guru.setDrawCenterText(true)
        pieChart_guru.setRotationAngle(0f)
        pieChart_guru.setRotationEnabled(true)

        pieChart_guru.setHighlightPerTapEnabled(true)
        pieChart_guru.animateY(1500, Easing.EaseInOutSine)
        pieChart_guru.legend.isEnabled = false
//        pieChart_guru.legend.textColor = Color.WHITE
        pieChart_guru.setEntryLabelColor(Color.BLACK)
        pieChart_guru.setEntryLabelTextSize(12f)
        val IDMEMBER: ArrayList<String> = ArrayList()
        for (k in 0 until guruDakshinabeans.size) {
            IDMEMBER.add(guruDakshinabeans.get(k).memberId!!)
        }
        val member_list = IDMEMBER.toSet()
        val pieEntries: ArrayList<PieEntry> = ArrayList()
        val labelsList: java.util.ArrayList<String> = java.util.ArrayList()
        var t_count = 0.0
        for (i in 0 until member_list.size) {
            var s_count: Double = 0.0
            var s_name = ""
            for (j in 0 until guruDakshinabeans.size) {
                if (member_list.elementAt(i) == guruDakshinabeans.get(j).memberId) {
//                    DebugLog.e("paid amt : " + guruDakshinabeans.get(j).paidAmount)
                    s_count = s_count + guruDakshinabeans.get(j).paidAmount!!.toDouble()
                    s_name = guruDakshinabeans.get(j).firstName.toString()
                }
            }
            t_count = t_count + s_count
//            DebugLog.e("Total paid amt : $s_count")
            labelsList.add(s_name)
            pieEntries.add(PieEntry(s_count.toFloat(), s_name))
        }
        val pieDataSet = PieDataSet(pieEntries, "")
        pieDataSet.setDrawIcons(false)
        pieDataSet.valueFormatter = DefaultValueFormatter(2)
        pieDataSet.sliceSpace = 2f
        pieDataSet.iconsOffset = MPPointF(0f, 40f)
        pieDataSet.selectionShift = 2f
        val colors: ArrayList<Int> = ArrayList()
        val piechart_color: IntArray = getResources().getIntArray(R.array.pieChart_guru)
        for (i in 0 until piechart_color.size) {
            colors.add(piechart_color[i])
        }
        pieDataSet.colors = colors
        val data = PieData(pieDataSet)
        data.setValueTextSize(14f)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        data.setValueTextColor(Color.WHITE)
        pieChart_guru.setData(data)
        pieChart_guru.setDrawEntryLabels(false)
        pieChart_guru.centerText = "Total\n" + UtilCommon.roundOffDecimal(t_count)
        pieChart_guru.setCenterTextColor(Color.BLACK)
        pieChart_guru.setCenterTextSize(20f)
        pieChart_guru.invalidate()
        setupPieChartChipGroup(labelsList)
    }

    private fun setupPieChartChipGroup(labelsList: java.util.ArrayList<String>) {
        if (chipGroup.childCount == 0) {
            chipGroup.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            chipGroup.isSingleSelection = true
            chipGroup.isSingleLine = false
            for (i in 0 until labelsList.size) {
                chipGroup.addView(createChip(labelsList.get(i).toString()))
            }
            val colorArray = resources.obtainTypedArray(R.array.pieChart_guru)
            var t_color = 0;
            for (i in 0 until chipGroup.childCount) {
                val chip = chipGroup.getChildAt(i) as Chip
                val chipColor = colorArray.getColor(t_color, 0)
                t_color = if (t_color < (colorArray.length() - 1)) t_color + 1 else 0
                chip.chipBackgroundColor = ColorStateList.valueOf(chipColor)
            }
            layout_pieChart_lable.addView(chipGroup)
        }
        chipGroup.invalidate()
    }

    private fun createChip(label: String): Chip {
        val chip = Chip(
            this, null, com.google.android.material.R.style.Widget_MaterialComponents_Chip_Entry
        )
        chip.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        chip.text = label
        chip.isCloseIconVisible = false
        chip.isChipIconVisible = false
        chip.isCheckable = false
        chip.isClickable = true
        chip.setTextColor(Color.WHITE)
        chip.setOnClickListener(DebouncedClickListener { openBarChart(label.toString()) })
        return chip
    }


    override fun onValueSelected(e: Entry?, h: Highlight?) {
        openBarChart((e as PieEntry).label.toString())
    }

    override fun onNothingSelected() {
    }

    private fun openBarChart(sName: String) {
        val listData_guru: ArrayList<BarchartDataModel> = ArrayList()
        val listData_guruDakshina: ArrayList<Datum_guru_dakshina> = ArrayList()

        for (i in 0 until guru_dakshinaBeans.size) {
            if (sName == guru_dakshinaBeans[i].firstName) {
                val barchartDataModel = BarchartDataModel()
                barchartDataModel.setValue_x(guru_dakshinaBeans[i].startDate)
                barchartDataModel.setValue_y(guru_dakshinaBeans[i].paidAmount)
                barchartDataModel.setValue_user(guru_dakshinaBeans[i].firstName)
                listData_guru.add(barchartDataModel)
                listData_guruDakshina.add(guru_dakshinaBeans[i])
            }
        }
        val intent = Intent(this@LinkedFamilyFragment, ViewBarchartActivity::class.java)
        intent.putExtra("case", "2")
        intent.putExtra("list_data", listData_guru)
        intent.putExtra("list_guruDakshina", listData_guruDakshina)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}