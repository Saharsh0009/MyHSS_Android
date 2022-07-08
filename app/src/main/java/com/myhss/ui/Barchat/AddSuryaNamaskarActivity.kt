package com.myhss.ui.Barchat

//import com.myhss.AllShakha.Adapter.ProductListAdapter

import android.R.id.message
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
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
import com.myhss.Utils.Functions
import com.myhss.Utils.InputFilterMinMax
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
    lateinit var select_date: TextView
    lateinit var select_date1: TextView
    lateinit var select_date2: TextView
    lateinit var edit_count: EditText
    lateinit var edit_count1: EditText
    lateinit var edit_count2: EditText
    lateinit var submit_layout: LinearLayout
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
            "AddSuryaNamaskarActivityVC",
            "AddSuryaNamaskarActivity"
        )

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        back_arrow = findViewById(R.id.back_arrow)
        header_title = findViewById(R.id.header_title)
        submit_layout = findViewById(R.id.submit_layout)
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

        select_date = findViewById(R.id.select_date)
        select_date1 = findViewById(R.id.select_date1)
        select_date2 = findViewById(R.id.select_date2)

        edit_count = findViewById(R.id.edit_count)
        edit_count1 = findViewById(R.id.edit_count1)
        edit_count2 = findViewById(R.id.edit_count2)

        edit_count.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.toString().length === 1 && s.toString().startsWith("0")) {
                    s.clear()
                }
            }
        })

        edit_count1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.toString().length === 1 && s.toString().startsWith("0")) {
                    s.clear()
                }
            }
        })

        edit_count2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.toString().length === 1 && s.toString().startsWith("0")) {
                    s.clear()
                }
            }
        })

//        spinner = findViewById(R.id.mySpinner)

        family_txt.onItemSelectedListener = mOnItemSelectedListener_family
        family_txt.setTitle("Select Family Member")

        val btnOk = findViewById(R.id.btnOk) as TextView
        val add_more_btn = findViewById(R.id.add_more_btn) as TextView
        val btnCancel = findViewById(R.id.btnCancel) as TextView


        var calendar = Calendar.getInstance(TimeZone.getDefault())

        val currentYear = calendar[Calendar.YEAR]
        val currentMonth = calendar[Calendar.MONTH] + 1
        val currentDay = calendar[Calendar.DAY_OF_MONTH]

        Log.e("CurrentDate", "" + currentDay + "/" + currentMonth + "/" + currentYear)
        Log.e("PreviousDate", "" + (currentDay - 1) + "/" + currentMonth + "/" + currentYear)
        Log.e("LastPrivousDate", "" + (currentDay - 2) + "/" + currentMonth + "/" + currentYear)

        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -1)

        val current = Date(System.currentTimeMillis() - 1000 * 60 * 60)
        val Previous = Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24)
        val LastPrivous = Date(System.currentTimeMillis() - 1000 * 60 * 60 * 48)
        dateFormat.format(cal.time)

        val _current = dateFormat.format(current)
        val _Previous = dateFormat.format(Previous)
        val Last_Privous = dateFormat.format(LastPrivous)

        Log.e("_currentnew", "" + _current)
        Log.e("PreviousDatenew", "" + _Previous)
        Log.e("LastPrivousDatenew", "" + Last_Privous)

        select_date.text = "" + _current
        select_date1.text = "" + _Previous
        select_date2.text = "" + Last_Privous

        select_date.setOnClickListener {
            calendar = Calendar.getInstance()
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH)
            day = calendar.get(Calendar.DAY_OF_MONTH)

            val dialog = DatePickerDialog(this, { _, year, month, day_of_month ->
                calendar[Calendar.YEAR] = year
                calendar[Calendar.MONTH] = month //+ 1
                calendar[Calendar.DAY_OF_MONTH] = day_of_month
                select_date.text = sdf.format(calendar.time)

            }, year, month, day)
            dialog.show()
        }

        select_date1.setOnClickListener {
            calendar = Calendar.getInstance()
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH)
            day = calendar.get(Calendar.DAY_OF_MONTH)

            val dialog = DatePickerDialog(this, { _, year, month, day_of_month ->
                calendar[Calendar.YEAR] = year
                calendar[Calendar.MONTH] = month //+ 1
                calendar[Calendar.DAY_OF_MONTH] = day_of_month
                select_date1.text = sdf.format(calendar.time)

            }, year, month, day)
            dialog.show()
        }

        select_date2.setOnClickListener {
            calendar = Calendar.getInstance()
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH)
            day = calendar.get(Calendar.DAY_OF_MONTH)

            val dialog = DatePickerDialog(this, { _, year, month, day_of_month ->
                calendar[Calendar.YEAR] = year
                calendar[Calendar.MONTH] = month //+ 1
                calendar[Calendar.DAY_OF_MONTH] = day_of_month
                select_date2.text = sdf.format(calendar.time)

            }, year, month, day)
            dialog.show()
        }

        submit_layout.setOnClickListener {
            if (Functions.isConnectingToInternet(this)) {
//                AddSuryanamaskar(sessionManager.fetchMEMBERID()!!, myarray)
            } else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.no_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        add_more_btn.visibility = View.GONE
        add_more_btn.setOnClickListener {

        }

        btnOk.setOnClickListener {
            edit_count.filters = arrayOf<InputFilter>(
                InputFilterMinMax(
                    "1",
                    "100"
                )
            )

            edit_count1.filters = arrayOf<InputFilter>(
                InputFilterMinMax(
                    "1",
                    "100"
                )
            )

            edit_count2.filters = arrayOf<InputFilter>(
                InputFilterMinMax(
                    "1",
                    "100"
                )
            )

            if (edit_count.text.isEmpty() && edit_count1.text.isEmpty() && edit_count2.text.isEmpty()) {
                Toast.makeText(
                    this@AddSuryaNamaskarActivity,
                    "Please enter count",
                    Toast.LENGTH_SHORT
                ).show()
//            } else if (edit_count1.text.isEmpty()) {
//                Toast.makeText(
//                    this@AddSuryaNamaskarActivity,
//                    "Please enter count",
//                    Toast.LENGTH_SHORT
//                ).show()
//            } else if (edit_count2.text.isEmpty()) {
//                Toast.makeText(
//                    this@AddSuryaNamaskarActivity,
//                    "Please enter count",
//                    Toast.LENGTH_SHORT
//                ).show()
            } else {

                JSONestimate.put("count", edit_count.text.toString())
                JSONestimate.put("date", select_date.text.toString())

                JSONestimate1.put("count", edit_count1.text.toString())
                JSONestimate1.put("date", select_date1.text.toString())

                JSONestimate2.put("count", edit_count2.text.toString())
                JSONestimate2.put("date", select_date2.text.toString())

                myarray.put(JSONestimate)
                myarray.put(JSONestimate1)
                myarray.put(JSONestimate2)
                Log.d("JSONobject: ", JSONestimate.toString())
                Log.d("JSONArray : ", myarray.toString())

                Log.d("USER_ID : ", USER_ID)

//                scoreList.add()

//                var DATE: String = ""
//                var COUNT: String = ""

                if (edit_count.text.isNotBlank()) {
                    DATE = "" + select_date.text.toString()
                    COUNT = "" + edit_count.text.toString()
                }
                if (edit_count1.text.isNotBlank()) {
                    DATE = "" + select_date1.text.toString()
                    COUNT = "" + edit_count1.text.toString()
                }
                if (edit_count2.text.isNotBlank()) {
                    DATE = "" + select_date2.text.toString()
                    COUNT = "" + edit_count2.text.toString()
                }
                if (edit_count.text.isNotBlank() && edit_count1.text.isNotBlank()) {
                    DATE = "" + select_date.text.toString() + "," + select_date1.text.toString()
                    COUNT = "" + edit_count.text.toString() + "," + edit_count1.text.toString()
                }
                if (edit_count1.text.isNotBlank() && edit_count2.text.isNotBlank()) {
                    DATE = "" + select_date1.text.toString() + "," + select_date2.text.toString()
                    COUNT = "" + edit_count1.text.toString() + "," + edit_count2.text.toString()
                }
                if (edit_count.text.isNotBlank() && edit_count2.text.isNotBlank()) {
                    DATE = "" + select_date.text.toString() + "," + select_date2.text.toString()
                    COUNT = "" + edit_count.text.toString() + "," + edit_count2.text.toString()
                }
                if (edit_count.text.isNotBlank() && edit_count1.text.isNotBlank() && edit_count2.text.isNotBlank()) {

                    DATE =
                        "" + select_date.text.toString() + "," + select_date1.text.toString() + "," + select_date2.text.toString()
                    COUNT =
                        "" + edit_count.text.toString() + "," + edit_count1.text.toString() + "," + edit_count2.text.toString()

                }

                if (Functions.isConnectingToInternet(this)) {
                    if (DATE.isNotEmpty() && COUNT.isNotEmpty()) {
                        AddSuryanamaskar(USER_ID, myarray, DATE, COUNT)
                    } else {
                                                Functions.showAlertMessageWithOK(
                            this@AddSuryaNamaskarActivity,
                            "MyHss Surya Namaskar",
                            "Please check Date and Count Value"
                        )
                    }
//                    AddSuryanamaskar(sessionManager.fetchMEMBERID()!!, myarray.toString())
                    /*val pd = CustomProgressBar(this@AddSuryaNamaskarActivity)
                    pd.show()

                    val notebookUsers = JSONArray()
                    notebookUsers.put(myarray)

                    val jsonString: String = myarray.toString().replace("[{\"", "\"[{").replace("\"}]", "}]\"")
                    Log.e("jsonString", jsonString)
                    Log.e("notebookUsers", notebookUsers.toString())

                    val retrofit: Retrofit = Retrofit.Builder()
                        .baseUrl(MyHssApplication.BaseURL)
//                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    val apiInterface = retrofit.create(ApiInterface::class.java)

                    try {
                        val paramObject = JSONObject()
                        paramObject.put("member_id", USER_ID)
                        paramObject.put("surynamaskar", jsonString)
                        Log.e("paramObject", paramObject.toString())

                        val notebookUsers = JSONArray()
                        notebookUsers.put(paramObject)
                        Log.e("notebookUsers", notebookUsers.toString())
//                        Log.e("paramObject", paramObject.toString().replace("{", "[").replace("}", "]").replace("[[", "\"[{").replace("]]", "}]\"").replace("],", "},").replace("[\"", "{\""))
                        val userCall: Call<save_suryanamaskarResponse> = apiInterface.postsuryanamasakar_count(notebookUsers.toString())
//                        val userCall: Call<save_suryanamaskarResponse> = apiInterface.postsuryanamasakar_count(paramObject.toString().replace("{", "[").replace("}", "]").replace("[[", "\"[{").replace("]]", "}]\"").replace("],", "},").replace("[\"", "{\""))
//                        userCall.enqueue(this)
                        userCall.enqueue(object : Callback<save_suryanamaskarResponse> {
                            @SuppressLint("NotifyDataSetChanged")
                            override fun onResponse(
                                call: Call<save_suryanamaskarResponse>,
                                response: Response<save_suryanamaskarResponse>
                            ) {
                                if (response.code() == 200 && response.body() != null) {
                                    Log.d("status", response.body()?.status.toString())
                                    if (response.body()?.status!!) {
                                        Functions.showAlertMessageWithOK(
                                            this@AddSuryaNamaskarActivity,
                                            "MyHss Surya Namaskar",
                                            response.body()?.message
                                        )

                                        select_date.text = ""
                                        select_date1.text = ""
                                        select_date2.text = ""
                                        edit_count.setText("")
                                        edit_count1.setText("")
                                        edit_count2.setText("")

//                        startActivity(Intent(this@AddSuryaNamaskarActivity, SuryaNamaskar::class.java))
                                        finish()

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
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }*/
                } else {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.no_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

//            if (edit_count.text.toString().isNotEmpty()) {
//                edit_count.filters = arrayOf<InputFilter>(
//                    InputFilterMinMax(
//                        "1",
//                        "100"
//                    )
//                )
//                if (Integer.valueOf(edit_count.text.toString()) > 0 && Integer.valueOf(edit_count.text.toString()) <= 1000) {
//                    edit_count.text = edit_count.text
//                } else {
//                    Toast.makeText(this@AddSuryaNamaskarActivity, "Please enter count 1-100", Toast.LENGTH_SHORT)
//                        .show()
//                }
//
//            } else {
//                Toast.makeText(this@AddSuryaNamaskarActivity, "Please enter count", Toast.LENGTH_SHORT).show()
//            }
        }

        btnCancel.setOnClickListener {
            select_date.text = ""
            select_date1.text = ""
            select_date2.text = ""
            edit_count.setText("")
            edit_count1.setText("")
            edit_count2.setText("")
        }

    }

    private val mOnItemSelectedListener_family: AdapterView.OnItemSelectedListener = object :
        AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
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
        user_id: String, tab: String, member_id: String, status: String,
        length: String, start: String, search: String, chapter_id: String
    ) {
        val pd = CustomProgressBar(this@AddSuryaNamaskarActivity)
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

//                            spinner!!.setOnItemSelectedListener(this@AddSuryaNamaskarActivity)
//
//                            // Create an ArrayAdapter using a simple spinner layout and languages array
//                            val aa = ArrayAdapter(this@AddSuryaNamaskarActivity, android.R.layout.simple_spinner_item, UserName)
//                            // Set layout to use when the list of choices appear
//                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                            // Set Adapter to Spinner
//                            spinner!!.adapter = aa

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
//                        Functions.showAlertMessageWithOK(
//                            this@AddSuryaNamaskarActivity,
//                            "Message",
//                            response.body()?.message
//                        )
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
        spinner_search: Array<String>,
        edit_txt: SearchableSpinner
    ) {
        val searchmethod = ArrayAdapter(
            this, android.R.layout.simple_spinner_item,
            spinner_search
        )
        searchmethod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        edit_txt.adapter = searchmethod
    }

    private fun AddSuryanamaskar(
        member_id: String,
        gsonObject: JSONArray,
        DATE: String,
        COUNT: String
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
                            select_date.text = ""
                            select_date1.text = ""
                            select_date2.text = ""
                            edit_count.setText("")
                            edit_count1.setText("")
                            edit_count2.setText("")

                            startActivity(
                                Intent(
                                    this@AddSuryaNamaskarActivity,
                                    SuryaNamaskar::class.java
                                )
                            )
                            finish()
                                      Toast.makeText(this@AddSuryaNamaskarActivity, response.body()?.message, Toast.LENGTH_SHORT)
                        }, 1500)

                        val alertDialog: AlertDialog.Builder =
                            AlertDialog.Builder(this@AddSuryaNamaskarActivity)
                        alertDialog.setTitle("MyHss Surya Namaskar")
                        alertDialog.setMessage(response.body()?.message)
                        alertDialog.setPositiveButton(
                            "yes"
                        ) { _, _ ->
                            select_date.text = ""
                            select_date1.text = ""
                            select_date2.text = ""
                            edit_count.setText("")
                            edit_count1.setText("")
                            edit_count2.setText("")

                            startActivity(
                                Intent(
                                    this@AddSuryaNamaskarActivity,
                                    SuryaNamaskar::class.java
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
}