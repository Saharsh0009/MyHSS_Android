package com.uk.myhss.Welcome

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.Login_Registration.Passcode_Activity
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.Functions
import com.uk.myhss.AddMember.AddMemberFirstActivity
import com.uk.myhss.Login_Registration.LoginActivity
import com.uk.myhss.Main.HomeActivity
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.Welcome.WelcomeModel.WelcomeResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class WelcomeActivity : AppCompatActivity() {
    //    private val sharedPrefFile = "MyHss"
    private lateinit var sessionManager: SessionManager

    private var user_id: String = ""
    private var TYPE_SELF: String = ""

    lateinit var welcome_txt_msg: TextView
    lateinit var add_self: TextView
    lateinit var add_self_family: TextView
    lateinit var btn_logOut: ImageView

    //    lateinit var welcome_layout: LinearLayout
    lateinit var add_self_layout: LinearLayout
    lateinit var rootLayout: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_activity)

        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("WelcomeVC")
        sessionManager.firebaseAnalytics.setUserProperty("WelcomeVC", "WelcomeActivity")

        sessionManager.firebaseAnalytics.setUserId("InitialVC")
        sessionManager.firebaseAnalytics.setUserProperty("InitialVC", "WelcomeActivity")

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        FirebaseApp.initializeApp(/*context=*/ this)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance()
        )

        val back_arrow = findViewById<ImageView>(R.id.back_arrow)
        val header_title = findViewById<TextView>(R.id.header_title)

        welcome_txt_msg = findViewById(R.id.welcome_txt_msg)
        add_self = findViewById(R.id.add_self)
        add_self_family = findViewById(R.id.add_self_family)
        add_self_layout = findViewById(R.id.add_self_layout)
        rootLayout = findViewById(R.id.rootLayout)
        btn_logOut = findViewById(R.id.info_tooltip)
        back_arrow.visibility = View.INVISIBLE
        header_title.text = getString(R.string.welocome)
        btn_logOut.setImageResource(R.drawable.ic_logout)


        if (sessionManager.fetchUserID().isNullOrEmpty() || sessionManager.fetchUserID()
                .isNullOrBlank()
        ) {
            val alertDialog: android.app.AlertDialog.Builder =
                android.app.AlertDialog.Builder(this@WelcomeActivity)
            alertDialog.setMessage(getString(R.string.myhss_app_regret_to_inform_you_that_the_installation_was_unsuccessful_kindly_uninstall_the_application_and_proceed_to_download_it_again_from_the_play_store_to_ensure_a_successful_installation_thank_you_for_your_understanding))
            alertDialog.setPositiveButton(
                "OK"
            ) { _, _ ->
                finishAffinity()
            }
            val alert: android.app.AlertDialog = alertDialog.create()
            alert.setCanceledOnTouchOutside(false)
            alert.setCancelable(false)
            alert.show()
            return
        } else {
            user_id = sessionManager.fetchUserID()!!
        }

        if (Functions.isConnectingToInternet(this@WelcomeActivity)) {
            welcome(user_id)
        } else {
            Toast.makeText(
                this@WelcomeActivity,
                resources.getString(R.string.no_connection),
                Toast.LENGTH_SHORT
            ).show()
        }

        if (add_self.text.toString() == getString(R.string.Add_self)) {
            add_self_layout.setOnClickListener(DebouncedClickListener {
                val i = Intent(this@WelcomeActivity, AddMemberFirstActivity::class.java)
                i.putExtra("TYPE_SELF", TYPE_SELF)
                startActivity(i)
            })
        }
        btn_logOut.setOnClickListener(DebouncedClickListener {
            callLogOutMethod()
        })
    }

    private fun callLogOutMethod() {
        val alertDialog: AlertDialog.Builder =
            AlertDialog.Builder(this@WelcomeActivity)
        alertDialog.setTitle("Logout")
        alertDialog.setMessage("Are you sure you would like to logout?")
        alertDialog.setPositiveButton(
            "yes"
        ) { _, _ ->

            val sharedPreferences = this@WelcomeActivity.getSharedPreferences(
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

            val i = Intent(this@WelcomeActivity, LoginActivity::class.java)
            startActivity(i)
            finishAffinity()
        }
        alertDialog.setNegativeButton(
            "No"
        ) { _, _ ->

        }
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }

    private fun welcome(user_id: String) {
        val pd = CustomProgressBar(this@WelcomeActivity)
        pd.show()
        val call: Call<WelcomeResponse> = MyHssApplication.instance!!.api.welcome_api(user_id)
        call.enqueue(object : Callback<WelcomeResponse> {
            override fun onResponse(
                call: Call<WelcomeResponse>, response: Response<WelcomeResponse>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body()?.status!!) {
                        val type = response.body()!!.type
                        try {
                            val responseBody = response.body()
                            if (responseBody != null && responseBody.member_id != null) {
                                sessionManager.saveMEMBERID(responseBody.member_id!!)
                            } else {
                                sessionManager.saveMEMBERID("")
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        val sharedPreferences = getSharedPreferences(
                            "production", Context.MODE_PRIVATE
                        )
                        sharedPreferences.edit().apply {
                            putString("TYPE_SELF", TYPE_SELF)
                            putString("MEMBERID", response.body()!!.member_id)
                        }.apply()

                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putString("TYPE_SELF", TYPE_SELF)
                        editor.putString("MEMBERID", response.body()!!.member_id)
                        editor.apply()
                        editor.commit()

                        TYPE_SELF = type.toString()
//                        Log.d("TYPE_SELF", TYPE_SELF)

                        welcome_txt_msg.visibility = View.VISIBLE
                        add_self_layout.visibility = View.VISIBLE

                        if (type == "self") {
                            add_self_layout.visibility = View.VISIBLE
                            add_self_family.visibility = View.GONE
                            welcome_txt_msg.text = response.body()!!.tooltip
                            btn_logOut.visibility = View.VISIBLE
                        } else {
                            add_self_layout.visibility = View.GONE
                            add_self_family.visibility = View.GONE
                            welcome_txt_msg.text = response.body()!!.tooltip
                            welcome_txt_msg.setBackgroundResource(R.drawable.greetext_background_round)
                            welcome_txt_msg.setTextColor(getResources().getColor(R.color.greenColor))
                            btn_logOut.visibility = View.VISIBLE
                        }

                        if (TYPE_SELF == "family") {
                            welcome_txt_msg.visibility = View.GONE
                            btn_logOut.visibility = View.GONE
                            Handler().postDelayed({
                                val i = Intent(
                                    this@WelcomeActivity, Passcode_Activity::class.java
                                )
                                i.putExtra("CHANGE_BIOMETRIC", "")
                                startActivity(i)
                                finish()
                            }, 100)
                        }
                    } else {
                        Functions.showAlertMessageWithOK(
                            this@WelcomeActivity, "",
                            response.body()?.message
                        )
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@WelcomeActivity, "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<WelcomeResponse>, t: Throwable) {
                Toast.makeText(this@WelcomeActivity, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }
}