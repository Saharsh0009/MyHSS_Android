package com.uk.myhss.Main

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.R.attr
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Scope
import com.google.android.gms.common.api.Status
import com.google.android.gms.drive.Drive
import com.google.android.gms.drive.DriveClient
import com.google.android.gms.drive.DriveResourceClient
import com.google.android.gms.location.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.Main.adapter.ClickListener
import com.myhss.Main.adapter.NavigationItemModel
import com.myhss.Main.adapter.NavigationRVAdapter
import com.myhss.Main.adapter.RecyclerTouchListener
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.Functions
import com.myhss.ui.Biomeric.ChangeBiomerticFragment
import com.myhss.ui.SuchanaBoard.NotificationList
import com.uk.myhss.Login_Registration.LoginActivity
import com.uk.myhss.Main.Get_Privileges.Get_Privileges_Response
import com.uk.myhss.Main.Get_Prpfile.Datum_Get_Profile
import com.uk.myhss.Main.Get_Prpfile.Get_Profile_Response
import com.uk.myhss.Main.Get_Prpfile.Member_Get_Profile
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.ui.dashboard.DashboardFragment
import com.uk.myhss.ui.dashboard.LinkedFamilyFragment
import com.uk.myhss.ui.policies.ChangePasswordFragment
import com.uk.myhss.ui.policies.PolicieshowFragment
import com.uk.myhss.ui.policies.ProfileFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*


import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.integration.android.IntentIntegrator
import com.myhss.QRCode.QRCodeFragment
import android.content.DialogInterface

import android.R.attr.data
import com.blikoon.qrcodescanner.QrCodeActivity
import com.myhss.QRCode.CaptureActivityPortrait
import com.myhss.Utils.DebugLog


//import com.blikoon.qrcodescanner.QrCodeActivity


class HomeActivity : AppCompatActivity() { //, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var sessionManager: SessionManager

    private val PERMISSION_REQUEST_CODE = 200

    //    private val sharedPrefFile = "MyHss"
    lateinit var First_name: String
    lateinit var Last_name: String

    lateinit var drawerLayout: DrawerLayout
    private lateinit var adapter: NavigationRVAdapter
    private lateinit var navigation_rv: RecyclerView

    var serviceListener: ServiceListener? = null //1
    private var driveClient: DriveClient? = null //2
    private var driveResourceClient: DriveResourceClient? = null //3
    private var signInAccount: GoogleSignInAccount? = null //4

    private val REQUEST_CODE_QR_SCAN = 101
    private val LOGTAG = "QRCScanner-MainActivity"

    companion object {
        private val SCOPES = setOf<Scope>(Drive.SCOPE_FILE, Drive.SCOPE_APPFOLDER)
        val documentMimeTypes = arrayListOf(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        )

        const val REQUEST_CODE_OPEN_ITEM = 100
        const val REQUEST_CODE_SIGN_IN = 101
        const val TAG = "GoogleDriveService"
    }

    data class GoogleDriveConfig(
        val activityTitle: String? = null, val mimeTypes: List<String>? = null
    )

    class GoogleDriveService(
        private val activity: Activity, private val config: GoogleDriveConfig
    ) {

    }

    private var items = arrayListOf(
        NavigationItemModel(R.drawable.home_icon, "Dashboard"),
        NavigationItemModel(R.drawable.shakha_icon, "Shakha"),
//        NavigationItemModel(R.drawable.qr_code, "Scan QR Code"),
//        NavigationItemModel(R.drawable.drive_img, "Google Drive"),
        NavigationItemModel(R.drawable.khel_icon, "Khel App"),  // Pratiyogita
        NavigationItemModel(R.drawable.lock, "Change Password"),
        NavigationItemModel(R.drawable.fingerprint, "Change Biometric"),
        NavigationItemModel(R.drawable.policy_icon, "Policies"),
        NavigationItemModel(R.drawable.logout, "Logout")
    )

    private var itemsnew = arrayListOf(
        NavigationItemModel(R.drawable.home_icon, "Dashboard"),
//        NavigationItemModel(R.drawable.drive_img, "Google Drive"),
        NavigationItemModel(R.drawable.khel_icon, "Khel App"),  // Pratiyogita
        NavigationItemModel(R.drawable.lock, "Change Password"),
        NavigationItemModel(R.drawable.fingerprint, "Change Biometric"),
        NavigationItemModel(R.drawable.policy_icon, "Policies"),
        NavigationItemModel(R.drawable.logout, "Logout")
    )

    /*private var items = arrayListOf(
        NavigationItemModel(R.drawable.home_icon, "Dashboard"),
        NavigationItemModel(R.drawable.organisation_icon, "Organization"),
        NavigationItemModel(R.drawable.shakha_icon, "Shakha"),
        NavigationItemModel(R.drawable.shakha_type_icon, "Shakha Type"),
        NavigationItemModel(R.drawable.roles_icon, "Roles"),
        NavigationItemModel(R.drawable.roles_responsiblity_icon, "Roles & Responsibility"),
        NavigationItemModel(R.drawable.karyakartas_icon, "Karyakartas"),
        NavigationItemModel(R.drawable.sankhya_icon, "Sankhya Report"),
        NavigationItemModel(R.drawable.setting_icon, "Setting"),
        NavigationItemModel(R.drawable.policy_icon, "Policies"),
        NavigationItemModel(R.drawable.khel_icon, "Khel Pratiyogita App"),
        NavigationItemModel(R.drawable.logout_icon, "Logout")
    )*/

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("SetTextI18n", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar: Toolbar = findViewById(R.id.activity_main_toolbar)
        setSupportActionBar(toolbar)

        val toolbar_logo = toolbar.findViewById<ImageView>(R.id.toolbar_logo)
        val scan_qr_img = toolbar.findViewById<ImageView>(R.id.scan_qr_img)
        val notification_img = toolbar.findViewById<ImageView>(R.id.notification_img)
        val activity_main_toolbar_title =
            toolbar.findViewById<TextView>(R.id.activity_main_toolbar_title)
        toolbar_logo.visibility = View.VISIBLE
        activity_main_toolbar_title.visibility = View.INVISIBLE

        toolbar.title = ""
//        activity_main_toolbar_title.text = ""
//        toolbar.setLogo(R.drawable.dashboard_logo)

        sessionManager = SessionManager(this)

        displayLocationSettingsRequest(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("HomeVC")
        sessionManager.firebaseAnalytics.setUserProperty("HomeVC", "HomeActivity")

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        notification_img.visibility = View.GONE
        scan_qr_img.visibility = View.GONE

        scan_qr_img.setOnClickListener {
//            val i = Intent(this@HomeActivity, QRCodeFragment::class.java)
//            startActivity(i)
            val integrator = IntentIntegrator(this@HomeActivity)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
            integrator.setPrompt("Scan")
            integrator.setCameraId(0)
            integrator.setBeepEnabled(true)
            integrator.setBarcodeImageEnabled(true)
//            integrator.captureActivity = HomeActivity::class.java
            integrator.setTorchEnabled(true)
            integrator.setOrientationLocked(true)
            integrator.initiateScan()
        }

        notification_img.visibility = View.GONE
        notification_img.setOnClickListener {
            /*toolbar.title = "Notification"
            toolbar_logo.visibility = View.GONE
            notification_img.visibility = View.GONE*/

            val i = Intent(this@HomeActivity, NotificationList::class.java)
            startActivity(i)

            /*val NotificationListFragment = NotificationList()
            supportFragmentManager.beginTransaction()
                .replace(R.id.activity_main_content_id, NotificationListFragment).commit()*/
        }

        if (Functions.isConnectingToInternet(this@HomeActivity)) {
            val user_id = sessionManager.fetchUserID()
            val member_id = sessionManager.fetchMEMBERID()
            val devicetype = "A"
            val device_token = sessionManager.fetchFCMDEVICE_TOKEN()
            Log.d("device_token", device_token!!)
//            myPrivileges("1", "approve")
//            if (sessionManager.fetchMEMBERID() != "") {
            myProfile(user_id!!, member_id!!, devicetype, device_token)
//            }
        } else {
            Toast.makeText(
                this@HomeActivity, resources.getString(R.string.no_connection), Toast.LENGTH_SHORT
            ).show()
        }

        drawerLayout = findViewById(R.id.drawer_layout)

        val activity_main_toolbar: Toolbar = findViewById(R.id.activity_main_toolbar)
        // Set the toolbar
        setSupportActionBar(activity_main_toolbar)

        navigation_rv = findViewById(R.id.navigation_rv)
        // Setup Recyclerview's Layout
        navigation_rv.layoutManager = LinearLayoutManager(this)
        navigation_rv.setHasFixedSize(true)

        // Add Item Touch Listener
        navigation_rv.addOnItemTouchListener(
            RecyclerTouchListener(this@HomeActivity, object : ClickListener {
                override fun onClick(view: View, position: Int) {
                    if (sessionManager.fetchSHAKHA_TAB() == "yes") {
                        when (position) {
                            0 -> {
                                toolbar.title = ""
//                        activity_main_toolbar_title.setText("")
                                toolbar_logo.visibility = View.VISIBLE
                                scan_qr_img.visibility = View.GONE
                                notification_img.visibility = View.GONE
                                // # Dashboard Fragment
                                val dashboardFragment = DashboardFragment()
                                supportFragmentManager.beginTransaction()
                                    .replace(R.id.activity_main_content_id, dashboardFragment)
                                    .commit()
                            }
                            1 -> {
                                // # QR Code Fragment
                                val i = Intent(this@HomeActivity, LinkedFamilyFragment::class.java)
                                i.putExtra("DashBoard", "SHAKHAVIEW")
                                i.putExtra("headerName", getString(R.string.my_shakha))
                                startActivity(i)
//                            val organisationFragment = OrgranizationFragment()
//                            supportFragmentManager.beginTransaction()
//                                .replace(R.id.activity_main_content_id, organisationFragment).commit()
                            }
                            /*2 -> {
    //                            val i = Intent(this@HomeActivity, QrCodeActivity::class.java)
    //                            startActivity(i)

                                val integrator = IntentIntegrator(this@HomeActivity)
                                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                                integrator.setPrompt("Scan")
                                integrator.setCameraId(0)
                                integrator.setBeepEnabled(true)
                                integrator.setBarcodeImageEnabled(true)
    //            integrator.captureActivity = HomeActivity::class.java
                                integrator.setTorchEnabled(false)
                                integrator.captureActivity = CaptureActivityPortrait::class.java
    //                            integrator.setResultDisplayDuration(0) //Text..
    //                            integrator.setScanningRectangle(450, 450);//size
                                integrator.setOrientationLocked(false)
                                integrator.initiateScan()
                            }*/
//                        2 -> {
//                            val i = Intent(this@HomeActivity, QRCodeFragment::class.java)
//                            startActivity(i)
//                        }
                            /*2 -> {
                                startOpenGoogleDriveApp()
    //                            checkLoginStatus()
    //                            startActivity(Intent(this@HomeActivity, GoogleCalendar::class.java))
                            }*/
                            2 -> {
                                // Use package name which we want to check
                                val isAppInstalled: Boolean =
                                    appInstalledOrNot("market://details?id=com.hss.khelappandroid")

                                if (isAppInstalled) {
                                    //This intent will help you to launch if the package is already installed
                                    val LaunchIntent =
                                        packageManager.getLaunchIntentForPackage("market://details?id=com.hss.khelappandroid")
                                    startActivity(LaunchIntent)
                                    Log.d("", "Application is already installed.")
                                } else {
                                    // Do whatever we want to do if application not installed
                                    // For example, Redirect to play store
                                    Log.d("", "Application is not currently installed.")
                                    startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("market://details?id=com.hss.khelappandroid")
                                        )
                                    )
                                }
                            }
                            3 -> {
                                toolbar.title = "Change Password"
                                toolbar_logo.visibility = View.GONE
                                scan_qr_img.visibility = View.GONE
                                notification_img.visibility = View.GONE
                                // # Policieshow Fragment
                                val organisationFragment = ChangePasswordFragment()
                                supportFragmentManager.beginTransaction().replace(
                                    R.id.activity_main_content_id, organisationFragment
                                ).commit()
                            }
                            4 -> {
                                /*val i = Intent(this@HomeActivity, Passcode_Activity::class.java)
                                i.putExtra("CHANGE_BIOMETRIC", "CHANGE_BIOMETRIC")
                                startActivity(i)
                                finish()*/
                                toolbar.title = "Change Biometric"
                                toolbar_logo.visibility = View.GONE
                                scan_qr_img.visibility = View.GONE
                                notification_img.visibility = View.GONE
                                // # Policieshow Fragment
                                val organisationFragment = ChangeBiomerticFragment()
                                supportFragmentManager.beginTransaction().replace(
                                    R.id.activity_main_content_id, organisationFragment
                                ).commit()
                            }
                            5 -> {
                                toolbar.title = "Policies"
//                        activity_main_toolbar_title.setText("Policies")
                                toolbar_logo.visibility = View.GONE
                                scan_qr_img.visibility = View.GONE
                                notification_img.visibility = View.GONE
                                // # Policieshow Fragment
                                val organisationFragment = PolicieshowFragment()
                                supportFragmentManager.beginTransaction().replace(
                                    R.id.activity_main_content_id, organisationFragment
                                ).commit()
                            }
                            6 -> {
                                val alertDialog: AlertDialog.Builder =
                                    AlertDialog.Builder(this@HomeActivity)
//            alertDialog.setTitle("Logout")
                                alertDialog.setMessage("Are you sure you would like to logout?")
                                alertDialog.setPositiveButton(
                                    "yes"
                                ) { _, _ ->

                                    val sharedPreferences = this@HomeActivity.getSharedPreferences(
                                        "production", Context.MODE_PRIVATE
                                    )
                                    sessionManager.saveFIRSTNAME("")
                                    sessionManager.saveSURNAME("")
                                    sessionManager.saveUSERNAME("")
                                    sessionManager.saveUserID("")
                                    sessionManager.saveUSEREMAIL("")
                                    sessionManager.saveUSERROLE("")
                                    sessionManager.saveMEMBERID("")
                                    sessionManager.saveSECURITYKEY("")
                                    sessionManager.saveAuthToken("")
                                    sessionManager.saveMIDDLENAME("")
                                    sessionManager.saveSHAKHA_SANKHYA_AVG("")
                                    sessionManager.saveSHAKHA_TAB("")
                                    sessionManager.saveSHAKHANAME("")
                                    sessionManager.savePOSTCODE("")
                                    sessionManager.saveCOUNTRY("")
                                    sessionManager.saveCITY("")
                                    sessionManager.saveLineOne("")
                                    sessionManager.saveGENDER("")
                                    sessionManager.saveAGE("")
                                    sessionManager.saveQUALIFICATIONAID("")
                                    sessionManager.saveQUALIFICATION_VALUE("")
                                    sessionManager.saveQUALIFICATION_VALUE_NAME("")
                                    sessionManager.saveQUALIFICATION_PRO_BODY_RED_NO("")
                                    sessionManager.saveQUALIFICATION_DATE("")
                                    sessionManager.saveQUALIFICATION_FILE("")
                                    sessionManager.saveQUALIFICATION_IS_DOC("")
                                    sessionManager.saveDOB("")
                                    sessionManager.saveVIBHAGNAME("")
                                    sessionManager.saveSPOKKENLANGUAGE("")
                                    sessionManager.saveMOBILENO("")
                                    sessionManager.saveSECMOBILENO("")
                                    sessionManager.saveOCCUPATIONNAME("")
                                    sessionManager.saveADDRESS("")
                                    sessionManager.saveGUAEMREMAIL("")
                                    sessionManager.saveGUAEMRNAME("")
                                    sessionManager.saveGUAEMRPHONE("")
                                    sessionManager.saveGUAEMRRELATIONSHIP("")
                                    sessionManager.saveSPOKKENLANGUAGEID("")
                                    sessionManager.saveSPOKKENLANGUAGE("")
                                    sessionManager.saveRELATIONSHIPNAME("")
                                    sessionManager.saveRELATIONSHIPNAME_OTHER("")
                                    sessionManager.saveNAGARID("")
                                    sessionManager.saveDIETARY("")
                                    sessionManager.saveDIETARYID("")
                                    sessionManager.saveVIBHAGID("")
                                    sessionManager.saveSTATE_IN_INDIA("")
                                    sessionManager.saveSHAKHAID("")

                                    sharedPreferences.edit().apply {
                                        putString("FIRSTNAME", "")
                                        putString("SURNAME", "")
                                        putString("USERNAME", "")
                                        putString("USERID", "")
                                        putString("USEREMAIL", "")
                                        putString("USERROLE", "")
                                        putString("MEMBERID", "")
                                        putString("SECURITYKEY", "")
                                        putString("USERTOKEN", "")
                                        putString("Allow_biometric", "")
                                    }.apply()

                                    val i = Intent(this@HomeActivity, LoginActivity::class.java)
                                    startActivity(i)
                                    finishAffinity()
                                }
                                alertDialog.setNegativeButton(
                                    "No"
                                ) { _, _ ->
                                    val i = Intent(this@HomeActivity, HomeActivity::class.java)
                                    startActivity(i)
                                    finishAffinity()
                                }
                                val alert: AlertDialog = alertDialog.create()
                                alert.setCanceledOnTouchOutside(false)
                                alert.show()
                            }
                        }
                    } else {
                        when (position) {
                            0 -> {
                                toolbar.title = ""
//                        activity_main_toolbar_title.setText("")
                                toolbar_logo.visibility = View.VISIBLE
                                scan_qr_img.visibility = View.GONE
                                notification_img.visibility = View.GONE
                                // # Dashboard Fragment
                                val dashboardFragment = DashboardFragment()
                                supportFragmentManager.beginTransaction()
                                    .replace(R.id.activity_main_content_id, dashboardFragment)
                                    .commit()
                            }
                            /*1 -> {
                                startOpenGoogleDriveApp()
    //                            checkLoginStatus()
    //                            startActivity(Intent(this@HomeActivity, GoogleCalendar::class.java))
                            }*/
                            1 -> {
                                // Use package name which we want to check
                                val isAppInstalled: Boolean =
                                    appInstalledOrNot("market://details?id=com.hss.khelappandroid")

                                if (isAppInstalled) {
                                    //This intent will help you to launch if the package is already installed
                                    val LaunchIntent =
                                        packageManager.getLaunchIntentForPackage("market://details?id=com.hss.khelappandroid")
                                    startActivity(LaunchIntent)
                                    Log.d("", "Application is already installed.")
                                } else {
                                    // Do whatever we want to do if application not installed
                                    // For example, Redirect to play store
                                    Log.d("", "Application is not currently installed.")
                                    startActivity(
                                        Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("market://details?id=com.hss.khelappandroid")
                                        )
                                    )
                                }
                            }
                            2 -> {
                                toolbar.title = "Change Password"
//                        activity_main_toolbar_title.setText("Policies")
                                toolbar_logo.visibility = View.GONE
                                scan_qr_img.visibility = View.GONE
                                notification_img.visibility = View.GONE
                                // # Policieshow Fragment
                                val organisationFragment = ChangePasswordFragment()
                                supportFragmentManager.beginTransaction().replace(
                                    R.id.activity_main_content_id, organisationFragment
                                ).commit()
                            }
                            3 -> {
                                toolbar.title = "Change Biometric"
                                toolbar_logo.visibility = View.GONE
                                scan_qr_img.visibility = View.GONE
                                notification_img.visibility = View.GONE
                                // # Policieshow Fragment
                                val organisationFragment = ChangeBiomerticFragment()
                                supportFragmentManager.beginTransaction().replace(
                                    R.id.activity_main_content_id, organisationFragment
                                ).commit()
                                /*val i = Intent(this@HomeActivity, Passcode_Activity::class.java)
                                i.putExtra("CHANGE_BIOMETRIC", "CHANGE_BIOMETRIC")
                                startActivity(i)
                                finish()*/
                            }
                            4 -> {
                                toolbar.title = "Policies"
//                        activity_main_toolbar_title.setText("Policies")
                                toolbar_logo.visibility = View.GONE
                                scan_qr_img.visibility = View.GONE
                                notification_img.visibility = View.GONE
                                // # Policieshow Fragment
                                val organisationFragment = PolicieshowFragment()
                                supportFragmentManager.beginTransaction().replace(
                                    R.id.activity_main_content_id, organisationFragment
                                ).commit()
                            }
                            5 -> {
                                val alertDialog: AlertDialog.Builder =
                                    AlertDialog.Builder(this@HomeActivity)
//            alertDialog.setTitle("Logout")
                                alertDialog.setMessage("Are you sure you would like to logout?")
                                alertDialog.setPositiveButton(
                                    "yes"
                                ) { _, _ ->

                                    val sharedPreferences = this@HomeActivity.getSharedPreferences(
                                        "production", Context.MODE_PRIVATE
                                    )
                                    sessionManager.saveFIRSTNAME("")
                                    sessionManager.saveSURNAME("")
                                    sessionManager.saveUSERNAME("")
                                    sessionManager.saveUserID("")
                                    sessionManager.saveUSEREMAIL("")
                                    sessionManager.saveUSERROLE("")
                                    sessionManager.saveMEMBERID("")
                                    sessionManager.saveSECURITYKEY("")
                                    sessionManager.saveAuthToken("")
                                    sessionManager.saveMIDDLENAME("")
                                    sessionManager.saveSHAKHA_SANKHYA_AVG("")
                                    sessionManager.saveSHAKHA_TAB("")
                                    sessionManager.saveSHAKHANAME("")
                                    sessionManager.savePOSTCODE("")
                                    sessionManager.saveCOUNTRY("")
                                    sessionManager.saveCITY("")
                                    sessionManager.saveLineOne("")
                                    sessionManager.saveGENDER("")
                                    sessionManager.saveAGE("")
                                    sessionManager.saveQUALIFICATIONAID("")
                                    sessionManager.saveQUALIFICATION_VALUE("")
                                    sessionManager.saveQUALIFICATION_VALUE_NAME("")
                                    sessionManager.saveQUALIFICATION_PRO_BODY_RED_NO("")
                                    sessionManager.saveQUALIFICATION_DATE("")
                                    sessionManager.saveQUALIFICATION_FILE("")
                                    sessionManager.saveQUALIFICATION_IS_DOC("")
                                    sessionManager.saveDOB("")
                                    sessionManager.saveVIBHAGNAME("")
                                    sessionManager.saveSPOKKENLANGUAGE("")
                                    sessionManager.saveMOBILENO("")
                                    sessionManager.saveSECMOBILENO("")
                                    sessionManager.saveOCCUPATIONNAME("")
                                    sessionManager.saveADDRESS("")
                                    sessionManager.saveGUAEMREMAIL("")
                                    sessionManager.saveGUAEMRNAME("")
                                    sessionManager.saveGUAEMRPHONE("")
                                    sessionManager.saveGUAEMRRELATIONSHIP("")
                                    sessionManager.saveSPOKKENLANGUAGEID("")
                                    sessionManager.saveSPOKKENLANGUAGE("")
                                    sessionManager.saveRELATIONSHIPNAME("")
                                    sessionManager.saveRELATIONSHIPNAME_OTHER("")
                                    sessionManager.saveNAGARID("")
                                    sessionManager.saveDIETARY("")
                                    sessionManager.saveDIETARYID("")
                                    sessionManager.saveVIBHAGID("")
                                    sessionManager.saveSTATE_IN_INDIA("")
                                    sessionManager.saveSHAKHAID("")

                                    sharedPreferences.edit().apply {
                                        putString("FIRSTNAME", "")
                                        putString("SURNAME", "")
                                        putString("USERNAME", "")
                                        putString("USERID", "")
                                        putString("USEREMAIL", "")
                                        putString("USERROLE", "")
                                        putString("MEMBERID", "")
                                        putString("SECURITYKEY", "")
                                        putString("USERTOKEN", "")
                                        putString("Allow_biometric", "")
                                    }.apply()

                                    val i = Intent(this@HomeActivity, LoginActivity::class.java)
                                    startActivity(i)
                                    finishAffinity()
                                }
                                alertDialog.setNegativeButton(
                                    "No"
                                ) { _, _ ->
                                    val i = Intent(this@HomeActivity, HomeActivity::class.java)
                                    startActivity(i)
                                    finishAffinity()
                                }
                                val alert: AlertDialog = alertDialog.create()
                                alert.setCanceledOnTouchOutside(false)
                                alert.show()
                            }
                        }
                    }

                    // Don't highlight the 'Profile' and 'Like us on Facebook' item row
//                if (position != 6 && position != 4) {
//                    updateAdapter(position)
//                }
                    Handler().postDelayed({
                        drawerLayout.closeDrawer(GravityCompat.START)
                    }, 200)
                }
            })
        )

        // Update Adapter with item data and highlight the default menu item ('Home' Fragment)
//        updateAdapter(0)

        // Set 'Home' as the default fragment when the app starts
//        val dashboardFragment = DashboardFragment()
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.activity_main_content_id, dashboardFragment).commit()

        // Close the soft keyboard when you open or close the Drawer
        val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawerLayout,
            activity_main_toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {
            override fun onDrawerClosed(drawerView: View) {
                // Triggered once the drawer closes
                super.onDrawerClosed(drawerView)
                try {
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                } catch (e: Exception) {
                    e.stackTrace
                }
            }

            override fun onDrawerOpened(drawerView: View) {
                // Triggered once the drawer opens
                super.onDrawerOpened(drawerView)
                try {
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
                } catch (e: Exception) {
                    e.stackTrace
                }
            }
        }
        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()

        // Set Header Image
//        navigation_header_img.setImageResource(R.drawable.logo)
        // Set background of Drawer
//        navigation_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))

        val profile_view = findViewById<LinearLayout>(R.id.profile_view)
        val user_name = findViewById<TextView>(R.id.user_name)
        val user_name_txt = findViewById<TextView>(R.id.user_name_txt)
        val user_role = findViewById<TextView>(R.id.user_role)
        val app_version = findViewById<TextView>(R.id.app_version)

        try {
            val currentVersion = packageManager.getPackageInfo(packageName, 0).versionName
            app_version.text = "App Version: $currentVersion"
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        user_name_txt.text = sessionManager.fetchFIRSTNAME()!!
            .capitalize(Locale.ROOT) + " " + sessionManager.fetchSURNAME()!!.capitalize(Locale.ROOT)
        user_role.text = sessionManager.fetchUSERROLE()!!.capitalize(Locale.ROOT)

        if (sessionManager.fetchSURNAME() != "" && sessionManager.fetchSURNAME() != "") {
            val first: String = sessionManager.fetchFIRSTNAME()!!.substring(0, 1)
                .uppercase(Locale.getDefault()) + sessionManager.fetchFIRSTNAME()!!.substring(
                1
            )
            val getfirst = first
            First_name = getfirst.substring(0, 1)

            val second: String = sessionManager.fetchSURNAME()!!.substring(0, 1)
                .toUpperCase(Locale.getDefault()) + sessionManager.fetchSURNAME()!!.substring(
                1
            )
            val getsecond = second
            Last_name = getsecond.substring(0, 1)

            user_name.text = First_name + Last_name
        }

        profile_view.setOnClickListener {
            Log.d("Hi", "Profile")
            openCloseNavigationDrawer()
            val i = Intent(this@HomeActivity, ProfileFragment::class.java)
            i.putExtra("FAMILY", i.getStringExtra("PROFILE"))
            startActivity(i)
        }

        val sharedNameValue = sessionManager.fetchUSERNAME()
        Log.d("NAME-->", "default name: ${sharedNameValue}")
    }

    fun startOpenGoogleDriveApp() {
        try {
            val intent =
                this.packageManager.getLaunchIntentForPackage("com.google.android.apps.docs")
            intent!!.putExtra(Intent.EXTRA_USER, "bhanu.iguru@gmail.com")
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val googleSignInClient: GoogleSignInClient by lazy {
        val builder = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        for (scope in SCOPES) {
            builder.requestScopes(scope)
        }
        val signInOptions = builder.build()
        GoogleSignIn.getClient(this@HomeActivity, signInOptions)
    }

    private fun handleSignIn(data: Intent) {
        val getAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
        if (getAccountTask.isSuccessful) {
            initializeDriveClient(getAccountTask.result!!)
        } else {
            serviceListener?.handleError(Exception("Sign-in failed.", getAccountTask.exception))
        }
    }

    private fun initializeDriveClient(signInAccount: GoogleSignInAccount) {
        driveClient = Drive.getDriveClient(this.applicationContext, signInAccount)
        driveResourceClient = Drive.getDriveResourceClient(this.applicationContext, signInAccount)
        serviceListener?.loggedIn()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_SIGN_IN -> {
                if (data != null) {
                    handleSignIn(data)
                } else {
                    serviceListener?.cancelled()
                }
            }

            REQUEST_CODE_OPEN_ITEM -> {
                if (data != null) {
//                    openItem(data)
                } else {
                    serviceListener?.cancelled()
                }
            }
        }

        /*if (resultCode !== RESULT_OK) {
            Log.d(LOGTAG, "COULD NOT GET A GOOD RESULT.")
            if (attr.data == null) return
            //Getting the passed result
            val result: String = "Error, Read in QR Code"
//                attr.data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image")
            if (result != null) {
                val alertDialog = AlertDialog.Builder(this@HomeActivity).create()
                alertDialog.setTitle("Scan Error")
                alertDialog.setMessage("QR Code could not be scanned")
                alertDialog.setButton(
                    AlertDialog.BUTTON_NEUTRAL, "OK"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()
            }
            return
        }
        if (requestCode === REQUEST_CODE_QR_SCAN) {
            if (attr.data == null) return
            //Getting the passed result
            val result: String = "Got QR Code Result"
//                attr.data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult")
            Log.d(LOGTAG, "Have scan result in your app activity :$result")
            val alertDialog = AlertDialog.Builder(this@HomeActivity).create()
            alertDialog.setTitle("Scan result")
            alertDialog.setMessage(result)
            alertDialog.setButton(
                AlertDialog.BUTTON_NEUTRAL, "OK"
            ) { dialog, which -> dialog.dismiss() }
            alertDialog.show()
        }*/

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Log.e("Scan*******", "Cancelled scan")
                val alertDialog = AlertDialog.Builder(this@HomeActivity).create()
                alertDialog.setTitle(R.string.app_name)
                alertDialog.setMessage("QR Code could not be scanned")
                alertDialog.setButton(
                    AlertDialog.BUTTON_NEUTRAL, "OK"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()
            } else {
                Log.e("Scan", "Scanned")
//                tv_qr_readTxt.setText(result.contents)
//                Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                val alertDialog = AlertDialog.Builder(this@HomeActivity).create()
                alertDialog.setTitle(R.string.app_name)
                alertDialog.setMessage(result.contents)
                alertDialog.setButton(
                    AlertDialog.BUTTON_NEUTRAL, "OK"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.show()
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun checkLoginStatus() {
        val requiredScopes = HashSet<Scope>(2)
        requiredScopes.add(Drive.SCOPE_FILE)
        requiredScopes.add(Drive.SCOPE_APPFOLDER)
        signInAccount = GoogleSignIn.getLastSignedInAccount(this)
        val containsScope = signInAccount?.grantedScopes?.containsAll(requiredScopes)
        val account = signInAccount
        if (account != null && containsScope == true) {
            initializeDriveClient(account)
        }
    }

    fun auth() {
        this.startActivityForResult(googleSignInClient.signInIntent, REQUEST_CODE_SIGN_IN)
    }

    fun logout() {
        googleSignInClient.signOut()
        signInAccount = null
    }

//    private fun openItem(data: Intent) {
//        val driveId = data.getParcelableExtra<DriveId>(OpenFileActivityOptions.EXTRA_RESPONSE_DRIVE_ID)
//        downloadFile(driveId)
//    }

    /*private fun downloadFile(data: DriveId?) {
        if (data == null) {
            Log.e(TAG, "downloadFile data is null")
            return
        }
        val drive = data.asDriveFile()
        var fileName = "test"
        driveResourceClient?.getMetadata(drive)?.addOnSuccessListener {
            fileName = it.originalFilename
        }
        val openFileTask = driveResourceClient?.openFile(drive, DriveFile.MODE_READ_ONLY)
        openFileTask?.continueWithTask { task ->
            val contents = task.result
            contents.inputStream.use {
                try {
                    //This is the app's download directory, not the phones
                    val storageDir = this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                    val tempFile = File(storageDir, fileName)
                    tempFile.createNewFile()
                    val sink = Okio.buffer(Okio.sink(tempFile))
                    sink.writeAll(Okio.source(it))
                    sink.close()

                    serviceListener?.fileDownloaded(tempFile)
                } catch (e: IOException) {
                    Log.e(TAG, "Problems saving file", e)
                    serviceListener?.handleError(e)
                }
            }
            driveResourceClient?.discardContents(contents)
        }?.addOnFailureListener { e ->
            // Handle failure
            Log.e(TAG, "Unable to read contents", e)
            serviceListener?.handleError(e)
        }
    }*/


    interface ServiceListener {
        fun loggedIn() //1
        fun fileDownloaded(file: File) //2
        fun cancelled() //3
        fun handleError(exception: Exception) //4
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateAdapter(highlightItemPos: Int) {
        if (sessionManager.fetchSHAKHA_TAB() == "yes") {
            Log.d("TAB", "YES")
            adapter = NavigationRVAdapter(items, highlightItemPos)
        } else {
            Log.d("TAB", "NO")
            adapter = NavigationRVAdapter(itemsnew, highlightItemPos)
        }
//        adapter = NavigationRVAdapter(items, highlightItemPos)
        navigation_rv.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    fun openCloseNavigationDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    /*myPrivileges API*/
    private fun myPrivileges(menu_id: String, approve: String) {
        val pd = CustomProgressBar(this@HomeActivity)
        pd.show()
        val call: Call<Get_Privileges_Response> = MyHssApplication.instance!!.api.get_privileges(
            sessionManager.fetchUserID()!!, menu_id, approve
        )
        call.enqueue(object : Callback<Get_Privileges_Response> {
            override fun onResponse(
                call: Call<Get_Privileges_Response>, response: Response<Get_Privileges_Response>
            ) {

                Log.d("status", response.body()?.status.toString())
                if (response.body()?.status!!) {
                    Functions.displayMessage(this@HomeActivity, response.body()?.message)
//                    Functions.showAlertMessageWithOK(
//                        this@HomeActivity, "",
////                        "Message",
//                        response.body()?.message
//                    )
                } else {
                    Functions.displayMessage(this@HomeActivity, response.body()?.message)
//                    Functions.showAlertMessageWithOK(
//                        this@HomeActivity, "",
////                        "Message",
//                        response.body()?.message
//                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Get_Privileges_Response>, t: Throwable) {
                Toast.makeText(this@HomeActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    /*myPrivileges API*/
    private fun myProfile(
        user_id: String,
        member_id: String,
        deviceType: String,
        device_token: String
    ) {
        val pd = CustomProgressBar(this@HomeActivity)
        pd.show()
        val call: Call<Get_Profile_Response> = MyHssApplication.instance!!.api.get_profile(
            user_id,
            member_id,
            deviceType,
            device_token
        )
        call.enqueue(object : Callback<Get_Profile_Response> {
            override fun onResponse(
                call: Call<Get_Profile_Response>, response: Response<Get_Profile_Response>
            ) {
                if (response.code() == 200 && response.body() != null) {

                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {
                        try {
                            var data_getprofile: List<Datum_Get_Profile> =
                                ArrayList<Datum_Get_Profile>()
                            data_getprofile = response.body()!!.data!!

                            var data_getmember: List<Member_Get_Profile> =
                                ArrayList<Member_Get_Profile>()
                            data_getmember = response.body()!!.member!!
//                    data_getmember = response.body()!!.data!![0].member!!

                            val sharedPreferences = getSharedPreferences(
                                "production", Context.MODE_PRIVATE
                            )

                            sharedPreferences.edit().apply {
                                putString("FIRSTNAME", data_getmember[0].firstName)
                                putString("SURNAME", data_getmember[0].lastName)
                                putString("USERNAME", data_getprofile[0].username)
                                putString("USERID", data_getmember[0].userId)
                                putString("USEREMAIL", data_getmember[0].email)
                                putString("USERROLE", data_getprofile[0].role)
                                putString("MEMBERID", data_getmember[0].memberId)
                            }.apply()

                            val editor: SharedPreferences.Editor = sharedPreferences.edit()
                            editor.putString("FIRSTNAME", data_getmember[0].firstName)
                            editor.putString("SURNAME", data_getmember[0].lastName)
                            editor.putString("USERNAME", data_getprofile[0].username)
                            editor.putString("USERID", data_getmember[0].userId)
                            editor.putString("USEREMAIL", data_getmember[0].email)
                            editor.putString("USERROLE", data_getprofile[0].role)
                            editor.putString("MEMBERID", data_getmember[0].memberId)
                            editor.apply()
                            editor.commit()

                            sessionManager.saveFIRSTNAME(data_getmember[0].firstName.toString())
                            sessionManager.saveMIDDLENAME(data_getmember[0].middleName.toString())
                            sessionManager.saveSURNAME(data_getmember[0].lastName.toString())
                            sessionManager.saveUSERNAME(data_getprofile[0].username.toString())
                            sessionManager.saveUSEREMAIL(data_getprofile[0].email.toString())
                            sessionManager.saveUSERROLE(data_getprofile[0].role.toString())
                            sessionManager.saveDOB(data_getmember[0].dob.toString())
                            sessionManager.saveSHAKHANAME(data_getmember[0].shakha.toString())
                            sessionManager.saveSHAKHAID(data_getmember[0].shakhaId.toString())
                            sessionManager.saveNAGARID(data_getmember[0].nagarId.toString())
                            sessionManager.saveVIBHAGID(data_getmember[0].vibhagId.toString())
                            sessionManager.saveNAGARNAME(data_getmember[0].nagar.toString())
                            sessionManager.saveVIBHAGNAME(data_getmember[0].vibhag.toString())
                            sessionManager.saveADDRESS(
                                data_getmember[0].buildingName.toString() + " " + data_getmember[0].addressLine1.toString() + " " + data_getmember[0].addressLine2.toString()
                            )
                            sessionManager.saveLineOne(data_getmember[0].addressLine1.toString())
                            sessionManager.saveRELATIONSHIPNAME(data_getmember[0].relationship.toString())
                            sessionManager.saveRELATIONSHIPNAME_OTHER(data_getmember[0].otherRelationship.toString())
                            sessionManager.saveOCCUPATIONNAME(data_getmember[0].occupation.toString())
                            sessionManager.saveSPOKKENLANGUAGE(data_getmember[0].rootLanguage.toString())
                            sessionManager.saveSPOKKENLANGUAGEID(data_getmember[0].root_language_id.toString())
                            sessionManager.saveMOBILENO(data_getmember[0].mobile.toString())
                            if (!data_getmember[0].landLine.toString().equals("null")) {
                                sessionManager.saveSECMOBILENO(data_getmember[0].landLine.toString())
                            }
                            if (!data_getmember[0].secondaryEmail.toString().equals("null")) {
                                sessionManager.saveSECEMAIL(data_getmember[0].secondaryEmail.toString())
                            }
                            DebugLog.e("First Aid : " + data_getmember[0].isQualifiedInFirstAid.toString())
                            sessionManager.saveGUAEMRNAME(data_getmember[0].emergencyName.toString())
                            sessionManager.saveGUAEMRPHONE(data_getmember[0].emergencyPhone.toString())
                            sessionManager.saveGUAEMREMAIL(data_getmember[0].emergencyEmail.toString())
                            sessionManager.saveGUAEMRRELATIONSHIP(data_getmember[0].emergencyRelatioship.toString())
                            sessionManager.saveGUAEMRRELATIONSHIP_OTHER(data_getmember[0].otherEmergencyRelationship.toString())
                            sessionManager.saveDOHAVEMEDICAL(data_getmember[0].medicalInformationDeclare.toString())

                            sessionManager.saveAGE(data_getmember[0].memberAge.toString())
                            sessionManager.saveGENDER(data_getmember[0].gender.toString())
                            sessionManager.saveCITY(data_getmember[0].city.toString())
                            sessionManager.saveCOUNTRY(data_getmember[0].country.toString())
                            sessionManager.savePOSTCODE(data_getmember[0].postalCode.toString())
                            sessionManager.saveSHAKHA_TAB(data_getmember[0].shakha_tab.toString())
                            var s_count  = data_getmember[0].shakha_sankhya_avg.toString()
                            if(s_count.length ==0){
                                s_count = "0"
                            }
                            sessionManager.saveSHAKHA_SANKHYA_AVG(s_count)
                            sessionManager.saveMEDICAL_OTHER_INFO(data_getmember[0].medicalDetails.toString())
                            sessionManager.saveQUALIFICATIONAID(data_getmember[0].isQualifiedInFirstAid.toString())
                            sessionManager.saveQUALIFICATION_VALUE(data_getmember[0].first_aid_qualification_val.toString())
                            sessionManager.saveQUALIFICATION_VALUE_NAME(data_getmember[0].first_aid_qualification_name.toString())// value name
                            sessionManager.saveQUALIFICATION_IS_DOC(data_getmember[0].first_aid_qualification_is_doc.toString())
                            sessionManager.saveQUALIFICATION_DATE(data_getmember[0].dateOfFirstAidQualification.toString())
                            sessionManager.saveQUALIFICATION_FILE(data_getmember[0].firstAidQualificationFile.toString())
                            sessionManager.saveQUALIFICATION_PRO_BODY_RED_NO(data_getmember[0].professional_body_registartion_number.toString())
                            sessionManager.saveDIETARY(data_getmember[0].specialMedDietryInfo.toString())
                            sessionManager.saveDIETARYID(data_getmember[0].special_med_dietry_info_id.toString())
                            sessionManager.saveSTATE_IN_INDIA(data_getmember[0].indianConnectionState.toString())

                            Log.d("Address", sessionManager.fetchADDRESS()!!)
                            Log.d("Username", sessionManager.fetchUSERNAME()!!)
                            Log.d("Shakha_tab", sessionManager.fetchSHAKHA_TAB()!!)

                            if (sessionManager.fetchSHAKHA_TAB() == "yes") {
                                updateAdapter(0)
                                // Set 'Home' as the default fragment when the app starts
                                val dashboardFragment = DashboardFragment()
                                supportFragmentManager.beginTransaction()
                                    .replace(R.id.activity_main_content_id, dashboardFragment)
                                    .commit()
                            } else {
                                updateAdapter(0)
                                val dashboardFragment = DashboardFragment()
                                supportFragmentManager.beginTransaction()
                                    .replace(R.id.activity_main_content_id, dashboardFragment)
                                    .commit()
                            }

                        } catch (e: ArithmeticException) {
                            println(e)
                        } finally {
                            println("Profile")
                        }

                    } else {
                        Functions.displayMessage(this@HomeActivity, response.body()?.message)
//                Functions.showAlertMessageWithOK(
//                    this@HomeActivity, "",
////                        "Message",
//                    response.body()?.message
//                )
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@HomeActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Get_Profile_Response>, t: Throwable) {
                Toast.makeText(this@HomeActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
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
                    TAG, "All location settings are satisfied."
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
                            this@HomeActivity, 1
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

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_REQUEST_CODE
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            // Checking for fragment count on back stack
            if (supportFragmentManager.backStackEntryCount > 0) {
                // Go to the previous fragment
                supportFragmentManager.popBackStack()
            } else {
                // Exit the app
//            super.onBackPressed()
                val alertBuilder = AlertDialog.Builder(this@HomeActivity)
                alertBuilder.setTitle(getString(R.string.app_name))
                alertBuilder.setMessage("Are you sure you want to quit " + this.getString(R.string.app_name) + " application?")
                alertBuilder.setPositiveButton(
                    "Yes"
                ) { dialog, which -> finishAffinity() }
                alertBuilder.setNegativeButton(
                    "No"
                ) { dialog, which ->
                    dialog?.dismiss()
                }
                val alertDialog = alertBuilder.create()
                alertDialog.show()
            }
        }
    }

    private fun appInstalledOrNot(uri: String): Boolean {
        val pm = packageManager
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onRestart() {
        super.onRestart()
        displayLocationSettingsRequest(this)
    }
}