package com.uk.myhss.ui.policies

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.uk.myhss.AddMember.AddMemberFirstActivity
import com.uk.myhss.R
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.ui.profile.AdapterProfile.ProfileAdapter
import java.util.*

class ProfileFragment : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null

    lateinit var back_arrow: ImageView
    lateinit var header_title: TextView
    lateinit var username_txt: TextView
    lateinit var user_name_txt: TextView
    lateinit var user_role_txt: TextView
    lateinit var user_age_txt: TextView
    lateinit var user_gender_txt: TextView
    lateinit var user_gender_img: ImageView
    lateinit var First_name: String
    lateinit var Last_name: String

    lateinit var edit_layout: LinearLayout

    @SuppressLint("SetTextI18n","MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_profile)
    /*override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)*/
        sessionManager = SessionManager(this)

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("ProfileVC")
        sessionManager.firebaseAnalytics.setUserProperty("ProfileVC", "ProfileFragment")

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        back_arrow = findViewById(R.id.back_arrow)
        header_title = findViewById(R.id.header_title)
        username_txt = findViewById(R.id.username_txt)
        user_name_txt = findViewById(R.id.user_name_txt)
        user_role_txt = findViewById(R.id.user_role_txt)
        user_age_txt = findViewById(R.id.user_age_txt)
        user_gender_txt = findViewById(R.id.user_gender_txt)
        user_gender_img = findViewById(R.id.user_gender_img)

        edit_layout = findViewById(R.id.edit_layout)

        header_title.text = getString(R.string.profile)

        back_arrow.setOnClickListener {
            finish()
        }

        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)

        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.about_us)))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.personal_info)))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.contact_info)))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(getString(R.string.other_info)))
//        tabLayout!!.tabGravity = TabLayout.GRAVITY_START
        tabLayout!!.tabGravity = TabLayout.GRAVITY_CENTER
        tabLayout!!.tabMode = TabLayout.MODE_SCROLLABLE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tabLayout!!.setBackgroundColor(getColor(R.color.white))
        }

        val adapter = this.let { ProfileAdapter(this, supportFragmentManager, tabLayout!!.tabCount) }
        viewPager!!.adapter = adapter
//        viewPager!!.beginFakeDrag()

        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            @SuppressLint("NewApi")
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        if (sessionManager.fetchSURNAME() != "") {
            val first: String = sessionManager.fetchFIRSTNAME()!!.substring(0, 1)
                .uppercase(Locale.getDefault()) + sessionManager.fetchFIRSTNAME()!!.substring(
                1
            )
            val getfirst = first
            First_name = getfirst.substring(0, 1)
        }

        if (sessionManager.fetchSURNAME() != "") {
            val second: String = sessionManager.fetchSURNAME()!!.substring(0, 1)
                .uppercase(Locale.getDefault()) + sessionManager.fetchSURNAME()!!.substring(
                1
            )
            val getsecond = second
            Last_name = getsecond.substring(0, 1)
        }

        username_txt.text = First_name+Last_name

        user_name_txt.text = sessionManager.fetchUSERNAME()!!.capitalize(Locale.ROOT)
        user_role_txt.text = sessionManager.fetchUSERROLE()!!.capitalize(Locale.ROOT)
        user_age_txt.text = sessionManager.fetchAGE() + " Years"
        user_gender_txt.text = sessionManager.fetchGENDER()

        if (sessionManager.fetchGENDER() == "Male") {
            user_gender_img.setImageResource(R.drawable.maleicon)
        } else {
            user_gender_img.setImageResource(R.drawable.femaleicon)
        }

        edit_layout.setOnClickListener {
            val i = Intent(this@ProfileFragment, AddMemberFirstActivity::class.java)
            i.putExtra("TYPE_SELF", "family")
            i.putExtra("FAMILY", "PROFILE")
            startActivity(i)
        }

//        return root
    }
}