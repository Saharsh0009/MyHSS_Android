package com.myhss.Login_Registration

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.Spannable
import android.text.Spanned
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.DebugLog
import com.myhss.Utils.Functions
import com.myhss.Utils.UtilCommon
import com.uk.myhss.Login_Registration.LoginActivity
import com.uk.myhss.Login_Registration.Model.RegistrationResponse
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.ui.linked_family.Model.Get_Member_Check_Username_Exist_Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegistrationActivity : AppCompatActivity() {

    var m_deviceId: String = ""
    var Check_value: String = ""
    lateinit var edit_email: TextInputEditText
    lateinit var til_username: TextInputLayout
    lateinit var edit_username: TextInputEditText
    private lateinit var sessionManager: SessionManager

    private val apiHandler = Handler(Looper.getMainLooper())
    private var lastTypedText: String = ""
    private var isUserNameValid = false
    private lateinit var userNameValidError: String

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("RegisterVC")
        sessionManager.firebaseAnalytics.setUserProperty("RegisterVC", "RegistrationActivity")

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        m_deviceId = intent.getStringExtra("m_deviceId")!!
        Log.d("m_deviceId", m_deviceId)

        val login_btn = findViewById<TextView>(R.id.login_btn)
        val registration_layout = findViewById<RelativeLayout>(R.id.registration_layout)
        val edit_firstname = findViewById<TextInputEditText>(R.id.edit_firstname)
        val edit_surname = findViewById<TextInputEditText>(R.id.edit_surname)
        edit_username = findViewById<TextInputEditText>(R.id.edit_username)
        edit_email = findViewById(R.id.edit_email)
        val edit_password = findViewById<TextInputEditText>(R.id.edit_password)
        val edit_confirmpassword = findViewById<TextInputEditText>(R.id.edit_confirmpassword)
        val rootLayout = findViewById<RelativeLayout>(R.id.rootLayout)
        val already_terms = findViewById<TextView>(R.id.already_terms)
        val checkbox = findViewById<AppCompatCheckBox>(R.id.checkbox)
        val til_firstname = findViewById<TextInputLayout>(R.id.til_firstname)
        val til_surname = findViewById<TextInputLayout>(R.id.til_surname)
        til_username = findViewById<TextInputLayout>(R.id.til_username)
        val til_email = findViewById<TextInputLayout>(R.id.til_email)
        val til_password = findViewById<TextInputLayout>(R.id.til_password)
        val til_confirmpassword = findViewById<TextInputLayout>(R.id.til_confirmpassword)

        checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
//            Toast.makeText(this,isChecked.toString(),Toast.LENGTH_SHORT).show()
            Check_value = isChecked.toString()
        }

        edit_firstname.doOnTextChanged { text, start, before, count ->
            til_firstname.isErrorEnabled = false
        }

        edit_surname.doOnTextChanged { text, start, before, count ->
            til_surname.isErrorEnabled = false
        }

        edit_username.doOnTextChanged { text, start, before, count ->
            til_username.isErrorEnabled = false
            til_username.defaultHintTextColor =
                ContextCompat.getColorStateList(this, R.color.grayColorColor)
//            til_username.boxBackgroundColor = ContextCompat.getColor(this, R.color.grayColorColor)
//            til_username.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grayColorColor)))
        }

        edit_email.doOnTextChanged { text, start, before, count ->
            til_email.isErrorEnabled = false
        }

        edit_password.doOnTextChanged { text, start, before, count ->
            til_password.isErrorEnabled = false
        }

        edit_confirmpassword.doOnTextChanged { text, start, before, count ->
            til_confirmpassword.isErrorEnabled = false
        }

        edit_username.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val currentText = s.toString().trim()
                if (currentText.length >= 5 && currentText != lastTypedText) {
                    apiHandler.removeCallbacksAndMessages(null)
                    apiHandler.postDelayed(
                        { CallUserNameMethod(currentText) },
                        1000
                    )
                } else if (currentText.length < 5 && currentText != lastTypedText) {
                    apiHandler.removeCallbacksAndMessages(null)
                }
                lastTypedText = currentText

            }
        })


        login_btn.setOnClickListener(DebouncedClickListener {
            val firstname = edit_firstname.text.toString()
            val surname = edit_surname.text.toString()
            val username = edit_username.text.toString()
            val email = edit_email.text.toString()
            val password = edit_password.text.toString()
            val confirm_password = edit_confirmpassword.text.toString()

            if (firstname.isEmpty()) {
                til_firstname.error = getString(R.string.first_name)
                til_firstname.isErrorEnabled = true
                edit_firstname.requestFocus()
                return@DebouncedClickListener
            } else if (!UtilCommon.isOnlyLetters(firstname)) {
                til_firstname.error = getString(R.string.valid_first_name)
                til_firstname.isErrorEnabled = true
                edit_firstname.requestFocus()
                return@DebouncedClickListener
            } else if (surname.isEmpty()) {
                til_surname.error = getString(R.string.sur_name)
                til_surname.isErrorEnabled = true
                edit_surname.requestFocus()
                return@DebouncedClickListener
            } else if (!UtilCommon.isOnlyLetters(surname)) {
                til_surname.error = getString(R.string.valid_surname)
                til_surname.isErrorEnabled = true
                edit_surname.requestFocus()
                return@DebouncedClickListener
            } else if (username.isEmpty()) {
                til_username.error = getString(R.string.user_name)
                til_username.isErrorEnabled = true
                edit_username.requestFocus()
                return@DebouncedClickListener
            } else if (!UtilCommon.isValidUserName(username)) {
                til_username.error = getString(R.string.valid_user_name)
                til_username.isErrorEnabled = true
                edit_username.requestFocus()
                return@DebouncedClickListener
            } else if (!isUserNameValid) {
                til_username.error = userNameValidError
                til_username.isErrorEnabled = true
                til_username.errorIconDrawable = null
                edit_username.requestFocus()
                return@DebouncedClickListener
            } else if (email.isEmpty()) {
                til_email.error = getString(R.string.email_id)
                til_email.isErrorEnabled = true
                edit_email.requestFocus()
                return@DebouncedClickListener
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                til_email.error = getString(R.string.valid_email)
                til_email.isErrorEnabled = true
                edit_email.requestFocus()
                return@DebouncedClickListener
            } else if (password.isEmpty()) {
                til_password.error = getString(R.string.enter_password)
                til_password.isErrorEnabled = true
                edit_password.requestFocus()
                return@DebouncedClickListener
            } else if (!UtilCommon.isValidPassword(password)) {
                til_password.error = getString(R.string.valid_password)
                til_password.isErrorEnabled = true
                edit_password.requestFocus()
                return@DebouncedClickListener
            } else if (confirm_password.isEmpty()) {
                til_confirmpassword.error = getString(R.string.enter_confirm_password)
                til_confirmpassword.isErrorEnabled = true
                edit_confirmpassword.requestFocus()
                return@DebouncedClickListener
            } else if (!UtilCommon.isValidPassword(confirm_password)) {
                til_confirmpassword.error = getString(R.string.valid_confirm_password)
                til_confirmpassword.isErrorEnabled = true
                edit_confirmpassword.requestFocus()
                return@DebouncedClickListener
            } else if (password != confirm_password) {
                til_confirmpassword.error = getString(R.string.confirm_both_pass)
                til_confirmpassword.isErrorEnabled = true
                edit_confirmpassword.requestFocus()
            } else if (Check_value != "true") {
                Snackbar.make(rootLayout, getString(R.string.tnc), Snackbar.LENGTH_SHORT).show()
                return@DebouncedClickListener
            } else {
                registration(firstname, surname, username, email, password, m_deviceId)
            }
        })

        registration_layout.setOnClickListener(DebouncedClickListener {
            moveToLoginScreen()
        })

        val policy = " Privacy Policy "
        val Terms = " MyHss Terms & Conditions."

        val span = Spannable.Factory.getInstance()
            .newSpannable("Choosing Register means that you agree to the Hss (UK) $policy and the $Terms")

        val webClickSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(v: View) {
                val uriUrl: Uri = Uri.parse(MyHssApplication.BaseURL + "page/privacy-policy/1")
                val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
                startActivity(launchBrowser)
            }
        }

        val webClickSpanTerms: ClickableSpan = object : ClickableSpan() {
            override fun onClick(v: View) {
                val uriUrl: Uri = Uri.parse(MyHssApplication.BaseURL + "page/terms-conditions/2")
                val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
                startActivity(launchBrowser)
            }
        }

        span.setSpan(webClickSpan, 55, 55 + policy.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        span.setSpan(webClickSpanTerms, 70, span.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        already_terms.text = span

        already_terms.movementMethod = LinkMovementMethod.getInstance()

        /*already_terms.setLinkTextColor(resources.getColor(R.color.primaryColor))
        val value = "<html>Choosing Register means that you agree to the Hss (UK) <a href=\"https://dev.myhss.org.uk/page/privacy-policy/1\">Privacy Policy </a> and the <a href=\"https://dev.myhss.org.uk/page/terms-conditions/2\">MyHss Terms &amp; Conditions</a></html>"
//        already_terms.setText("For us to reach out to you, please fill the details below or contact our customer care at  18004190899 or visit our website http://www.dupont.co.in/corporate-links/contact-dupont.html ")
        already_terms.text = Html.fromHtml(value)
        //"Choosing Register means that you agree to the Hss (UK) Privacy Policy and the MyHss Terms &amp; Conditions"
        Linkify.addLinks(already_terms, Linkify.WEB_URLS)
        Linkify.addLinks(already_terms, Linkify.ALL)*/

    }

    private fun UserNameCheck(username: String) {
        val call: Call<Get_Member_Check_Username_Exist_Response> =
            MyHssApplication.instance!!.api.get_member_check_username_exist(username)
        call.enqueue(object : Callback<Get_Member_Check_Username_Exist_Response> {
            override fun onResponse(
                call: Call<Get_Member_Check_Username_Exist_Response>,
                response: Response<Get_Member_Check_Username_Exist_Response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body()?.status!!) {
//                        til_username.isErrorEnabled = false
//                        til_username.error = response.body()?.message.toString()
//                        userNameValidError = response.body()?.message.toString()
//                        til_username.setErrorTextColor(ColorStateList.valueOf(Color.GREEN))
//                        edit_username.setHintTextColor(ColorStateList.valueOf(Color.GREEN))
////                        til_username.defaultHintTextColor = ColorStateList.valueOf(Color.GREEN)
//                        til_username.defaultHintTextColor = ContextCompat.getColorStateList(baseContext, R.color.grayColorColor)
//                        til_username.errorIconDrawable = null
                        isUserNameValid = true
                    } else {
                        til_username.error = response.body()?.message.toString()
                        userNameValidError = response.body()?.message.toString()
                        til_username.setErrorTextColor(ColorStateList.valueOf(Color.RED))
                        edit_username.setHintTextColor(ColorStateList.valueOf(Color.RED))
                        til_username.defaultHintTextColor = ColorStateList.valueOf(Color.RED)
                        til_username.isErrorEnabled = true
                        til_username.errorIconDrawable = null
                        edit_username.requestFocus()
                        isUserNameValid = false
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@RegistrationActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
            }

            override fun onFailure(
                call: Call<Get_Member_Check_Username_Exist_Response>, t: Throwable
            ) {
                Toast.makeText(this@RegistrationActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    fun CallUserNameMethod(userNameS: String) {
        UserNameCheck(userNameS)
    }

    private fun registration(
        firstname: String,
        surname: String,
        username: String,
        email: String,
        password: String,
        m_deviceId: String
    ) {
        val pd = CustomProgressBar(this@RegistrationActivity)
        pd.show()
        val call: Call<RegistrationResponse> = MyHssApplication.instance!!.api.userRegistration(
            firstname, surname, username, email, password, m_deviceId
        )
        call.enqueue(object : Callback<RegistrationResponse> {
            override fun onResponse(
                call: Call<RegistrationResponse>, response: Response<RegistrationResponse>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {
                        dialogShow(edit_email.text.toString())
                    } else {
                        var errorMsg = ""
                        val error = response?.body()!!.error
                        if (error != null) {
                            for ((paramName, paramValue) in error) {
                                if (errorMsg.isEmpty()) {
                                    errorMsg = paramValue
                                } else {
                                    errorMsg = errorMsg + "\n\n" + paramValue
                                }
                            }
                        }
                        Functions.showAlertMessageWithOK(
                            this@RegistrationActivity, "Error",
                            errorMsg
                        )

                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@RegistrationActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                Toast.makeText(this@RegistrationActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }


    private fun dialogShow(toString: String) {
        val dialog = BottomSheetDialog(this)
        val view_d = layoutInflater.inflate(R.layout.dialog_registrationsuccess, null)

        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
                behaviour.isDraggable = false
            }
        }

        val btnClose = view_d.findViewById<ImageView>(R.id.close_layout)
        val registration_success_btn = view_d.findViewById<TextView>(R.id.registration_success_btn)
        val emal_txt = view_d.findViewById<TextView>(R.id.emal_txt)

        emal_txt.text = toString
        btnClose.setOnClickListener(DebouncedClickListener {
            dialog.dismiss()
            moveToLoginScreen()
        })
        registration_success_btn.setOnClickListener(DebouncedClickListener {
            dialog.dismiss()
            moveToLoginScreen()
        })
        dialog.setCancelable(false)
        dialog.setContentView(view_d)
        dialog.show()
    }

    private fun setupFullHeight(it: View) {
        val layoutParams = it.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        it.layoutParams = layoutParams
    }

    private fun moveToLoginScreen() {
        val i = Intent(this@RegistrationActivity, LoginActivity::class.java)
        startActivity(i)
        finish()
    }
}

