package com.uk.myhss.ui.policies

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.uk.myhss.AddMember.Get_Relationship.Datum_Relationship
import com.uk.myhss.AddMember.Get_Relationship.Get_Relationship_Response
import com.uk.myhss.Guru_Dakshina_OneTime.GuruDakshinaOneTimeFourthActivity
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.Functions
import com.uk.myhss.Utils.SessionManager
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SupportFragment : Fragment() {

    private lateinit var sessionManager: SessionManager

    var relationshipName: List<String> = ArrayList<String>()
    var relationshipID: List<String> = ArrayList<String>()

    private var RELATIONSHIP_ID: String = ""
//    private var DASHINA_ID: String = ""

//    private var dakshina = arrayOf("Monthly", "Quarterly", "Annually")

    private lateinit var occupation_select_txt: SearchableSpinner
//    private lateinit var dashina_select_txt: SearchableSpinner

    private lateinit var rootview: LinearLayout

    private lateinit var occupation_select_other_view: RelativeLayout
    private lateinit var realationship_other_view: RelativeLayout


    private lateinit var edit_realationship_name: TextInputEditText

    private lateinit var support_view: RelativeLayout
    private lateinit var clear_view: RelativeLayout
    private lateinit var dashina_select_view: RelativeLayout


    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_support, container, false)

        rootview = root.findViewById(R.id.rootview)

        sessionManager = SessionManager(requireContext())

        sessionManager = SessionManager(requireContext())

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
        sessionManager.firebaseAnalytics.setUserId("SupportVC")
        sessionManager.firebaseAnalytics.setUserProperty("SupportVC", "SupportFragment")

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        occupation_select_txt = root.findViewById(R.id.occupation_select_txt)
//        dashina_select_txt = root.findViewById(R.id.dashina_select_txt)
        occupation_select_other_view = root.findViewById(R.id.occupation_select_other_view)
        realationship_other_view = root.findViewById(R.id.realationship_other_view)
        edit_realationship_name = root.findViewById(R.id.edit_realationship_name)
        clear_view = root.findViewById(R.id.clear_view)
        support_view = root.findViewById(R.id.support_view)
//        dashina_select_view = root.findViewById(R.id.dashina_select_view)

        if (Functions.isConnectingToInternet(requireContext())) {
            myRelationship()
        } else {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.no_connection),
                Toast.LENGTH_SHORT
            ).show()
        }

        occupation_select_txt.onItemSelectedListener = mOnItemSelectedListener_realationship
//        dashina_select_txt.onItemSelectedListener = mOnItemSelectedListener_dakshina

        occupation_select_txt.setTitle("Select Occupation")

        occupation_select_other_view.setOnClickListener {
            SearchSpinner(relationshipName.toTypedArray(), occupation_select_txt)
        }

        /*For Selection Dasgina drop down*/
//        SearchSpinner(dakshina, dashina_select_txt)
//
//        occupation_select_other_view.setOnClickListener {
//            SearchSpinner(dakshina, dashina_select_txt)
//        }

        support_view.setOnClickListener {
            Snackbar.make(rootview, "Support", Snackbar.LENGTH_SHORT).show()
        }

        clear_view.setOnClickListener {
            Snackbar.make(rootview, "Clear", Snackbar.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), GuruDakshinaOneTimeFourthActivity::class.java))
        }

        return root
    }

    private fun SearchSpinner(
        spinner_search: Array<String>,
        edit_txt: SearchableSpinner
    ) {
        val searchmethod = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item,
            spinner_search
        )
        searchmethod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        edit_txt.adapter = searchmethod
    }

    private val mOnItemSelectedListener_realationship: AdapterView.OnItemSelectedListener = object :
        AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//            TODO("Not yet implemented")
            Log.d("Name", relationshipName[position])
            Log.d("Postion", relationshipID[position])
            RELATIONSHIP_ID = relationshipID[position]

            if (RELATIONSHIP_ID == "5") {
                realationship_other_view.visibility = View.VISIBLE
            } else {
                realationship_other_view.visibility = View.GONE
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
//            TODO("Not yet implemented")
        }
    }

    /*private val mOnItemSelectedListener_dakshina: AdapterView.OnItemSelectedListener = object :
        AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//            TODO("Not yet implemented")
            Log.d("Name", dakshina[position])
            DASHINA_ID = dakshina[position]
            Log.d("DASHINA_ID", DASHINA_ID)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
//            TODO("Not yet implemented")
        }
    }*/

    /*Relationship API*/
    private fun myRelationship() {
        val pd = CustomProgressBar(requireContext())
        pd.show()
        val call: Call<Get_Relationship_Response> =
            MyHssApplication.instance!!.api.get_relationship()
        call.enqueue(object : Callback<Get_Relationship_Response> {
            override fun onResponse(
                call: Call<Get_Relationship_Response>,
                response: Response<Get_Relationship_Response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {

                        var data_relationship: List<Datum_Relationship> =
                            ArrayList<Datum_Relationship>()
                        data_relationship = response.body()!!.data!!
                        Log.d("atheletsBeans", data_relationship.toString())
                        for (i in 1 until data_relationship.size) {
                            Log.d(
                                "relationshipName",
                                data_relationship[i].relationshipName.toString()
                            )
                        }
                        relationshipName = listOf(arrayOf(data_relationship).toString())
                        relationshipID = listOf(arrayOf(data_relationship).toString())

                        val mStringList = ArrayList<String>()
                        for (i in 0 until data_relationship.size) {
                            mStringList.add(
//                            data_relationship[i].memberRelationshipId.toString() +
                                data_relationship[i].relationshipName.toString()
                            )
                        }

                        val mStringListnew = ArrayList<String>()
                        for (i in 0 until data_relationship.size) {
                            mStringListnew.add(
                                data_relationship[i].memberRelationshipId.toString()
                            )
                        }

//                    getCountry(relationshipName.toTypedArray())
                        var mStringArray = mStringList.toArray()
                        var mStringArraynew = mStringListnew.toArray()

                        for (i in mStringArray.indices) {
                            Log.d("string is", mStringArray[i] as String)
                        }

                        for (i in mStringArraynew.indices) {
                            Log.d("mStringArraynew is", mStringArraynew[i] as String)
                        }

                        mStringArray = mStringList.toArray(mStringArray)
                        mStringArraynew = mStringListnew.toArray(mStringArraynew)

                        val list: ArrayList<String> = arrayListOf<String>()
                        val listnew: ArrayList<String> = arrayListOf<String>()

                        for (element in mStringArray) {
                            Log.d("LIST==>", element.toString())
                            list.add(element.toString())
                            Log.d("list==>", list.toString())

                            val listn = arrayOf(element)
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                relationshipName =
                                    list//listOf(listn.toCollection(ArrayList()).toString())
                            }
                        }

                        for (element in mStringArraynew) {
                            Log.d("LIST==>", element.toString())
                            listnew.add(element.toString())
                            Log.d("list==>", listnew.toString())

                            val listn = arrayOf(element)
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                relationshipID =
                                    listnew//listOf(listn.toCollection(ArrayList()).toString())
                            }
                        }

                        Log.d("relationshipName==>", relationshipName.toString())
                        SearchSpinner(relationshipName.toTypedArray(), occupation_select_txt)

                    } else {
                        Functions.displayMessage(requireContext(),response.body()?.message)
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

            override fun onFailure(call: Call<Get_Relationship_Response>, t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }
}