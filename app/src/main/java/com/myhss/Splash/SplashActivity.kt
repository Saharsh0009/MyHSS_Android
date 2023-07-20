package com.uk.myhss.Splash

import android.Manifest.permission
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Obtain the shared Tracker instance.
        val application = application as MyApplication
        mTracker = application.defaultTracker
        Log.i(TAG.toString(), "Setting screen name: ${this@SplashActivity}")
        mTracker!!.setScreenName("Image~${this@SplashActivity}")
        mTracker!!.send(ScreenViewBuilder().build())
        mTracker!!.send(EventBuilder().setCategory("Action").setAction("Share").build())

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        sessionManager = SessionManager(this)

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
        Handler().postDelayed({
            Log.e("TAG-SharedPref", "" + sharedPreferences.getString("USERID", ""))
            if (Functions.isConnectingToInternet(this@SplashActivity)) {
                val OSName = "android"
                val app_version = currentVersion
                val app_name = "android_hss"//getString(R.string.app_name)
                myLatestUpdate(OSName, app_version, app_name)
            } else {
                showAlertDialogforfinish("No Internet", "Please check your internet connection!")
            }
        }, 500)
    }

    private fun myLatestUpdate(OSName: String, app_version: String, app_name: String) {
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
                                    Log.e(
                                        "TAG-SharedPref",
                                        "" + sharedPreferences.getString("USERID", "")
                                    )
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
                            showAlertDialogforfinish(
                                "Error Message",
                                getString(R.string.some_thing_wrong)
                            )
                        }

                    } else {
                        showAlertDialogforfinish(
                            "Error Message", response.body()?.message.toString()
                        )
                    }
                } else {
                    showAlertDialogforfinish("Error Message", getString(R.string.some_thing_wrong))
                }
            }

            override fun onFailure(call: Call<latest_update_response>, t: Throwable) {
                showAlertDialogforfinish("Error Message", t.message.toString())
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

    fun showAlertDialogforfinish(sTitle: String, sBody: String) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@SplashActivity)
        alertDialog.setTitle(sTitle)
        alertDialog.setMessage(sBody)
        alertDialog.setPositiveButton(
            "Ok"
        ) { _, _ ->
            finish()
        }
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }
}