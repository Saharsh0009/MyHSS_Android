package com.myhss.ui.Barchat

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.google.gson.JsonParser
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.DebugLog
import com.myhss.Utils.Functions
import com.myhss.ui.Barchat.Model.save_suryanamaskarResponse
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.ui.linked_family.Model.Get_Member_Listing_Datum
import com.uk.myhss.ui.linked_family.Model.Get_Member_Listing_Response

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class AddSuryaNamaskarActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var sessionManager: SessionManager
    lateinit var back_arrow: ImageView
    lateinit var header_title: TextView

    lateinit var submit_layout: LinearLayout
    lateinit var layout_dynamic_view: LinearLayout
    lateinit var layout_spiner: LinearLayout
    lateinit var username: TextView
    private var DATE: String = ""
    private var COUNT: String = ""

    //    var spinner:Spinner? = null
    private lateinit var family_txt: SearchableSpinner

    var arrayListUser: ArrayList<String> = ArrayList()
    var arrayListUserId: ArrayList<String> = ArrayList()
    var UserName: ArrayList<String> = ArrayList<String>()
    var UserCategory: ArrayList<String> = ArrayList<String>()

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

    private var athelets_Beans: List<Get_Member_Listing_Datum> =
        ArrayList<Get_Member_Listing_Datum>()

    private var year = 0
    private var month = 0
    private var day = 0
    private lateinit var calendar: Calendar

    val sdf: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    //    val sdfnew: SimpleDateFormat = SimpleDateFormat("dd MMM | EEEE", Locale.getDefault())

    private var scoreList = ArrayList<String>()

    //    private var mAdapter: ProductListAdapter? = null
    //    private var modelToBeUpdated: Stack<ProductModel> = Stack()

    lateinit var add_listview: RecyclerView

    //    private val mOnProductClickListener = object : OnProductClickListener {
    //        fun onUpdate(position: Int, model: ProductModel) {
    //
    //            modelToBeUpdated.add(model)
    //
    //            productName.setText(model.name)
    //            productUnit.setText(model.price)
    //        }
    //
    //        fun onDelete(model: ProductModel) {
    //
    //            mProductListAdapter.removeProduct(model)
    //        }
    //    }


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
        submit_layout = findViewById(R.id.submit_layout)
        layout_dynamic_view = findViewById(R.id.layout_dynamic_view)
        layout_spiner = findViewById(R.id.layout_spiner)
        username = findViewById(R.id.username)

        header_title.text = getString(R.string.record_surya_namaskar)

        family_txt = findViewById(R.id.family_txt)
        add_listview = findViewById(R.id.add_listview)
        add_listview.layoutManager = LinearLayoutManager(this)
        add_listview.setHasFixedSize(true)

        back_arrow.setOnClickListener {
            finish()
        }

        if (Functions.isConnectingToInternet(this@AddSuryaNamaskarActivity)) {
            val end: Int = 100
            val start: Int = 0

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
                this@AddSuryaNamaskarActivity,
                resources.getString(R.string.no_connection),
                Toast.LENGTH_SHORT
            ).show()
        }

        val jsonObject = JSONObject()
        try {
            val subJsonObject = JSONObject()
            subJsonObject.put("date", "28/01/2022")
            subJsonObject.put("count", "1")
            //            subJsonObject.put("lastname", "xyz")
            //            jsonObject.put("customer", subJsonObject)
            //            jsonObject.put("password", "password")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val jsonParser = JsonParser()
        val gsonObject = jsonParser.parse(jsonObject.toString())
        Log.e("gsonObject", gsonObject.toString())

        val JSONestimate = JSONObject()
        val JSONestimate1 = JSONObject()
        val JSONestimate2 = JSONObject()
        val myarray = JSONArray()

        //        for (i in 0 until items.size()) {
        //            try {
        //                JSONestimate.put("date:" + (i + 1).toString(), items.get(i).getJSONObject())
        //                myarray.put(items.get(i).getJSONObject())
        //            } catch (e: JSONException) {
        //                e.printStackTrace()
        //            }
        //        }
        //        JSONestimate.put("date", "28/01/2022")
        //        JSONestimate.put("count", "1")
        //
        //        JSONestimate.put("date", "27/01/2022")
        //        JSONestimate.put("count", "5")
        //
        //        myarray.put(JSONestimate)
        //        Log.d("JSONobject: ", JSONestimate.toString())
        //        Log.d("JSONArray : ", myarray.toString())
        //        spinner = findViewById(R.id.mySpinner)

        family_txt.onItemSelectedListener = mOnItemSelectedListener_family
        family_txt.setTitle("Select Family Member")

        val btnOk = findViewById(R.id.btnOk) as TextView
        val add_more_btn = findViewById(R.id.add_more_btn) as TextView
        val btn_add_more = findViewById(R.id.btn_add_more) as TextView
        val btnCancel = findViewById(R.id.btnCancel) as TextView

        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -1)
        dateFormat.format(cal.time)

        submit_layout.setOnClickListener {
            if (Functions.isConnectingToInternet(this)) {
                //                AddSuryanamaskar(sessionManager.fetchMEMBERID()!!, myarray)
            } else {
                Toast.makeText(
                    this, resources.getString(R.string.no_connection), Toast.LENGTH_SHORT
                ).show()
            }
        }

        add_more_btn.visibility = View.GONE
        btn_add_more.setOnClickListener {
            val inflater =
                LayoutInflater.from(this).inflate(R.layout.include_suryanamaskar_dynamic_view, null)
            layout_dynamic_view.addView(inflater, layout_dynamic_view.childCount)

            val count = layout_dynamic_view.childCount
            manageDynamicView(layout_dynamic_view)
        }


        btnOk.setOnClickListener {
            val count = layout_dynamic_view.childCount
            var view: View?
            if (count == 0) {
                Toast.makeText(
                    this,
                    "Please Add Date and Count for Surya Namaskar by clicking Add More",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                for (i in 0 until count) {
                    view = layout_dynamic_view.getChildAt(i)
                    val select_date: TextView = view.findViewById(R.id.select_date)
                    val edit_count: EditText = view.findViewById(R.id.edit_count)
                    if (select_date.text.isEmpty() && edit_count.text.isEmpty()) {
                        Toast.makeText(
                            this,
                            "Please Enter the Date and Count for Surya Namaskar",
                            Toast.LENGTH_SHORT
                        ).show()
                        DATE = ""
                        COUNT = ""
                        break
                    } else if (select_date.text.isEmpty()) {
                        Toast.makeText(
                            this, "Please Enter the Date for Surya Namaskar", Toast.LENGTH_SHORT
                        ).show()
                        DATE = ""
                        COUNT = ""
                        break
                    } else if (edit_count.text.isEmpty()) {
                        Toast.makeText(
                            this, "Please Enter the Count for Surya Namaskar", Toast.LENGTH_SHORT
                        ).show()
                        DATE = ""
                        COUNT = ""
                        break
                    } else if (edit_count.text.toString().toInt() < 0 || edit_count.text.toString()
                            .toInt() > 100
                    ) {
                        Toast.makeText(
                            this,
                            "Please Enter the Count between 1 to 100 for the selected date",
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
                }

                if (Functions.isConnectingToInternet(this)) {
                    if (DATE.isNotEmpty() && COUNT.isNotEmpty()) {
                        AddSuryanamaskar(USER_ID, myarray, DATE, COUNT)
                    }
                } else {
                    Toast.makeText(
                        this, resources.getString(R.string.no_connection), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        btnCancel.setOnClickListener {
            val count = layout_dynamic_view.childCount
            var viewClear: View?
            for (i in 0 until count) {
                viewClear = layout_dynamic_view.getChildAt(i)
                val text_date: TextView = viewClear.findViewById(R.id.select_date)
                val edit_count: EditText = viewClear.findViewById(R.id.edit_count)
                text_date.setText("")
                edit_count.setText("")
            }
        }
    }

    private fun manageDynamicView(linear_count: LinearLayout) {
        var v: View?
        for (i in 0 until linear_count.childCount) {
            v = linear_count.getChildAt(i)
            val text_date: TextView = v.findViewById(R.id.select_date)
            val img_removeView: ImageView = v.findViewById(R.id.img_delete_view)
            text_date.setOnClickListener {
                setDateFoSuryaNamaskar(text_date)
            }
            img_removeView.setOnClickListener {
                removeItemFromDynamicView(i)
            }
        }
    }

    private fun removeItemFromDynamicView(indexToRemove: Int) {
        if (indexToRemove < 0 || indexToRemove >= layout_dynamic_view.childCount) {
            return
        }
        layout_dynamic_view.removeViewAt(indexToRemove)
        manageDynamicView(layout_dynamic_view)
    }


    private val mOnItemSelectedListener_family: AdapterView.OnItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                Log.d("Name", UserName[position])
                Log.d("Postion", UserCategory[position])
                USER_NAME = UserName[position]
                USER_ID = UserCategory[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //            TODO("Not yet implemented")
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
        val pd = CustomProgressBar(this@AddSuryaNamaskarActivity)
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
                        //                    if (response.body()!!.data!!.isNullOrEmpty()) {
                        try {
                            athelets_Beans = response.body()!!.data!!
                            Log.d("atheletsBeans", athelets_Beans.toString())

                            val mStringList = ArrayList<String>()
                            mStringList.add(sessionManager.fetchFIRSTNAME()!! + " " + sessionManager.fetchSURNAME()!!)
                            for (i in 0 until athelets_Beans.size) {
                                mStringList.add(
                                    athelets_Beans[i].firstName.toString() + " " + athelets_Beans[i].lastName.toString()
                                )
                            }

                            val mStringListnew = ArrayList<String>()
                            mStringListnew.add(sessionManager.fetchMEMBERID()!!)
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
                            SearchSpinner(UserName.toTypedArray(), family_txt)
                        } catch (e: ArithmeticException) {
                            println(e)
                        } finally {
                            println("Family")
                        }
                    } else {
                        family_txt.visibility = View.GONE
                        username.visibility = View.VISIBLE

                        USER_NAME = sessionManager.fetchUSERNAME()!!
                        USER_ID = sessionManager.fetchMEMBERID()!!
                        username.text = USER_NAME
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

    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
        Log.e("Spinner==>", "Selected : " + UserName[position])
        Log.e("Spinner==>", "Selected : " + UserCategory[position])
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

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

    private fun AddSuryanamaskar(
        member_id: String, gsonObject: JSONArray, DATE: String, COUNT: String
    ) {
        val fields: HashMap<String, String> = HashMap()
        //        fields["member_id"] = member_id
        //        fields["surynamaskar"] = gsonObject

        val jsonString: String =
            gsonObject.toString().replace("[{\"", "\"[{").replace("\"}]", "}]\"")
        Log.e("jsonString", jsonString)

        val paramObject = JSONObject()
        paramObject.put("member_id", member_id)
        paramObject.put("surynamaskar", jsonString)
        Log.e("paramObject", paramObject.toString())

        val notebookUsers = JSONArray()
        notebookUsers.put(paramObject)
        Log.e("notebookUsers", notebookUsers.toString())


        //        val paramObject = JSONObject()
        //        paramObject.put("member_id", member_id)
        //        paramObject.put("surynamaskar", "123")   //  gsonObject

        val listOfStringColumn: List<String> = ArrayList()
        //        val commaSeperatedString = listOfStringColumn.joinToString { it -> "\'${it.}\'" }

        val pd = CustomProgressBar(this)
        pd.show()
        val call: Call<save_suryanamaskarResponse> =
            MyHssApplication.instance!!.api.save_suryanamasakar_count(
                member_id, "", DATE, COUNT
                //            MyHssApplication.instance!!.api.postsuryanamasakar_count(notebookUsers.toString()
                //                paramObject.toString()
                //                fields
                //                member_id,  "[{\"06-02-2022\",\"15\"}  {\"03-02-2022\",\"25\"}]"//.encodeUtf8().toString()
                //                gsonObject as JsonObject
            )
        call.enqueue(object : Callback<save_suryanamaskarResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<save_suryanamaskarResponse>,
                response: Response<save_suryanamaskarResponse>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {
                        //                        Functions.showAlertMessageWithOK(
                        //                            this@AddSuryaNamaskarActivity,
                        //                            "MyHss Surya Namaskar",
                        //                            response.body()?.message
                        //                        )

                        Handler().postDelayed({
                            startActivity(
                                Intent(
                                    this@AddSuryaNamaskarActivity, SuryaNamaskar::class.java
                                )
                            )
                            finish()
                            Toast.makeText(
                                this@AddSuryaNamaskarActivity,
                                response.body()?.message,
                                Toast.LENGTH_SHORT
                            )
                        }, 1500)

                        val alertDialog: AlertDialog.Builder =
                            AlertDialog.Builder(this@AddSuryaNamaskarActivity)
                        alertDialog.setTitle("MyHss Surya Namaskar")
                        alertDialog.setMessage(response.body()?.message)
                        alertDialog.setPositiveButton(
                            "yes"
                        ) { _, _ ->
                            startActivity(
                                Intent(
                                    this@AddSuryaNamaskarActivity, SuryaNamaskar::class.java
                                )
                            )
                            finish()
                        }
                        //                        alertDialog.setNegativeButton(
                        //                            "No"
                        //                        ) { _, _ ->
                        //
                        //                        }
                        val alert: AlertDialog = alertDialog.create()
                        alert.setCanceledOnTouchOutside(false)
                        //                        alert.show()

                        //                        startActivity(Intent(this@AddSuryaNamaskarActivity, SuryaNamaskar::class.java))
                        //                        finish()

                        //                        val i = Intent(this@AddSuryaNamaskarActivity, HomeActivity::class.java)
                        //                        startActivity(i)
                        //                        finishAffinity()
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@AddSuryaNamaskarActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<save_suryanamaskarResponse>, t: Throwable) {
                Toast.makeText(this@AddSuryaNamaskarActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    fun setDateFoSuryaNamaskar(dateTextView: TextView) {
        calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(this, { _, year, month, day_of_month ->
            calendar[Calendar.YEAR] = year
            calendar[Calendar.MONTH] = month //+ 1
            calendar[Calendar.DAY_OF_MONTH] = day_of_month
            dateTextView.text = sdf.format(calendar.time)
        }, year, month, day)
        dialog.show()
    }
}