package com.myhss.Login_Registration

import `in`.arjsna.passcodeview.PassCodeView
import android.Manifest
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Dialog
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.DisplayMetrics
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.google.gson.JsonObject
import com.myhss.Fingerprint.BiometricCallback
import com.myhss.Fingerprint.BiometricManager.BiometricBuilder
import com.myhss.Fingerprint.BiometricUtils
import com.myhss.Fingerprint.FingerPrintPopUp
import com.myhss.Splash.Model.Biometric.Biometric_response
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.Functions
import com.myhss.Utils.SharedPreferences_String_Name
import com.samsung.android.sdk.SsdkUnsupportedException
import com.samsung.android.sdk.SsdkVendorCheck
import com.samsung.android.sdk.pass.Spass
import com.uk.myhss.Login_Registration.LoginActivity
import com.uk.myhss.Main.HomeActivity
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.Welcome.WelcomeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class PasscodeActivity : AppCompatActivity(), View.OnClickListener, BiometricCallback {

    lateinit var sharedPreferences: SharedPreferences
    val TAG = PasscodeActivity::class.java.name
    val passCodeView: PassCodeView? = null
    var isRePass = false
    private var isNew: Boolean = false
    val onetxt: TextView? = null

    private var btnOne: TextView? = null
    private var btnTwo: TextView? = null
    private var btnThree: TextView? = null
    private var btnFour: TextView? = null
    private var btnFive: TextView? = null
    private var btnSix: TextView? = null
    private var btnSeven: TextView? = null
    private var btnEight: TextView? = null
    private var btnNine: TextView? = null
    private var btnZero: TextView? = null
    var txtEnterPasscode: TextView? = null
    private var txtusername: TextView? = null
    private var txtWrongPasscode: TextView? = null
    private var tv_forgot_password: TextView? = null
    private var passcode_logout: TextView? = null
    var imgPass1: ImageView? = null
    private var imgPass2: ImageView? = null
    private var imgPass3: ImageView? = null
    private var imgPass4: ImageView? = null
    private var imgBackDelete: ImageView? = null
    var mainLayout: LinearLayout? = null
    private var passcode_img_btn: LinearLayout? = null
    var strPasslock = ""
    private var strRePasslock: String? = ""
    private var passcode: String? = null
    private var f_name: String? = ""
    private var l_name: String? = ""
    private var user_id: String? = ""
    private var SETTING: String? = "logout"
    val diagonal = 0.0
    val realwidth = 0
    private var realheight: Int = 0
    var btnDalet: RelativeLayout? = null
    var diagonalInches = 0.0
    val dialog: Dialog? = null
    val isAvailable = false

    val isacceptchk = false
    val passcodeActivity: AppCompatActivity? = null
    val PERMISSION_REQUEST_CODE = 1

    val sh: SharedPreferences? = null
    val sh1: SharedPreferences.Editor? = null
    var page = ""

    private lateinit var sessionManager: SessionManager

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_passcode)
        Functions.adjustFontScale(getApplication(), getResources().getConfiguration())

        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = Firebase.analytics

        sessionManager.firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
            param(FirebaseAnalytics.Param.ITEM_ID, "PasscodeVC")
            param(FirebaseAnalytics.Param.ITEM_NAME, "PasscodeVC")
//            param(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
        }

        sharedPreferences = getSharedPreferences("production", Context.MODE_PRIVATE)

        Log.d("member", sharedPreferences.getString("MEMBERID", "").toString())

        init()

        adjustPassCodeScreen()

//        sh1.putString(SharedPreferences_String_Name.DEVICE_ID, getIntent().getStringExtra("m_deviceId"));
//        sh1.putString(SharedPreferences_String_Name.DEVICE_FCM, getIntent().getStringExtra("fcm_id"));
//        sh1.commit();
//        if (sessionManager!!.isLoggedIn) {

//        Log.d("SECURITYKEY", sharedPreferences.getString("SECURITYKEY", "").toString())
        Log.d("FIRSTNAME", sharedPreferences.getString("FIRSTNAME", "").toString())
        if (sharedPreferences.getString("USERID", "") != "") {
            passcode = sharedPreferences.getString("SECURITYKEY", "")

            f_name = sharedPreferences.getString("FIRSTNAME", "")
            l_name = sharedPreferences.getString("SURNAME", "")
            //            user_id = sessionManager.getUserDetails().getUserId();
//            Functions.printLog("Passcode", f_name + "-->> " + passcode);
            if (passcode == null || passcode == " " || passcode == "" || passcode == "null") {
                isNew = true
                Functions.printLog("isNew", "IF---$isNew")
                //                if (BiometricUtils.isSdkVersionSupported())
//                    fingerPrintLock();
//                faceOpenLock();
                txtusername!!.visibility = View.VISIBLE
                val FUsername: String =
                    capitalize(sharedPreferences.getString("FIRSTNAME", "").toString())!!
                val LUsername: String =
                    capitalize(sharedPreferences.getString("SURNAME", "").toString())!!
                val othertext: String = getColoredSpanned("Welcome \n", "#142F4D")!!
                txtusername!!.text = Html.fromHtml("$othertext <b>$FUsername $LUsername</b>")

                if (Functions.isConnectingToInternet(this@PasscodeActivity)) {
                    passcode(
                        sharedPreferences.getString("USERID", "")!!,
                        passcode.toString(),
                        "false"
                    )
                } else {
                    Toast.makeText(
                        this@PasscodeActivity,
                        resources.getString(R.string.no_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {
                isNew = false
                Functions.printLog("isNew", "ELSE---$isNew")
                //                Log.e("FACE", sessionManager.getDEVICE_FACEID());
//                Log.e("FINGER", sessionManager.getDEVICE_FINGERID());

                // Check if we're running on Android 6.0 (M) or higher
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    //Fingerprint API only available on from Android 6.0 (M)
                    val fingerprintManager =
                        this.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
                    if (!fingerprintManager.isHardwareDetected) {
                        // Device doesn't support fingerprint authentication
                    } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                        // User hasn't enrolled any fingerprints to authenticate with
//                        if (sessionManager.getDEVICE_FACEID().equals("1", ignoreCase = true)) {
//                            if (BiometricUtils.isSdkVersionSupported()) faceOpenLock()
//                        } else
//                            if (sessionManager!!.getDEVICE_FINGERID()
//                                .equals("1", ignoreCase = true)
//                        ) {
                        if (BiometricUtils.isSdkVersionSupported()) fingerPrintLock()
//                        }
                    } else {
                        // Everything is ready for fingerprint authentication
//                        if (sessionManager.getDEVICE_FACEID().equals("1", ignoreCase = true)) {
//                            if (BiometricUtils.isSdkVersionSupported()) faceOpenLock()
//                        } else
//                            if (sessionManager!!.getDEVICE_FINGERID()
//                                .equals("1", ignoreCase = true)
//                        ) {
                        if (BiometricUtils.isSdkVersionSupported()) fingerPrintLock()
//                        }
                    }
                }
                txtusername!!.visibility = View.VISIBLE
                val FUsername: String =
                    capitalize(sharedPreferences.getString("FIRSTNAME", "").toString())!!
                val LUsername: String =
                    capitalize(sharedPreferences.getString("SURNAME", "").toString())!!
                val othertext: String = getColoredSpanned("Last logged in as \n", "#142F4D")!!
                txtusername!!.text = Html.fromHtml("$othertext <b>$FUsername $LUsername</b>")
            }
//        }
        }
    }

    open fun init() {
//        if (getIntent().hasExtra("page")) {
//            page = getIntent().getStringExtra("page").toString()
//        }
        btnOne = findViewById<View>(R.id.btnOne) as TextView
        btnTwo = findViewById<View>(R.id.btnTwo) as TextView
        btnThree = findViewById<View>(R.id.btnThree) as TextView
        btnFour = findViewById<View>(R.id.btnFour) as TextView
        btnFive = findViewById<View>(R.id.btnFive) as TextView
        btnSix = findViewById<View>(R.id.btnSix) as TextView
        btnSeven = findViewById<View>(R.id.btnSeven) as TextView
        btnEight = findViewById<View>(R.id.btnEight) as TextView
        btnNine = findViewById<View>(R.id.btnNine) as TextView
        btnZero = findViewById<View>(R.id.btnZero) as TextView
        btnDalet = findViewById<View>(R.id.btnDalet) as RelativeLayout?
        mainLayout = findViewById<View>(R.id.mainLayout) as LinearLayout?
        passcode_img_btn = findViewById<View>(R.id.passcode_img_btn) as LinearLayout
        txtEnterPasscode = findViewById<View>(R.id.txtEnterPasscode) as TextView?
        txtWrongPasscode = findViewById<View>(R.id.txtWrongPwd) as TextView
        txtusername = findViewById<View>(R.id.txtusername) as TextView
        passcode_logout = findViewById<View>(R.id.passcode_logout) as TextView
        val actiontitle = findViewById<View>(R.id.header_title) as TextView
        val backarrow = findViewById<View>(R.id.back_arrow) as ImageView

        btnOne!!.setBackgroundResource(R.drawable.empty_dot)
        btnTwo!!.setBackgroundResource(R.drawable.empty_dot)
        btnThree!!.setBackgroundResource(R.drawable.empty_dot)
        btnFour!!.setBackgroundResource(R.drawable.empty_dot)
        btnFive!!.setBackgroundResource(R.drawable.empty_dot)
        btnSix!!.setBackgroundResource(R.drawable.empty_dot)
        btnSeven!!.setBackgroundResource(R.drawable.empty_dot)
        btnEight!!.setBackgroundResource(R.drawable.empty_dot)
        btnNine!!.setBackgroundResource(R.drawable.empty_dot)
        btnZero!!.setBackgroundResource(R.drawable.empty_dot)

        backarrow.visibility = View.INVISIBLE
        actiontitle.text = "Passcode"
        if (isNew) {
            txtEnterPasscode!!.text =
                "Please create a pin that will be your login details for the MyHSS App. Once verified, that will be your pin."//"Set Passcode"
        } else {
            txtEnterPasscode!!.text = "Enter Passcode"
            passcode_logout!!.setVisibility(View.VISIBLE)
        }
        tv_forgot_password = findViewById<View>(R.id.tv_forgot_password) as TextView
        tv_forgot_password!!.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(
                Intent(
                    this@PasscodeActivity,
                    LoginActivity::class.java
                )
            )
        })
        imgPass1 = findViewById<View>(R.id.img1) as ImageView?
        imgPass2 = findViewById<View>(R.id.img2) as ImageView
        imgPass3 = findViewById<View>(R.id.img3) as ImageView
        imgPass4 = findViewById<View>(R.id.img4) as ImageView
        imgBackDelete = findViewById<View>(R.id.imgBackDelete) as ImageView
        setListener()
    }

    open fun getColoredSpanned(text: String, color: String): String? {
        return "<font color=$color>$text</font>"
    }

    open fun capitalize(capString: String): String? {
        val capBuffer = StringBuffer()
        val capMatcher =
            Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString)
        while (capMatcher.find()) {
            capMatcher.appendReplacement(
                capBuffer,
                capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase()
            )
        }
        return capMatcher.appendTail(capBuffer).toString()
    }

    fun setListener() {
        btnOne!!.setOnClickListener(this)
        btnTwo!!.setOnClickListener(this)
        btnThree!!.setOnClickListener(this)
        btnFour!!.setOnClickListener(this)
        btnFive!!.setOnClickListener(this)
        btnSix!!.setOnClickListener(this)
        btnSeven!!.setOnClickListener(this)
        btnEight!!.setOnClickListener(this)
        btnNine!!.setOnClickListener(this)
        btnZero!!.setOnClickListener(this)
        btnDalet!!.setOnClickListener(this)
        imgBackDelete!!.setOnClickListener(this)
        passcode_logout!!.setOnClickListener(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (checkPermission()) {
            } else {
                requestPermission()
            }
        }
    }

    fun adjustPassCodeScreen() {
        val metrics = DisplayMetrics()
        getWindowManager().getDefaultDisplay().getMetrics(metrics)
        val widthPixels = metrics.widthPixels
        val heightPixels = metrics.heightPixels
        val widthDpi = metrics.xdpi
        val heightDpi = metrics.ydpi
        val widthInches = widthPixels / widthDpi
        val heightInches = heightPixels / heightDpi
        diagonalInches =
            Math.sqrt((widthInches * widthInches + heightInches * heightInches).toDouble())
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // setFinishOnTouchOutside(false);
            if (dialog != null && dialog.isShowing) {
                dialog.setCanceledOnTouchOutside(false)
                dialog.setCancelable(false)
                dialog.setOnKeyListener(null)
            }
        }
        return false
    }

    fun setPointView() {
        imgPass1!!.setImageResource(R.drawable.empty_dot)
        imgPass2!!.setImageResource(R.drawable.empty_dot)
        imgPass3!!.setImageResource(R.drawable.empty_dot)
        imgPass4!!.setImageResource(R.drawable.empty_dot)
        for (i in 0 until strPasslock.length) {
            if (i == 0) {
                imgPass1!!.setImageResource(R.drawable.passcode_blue)
            } else if (i == 1) {
                imgPass2!!.setImageResource(R.drawable.passcode_blue)
            } else if (i == 2) {
                imgPass3!!.setImageResource(R.drawable.passcode_blue)
            } else if (i == 3) {
                imgPass4!!.setImageResource(R.drawable.passcode_blue)
            }
        }
        if (strPasslock.length == 1) {
            txtWrongPasscode!!.visibility = View.INVISIBLE
        }
        if (strPasslock.length == 4) {
//            Functions.printLog("TAG", isNew + "");

            if (Functions.isConnectingToInternet(this@PasscodeActivity)) {
                passcode(
                    sharedPreferences.getString("USERID", "")!!,
                    strPasslock,
                    "false"
                )
            } else {
                Toast.makeText(
                    this@PasscodeActivity,
                    resources.getString(R.string.no_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    open fun passcode(user_id: String, biometric_key: String, biometric_key_update: String) {
        val pd = CustomProgressBar(this@PasscodeActivity)
        pd.show()
        val call: Call<Biometric_response> =
            MyHssApplication.instance!!.api.biometric_key(
//                token = "Bearer ${sessionManager.fetchAuthToken()}",
                user_id,
                biometric_key,
                biometric_key_update
            )
        call.enqueue(object : Callback<Biometric_response> {
            override fun onResponse(
                call: Call<Biometric_response>,
                response: Response<Biometric_response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body()!!.status === true) {
                        sharedPreferences.edit().apply {
                            putString("SECURITYKEY", response.body()!!.data!!.biometricKey)
                        }

                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putString("SECURITYKEY", response.body()!!.data!!.biometricKey)
                        editor.apply()
                        editor.commit()


                        if (strPasslock == response.body()!!.data!!.biometricKey) {

                            if (sharedPreferences.getString("MEMBERID", "") != "") {
                                startActivity(
                                    Intent(
                                        this@PasscodeActivity,
                                        HomeActivity::class.java
                                    )
                                )
//                                MainActivity::class.java))
                                finish()
//                            } else {
//                                startActivity(
//                                    Intent(
//                                        this@PasscodeActivity,
//                                        WelcomeActivity::class.java
//                                    )
//                                )
//                                finish()
                            }
                        } else {
                            Functions.showAlertMessageWithOK(
                                this@PasscodeActivity,
                                "Passcode do not match.",
                                "\nPlease try again"
                            )
                            ObjectAnimator.ofFloat(
                                mainLayout,
                                "translationX",
                                0f,
                                25f,
                                -25f,
                                25f,
                                -25f,
                                15f,
                                -15f,
                                6f,
                                -6f,
                                0f
                            )
                                .setDuration(500).start()
                            strPasslock = ""
                            setPointView()
                        }
                    } else {
                        Functions.showAlertMessageWithOK(
                            this@PasscodeActivity, "",
//                        "Message",
                            response.body()!!.message
                        )
                        setPointView()
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@PasscodeActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Biometric_response>, t: Throwable) {
//                Toast.makeText(this@Passcode_Activity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (dialog != null && dialog.isShowing) {
            dialog.dismiss()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    open fun faceOpenLock() {
        if (BiometricUtils.isBiometricPromptEnabled()) {
            if (BiometricUtils.isHardwareSupported(this@PasscodeActivity) &&
                BiometricUtils.isFingerprintAvailable(this@PasscodeActivity) &&
                BiometricUtils.isPermissionGranted(this@PasscodeActivity)
            ) {
                displayBiometricPrompt()
            }
        } else if (SsdkVendorCheck.isSamsungDevice()) {
            var isFingerprintSupported = false
            val spass = Spass()
            try {
                spass.initialize(this@PasscodeActivity)
                isFingerprintSupported = spass.isFeatureEnabled(Spass.DEVICE_FINGERPRINT)
            } catch (e: SsdkUnsupportedException) {
                // Error handling
            } catch (e: UnsupportedOperationException) {
            }
            //            if (isFingerprintSupported) {
////                if (sh.getBoolean(SharedPreferences_String_Name.Finger_Print_Access, false))
//                if (sessionManager.getDEVICE_FACEID().equalsIgnoreCase("1")) {
////                    startActivity(new Intent(PasscodeActivity.this, FaceTrackerActivity.class));
//                    Intent finger = new Intent(PasscodeActivity.this, FaceTrackerActivity.class);
//                    finger.putExtra("Home", "Dashboard");
//                    finger.putExtra("m_deviceId", getIntent().getStringExtra("m_deviceId"));
//                    finger.putExtra("fcm_id", getIntent().getStringExtra("fcm_id"));
//                    startActivity(finger);
//                }
//            }
        } else if (BiometricUtils.isHardwareSupported(this@PasscodeActivity) &&
            BiometricUtils.isFingerprintAvailable(this@PasscodeActivity) &&
            BiometricUtils.isPermissionGranted(this@PasscodeActivity)
        ) {
            if (BiometricUtils.isBiometricPromptEnabled()) {
                displayBiometricPrompt()
                //            } else if (getSystemService(KEYGUARD_SERVICE) != null ||
//                    ((KeyguardManager) getSystemService(KEYGUARD_SERVICE)).isKeyguardSecure()) {
////                if (sh.getBoolean(SharedPreferences_String_Name.Finger_Print_Access, false))
//                if (sessionManager.getDEVICE_FACEID().equalsIgnoreCase("1")) {
////                    startActivity(new Intent(PasscodeActivity.this, FaceTrackerActivity.class)); // FaceDetectPopUp
//                    Intent finger = new Intent(PasscodeActivity.this, FaceTrackerActivity.class);
//                    finger.putExtra("Home", "Dashboard");
//                    finger.putExtra("m_deviceId", getIntent().getStringExtra("m_deviceId"));
//                    finger.putExtra("fcm_id", getIntent().getStringExtra("fcm_id"));
//                    startActivity(finger);
//                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    open fun fingerPrintLock() {
        if (BiometricUtils.isBiometricPromptEnabled()) {
            if (BiometricUtils.isHardwareSupported(this@PasscodeActivity) &&
                BiometricUtils.isFingerprintAvailable(this@PasscodeActivity) &&
                BiometricUtils.isPermissionGranted(this@PasscodeActivity)
            ) {
//                displayBiometricPrompt();
                val finger = Intent(this@PasscodeActivity, FingerPrintPopUp::class.java)
//                finger.putExtra("Home", "Dashboard")
//                finger.putExtra("m_deviceId", getIntent().getStringExtra("m_deviceId"))
//                finger.putExtra("fcm_id", getIntent().getStringExtra("fcm_id"))
                startActivity(finger)
            }
        } else if (SsdkVendorCheck.isSamsungDevice()) {
            var isFingerprintSupported = false
            val spass = Spass()
            try {
                spass.initialize(this@PasscodeActivity)
                isFingerprintSupported = spass.isFeatureEnabled(Spass.DEVICE_FINGERPRINT)
            } catch (e: SsdkUnsupportedException) {
                // Error handling
            } catch (e: UnsupportedOperationException) {
            }
            if (isFingerprintSupported) {
//                if (sessionManager!!.getDEVICE_FINGERID().equals("1", ignoreCase = true)) {
//                if (sh.getBoolean(SharedPreferences_String_Name.Finger_Print_Access, false))
//                    startActivity(new Intent(PasscodeActivity.this, FingerPrintPopUp.class));
                val finger = Intent(this@PasscodeActivity, FingerPrintPopUp::class.java)
//                    finger.putExtra("Home", "Dashboard")
//                    finger.putExtra("m_deviceId", getIntent().getStringExtra("m_deviceId"))
//                    finger.putExtra("fcm_id", getIntent().getStringExtra("fcm_id"))
                startActivity(finger)
//                }
            }
        } else if (BiometricUtils.isHardwareSupported(this@PasscodeActivity) &&
            BiometricUtils.isFingerprintAvailable(this@PasscodeActivity) &&
            BiometricUtils.isPermissionGranted(this@PasscodeActivity)
        ) {
            if (BiometricUtils.isBiometricPromptEnabled()) {
//                displayBiometricPrompt();
                val finger = Intent(this@PasscodeActivity, FingerPrintPopUp::class.java)
//                finger.putExtra("Home", "Dashboard")
//                finger.putExtra("m_deviceId", getIntent().getStringExtra("m_deviceId"))
//                finger.putExtra("fcm_id", getIntent().getStringExtra("fcm_id"))
                startActivity(finger)
            } else if (getSystemService(Context.KEYGUARD_SERVICE) != null ||
                (getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager).isKeyguardSecure
            ) {
//                if (sh.getBoolean(SharedPreferences_String_Name.Finger_Print_Access, false))
//                if (sessionManager!!.getDEVICE_FINGERID().equals("1", ignoreCase = true)) {
//                    startActivity(new Intent(PasscodeActivity.this, FingerPrintPopUp.class));
                val finger = Intent(this@PasscodeActivity, FingerPrintPopUp::class.java)
//                    finger.putExtra("Home", "Dashboard")
//                    finger.putExtra("m_deviceId", getIntent().getStringExtra("m_deviceId"))
//                    finger.putExtra("fcm_id", getIntent().getStringExtra("fcm_id"))
                startActivity(finger)
//                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.P)
    open fun displayBiometricPrompt() {
        BiometricBuilder(this@PasscodeActivity)
            .setTitle("Fingerprint Login for MyHss")
            .setSubtitle("Touch to log in")
            .setDescription("")
            .setNegativeButtonText("USE PASSCODE")
            .build()
            .authenticate(this@PasscodeActivity)
    }

    override fun onSdkVersionNotSupported() {
        Toast.makeText(
            getApplicationContext(),
            "Device does not support finger print authentication",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onBiometricAuthenticationNotSupported() {
        Toast.makeText(
            getApplicationContext(),
            "Device does not support finger print authentication",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onBiometricAuthenticationNotAvailable() {
        Toast.makeText(
            getApplicationContext(),
            "Fingerprint is not registered in device",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onBiometricAuthenticationPermissionNotGranted() {
        Toast.makeText(
            getApplicationContext(),
            "Permission is not granted by user",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onBiometricAuthenticationInternalError(error: String?) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show()
    }

    override fun onAuthenticationFailed() {}

    override fun onAuthenticationCancelled() {}

    override fun onAuthenticationSuccessful() {
//        if (Functions.inBackground == true) {
//            Functions.inBackground = false;
//        } else {
//        val i = Intent(this@Passcode_Activity, MainActivity::class.java)
        //        i.putExtra("Home", "Dashboard");
//        i.putExtra("m_deviceId", getIntent().getStringExtra("m_deviceId"));
//        i.putExtra("fcm_id", getIntent().getStringExtra("fcm_id"));
//        if (!page.isEmpty()) {
//            i.putExtra("page", page)
//        }
//        startActivity(i)
//        finish()

        if (sharedPreferences.getString("MEMBERID", "") != "") {
            startActivity(Intent(this@PasscodeActivity, HomeActivity::class.java))
//                MainActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this@PasscodeActivity, WelcomeActivity::class.java))
            finish()
        }
//        }
//        finish();
    }

    override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {}

    override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {}

    open fun checkPermission(): Boolean {
        val result_read = ContextCompat.checkSelfPermission(
            this@PasscodeActivity,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val result_write = ContextCompat.checkSelfPermission(
            this@PasscodeActivity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return if (result_write == PackageManager.PERMISSION_GRANTED && result_read == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            false
        }
    }

    open fun requestPermission() {
        ActivityCompat.requestPermissions(
            this@PasscodeActivity,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            PERMISSION_REQUEST_CODE
        )
    }

//    fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String?>?,
//        grantResults: IntArray
//    ) {
//        when (requestCode) {
//            PERMISSION_REQUEST_CODE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Toast.makeText(PasscodeActivity.this, "Permission Granted, Now you can access Dashboard.", Toast.LENGTH_LONG).show();
//            } else {
//                //  Toast.makeText(PasscodeActivity.this, "Permission Denied, You cannot access Dashboard.", Toast.LENGTH_LONG).show();
//            }
//        }
//    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnOne -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && !checkPermission()) {
                Functions.showAlertMessageWithOK(
                    this@PasscodeActivity, "",
                    "Permission allows us to access Dashboard. \nPlease allow in App Settings for additional functionality."
                )
            } else {
                if (strPasslock.length < 4) {
                    strPasslock = strPasslock + btnOne!!.text.toString()
                    setPointView()
                }
            }
            R.id.btnTwo -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && !checkPermission()) {
                Functions.showAlertMessageWithOK(
                    this@PasscodeActivity, "",
                    "Permission allows us to access Dashboard. \nPlease allow in App Settings for additional functionality."
                )
            } else {
                if (strPasslock.length < 4) {
                    strPasslock = strPasslock + btnTwo!!.getText().toString()
                    setPointView()
                }
            }
            R.id.btnThree -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && !checkPermission()) {
                Functions.showAlertMessageWithOK(
                    this@PasscodeActivity, "",
                    "Permission allows us to access Dashboard. \nPlease allow in App Settings for additional functionality."
                )
            } else {
                if (strPasslock.length < 4) {
                    strPasslock = strPasslock + btnThree!!.getText().toString()
                    setPointView()
                }
            }
            R.id.btnFour -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && !checkPermission()) {
                Functions.showAlertMessageWithOK(
                    this@PasscodeActivity, "",
                    "Permission allows us to access Dashboard. \nPlease allow in App Settings for additional functionality."
                )
            } else {
                if (strPasslock.length < 4) {
                    strPasslock = strPasslock + btnFour!!.getText().toString()
                    setPointView()
                }
            }
            R.id.btnFive -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && !checkPermission()) {
                Functions.showAlertMessageWithOK(
                    this@PasscodeActivity, "",
                    "Permission allows us to access Dashboard. \nPlease allow in App Settings for additional functionality."
                )
            } else {
                if (strPasslock.length < 4) {
                    strPasslock = strPasslock + btnFive!!.getText().toString()
                    setPointView()
                }
            }
            R.id.btnSix -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && !checkPermission()) {
                Functions.showAlertMessageWithOK(
                    this@PasscodeActivity, "",
                    "Permission allows us to access Dashboard. \nPlease allow in App Settings for additional functionality."
                )
            } else {
                if (strPasslock.length < 4) {
                    strPasslock = strPasslock + btnSix!!.getText().toString()
                    setPointView()
                }
            }
            R.id.btnSeven -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && !checkPermission()) {
                Functions.showAlertMessageWithOK(
                    this@PasscodeActivity, "",
                    "Permission allows us to access Dashboard. \nPlease allow in App Settings for additional functionality."
                )
            } else {
                if (strPasslock.length < 4) {
                    strPasslock = strPasslock + btnSeven!!.getText().toString()
                    setPointView()
                }
            }
            R.id.btnEight -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && !checkPermission()) {
                Functions.showAlertMessageWithOK(
                    this@PasscodeActivity, "",
                    "Permission allows us to access Dashboard. \nPlease allow in App Settings for additional functionality."
                )
            } else {
                if (strPasslock.length < 4) {
                    strPasslock = strPasslock + btnEight!!.getText().toString()
                    setPointView()
                }
            }
            R.id.btnNine -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && !checkPermission()) {
                Functions.showAlertMessageWithOK(
                    this@PasscodeActivity, "",
                    "Permission allows us to access Dashboard. \nPlease allow in App Settings for additional functionality."
                )
            } else {
                if (strPasslock.length < 4) {
                    strPasslock = strPasslock + btnNine!!.getText().toString()
                    setPointView()
                }
            }
            R.id.btnZero -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && !checkPermission()) {
                Functions.showAlertMessageWithOK(
                    this@PasscodeActivity, "",
                    "Permission allows us to access Dashboard. \nPlease allow in App Settings for additional functionality."
                )
            } else {
                if (strPasslock.length < 4) {
                    strPasslock = strPasslock + btnZero!!.getText().toString()
                    setPointView()
                }
            }
            R.id.btnDalet -> if (strPasslock.length > 0) {
                strPasslock = strPasslock.substring(0, strPasslock.length - 1)
                setPointView()
            }
            R.id.imgBackDelete -> if (strPasslock.length > 0) {
                strPasslock = strPasslock.substring(0, strPasslock.length - 1)
                setPointView()
            }
            R.id.passcode_logout -> {
                val alertDialog: AlertDialog.Builder =
                    AlertDialog.Builder(this@PasscodeActivity)
//            alertDialog.setTitle("Logout")
                alertDialog.setMessage("Are you sure you would like to logout?")
                alertDialog.setPositiveButton(
                    "yes"
                ) { _, _ ->

                    val sharedPreferences = this@PasscodeActivity.getSharedPreferences(
                        "production",
                        Context.MODE_PRIVATE
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
                    sessionManager.saveQUALIFICATION_FILE("")
                    sessionManager.saveQUALIFICATION_DATE("")
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
                    }.apply()

                    val i = Intent(this@PasscodeActivity, LoginActivity::class.java)
                    startActivity(i)
                    finishAffinity()
                }
                alertDialog.setNegativeButton(
                    "No"
                ) { _, _ ->
                    val i = Intent(this@PasscodeActivity, PasscodeActivity::class.java)
                    startActivity(i)
                    finishAffinity()
                }
                val alert: AlertDialog = alertDialog.create()
                alert.setCanceledOnTouchOutside(false)
                alert.show()
            }
            else -> {
            }
        }
        Functions.printLog("strpassCode", strPasslock)
    }

}