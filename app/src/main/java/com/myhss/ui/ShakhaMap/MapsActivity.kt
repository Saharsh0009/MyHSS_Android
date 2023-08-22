package com.myhss.ui.ShakhaMap


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.AllShakha.Adapter.ShakhaAdapter
import com.myhss.AllShakha.AllShakhaListActivity
import com.myhss.AllShakha.ShakhaDetailsActivity
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.Functions
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import com.uk.myhss.AddMember.Get_Shakha.Datum_Get_Shakha
import com.uk.myhss.AddMember.Get_Shakha.Get_Shakha_Response
import com.uk.myhss.Main.HomeActivity
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin


class MapsActivity : FragmentActivity(), OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    protected val TAG = "LocationOnOff"
    var latlong_value: LatLng? = null
    var latitude: Double? = null
    var longitude: Double? = null

    var Currlatitude: Double? = null
    var Currlongitude: Double? = null

    lateinit var back_arrow: ImageView
    lateinit var map_reload: ImageView
    lateinit var map_icon: ImageView
    lateinit var menu_icon: ImageView
    lateinit var header_title: TextView

    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    private lateinit var sessionManager: SessionManager

    private var shakhalist: List<Datum_Get_Shakha> =
        ArrayList<Datum_Get_Shakha>()

    private var shakhalistsort: List<Datum_Get_Shakha> =
        ArrayList<Datum_Get_Shakha>()

    lateinit var spinnerArrayAdapter: ArrayAdapter<String>

    // creating array list for adding all our locations.
    private var locationArrayList: ArrayList<LatLng>? = null
    private var NameArrayList: ArrayList<String>? = null

    var relationshipName: List<String> = java.util.ArrayList<String>()
    var relationshipID: List<String> = java.util.ArrayList<String>()
    var edittextFilter: List<String> = java.util.ArrayList<String>()
    var editlatlongFilter: List<String> = java.util.ArrayList<String>()
    var Chapter_Name: List<String> = java.util.ArrayList<String>()
    var Chapter_ID: List<String> = java.util.ArrayList<String>()

    private lateinit var edit_Filter: SearchableSpinner

    private var markerOptions: Marker? = null

    lateinit var search_fields: AppCompatEditText
    lateinit var search: ImageView
    lateinit var filter: ImageView
    var zoom_spiner: Spinner? = null
    private var ZOOM_ID: String = "11"
    var flag: Boolean = false
    private var zooming =
        arrayOf("10 Miles", "20 Miles", "30 Miles", "40 Miles", "50 Miles")
    private var zoomingId = arrayOf("11", "12", "13", "14", "15")
    private lateinit var zoom_list: ListView

    private var Address: String = ""
//    private var arryListPosition: Int = "null"

    /* SHAKHA LIST VIEW CODE*/

    private var shakha_adapter: ShakhaAdapter? = null
    var Shakha_Name: List<String> = java.util.ArrayList<String>()
    var Shakha_ID: List<String> = java.util.ArrayList<String>()

    private var ShakhaName: String = ""
    private var strDouble: String = ""
    private var newstrDouble: Float = 0.0f

    lateinit var shakha_list: RecyclerView
    lateinit var allshakha_listview: LinearLayout
    lateinit var allmap_listview: LinearLayout

    lateinit var mLayoutManager: LinearLayoutManager

    lateinit var search_shakha: AppCompatEditText

    private var ascending = true

    /**************************************/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("MyMapVC")
        sessionManager.firebaseAnalytics.setUserProperty("MyMapVC", "MapsActivity")

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        search_fields = findViewById(R.id.search_fields)
        search = findViewById(R.id.search)
        filter = findViewById(R.id.filter)
        zoom_spiner = findViewById(R.id.zoom_spiner)
        edit_Filter = findViewById(R.id.edit_Filter)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment: SupportMapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        displayLocationSettingsRequest(this)

        /* SHAKHA LIST */
        val tracker = GPSTracker(this@MapsActivity)
        if (!tracker.canGetLocation()) {
//                tracker.showSettingsAlert()
        } else {
            Currlatitude = 53.4892791
            Currlongitude = -2.0997323
//            Currlatitude = tracker.getLatitude()
//            Currlongitude = tracker.getLongitude()
            Log.d("Current", (Currlatitude!! + Currlongitude!!).toString())

            if (!Currlatitude!!.equals("") && !Currlongitude!!.equals("")) {
                if (intent.getStringExtra("Sorted_List") == "Sorted_List") {
                    shakhalistsort =
                        intent.getSerializableExtra("Sorted_List") as ArrayList<Datum_Get_Shakha>
                    for (i in 0 until shakhalistsort.size) {
                        latitude = shakhalistsort[i].getLatitude()!!.toDouble()
                        longitude = shakhalistsort[i].getLongitude()!!.toDouble()

                        Chapter_Name = listOf(shakhalistsort[i].getChapterName().toString())
                        Chapter_ID = listOf(shakhalistsort[i].getOrgChapterId().toString())

                        if (latitude!! != 0.0 && longitude!! != 0.0 && latitude!! != -0.0 && longitude!! != -0.0) {

                            mMap!!.cameraPosition

                            mMap!!.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        Currlatitude!!,  // 53.4892791,
                                        Currlongitude!!,  // -2.0997323,
//                                                shakhalist[i].latitude!!.toDouble(),
//                                                shakhalist[i].longitude!!.toDouble()
//                                                latitude!!,
//                                                longitude!!
                                    ), ZOOM_ID.toDouble().toFloat()
                                )
                            )

                            if (distance(
                                    Currlatitude!!,  // 53.4892791,
                                    Currlongitude!!,  // -2.0997323,
//                                            shakhalist[0].latitude!!.toDouble(),
//                                            shakhalist[0].longitude!!.toDouble(),
                                    latitude!!,
                                    longitude!!
                                )
                            ) {
//                                        mMap!!.animateCamera(CameraUpdateFactory.zoomTo(11.0f))

                                mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
                                mMap!!.uiSettings.isZoomControlsEnabled = true
                                mMap!!.uiSettings.isZoomGesturesEnabled = true
                                mMap!!.uiSettings.isCompassEnabled = true
                                mMap!!.uiSettings.setAllGesturesEnabled(true)
                                mMap!!.uiSettings.isMyLocationButtonEnabled = true

                                mMap!!.setOnMarkerClickListener(OnMarkerClickListener { arg0 ->
                                    if (arg0 != null && arg0.title == shakhalistsort[i].getChapterName()
                                            .toString()
                                    );

                                    val pos: LatLng = arg0.position
                                    val arryListPosition: Int = getArrayListPositionnew(pos)

                                    CALLDIALOG(arryListPosition)

//                                            MyCustomView(this@MapsActivity, shakhalist[arryListPosition].chapter_name)

                                    true
                                })

                                val markerOptions: MarkerOptions = MarkerOptions().position(
                                    LatLng(
                                        latitude!!,
                                        longitude!!
                                    )
                                ).title(shakhalistsort[i].getChapterName()!!)
                                    .snippet("Lat :-" + shakhalistsort[i].getLatitude() + " Long :-" + shakhalistsort[i].getLongitude())
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.locationflag))
                                mMap!!.addMarker(markerOptions)
                            }
                        }
                    }
                } else {
                    Handler().postDelayed({
                        CallAPI()
                    }, 10)
                }
            } else {
                val ha = Handler()
                ha.postDelayed(object : Runnable {
                    override fun run() {
                        //call function
                        displayLocationSettingsRequest(this@MapsActivity)
                        ha.postDelayed(this, 10000)
                    }
                }, 10000)
            }
        }

        shakha_list = findViewById(R.id.shakha_list)
        allshakha_listview = findViewById(R.id.allshakha_listview)
        allmap_listview = findViewById(R.id.allmap_listview)
        search_shakha = findViewById(R.id.search_shakha)

        shakha_list.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(this@MapsActivity)
        shakha_list.layoutManager = mLayoutManager

        /**********************/

        // in below line we are initializing our array list.
        locationArrayList = ArrayList()

        back_arrow = findViewById(R.id.back_arrow)
        map_icon = findViewById(R.id.map_icon)
        map_reload = findViewById(R.id.map_reload)
        menu_icon = findViewById(R.id.menu_icon)
        header_title = findViewById(R.id.header_title)

        map_icon.setColorFilter(
            ContextCompat.getColor(this, R.color.white),
            android.graphics.PorterDuff.Mode.MULTIPLY
        )
        menu_icon.setColorFilter(
            ContextCompat.getColor(this, R.color.white),
            android.graphics.PorterDuff.Mode.MULTIPLY
        )

        menu_icon.visibility = View.VISIBLE
        header_title.text = "Find a Shakha"

//        if (intent.getStringExtra("SHAKHA_LIST") == "SHAKHA_LIST") {
//            map_icon.visibility = View.VISIBLE
//            map_icon.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)
//            header_title.text = "Find a Shakha"
//
//            allshakha_listview.visibility = View.VISIBLE
//            allmap_listview.visibility = View.GONE
//        } else {
//            menu_icon.visibility = View.VISIBLE
//            menu_icon.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)
//            header_title.text = getString(R.string.all_shakha)
//
//            allshakha_listview.visibility = View.GONE
//            allmap_listview.visibility = View.VISIBLE
//        }

        back_arrow.setOnClickListener(DebouncedClickListener {
            val i = Intent(this@MapsActivity, HomeActivity::class.java)
            startActivity(i)
            finishAffinity()
        })

        menu_icon.setOnClickListener(DebouncedClickListener {
            val popupMenu = PopupMenu(this@MapsActivity, filter)
            popupMenu.menu.add("Shakha List")
            popupMenu.menu.add("Reload Map")
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.title.toString()) {
                    "Shakha List" -> {
//                        menu_icon.visibility = View.GONE
//                        map_icon.visibility = View.VISIBLE
//                        map_icon.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)
//                        header_title.text = "Find a Shakha"
//
//                        allshakha_listview.visibility = View.VISIBLE
//                        allmap_listview.visibility = View.GONE
                        val i = Intent(this@MapsActivity, AllShakhaListActivity::class.java)
                        startActivity(i)
                        finishAffinity()
                        overridePendingTransition(R.anim.rotate_out, R.anim.rotate_in)
                    }

                    "Reload Map" -> {
                        search_fields.setText("")
                        CallAPI()
                    }
                }
                false
            }
            popupMenu.show()
        })

        map_icon.setOnClickListener(DebouncedClickListener {
//            menu_icon.visibility = View.VISIBLE
//            map_icon.visibility = View.GONE
//            header_title.text = getString(R.string.all_shakha)
//
//            allshakha_listview.visibility = View.GONE
//            allmap_listview.visibility = View.VISIBLE
            val i = Intent(this@MapsActivity, AllShakhaListActivity::class.java)
            startActivity(i)
            finishAffinity()
            overridePendingTransition(R.anim.rotate_out, R.anim.rotate_in)
        })

        map_reload.setOnClickListener(DebouncedClickListener {
            search_fields.setText("")
            CallAPI()
        })

        zoom_list = findViewById(R.id.zoom_list)

        filter.setOnClickListener(DebouncedClickListener {
//            Log.d("click", "Yes")
//            if (flag) {
//                zoom_spiner!!.visibility = View.GONE
//                flag = false
//            } else {
//                zoom_spiner!!.visibility = View.VISIBLE
//                flag = true
//            }

            val popupMenu = PopupMenu(this@MapsActivity, filter)
            popupMenu.menu.add("10 Miles")
            popupMenu.menu.add("20 Miles")
            popupMenu.menu.add("30 Miles")
            popupMenu.menu.add("40 Miles")
            popupMenu.menu.add("50 Miles")
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.title.toString()) {
                    "10 Miles" -> {
                        ZOOM_ID = "11"
                        Log.d("ZOOM_ID", ZOOM_ID)
                        mMap!!.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    latitude!!,
                                    longitude!!
                                ), ZOOM_ID.toDouble().toFloat()
                            )
                        )
                    }

                    "20 Miles" -> {
                        ZOOM_ID = "12"
                        Log.d("ZOOM_ID", ZOOM_ID)
                        mMap!!.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    latitude!!,
                                    longitude!!
                                ), ZOOM_ID.toDouble().toFloat()
                            )
                        )
                    }

                    "30 Miles" -> {
                        ZOOM_ID = "13"
                        Log.d("ZOOM_ID", ZOOM_ID)
                        mMap!!.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    latitude!!,
                                    longitude!!
                                ), ZOOM_ID.toDouble().toFloat()
                            )
                        )
                    }

                    "40 Miles" -> {
                        ZOOM_ID = "14"
                        Log.d("ZOOM_ID", ZOOM_ID)
                        mMap!!.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    latitude!!,
                                    longitude!!
                                ), ZOOM_ID.toDouble().toFloat()
                            )
                        )
                    }

                    "50 Miles" -> {
                        ZOOM_ID = "15"
                        Log.d("ZOOM_ID", ZOOM_ID)
                        mMap!!.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    latitude!!,
                                    longitude!!
                                ), ZOOM_ID.toDouble().toFloat()
                            )
                        )
                    }
                }
                false
            }
            popupMenu.show()
        })

        edit_Filter.setTitle("Search Shakha")

        search_fields.setOnClickListener(DebouncedClickListener {
            edit_Filter.visibility = View.VISIBLE
            edit_Filter.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        adapter: AdapterView<*>,
                        v: View,
                        position: Int,
                        id: Long
                    ) {
                        // On selecting a spinner item
                        (adapter.getChildAt(0) as TextView).setTextColor(Color.BLACK)

                        Log.d("Address", edittextFilter[position])
                        Log.d("LatLong", editlatlongFilter[position])
//                    OCCUPATION_ID = OccupationID[position]

                        search_fields.setText(edittextFilter[position])
                        Log.d("Lat", editlatlongFilter[position].split(" ")[0])
                        Log.d("Long", editlatlongFilter[position].split(" ")[1])

                        mMap!!.clear()
                        mMap!!.cameraPosition

                        mMap!!.setOnMarkerClickListener(GoogleMap.OnMarkerClickListener { marker ->

                            if (shakhalist[position].getBuildingName()!!.isNotEmpty()) {
                                Address =
                                    shakhalist[position].getBuildingName()!! + ", " + shakhalist[position].getAddressLine1()!! + ", " +
                                            shakhalist[position].getAddressLine2()!! + ", " + shakhalist[position].getPostalCode()!! + ", " +
                                            shakhalist[position].getCity()!! + ", " + shakhalist[position].getCountry()!!
                            } else {
                                Address = shakhalist[position].getAddressLine1()!! + ", " +
                                        shakhalist[position].getAddressLine2()!! + ", " + shakhalist[position].getPostalCode()!! + ", " +
                                        shakhalist[position].getCity()!! + ", " + shakhalist[position].getCountry()!!
                            }

                            if (marker == markerOptions) {
                                //handle click here
                                val dialog = Dialog(this@MapsActivity)
//                                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//                                            dialog.setCancelable(false)
                                dialog.setContentView(R.layout.mew_custom_map_marker)
                                val body =
                                    dialog.findViewById(R.id.txtLocMarkerName) as TextView
                                val info_img =
                                    dialog.findViewById(R.id.info_img) as ImageView
                                body.text = shakhalist[position].getChapterName()
//                                            val yesBtn = dialog.findViewById(R.id.login_btn) as TextView
//                                            val noBtn = dialog.findViewById(R.id.noBtn) as TextView

                                body.setOnClickListener(DebouncedClickListener {
                                    dialog.dismiss()

                                    val intent = Intent(
                                        this@MapsActivity,
                                        ShakhaDetailsActivity::class.java
                                    )
//                            intent.putExtra("Shakha_ID", shakhalist[position].org_chapter_id)
                                    intent.putExtra(
                                        "Shakha_ID",
                                        shakhalist[position].getOrgChapterId()
                                    )
                                    intent.putExtra(
                                        "Shakha_Name",
                                        shakhalist[position].getChapterName()
                                    )
                                    intent.putExtra(
                                        "Shakha_Contact_Person",
                                        shakhalist[position].getContactPersonName()
                                    )
                                    intent.putExtra("Shakha_Address", Address)
                                    intent.putExtra("Shakha_Email", shakhalist[position].getEmail())
                                    intent.putExtra("Shakha_Phone", shakhalist[position].getPhone())
                                    intent.putExtra("Shakha_Day", shakhalist[position].getDay())
                                    intent.putExtra(
                                        "Shakha_Start",
                                        shakhalist[position].getStartTime()
                                    )
                                    intent.putExtra("Shakha_End", shakhalist[position].getEndTime())
                                    intent.putExtra("Lati", shakhalist[position].getLatitude())
                                    intent.putExtra("Longi", shakhalist[position].getLongitude())
                                    intent.putExtra("MAP", "MAP")
//                            intent.putExtra("Shakha_ID", relationshipID[position])
                                    this@MapsActivity.startActivity(intent)
                                })

                                info_img.setOnClickListener(DebouncedClickListener {
                                    dialog.dismiss()
                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(
                                            "http://maps.google.com/maps?daddr=" + shakhalist[position].getLatitude()!!
                                                .toDouble() + "," + shakhalist[position].getLongitude()!!
                                                .toDouble()
                                        )
                                    )
                                    this@MapsActivity.startActivity(intent)
                                })
//                                            noBtn.setOnClickListener { dialog.dismiss() }
                                dialog.show()
                            }
                            true
                        })

//                    mMap!!.animateCamera(CameraUpdateFactory.zoomTo(11.0f))

                        mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
                        mMap!!.uiSettings.isZoomControlsEnabled = true
                        mMap!!.uiSettings.isZoomGesturesEnabled = true
                        mMap!!.uiSettings.isCompassEnabled = true
                        mMap!!.uiSettings.setAllGesturesEnabled(true)
                        mMap!!.uiSettings.isMyLocationButtonEnabled = true

                        latitude = editlatlongFilter[position].split(" ")[0].toDouble()
                        longitude = editlatlongFilter[position].split(" ")[1].toDouble()

                        mMap!!.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    latitude!!,
                                    longitude!!
                                ), ZOOM_ID.toDouble().toFloat()
                            )
                        )

                        distance(
                            Currlatitude!!,  // 53.4892791,
                            Currlongitude!!,  // -2.0997323,
//                            shakhalist[0].latitude!!.toDouble(),
//                            shakhalist[0].longitude!!.toDouble(),
                            latitude!!,
                            longitude!!
                        )

                        markerOptions = mMap!!.addMarker(
                            MarkerOptions()
                                .position(
                                    LatLng(
                                        latitude!!,
                                        longitude!!
                                    )
                                )
                                .title(edittextFilter[position])
                                .snippet(
                                    "Lat :-" + editlatlongFilter[position].split(" ")[0] + " Long :-" + editlatlongFilter[position].split(
                                        " "
                                    )[1]
                                )
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.locationflag))
//                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        )

//                    val markerOptions: MarkerOptions  = MarkerOptions().position(
//                        LatLng(
//                            latitude!!,
//                            longitude!!
//                        )
//                    ).title(edittextFilter[position]).snippet("Lat :-"+editlatlongFilter[position].split(" ")[0]+" Long :-"+ editlatlongFilter[position].split(" ")[1])
//
//                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.location))
//
//                    mMap!!.addMarker(markerOptions)

                        edit_Filter.visibility = View.GONE
                    }

                    override fun onNothingSelected(arg0: AdapterView<*>?) {
                        // TODO Auto-generated method stub
                    }
                }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.clear()
        mMap!!.cameraPosition

//        mMap!!.animateCamera(CameraUpdateFactory.zoomTo(11.0f))

        mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap!!.uiSettings.isZoomControlsEnabled = true
        mMap!!.uiSettings.isZoomGesturesEnabled = true
        mMap!!.uiSettings.isCompassEnabled = true
        mMap!!.uiSettings.setAllGesturesEnabled(true)
        mMap!!.uiSettings.isMyLocationButtonEnabled = true
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mMap!!.isMyLocationEnabled = true

        val tracker = GPSTracker(this)
        if (!tracker.canGetLocation()) {
            tracker.showSettingsAlert()
        } else {
            if (mMap != null) {
//                Currlatitude = 53.4892791
//                Currlongitude = -2.0997323
//                Currlatitude = tracker.getLatitude()
//                Currlongitude = tracker.getLongitude()
                Log.d("Current", (Currlatitude!! + Currlongitude!!).toString())

                Handler().postDelayed({
                    CallAPI()
                }, 100)

                mMap!!.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
//                                        CameraUpdateFactory.newLatLng(
                        LatLng(
                            Currlatitude!!,
                            Currlongitude!!
                        ), ZOOM_ID.toDouble().toFloat()
                    )
                )

                val markerOptions: MarkerOptions = MarkerOptions().position(
                    LatLng(
                        Currlatitude!!,
                        Currlongitude!!
                    )
                ).title(sessionManager.fetchUSERNAME())
                    .snippet("Lat :-" + Currlatitude + " Long :-" + Currlongitude)

                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.location))

                mMap!!.addMarker(markerOptions)
            }
        }
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
        val pd = CustomProgressBar(this@MapsActivity)
        pd.show()
        val call: Call<Get_Shakha_Response> =
            MyHssApplication.instance!!.api.get_shakha_info()
        call.enqueue(object : Callback<Get_Shakha_Response> {
            override fun onResponse(
                call: Call<Get_Shakha_Response>,
                response: Response<Get_Shakha_Response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {

                        val list = ArrayList<String>()

                        try {
                            shakhalist = response.body()!!.data!!
//                            Log.d("shakhalist", shakhalist.toString())

                            Log.d("count=>", shakhalist.size.toString())

                            for (i in 0 until shakhalist.size) {
//                                locationArrayList!!.add(shakhalist[i].chapter_name)
                                latitude = shakhalist[i].getLatitude()!!.toDouble()
                                longitude = shakhalist[i].getLongitude()!!.toDouble()

                                Chapter_Name = listOf(shakhalist[i].getChapterName().toString())
                                Chapter_ID = listOf(shakhalist[i].getOrgChapterId().toString())

                                if (latitude!! != 0.0 && longitude!! != 0.0 && latitude!! != -0.0 && longitude!! != -0.0) {

                                    mMap!!.cameraPosition

                                    mMap!!.moveCamera(
                                        CameraUpdateFactory.newLatLngZoom(
                                            LatLng(
                                                Currlatitude!!,  // 53.4892791,
                                                Currlongitude!!,  // -2.0997323,
//                                                shakhalist[i].latitude!!.toDouble(),
//                                                shakhalist[i].longitude!!.toDouble()
//                                                latitude!!,
//                                                longitude!!
                                            ), ZOOM_ID.toDouble().toFloat()
                                        )
                                    )

                                    if (distance(
                                            Currlatitude!!,  // 53.4892791,
                                            Currlongitude!!,  // -2.0997323,
//                                            shakhalist[0].latitude!!.toDouble(),
//                                            shakhalist[0].longitude!!.toDouble(),
                                            latitude!!,
                                            longitude!!
                                        )
                                    ) {
//                                        mMap!!.animateCamera(CameraUpdateFactory.zoomTo(11.0f))

                                        mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
                                        mMap!!.uiSettings.isZoomControlsEnabled = true
                                        mMap!!.uiSettings.isZoomGesturesEnabled = true
                                        mMap!!.uiSettings.isCompassEnabled = true
                                        mMap!!.uiSettings.setAllGesturesEnabled(true)
                                        mMap!!.uiSettings.isMyLocationButtonEnabled = true

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

                                        mMap!!.setOnMarkerClickListener(OnMarkerClickListener { arg0 ->
                                            if (arg0 != null && arg0.title == shakhalist[i].getChapterName()
                                                    .toString()
                                            );

                                            val pos: LatLng = arg0.position
                                            val arryListPosition: Int = getArrayListPosition(pos)

                                            CALLDIALOG(arryListPosition)

//                                            MyCustomView(this@MapsActivity, shakhalist[arryListPosition].chapter_name)

                                            true
                                        })

                                        val markerOptions: MarkerOptions = MarkerOptions().position(
                                            LatLng(
                                                latitude!!,
                                                longitude!!
                                            )
                                        ).title(shakhalist[i].getChapterName()!!)
                                            .snippet("Lat :-" + shakhalist[i].getLatitude() + " Long :-" + shakhalist[i].getLongitude())
                                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.locationflag))
                                        mMap!!.addMarker(markerOptions)
                                    }

                                    list.sortedBy { shakhalist[i].getdistanceInMiles() }

                                    Log.d("list-->", list.toString())
                                    shakhalist[i].setdistanceInMiles(newstrDouble)
                                    Log.d(
                                        "distanceInMiles",
                                        shakhalist[i].getdistanceInMiles().toString()
                                    )
                                }
                            }

                            /*SHAKHA LIST*/

                            val sortedList = shakhalist.sortedWith(compareBy {
                                it.getdistanceInMiles()
                            })
                            println("After sorting : $sortedList")

                            search_shakha.addTextChangedListener(object : TextWatcher {
                                override fun afterTextChanged(s: Editable?) {
                                    shakha_adapter!!.filter.filter(s)
                                    if (s!!.isEmpty()) {
                                        shakha_adapter = ShakhaAdapter()
                                        shakha_adapter!!.setData(shakhalist)
//                                        shakha_adapter = ShakhaAdapter(
//                                            shakhalist, Currlatitude!!,
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

                            Handler().postDelayed({

                                shakha_adapter = ShakhaAdapter()
                                shakha_adapter!!.setData(sortedList)

//                                shakha_adapter = ShakhaAdapter(
//                                    sortedList, Currlatitude!!,
//                                    Currlongitude!!
//                                )

                                shakha_list.adapter = shakha_adapter
                                shakha_adapter!!.notifyDataSetChanged()
                            }, 10)

                            /*******************************/

                            relationshipName = listOf(arrayOf(shakhalist).toString())
                            relationshipID = listOf(arrayOf(shakhalist).toString())
                            edittextFilter = listOf(arrayOf(shakhalist).toString())
                            editlatlongFilter = listOf(arrayOf(shakhalist).toString())

                            val FilterStringList = java.util.ArrayList<String>()
                            for (i in 0 until shakhalist.size) {
                                FilterStringList.add(
//                                            shakhalist[i].building_name.toString()+","+shakhalist[i].address_line_1+
//                                            ","+shakhalist[i].address_line_2+","+
                                    shakhalist[i].getPostalCode()!!
//                                                    +","+shakhalist[i].city+","+shakhalist[i].country
                                )
                            }

                            val latlongStringList = java.util.ArrayList<String>()
                            for (i in 0 until shakhalist.size) {
                                latlongStringList.add(
                                    shakhalist[i].getLatitude()
                                        .toString() + " " + shakhalist[i].getLongitude()
                                )
                            }

                            val mStringList = java.util.ArrayList<String>()
                            for (i in 0 until shakhalist.size) {
                                mStringList.add(
                                    shakhalist[i].getChapterName().toString()
                                )
                            }

                            val mStringListnew = java.util.ArrayList<String>()
                            for (i in 0 until shakhalist.size) {
                                mStringListnew.add(
                                    shakhalist[i].getOrgChapterId().toString()
                                )
                            }

                            var latlongStringArray = latlongStringList.toArray()
                            var FilterStringArray = FilterStringList.toArray()
                            var mStringArray = mStringList.toArray()
                            var mStringArraynew = mStringListnew.toArray()

                            for (i in FilterStringArray.indices) {
                                Log.d("string is", FilterStringArray[i] as String)
                            }

                            for (i in latlongStringArray.indices) {
                                Log.d("string is", latlongStringArray[i] as String)
                            }

                            for (i in mStringArray.indices) {
                                Log.d("string is", mStringArray[i] as String)
                            }

                            for (i in mStringArraynew.indices) {
                                Log.d("mStringArraynew is", mStringArraynew[i] as String)
                            }

                            latlongStringArray = latlongStringList.toArray(latlongStringArray)
                            FilterStringArray = FilterStringList.toArray(FilterStringArray)
                            mStringArray = mStringList.toArray(mStringArray)
                            mStringArraynew = mStringListnew.toArray(mStringArraynew)

                            val latlonglist: java.util.ArrayList<String> = arrayListOf<String>()
                            val Filterlist: java.util.ArrayList<String> = arrayListOf<String>()
                            val list: java.util.ArrayList<String> = arrayListOf<String>()
                            val listnew: java.util.ArrayList<String> = arrayListOf<String>()

                            for (element in FilterStringArray) {
                                Log.d("LIST==>", element.toString())
                                Filterlist.add(element.toString())
                                Log.d("Filterlist==>", Filterlist.toString())

                                val listn = arrayOf(element)
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                    edittextFilter = Filterlist
                                }
                            }

                            for (element in latlongStringArray) {
                                Log.d("LIST==>", element.toString())
                                latlonglist.add(element.toString())
                                Log.d("latlonglist==>", latlonglist.toString())

                                val listn = arrayOf(element)
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                    editlatlongFilter = latlonglist
                                }
                            }

                            for (element in mStringArray) {
                                Log.d("LIST==>", element.toString())
                                list.add(element.toString())
                                Log.d("list==>", list.toString())

                                val listn = arrayOf(element)
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                    relationshipName =
                                        list
                                }
                            }

                            for (element in mStringArraynew) {
                                Log.d("LIST==>", element.toString())
                                listnew.add(element.toString())
                                Log.d("list==>", listnew.toString())

                                val listn = arrayOf(element)
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                    relationshipID =
                                        listnew
                                }
                            }

                            Log.d(
                                "relationshipName-->",
                                relationshipName.toString()
                            )
                            Log.d(
                                "relationshipID-->",
                                relationshipID.toString()
                            )

                            spinnerArrayAdapter = ArrayAdapter<String>(
                                this@MapsActivity,
                                android.R.layout.simple_spinner_dropdown_item,
                                edittextFilter
                            )
                            //selected item will look like a spinner set from XML
//                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                            zoom_list.adapter = spinnerArrayAdapter
                            edit_Filter.adapter = spinnerArrayAdapter
//                            edittextFilter = listOf(shakhalist[i].building_name.toString()+"\n"+shakhalist[i].address_line_1+
//                                    "\n"+shakhalist[i].address_line_2+"\n"+shakhalist[i].postal_code+","+shakhalist[i].city+","+shakhalist[i].country)


                        } catch (e: ArithmeticException) {
                            println(e)
                        } finally {
                            println("Family")
                        }
                    } else {
                        Functions.displayMessage(this@MapsActivity, response.body()?.message)
//                        Functions.showAlertMessageWithOK(
//                            this@MapsActivity, "",
////                        "Message",
//                            response.body()?.message
//                        )
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@MapsActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Get_Shakha_Response>, t: Throwable) {
                Toast.makeText(this@MapsActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    private fun getArrayListPosition(pos: LatLng): Int {
        for (i in shakhalist.indices) {
            if (pos.latitude == shakhalist[i].getLatitude()!!.toDouble()) {
                if (pos.longitude == shakhalist[i].getLongitude()!!.toDouble()
                ) return i
            }
        }
        return 0
    }

    private fun getArrayListPositionnew(pos: LatLng): Int {
        for (i in shakhalistsort.indices) {
            if (pos.latitude == shakhalistsort[i].getLatitude()!!.toDouble()) {
                if (pos.longitude == shakhalistsort[i].getLongitude()!!.toDouble()
                ) return i
            }
        }
        return 0
    }

    fun CALLDIALOG(arryListPosition: Int) {
        val dialog = Dialog(this@MapsActivity)
//                                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//                                            dialog.setCancelable(false)
        dialog.setContentView(R.layout.mew_custom_map_marker)
        val body =
            dialog.findViewById(R.id.txtLocMarkerName) as TextView
        val info_img =
            dialog.findViewById(R.id.info_img) as ImageView
        body.text = shakhalist[arryListPosition].getChapterName().toString()
//                                            val yesBtn = dialog.findViewById(R.id.login_btn) as TextView
//                                            val noBtn = dialog.findViewById(R.id.noBtn) as TextView

        body.setOnClickListener(DebouncedClickListener {
            dialog.dismiss()
            Log.d(
                "chapter_name",
                shakhalist[arryListPosition].getChapterName().toString()
            )
            Log.d(
                "contact_person_name",
                shakhalist[arryListPosition].getContactPersonName().toString()
            )
            Log.d(
                "org_chapter_id",
                shakhalist[arryListPosition].getOrgChapterId().toString()
            )

            if (shakhalist[arryListPosition].getBuildingName()!!.isNotEmpty()) {
                Address =
                    shakhalist[arryListPosition].getBuildingName()!! + ", " + shakhalist[arryListPosition].getAddressLine1()!! + ", " +
                            shakhalist[arryListPosition].getAddressLine2()!! + ", " + shakhalist[arryListPosition].getPostalCode()!! + ", " +
                            shakhalist[arryListPosition].getCity()!! + ", " + shakhalist[arryListPosition].getCountry()!!
            } else {
                Address =
                    shakhalist[arryListPosition].getAddressLine1()!! + ", " +
                            shakhalist[arryListPosition].getAddressLine2()!! + ", " + shakhalist[arryListPosition].getPostalCode()!! + ", " +
                            shakhalist[arryListPosition].getCity()!! + ", " + shakhalist[arryListPosition].getCountry()!!
            }

            val intent = Intent(
                this@MapsActivity,
                ShakhaDetailsActivity::class.java
            )
            intent.putExtra(
                "Shakha_ID",
                shakhalist[arryListPosition].getOrgChapterId()
            )
            intent.putExtra(
                "Shakha_Name",
                shakhalist[arryListPosition].getChapterName()
            )
            intent.putExtra(
                "Shakha_Contact_Person",
                shakhalist[arryListPosition].getContactPersonName()
            )
            intent.putExtra("Shakha_Address", Address)
            intent.putExtra(
                "Shakha_Email",
                shakhalist[arryListPosition].getEmail()
            )
            intent.putExtra(
                "Shakha_Phone",
                shakhalist[arryListPosition].getPhone()
            )
            intent.putExtra("Shakha_Day", shakhalist[arryListPosition].getDay())
            intent.putExtra(
                "Shakha_Start",
                shakhalist[arryListPosition].getStartTime()
            )
            intent.putExtra(
                "Shakha_End",
                shakhalist[arryListPosition].getEndTime()
            )
            intent.putExtra("Lati", shakhalist[arryListPosition].getLatitude())
            intent.putExtra("Longi", shakhalist[arryListPosition].getLongitude())
            intent.putExtra("MAP", "MAP")
            this@MapsActivity.startActivity(intent)
        })

        info_img.setOnClickListener(DebouncedClickListener {
            dialog.dismiss()
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    "http://maps.google.com/maps?daddr=" + shakhalist[arryListPosition].getLatitude()!!
                        .toDouble() + "," + shakhalist[arryListPosition].getLongitude()!!.toDouble()
                )
            )
            this@MapsActivity.startActivity(intent)
        })
//                                            noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
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
        return dist <= 30
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
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
                            this@MapsActivity,
                            1
                        )
                    } catch (e: IntentSender.SendIntentException) {
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

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
//        super.onBackPressed()
        val i = Intent(this@MapsActivity, HomeActivity::class.java)
        startActivity(i)
        finishAffinity()
    }
}