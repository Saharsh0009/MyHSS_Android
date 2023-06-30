package com.uk.myhss.Splash

import android.Manifest.permission
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.analytics.HitBuilders.EventBuilder
import com.google.android.gms.analytics.HitBuilders.ScreenViewBuilder
import com.google.android.gms.analytics.Tracker
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.common.wrappers.InstantApps
import com.google.android.gms.location.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.MyApplication
import com.myhss.Login_Registration.Passcode_Activity
import com.myhss.Splash.Model.Biometric.Latest_Update.latest_update_response
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.Functions
import com.uk.myhss.Login_Registration.LoginActivity
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.Welcome.WelcomeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.DriverManager.println


class SplashActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private val TAG = this@SplashActivity
    private var currentVersion: String = ""
    lateinit var sharedPreferences: SharedPreferences
    private var mTracker: Tracker? = null

    val STATUS_INSTALLED = "installed"
    val STATUS_INSTANT = "instant"
    val ANALYTICS_USER_PROP = "app_type"

    private val PERMISSION_REQUEST_CODE = 200

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Obtain the shared Tracker instance.
        val application = application as MyApplication
        mTracker = application.defaultTracker
        Log.i(TAG.toString(), "Setting screen name: ${this@SplashActivity}")
        mTracker!!.setScreenName("Image~${this@SplashActivity}")
        mTracker!!.send(ScreenViewBuilder().build())
        mTracker!!.send(
            EventBuilder().setCategory("Action").setAction("Share").build()
        )

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        sessionManager = SessionManager(this)

        if (!checkPermission()) {
            requestPermission()
        } else {
            Handler().postDelayed({
                Log.e("TAG-SharedPref",""+sharedPreferences.getString("USERID", ""))
                if (sharedPreferences.getString("USERID", "") != "") {
                    try {
                        if (sharedPreferences.getString("MEMBERID", "") != "") {
                            val i = Intent(this@SplashActivity, Passcode_Activity::class.java)
                            i.putExtra("CHANGE_BIOMETRIC", "")
                            startActivity(i)
                            finish()
                        } else {
                            startActivity(
                                Intent(
                                    this@SplashActivity, WelcomeActivity::class.java
                                )
                            )
                            finish()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    startActivity(
                        Intent(
                            this@SplashActivity, LoginActivity::class.java
                        )
                    )
                    finish()
                }
            }, 500)
        }

        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("LaunchVC")
        sessionManager.firebaseAnalytics.setUserProperty("LaunchVC", "SplashActivity")

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = Firebase.analytics

        sessionManager.firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "LaunchVC")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "SplashActivity")
        }

        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);

        // set the corresponding user property for Google Analytics.
        if (InstantApps.isInstantApp(this@SplashActivity)) {
            sessionManager.firebaseAnalytics.setUserProperty(ANALYTICS_USER_PROP, STATUS_INSTANT)
        } else {
            sessionManager.firebaseAnalytics.setUserProperty(ANALYTICS_USER_PROP, STATUS_INSTALLED)
        }

        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)

        sharedPreferences = getSharedPreferences("production", Context.MODE_PRIVATE)

        try {
            currentVersion = packageManager.getPackageInfo(packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        Log.d("currentVersion", currentVersion)
        Log.d(
            "Allow_biometric", sharedPreferences.getString(
                "Allow_biometric", ""
            ).toString()
        )
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkPermission(): Boolean {
        val result =
            ContextCompat.checkSelfPermission(applicationContext, permission.WRITE_EXTERNAL_STORAGE)
        val result1 =
            ContextCompat.checkSelfPermission(applicationContext, permission.READ_EXTERNAL_STORAGE)
//        val result2 =
//            ContextCompat.checkSelfPermission(applicationContext, permission.ACCESS_FINE_LOCATION)
        val result3 = ContextCompat.checkSelfPermission(applicationContext, permission.CALL_PHONE)
//        val result4 = ContextCompat.checkSelfPermission(applicationContext, permission.ACCESS_BACKGROUND_LOCATION)
//        val result5 =
//            ContextCompat.checkSelfPermission(applicationContext, permission.ACCESS_COARSE_LOCATION)
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED &&
//                result2 == PackageManager.PERMISSION_GRANTED &&
                result3 == PackageManager.PERMISSION_GRANTED
//                result4 == PackageManager.PERMISSION_GRANTED &&
//                result5 == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                permission.WRITE_EXTERNAL_STORAGE, permission.READ_EXTERNAL_STORAGE,
//                permission.ACCESS_FINE_LOCATION,
//                permission.ACCESS_COARSE_LOCATION,
//                permission.ACCESS_BACKGROUND_LOCATION,
                permission.CALL_PHONE
            ), PERMISSION_REQUEST_CODE
        )
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.size > 0) {
                val readAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED
                val callAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED
//                val gpsAccepted = grantResults[3] == PackageManager.PERMISSION_GRANTED
//                val locatioAccepted = grantResults[4] == PackageManager.PERMISSION_GRANTED
//                val backgroundlocatioAccepted = grantResults[5] == PackageManager.PERMISSION_GRANTED
//                if (readAccepted && cameraAccepted && callAccepted && gpsAccepted && locatioAccepted) {
                if (readAccepted && cameraAccepted && callAccepted) {
//                    displayLocationSettingsRequest(this)
                    Handler().postDelayed({
                        if (Functions.isConnectingToInternet(this@SplashActivity)) {
                            val OSName = "android"
                            val app_version = currentVersion
                            val app_name = "android_hss"//getString(R.string.app_name)
                            myLatestUpdate(OSName, app_version, app_name)
                        } else {
                            Toast.makeText(
                                this@SplashActivity,
                                resources.getString(R.string.no_connection),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }, 500)
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        if (shouldShowRequestPermissionRationale(permission.READ_EXTERNAL_STORAGE)) {
                            showMessageOKCancel("You need to allow access to read the permissions",
                                DialogInterface.OnClickListener { dialog, which ->
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                        requestPermissions(
                                            arrayOf(
                                                permission.WRITE_EXTERNAL_STORAGE,
                                                permission.READ_EXTERNAL_STORAGE,
//                                                permission.ACCESS_FINE_LOCATION,
//                                                permission.ACCESS_COARSE_LOCATION,
//                                                permission.ACCESS_BACKGROUND_LOCATION,
                                                permission.CALL_PHONE
                                            ), PERMISSION_REQUEST_CODE
                                        )
                                    }
                                })
                            return
                        } else if (shouldShowRequestPermissionRationale(permission.CAMERA)) {
                            showMessageOKCancel("You need to allow access to camera the permissions",
                                DialogInterface.OnClickListener { dialog, which ->
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                        requestPermissions(
                                            arrayOf(
                                                permission.WRITE_EXTERNAL_STORAGE,
                                                permission.READ_EXTERNAL_STORAGE,
//                                                permission.ACCESS_FINE_LOCATION,
//                                                permission.ACCESS_COARSE_LOCATION,
//                                                permission.ACCESS_BACKGROUND_LOCATION,
                                                permission.CALL_PHONE
                                            ), PERMISSION_REQUEST_CODE
                                        )
                                    }
                                })
                            return
                        } else if (shouldShowRequestPermissionRationale(permission.CALL_PHONE)) {
                            showMessageOKCancel("You need to allow access to phone the permissions",
                                DialogInterface.OnClickListener { dialog, which ->
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                        requestPermissions(
                                            arrayOf(
                                                permission.WRITE_EXTERNAL_STORAGE,
                                                permission.READ_EXTERNAL_STORAGE,
//                                                permission.ACCESS_FINE_LOCATION,
//                                                permission.ACCESS_COARSE_LOCATION,
//                                                permission.ACCESS_BACKGROUND_LOCATION,
                                                permission.CALL_PHONE
                                            ), PERMISSION_REQUEST_CODE
                                        )
                                    }
                                })
                            return
                        } /* else if (shouldShowRequestPermissionRationale(permission.ACCESS_FINE_LOCATION)) {
                            showMessageOKCancel("You need to allow access to fine location the permissions",
                                DialogInterface.OnClickListener { dialog, which ->
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                        requestPermissions(
                                            arrayOf(
                                                permission.WRITE_EXTERNAL_STORAGE,
                                                permission.READ_EXTERNAL_STORAGE,
                                                permission.ACCESS_FINE_LOCATION,
                                                permission.ACCESS_COARSE_LOCATION,
//                                                permission.ACCESS_BACKGROUND_LOCATION,
                                                permission.CALL_PHONE
                                            ),
                                            PERMISSION_REQUEST_CODE
                                        )
                                    }
                                })
                            return
                        } else if (shouldShowRequestPermissionRationale(permission.ACCESS_COARSE_LOCATION)) {
                            showMessageOKCancel("You need to allow access to cross location the permissions",
                                DialogInterface.OnClickListener { dialog, which ->
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                        requestPermissions(
                                            arrayOf(
                                                permission.WRITE_EXTERNAL_STORAGE,
                                                permission.READ_EXTERNAL_STORAGE,
                                                permission.ACCESS_FINE_LOCATION,
                                                permission.ACCESS_COARSE_LOCATION,
//                                                permission.ACCESS_BACKGROUND_LOCATION,
                                                permission.CALL_PHONE
                                            ),
                                            PERMISSION_REQUEST_CODE
                                        )
                                    }
                                })
                            return
                        } */
                        /*else if (shouldShowRequestPermissionRationale(permission.ACCESS_BACKGROUND_LOCATION)) {
                                showMessageOKCancel("You need to allow access to background location the permissions",
                                    DialogInterface.OnClickListener { dialog, which ->
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                            requestPermissions(
                                                arrayOf(
                                                    permission.WRITE_EXTERNAL_STORAGE,
                                                    permission.READ_EXTERNAL_STORAGE,
                                                    permission.ACCESS_FINE_LOCATION,
                                                    permission.ACCESS_COARSE_LOCATION,
    //                                                permission.ACCESS_BACKGROUND_LOCATION,
                                                    permission.CALL_PHONE
                                                ),
                                                PERMISSION_REQUEST_CODE
                                            )
                                        }
                                    })
                                return
                            }*/
                    }
                }
            }
        }
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this@SplashActivity).setMessage(message)
            .setPositiveButton("OK", okListener).setNegativeButton(
                "Cancel"
            ) { dialogInterface, i -> finishAffinity() }.create().show()
    }

    private fun myLatestUpdate(OSName: String, app_version: String, app_name: String) {
        val pd = CustomProgressBar(this@SplashActivity)
        pd.show()
        val call: Call<latest_update_response> =
            MyHssApplication.instance!!.api.latestupdate(OSName, app_version, app_name)
        call.enqueue(object : Callback<latest_update_response> {
            override fun onResponse(
                call: Call<latest_update_response>, response: Response<latest_update_response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {
                        try {
                            val forceUpdateRequired = response.body()!!.data!!.forceUpdateRequired
                            val lastestAppVersion = response.body()!!.data!!.lastestAppVersion

                            if (forceUpdateRequired == "false") {
                                Handler().postDelayed({
                                    if (sharedPreferences.getString("USERID", "") != "") {
                                        try {
                                            if (sharedPreferences.getString("MEMBERID", "") != "") {
                                                val i = Intent(
                                                    this@SplashActivity,
                                                    Passcode_Activity::class.java
                                                )
                                                i.putExtra("CHANGE_BIOMETRIC", "")
                                                startActivity(i)
                                                finish()
                                            } else {
                                                startActivity(
                                                    Intent(
                                                        this@SplashActivity,
                                                        WelcomeActivity::class.java
                                                    )
                                                )
                                                finish()
                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                    } else {
                                        startActivity(
                                            Intent(
                                                this@SplashActivity, LoginActivity::class.java
                                            )
                                        )
                                        finish()
                                    }
                                }, 500)

                            } else {
                                val alertDialog: AlertDialog.Builder =
                                    AlertDialog.Builder(this@SplashActivity)
                                alertDialog.setTitle(getString(R.string.app_name))
                                alertDialog.setMessage("Update available app is needed to force update")
                                alertDialog.setPositiveButton(
                                    "yes"
                                ) { _, _ ->
                                    openAppInPlayStore(packageName)
                                }
                                alertDialog.setNegativeButton(
                                    "No"
                                ) { _, _ ->
                                    Handler().postDelayed({
                                        if (sharedPreferences.getString("USERID", "") != "") {
                                            try {
                                                if (sharedPreferences.getString(
                                                        "MEMBERID", ""
                                                    ) != ""
                                                ) {
                                                    val i = Intent(
                                                        this@SplashActivity,
                                                        Passcode_Activity::class.java
                                                    )
                                                    i.putExtra("CHANGE_BIOMETRIC", "")
                                                    startActivity(i)
                                                    finish()
                                                } else {
                                                    startActivity(
                                                        Intent(
                                                            this@SplashActivity,
                                                            WelcomeActivity::class.java
                                                        )
                                                    )
                                                    finish()
                                                }
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                            }
                                        } else {
                                            startActivity(
                                                Intent(
                                                    this@SplashActivity, LoginActivity::class.java
                                                )
                                            )
                                            finish()
                                        }
                                    }, 500)
                                }
                                val alert: AlertDialog = alertDialog.create()
                                alert.setCanceledOnTouchOutside(false)
                                alert.show()
                            }
                        } catch (e: ArithmeticException) {
                            println(e.toString())
                        } finally {
                            println("forceUpdateRequired")
                        }

                    } else {
                        Functions.showAlertMessageWithOK(
                            this@SplashActivity, "Message", response.body()?.message
                        )
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@SplashActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<latest_update_response>, t: Throwable) {
                Toast.makeText(this@SplashActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    fun openAppInPlayStore(appPackageName: String) {
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")
                )
            )
        } catch (exception: android.content.ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                )
            )
        }
    }

    private fun displayLocationSettingsRequest(context: Context) {
        val googleApiClient = GoogleApiClient.Builder(context).addApi(LocationServices.API).build()
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
                LocationSettingsStatusCodes.SUCCESS -> Log.i(
                    TAG.toString(), "All location settings are satisfied."
                )
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    Log.i(
                        TAG.toString(),
                        "Location settings are not satisfied. Show the user a dialog to upgrade location settings "
                    )
                    try {
                        // Show the dialog by calling startResolutionForResult(), and check the result
                        // in onActivityResult().
                        status.startResolutionForResult(
                            this@SplashActivity, 1
                        )
                    } catch (e: IntentSender.SendIntentException) {
                        Log.i(TAG.toString(), "PendingIntent unable to execute request.")
                    }
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Log.i(
                    TAG.toString(),
                    "Location settings are inadequate, and cannot be fixed here. Dialog not created."
                )
            }
        }
    }
}