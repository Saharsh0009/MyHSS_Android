package com.myhss.ui.Biomeric

//import `in`.arjsna.passcodeview.PassCodeView
import android.Manifest
import android.animation.ObjectAnimator
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.myhss.Fingerprint.BiometricCallback
import com.myhss.Fingerprint.BiometricManager.BiometricBuilder
import com.myhss.Fingerprint.BiometricUtils
import com.myhss.Fingerprint.FingerPrintPopUp
import com.myhss.Splash.Model.Biometric.Biometric_response
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.DebugLog
import com.myhss.Utils.Functions
import com.samsung.android.sdk.SsdkUnsupportedException
import com.samsung.android.sdk.SsdkVendorCheck
import com.samsung.android.sdk.pass.Spass
import com.uk.myhss.Login_Registration.LoginActivity
import com.uk.myhss.Main.HomeActivity
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.regex.Pattern

class ChangeBiomerticFragment : Fragment(), View.OnClickListener, BiometricCallback {

    private lateinit var sessionManager: SessionManager
    lateinit var sharedPreferences: SharedPreferences

    //    val passCodeView: PassCodeView? = null
    var isRePass = false
    private var isNew: Boolean = false

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
    private var imgPass2: android.widget.ImageView? = null
    private var imgPass3: android.widget.ImageView? = null
    private var imgPass4: android.widget.ImageView? = null
    private var imgBackDelete: android.widget.ImageView? = null
    var mainLayout: LinearLayout? = null
    private var passcode_img_btn: LinearLayout? = null
    var strPasslock = ""
    private var strRePasslock: kotlin.String? = ""
    private var passcode: kotlin.String? = null
    private var f_name: kotlin.String? = ""
    private var l_name: kotlin.String? = ""
    private var user_id: kotlin.String? = ""
    private var SETTING: kotlin.String? = "logout"
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

    //    val sessionManager: SessionManager? = null
    val sh: SharedPreferences? = null
    val sh1: SharedPreferences.Editor? = null
    var page = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.act_passcode, container, false)
        sessionManager = SessionManager(requireContext())

        Functions.adjustFontScale(requireContext(), resources.configuration)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = Firebase.analytics

        sessionManager.firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
            param(FirebaseAnalytics.Param.ITEM_ID, "PasscodeVC")
            param(FirebaseAnalytics.Param.ITEM_NAME, "PasscodeVC")
        }

        sharedPreferences =
            requireActivity().getSharedPreferences("production", Context.MODE_PRIVATE)

        Log.d("member", sharedPreferences.getString("MEMBERID", "").toString())

        btnOne = root.findViewById(R.id.btnOne) as TextView
        btnTwo = root.findViewById(R.id.btnTwo) as TextView
        btnThree = root.findViewById(R.id.btnThree) as TextView
        btnFour = root.findViewById(R.id.btnFour) as TextView
        btnFive = root.findViewById(R.id.btnFive) as TextView
        btnSix = root.findViewById(R.id.btnSix) as TextView
        btnSeven = root.findViewById(R.id.btnSeven) as TextView
        btnEight = root.findViewById(R.id.btnEight) as TextView
        btnNine = root.findViewById(R.id.btnNine) as TextView
        btnZero = root.findViewById(R.id.btnZero) as TextView
        btnDalet = root.findViewById(R.id.btnDalet) as RelativeLayout?
        mainLayout = root.findViewById(R.id.mainLayout) as LinearLayout?
        passcode_img_btn = root.findViewById(R.id.passcode_img_btn) as LinearLayout
        val passcode_border_logo_view =
            root.findViewById(R.id.passcode_border_logo_view) as LinearLayout
        txtEnterPasscode = root.findViewById(R.id.txtEnterPasscode) as TextView?
        txtWrongPasscode = root.findViewById(R.id.txtWrongPwd) as TextView
        txtusername = root.findViewById(R.id.txtusername) as TextView
        passcode_logout = root.findViewById(R.id.passcode_logout) as TextView
        val actiontitle = root.findViewById(R.id.header_title) as TextView
        val backarrow = root.findViewById(R.id.back_arrow) as ImageView

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

        passcode_border_logo_view.visibility = View.GONE
        backarrow.visibility = View.INVISIBLE
        actiontitle.text = "Passcode"
        if (isNew) {
            txtEnterPasscode!!.text =
                "Please create a pin that will be your login details for the MyHSS App. Once verified, that will be your pin."//"Set Passcode"
        } else {
            txtEnterPasscode!!.text =
                    //"Please create a pin that will be your login details for the MyHSS App. Once verified, that will be your pin."
                "Enter Passcode"
            passcode_logout!!.setVisibility(View.VISIBLE)
        }
        tv_forgot_password = root.findViewById(R.id.tv_forgot_password) as TextView
        tv_forgot_password!!.setOnClickListener(DebouncedClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    LoginActivity::class.java
                )
            )
        })

        imgPass1 = root.findViewById(R.id.img1) as ImageView?
        imgPass2 = root.findViewById(R.id.img2) as ImageView
        imgPass3 = root.findViewById(R.id.img3) as ImageView
        imgPass4 = root.findViewById(R.id.img4) as ImageView
        imgBackDelete = root.findViewById(R.id.imgBackDelete) as ImageView

        passcode_logout!!.visibility = View.GONE
        setListener()
        adjustPassCodeScreen()

        Log.d("FIRSTNAME", sharedPreferences.getString("FIRSTNAME", "").toString())
        if (sharedPreferences.getString("USERID", "") != "") {

            passcode = ""

            f_name = sharedPreferences.getString("FIRSTNAME", "")
            l_name = sharedPreferences.getString("SURNAME", "")

            if (passcode == null || passcode == " " || passcode == "" || passcode == "null") {
                isNew = true
                Functions.printLog("isNew", "IF---$isNew")

                txtusername!!.visibility = View.VISIBLE
                val FUsername: String =
                    (sharedPreferences.getString("FIRSTNAME", "")
                        .toString()).capitalize(Locale.ROOT)
                val LUsername: String =
                    (sharedPreferences.getString("SURNAME", "").toString()).capitalize(Locale.ROOT)
                val othertext: String = getColoredSpanned("Welcome \n", "#142F4D")!!
                txtusername!!.text = Html.fromHtml("$othertext <b>$FUsername $LUsername</b>")

                if (Functions.isConnectingToInternet(requireActivity())) { //A
                    passcode(
                        sharedPreferences.getString("USERID", "")!!,
                        passcode.toString(),
                        "false"
                    )
                } else {
                    Toast.makeText(
                        requireActivity(),
                        resources.getString(R.string.no_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {
                isNew = false
                Functions.printLog("isNew", "ELSE---$isNew")

                // Check if we're running on Android 6.0 (M) or higher
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    //Fingerprint API only available on from Android 6.0 (M)
                    val fingerprintManager =
                        requireActivity().getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
                    if (!fingerprintManager.isHardwareDetected) {
                        // Device doesn't support fingerprint authentication
                    } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                        if (BiometricUtils.isSdkVersionSupported()) fingerPrintLock()
                    } else {
                        if (BiometricUtils.isSdkVersionSupported()) fingerPrintLock()
                    }
                }
                txtusername!!.visibility = View.VISIBLE
                val FUsername: String =
                    (sharedPreferences.getString("FIRSTNAME", "")
                        .toString()).capitalize(Locale.ROOT)
                val LUsername: String =
                    (sharedPreferences.getString("SURNAME", "").toString()).capitalize(Locale.ROOT)
                val othertext: String = getColoredSpanned("Last logged in as \n", "#142F4D")!!
                txtusername!!.text = Html.fromHtml("$othertext <b>$FUsername $LUsername</b>")
            }
        }
        return root
    }

    fun getColoredSpanned(text: String, color: String): String? {
        return "<font color=$color>$text</font>"
    }

    fun capitalize(capString: String): String? {
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
//        passcode_logout!!.setOnClickListener(this)
    }

    fun adjustPassCodeScreen() {
        val metrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(metrics)
        val widthPixels = metrics.widthPixels
        val heightPixels = metrics.heightPixels
        val widthDpi = metrics.xdpi
        val heightDpi = metrics.ydpi
        val widthInches = widthPixels / widthDpi
        val heightInches = heightPixels / heightDpi
        diagonalInches =
            Math.sqrt((widthInches * widthInches + heightInches * heightInches).toDouble())
        txtEnterPasscode!!.textSize = 12F
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
            if (isNew) {
                if (!isRePass) {
                    if(strPasslock == sharedPreferences.getString("SECURITYKEY", "")){
                        performWrongPinAnimation("Old passcode and new passcode cannot be same.\nPlease try again.")
                    }else{
                        isRePass = true
                        strRePasslock = strPasslock
                        strPasslock = ""
                        txtEnterPasscode!!.text = "Verify Passcode"
                        setPointView()
                    }
                } else {
                    if (strPasslock == strRePasslock) {
                        if (Functions.isConnectingToInternet(requireContext())) { // b
                            passcode(
                                sharedPreferences.getString("USERID", "")!!,
                                strPasslock,
                                "true"
                            )
                        } else {
                            Toast.makeText(
                                requireContext(),
                                resources.getString(R.string.no_connection),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        performWrongPinAnimation("Passcode do not match.\nPlease try again.")
                    }
                }
            } else {
                if (strPasslock == passcode) {
                    startActivity(Intent(requireContext(), HomeActivity::class.java))
                    (context as Activity).finish()
                    Functions.printLog("Here is", "2")
                    (context as Activity).finish()
                } else {
                    txtWrongPasscode!!.visibility = View.VISIBLE
                    txtWrongPasscode!!.text = "Wrong Passcode"
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
            }
        }
    }

    private fun performWrongPinAnimation(stringMsg : String) {
        isRePass = false
        txtWrongPasscode!!.setVisibility(View.VISIBLE)
        txtWrongPasscode!!.setText(stringMsg)
        txtEnterPasscode!!.text = "Set Passcode"
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
        ).setDuration(500).start()
        strPasslock = ""
        setPointView()
    }

    fun passcode(user_id: String, biometric_key: String, biometric_key_update: String) {
        val pd = CustomProgressBar(requireContext())
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
                    if (response.body()!!.status == true) {
                        sharedPreferences.edit().apply {
                            putString("SECURITYKEY", response.body()!!.data!!.biometricKey)
                        }

                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putString("SECURITYKEY", response.body()!!.data!!.biometricKey)
                        editor.apply()
                        editor.commit()


                        if (strPasslock == response.body()!!.data!!.biometricKey) {
                            startActivity(
                                Intent(
                                    requireContext(),
                                    HomeActivity::class.java
                                )
                            )
                            (context as Activity).finish()
                        } else {
                            Functions.showAlertMessageWithOK(
                                requireContext(),
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
                            requireContext(), "",
//                        "Message",
                            response.body()!!.message
                        )
                        setPointView()
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        requireContext(), "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Biometric_response>, t: Throwable) {
//                Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
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
    fun fingerPrintLock() {
        if (BiometricUtils.isBiometricPromptEnabled()) {
            if (BiometricUtils.isHardwareSupported(requireContext()) &&
                BiometricUtils.isFingerprintAvailable(requireContext()) &&
                BiometricUtils.isPermissionGranted(requireContext())
            ) {
                val finger = Intent(requireContext(), FingerPrintPopUp::class.java)
                startActivity(finger)
            }
        } else if (SsdkVendorCheck.isSamsungDevice()) {
            var isFingerprintSupported = false
            val spass = Spass()
            try {
                spass.initialize(requireContext())
                isFingerprintSupported = spass.isFeatureEnabled(Spass.DEVICE_FINGERPRINT)
            } catch (e: SsdkUnsupportedException) {
                // Error handling
            } catch (e: UnsupportedOperationException) {
            }
            if (isFingerprintSupported) {
                val finger = Intent(requireContext(), FingerPrintPopUp::class.java)
                startActivity(finger)
            }
        } else if (BiometricUtils.isHardwareSupported(requireContext()) &&
            BiometricUtils.isFingerprintAvailable(requireContext()) &&
            BiometricUtils.isPermissionGranted(requireContext())
        ) {
            if (BiometricUtils.isBiometricPromptEnabled()) {
                val finger = Intent(requireContext(), FingerPrintPopUp::class.java)
                startActivity(finger)
            } else if (requireActivity().getSystemService(Context.KEYGUARD_SERVICE) != null ||
                (requireActivity().getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager).isKeyguardSecure
            ) {
                val finger = Intent(requireContext(), FingerPrintPopUp::class.java)
                startActivity(finger)
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.P)
    fun displayBiometricPrompt() {
        BiometricBuilder(requireContext())
            .setTitle("Fingerprint Login for MyHss")
            .setSubtitle("Touch to log in")
            .setDescription("")
            .setNegativeButtonText("USE PASSCODE")
            .build()
            .authenticate(this)
    }

    override fun onSdkVersionNotSupported() {
        Toast.makeText(
            requireContext(),
            "Device does not support finger print authentication",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onBiometricAuthenticationNotSupported() {
        Toast.makeText(
            requireContext(),
            "Device does not support finger print authentication",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onBiometricAuthenticationNotAvailable() {
        Toast.makeText(
            requireContext(),
            "Fingerprint is not registered in device",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onBiometricAuthenticationPermissionNotGranted() {
        Toast.makeText(
            requireContext(),
            "Permission is not granted by user",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onBiometricAuthenticationInternalError(error: String?) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
    }

    override fun onAuthenticationFailed() {}

    override fun onAuthenticationCancelled() {}

    override fun onAuthenticationSuccessful() {
        startActivity(Intent(requireContext(), HomeActivity::class.java))
        (context as Activity).finish()
    }

    override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {}

    override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {}

//    fun checkPermission(): Boolean {
//        val result_read = ContextCompat.checkSelfPermission(
//            requireContext(),
//            Manifest.permission.READ_EXTERNAL_STORAGE
//        )
//        val result_write = ContextCompat.checkSelfPermission(
//            requireContext(),
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//        )
//        return if (result_write == PackageManager.PERMISSION_GRANTED && result_read == PackageManager.PERMISSION_GRANTED) {
//            true
//        } else {
//            false
//        }
//    }

//    fun requestPermission() {
//        ActivityCompat.requestPermissions(
//            requireContext() as Activity,
//            arrayOf(
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//            ),
//            PERMISSION_REQUEST_CODE
//        )
//    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnOne -> if (strPasslock.length < 4) {
                strPasslock = strPasslock + btnOne!!.text.toString()
                setPointView()
            }

            R.id.btnTwo -> if (strPasslock.length < 4) {
                strPasslock = strPasslock + btnTwo!!.getText().toString()
                setPointView()
            }

            R.id.btnThree -> if (strPasslock.length < 4) {
                strPasslock = strPasslock + btnThree!!.getText().toString()
                setPointView()
            }

            R.id.btnFour -> if (strPasslock.length < 4) {
                strPasslock = strPasslock + btnFour!!.getText().toString()
                setPointView()
            }

            R.id.btnFive -> if (strPasslock.length < 4) {
                strPasslock = strPasslock + btnFive!!.getText().toString()
                setPointView()
            }

            R.id.btnSix -> if (strPasslock.length < 4) {
                strPasslock = strPasslock + btnSix!!.getText().toString()
                setPointView()
            }

            R.id.btnSeven -> if (strPasslock.length < 4) {
                strPasslock = strPasslock + btnSeven!!.getText().toString()
                setPointView()
            }

            R.id.btnEight -> if (strPasslock.length < 4) {
                strPasslock = strPasslock + btnEight!!.getText().toString()
                setPointView()
            }

            R.id.btnNine -> if (strPasslock.length < 4) {
                strPasslock = strPasslock + btnNine!!.getText().toString()
                setPointView()
            }

            R.id.btnZero -> if (strPasslock.length < 4) {
                strPasslock = strPasslock + btnZero!!.getText().toString()
                setPointView()
            }

            R.id.btnDalet -> if (strPasslock.length > 0) {
                strPasslock = strPasslock.substring(0, strPasslock.length - 1)
                setPointView()
            }

            R.id.imgBackDelete -> if (strPasslock.length > 0) {
                strPasslock = strPasslock.substring(0, strPasslock.length - 1)
                setPointView()
            }
        }
    }

}