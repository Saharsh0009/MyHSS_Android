package com.myhss.AllShakha

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.ParseException
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.AllShakha.Model.Datum_Get_Shakha_Details
import com.myhss.AllShakha.Model.Get_Shakha_Details_Response
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.Functions
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


class ShakhaDetailsActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    lateinit var rootLayout: RelativeLayout
    lateinit var shakha_name_txt: TextView
    lateinit var shakha_address_txt: TextView
    lateinit var parent_shakha_name_txt: TextView
    lateinit var parent_shakha_address_txt: TextView
    lateinit var contact_person_txt: TextView
    lateinit var organisation_name_txt: TextView
    lateinit var organisation_address_txt: TextView
    lateinit var email_name_txt: TextView
    lateinit var email_address_txt: TextView
    lateinit var phone_name_txt: TextView
    lateinit var phone_address_txt: TextView
    lateinit var day_name_txt: TextView
    lateinit var day_address_txt: TextView
    lateinit var time_name_txt: TextView
    lateinit var time_address_txt: TextView
    lateinit var shakha_adapter_map: LinearLayout

    private var shakhadetails: Datum_Get_Shakha_Details =
        Datum_Get_Shakha_Details()

    lateinit var back_arrow: ImageView
    lateinit var header_title: TextView

    private var StartTime: String = ""
    private var EndTime: String = ""

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n", "CutPasteId", "MissingPermission", "NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_shakha_details)

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

        header_title.text = getString(R.string.shakha_details)

        back_arrow.setOnClickListener(DebouncedClickListener {
            finish()
        })

        Log.d("Shakha_ID", intent.getStringExtra("Shakha_ID")!!)
        Log.d("Lati", intent.getStringExtra("Lati")!!)
        Log.d("Longi", intent.getStringExtra("Longi")!!)

        rootLayout = findViewById(R.id.rootLayout)
        shakha_name_txt = findViewById(R.id.shakha_name_txt)
        shakha_address_txt = findViewById(R.id.shakha_address_txt)
        parent_shakha_name_txt = findViewById(R.id.parent_shakha_name_txt)
        parent_shakha_address_txt = findViewById(R.id.parent_shakha_address_txt)
        organisation_name_txt = findViewById(R.id.organisation_name_txt)
        contact_person_txt = findViewById(R.id.contact_person_txt)
        organisation_address_txt = findViewById(R.id.organisation_address_txt)
        email_name_txt = findViewById(R.id.email_name_txt)
        email_address_txt = findViewById(R.id.email_address_txt)
        phone_name_txt = findViewById(R.id.phone_name_txt)
        phone_address_txt = findViewById(R.id.phone_address_txt)
        day_name_txt = findViewById(R.id.day_name_txt)
        day_address_txt = findViewById(R.id.day_address_txt)
        time_name_txt = findViewById(R.id.time_name_txt)
        time_address_txt = findViewById(R.id.time_address_txt)
        shakha_adapter_map = findViewById(R.id.shakha_adapter_map)

        if (intent.getStringExtra("MAP").equals("MAP")) {
            if (sessionManager.fetchSHAKHAID() != "") {
                if (Functions.isConnectingToInternet(this)) {
                    val shakha_id = intent.getStringExtra("Shakha_ID")
                    myShakhaDetails(shakha_id!!)
                } else {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.no_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            val localTime: LocalTime = LocalTime.now()
            val dateTimeFormatter: DateTimeFormatter =
                DateTimeFormatter.ofPattern("hh:mm a")
            System.out.println(intent.getStringExtra("Shakha_Start"))
            System.out.println(intent.getStringExtra("Shakha_End"))

            StartTime = intent.getStringExtra("Shakha_Start")!!
            EndTime = intent.getStringExtra("Shakha_End")!!

            if (StartTime != "" && EndTime != "") {
                val start_time = StringTokenizer(StartTime)
                val end_time = StringTokenizer(EndTime)
                val starttime = start_time.nextToken()
                val endtime = end_time.nextToken()

                val sdf = SimpleDateFormat("hh:mm:ss")
                val sdfs = SimpleDateFormat("hh:mm a")
                val dt_starttime: Date
                val dt_endtime: Date

                var Start_time: String = ""
                var End_time: String = ""
                try {
                    dt_starttime = sdf.parse(starttime)
                    dt_endtime = sdf.parse(endtime)
                    System.out.println("Time Display: " + sdfs.format(dt_starttime)) // <-- I got result here
                    System.out.println("Time Display: " + sdfs.format(dt_endtime)) // <-- I got result here

                    Start_time = sdfs.format(dt_starttime)
                    End_time = sdfs.format(dt_endtime)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }

                time_address_txt.text = "$Start_time-$End_time"
            }

            shakha_address_txt.text = intent.getStringExtra("Shakha_Name")
            parent_shakha_address_txt.text = intent.getStringExtra("Shakha_Address")
            contact_person_txt.text = intent.getStringExtra("Shakha_Contact_Person")
            organisation_address_txt.text = "HSS (UK)"
            email_address_txt.text = intent.getStringExtra("Shakha_Email")
            phone_address_txt.text = intent.getStringExtra("Shakha_Phone")
            day_address_txt.text = intent.getStringExtra("Shakha_Day")
            time_name_txt.text = intent.getStringExtra("Shakha_Day") // "Timing"

            shakha_adapter_map.setOnClickListener(DebouncedClickListener {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr="+ intent.getStringExtra("Lati")!!
                        .toDouble()+","+intent.getStringExtra("Longi")!!.toDouble())
                )
                startActivity(intent)
            })
        }
    }

    private fun myShakhaDetails(shakha_id: String) {
        val pd = CustomProgressBar(this)
        pd.show()
        val call: Call<Get_Shakha_Details_Response> =
            MyHssApplication.instance!!.api.get_shakha_details(shakha_id)
        call.enqueue(object : Callback<Get_Shakha_Details_Response> {
            @SuppressLint("NewApi")
            override fun onResponse(
                call: Call<Get_Shakha_Details_Response>,
                response: Response<Get_Shakha_Details_Response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {

                        try {
                            shakhadetails = response.body()!!.data!!
                            Log.d("shakhadetails", shakhadetails.toString())
                            Log.d("shakhadetails", shakhadetails.chapter_name.toString())

                            val localTime: LocalTime = LocalTime.now()
                            val dateTimeFormatter: DateTimeFormatter =
                                DateTimeFormatter.ofPattern("hh:mm a")
                            System.out.println(localTime.format(dateTimeFormatter))

                            var StartTime: String = ""
                            var EndTime: String = ""
                            if (shakhadetails.start_time != "") {
                                StartTime = shakhadetails.start_time!!
                            }
                            if (shakhadetails.end_time != "") {
                                EndTime = shakhadetails.end_time!!
                            }

                            if (StartTime != "" && EndTime != "") {
                                val start_time = StringTokenizer(StartTime)
                                val end_time = StringTokenizer(EndTime)
                                val starttime = start_time.nextToken()
                                val endtime = end_time.nextToken()

                                val sdf = SimpleDateFormat("hh:mm:ss")
                                val sdfs = SimpleDateFormat("hh:mm a")
                                val dt_starttime: Date
                                val dt_endtime: Date

                                var Start_time: String = ""
                                var End_time: String = ""
                                try {
                                    dt_starttime = sdf.parse(starttime)
                                    dt_endtime = sdf.parse(endtime)
                                    System.out.println("Time Display: " + sdfs.format(dt_starttime)) // <-- I got result here
                                    System.out.println("Time Display: " + sdfs.format(dt_endtime)) // <-- I got result here

                                    Start_time = sdfs.format(dt_starttime)
                                    End_time = sdfs.format(dt_endtime)
                                } catch (e: ParseException) {
                                    e.printStackTrace()
                                }

//                            shakha_name_txt.text = shakhadetails.chapter_name
                                shakha_address_txt.text = shakhadetails.chapter_name
//                            parent_shakha_name_txt.text = shakhadetails.chapter_name
                                parent_shakha_address_txt.text =
                                    shakhadetails.building_name + " " + shakhadetails.address_line_1 +
                                            " " + shakhadetails.address_line_2 + " " + shakhadetails.postal_code + " " + shakhadetails.country
//                            organisation_name_txt.text = shakhadetails.chapter_name
                                organisation_address_txt.text = "HSS (UK)"
//                            email_name_txt.text = shakhadetails.chapter_name
                                email_address_txt.text = shakhadetails.email
//                            phone_name_txt.text = shakhadetails.chapter_name
                                phone_address_txt.text = shakhadetails.phone
//                            day_name_txt.text = shakhadetails.chapter_name
                                day_address_txt.text = shakhadetails.day
                                time_name_txt.text = shakhadetails.day // "Timing"
                                time_address_txt.text = Start_time + "-" + End_time

                                shakha_adapter_map.setOnClickListener(DebouncedClickListener {
                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("http://maps.google.com/maps?daddr="+ shakhadetails.latitude!!
                                            .toDouble()+","+shakhadetails.longitude!!.toDouble())
                                    )
                                    startActivity(intent)
                                })

                            }

                        } catch (e: ArithmeticException) {
                            println(e)
                        } finally {
                            println("Family")
                        }
                    } else {
                        Functions.showAlertMessageWithOK(
                            this@ShakhaDetailsActivity, "",
//                        "Message",
                            response.body()?.message
                        )
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@ShakhaDetailsActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Get_Shakha_Details_Response>, t: Throwable) {
                Toast.makeText(this@ShakhaDetailsActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }
}