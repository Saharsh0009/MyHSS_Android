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
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.DebugLog
import com.myhss.Utils.Functions
import com.myhss.dialog.DialogSearchableSpinner
import com.myhss.dialog.iDialogSearchableSpinner
import com.myhss.ui.sankhya_report.Adapter.ApproveRecyclerView
import com.myhss.ui.sankhya_report.Model.sankhya.ActiveMember
import com.myhss.ui.sankhya_report.Model.sankhya.SankhyaList
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.ui.guru_dakshina.Model.Get_Sankhya_Add_Response
import com.uk.myhss.ui.my_family.Model.Datum
import com.uk.myhss.ui.policies.SankhyaActivity
import com.uk.myhss.ui.sankhya_report.Adapter.SankhyaAdapter
import com.uk.myhss.ui.sankhya_report.Model.Get_Sankhya_Utsav_Response
import com.uk.myhss.ui.sankhya_report.Model.Sankhya_Datum
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AddSankhyaActivity : AppCompatActivity(), iDialogSearchableSpinner {

    private lateinit var sessionManager: SessionManager

    private var year = 0
    private var month = 0
    private var day = 0
    private lateinit var calendar: Calendar

    //    var utsavName: List<String> = ArrayList<String>()
//    var utsavID: List<String> = ArrayList<String>()
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
    private lateinit var utsav_txt: TextView
    private var atheletsBeans: List<Sankhya_Datum> = ArrayList<Sankhya_Datum>()
    private var athelets_Beans: List<ActiveMember> = ArrayList<ActiveMember>()
    private var currentSelectedItems: List<Datum> = ArrayList<Datum>()
    private var selected_user: ArrayList<String> = ArrayList<String>()
    private var selected_userName: ArrayList<String> = ArrayList<String>()
    private var selected_userNameAll: ArrayList<String> = ArrayList<String>()
    private var selected_userAll: ArrayList<String> = ArrayList<String>()
    private var mAdapterGuru: SankhyaAdapter? = null
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
        utsav_txt.text = "Select Utsav"
        shakha_name_txt.text = sessionManager.fetchSHAKHANAME()!!.capitalize(Locale.ROOT)
        cbSelectAll = findViewById(R.id.cbSelectAll)
        tvSelect = findViewById(R.id.tvSelect)
        mLayoutManager = LinearLayoutManager(this@AddSankhyaActivity)
        attended_list.layoutManager = mLayoutManager

        actualDate = Calendar.getInstance()
        TODAY_DATE = sdf.format(actualDate.time)

        current_date_txt.text = sdfnew.format(actualDate.time)
        EVENT_DATE = sdf.format(actualDate.time)

        val smsTime = Calendar.getInstance()
        smsTime.timeInMillis = actualDate.timeInMillis

        previous_date.setOnClickListener(DebouncedClickListener {
            actualDate.add(Calendar.DAY_OF_MONTH, -1)
            current_date_txt.text = sdfnew.format(actualDate.time)
            next_date.setBackgroundResource(R.drawable.button_round)
        })

        if (current_date_txt.text.toString() == current_date_txt.text.toString()) {
            next_date.setBackgroundResource(R.drawable.gray_round_border)
        } else {
            next_date.setBackgroundResource(R.drawable.button_round)

            next_date.setOnClickListener(DebouncedClickListener {
                actualDate.add(Calendar.DAY_OF_MONTH, 1)
                current_date_txt.text = sdfnew.format(actualDate.time)
                next_date.setBackgroundResource(R.drawable.button_round)
            })
        }

        current_date_txt.setOnClickListener(DebouncedClickListener {
            calendar = Calendar.getInstance()
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH)
            day = calendar.get(Calendar.DAY_OF_MONTH)

            val dialog = DatePickerDialog(this, { _, year, month, day_of_month ->
                calendar[Calendar.YEAR] = year
                calendar[Calendar.MONTH] = month //+ 1
                calendar[Calendar.DAY_OF_MONTH] = day_of_month
                current_date_txt.text = sdfnew.format(calendar.time)
                EVENT_DATE = sdf.format(calendar.time)

            }, year, month, day)
            dialog.show()
        })

        calender_icon.setOnClickListener(DebouncedClickListener {
            calendar = Calendar.getInstance()
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH)
            day = calendar.get(Calendar.DAY_OF_MONTH)

            val dialog = DatePickerDialog(this, { _, year, month, day_of_month ->
                calendar[Calendar.YEAR] = year
                calendar[Calendar.MONTH] = month //+ 1
                calendar[Calendar.DAY_OF_MONTH] = day_of_month
                current_date_txt.text = sdfnew.format(calendar.time)
                EVENT_DATE = sdf.format(calendar.time)

            }, year, month, day)
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
            mySankhyaMemberList(sessionManager.fetchSHAKHAID().toString())
            apiUtsavList()
        } else {
            Toast.makeText(
                this@AddSankhyaActivity,
                resources.getString(R.string.no_connection),
                Toast.LENGTH_SHORT
            ).show()
        }
        sessionManager.saveSELECTED_ALL("")
        sessionManager.saveSELECTED_TRUE("")
        sessionManager.saveSELECTED_FALSE("")

        cbSelectAll.visibility = View.VISIBLE

        additional_guest_layout.setOnClickListener(DebouncedClickListener {
            for (num in arrayListUserId) {      // iterate through the second list
                if (!arrayListUserId.contains(num)) {   // if first list doesn't contain current element
                    arrayListUserId.add(num) // add it to the first list
                }
            }

            for (num in arrayListUser) {      // iterate through the second list
                if (!arrayListUser.contains(num)) {   // if first list doesn't contain current element
                    arrayListUser.add(num) // add it to the first list
                }
            }

            for (x in selected_userAll) {
                if (!selected_user.contains(x)) selected_user.add(x)
            }

            for (x in selected_userNameAll) {
                if (!selected_userName.contains(x)) selected_userName.add(x)
            }

            USER_ID = sessionManager.fetchUserID()!!
            MEMBER_ID = arrayListUserId.toString().replace("[", "").replace("]", "")
            MEMBER_NAME = arrayListUser.toString()

            ORG_CHAPTER_ID = sessionManager.fetchSHAKHAID()!!


            EVENTDATE = EVENT_DATE
//            if (UTSAV_NAME == "") {
//                Snackbar.make(root_view, "Please select the Utsav", Snackbar.LENGTH_SHORT).show()
//            } else
            if (MEMBER_ID == "") {
                Snackbar.make(root_view, "Please select the Member", Snackbar.LENGTH_SHORT).show()
            } else {
                val i = Intent(this@AddSankhyaActivity, SankhyaFormDetail::class.java)
                i.putExtra("SANKHYA", "SANKHYA")
                i.putExtra("SANKHYA_ID", "")
                i.putExtra("CURRENT_DATE", EVENTDATE)
                i.putExtra("UTSAVE_ID", UTSAV_ID)
                i.putExtra("UTSAV_NAME", UTSAV_NAME)
                i.putExtra("USER_ID", USER_ID)
                i.putExtra("MEMBER_ID", MEMBER_ID)
                i.putExtra("ORG_CHAPTER_ID", ORG_CHAPTER_ID)
                i.putExtra("EVENT_DATE", EVENT_DATE)
                i.putStringArrayListExtra("MEMBER_NAME", arrayListUser)
                i.putStringArrayListExtra("USERNAME_LIST", UserName)
                i.putStringArrayListExtra("USERID_LIST", UserCategory)
                i.putExtra("SHAKHA_NAME", shakha_name_txt.text.toString())
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

            for (num in arrayListUserId) {      // iterate through the second list
                if (!arrayListUserId.contains(num)) {   // if first list doesn't contain current element
                    arrayListUserId.add(num) // add it to the first list
                }
            }

            for (x in selected_userAll) {
                if (!selected_user.contains(x)) selected_user.add(x)
            }

            for (x in selected_userNameAll) {
                if (!selected_userName.contains(x)) selected_userName.add(x)
            }

            USER_ID = sessionManager.fetchUserID()!!
            MEMBER_ID = arrayListUserId.toString().replace("[", "").replace("]", "")

            ORG_CHAPTER_ID = sessionManager.fetchSHAKHAID()!!
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
//            if (UTSAV_NAME == "") {
//                Snackbar.make(root_view, "Please select the Utsav", Snackbar.LENGTH_SHORT).show()
//            } else
            if (MEMBER_ID == "") {
                Snackbar.make(root_view, "Please select the Member", Snackbar.LENGTH_SHORT).show()
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
    }

    /*Relationship API*/
    private fun apiUtsavList() {
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
                    if (response.body()?.status!!) {
                        var utsavBeans = response.body()!!.data!!
                        var utsavNameList: MutableList<String> = mutableListOf()
                        var utsavIDList: MutableList<String> = mutableListOf()
                        for (i in utsavBeans.indices) {
                            utsavNameList.add(utsavBeans[i].utsav.toString())
                            utsavIDList.add(utsavBeans[i].id.toString())
                        }

                        utsav_txt.setOnClickListener(DebouncedClickListener {
                            openSearchableSpinnerDialog(
                                "1",
                                "Select Utsav",
                                utsavNameList,
                                utsavIDList
                            )
                        })

                    } else {
                        Functions.displayMessage(this@AddSankhyaActivity, response.body()?.message)
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

    private fun mySankhyaMemberList(user_id: String) {
        val pd = CustomProgressBar(this@AddSankhyaActivity)
        pd.show()
        val call: Call<SankhyaList> = MyHssApplication.instance!!.api.get_sankhyaList(user_id)
        call.enqueue(object : Callback<SankhyaList> {
            override fun onResponse(
                call: Call<SankhyaList>, response: Response<SankhyaList>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body()?.status!!) {
                        try {
                            athelets_Beans = response.body()!!.active_member!!
                            val mStringList = java.util.ArrayList<String>()
                            for (i in 0 until athelets_Beans.size) {
                                mStringList.add(
                                    athelets_Beans[i].first_name.toString() + " " + athelets_Beans[i].last_name.toString()
                                )
                            }
                            val mStringListnew = java.util.ArrayList<String>()
                            for (i in 0 until athelets_Beans.size) {
                                mStringListnew.add(
                                    athelets_Beans[i].member_id.toString()
                                )
                            }
                            var mStringArray = mStringList.toArray()
                            var mStringArraynew = mStringListnew.toArray()

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

                            approveRecyclerView = ApproveRecyclerView(
                                this@AddSankhyaActivity,
                                athelets_Beans,
                                arrayListUser,
                                arrayListUserId,
                                selectAllItems,
                                arrayData
                            )

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

            override fun onFailure(call: Call<SankhyaList>, t: Throwable) {
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
        submit_layout.isEnabled = false
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
                    if (response.body()?.status!!) {
                        val alertBuilder =
                            AlertDialog.Builder(this@AddSankhyaActivity)
                        alertBuilder.setMessage(response.body()?.message)
                        alertBuilder.setPositiveButton(
                            "OK"
                        ) { dialog, which ->
                            val intent = Intent(this@AddSankhyaActivity, SankhyaActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                            finish()
                        }
                        val alertDialog = alertBuilder.create()
                        alertDialog.show()
                        alertDialog.setCancelable(false)
                        alertDialog.setCanceledOnTouchOutside(false)
                    } else {
                        Functions.showAlertMessageWithOK(
                            this@AddSankhyaActivity, "Message", response.body()?.message
                        )
                        submit_layout.isEnabled = true
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@AddSankhyaActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                    submit_layout.isEnabled = true
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Get_Sankhya_Add_Response>, t: Throwable) {
                Toast.makeText(this@AddSankhyaActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
                submit_layout.isEnabled = true
            }
        })
    }

    private fun openSearchableSpinnerDialog(
        sType: String,
        sTitle: String,
        ItemName: List<String>,
        ItemID: List<String>
    ) {
        val fragment = supportFragmentManager.findFragmentByTag("DialogSearchableSpinner")
        if (fragment == null) {
            val dialogSearch = DialogSearchableSpinner.newInstance(
                this,
                sType,
                sTitle,
                ItemName,
                ItemID
            )
            dialogSearch.show(supportFragmentManager, "DialogSearchableSpinner")
        }
    }

    override fun searchableItemSelectedData(stype: String, sItemName: String, sItemID: String) {
        UTSAV_ID = sItemID
        UTSAV_NAME = sItemName
        utsav_txt.text = sItemName
    }

}

