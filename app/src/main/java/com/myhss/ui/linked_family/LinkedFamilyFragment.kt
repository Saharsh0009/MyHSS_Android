package com.uk.myhss.ui.dashboard

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
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
import com.myhss.Utils.Functions
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
import com.uk.myhss.ui.my_family.Adapter.GuruCustomAdapter
import com.uk.myhss.ui.my_family.Model.Datum_guru_dakshina
import com.uk.myhss.ui.my_family.Model.guru_dakshina_response
import com.uk.myhss.ui.policies.SankhyaActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class LinkedFamilyFragment : AppCompatActivity() { //Fragment() {
    private lateinit var sessionManager: SessionManager
    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null
    lateinit var total_count: TextView
    lateinit var home_layout: RelativeLayout

    lateinit var myfamily_layout: LinearLayout
    lateinit var myshakha_layout: LinearLayout
    lateinit var gurudakshina_layout: LinearLayout

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
    private var mAdapterGuru: CustomAdapter? = null
    /*End My Family*/


    /*Start My Shakha*/
    lateinit var membership_view: RelativeLayout
    lateinit var active_membership_view: RelativeLayout
    lateinit var inactive_member: RelativeLayout
    lateinit var rejected_member: RelativeLayout
    lateinit var sankhya_layout: RelativeLayout
    lateinit var root_view: LinearLayout
    lateinit var shakha_layout: RelativeLayout
    /*End My Shakha*/

    /*Start Guru Dakshina*/
    lateinit var guru_dakshina_list: RecyclerView

    lateinit var one_time_layout: LinearLayout
    lateinit var regular_layout: LinearLayout
    lateinit var guru_dakshina_layout: RelativeLayout
    lateinit var guru_dakshinasearch_fields: AppCompatEditText
//    lateinit var USERID: String

    private var guru_dakshinaBeans: List<Datum_guru_dakshina> = ArrayList<Datum_guru_dakshina>()
    private var mAdapterGuruDakshina: GuruCustomAdapter? = null
    /*End Guru Dakshina*/

    private var loading = true
    var pastVisiblesItems = 0
    var visibleItemCount:Int = 0
    var totalItemCount:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_dashboard)

//        val intent = Intent(Intent.ACTION_SEND)
//        intent.type = "message/rfc822"
//        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>(emailAddress))
//        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject")
//        intent.putExtra(Intent.EXTRA_TEXT, "email body.")
//        startActivity(Intent.createChooser(intent, "Send Email"))

        /*override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val root = inflater.inflate(R.layout.fragment_dashboard, container, false)*/
//        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("MembershipApplicationsVC")
        sessionManager.firebaseAnalytics.setUserProperty("MembershipApplicationsVC", "LinkedFamilyFragment")

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        back_arrow = findViewById(R.id.back_arrow)
        header_title = findViewById(R.id.header_title)

        header_title.text = intent.getStringExtra("headerName")

        back_arrow.setOnClickListener {
            val i = Intent(this@LinkedFamilyFragment, HomeActivity::class.java)
            startActivity(i)
            finishAffinity()
        }

//        tabLayout = findViewById(R.id.tabLayout)
//        viewPager = findViewById(R.id.viewPager)

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

//        total_count = findViewById(R.id.total_count)
//        home_layout = findViewById(R.id.home_layout)

        shakha_count_layout.setOnClickListener {
            val shakha_count = sessionManager.fetchSHAKHA_SANKHYA_AVG()!!
            val shakha_name = sessionManager.fetchSHAKHANAME()!!
            val nagar_name = sessionManager.fetchNAGARNAME()!!
            val vibhag_name = sessionManager.fetchVIBHAGNAME()!!
            ShakhaCountDialog(shakha_count, shakha_name, nagar_name, vibhag_name)
        }

        /*My Family*/
        add_family_layout = findViewById(R.id.add_family_layout)
        rootview = findViewById(R.id.rootview)
        family_layout = findViewById(R.id.family_layout)
        search_fields = findViewById(R.id.search_fields)

        my_family_list = findViewById(R.id.my_family_list)

        mLayoutManager = LinearLayoutManager(this@LinkedFamilyFragment)
        my_family_list.layoutManager = mLayoutManager

        if (intent.getStringExtra("DashBoard") == "SHAKHAVIEW") {
            Handler().postDelayed({ myshakha_view.callOnClick() }, 100)
        } else {
            val end: Int = 100
            val start: Int = 0
            CallAPI(start, end)
        }

        /*if (Functions.isConnectingToInternet(this@LinkedFamilyFragment)) {
            USERID = sessionManager.fetchUserID()!!
            Log.d("USERID", USERID)
            TAB = "family"
            MEMBERID = sessionManager.fetchMEMBERID()!!
            STATUS = "all"
            LENGTH = end.toString()
            START = PAGE.toString()
            SEARCH = ""
            CHAPTERID = ""
            myMemberList(USERID, TAB, MEMBERID, STATUS, LENGTH, START, SEARCH, CHAPTERID)
        } else {
            Toast.makeText(
                this@LinkedFamilyFragment,
                resources.getString(R.string.no_connection),
                Toast.LENGTH_SHORT
            ).show()
        }*/

        /*my_family_list.setOnScrollListener(object : EndLessScroll(mLayoutManager) {
            override fun loadMore(current_page: Int) {
                var end:Int = 100
                var start:Int = 0

                start = end + 1
                end += 100

                Functions.printLog("start", start.toString())
                Functions.printLog("end", end.toString())
                CallAPI(start, end)
//                EndLessScroll.setLoaded()
            }
        })*/

        /*my_family_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = mLayoutManager.childCount
                    totalItemCount = mLayoutManager.itemCount
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition()

                    PAGE = (PAGE + 1)

                    if (Functions.isConnectingToInternet(this@LinkedFamilyFragment)) {
                        USERID = sessionManager.fetchUserID()!!
                        Log.d("USERID", USERID)
                        TAB = "family"
                        MEMBERID = sessionManager.fetchMEMBERID()!!
                        STATUS = "all"
                        LENGTH = "10"
                        START = PAGE.toString()
                        SEARCH = ""
                        CHAPTERID = ""
                        myMemberList(USERID, TAB, MEMBERID, STATUS, LENGTH, START, SEARCH, CHAPTERID)
                    } else {
                        Toast.makeText(
                            this@LinkedFamilyFragment,
                            resources.getString(R.string.no_connection),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    if (loading) {
                        if (totalItemCount > pastVisiblesItems) {
                            loading = false
                            pastVisiblesItems = totalItemCount
                        }
                    }
                    if (loading) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            loading = false
                            Log.v("...", "Last Item Wow !")
                            // Do pagination.. i.e. fetch new data
                            loading = true
                        }
                    }
                }
            }
        })*/

        search_fields.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val end:Int = 100
                val start:Int = 0

                if (Functions.isConnectingToInternet(this@LinkedFamilyFragment)) {
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

        add_family_layout.setOnClickListener {
            val i = Intent(this@LinkedFamilyFragment, AddMemberFirstActivity::class.java)
            i.putExtra("TYPE_SELF", "family")
//                i.putExtra("FAMILY", "FAMILY")
            startActivity(i)
        }
        /*End*/

        /*My Shakha*/
        membership_view = findViewById(R.id.membership_view)
        active_membership_view = findViewById(R.id.active_membership_view)
        inactive_member = findViewById(R.id.inactive_member)
        rejected_member = findViewById(R.id.rejected_member)
        sankhya_layout = findViewById(R.id.sankhya_layout)
        root_view = findViewById(R.id.root_view)
        shakha_layout = findViewById(R.id.shakha_layout)

        rejected_member.visibility = View.GONE

        membership_view.setOnClickListener {
//            Snackbar.make(root_view, "Member Ship", Snackbar.LENGTH_SHORT).show()
            val i = Intent(this@LinkedFamilyFragment, MemberShipActivity::class.java)
            i.putExtra("MEMBERS", "ALL_MEMBERS")
            startActivity(i)
        }

        active_membership_view.setOnClickListener {
//            Snackbar.make(root_view, "Active Member", Snackbar.LENGTH_SHORT).show()
            val i = Intent(this@LinkedFamilyFragment, MemberShipActivity::class.java)
            i.putExtra("MEMBERS", "ACTIVE_MEMBERS")
            startActivity(i)
        }

        inactive_member.setOnClickListener {
            val i = Intent(this@LinkedFamilyFragment, MemberShipActivity::class.java)
            i.putExtra("MEMBERS", "INACTIVE_MEMBERS")
            startActivity(i)
        }

        rejected_member.setOnClickListener {
//            Snackbar.make(root_view, "Rejected Member", Snackbar.LENGTH_SHORT).show()
            val i = Intent(this@LinkedFamilyFragment, MemberShipActivity::class.java)
            i.putExtra("MEMBERS", "REJECTED_MEMBERS")
            startActivity(i)
        }

        sankhya_layout.setOnClickListener {
            startActivity(Intent(this@LinkedFamilyFragment, SankhyaActivity::class.java))
        }
        /*End My Shakha*/

        /*Guru Dakshina*/
        one_time_layout = findViewById(R.id.one_time_layout)
        regular_layout = findViewById(R.id.regular_layout)
        guru_dakshina_layout = findViewById(R.id.guru_dakshina_layout)
        guru_dakshinasearch_fields = findViewById(R.id.guru_dakshinasearch_fields)

        guru_dakshina_list = findViewById(R.id.guru_dakshina_list)

        mLayoutManager = LinearLayoutManager(this@LinkedFamilyFragment)
        guru_dakshina_list.layoutManager = mLayoutManager

        /*guru_dakshina_list.setOnScrollListener(object : EndLessScroll(mLayoutManager) {
            override fun loadMore(current_page: Int) {
                var end:Int = 100
                var start:Int = 0

                start = end + 1
                end += 10
                Functions.printLog("start", start.toString())
                Functions.printLog("end", end.toString())
                CallGuruDakshinaAPI(start, end)
            }
        })*/

        guru_dakshinasearch_fields.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                val end:Int = 100
                val start:Int = 0

                if (Functions.isConnectingToInternet(this@LinkedFamilyFragment)) {
                    USERID = sessionManager.fetchUserID()!!
                    Log.d("USERID", USERID)
                    val LENGTH: String = end.toString()
                    val START: String = start.toString()
                    val SEARCH: String = s.toString()
                    val CHAPTER_ID: String = sessionManager.fetchSHAKHAID()!!   // "227"
                    mySearchGuruDakshina(USERID, LENGTH, START, SEARCH, CHAPTER_ID)
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

        one_time_layout.setOnClickListener {
//            Snackbar.make(rootview, "One-Time Dakshina", Snackbar.LENGTH_SHORT).show()
            startActivity(
                Intent(
                    this@LinkedFamilyFragment,
                    GuruDakshinaOneTimeFirstActivity::class.java
                )
            )
//            val i = Intent(requireContext(), AddMemberFirstActivity::class.java)
//            i.putExtra("TYPE_SELF", "family");
//            startActivity(i)
        }

        regular_layout.setOnClickListener {
//            Snackbar.make(rootview, "Regular Dakshina", Snackbar.LENGTH_SHORT).show()
            startActivity(
                Intent(
                    this@LinkedFamilyFragment,
                    GuruDakshinaRegularFirstActivity::class.java
                )
            )
        }
        /*End*/

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

//        shakha_count_layout.visibility = View.INVISIBLE

        myfamily_line.visibility = View.VISIBLE
        myshakha_line.visibility = View.INVISIBLE
        gurudakshina_line.visibility = View.INVISIBLE

        myfamily_view.setTextColor(
            ContextCompat.getColor(
                this@LinkedFamilyFragment,
                R.color.primaryColor
            )
        )
        myshakha_view.setTextColor(
            ContextCompat.getColor(
                this@LinkedFamilyFragment,
                R.color.grayColorColor
            )
        )
        gurudakshina_view.setTextColor(
            ContextCompat.getColor(
                this@LinkedFamilyFragment,
                R.color.grayColorColor
            )
        )

        family_layout.visibility = View.VISIBLE
        shakha_layout.visibility = View.INVISIBLE
        guru_dakshina_layout.visibility = View.INVISIBLE

        myfamily_view.setOnClickListener {
            myfamily_line.visibility = View.VISIBLE
            myshakha_line.visibility = View.INVISIBLE
            gurudakshina_line.visibility = View.INVISIBLE

            family_layout.visibility = View.VISIBLE
            shakha_layout.visibility = View.INVISIBLE
            guru_dakshina_layout.visibility = View.INVISIBLE

//            shakha_count_layout.visibility = View.INVISIBLE

            myfamily_view.setTextColor(
                ContextCompat.getColor(
                    this@LinkedFamilyFragment,
                    R.color.primaryColor
                )
            )
            myshakha_view.setTextColor(
                ContextCompat.getColor(
                    this@LinkedFamilyFragment,
                    R.color.grayColorColor
                )
            )
            gurudakshina_view.setTextColor(
                ContextCompat.getColor(
                    this@LinkedFamilyFragment,
                    R.color.grayColorColor
                )
            )

            val end:Int = 100
            val start:Int = 0

            if (Functions.isConnectingToInternet(this@LinkedFamilyFragment)) {
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
                    this@LinkedFamilyFragment,
                    resources.getString(R.string.no_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        myshakha_view.setOnClickListener {
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
                    this@LinkedFamilyFragment,
                    R.color.grayColorColor
                )
            )
            myshakha_view.setTextColor(
                ContextCompat.getColor(
                    this@LinkedFamilyFragment,
                    R.color.primaryColor
                )
            )
            gurudakshina_view.setTextColor(
                ContextCompat.getColor(
                    this@LinkedFamilyFragment,
                    R.color.grayColorColor
                )
            )
        }

        gurudakshina_view.setOnClickListener {
            myfamily_line.visibility = View.INVISIBLE
            myshakha_line.visibility = View.INVISIBLE
            gurudakshina_line.visibility = View.VISIBLE

            family_layout.visibility = View.INVISIBLE
            shakha_layout.visibility = View.INVISIBLE
            guru_dakshina_layout.visibility = View.VISIBLE

//            shakha_count_layout.visibility = View.INVISIBLE

            myfamily_view.setTextColor(
                ContextCompat.getColor(
                    this@LinkedFamilyFragment,
                    R.color.grayColorColor
                )
            )
            myshakha_view.setTextColor(
                ContextCompat.getColor(
                    this@LinkedFamilyFragment,
                    R.color.grayColorColor
                )
            )
            gurudakshina_view.setTextColor(
                ContextCompat.getColor(
                    this@LinkedFamilyFragment,
                    R.color.primaryColor
                )
            )

            if (sessionManager.fetchSHAKHAID() != "") {
                val end:Int = 100
                val start:Int = 0

                CallGuruDakshinaAPI(start, end)
                /*if (Functions.isConnectingToInternet(this@LinkedFamilyFragment)) {
                    USERID = sessionManager.fetchUserID()!!
                    Log.d("USERID", USERID)
                    val LENGTH: String = "10"
                    val START: String = "0"
                    val SEARCH: String = ""
                    val CHAPTER_ID: String = sessionManager.fetchSHAKHAID()!!   // "227"
                    myGuruDakshina(USERID, LENGTH, START, SEARCH, CHAPTER_ID)
                } else {
                    Toast.makeText(
                        this@LinkedFamilyFragment,
                        resources.getString(R.string.no_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }*/
            }
        }
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

    fun CallAPI(PAGE: Int, END: Int) {
        if (Functions.isConnectingToInternet(this@LinkedFamilyFragment)) {
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
                this@LinkedFamilyFragment,
                resources.getString(R.string.no_connection),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun ShakhaCountDialog(
        count: String,
        shakha_name: String,
        nagar_name: String,
        vibhag_name: String
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

        close_button.setOnClickListener {
            dialog?.dismiss()
        }
    }

    private fun myMemberList(
        user_id: String, tab: String, member_id: String, status: String,
        length: String, start: String, search: String, chapter_id: String
    ) {
        val pd = CustomProgressBar(this@LinkedFamilyFragment)
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
                            for (i in 1 until atheletsBeans.size) {
                                Log.d("firstName", atheletsBeans[i].firstName.toString())
                            }
                            Log.d("count=>", atheletsBeans.size.toString())

                            if (atheletsBeans.isNotEmpty()) {
                                member_count_layout.visibility = View.VISIBLE
                                member_count.text = atheletsBeans.size.toString()
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
//                        data_not_found_layout.visibility = View.VISIBLE
                        Functions.displayMessage(this@LinkedFamilyFragment,response.body()?.message)
//                        Functions.showAlertMessageWithOK(
//                            this@LinkedFamilyFragment, "",
////                        "Message",
//                            response.body()?.message
//                        )
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@LinkedFamilyFragment, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
//                pd.dismiss()
            }

            override fun onFailure(call: Call<Get_Member_Listing_Response>, t: Throwable) {
                Toast.makeText(this@LinkedFamilyFragment, t.message, Toast.LENGTH_LONG).show()
//                pd.dismiss()
            }
        })
    }

    /*Guru Dakshina*/
    private fun myGuruDakshina(
        user_id: String,
        length: String,
        start: String,
        search: String,
        chapter_id: String
    ) {
        val pd = CustomProgressBar(this@LinkedFamilyFragment)
        pd.show()
        val call: Call<guru_dakshina_response> = MyHssApplication.instance!!.api.get_guru_dakshina(
            user_id, length, start, search, chapter_id
        )
        call.enqueue(object : Callback<guru_dakshina_response> {
            override fun onResponse(
                call: Call<guru_dakshina_response>,
                response: Response<guru_dakshina_response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {
                        data_not_found_layout.visibility = View.GONE
                        try {
                            guru_dakshinaBeans = response.body()!!.data!!
                            Log.d("guru_dakshinaBeans", guru_dakshinaBeans.toString())
                            mAdapterGuruDakshina = GuruCustomAdapter(guru_dakshinaBeans)

                            guru_dakshina_list.adapter = mAdapterGuruDakshina
                            mAdapterGuruDakshina!!.notifyDataSetChanged()

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

    private fun mySearchGuruDakshina(
        user_id: String,
        length: String,
        start: String,
        search: String,
        chapter_id: String
    ) {
//        val pd = CustomProgressBar(context)
//        pd.show()
        val call: Call<guru_dakshina_response> = MyHssApplication.instance!!.api.get_guru_dakshina(
            user_id, length, start, search, chapter_id
        )
        call.enqueue(object : Callback<guru_dakshina_response> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<guru_dakshina_response>,
                response: Response<guru_dakshina_response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {
                        data_not_found_layout.visibility = View.GONE
                        try {
                            guru_dakshinaBeans = response.body()!!.data!!
                            Log.d("guru_dakshinaBeans", guru_dakshinaBeans.toString())
                            mAdapterGuruDakshina = GuruCustomAdapter(guru_dakshinaBeans)

                            guru_dakshina_list.adapter = mAdapterGuruDakshina
                            mAdapterGuruDakshina!!.notifyDataSetChanged()

                        } catch (e: ArithmeticException) {
                            println(e)
                        } finally {
                            println("Family")
                        }
                    } else {
//                        data_not_found_layout.visibility = View.VISIBLE
                        Functions.displayMessage(this@LinkedFamilyFragment, response.body()?.message)
//                        Functions.showAlertMessageWithOK(
//                            this@LinkedFamilyFragment, "",
////                        "Message",
//                            response.body()?.message
//                        )
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@LinkedFamilyFragment, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
//                pd.dismiss()
            }

            override fun onFailure(call: Call<guru_dakshina_response>, t: Throwable) {
                Toast.makeText(this@LinkedFamilyFragment, t.message, Toast.LENGTH_LONG).show()
//                pd.dismiss()
            }
        })
    }
}