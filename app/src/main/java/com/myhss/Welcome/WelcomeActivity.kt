package com.uk.myhss.Welcome

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
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
import com.myhss.Utils.Functions
import com.uk.myhss.AddMember.AddMemberFirstActivity
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
//        welcome_layout = findViewById(R.id.welcome_layout)
        add_self_layout = findViewById(R.id.add_self_layout)
        rootLayout = findViewById(R.id.rootLayout)

        back_arrow.visibility = View.INVISIBLE

        header_title.text = getString(R.string.welocome)

        user_id = sessionManager.fetchUserID()!!
        Log.d("user_id", user_id)
        Log.d("user_id", sessionManager.fetchUserID()!!)

        if (Functions.isConnectingToInternet(this@WelcomeActivity)) {
            welcome(user_id)
        } else {
            Toast.makeText(
                this@WelcomeActivity,
                resources.getString(R.string.no_connection),
                Toast.LENGTH_SHORT
            ).show()
        }

        /*Handler().postDelayed({
            RetrofitClient.instance.welcome_api(user_id)
                .enqueue(object : Callback<WelcomeResponse> {
                    override fun onFailure(call: Call<WelcomeResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }

                    @SuppressLint("CommitPrefEdits")
                    override fun onResponse(
                        call: Call<WelcomeResponse>,
                        response: Response<WelcomeResponse>
                    ) {
                        Log.d("status", response.body()?.getStatus().toString())
                        if (response.body()?.getStatus()!!) {
                            Log.d("response", response.message())
                            Log.d("body", response.body().toString())

                            val type = response.body()!!.getType().toString()

                            if (type == "self") {
                                add_self.visibility = View.VISIBLE
                                add_self_family.visibility = View.GONE
                                welcome_txt_msg.text = response.body()!!.getTooltip().toString()
                                add_self.text = getString(R.string.Add_self)
                            } else {
                                add_self.visibility = View.GONE
                                add_self_family.visibility = View.VISIBLE
                                welcome_txt_msg.text = response.body()!!.getTooltip().toString()
                                add_self_family.text = getString(R.string.Add_family_member)
                            }

                        } else {
                            Toast.makeText(
                                applicationContext,
                                response.body()?.getMessage(),
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                })
        }, 500)*/

        if (add_self.text.toString() == getString(R.string.Add_self)) {
//            welcome_layout.setOnClickListener {
            add_self_layout.setOnClickListener {
//                Snackbar.make(rootLayout, "Add self", Snackbar.LENGTH_SHORT).show()
                val i = Intent(this@WelcomeActivity, AddMemberFirstActivity::class.java)
                i.putExtra("TYPE_SELF", TYPE_SELF)
//                    i.putExtra("FAMILY", "MEMBER")
//                val i = Intent(this@WelcomeActivity, MainActivity::class.java)
                startActivity(i)
            }
        }
//        else /*if (add_self_family.text.toString() == getString(R.string.Add_family_member)) {
//            Snackbar.make(rootLayout, "Add family member", Snackbar.LENGTH_SHORT).show()
//        }*/
//        /*if (add_self_family.text.toString() == getString(R.string.Add_family_member))*/ {
//            Handler().postDelayed({
//            welcome_layout.setOnClickListener {
//                val i = Intent(this@WelcomeActivity, MainActivity::class.java)
//                startActivity(i)
//                finish()
//            }
//            }, 500)
//        }
    }

    private fun welcome(user_id: String) {
        val pd = CustomProgressBar(this@WelcomeActivity)
        pd.show()
//        val call: Call<WelcomeResponse> = MyHssApplication.instance!!.api.welcome_api(sessionManager.fetchUserID()!!, token = "Bearer ${sessionManager.fetchAuthToken()}")
//        val call: Call<WelcomeResponse> = MyHssApplication.instance!!.api.welcome_api(//token = "Bearer ${sessionManager.fetchAuthToken()}",
//            user_id = User_id(user_id)
//            user_id
//        )
        val call: Call<WelcomeResponse> = MyHssApplication.instance!!.api.welcome_api(user_id)
        call.enqueue(object : Callback<WelcomeResponse> {
            override fun onResponse(
                call: Call<WelcomeResponse>, response: Response<WelcomeResponse>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {
                        Log.d("response", response.message())
                        Log.d("body", response.body().toString())

                        val type = response.body()!!.type
                        try {
                            val member_id = response.body()!!.member_id

                            sessionManager.saveMEMBERID(member_id!!)
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
                        Log.d("TYPE_SELF", TYPE_SELF)

                        welcome_txt_msg.visibility = View.VISIBLE
                        add_self_layout.visibility = View.VISIBLE
//                    welcome_layout.visibility = View.VISIBLE

                        if (type == "self") {
                            add_self_layout.visibility = View.VISIBLE
//                        add_self.visibility = View.VISIBLE
                            add_self_family.visibility = View.GONE
                            welcome_txt_msg.text = response.body()!!.tooltip
//                        add_self.text = getString(R.string.Add_self)
                        } else {
                            add_self_layout.visibility = View.GONE
//                        add_self.visibility = View.GONE
                            add_self_family.visibility = View.GONE
//                        welcome_layout.visibility = View.GONE
                            welcome_txt_msg.text = response.body()!!.tooltip
//                        add_self_family.text = getString(R.string.Add_family_member)

                            welcome_txt_msg.setBackgroundResource(R.drawable.greetext_background_round)
                            welcome_txt_msg.setTextColor(getResources().getColor(R.color.greenColor))
                        }

                        if (TYPE_SELF == "family") {
                            welcome_txt_msg.visibility = View.GONE
                            Handler().postDelayed({
                                val i = Intent(
                                    this@WelcomeActivity, Passcode_Activity::class.java
                                )
//                                    HomeActivity::class.java)
//                                MainActivity::class.java)
                                i.putExtra("CHANGE_BIOMETRIC", "")
                                startActivity(i)
                                finish()
                            }, 100)
                        }
                    } else {
                        Functions.showAlertMessageWithOK(
                            this@WelcomeActivity, "",
//                        "Message",
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