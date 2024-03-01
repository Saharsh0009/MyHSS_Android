package com.myhss.ui.suryanamaskar

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.Utils.CustomProgressDialog
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.Functions
import com.myhss.dialog.DialogSearchableSpinner
import com.myhss.dialog.iDialogSearchableSpinner
import com.myhss.ui.suryanamaskar.Model.save_suryanamaskarResponse
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.ui.linked_family.Model.Get_Member_Listing_Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class AddSuryaNamaskarActivity : AppCompatActivity(), iDialogSearchableSpinner {

    private lateinit var sessionManager: SessionManager
    private lateinit var btnOk: TextView
    lateinit var back_arrow: ImageView
    lateinit var header_title: TextView
    lateinit var layout_dynamic_view: LinearLayout
    lateinit var layout_spiner: LinearLayout
    private var DATE: String = ""
    private var COUNT: String = ""
    private lateinit var family_txt: TextView
    private var USER_NAME: String = ""
    private var USER_ID: String = ""
    lateinit var USERID: String
    lateinit var TAB: String
    lateinit var MEMBERID: String
    lateinit var STATUS: String
    lateinit var LENGTH: String
    lateinit var START: String
    lateinit var SEARCH: String
    lateinit var CHAPTERID: String
    private var year = 0
    private var month = 0
    private var day = 0
    private lateinit var calendar: Calendar

    val sdf: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    @SuppressLint("Range")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_suryanamaskar)

        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("AddSuryaNamaskarActivityVC")
        sessionManager.firebaseAnalytics.setUserProperty(
            "AddSuryaNamaskarActivityVC", "AddSuryaNamaskarActivity"
        )

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        back_arrow = findViewById(R.id.back_arrow)
        header_title = findViewById(R.id.header_title)

        layout_dynamic_view = findViewById(R.id.layout_dynamic_view)
        layout_spiner = findViewById(R.id.layout_spiner)
        header_title.text = getString(R.string.record_surya_namaskar)
        family_txt = findViewById(R.id.family_txt)
        back_arrow.setOnClickListener(DebouncedClickListener {
            finish()
        })

        if (Functions.isConnectingToInternet(this@AddSuryaNamaskarActivity)) {
            val end: Int = 100
            val start: Int = 0

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
                this@AddSuryaNamaskarActivity,
                resources.getString(R.string.no_connection),
                Toast.LENGTH_SHORT
            ).show()
        }
        family_txt.text = "Select Family Member"
        btnOk = findViewById(R.id.btnOk)
        val btn_add_more = findViewById(R.id.btn_add_more) as TextView
        val btnCancel = findViewById(R.id.btnCancel) as TextView

        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -1)
        dateFormat.format(cal.time)

        val inflater =
            LayoutInflater.from(this).inflate(R.layout.include_suryanamaskar_dynamic_view, null)
        layout_dynamic_view.addView(inflater, layout_dynamic_view.childCount)
        manageDynamicView(layout_dynamic_view)

        btn_add_more.setOnClickListener(DebouncedClickListener {
            val inflater_new =
                LayoutInflater.from(this).inflate(R.layout.include_suryanamaskar_dynamic_view, null)
            layout_dynamic_view.addView(inflater_new, layout_dynamic_view.childCount)
            manageDynamicView(layout_dynamic_view)
        })


        btnOk.setOnClickListener(DebouncedClickListener {
            val count = layout_dynamic_view.childCount
            var view: View?
            if (USER_ID == "") {
                Toast.makeText(
                    this,
                    getString(R.string.please_select_member_in_order_to_log_your_surya_namaskar_count),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (count == 0) {
                Toast.makeText(
                    this,
                    getString(R.string.please_add_date_and_count_for_surya_namaskar_by_clicking_add_more),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val dateSet = HashSet<String>()
                for (i in 0 until count) {
                    view = layout_dynamic_view.getChildAt(i)
                    val select_date: TextView = view.findViewById(R.id.select_date)
                    val edit_count: EditText = view.findViewById(R.id.edit_count)
                    if (select_date.text.isEmpty() && edit_count.text.isEmpty()) {
                        Toast.makeText(
                            this,
                            getString(R.string.please_fill_in_all_the_details_in_order_to_log_your_surya_namaskar_count),
                            Toast.LENGTH_SHORT
                        ).show()
                        DATE = ""
                        COUNT = ""
                        break
                    } else if (select_date.text.isEmpty()) {
                        Toast.makeText(
                            this,
                            getString(R.string.please_fill_in_all_the_details_in_order_to_log_your_surya_namaskar_count),
                            Toast.LENGTH_SHORT
                        ).show()
                        DATE = ""
                        COUNT = ""
                        break
                    } else if (edit_count.text.isEmpty()) {
                        Toast.makeText(
                            this,
                            getString(R.string.please_fill_in_all_the_details_in_order_to_log_your_surya_namaskar_count),
                            Toast.LENGTH_SHORT
                        ).show()
                        DATE = ""
                        COUNT = ""
                        break
                    } else if (edit_count.text.toString()
                            .toDouble() < 1 || edit_count.text.toString()
                            .toDouble() > 500
                    ) {
                        Toast.makeText(
                            this,
                            getString(R.string.please_enter_a_count_between_1_and_500),
                            Toast.LENGTH_SHORT
                        ).show()
                        DATE = ""
                        COUNT = ""
                        break
                    } else if (dateSet.contains(select_date.text.toString())) {
                        Toast.makeText(
                            this,
                            getString(R.string.please_enter_diverse_dates_in_order_to_log_your_surya_namaskar_count),
                            Toast.LENGTH_SHORT
                        ).show()
                        DATE = ""
                        COUNT = ""
                        break
                    } else {
                        when (i) {
                            0 -> {
                                DATE = "" + select_date.text.toString()
                                COUNT = "" + edit_count.text.toString()
                            }

                            else -> {
                                DATE += "," + select_date.text.toString()
                                COUNT += "," + edit_count.text.toString()
                            }
                        }
                    }
                    dateSet.add(select_date.text.toString())
                }

                if (Functions.isConnectingToInternet(this)) {
                    if (DATE.isNotEmpty() && COUNT.isNotEmpty()) {
                        AddSuryanamaskar(USER_ID, DATE, COUNT)
                    }
                } else {
                    Toast.makeText(
                        this, resources.getString(R.string.no_connection), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

        btnCancel.setOnClickListener(DebouncedClickListener {
            finish()
        })
    }

    private fun manageDynamicView(linear_count: LinearLayout) {

        var v: View?
        for (i in 0 until linear_count.childCount) {
            v = linear_count.getChildAt(i)
            val text_date: TextView = v.findViewById(R.id.select_date)
            val img_removeView: ImageView = v.findViewById(R.id.img_delete_view)
            text_date.setOnClickListener(DebouncedClickListener {
                setDateFoSuryaNamaskar(text_date)
            })
            img_removeView.setOnClickListener(DebouncedClickListener {
                removeItemFromDynamicView(i)
            })
        }
    }

    private fun removeItemFromDynamicView(indexToRemove: Int) {
        if (indexToRemove < 0 || indexToRemove >= layout_dynamic_view.childCount) {
            return
        }
        layout_dynamic_view.removeViewAt(indexToRemove)
        manageDynamicView(layout_dynamic_view)
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
        val pd = CustomProgressDialog(this@AddSuryaNamaskarActivity)
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
                    if (response.body()?.status!!) {
                        try {
                            val athelets_Beans = response.body()!!.data!!
                            val mStringListName = ArrayList<String>()
                            mStringListName.add(sessionManager.fetchFIRSTNAME()!! + " " + sessionManager.fetchSURNAME()!!)
                            for (i in 0 until athelets_Beans.size) {
                                mStringListName.add(
                                    athelets_Beans[i].firstName.toString() + " " + athelets_Beans[i].lastName.toString()
                                )
                            }

                            val mStringListID = ArrayList<String>()
                            mStringListID.add(sessionManager.fetchMEMBERID()!!)
                            for (i in 0 until athelets_Beans.size) {
                                mStringListID.add(
                                    athelets_Beans[i].memberId.toString()
                                )
                            }
                            family_txt.setOnClickListener(DebouncedClickListener {
                                openSearchableSpinnerDialog(
                                    "1",
                                    "Select Family Member",
                                    mStringListName,
                                    mStringListID
                                )
                            })
                        } catch (e: ArithmeticException) {
                            println(e)
                        } finally {
                            println("Family")
                        }
                    } else {
                        USER_NAME = sessionManager.fetchUSERNAME()!!
                        USER_ID = sessionManager.fetchMEMBERID()!!
                        family_txt.text = USER_NAME
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@AddSuryaNamaskarActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Get_Member_Listing_Response>, t: Throwable) {
                Toast.makeText(this@AddSuryaNamaskarActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    private fun AddSuryanamaskar(
        member_id: String, DATE: String, COUNT: String
    ) {
        btnOk.isEnabled = false
        val pd = CustomProgressDialog(this)
        pd.show()
        val call: Call<save_suryanamaskarResponse> =
            MyHssApplication.instance!!.api.save_suryanamasakar_count(member_id, DATE, COUNT)
        call.enqueue(object : Callback<save_suryanamaskarResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<save_suryanamaskarResponse>,
                response: Response<save_suryanamaskarResponse>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {
                        val alertDialog: AlertDialog.Builder =
                            AlertDialog.Builder(this@AddSuryaNamaskarActivity)
                        alertDialog.setTitle("MyHSS")
                        alertDialog.setMessage(response.body()?.message)
                        alertDialog.setCancelable(false)
                        alertDialog.setPositiveButton(
                            "OK"
                        ) { _, _ ->
                            startActivity(
                                Intent(
                                    this@AddSuryaNamaskarActivity, SuryaNamaskar::class.java
                                )
                            )
                            finishAffinity()
                            btnOk.isEnabled = true
                        }
                        val alert: AlertDialog = alertDialog.create()
                        alert.setCanceledOnTouchOutside(false)
                        alert.show()
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@AddSuryaNamaskarActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                    btnOk.isEnabled = true
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<save_suryanamaskarResponse>, t: Throwable) {
                Toast.makeText(this@AddSuryaNamaskarActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
                btnOk.isEnabled = true
            }
        })
    }

    fun setDateFoSuryaNamaskar(dateTextView: TextView) {
        calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)

        calendar.add(Calendar.MONTH, -3)

        val dialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            calendar[Calendar.YEAR] = year
            calendar[Calendar.MONTH] = month
            calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            dateTextView.text = sdf.format(calendar.time)
        }, year, month, day)
        dialog.datePicker.minDate = calendar.timeInMillis
        dialog.datePicker.maxDate = System.currentTimeMillis()
        dialog.show()
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
        USER_NAME = sItemName
        USER_ID = sItemID
        family_txt.text = sItemName
    }
}