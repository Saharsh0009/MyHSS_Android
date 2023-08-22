package com.uk.myhss.ui.sankhya_report

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.DebugLog
import com.myhss.Utils.Functions
import com.myhss.ui.sankhya_report.Adapter.ApproveRecyclerView
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.ui.guru_dakshina.Model.Get_Sankhya_Add_Response
import com.uk.myhss.ui.my_family.Model.Datum
import com.uk.myhss.ui.my_family.Model.my_family_response
import com.uk.myhss.ui.policies.SankhyaActivity
import com.uk.myhss.ui.sankhya_report.Adapter.SankhyaAdapter
import com.uk.myhss.ui.sankhya_report.Model.Get_Sankhya_Utsav_Datum
import com.uk.myhss.ui.sankhya_report.Model.Get_Sankhya_Utsav_Response
import com.uk.myhss.ui.sankhya_report.Model.Sankhya_Datum
import com.uk.myhss.ui.sankhya_report.Model.Sankhya_List_Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AddSankhyaActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private lateinit var sessionManager: SessionManager

    private var year = 0
    private var month = 0
    private var day = 0
    private lateinit var calendar: Calendar

    var utsavName: List<String> = java.util.ArrayList<String>()
    var utsavID: List<String> = java.util.ArrayList<String>()
    var UserName: ArrayList<String> = ArrayList<String>()
    var UserCategory: ArrayList<String> = ArrayList<String>()

    private var UTSAV_ID: String = ""
    private var TODAY_DATE: String = ""

    private var USER_ID: String = ""
    private var MEMBER_ID: String = ""
    private var MEMBER_NAME: String = ""
    private var ORG_CHAPTER_ID: String = ""
    private var EVENT_DATE: String = ""
    private var EVENTDATE: String = ""
    private var UTSAV_NAME: String = ""
    private var SHISHU_MALE: String = ""
    private var SHISHU_FEMALE: String = ""
    private var BAAL: String = ""
    private var BAALIKA: String = ""
    private var KISHOR: String = ""
    private var KISHORI: String = ""
    private var TARUN: String = ""
    private var TARUNI: String = ""
    private var YUVA: String = ""
    private var YUVTI: String = ""
    private var PRODH: String = ""
    private var PRODHA: String = ""
    private var API: String = ""

    private lateinit var utsav_txt: SearchableSpinner

    private var IsVisible = true
    private var IsVisiblenew = true

    private var atheletsBeans: List<Sankhya_Datum> = ArrayList<Sankhya_Datum>()
    private var athelets_Beans: List<Datum> = ArrayList<Datum>()
    private var currentSelectedItems: List<Datum> = ArrayList<Datum>()
    private var selected_user: ArrayList<String> = ArrayList<String>()
    private var selected_userName: ArrayList<String> = ArrayList<String>()
    private var selected_userNameAll: ArrayList<String> = ArrayList<String>()
    private var selected_userAll: ArrayList<String> = ArrayList<String>()
    private var mAdapterGuru: SankhyaAdapter? = null

    //    private var mAdapterGurunew: StudentAdapter? = null
    private var approveRecyclerView: ApproveRecyclerView? = null

    var arrayListUser: ArrayList<String> = ArrayList()
    var arrayListUserId: ArrayList<String> = ArrayList()
    var arrayData: String = ""

    lateinit var USERID: String
    lateinit var MEMBERID: String
    lateinit var LENGTH: String
    lateinit var START: String
    lateinit var SEARCH: String
    lateinit var START_DATE: String
    lateinit var END_DATE: String

    lateinit var root_view: LinearLayout
    lateinit var additional_guest_layout: LinearLayout
    lateinit var submit_layout: LinearLayout
    lateinit var current_date_txt: TextView
    lateinit var shakha_name_txt: TextView
    lateinit var next_date: ImageView
    lateinit var previous_date: ImageView
    lateinit var calender_icon: ImageView
    lateinit var attended_list: RecyclerView
    lateinit var attendedlist: ListView

    lateinit var cbSelectAll: CheckBox
    lateinit var tvSelect: TextView
    lateinit var txt_sankhya_count: TextView

    private var selectAllItems: Boolean = false

    val sdf: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val sdfnew: SimpleDateFormat = SimpleDateFormat("dd MMM | EEEE", Locale.getDefault())
    var actualDate: Calendar = Calendar.getInstance()

    lateinit var mLayoutManager: LinearLayoutManager

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_sankhya)

        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("AddShakhaVS")
        sessionManager.firebaseAnalytics.setUserProperty("AddShakhaVS", "AddSankhyaActivity")

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        val back_arrow = findViewById<ImageView>(R.id.back_arrow)
        val header_title = findViewById<TextView>(R.id.header_title)

        header_title.text = "Add " + getString(R.string.sankhya)

        back_arrow.setOnClickListener(DebouncedClickListener {
            finish()
        })

        utsav_txt = findViewById(R.id.utsav_txt)

        root_view = findViewById(R.id.root_view)
        attended_list = findViewById(R.id.attended_list)
        attendedlist = findViewById(R.id.attendedlist)
        additional_guest_layout = findViewById(R.id.additional_guest_layout)
        submit_layout = findViewById(R.id.submit_layout)
        current_date_txt = findViewById(R.id.current_date_txt)
        shakha_name_txt = findViewById(R.id.shakha_name_txt)
        next_date = findViewById(R.id.next_date)
        previous_date = findViewById(R.id.previous_date)
        calender_icon = findViewById(R.id.calender_icon)
        txt_sankhya_count = findViewById(R.id.txt_sankhya_count)

        calender_icon.visibility = View.VISIBLE

        utsav_txt.onItemSelectedListener = mOnItemSelectedListener_utsav

        utsav_txt.setTitle("Select Utsav")

        shakha_name_txt.text = sessionManager.fetchSHAKHANAME()!!.capitalize(Locale.ROOT)

        cbSelectAll = findViewById(R.id.cbSelectAll)
        tvSelect = findViewById(R.id.tvSelect)

        /*attended_list.layoutManager = LinearLayoutManager(
            this,
            LinearLayout.VERTICAL,
            false
        )*/

        mLayoutManager = LinearLayoutManager(this@AddSankhyaActivity)
        attended_list.layoutManager = mLayoutManager

        actualDate = Calendar.getInstance()
        val date = sdf.format(actualDate.time)
        Log.d("cuurent Date==>", date)

        TODAY_DATE = sdf.format(actualDate.time)

        current_date_txt.text = sdfnew.format(actualDate.time)
        EVENT_DATE = sdf.format(actualDate.time)

        val smsTime = Calendar.getInstance()
        smsTime.timeInMillis = actualDate.timeInMillis

        previous_date.setOnClickListener(DebouncedClickListener {
            //modify actual date, removing one day
            actualDate.add(Calendar.DAY_OF_MONTH, -1)
            val date = sdf.format(actualDate.time)
            Log.d("previous_date==>", date)

            current_date_txt.text = sdfnew.format(actualDate.time)

            next_date.setBackgroundResource(R.drawable.button_round)
        })

        if (current_date_txt.text.toString() == current_date_txt.text.toString()) {
            next_date.setBackgroundResource(R.drawable.gray_round_border)
        } else {
            next_date.setBackgroundResource(R.drawable.button_round)

            next_date.setOnClickListener(DebouncedClickListener {
                actualDate.add(Calendar.DAY_OF_MONTH, 1)
                val date = sdf.format(actualDate.time)
                Log.d("next_date==>", date)

                current_date_txt.text = sdfnew.format(actualDate.time)

                next_date.setBackgroundResource(R.drawable.button_round)
            })
        }

        current_date_txt.setOnClickListener(DebouncedClickListener {
            calendar = Calendar.getInstance()
//            calendar.add(Calendar.YEAR, -3)
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH)
            day = calendar.get(Calendar.DAY_OF_MONTH)

            val dialog = DatePickerDialog(this, { _, year, month, day_of_month ->
                calendar[Calendar.YEAR] = year
                calendar[Calendar.MONTH] = month //+ 1
                calendar[Calendar.DAY_OF_MONTH] = day_of_month
                val myFormat = "dd/MM/yyyy"
//                val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
                current_date_txt.text = sdfnew.format(calendar.time)
                EVENT_DATE = sdf.format(calendar.time)

            }, year, month, day)
//            dialog.datePicker.minDate = calendar.timeInMillis
//            calendar.add(Calendar.YEAR, 0)
//            dialog.datePicker.maxDate = calendar.timeInMillis
            dialog.show()
        })

        calender_icon.setOnClickListener(DebouncedClickListener {
            calendar = Calendar.getInstance()
//            calendar.add(Calendar.YEAR, -3)
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH)
            day = calendar.get(Calendar.DAY_OF_MONTH)

            val dialog = DatePickerDialog(this, { _, year, month, day_of_month ->
                calendar[Calendar.YEAR] = year
                calendar[Calendar.MONTH] = month //+ 1
                calendar[Calendar.DAY_OF_MONTH] = day_of_month
                val myFormat = "dd/MM/yyyy"
//                val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
                current_date_txt.text = sdfnew.format(calendar.time)
                EVENT_DATE = sdf.format(calendar.time)

            }, year, month, day)
//            dialog.datePicker.minDate = calendar.timeInMillis
//            calendar.add(Calendar.YEAR, 0)
            dialog.datePicker.maxDate = calendar.timeInMillis
            dialog.show()
        })

        /*if (date.get(Calendar.DATE).equals(smsTime.get(Calendar.DATE))) {
            next_date.setBackgroundResource(R.drawable.gray_round_border)
        } else {
            next_date.setBackgroundResource(R.drawable.button_round)

            next_date.setOnClickListener(View.OnClickListener {
                actualDate.add(Calendar.DAY_OF_MONTH, 1)
                val date = sdf.format(actualDate.time)
                Log.d("next_date==>", date)

                current_date_txt.text = sdfnew.format(actualDate.time)
            })
        }*/

        val end: Int = 100
        val start: Int = 0

        if (Functions.isConnectingToInternet(this@AddSankhyaActivity)) {
            USERID = sessionManager.fetchUserID()!!
            MEMBERID = sessionManager.fetchSHAKHAID()!!
            LENGTH = end.toString()
            START = start.toString()
            SEARCH = ""
            START_DATE = EVENT_DATE
            END_DATE = TODAY_DATE

            Log.d("USERID", USERID)
            Log.d("MEMBERID", MEMBERID)
            myFamily(USERID)
//            mySankhyaList(USERID, MEMBERID, LENGTH, START, SEARCH, START_DATE, END_DATE)

            myRelationship()
        } else {
            Toast.makeText(
                this@AddSankhyaActivity,
                resources.getString(R.string.no_connection),
                Toast.LENGTH_SHORT
            ).show()
        }

        /*attended_list.setOnScrollListener(object : EndLessScroll(mLayoutManager) {
            override fun loadMore(current_page: Int) {
                var end:Int = 100
                var start:Int = 0

                start = end + 1
                end += 100

                if (Functions.isConnectingToInternet(this@AddSankhyaActivity)) {
                    USERID = sessionManager.fetchUserID()!!
                    MEMBERID = sessionManager.fetchSHAKHAID()!!
                    LENGTH = end.toString()
                    START = start.toString()
                    SEARCH = ""
                    START_DATE = EVENT_DATE
                    END_DATE = TODAY_DATE

                    Log.d("USERID", USERID)
                    Log.d("MEMBERID", MEMBERID)
                    myFamily(USERID)
//            mySankhyaList(USERID, MEMBERID, LENGTH, START, SEARCH, START_DATE, END_DATE)

                    myRelationship()
                } else {
                    Toast.makeText(
                        this@AddSankhyaActivity,
                        resources.getString(R.string.no_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })*/

        sessionManager.saveSELECTED_ALL("")
        sessionManager.saveSELECTED_TRUE("")
        sessionManager.saveSELECTED_FALSE("")

        cbSelectAll.visibility = View.VISIBLE
        /*cbSelectAll.setOnCheckedChangeListener { buttonView, isChecked ->
            if (approveRecyclerView != null) {
                *//*for(i in 0 until arrayListUser.size) {
                }*//*

                if(cbSelectAll.isChecked){
                    tvSelect.text = if (isChecked) "Deselect All" else "All Attended"
                    approveRecyclerView!!.selectAllItems()
                }
                else {
                    tvSelect.text = if (isChecked) "All Attended" else "Deselect All"
                    approveRecyclerView!!.unselectAllItems()
                }
                approveRecyclerView!!.notifyDataSetChanged()

                *//*if (isChecked) {
                    selectAllItems = true
                    approveRecyclerView!!.selectAllItems(selectAllItems)
                    approveRecyclerView!!.updateList(athelets_Beans)
                    approveRecyclerView!!.notifyDataSetChanged()
                } else {
                    selectAllItems = false
                    approveRecyclerView!!.unselectAllItems(selectAllItems)
                    approveRecyclerView!!.updateList(athelets_Beans)
                    approveRecyclerView!!.notifyDataSetChanged()
                }*//*

                *//*val size = athelets_Beans.size
                for (i in 0 until size) {
                    val dto = athelets_Beans[i]
                    dto.setChecked(true)
                }
                approveRecyclerView!!.notifyDataSetChanged()*//*

                *//*for (i in 0 until attended_list.childCount) {
                    attended_list.adapter = isChecked
                    attended_list.setItemChecked(i, true)
                }*//*
            }
            *//*if (mAdapterGurunew != null) {
                mAdapterGurunew!!.toggleSelection(isChecked)
                mAdapterGurunew!!.getSelection()
                tvSelect.text = if (isChecked) "Deselect All" else "All Attended"
                Functions.printLog("mAdapterGurunew", mAdapterGurunew!!.getSelection().toString())
            }*//*
        }*/

        /*attended_list.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, itemIndex, l ->
                // Get user selected item.
                val itemObject = adapterView.adapter.getItem(itemIndex)
                // Translate the selected item to DTO object.
                val itemDto = itemObject as ListViewItemDTO
                // Get the checkbox.
                val itemCheckbox = view.findViewById(R.id.chbContent) as CheckBox
                // Reverse the checkbox and clicked item check state.
                if (itemDto.isChecked()) {
                    itemCheckbox.isChecked = false
                    itemDto.setChecked(false)
                } else {
                    itemCheckbox.isChecked = true
                    itemDto.setChecked(true)
                }
                //Toast.makeText(getApplicationContext(), "select item text : " + itemDto.getItemText(), Toast.LENGTH_SHORT).show();
            }*/
        additional_guest_layout.setOnClickListener(DebouncedClickListener {
//            Snackbar.make(root_view, "Additional Guest Information", Snackbar.LENGTH_SHORT).show()
//            startActivity(Intent(this@AddSankhyaActivity, SankhyaDetail::class.java))
//            val i = Intent(this@AddSankhyaActivity, SankhyaDetail::class.java)

            DebugLog.d("arrayListUser : " + arrayListUser.toString())
            DebugLog.d("arrayListUserId : " + arrayListUserId.toString())
            DebugLog.d("arrayData : " + arrayData)

            val mergedId = arrayListUserId + arrayListUserId
            DebugLog.d("mergedId  ==>   " + mergedId.toString())

            for (num in arrayListUserId) {      // iterate through the second list
                if (!arrayListUserId.contains(num)) {   // if first list doesn't contain current element
                    arrayListUserId.add(num) // add it to the first list
                }
            }

            val mergedUser = arrayListUser + arrayListUser
            DebugLog.d("mergedUser==> 3 :  " + mergedUser.toString())

            for (num in arrayListUser) {      // iterate through the second list
                if (!arrayListUser.contains(num)) {   // if first list doesn't contain current element
                    arrayListUser.add(num) // add it to the first list
                }
            }

            val merged = selected_user + selected_userAll
            DebugLog.d("merged==> 4 : " + merged.toString())

            for (x in selected_userAll) {
                if (!selected_user.contains(x)) selected_user.add(x)
            }

            val mergedname = selected_userName + selected_userNameAll
            DebugLog.d("mergedname==> 5 : " + mergedname.toString())

            for (x in selected_userNameAll) {
                if (!selected_userName.contains(x)) selected_userName.add(x)
            }

            USER_ID = sessionManager.fetchUserID()!!
            /*MEMBER_ID = selected_user.toString().replace("[", "").replace("]", "")
            MEMBER_NAME = selected_userName.toString()*/

            MEMBER_ID = arrayListUserId.toString().replace("[", "").replace("]", "")
            MEMBER_NAME = arrayListUser.toString()

            ORG_CHAPTER_ID = sessionManager.fetchSHAKHAID()!!
            if (UTSAV_ID == "Please Select Utsav") {
                UTSAV_NAME = ""
            } else {
                UTSAV_NAME = UTSAV_ID
            }
//            UTSAV_NAME = UTSAV_ID
            EVENTDATE = EVENT_DATE

            /*if (UserName.equals("")) {
                Snackbar.make(root_view, "User Name is Empty", Snackbar.LENGTH_SHORT).show()
            } else if (UserCategory.equals("")) {
                Snackbar.make(root_view, "User Type is Empty", Snackbar.LENGTH_SHORT).show()
            } else */
            if (MEMBER_ID == "") {
                Snackbar.make(root_view, "Member is Empty", Snackbar.LENGTH_SHORT).show()
            } else {

                val i = Intent(this@AddSankhyaActivity, SankhyaFormDetail::class.java)
                i.putExtra("SANKHYA", "SANKHYA")
                i.putExtra("SANKHYA_ID", "")
                i.putExtra("CURRENT_DATE", EVENTDATE)
                if (UTSAV_ID == "Please Select Utsav") {
                    i.putExtra("UTSAVE_ID", "")
                    i.putExtra("UTSAV_NAME", "")
                } else {
                    i.putExtra("UTSAVE_ID", UTSAV_ID)
                    i.putExtra("UTSAV_NAME", UTSAV_NAME)
                }
                //                i.putExtra("UTSAVE_ID", UTSAV_ID)
                i.putExtra("USER_ID", USER_ID)
                i.putExtra("MEMBER_ID", MEMBER_ID)
                i.putExtra("ORG_CHAPTER_ID", ORG_CHAPTER_ID)
                //                i.putExtra("UTSAV_NAME", UTSAV_NAME)
                i.putExtra("EVENT_DATE", EVENT_DATE)
                i.putStringArrayListExtra("MEMBER_NAME", arrayListUser)
                i.putStringArrayListExtra("USERNAME_LIST", UserName)
                i.putStringArrayListExtra("USERID_LIST", UserCategory)
                i.putExtra("SHAKHA_NAME", shakha_name_txt.text.toString())
                //            i.putExtra("SELECTED_USER", selected_user)
                //            i.putExtra("ALL_USER", selected_userAll)
                startActivity(i)
            }
        })

        for (element in selected_user) {
            println(element)
            selected_userAll.add(element)
        }

        for (element in selected_userName) {
            print(element)
            selected_userNameAll.add(element)
        }

        submit_layout.setOnClickListener(DebouncedClickListener {

            Log.d("arrayListUserId==>", arrayListUserId.toString())

//            val Date = current_date_txt.text.toString()
            val Utsav = UTSAV_ID
            val All_user = selected_user
            val Selected_user = selected_userAll

            val mergedId = arrayListUserId + arrayListUserId
            Log.d("mergedId==>", mergedId.toString())

            for (num in arrayListUserId) {      // iterate through the second list
                if (!arrayListUserId.contains(num)) {   // if first list doesn't contain current element
                    arrayListUserId.add(num) // add it to the first list
                }
            }

            val merged = selected_user + selected_userAll
            Log.d("merged==>", merged.toString())

            for (x in selected_userAll) {
                if (!selected_user.contains(x)) selected_user.add(x)
            }

            val mergedname = selected_userName + selected_userNameAll
            Log.d("mergedname", mergedname.toString())

            for (x in selected_userNameAll) {
                if (!selected_userName.contains(x)) selected_userName.add(x)
            }

            Log.d("mergedselected_user==>", selected_user.toString())
            Log.d("mergedselected_all==>", selected_userAll.toString())
//            Snackbar.make(root_view, "Submit"+selected_user + "All" + selected_userAll, Snackbar.LENGTH_SHORT).show()

            USER_ID = sessionManager.fetchUserID()!!
//            MEMBER_ID = selected_user.toString().replace("[", "").replace("]", "")

            MEMBER_ID = arrayListUserId.toString().replace("[", "").replace("]", "")

            ORG_CHAPTER_ID = sessionManager.fetchSHAKHAID()!!
            if (UTSAV_ID == "Please Select Utsav") {
                UTSAV_NAME = ""
            } else {
                UTSAV_NAME = UTSAV_ID
            }
            EVENTDATE = EVENT_DATE
            SHISHU_MALE = ""
            SHISHU_FEMALE = ""
            BAAL = ""
            BAALIKA = ""
            KISHOR = ""
            KISHORI = ""
            TARUN = ""
            TARUNI = ""
            YUVA = ""
            YUVTI = ""
            PRODH = ""
            PRODHA = ""
            API = "yes"

            /*if (UserName.equals("")) {
                Snackbar.make(root_view, "User Name is Empty", Snackbar.LENGTH_SHORT).show()
            } else if (UserCategory.equals("")) {
                Snackbar.make(root_view, "User Type is Empty", Snackbar.LENGTH_SHORT).show()
            } else*/ if (MEMBER_ID == "") {
            Snackbar.make(root_view, "Member is Empty", Snackbar.LENGTH_SHORT).show()
        } else {

            if (Functions.isConnectingToInternet(this@AddSankhyaActivity)) {
                AddSankhya(
                    USER_ID,
                    MEMBER_ID,
                    ORG_CHAPTER_ID,
                    UTSAV_NAME,
                    EVENTDATE,
                    SHISHU_MALE,
                    SHISHU_FEMALE,
                    BAAL,
                    BAALIKA,
                    KISHOR,
                    KISHORI,
                    TARUN,
                    TARUNI,
                    YUVA,
                    YUVTI,
                    PRODH,
                    PRODHA,
                    API
                )
            } else {
                Toast.makeText(
                    this@AddSankhyaActivity,
                    resources.getString(R.string.no_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        })

        attended_list.setOnItemClickListener(object : SankhyaAdapter.ClickListener {
            override fun onItemClick(v: View, position: Int) {

                Log.v("Bhanu", "onItemClick ${position}")

                Toast.makeText(
                    this@AddSankhyaActivity, "Clicked: ${approveRecyclerView!!.mItems[position]}",
//                    "Clicked: ${mAdapterGuru!!.mItems[position]}",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun SearchSpinner(
        spinner_search: Array<String>, edit_txt: SearchableSpinner
    ) {
        val searchmethod = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, spinner_search
        )
        searchmethod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        edit_txt.adapter = searchmethod
    }

    private val mOnItemSelectedListener_utsav: AdapterView.OnItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
//            TODO("Not yet implemented")
                Log.d("Name", utsavName[position])
                Log.d("Postion", utsavID[position])
                UTSAV_ID = utsavName[position]
//            UTSAV_ID = utsavID[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
//            TODO("Not yet implemented")
            }
        }

    /*Relationship API*/
    private fun myRelationship() {
        val pd = CustomProgressBar(this@AddSankhyaActivity)
        pd.show()
        val call: Call<Get_Sankhya_Utsav_Response> =
            MyHssApplication.instance!!.api.get_sankhya_utsav()
        call.enqueue(object : Callback<Get_Sankhya_Utsav_Response> {
            override fun onResponse(
                call: Call<Get_Sankhya_Utsav_Response>,
                response: Response<Get_Sankhya_Utsav_Response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {

                        var utsavBeans: List<Get_Sankhya_Utsav_Datum> =
                            ArrayList<Get_Sankhya_Utsav_Datum>()

                        utsavBeans = response.body()!!.data!!
                        Log.d("atheletsBeans", utsavBeans.toString())

                        utsavName = listOf(arrayOf(utsavBeans).toString())
                        utsavID = listOf(arrayOf(utsavBeans).toString())

                        val mStringList = java.util.ArrayList<String>()
                        mStringList.add("Please Select Utsav")
                        for (i in 0 until utsavBeans.size) {
                            mStringList.add(
                                utsavBeans[i].utsav.toString()
                            )
                        }

                        val mStringListnew = java.util.ArrayList<String>()
                        mStringListnew.add("-999")
                        for (i in 0 until utsavBeans.size) {
                            mStringListnew.add(
                                utsavBeans[i].id.toString()
                            )
                        }

                        var mStringArray = mStringList.toArray()
                        var mStringArraynew = mStringListnew.toArray()

                        for (i in mStringArray.indices) {
                            Log.d("string is", mStringArray[i] as String)
                        }

                        for (i in mStringArraynew.indices) {
                            Log.d("mStringArraynew is", mStringArraynew[i] as String)
                        }

                        mStringArray = mStringList.toArray(mStringArray)
                        mStringArraynew = mStringListnew.toArray(mStringArraynew)

                        val list: java.util.ArrayList<String> = arrayListOf<String>()
                        val listnew: java.util.ArrayList<String> = arrayListOf<String>()

                        for (element in mStringArray) {
                            Log.d("LIST==>", element.toString())
                            list.add(element.toString())
                            Log.d("list==>", list.toString())

                            val listn = arrayOf(element)
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                utsavName = list
                            }
                        }

                        for (element in mStringArraynew) {
                            Log.d("LIST==>", element.toString())
                            listnew.add(element.toString())
                            Log.d("list==>", listnew.toString())

                            val listn = arrayOf(element)
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                utsavID = listnew
                            }
                        }

                        Log.d("relationshipName==>", utsavName.toString())

                        SearchSpinner(utsavName.toTypedArray(), utsav_txt)

                    } else {
                        Functions.displayMessage(this@AddSankhyaActivity, response.body()?.message)
//                        Functions.showAlertMessageWithOK(
//                            this@AddSankhyaActivity, "",
////                        "Message",
//                            response.body()?.message
//                        )
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@AddSankhyaActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Get_Sankhya_Utsav_Response>, t: Throwable) {
                Toast.makeText(this@AddSankhyaActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    private fun mySankhyaList(
        user_id: String,
        chapter_id: String,
        length: String,
        start: String,
        search: String,
        start_date: String,
        end_date: String
    ) {
        val pd = CustomProgressBar(this@AddSankhyaActivity)
        pd.show()
        val call: Call<Sankhya_List_Response> = MyHssApplication.instance!!.api.get_sankhya_listing(
            user_id, chapter_id, length, start, search, start_date, end_date
        )
        call.enqueue(object : Callback<Sankhya_List_Response> {
            override fun onResponse(
                call: Call<Sankhya_List_Response>, response: Response<Sankhya_List_Response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {

                        try {
                            atheletsBeans = response.body()!!.data!!

//                        for (i in 1 until atheletsBeans.size) {
//                            UserName = listOf(atheletsBeans[i].chapterName.toString())
//                            UserCategory = listOf(atheletsBeans[i].id.toString())
//                        }
//
//                        mAdapterGuru = SankhyaAdapter(atheletsBeans, selectAllItems, selected_user)
//
//                        attended_list.adapter = mAdapterGuru
//
//                        mAdapterGuru!!.notifyDataSetChanged()

                        } catch (e: ArithmeticException) {
                            println(e)
                        } finally {
                            println("Family")
                        }
                    } else {
                        Functions.showAlertMessageWithOK(
                            this@AddSankhyaActivity, "",
//                        "Message",
                            response.body()?.message
                        )
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@AddSankhyaActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Sankhya_List_Response>, t: Throwable) {
                Toast.makeText(this@AddSankhyaActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    private fun myFamily(user_id: String) {
        val pd = CustomProgressBar(this@AddSankhyaActivity)
        pd.show()
        val call: Call<my_family_response> = MyHssApplication.instance!!.api.get_members(user_id)
        call.enqueue(object : Callback<my_family_response> {
            override fun onResponse(
                call: Call<my_family_response>, response: Response<my_family_response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {

                        try {
                            athelets_Beans = response.body()!!.data!!
                            Log.d("atheletsBeans", athelets_Beans.toString())

//                        UserName = listOf(arrayOf(athelets_Beans).toString())
//                        UserCategory = listOf(arrayOf(athelets_Beans).toString())

                            val mStringList = java.util.ArrayList<String>()
                            for (i in 0 until athelets_Beans.size) {
                                mStringList.add(
                                    athelets_Beans[i].firstName.toString() + " " + athelets_Beans[i].lastName.toString()
                                )
                            }

                            val mStringListnew = java.util.ArrayList<String>()
                            for (i in 0 until athelets_Beans.size) {
                                mStringListnew.add(
                                    athelets_Beans[i].memberId.toString()
                                )
                            }

                            var mStringArray = mStringList.toArray()
                            var mStringArraynew = mStringListnew.toArray()

                            for (i in mStringArray.indices) {
                                Log.d("string is", mStringArray[i] as String)
                            }

                            for (i in mStringArraynew.indices) {
                                Log.d("mStringArraynew is", mStringArraynew[i] as String)
                            }

                            mStringArray = mStringList.toArray(mStringArray)
                            mStringArraynew = mStringListnew.toArray(mStringArraynew)

                            val list: java.util.ArrayList<String> = arrayListOf<String>()
                            val listnew: java.util.ArrayList<String> = arrayListOf<String>()

                            for (element in mStringArray) {
                                list.add(element.toString())
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                    UserName = list
                                }
                            }

                            for (element in mStringArraynew) {
                                listnew.add(element.toString())
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                    UserCategory = listnew
                                }
                            }

//                            mAdapterGurunew = StudentAdapter(
//                                this@AddSankhyaActivity, athelets_Beans
////                                prepareData()
//                            )
//
//                            attended_list.adapter = mAdapterGurunew
//
//                            mAdapterGurunew!!.notifyDataSetChanged()

                            approveRecyclerView = ApproveRecyclerView(
                                this@AddSankhyaActivity,
                                athelets_Beans,
                                arrayListUser,
                                arrayListUserId,
                                selectAllItems,
                                arrayData
                            )

                            /*mAdapterGuru = SankhyaAdapter(
                                athelets_Beans,
                                selectAllItems,
                                selected_user,
                                selected_userName
                            )

                            attended_list.adapter = mAdapterGuru*/

                            val adapter = ArrayAdapter(
                                this@AddSankhyaActivity,
                                R.layout.simple_list_item_multiple_choice,
                                UserName
                            )
                            /** Setting the arrayadapter for this listview   */
                            attendedlist.adapter = adapter
                            /** Defining checkbox click event listener  */
                            val clickListener = View.OnClickListener { v ->
                                arrayListUser.clear()
                                arrayListUserId.clear()
                                val chk = v as CheckBox
                                val itemCount = attendedlist.count
                                for (i in 0 until itemCount) {
                                    attendedlist.setItemChecked(i, chk.isChecked)

                                    if (chk.isChecked) {
                                        arrayListUser.add(UserName[i])
                                        arrayListUserId.add(UserCategory[i])
                                    } else {
                                        arrayListUser.remove(UserName[i])
                                        arrayListUserId.remove(UserCategory[i])
                                    }
                                }
                                DebugLog.d("arrayListUser=====> " + arrayListUser.toString())
                                DebugLog.d("arrayListUser SIZE=====> " + arrayListUser.size)
                                DebugLog.d("arrayListUserId=====> " + arrayListUserId.toString())
                                txt_sankhya_count.text = arrayListUser.size.toString()
                            }

                            /** Defining click event listener for the listitem checkbox  */
                            val itemClickListener =
                                AdapterView.OnItemClickListener { arg0, arg1, position, arg3 ->
                                    val checkedTextView = arg1 as CheckedTextView
                                    val isSelected = checkedTextView.isChecked
                                    if (isSelected) {
                                        arrayListUser.add(UserName[position])
                                        arrayListUserId.add(UserCategory[position])
                                    } else {
                                        arrayListUser.remove(UserName[position])
                                        arrayListUserId.remove(UserCategory[position])
                                    }
                                    cbSelectAll.isChecked = attendedlist.count == checkedItemCount
                                    txt_sankhya_count.text = arrayListUser.size.toString()
                                }

                            cbSelectAll.setOnClickListener(clickListener)
                            /** Setting a click listener for the listitem checkbox  */
                            attendedlist.onItemClickListener = itemClickListener

//                            attended_list.adapter = approveRecyclerView
//                            approveRecyclerView!!.notifyDataSetChanged()
//                            mAdapterGuru!!.notifyDataSetChanged()

                        } catch (e: ArithmeticException) {
                            println(e)
                        } finally {
                            println("Family")
                        }
                    } else {
                        Functions.showAlertMessageWithOK(
                            this@AddSankhyaActivity,
                            "Message",
                            getString(R.string.can_not_add_sankhya)
                        )
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@AddSankhyaActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<my_family_response>, t: Throwable) {
                Toast.makeText(this@AddSankhyaActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    private val checkedItemCount: Int
        private get() {
            var cnt = 0
            val positions = attendedlist.checkedItemPositions
            val itemCount = attendedlist.count
            for (i in 0 until itemCount) {
                if (positions[i]) cnt++
            }
            return cnt
        }

    fun prepareData(): List<Datum?>? {
        try {
            var studentList: List<Datum?>? = ArrayList()
            val stream: InputStream = assets.open("Student.json")
            val reader = BufferedReader(InputStreamReader(stream))
            val builder = StringBuilder()
            var line: String? = ""
            while (reader.readLine().also { line = it } != null) {
                builder.append(line)
            }
            val data = builder.toString()
            studentList = Gson().fromJson<List<Datum?>>(
                data, object : TypeToken<List<Datum?>?>() {}.type
            )
            Functions.printLog("studentList", studentList.toString())
            return studentList
        } catch (e: Exception) {
            //java.lang.IllegalStateException: Expected BEGIN_ARRAY but was BEGIN_OBJECT at line 1 column 2 path $
            //dont keep root key...
            e.printStackTrace()
        }
        return null
    }

    /*Add Sankhya API*/
    private fun AddSankhya(
        user_id: String,
        member_id: String,
        org_chapter_id: String,
        utsav: String,
        event_date: String,
        shishu_male: String,
        shishu_female: String,
        baal: String,
        baalika: String,
        kishore: String,
        kishori: String,
        tarun: String,
        taruni: String,
        yuva: String,
        yuvati: String,
        proudh: String,
        proudha: String,
        api: String
    ) {
        val pd = CustomProgressBar(this@AddSankhyaActivity)
        pd.show()
        val call: Call<Get_Sankhya_Add_Response> = MyHssApplication.instance!!.api.get_sankhya_add(
            user_id,
            member_id,
            org_chapter_id,
            event_date,
            utsav,
            shishu_male,
            shishu_female,
            baal,
            baalika,
            kishore,
            kishori,
            tarun,
            taruni,
            yuva,
            yuvati,
            proudh,
            proudha,
            api
        )
        call.enqueue(object : Callback<Get_Sankhya_Add_Response> {
            override fun onResponse(
                call: Call<Get_Sankhya_Add_Response>, response: Response<Get_Sankhya_Add_Response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {

                        val alertBuilder =
                            AlertDialog.Builder(this@AddSankhyaActivity) // , R.style.dialog_custom

                        alertBuilder.setTitle("Message")
                        alertBuilder.setMessage(response.body()?.message)
                        alertBuilder.setPositiveButton(
                            "OK"
                        ) { dialog, which ->
                            startActivity(
                                Intent(
                                    this@AddSankhyaActivity, SankhyaActivity::class.java
                                )
                            )
                            finish()
                        }
                        val alertDialog = alertBuilder.create()
                        alertDialog.show()

//                    Functions.showAlertMessageWithOK(
//                        this@AddSankhyaActivity,
//                        "Message",
//                        response.body()?.message
//                    )

                    } else {
                        Functions.showAlertMessageWithOK(
                            this@AddSankhyaActivity, "Message", response.body()?.message
                        )
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@AddSankhyaActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Get_Sankhya_Add_Response>, t: Throwable) {
                Toast.makeText(this@AddSankhyaActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        TODO("Not yet implemented")
        Log.v("Bhanu", "onItemClick ${position}")

        Toast.makeText(
            this@AddSankhyaActivity,
            "Clicked: ${mAdapterGuru!!.mItems[position]}",
            Toast.LENGTH_SHORT
        ).show()
    }

}

private fun RecyclerView.setOnItemClickListener(clickListener: SankhyaAdapter.ClickListener) {
//    TODO("Not yet implemented")
}
