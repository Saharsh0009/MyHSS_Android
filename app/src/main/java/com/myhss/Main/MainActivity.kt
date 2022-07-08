//package com.uk.myhss.Main
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.content.Intent
//import android.content.SharedPreferences
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import android.widget.LinearLayout
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.appcompat.widget.Toolbar
//import androidx.core.view.GravityCompat
//import androidx.drawerlayout.widget.DrawerLayout
//import androidx.fragment.app.FragmentManager
//import androidx.fragment.app.FragmentTransaction
//import androidx.navigation.findNavController
//import androidx.navigation.ui.AppBarConfiguration
//import androidx.navigation.ui.navigateUp
//import androidx.navigation.ui.setupActionBarWithNavController
//import androidx.navigation.ui.setupWithNavController
//import com.google.android.material.floatingactionbutton.FloatingActionButton
//import com.google.android.material.navigation.NavigationView
//import com.google.android.material.snackbar.Snackbar
//import com.myhss.Utils.CustomProgressBar
//import com.myhss.Utils.Functions
//import com.uk.myhss.Main.Get_Privileges.Get_Privileges_Response
//import com.uk.myhss.Main.Get_Prpfile.Datum_Get_Profile
//import com.uk.myhss.Main.Get_Prpfile.Get_Profile_Response
//import com.uk.myhss.Main.Get_Prpfile.Member_Get_Profile
//import com.uk.myhss.R
//import com.uk.myhss.Restful.MyHssApplication
//import com.uk.myhss.Utils.SessionManager
//import com.uk.myhss.ui.dashboard.DashboardFragment
//import com.uk.myhss.ui.policies.ProfileFragment
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import java.util.*
//
//
//class MainActivity : AppCompatActivity() { //, NavigationView.OnNavigationItemSelectedListener {
//
//    private lateinit var sessionManager: SessionManager
//
//    //    private val sharedPrefFile = "MyHss"
//    lateinit var First_name: String
//    lateinit var Last_name: String
//    var drawerLayout: DrawerLayout? = null
//
//    //    private val sessionManager: SessionManager? = null
////    private val sh1: SharedPreferences.Editor? = null
//    private lateinit var appBarConfiguration: AppBarConfiguration
//
//    @SuppressLint("SetTextI18n")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        val toolbar: Toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)
//
//        sessionManager = SessionManager(this)
//        /*val sharedPreferences: SharedPreferences = this.getSharedPreferences(
//            sharedPrefFile,
//            Context.MODE_PRIVATE
//        )*/
//
//        /*// Obtain the FirebaseAnalytics instance.
//        sessionManager.firebaseAnalytics = Firebase.analytics
//
//        sessionManager.firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
//            param(FirebaseAnalytics.Param.ITEM_ID, "LaunchVC")
//            param(FirebaseAnalytics.Param.ITEM_NAME, "LaunchVC")
////            param(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
//        }*/
//
//        if (Functions.isConnectingToInternet(this@MainActivity)) {
//            val user_id = sessionManager.fetchUserID()
//            val member_id = sessionManager.fetchMEMBERID()
////            myPrivileges("1", "approve")
////            if (sessionManager.fetchMEMBERID() != "") {
//            myProfile(user_id!!, member_id!!)
////            }
//        } else {
//            Toast.makeText(
//                this@MainActivity,
//                resources.getString(R.string.no_connection),
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//
//        val fab: FloatingActionButton = findViewById(R.id.fab)
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
//
//        drawerLayout = findViewById(R.id.drawer_layout)
//        val navView: NavigationView = findViewById(R.id.nav_view)
//        val navController = findNavController(R.id.nav_host_fragment)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//
//        appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.nav_dashboard,
////                R.id.nav_organization,
////                R.id.nav_shakha,
////                R.id.nav_shakha_type,
////                R.id.nav_roles,
////                R.id.nav_roles_responsibility,
////                R.id.nav_karyakarts,
////                R.id.nav_sankhya_report,
////                R.id.nav_setting,
//                R.id.nav_policiies,
//                R.id.nav_logout
//            ), drawerLayout
//        )
//
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
//
//        val header: View = navView.getHeaderView(0)
//        val profile_view =
//            header.findViewById<View>(R.id.profile_view) as LinearLayout
////        var profile_img = header.findViewById<View>(R.id.profile_img) as ImageView
//        val user_name = header.findViewById<View>(R.id.user_name) as TextView
//        val user_name_txt =
//            header.findViewById<View>(R.id.user_name_txt) as TextView
//        val user_role = header.findViewById<View>(R.id.user_role) as TextView
//
//        user_name_txt.text = sessionManager.fetchFIRSTNAME()!!
//            .capitalize(Locale.ROOT) + " " + sessionManager.fetchSURNAME()!!.capitalize(Locale.ROOT)
//        user_role.text = sessionManager.fetchUSERROLE()!!.capitalize(Locale.ROOT)
//
//        if (sessionManager.fetchSURNAME() != "" && sessionManager.fetchSURNAME() != "") {
//            val first: String = sessionManager.fetchFIRSTNAME()!!.substring(0, 1)
//                .toUpperCase() + sessionManager.fetchFIRSTNAME()!!.substring(
//                1
//            )
//            val getfirst = first
//            First_name = getfirst.substring(0, 1)
//
//            val second: String = sessionManager.fetchSURNAME()!!.substring(0, 1)
//                .toUpperCase() + sessionManager.fetchSURNAME()!!.substring(
//                1
//            )
//            val getsecond = second
//            Last_name = getsecond.substring(0, 1)
//
//            user_name.text = First_name + Last_name
//        }
//
//        profile_view.setOnClickListener {
////            ProfileFragment    nav_host_fragment
//            Log.d("Hi", "Profile")
//            openCloseNavigationDrawer()
//            val i = Intent(this@MainActivity, ProfileFragment::class.java)
//            i.putExtra("FAMILY", i.getStringExtra("PROFILE"))
//            startActivity(i)
//
////            val homeFragment = ProfileFragment()
////            val fragmentManager: FragmentManager = supportFragmentManager
////            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
////            fragmentTransaction.replace(R.id.nav_host_fragment, homeFragment).commit()
//        }
//
////        val mgr: FragmentManager = supportFragmentManager
////        val trans: FragmentTransaction = mgr.beginTransaction()
////        val memberDepositReceiv1 = DashboardFragment()
////        trans.add(R.id.nav_dashboard, memberDepositReceiv1)
////        trans.addToBackStack(null)
////        trans.commit()
//
////        val F_Name: SharedPreferences = getSharedPreferences("FIRSTNAME", sessionManager!!.PRIVATE_MODE)
//
//        val sharedNameValue = sessionManager.fetchUSERNAME()
//        Log.d("NAME-->", "default name: ${sharedNameValue}")
//    }
//
//    /*override fun onNavigationItemSelected(@NonNull item: MenuItem): Boolean {
//        // Handle navigation view item clicks here.
//        when (item.getItemId()) {
////            R.id.nav_logout -> {
////                Log.d("NAME-->", "Logout")
////            }
//        }
//        //close navigation drawer
//        drawerLayout!!.closeDrawer(GravityCompat.START)
//        return true
//    }*/
//
//    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }*/
//
//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment)
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }
//
//    fun openCloseNavigationDrawer() {
//        if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
//            drawerLayout!!.closeDrawer(GravityCompat.START)
//        } else {
//            drawerLayout!!.openDrawer(GravityCompat.START)
//        }
//    }
//
//    /*myPrivileges API*/
//    private fun myPrivileges(menu_id: String, approve: String) {
//        val pd = CustomProgressBar(this@MainActivity)
//        pd.show()
//        val call: Call<Get_Privileges_Response> =
//            MyHssApplication.instance!!.api.get_privileges(
//                sessionManager.fetchUserID()!!,
//                menu_id,
//                approve
//            )
//        call.enqueue(object : Callback<Get_Privileges_Response> {
//            override fun onResponse(
//                call: Call<Get_Privileges_Response>,
//                response: Response<Get_Privileges_Response>
//            ) {
//
//                Log.d("status", response.body()?.status.toString())
//                if (response.body()?.status!!) {
//                    Functions.showAlertMessageWithOK(
//                        this@MainActivity, "",
////                        "Message",
//                        response.body()?.message
//                    )
//                } else {
//                    Functions.showAlertMessageWithOK(
//                        this@MainActivity, "",
////                        "Message",
//                        response.body()?.message
//                    )
//                }
//                pd.dismiss()
//            }
//
//            override fun onFailure(call: Call<Get_Privileges_Response>, t: Throwable) {
//                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG).show()
//                pd.dismiss()
//            }
//        })
//    }
//
//    /*myPrivileges API*/
//    private fun myProfile(user_id: String, member_id: String) {
//        val pd = CustomProgressBar(this@MainActivity)
//        pd.show()
//        val call: Call<Get_Profile_Response> =
//            MyHssApplication.instance!!.api.get_profile(user_id, member_id)
//        call.enqueue(object : Callback<Get_Profile_Response> {
//            override fun onResponse(
//                call: Call<Get_Profile_Response>,
//                response: Response<Get_Profile_Response>
//            ) {
//                Log.d("status", response.body()?.status.toString())
//                if (response.body()?.status!!) {
//                    try {
//                        var data_getprofile: List<Datum_Get_Profile> =
//                            ArrayList<Datum_Get_Profile>()
//                        data_getprofile = response.body()!!.data!!
//
//                        var data_getmember: List<Member_Get_Profile> =
//                            ArrayList<Member_Get_Profile>()
//                        data_getmember = response.body()!!.member!!
////                    data_getmember = response.body()!!.data!![0].member!!
//
//                        val sharedPreferences = getSharedPreferences(
//                            "production",
//                            Context.MODE_PRIVATE
//                        )
//
//                        sharedPreferences.edit().apply {
//                            putString("FIRSTNAME", data_getmember[0].firstName)
//                            putString("SURNAME", data_getmember[0].lastName)
//                            putString("USERNAME", data_getprofile[0].username)
//                            putString("USERID", data_getmember[0].userId)
//                            putString("USEREMAIL", data_getmember[0].email)
//                            putString("USERROLE", data_getprofile[0].role)
//                            putString("MEMBERID", data_getmember[0].memberId)
//                        }.apply()
//
//                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
//                        editor.putString("FIRSTNAME", data_getmember[0].firstName)
//                        editor.putString("SURNAME", data_getmember[0].lastName)
//                        editor.putString("USERNAME", data_getprofile[0].username)
//                        editor.putString("USERID", data_getmember[0].userId)
//                        editor.putString("USEREMAIL", data_getmember[0].email)
//                        editor.putString("USERROLE", data_getprofile[0].role)
//                        editor.putString("MEMBERID", data_getmember[0].memberId)
//                        editor.apply()
//                        editor.commit()
//
//                        sessionManager.saveFIRSTNAME(data_getmember[0].firstName.toString())
//                        sessionManager.saveMIDDLENAME(data_getmember[0].middleName.toString())
//                        sessionManager.saveSURNAME(data_getmember[0].lastName.toString())
//                        sessionManager.saveUSERNAME(data_getprofile[0].username.toString())
//                        sessionManager.saveUSEREMAIL(data_getmember[0].email.toString())
//                        sessionManager.saveUSERROLE(data_getprofile[0].role.toString())
//                        sessionManager.saveDOB(data_getmember[0].dob.toString())
//                        sessionManager.saveSHAKHANAME(data_getmember[0].shakha.toString())
//                        sessionManager.saveSHAKHAID(data_getmember[0].shakhaId.toString())
//                        sessionManager.saveNAGARID(data_getmember[0].nagarId.toString())
//                        sessionManager.saveVIBHAGID(data_getmember[0].vibhagId.toString())
//                        sessionManager.saveNAGARNAME(data_getmember[0].nagar.toString())
//                        sessionManager.saveVIBHAGNAME(data_getmember[0].vibhag.toString())
//                        sessionManager.saveADDRESS(
//                            data_getmember[0].buildingName.toString() + " " + data_getmember[0].addressLine1.toString()
//                                    + " " + data_getmember[0].addressLine2.toString()
//                        )
//                        sessionManager.saveLineOne(data_getmember[0].addressLine1.toString())
//                        sessionManager.saveRELATIONSHIPNAME(data_getmember[0].relationship.toString())
//                        sessionManager.saveRELATIONSHIPNAME_OTHER(data_getmember[0].otherRelationship.toString())
//                        sessionManager.saveOCCUPATIONNAME(data_getmember[0].occupation.toString())
//                        sessionManager.saveSPOKKENLANGUAGE(data_getmember[0].rootLanguage.toString())
//                        sessionManager.saveSPOKKENLANGUAGEID(data_getmember[0].root_language_id.toString())
//                        sessionManager.saveMOBILENO(data_getmember[0].mobile.toString())
//                        sessionManager.saveSECMOBILENO(data_getmember[0].landLine.toString())
//                        sessionManager.saveSECEMAIL(data_getmember[0].secondaryEmail.toString())
//                        sessionManager.saveGUAEMRNAME(data_getmember[0].emergencyName.toString())
//                        sessionManager.saveGUAEMRPHONE(data_getmember[0].emergencyPhone.toString())
//                        sessionManager.saveGUAEMREMAIL(data_getmember[0].emergencyEmail.toString())
//                        sessionManager.saveGUAEMRRELATIONSHIP(data_getmember[0].emergencyRelatioship.toString())
//                        sessionManager.saveGUAEMRRELATIONSHIP_OTHER(data_getmember[0].otherEmergencyRelationship.toString())
//                        sessionManager.saveDOHAVEMEDICAL(data_getmember[0].medicalInformationDeclare.toString())
//                        sessionManager.saveQUALIFICATIONAID(data_getmember[0].isQualifiedInFirstAid.toString())
//                        sessionManager.saveAGE(data_getmember[0].memberAge.toString())
//                        sessionManager.saveGENDER(data_getmember[0].gender.toString())
//                        sessionManager.saveCITY(data_getmember[0].city.toString())
//                        sessionManager.saveCOUNTRY(data_getmember[0].country.toString())
//                        sessionManager.savePOSTCODE(data_getmember[0].postalCode.toString())
//                        sessionManager.saveSHAKHA_TAB(data_getmember[0].shakha_tab.toString())
//                        sessionManager.saveSHAKHA_SANKHYA_AVG(data_getmember[0].shakha_sankhya_avg.toString())
//
//                        sessionManager.saveMEDICAL_OTHER_INFO(data_getmember[0].medicalDetails.toString())
//                        sessionManager.saveQUALIFICATION_DATE(data_getmember[0].dateOfFirstAidQualification.toString())
//                        sessionManager.saveQUALIFICATION_FILE(data_getmember[0].firstAidQualificationFile.toString())
//                        sessionManager.saveDIETARY(data_getmember[0].specialMedDietryInfo.toString())
//                        sessionManager.saveDIETARYID(data_getmember[0].special_med_dietry_info_id.toString())
//                        sessionManager.saveSTATE_IN_INDIA(data_getmember[0].indianConnectionState.toString())
//
//                        Log.d("Address", sessionManager.fetchADDRESS()!!)
//                        Log.d("Username", sessionManager.fetchUSERNAME()!!)
//                        Log.d("Shakha_tab", sessionManager.fetchSHAKHA_TAB()!!)
//
//                    } catch (e: ArithmeticException) {
//                        println(e)
//                    } finally {
//                        println("Profile")
//                    }
//
//                } else {
//                    Functions.showAlertMessageWithOK(
//                        this@MainActivity, "",
////                        "Message",
//                        response.body()?.message
//                    )
//                }
//                pd.dismiss()
//            }
//
//            override fun onFailure(call: Call<Get_Profile_Response>, t: Throwable) {
//                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG).show()
//                pd.dismiss()
//            }
//        })
//    }
//}