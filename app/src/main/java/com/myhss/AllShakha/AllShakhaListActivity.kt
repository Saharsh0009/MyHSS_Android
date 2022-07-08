package com.myhss.AllShakha

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.AllShakha.Adapter.ShakhaAdapter
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.Functions
import com.myhss.ui.ShakhaMap.MapsActivity
import com.uk.myhss.AddMember.Get_Shakha.Datum_Get_Shakha
import com.uk.myhss.AddMember.Get_Shakha.Get_Shakha_Response
import com.uk.myhss.Main.HomeActivity
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin
import com.google.android.gms.common.api.GoogleApiClient
import android.widget.Toast
import com.myhss.ui.ShakhaMap.GPSTracker
import com.google.android.gms.location.LocationSettingsStatusCodes

import android.content.IntentSender.SendIntentException
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status

import com.google.android.gms.location.LocationSettingsResult

import com.google.android.gms.location.LocationServices

import com.google.android.gms.location.LocationSettingsRequest

import com.google.android.gms.location.LocationRequest


class AllShakhaListActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    var Shakha_Name: List<String> = java.util.ArrayList<String>()
    var Shakha_ID: List<String> = java.util.ArrayList<String>()
    var DistanceInmiles: List<String> = java.util.ArrayList<String>()

    private var ShakhaName: String = ""
    private var strDouble: String = ""
    private var newstrDouble: Float = 0.0f

    lateinit var data_not_found_layout: RelativeLayout
    lateinit var shakha_list: RecyclerView
    lateinit var allshakha_listview: LinearLayout

    lateinit var mLayoutManager: LinearLayoutManager

    private var shakhalist: List<Datum_Get_Shakha> =
        ArrayList<Datum_Get_Shakha>()

    private var shakhalistsort: List<Datum_Get_Shakha> =
        ArrayList<Datum_Get_Shakha>()

    private var shakha_adapter: ShakhaAdapter? = null

    lateinit var back_arrow: ImageView
    lateinit var map_icon: ImageView
    lateinit var header_title: TextView

    lateinit var search_shakha: AppCompatEditText

    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    private var ascending = true

    var latitude: Double? = null
    var longitude: Double? = null
    var Currlatitude: Double? = null
    var Currlongitude: Double? = null

    //    val tracker: GPSTracker = TODO()
    protected val TAG = "LocationOnOff"
    private var googleApiClient: GoogleApiClient? = null

    @SuppressLint("SetTextI18n", "CutPasteId", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_shakha_list)

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
        map_icon = findViewById(R.id.map_icon)
        header_title = findViewById(R.id.header_title)

        data_not_found_layout = findViewById(R.id.data_not_found_layout)

        map_icon.visibility = View.VISIBLE

        header_title.text = "Find a Shakha"//getString(R.string.all_shakha)

        back_arrow.setOnClickListener {
            val i = Intent(this@AllShakhaListActivity, HomeActivity::class.java)
            startActivity(i)
            finishAffinity()
        }

        map_icon.setOnClickListener {
            val i = Intent(this@AllShakhaListActivity, MapsActivity::class.java)
            i.putExtra("Sorted_List", shakhalistsort.toString())
            startActivity(i)
            finishAffinity()
            overridePendingTransition(R.anim.rotate_in, R.anim.rotate_out)
        }

//        Handler().postDelayed({
//        startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//        }, 100)

//        turnGPSOn()

        displayLocationSettingsRequest(this)

        val tracker = GPSTracker(this@AllShakhaListActivity)
        if (!tracker.canGetLocation()) {
//                tracker.showSettingsAlert()
        } else {
//            Currlatitude = 53.4892791
//            Currlongitude = -2.0997323
            Currlatitude = tracker.getLatitude()
            Currlongitude = tracker.getLongitude()
            Log.d("Current", (Currlatitude!! + Currlongitude!!).toString())

            Handler().postDelayed({
                CallAPI()
            }, 500)
        }

        shakha_list = findViewById(R.id.shakha_list)
        allshakha_listview = findViewById(R.id.allshakha_listview)
        search_shakha = findViewById(R.id.search_shakha)

        shakha_list.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(this@AllShakhaListActivity)
        shakha_list.layoutManager = mLayoutManager

    }

    private fun CallAPI() {
        if (sessionManager.fetchSHAKHAID() != "") {
            if (Functions.isConnectingToInternet(this)) {
                myShakhaList()
            } else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.no_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun myShakhaList() {
        val pd = CustomProgressBar(this)
        pd.show()
        val call: Call<Get_Shakha_Response> =
            MyHssApplication.instance!!.api.get_shakha_info()
        call.enqueue(object : Callback<Get_Shakha_Response> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<Get_Shakha_Response>,
                response: Response<Get_Shakha_Response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {
                        data_not_found_layout.visibility = View.GONE
                        val list = ArrayList<String>()

                        try {
                            shakhalist = response.body()!!.data!!
                            Log.d("shakhalist", shakhalist.toString())

                            Log.d("count=>", shakhalist.size.toString())

                            for (i in 0 until shakhalist.size) {
                                latitude = shakhalist[i].getLatitude()!!.toDouble()
                                longitude = shakhalist[i].getLongitude()!!.toDouble()

                                Shakha_Name = listOf(shakhalist[i].getChapterName().toString())
                                Shakha_ID = listOf(shakhalist[i].getOrgChapterId().toString())

                                if (distance(
                                        Currlatitude!!,  // 53.4892791,
                                        Currlongitude!!,  // -2.0997323,
//                                        shakhalist[0].latitude!!.toDouble(),
//                                        shakhalist[0].longitude!!.toDouble(),
                                        latitude!!,
                                        longitude!!
                                    )
                                ) {

                                    val loc1 = Location("")
                                    loc1.latitude = Currlatitude!!
                                    loc1.longitude = Currlongitude!!

                                    val loc2 = Location("")
                                    loc2.latitude = latitude!!.toDouble()
                                    loc2.longitude = longitude!!.toDouble()

                                    val distanceInMeters: Float =
                                        ((loc1.distanceTo(loc2) / 1609.344).toFloat())

//                                    val strDouble: String =
                                    strDouble = String.format("%.2f", distanceInMeters)

                                    newstrDouble = strDouble.toFloat()
                                    list.add(strDouble)

//                                    shakhalist[i].building_name { it.year }
//                                    shakhalist.forEach { println(it) }
                                }
                                list.sortedBy { shakhalist[i].getdistanceInMiles() }
//                                val cmp = compareBy<String> {it.compareTo(strDouble)}
//                                list.sortedWith(cmp).forEach(::println)
                                Log.d("list-->", list.toString())
                                shakhalist[i].setdistanceInMiles(newstrDouble)
                                Log.d("distanceInMiles",
                                    shakhalist[i].getdistanceInMiles().toString()
                                )
                            }

//                            shakhalist.toSortedSet(Comparator { object1, object2 ->
//                                object1.getdistanceInMiles()!!.compareTo(object2.getdistanceInMiles()!!)
//                            })

                            DistanceInmiles = listOf(listOf(shakhalist).toString())

                            Log.d("distanceInMiles", shakhalist.toString())
                            Log.d("DistanceInmiles", DistanceInmiles.toString())
//                            Log.d("distanceInMiles", shakhalist[0].getdistanceInMiles()!!)
//                            Log.d("distanceInMiles", shakhalist[1].getdistanceInMiles()!!)

                            val sortedList = shakhalist.sortedWith(compareBy {
                                it.getdistanceInMiles()
                            })
                            println("After sorting : $sortedList")

                            shakhalistsort = sortedList

//                            shakhalist.sortedWith(compareBy<Datum_Get_Shakha> { it.getdistanceInMiles()}.thenByDescending { it.getChapterName()}.thenBy { it.getPostalCode()})
//                            shakhalist.sortedBy { sortedList.indexOf(Datum_Get_Shakha()) }

                            search_shakha.addTextChangedListener(object : TextWatcher {
                                override fun afterTextChanged(s: Editable?) {
                                    shakha_adapter!!.filter.filter(s)
                                    if (s!!.isEmpty()) {
//                                        search_shakha.setText("")
//                                        shakha_adapter!!.filter.filter(s)
//                                    } else {
                                        shakha_adapter = ShakhaAdapter()
                                        shakha_adapter!!.setData(sortedList)

//                                        shakha_adapter = ShakhaAdapter(
//                                            shakhalist,
//                                            Currlatitude!!,
//                                            Currlongitude!!
//                                        )

                                        shakha_list.adapter = shakha_adapter
                                        shakha_adapter!!.notifyDataSetChanged()
                                    }
                                }

                                override fun beforeTextChanged(
                                    s: CharSequence?,
                                    start: Int,
                                    count: Int,
                                    after: Int
                                ) {
                                }

                                override fun onTextChanged(
                                    s: CharSequence?,
                                    start: Int,
                                    before: Int,
                                    count: Int
                                ) {

                                }

                            })

//                            Collections.sort(shakhalist,
//                                Comparator<Any> { lhs, rhs -> lhs.toString().compareTo(rhs.toString()) })
//
//                            shakhalist.sortedWith(Comparator { lhs, rhs ->
//                                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
////                                if (lhs.getdistanceInMiles().toString() < rhs.getdistanceInMiles().toString()) -1 else 0
//                                if (lhs.getdistanceInMiles().toString() > rhs.getdistanceInMiles().toString()) -1
//                                else if (lhs.getdistanceInMiles().toString() < rhs.getdistanceInMiles().toString()) 1 else 0
//                            })

//                            Handler().postDelayed({
                                shakha_adapter = ShakhaAdapter()
                                shakha_adapter!!.setData(sortedList)

//                                shakha_adapter = ShakhaAdapter(
//                                    sortedList,
//                                    Currlatitude!!,
//                                    Currlongitude!!
//                                )

                                shakha_list.adapter = shakha_adapter
                                shakha_adapter!!.notifyDataSetChanged()
//                            }, 10)


                        } catch (e: ArithmeticException) {
                            println(e)
                        } finally {
                            println("Family")
                        }
                    } else {
                        data_not_found_layout.visibility = View.VISIBLE
//                        Functions.displayMessage(this@AllShakhaListActivity,response.body()?.message)
//                        Functions.showAlertMessageWithOK(
//                            this@AllShakhaListActivity, "",
////                        "Message",
//                            response.body()?.message
//                        )
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@AllShakhaListActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Get_Shakha_Response>, t: Throwable) {
                Toast.makeText(this@AllShakhaListActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Boolean {
        val theta = lon1 - lon2
        var dist = (Math.sin(deg2rad(lat1))
                * sin(deg2rad(lat2))
                + (cos(deg2rad(lat1))
                * cos(deg2rad(lat2))
                * cos(deg2rad(theta))))
        dist = acos(dist)
        dist = rad2deg(dist)
        dist *= 60 * 1.1515
        Log.d("dist", dist.toString())
//        val mil = 6357 * 1000
//        dist = mil / 1609.344
//        Log.d("dist Miles", dist.toString())
        return dist <= 40
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    override fun onResume() {
        super.onResume()
//        val tracker = GPSTracker(this)
//        if (tracker.isGPSEnabled === true) {
//            finish()
//            startActivity(intent)
//            tracker.isGPSEnabled = false
//        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val i = Intent(this@AllShakhaListActivity, HomeActivity::class.java)
        startActivity(i)
        finishAffinity()
    }

    private fun turnGPSOn() {
        val provider = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.LOCATION_PROVIDERS_ALLOWED
        )
        if (!provider.contains("gps")) { // if gps is disabled
            val poke = Intent()
            poke.setClassName(
                "com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider"
            )
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE)
            poke.data = Uri.parse("3")
            sendBroadcast(poke)
        }
    }

    private fun displayLocationSettingsRequest(context: Context) {
        val googleApiClient = GoogleApiClient.Builder(context)
            .addApi(LocationServices.API).build()
        googleApiClient.connect()
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000
        locationRequest.fastestInterval = (10000 / 2).toLong()
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        val result: PendingResult<LocationSettingsResult> =
            LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
        result.setResultCallback { result ->
            val status: Status = result.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS ->
                    Log.i(
                        TAG,
                        "All location settings are satisfied."
                    )
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    Log.i(
                        TAG,
                        "Location settings are not satisfied. Show the user a dialog to upgrade location settings "
                    )
                    try {
                        // Show the dialog by calling startResolutionForResult(), and check the result
                        // in onActivityResult().
                        status.startResolutionForResult(
                            this@AllShakhaListActivity,
                            1
                        )
                    } catch (e: SendIntentException) {
                        Log.i(TAG, "PendingIntent unable to execute request.")
                    }
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Log.i(
                    TAG,
                    "Location settings are inadequate, and cannot be fixed here. Dialog not created."
                )
            }
        }
    }
}