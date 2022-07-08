package com.myhss.Login_Registration

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.util.Linkify
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatCheckBox
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.Functions
import com.uk.myhss.Login_Registration.LoginActivity
import com.uk.myhss.Login_Registration.Model.RegistrationResponse
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegistrationActivity : AppCompatActivity() {

    var m_deviceId: String = ""
    var Check_value: String = ""
    lateinit var registration_success_layout: RelativeLayout
    lateinit var emal_txt: TextView
    lateinit var edit_email: TextInputEditText

    private lateinit var sessionManager: SessionManager


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
        val edit_username = findViewById<TextInputEditText>(R.id.edit_username)
        edit_email = findViewById(R.id.edit_email)
        val edit_password = findViewById<TextInputEditText>(R.id.edit_password)
        val edit_confirmpassword = findViewById<TextInputEditText>(R.id.edit_confirmpassword)
        val registration_success_btn = findViewById<TextView>(R.id.registration_success_btn)
        val close_layout = findViewById<ImageView>(R.id.close_layout)
        registration_success_layout = findViewById(R.id.registration_success_layout)
        val rootLayout = findViewById<RelativeLayout>(R.id.rootLayout)
        val already_terms = findViewById<TextView>(R.id.already_terms)
        val checkbox = findViewById<AppCompatCheckBox>(R.id.checkbox)
        emal_txt = findViewById(R.id.emal_txt)

        checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
//            Toast.makeText(this,isChecked.toString(),Toast.LENGTH_SHORT).show()
            Check_value = isChecked.toString()
        }

        login_btn.setOnClickListener {
            val firstname = edit_firstname.text.toString()
            val surname = edit_surname.text.toString()
            val username = edit_username.text.toString()
            val email = edit_email.text.toString()
            val password = edit_password.text.toString()
            val confirm_password = edit_confirmpassword.text.toString()

            if (firstname.isEmpty()) {
                Snackbar.make(rootLayout, "Please Enter First name", Snackbar.LENGTH_SHORT).show()
                edit_firstname.error = "First name required"
                edit_firstname.requestFocus()
                return@setOnClickListener
            } else if (surname.isEmpty()) {
                Snackbar.make(rootLayout, "Please Enter surname", Snackbar.LENGTH_SHORT).show()
                edit_surname.error = "surname required"
                edit_surname.requestFocus()
                return@setOnClickListener
            } else if (username.isEmpty()) {
                Snackbar.make(rootLayout, "Please Enter User name", Snackbar.LENGTH_SHORT).show()
                edit_username.error = "Username required"
                edit_username.requestFocus()
                return@setOnClickListener
            } else if (email.isEmpty()) {
                Snackbar.make(rootLayout, "Please Enter Email", Snackbar.LENGTH_SHORT).show()
                edit_email.error = "Email required"
                edit_email.requestFocus()
                return@setOnClickListener
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Snackbar.make(rootLayout, "Please Enter Valid Email", Snackbar.LENGTH_SHORT).show()
                edit_email.error = "Valid Email required"
                edit_email.requestFocus()
                return@setOnClickListener
            } else if (password.isEmpty()) {
                Snackbar.make(rootLayout, "Please Enter Password.", Snackbar.LENGTH_SHORT).show()
                edit_password.error = "Password required"
                edit_password.requestFocus()
                return@setOnClickListener
            } else if (password.length < 8) {
                Snackbar.make(rootLayout, "Please Enter 8 characters.", Snackbar.LENGTH_SHORT)
                    .show()
                edit_password.error = "Password required"
                edit_password.requestFocus()
                return@setOnClickListener
            } else if (confirm_password.isEmpty()) {
                Snackbar.make(rootLayout, "Please Enter Confirm Password.", Snackbar.LENGTH_SHORT)
                    .show()
                edit_confirmpassword.error = "Confirm Password required"
                edit_confirmpassword.requestFocus()
                return@setOnClickListener
            } else if (confirm_password.length < 8) {
                Snackbar.make(rootLayout, "Please Enter 8 characters.", Snackbar.LENGTH_SHORT)
                    .show()
                edit_confirmpassword.error = "Password required"
                edit_confirmpassword.requestFocus()
                return@setOnClickListener
            } else if (password != confirm_password) {
                Snackbar.make(rootLayout, "Password Not matching", Snackbar.LENGTH_SHORT).show()
            } else if (Check_value != "true") {
                Snackbar.make(rootLayout, "Please check terms & conditions", Snackbar.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            } else {
                registration(firstname, surname, username, email, password, m_deviceId)
            }

            /*RetrofitClient.instance.userRegistration(firstname, surname, username, email, password, m_deviceId)
                    .enqueue(object: Callback<RegistrationResponse> {
                        override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                            Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                        }

                        override fun onResponse(call: Call<RegistrationResponse>, response: Response<RegistrationResponse>) {
                            Log.d("status", response.body()?.status.toString())
                            if(response.body()?.status!!){
                                slideUp(registration_success_layout)
                            }else{
                                Toast.makeText(applicationContext, response.body()?.message, Toast.LENGTH_LONG).show()
                            }

                        }
                    })*/
        }

        registration_layout.setOnClickListener {
            val i = Intent(this@RegistrationActivity, LoginActivity::class.java)
            startActivity(i)
            finish()
        }

        close_layout.setOnClickListener {
            sessionManager.slideDown(registration_success_layout)
        }

        registration_success_btn.setOnClickListener {
            sessionManager.slideDown(registration_success_layout)
            val i = Intent(this@RegistrationActivity, LoginActivity::class.java)
            startActivity(i)
            finish()
        }

        val policy = " Privacy Policy "
        val Terms = " MyHss Terms & Conditions."

        val span = Spannable.Factory.getInstance()
            .newSpannable("Choosing Register means that you agree to the Hss (UK) $policy and the $Terms")

        val webClickSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(v: View) {
                val uriUrl: Uri =
                    Uri.parse(MyHssApplication.BaseURL+"page/privacy-policy/1")
                val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
                startActivity(launchBrowser)
            }
        }

        val webClickSpanTerms: ClickableSpan = object : ClickableSpan() {
            override fun onClick(v: View) {
                val uriUrl: Uri =
                    Uri.parse(MyHssApplication.BaseURL+"page/terms-conditions/2")
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

    private fun registration(
        firstname: String, surname: String, username: String, email: String,
        password: String, m_deviceId: String
    ) {
        val pd = CustomProgressBar(this@RegistrationActivity)
        pd.show()
        val call: Call<RegistrationResponse> = MyHssApplication.instance!!.api.userRegistration(
            firstname, surname, username, email,
            password, m_deviceId
        )
        call.enqueue(object : Callback<RegistrationResponse> {
            override fun onResponse(
                call: Call<RegistrationResponse>,
                response: Response<RegistrationResponse>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {
                        emal_txt.text = edit_email.text.toString()
                        sessionManager.slideUp(registration_success_layout)
                    } else {
                        Functions.showAlertMessageWithOK(
                            this@RegistrationActivity, "",
//                        "Message",
                            response.body()?.message
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
}

