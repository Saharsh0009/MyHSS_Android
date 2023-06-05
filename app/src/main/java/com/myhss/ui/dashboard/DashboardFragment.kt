package com.uk.myhss.ui.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.AllShakha.AllShakhaListActivity
import com.myhss.Login_Registration.Passcode_Activity
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.Functions
import com.myhss.Utils.ScrollableGridView
import com.myhss.ui.Barchat.SuryaNamaskar
import com.myhss.ui.SanghSandesh.SanghSandeshActivity
import com.myhss.ui.ShakhaMap.MapsActivity
import com.myhss.ui.SuchanaBoard.SuchanaBoardActivity
import com.uk.myhss.AddMember.AddMemberFirstActivity
import com.uk.myhss.Guru_Dakshina_OneTime.GuruDakshinaOneTimeFirstActivity
import com.uk.myhss.Guru_Dakshina_Regular.GuruDakshinaRegularFirstActivity
import com.uk.myhss.Login_Registration.LoginActivity
import com.uk.myhss.Main.Get_Privileges.Get_Privileges_Response
import com.uk.myhss.Main.Get_Prpfile.Datum_Get_Profile
import com.uk.myhss.Main.Get_Prpfile.Get_Profile_Response
import com.uk.myhss.Main.Get_Prpfile.Member_Get_Profile
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.Welcome.WelcomeActivity
import com.uk.myhss.ui.policies.ProfileFragment
import com.uk.myhss.ui.policies.SankhyaActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class DashboardFragment : Fragment() {
    private lateinit var sessionManager: SessionManager
    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null
    lateinit var total_count: TextView

    private var membershipIsVisible = true
    private var eventsIsVisible = true
    private var gurupujaIsVisible = true
    private var newsIsVisible = true
    private var reportsIsVisible = true
    private var shakhaIsVisible = true
    private var otherIsVisible = false

    lateinit var member_hide_show_layout: RelativeLayout
    lateinit var events_hide_show_layout: RelativeLayout
    lateinit var gurupuja_hide_show_layout: RelativeLayout
    lateinit var other_hide_show_layout: RelativeLayout
    lateinit var news_hide_show_layout: RelativeLayout
    lateinit var reports_hide_show_layout: RelativeLayout
    lateinit var shakha_hide_show_layout: RelativeLayout

    lateinit var news_layout: LinearLayout

    lateinit var member_hide: ImageView
    lateinit var gurupuja_hide: ImageView
    lateinit var shakha_hide: ImageView
    lateinit var news_hide: ImageView
    lateinit var event_hide: ImageView
    lateinit var other_hide: ImageView

    /*Member ship Grid View*/
    lateinit var membership_gridview: ScrollableGridView
    var memberNames = arrayOf(
        "User Profile", "View Linked Family Members", "Add a Family Member",
//        "Change Username", "Change Password"
    )
    var memberImages = intArrayOf(
        R.drawable.user_profile,
        R.drawable.linked_family_member,
        R.drawable.add_family_member,
//        R.drawable.change_user_name,
//        R.drawable.change_password
    )

    /*Events ship Grid View*/
    lateinit var events_gridview: ScrollableGridView
    var eventsNames = arrayOf(
        "Events"
//        "Add Events", "Registration For Events", "Event Calendar",
//        "User Event History"
    )
    var eventsImages = intArrayOf(
        R.drawable.add_events,
//        R.drawable.register_forevents,
//        R.drawable.register_for_events,
//        R.drawable.user_event
    )


    /*Guru Puja ship Grid View*/
    lateinit var guru_puja_gridview: ScrollableGridView
    var guru_pujaNames = arrayOf("One-Off Dakshina", "Regular Dakshina") //, "Shakha Guru Dakshina")
    var guru_pujaImages = intArrayOf(
        R.drawable.one_off,
        R.drawable.regular_dakshina,
//        R.drawable.shakaha_guru_dakshina
    )

    /*News Grid View*/
    lateinit var news_gridview: ScrollableGridView
    var newsNames = arrayOf("Sangh Mail/Sangh Sandesh")

    //    var newsNames = arrayOf("Sangh Mail", "Sangh Sandesh")
    var newsImages = intArrayOf(
        R.drawable.shakaha_mail
//        R.drawable.sangh_sandesh
    )

    /*Reports Grid View*/
    lateinit var reports_gridview: ScrollableGridView
    var reportsNames = arrayOf("Sankhya")
    var reportsImages = intArrayOf(
        R.drawable.sankhya
    )

    /*other_gridview Grid View*/
    lateinit var other_gridview: ScrollableGridView
    //var otherNames = arrayOf("Find a Shakha")
    //var otherImages = intArrayOf(R.drawable.find_shakha)

    /*Shakha Grid View*/
    lateinit var shakha_gridview: ScrollableGridView
    var shakhaNames = arrayOf("Suchana Board", "Surya Namasakar") /*"Find a Shakha"*/
    var shakhaNamesNew =
        arrayOf("Suchana Board", "My Shakha", "Surya Namasakar") /*"Find a Shakha"*/
//    var shakhaNames = arrayOf("My Shakha")

    //        "Sankhiya Form", "My Shakha")
//        "My Nagar", "My Vibhag", "My Kendriya"
//    )
    var shakhaImages = intArrayOf(
//        R.drawable.sankhya_form,
        R.drawable.suchana,
        R.drawable.surya_namskar,
//        R.drawable.my_shakha,
//        R.drawable.find_shakha
//        R.drawable.my_nagar,
//        R.drawable.my_vibhag,
//        R.drawable.my_kendriya
    )

    var shakhaImagesNew = intArrayOf(
//        R.drawable.sankhya_form,
        R.drawable.suchana,
        R.drawable.my_shakha,
        R.drawable.surya_namskar,
//        R.drawable.find_shakha
//        R.drawable.my_nagar,
//        R.drawable.my_vibhag,
//        R.drawable.my_kendriya
    )


    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard_new, container, false)
//        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        sessionManager = SessionManager(requireContext())

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
        sessionManager.firebaseAnalytics.setUserId("DashbaordVC")
        sessionManager.firebaseAnalytics.setUserProperty("DashbaordVC", "DashboardFragment")

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        member_hide_show_layout = root.findViewById(R.id.member_hide_show_layout) as RelativeLayout
        events_hide_show_layout = root.findViewById(R.id.events_hide_show_layout) as RelativeLayout
        gurupuja_hide_show_layout =
            root.findViewById(R.id.gurupuja_hide_show_layout) as RelativeLayout
        news_hide_show_layout = root.findViewById(R.id.news_hide_show_layout) as RelativeLayout
        reports_hide_show_layout =
            root.findViewById(R.id.reports_hide_show_layout) as RelativeLayout
        shakha_hide_show_layout = root.findViewById(R.id.shakha_hide_show_layout) as RelativeLayout
        other_hide_show_layout = root.findViewById(R.id.other_hide_show_layout) as RelativeLayout

        news_layout = root.findViewById(R.id.news_layout) as LinearLayout

        membership_gridview = root.findViewById(R.id.membership_gridview) as ScrollableGridView
        events_gridview = root.findViewById(R.id.events_gridview) as ScrollableGridView
        guru_puja_gridview = root.findViewById(R.id.guru_puja_gridview) as ScrollableGridView
        news_gridview = root.findViewById(R.id.news_gridview) as ScrollableGridView
        reports_gridview = root.findViewById(R.id.reports_gridview) as ScrollableGridView
        other_gridview = root.findViewById(R.id.other_gridview) as ScrollableGridView
        shakha_gridview = root.findViewById(R.id.shakha_gridview) as ScrollableGridView

        member_hide = root.findViewById(R.id.member_hide)
        gurupuja_hide = root.findViewById(R.id.gurupuja_hide)
        shakha_hide = root.findViewById(R.id.shakha_hide)
        news_hide = root.findViewById(R.id.news_hide)
        event_hide = root.findViewById(R.id.event_hide)
        other_hide = root.findViewById(R.id.other_hide)

        reports_gridview.visibility = View.GONE
        reports_hide_show_layout.visibility = View.GONE
        events_hide_show_layout.visibility = View.GONE
        events_gridview.visibility = View.GONE
        news_hide_show_layout.visibility = View.GONE
        news_gridview.visibility = View.GONE

        /*if (sessionManager.fetchSHAKHA_TAB() == "yes") {
            shakha_gridview.visibility = View.VISIBLE
            shakha_hide_show_layout.visibility = View.VISIBLE
        } else {
            shakha_gridview.visibility = View.GONE
            shakha_hide_show_layout.visibility = View.GONE
        }*/

        /*For Membership*/
        val member_customAdapter = context?.let { Adapter_dashboard(it, memberImages, memberNames) }
        membership_gridview.adapter = member_customAdapter

        /*For Events*/
        val events_customAdapter = context?.let { Adapter_dashboard(it, eventsImages, eventsNames) }
        events_gridview.adapter = events_customAdapter

        /*For Guru Puja*/
        val guru_customAdapter = context?.let {
            Adapter_dashboard(
                it,
                guru_pujaImages,
                guru_pujaNames
            )
        }
        guru_puja_gridview.adapter = guru_customAdapter


        if (sessionManager.fetchSHAKHA_TAB() == "yes") {

            /*For News*/
//            news_layout.visibility = View.VISIBLE
//            news_hide_show_layout.visibility = View.VISIBLE
//            news_gridview.visibility = View.VISIBLE

            /*For News*/
            val news_customAdapter = context?.let { Adapter_dashboard(it, newsImages, newsNames) }
            news_gridview.adapter = news_customAdapter

            news_gridview.onItemClickListener = OnItemClickListener { parent, v, position, id ->
                if (position == 0) {
                    startActivity(Intent(requireContext(), SanghSandeshActivity::class.java))
                }
            }

            /*For Shakha*/
            val shakha_customAdapter =
                context?.let { Adapter_dashboard(it, shakhaImagesNew, shakhaNamesNew) }
            shakha_gridview.adapter = shakha_customAdapter

            shakha_gridview.onItemClickListener = OnItemClickListener { parent, v, position, id ->
                if (position == 0) {
                    /*val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.activity_main_content_id, SuchanaBoardFragment())
                    transaction.disallowAddToBackStack()
                    transaction.commit()*/
                    val i = Intent(requireContext(), SuchanaBoardActivity::class.java)
                    startActivity(i)
                } else if (position == 1) {
                    val i = Intent(requireContext(), LinkedFamilyFragment::class.java)
                    i.putExtra("DashBoard", "SHAKHAVIEW")
                    i.putExtra("headerName", getString(R.string.my_shakha))
                    startActivity(i)
                } else if (position == 2) {
                    startActivity(Intent(requireContext(), SuryaNamaskar::class.java))
                }
            }
        } else {

            /*For News*/
//            news_layout.visibility = View.GONE
//            news_hide_show_layout.visibility = View.GONE
//            news_gridview.visibility = View.GONE

            /*For Shakha*/
            val shakha_customAdapter =
                context?.let { Adapter_dashboard(it, shakhaImages, shakhaNames) }
            shakha_gridview.adapter = shakha_customAdapter

            shakha_gridview.onItemClickListener = OnItemClickListener { parent, v, position, id ->
                if (position == 0) {
                    /*val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.activity_main_content_id, SuchanaBoardFragment())
                    transaction.disallowAddToBackStack()
                    transaction.commit()*/
                    val i = Intent(requireContext(), SuchanaBoardActivity::class.java)
                    startActivity(i)
                    /*} else if (position == 1) {
                        val i = Intent(requireContext(), LinkedFamilyFragment::class.java)
                        i.putExtra("DashBoard", "SHAKHAVIEW")
                        startActivity(i)*/
                } else if (position == 1) {
                    startActivity(Intent(requireContext(), SuryaNamaskar::class.java))
//                    val i = Intent(requireContext(), AllShakhaListActivity::class.java)
//                    val i = Intent(requireContext(), MapsActivity::class.java)
//                    i.putExtra("SHAKHA_LIST","SHAKHA_LIST")
//                    startActivity(i)
                }
            }
        }


        /*For otherNames*/
//        val other_customAdapter = context?.let {
//            Adapter_dashboard(
//                it,
//                otherImages,
//                otherNames
//            )
//        }
        //other_gridview.adapter = other_customAdapter

//        other_gridview.onItemClickListener = OnItemClickListener { parent, v, position, id ->
//            if (position == 0) {
//                val i = Intent(requireContext(), AllShakhaListActivity::class.java)
//                    val i = Intent(requireContext(), MapsActivity::class.java)
////                    i.putExtra("SHAKHA_LIST","SHAKHA_LIST")
//                startActivity(i)
//            }
//        }


        /*For Reports*/
        val reports_customAdapter = context?.let {
            Adapter_dashboard(
                it,
                reportsImages,
                reportsNames
            )
        }
        reports_gridview.adapter = reports_customAdapter

        reports_gridview.onItemClickListener = OnItemClickListener { parent, v, position, id ->
            if (position == 0) {
//                startActivity(Intent(requireContext(), SanghSandeshActivity::class.java))
            }
        }

        membership_gridview.onItemClickListener = OnItemClickListener { parent, v, position, id ->
            if (position == 0) {
//                startActivity(Intent(requireContext(), ProfileFragment::class.java))
                val i = Intent(requireContext(), ProfileFragment::class.java)
                i.putExtra("FAMILY", i.getStringExtra("PROFILE"))
                startActivity(i)
            } else if (position == 1) {
                val i = Intent(requireContext(), LinkedFamilyFragment::class.java)
                i.putExtra("DashBoard", "")
                i.putExtra("headerName", getString(R.string.linked_family_member))
                startActivity(i)
//                startActivity(Intent(requireContext(), LinkedFamilyFragment::class.java))
            } else if (position == 2) {
                val i = Intent(requireContext(), AddMemberFirstActivity::class.java)
                i.putExtra("TYPE_SELF", "family")
//                i.putExtra("FAMILY", "FAMILY")
                startActivity(i)
            } /*else if (position == 3) {

            } else if (position == 4) {

            }*/
        }

        events_gridview.onItemClickListener = OnItemClickListener { parent, v, position, id ->
            if (position == 0) {
                val i = Intent(requireContext(), EventsFragment::class.java)
                startActivity(i)
//                startActivity(Intent(requireContext(), ProfileFragment::class.java))
//            } else if (position == 1) {
//                startActivity(Intent(requireContext(), LinkedFamilyFragment::class.java))
//            } else if (position == 2) {
//                val i = Intent(requireContext(), AddMemberFirstActivity::class.java)
//                i.putExtra("TYPE_SELF", "family");
//                startActivity(i)
//            } else if (position == 3) {
            }
        }

        guru_puja_gridview.onItemClickListener = OnItemClickListener { parent, v, position, id ->
            if (position == 0) {
                startActivity(
                    Intent(
                        requireContext(),
                        GuruDakshinaOneTimeFirstActivity::class.java
                    )
                )
            } else if (position == 1) {
                startActivity(
                    Intent(
                        requireContext(),
                        GuruDakshinaRegularFirstActivity::class.java
                    )
                )
            }
        }

        member_hide_show_layout.setOnClickListener {
            if (membershipIsVisible) {
                membership_gridview.visibility = View.GONE
                membershipIsVisible = false
                member_hide.setImageResource(R.drawable.drop_down_icon)
            } else if (!membershipIsVisible) {
                membership_gridview.visibility = View.VISIBLE
                membershipIsVisible = true
                member_hide.setImageResource(R.drawable.drop_up_icon)
            }
        }

        events_hide_show_layout.setOnClickListener {
            if (eventsIsVisible) {
                events_gridview.visibility = View.GONE
                eventsIsVisible = false
                event_hide.setImageResource(R.drawable.drop_down_icon)
            } else if (!eventsIsVisible) {
                events_gridview.visibility = View.VISIBLE
                eventsIsVisible = true
                event_hide.setImageResource(R.drawable.drop_up_icon)
            }
        }

        gurupuja_hide_show_layout.setOnClickListener {
            if (gurupujaIsVisible) {
                guru_puja_gridview.visibility = View.GONE
                gurupujaIsVisible = false
                gurupuja_hide.setImageResource(R.drawable.drop_down_icon)
            } else if (!gurupujaIsVisible) {
                guru_puja_gridview.visibility = View.VISIBLE
                gurupujaIsVisible = true
                gurupuja_hide.setImageResource(R.drawable.drop_up_icon)
            }
        }

        news_hide_show_layout.setOnClickListener {
            if (newsIsVisible) {
                news_gridview.visibility = View.GONE
                newsIsVisible = false
                news_hide.setImageResource(R.drawable.drop_down_icon)
            } else if (!newsIsVisible) {
                news_gridview.visibility = View.VISIBLE
                newsIsVisible = true
                news_hide.setImageResource(R.drawable.drop_up_icon)
            }
        }

        reports_hide_show_layout.setOnClickListener {
            if (reportsIsVisible) {
                reports_gridview.visibility = View.GONE
                reportsIsVisible = false
            } else if (!reportsIsVisible) {
                reports_gridview.visibility = View.VISIBLE
                reportsIsVisible = true
            }
        }

        shakha_hide_show_layout.setOnClickListener {
            if (shakhaIsVisible) {
                shakha_gridview.visibility = View.GONE
                shakhaIsVisible = false
                shakha_hide.setImageResource(R.drawable.drop_down_icon)
            } else if (!shakhaIsVisible) {
                shakha_gridview.visibility = View.VISIBLE
                shakhaIsVisible = true
                shakha_hide.setImageResource(R.drawable.drop_up_icon)
            }
        }

        other_hide_show_layout.setOnClickListener {
            if (otherIsVisible) {
                other_gridview.visibility = View.GONE
                otherIsVisible = false
                other_hide.setImageResource(R.drawable.drop_down_icon)
            } else if (!otherIsVisible) {
                other_gridview.visibility = View.VISIBLE
                otherIsVisible = true
                other_hide.setImageResource(R.drawable.drop_up_icon)
            }
        }

        if (Functions.isConnectingToInternet(requireContext())) {
            val user_id = sessionManager.fetchUserID()
            val member_id = sessionManager.fetchMEMBERID()
            val devicetype = "A"
            val device_token = sessionManager.fetchFCMDEVICE_TOKEN()
//            myPrivileges("1", "approve")
//            if (sessionManager.fetchMEMBERID() != "") {
            myProfile(user_id!!, member_id!!, devicetype, device_token!!)
//            }
        } else {
            Toast.makeText(
                context,
                resources.getString(R.string.no_connection),
                Toast.LENGTH_SHORT
            ).show()
        }
        return root
    }

    /*myPrivileges API*/
    private fun myPrivileges(menu_id: String, approve: String) {
        val pd = CustomProgressBar(requireContext())
        pd.show()
        val call: Call<Get_Privileges_Response> =
            MyHssApplication.instance!!.api.get_privileges(
                sessionManager.fetchUserID()!!,
                menu_id,
                approve
            )
        call.enqueue(object : Callback<Get_Privileges_Response> {
            override fun onResponse(
                call: Call<Get_Privileges_Response>,
                response: Response<Get_Privileges_Response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {
                        Functions.showAlertMessageWithOK(
                            requireContext(), "",
//                        "Message",
                            response.body()?.message
                        )
                    } else {
                        Functions.showAlertMessageWithOK(
                            requireContext(), "",
//                        "Message",
                            response.body()?.message
                        )
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        requireContext(), "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Get_Privileges_Response>, t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
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
        val pd = CustomProgressBar(requireContext())
        pd.show()
        val call: Call<Get_Profile_Response> =
            MyHssApplication.instance!!.api.get_profile(
                user_id,
                member_id,
                deviceType,
                device_token
            )
        call.enqueue(object : Callback<Get_Profile_Response> {
            override fun onResponse(
                call: Call<Get_Profile_Response>,
                response: Response<Get_Profile_Response>
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

                            val sharedPreferences = requireContext().getSharedPreferences(
                                "production",
                                Context.MODE_PRIVATE
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
                                data_getmember[0].buildingName.toString() + " " + data_getmember[0].addressLine1.toString()
                                        + " " + data_getmember[0].addressLine2.toString()
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
//                            sessionManager.saveSECMOBILENO(data_getmember[0].landLine.toString())
//                            sessionManager.saveSECEMAIL(data_getmember[0].secondaryEmail.toString())
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

                        } catch (e: ArithmeticException) {
                            println(e)
                        } finally {
                            println("Profile")
                        }

                    } else {
                        Functions.displayMessage(requireContext(), response.body()?.message)
//                        Functions.showAlertMessageWithOK(
//                            requireContext(), "",
////                        "Message",
//                            response.body()?.message
//                        )
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        requireContext(), "Message",
                        getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<Get_Profile_Response>, t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    class Adapter_dashboard(var context: Context, var Images: IntArray, var Names: Array<String>) :
        BaseAdapter() {
        override fun getCount(): Int {
            return Names.size
        }

        override fun getItem(i: Int): Any {
            return i
        }

        override fun getItemId(i: Int): Long {
            return 0
        }

        @SuppressLint("ViewHolder")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view1 = LayoutInflater.from(parent.context).inflate(
                R.layout.row_data,
                parent,
                false
            )
            //getting view in row_data
            val adapter_name_txt = view1.findViewById<TextView>(R.id.adapter_name_txt)
            val adapter_image_view = view1.findViewById<ImageView>(R.id.adapter_image_view)

            adapter_name_txt.text = Names[position]
            adapter_image_view.setImageResource(Images[position])
            return view1
        }
    }

    fun CallMapRequest() {
        val alertDialog: AlertDialog.Builder =
            AlertDialog.Builder(requireContext())
        alertDialog.setTitle(getString(R.string.app_name))
        alertDialog.setMessage("GPS is not enabled. Do you want to enable")
        alertDialog.setPositiveButton(
            "yes"
        ) { _, _ ->
            val i = Intent(requireContext(), AllShakhaListActivity::class.java)
//            val i = Intent(requireContext(), MapsActivity::class.java)
//            i.putExtra("SHAKHA_LIST","SHAKHA_LIST")
            startActivity(i)
        }
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }
}