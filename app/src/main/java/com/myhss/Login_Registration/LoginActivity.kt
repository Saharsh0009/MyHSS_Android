package com.uk.myhss.Login_Registration

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.Login_Registration.ForgotPasswordDialog
import com.myhss.Login_Registration.Passcode_Activity
import com.myhss.Login_Registration.RegistrationActivity
import com.myhss.Login_Registration.iForgotPasswordDialog
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.DebugLog
import com.myhss.Utils.Functions
import com.myhss.Utils.UtilCommon
import com.myhss.Welcome.BiometricDialogV23
import com.uk.myhss.Login_Registration.Model.ForgotPasswordResponse
import com.uk.myhss.Login_Registration.Model.LoginResponse
import com.uk.myhss.Main.HomeActivity
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.Welcome.WelcomeActivity
import pro.devapp.biometric.BiometricCallback
import pro.devapp.biometric.BiometricManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity(), iForgotPasswordDialog {
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var sessionManager: SessionManager
    var m_deviceId: String = ""
    var device_type: String = "A"

    /*Facebook*/
    lateinit var callbackManager: CallbackManager
    private val EMAIL = "email"

    /*Gmail*/
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("LoginVC")
        sessionManager.firebaseAnalytics.setUserProperty("LoginVC", "LoginActivity")

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        sharedPreferences = getSharedPreferences("production", Context.MODE_PRIVATE)

        m_deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

        Log.d("m_deviceId", m_deviceId)

        val login_btn = findViewById<TextView>(R.id.login_btn)
        val edit_username = findViewById<TextInputEditText>(R.id.edit_username)
        val edit_password = findViewById<TextInputEditText>(R.id.edit_password)
        val registration_layout = findViewById<RelativeLayout>(R.id.registration_layout)
        val forgot_btn = findViewById<TextView>(R.id.forgot_btn)
        val rootLayout = findViewById<RelativeLayout>(R.id.rootLayout)
//        val gmail_login = findViewById<ImageView>(R.id.gmail_login)

        val loginButton = findViewById<LoginButton>(R.id.login_button)
        val til_userName = findViewById<TextInputLayout>(R.id.til_userName)
        val til_password = findViewById<TextInputLayout>(R.id.til_password)


        loginButton.setOnClickListener(DebouncedClickListener {
            loginButton.setReadPermissions(listOf(EMAIL))
            callbackManager = CallbackManager.Factory.create()
            // If you are using in a fragment, call loginButton.setFragment(this);
            // Callback registration
            // If you are using in a fragment, call loginButton.setFragment(this);
            // Callback registration
            loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    Log.d("MainActivity", "Facebook token: " + loginResult!!.accessToken.token)
                    startActivity(
                        Intent(
                            applicationContext, HomeActivity::class.java
                        )
                    )// App code
                }

                override fun onCancel() { // App code
                }

                override fun onError(exception: FacebookException) { // App code
                }
            })
            callbackManager = CallbackManager.Factory.create()
            LoginManager.getInstance()
                .registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
                    override fun onSuccess(loginResult: LoginResult?) { // App code
                    }

                    override fun onCancel() { // App code
                    }

                    override fun onError(exception: FacebookException) { // App code
                    }
                })
        })

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("780977723182-n1l2dbg5k9h8trm1nfeditcje078s9kj.apps.googleusercontent.com")
            .requestEmail().build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

//        gmail_login.setOnClickListener(DebouncedClickListener {
//            signIn()
//        })

        edit_username.doOnTextChanged { text, start, before, count ->
            til_userName.isErrorEnabled = false
        }

        edit_password.doOnTextChanged { text, start, before, count ->
            til_password.isErrorEnabled = false
        }

        // Button click listener
        login_btn.setOnClickListener(DebouncedClickListener {
            val user = edit_username.text.toString()
            val password = edit_password.text.toString()
            if (user.isEmpty() && password.isEmpty()) {
                til_userName.error = getString(R.string.username_required)
                til_password.error = getString(R.string.password_required)
                til_userName.isErrorEnabled = true
                til_password.isErrorEnabled = true
                edit_username.requestFocus()
                return@DebouncedClickListener
            } else if (user.isEmpty() || user.isBlank()) {
                til_userName.error = getString(R.string.username_required)
                til_userName.isErrorEnabled = true
                edit_username.requestFocus()
                return@DebouncedClickListener
            } else if (password.isEmpty() || password.isBlank()) {
                til_password.error = getString(R.string.password_required)
                til_password.isErrorEnabled = true
                edit_password.requestFocus()
                return@DebouncedClickListener
            } else if (!UtilCommon.isValidPassword(password)) {
                til_password.error = getString(R.string.valid_password)
                til_password.isErrorEnabled = true
                edit_password.requestFocus()
                return@DebouncedClickListener
            } else {
                if (Functions.isConnectingToInternet(this@LoginActivity)) {
                    sessionManager.saveDEVICE_TOKEN(m_deviceId)
                    sessionManager.savePASSWORD(password)
                    login(user, password, m_deviceId, device_type)
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        resources.getString(R.string.no_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

        registration_layout.setOnClickListener(DebouncedClickListener {
            val i = Intent(this@LoginActivity, RegistrationActivity::class.java)
            i.putExtra("m_deviceId", m_deviceId)
            startActivity(i)
            finish()
        })

        forgot_btn.setOnClickListener(DebouncedClickListener {
            val fragment = supportFragmentManager.findFragmentByTag("ExitDialogFragment")
            if (fragment == null) {
                val exitFragment = ForgotPasswordDialog.newInstance(this)
                exitFragment.show(supportFragmentManager, "ExitDialogFragment")
            }
        })
    }

//    private fun signIn() {
//        val signInIntent = mGoogleSignInClient.signInIntent
//        startActivityForResult(
//            signInIntent, RC_SIGN_IN
//        )
//    }

//    private fun revokeAccess() {
//        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this) {
//            // Update your UI here
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun login(user: String, password: String, m_deviceId: String, device_type: String) {
        val pd = CustomProgressBar(this@LoginActivity)
        pd.show()

        val call: Call<LoginResponse> =
            MyHssApplication.instance!!.api.userLogin(user, password, m_deviceId, device_type)
        call.enqueue(object : Callback<LoginResponse> {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                if (response.code() == 200 && response.body() != null) {
                    if (response.body()!!.status == true) {
                        val sharedPreferences = getSharedPreferences(
                            "production", Context.MODE_PRIVATE
                        )
                        sessionManager.saveDEVICE_TOKEN(m_deviceId)
                        sessionManager.saveFIRSTNAME(response.body()!!.firstname!!)
                        sessionManager.saveSURNAME(response.body()!!.lastname!!)
                        sessionManager.saveUSERNAME(response.body()!!.username!!)
                        sessionManager.saveUserID(response.body()!!.userId!!)
                        sessionManager.saveUSEREMAIL(response.body()!!.email!!)
                        sessionManager.saveUSERROLE(response.body()!!.role!!)
                        sessionManager.saveMEMBERID(response.body()!!.memberId!!)
//                        sessionManager.saveSECURITYKEY(response.body()!!.securityKey!!)
                        sessionManager.saveAuthToken(response.body()!!.token!!)


                        sharedPreferences.edit().apply {
                            putString("DEVICE_TOKEN", m_deviceId)
                            putString("FIRSTNAME", response.body()!!.firstname)
                            putString("SURNAME", response.body()!!.lastname)
                            putString("USERNAME", response.body()!!.username)
                            putString("USERID", response.body()!!.userId)
                            putString("USEREMAIL", response.body()!!.email)
                            putString("USERROLE", response.body()!!.role)
                            putString("MEMBERID", response.body()!!.memberId)
//                            putString("SECURITYKEY", response.body()!!.securityKey)
                            putString("USERTOKEN", response.body()!!.token)
                        }.apply()

                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putString("DEVICE_TOKEN", m_deviceId)
                        editor.putString("FIRSTNAME", response.body()!!.firstname)
                        editor.putString("SURNAME", response.body()!!.lastname)
                        editor.putString("USERNAME", response.body()!!.username)
                        editor.putString("USERID", response.body()!!.userId)
                        editor.putString("USEREMAIL", response.body()!!.email)
                        editor.putString("USERROLE", response.body()!!.role)
                        editor.putString("MEMBERID", response.body()!!.memberId)
//                        editor.putString("SECURITYKEY", response.body()!!.securityKey)
                        editor.putString("USERTOKEN", response.body()!!.token)
                        editor.apply()
                        editor.commit()

                        if (response.body()!!.memberId == "" || response.body()!!.memberStatus == "0") {  // && sharedPreferences.getString("MEMBERID", "") == ""
                            val i = Intent(this@LoginActivity, WelcomeActivity::class.java)
                            startActivity(i)
                            finish()
                        } else if (response.body()!!.memberId != "" && response.body()!!.memberStatus == "1") {
                            val i = Intent(this@LoginActivity, Passcode_Activity::class.java)
                            i.putExtra("CHANGE_BIOMETRIC", "")
                            startActivity(i)
                            finish()
                        } else {
                            Functions.displayMessage(
                                this@LoginActivity, "Login Error, Please try after sometime"
                            )
                        }
                    } else {
                        Functions.displayMessage(this@LoginActivity, response.body()?.message)
                    }
                } else if (response.code() == 404) {
                    Functions.showAlertMessageWithOK(
                        this@LoginActivity, "",
                        response.body()?.message
                    )
                } else {
                    Functions.showAlertMessageWithOK(
                        this@LoginActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    private fun forgot(forgotuser: String) {
        val pd = CustomProgressBar(this@LoginActivity)
        pd.show()
        val call: Call<ForgotPasswordResponse> =
            MyHssApplication.instance!!.api.userForgot(forgotuser)
        call.enqueue(object : Callback<ForgotPasswordResponse> {
            override fun onResponse(
                call: Call<ForgotPasswordResponse>, response: Response<ForgotPasswordResponse>
            ) {

                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {
                        Functions.showAlertMessageWithOK(
                            this@LoginActivity, "",
//                        "Message",
                            response.body()?.message
                        )
                    } else {
                        Functions.showAlertMessageWithOK(
                            this@LoginActivity, "",
//                        "Message",
                            response.body()?.message
                        )
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@LoginActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(
                ApiException::class.java
            )
            // Signed in successfully
            val googleId = account?.id ?: ""
            Log.i("Google ID", googleId)

            val googleFirstName = account?.givenName ?: ""
            Log.i("Google First Name", googleFirstName)

            val googleLastName = account?.familyName ?: ""
            Log.i("Google Last Name", googleLastName)

            val googleEmail = account?.email ?: ""
            Log.i("Google Email", googleEmail)

            val googleProfilePicURL = account?.photoUrl.toString()
            Log.i("Google Profile Pic URL", googleProfilePicURL)

            val googleIdToken = account?.idToken ?: ""
            Log.i("Google ID Token", googleIdToken)

        } catch (e: ApiException) {
            // Sign in was unsuccessful
            Log.e(
                "failed code=", e.statusCode.toString()
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showDialog(loginActivity: LoginActivity) {
        val dialog = Dialog(loginActivity, R.style.BottomSheetDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_biometric)

        val imgLogo = dialog.findViewById(R.id.img_logo) as ImageView
        val itemTitle = dialog.findViewById(R.id.item_title) as TextView
        val itemStatus = dialog.findViewById(R.id.item_status) as TextView
        val itemSubtitle = dialog.findViewById(R.id.item_subtitle) as TextView
        val itemDescription = dialog.findViewById(R.id.item_description) as TextView

        val btn_allow = dialog.findViewById(R.id.btn_allow) as Button
        val btn_cancel = dialog.findViewById(R.id.btn_cancel) as Button

        itemTitle.text = getString(R.string.app_name)
        itemSubtitle.text =
            "Do you want to allow " + getString(R.string.app_name) + " to use TouchId or FaceId"

        btn_allow.visibility = View.VISIBLE
        btn_allow.text = "Allow"
        btn_cancel.text = "Cancel"
        btn_allow.setTextColor(getColor(R.color.black))
        btn_cancel.setTextColor(getColor(R.color.black))

        updateLogo(imgLogo)

        btn_allow.setOnClickListener {
            dialog.dismiss()
            sharedPreferences.edit().apply {
                putString("Allow_biometric", "ALLOW")
            }.apply()
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("Allow_biometric", "ALLOW")

            checkBiometric()
        }

        btn_cancel.setOnClickListener {
            dialog.dismiss()
            CallDashboard()
        }
        dialog.show()
    }

    private fun updateLogo(imgLogo: ImageView) {
        try {
            val drawable: Drawable = packageManager.getApplicationIcon(packageName)
            imgLogo?.setImageDrawable(drawable)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkBiometric() {
        val biometricCallback = object : BiometricCallback {
            override fun onSdkVersionNotSupported() {
                Log.d("FP", "onSdkVersionNotSupported")
                Toast.makeText(this@LoginActivity, "onSdkVersionNotSupported", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onBiometricAuthenticationNotSupported() {
                Log.d("FP", "onBiometricAuthenticationNotSupported")
                CallDashboard()
//                Toast.makeText(
//                    this@LoginActivity,
//                    "onBiometricAuthenticationNotSupported",
//                    Toast.LENGTH_SHORT
//                ).show()
            }

            override fun onBiometricAuthenticationNotAvailable() {
                Log.d("FP", "onBiometricAuthenticationNotAvailable")
                Toast.makeText(
                    this@LoginActivity, "onBiometricAuthenticationNotAvailable", Toast.LENGTH_SHORT
                ).show()
                CallDashboard()
            }

            override fun onBiometricAuthenticationPermissionNotGranted() {
                Log.d("FP", "onBiometricAuthenticationPermissionNotGranted")
                Toast.makeText(
                    this@LoginActivity,
                    "onBiometricAuthenticationPermissionNotGranted",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onBiometricAuthenticationInternalError(error: String) {
                Toast.makeText(this@LoginActivity, error, Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationFailed() {
                Log.d("FP", "onAuthenticationFailed")
                Toast.makeText(this@LoginActivity, "onAuthenticationFailed", Toast.LENGTH_SHORT)
                    .show()
                finishAffinity()
            }

            override fun onAuthenticationCancelled() {
                Log.d("FP", "onAuthenticationCancelled")
                Toast.makeText(this@LoginActivity, "onAuthenticationCancelled", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onAuthenticationSuccessful() {
                Log.d("FP", "onAuthenticationSuccessful")
                Toast.makeText(this@LoginActivity, "onAuthenticationSuccessful", Toast.LENGTH_SHORT)
                    .show()
                CallDashboard()
            }

            override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence) {
                Toast.makeText(this@LoginActivity, helpString, Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                Toast.makeText(this@LoginActivity, errString, Toast.LENGTH_SHORT).show()
            }
        }

//        BiometricDialog(this@LoginActivity)
        val dialogV23 = BiometricDialogV23(this@LoginActivity, biometricCallback)
        dialogV23.setCanceledOnTouchOutside(false)
        BiometricManager(
            this@LoginActivity,
            getString(R.string.app_name),
            getString(R.string.sub_title),
            getString(R.string.description_title),
            getString(R.string.cancel),
            "Error text",
            dialogV23
        ).authenticate(biometricCallback)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun BiometricDialog(loginActivity: LoginActivity) {
        val dialog = Dialog(loginActivity, R.style.BottomSheetDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_biometric)

        val imgLogo = dialog.findViewById(R.id.img_logo) as ImageView
        val itemTitle = dialog.findViewById(R.id.item_title) as TextView
        val itemStatus = dialog.findViewById(R.id.item_status) as TextView
        val itemSubtitle = dialog.findViewById(R.id.item_subtitle) as TextView
        val itemDescription = dialog.findViewById(R.id.item_description) as TextView

        val btn_allow = dialog.findViewById(R.id.btn_allow) as Button
        val btn_cancel = dialog.findViewById(R.id.btn_cancel) as Button

        itemTitle.text = getString(R.string.app_name)
        itemStatus.text = getString(R.string.sub_title)
        itemSubtitle.text = getString(R.string.description_title)

        btn_cancel.text = "Cancel"

        btn_cancel.setTextColor(getColor(R.color.black))

        updateLogo(imgLogo)

        btn_allow.setOnClickListener {
            dialog.dismiss()
        }

        btn_cancel.setOnClickListener {
            dialog.dismiss()
            CallDashboard()
        }
        dialog.show()
    }

    private fun CallDashboard() {
        if (sharedPreferences.getString("MEMBERID", "") != "") {
            startActivity(
                Intent(
                    this@LoginActivity, HomeActivity::class.java
                )
            )
            finish()
        } else {
            startActivity(
                Intent(
                    this@LoginActivity, WelcomeActivity::class.java
                )
            )
            finish()
        }
    }

    override fun forgotPasswordDialog(forgotUserID: String) {
        DebugLog.e("forgotuser name : $forgotUserID")
        if (Functions.isConnectingToInternet(this@LoginActivity)) {
            forgot(forgotUserID)
        } else {
            Toast.makeText(
                this@LoginActivity,
                resources.getString(R.string.no_connection),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}