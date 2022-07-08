package com.uk.myhss.ui.policies

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.uk.myhss.Guru_Dakshina_OneTime.GuruDakshinaOneTimeFirstActivity
import com.uk.myhss.Guru_Dakshina_Regular.GuruDakshinaRegularFirstActivity
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.EndLessScroll
import com.myhss.Utils.Functions
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.ui.my_family.Adapter.GuruCustomAdapter
import com.uk.myhss.ui.my_family.Model.Datum_guru_dakshina
import com.uk.myhss.ui.my_family.Model.guru_dakshina_response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat.getDrawable
import android.widget.LinearLayout





class GuruDakshinaFragment : Fragment() {
    private lateinit var sessionManager: SessionManager

    lateinit var guru_dakshina_list: RecyclerView

    lateinit var one_time_layout: LinearLayout
    lateinit var regular_layout: LinearLayout
    lateinit var guru_dakshina_layout: RelativeLayout
    lateinit var guru_dakshinasearch_fields: AppCompatEditText
    lateinit var USERID: String

    lateinit var mLayoutManager: LinearLayoutManager

    private var atheletsBeans: List<Datum_guru_dakshina> = ArrayList<Datum_guru_dakshina>()
    private var mAdapterGuru: GuruCustomAdapter? = null
    lateinit var First_name: String

    var end:Int = 100
    var start:Int = 0

    @SuppressLint("WrongConstant", "NewApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_guru_dakshina, container, false)

        sessionManager = SessionManager(requireContext())

        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
        sessionManager.firebaseAnalytics.setUserId("GuruDakshinaaVC")
        sessionManager.firebaseAnalytics.setUserProperty("GuruDakshinaaVC", "GuruDakshinaFragment")

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        one_time_layout = root.findViewById(R.id.one_time_layout)
        regular_layout = root.findViewById(R.id.regular_layout)
        guru_dakshina_layout = root.findViewById(R.id.guru_dakshina_layout)
        guru_dakshinasearch_fields = root.findViewById(R.id.guru_dakshinasearch_fields)

        guru_dakshina_list = root.findViewById(R.id.guru_dakshina_list)

        /*guru_dakshina_list.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayout.VERTICAL,
            false
        )*/

        mLayoutManager = LinearLayoutManager(context)
        guru_dakshina_list.layoutManager = mLayoutManager

        CallAPI(start)

        /*guru_dakshina_list.setOnScrollListener(object : EndLessScroll(mLayoutManager) {
            override fun loadMore(current_page: Int) {
                start = end + 1
                end += 100
                Functions.printLog("start", start.toString())
                Functions.printLog("end", end.toString())
                CallAPI(start)
            }
        })*/

        val bestCities =
            listOf("Lahore", "Berlin", "Lisbon", "Tokyo", "Toronto", "Sydney", "Osaka", "Istanbul")
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            bestCities
        )

        guru_dakshinasearch_fields.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                var end:Int = 100
                var start:Int = 0

                if (Functions.isConnectingToInternet(context)) {
                    USERID = sessionManager.fetchUserID()!!
                    Log.d("USERID", USERID)
                    val LENGTH: String = end.toString()
                    val START: String = start.toString()
                    val SEARCH: String = s.toString()
                    val CHAPTER_ID: String = sessionManager.fetchSHAKHAID()!!   // "227"
                    mySearchGuruDakshina(USERID, LENGTH, START, SEARCH, CHAPTER_ID)
                } else {
                    Toast.makeText(
                        context,
                        resources.getString(R.string.no_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        one_time_layout.setOnClickListener {
//            Snackbar.make(rootview, "One-Time Dakshina", Snackbar.LENGTH_SHORT).show()
            startActivity(Intent(context, GuruDakshinaOneTimeFirstActivity::class.java))
//            val i = Intent(requireContext(), AddMemberFirstActivity::class.java)
//            i.putExtra("TYPE_SELF", "family");
//            startActivity(i)
        }

        regular_layout.setOnClickListener {
//            Snackbar.make(rootview, "Regular Dakshina", Snackbar.LENGTH_SHORT).show()
            startActivity(Intent(context, GuruDakshinaRegularFirstActivity::class.java))
        }

        /*membership_view.setOnClickListener {
            showPopup()
        }*/

        return root
    }

    private fun CallAPI(start: Int) {
        if (Functions.isConnectingToInternet(context)) {
            USERID = sessionManager.fetchUserID()!!
            Log.d("USERID", USERID)
            val LENGTH: String = end.toString()
            val START: String = start.toString()
            val SEARCH: String = ""
            val CHAPTER_ID: String = sessionManager.fetchSHAKHAID()!!   // "227"
            myGuruDakshina(USERID, LENGTH, START, SEARCH, CHAPTER_ID)
        } else {
            Toast.makeText(
                context,
                resources.getString(R.string.no_connection),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun myGuruDakshina(
        user_id: String,
        length: String,
        start: String,
        search: String,
        chapter_id: String
    ) {
        val pd = CustomProgressBar(context)
        pd.show()
        val call: Call<guru_dakshina_response> = MyHssApplication.instance!!.api.get_guru_dakshina(
            user_id, length, start, search, chapter_id
        )
        call.enqueue(object : Callback<guru_dakshina_response> {
            override fun onResponse(
                call: Call<guru_dakshina_response>,
                response: Response<guru_dakshina_response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {

                        try {
                            atheletsBeans = response.body()!!.data!!
                            Log.d("atheletsBeans", atheletsBeans.toString())
                            mAdapterGuru = GuruCustomAdapter(atheletsBeans)

                            guru_dakshina_list.adapter = mAdapterGuru
                            mAdapterGuru!!.notifyDataSetChanged()

                        } catch (e: ArithmeticException) {
                            println(e)
                        } finally {
                            println("Family")
                        }
                    } else {
                        Functions.displayMessage(context,response.body()?.message)
//                        Functions.showAlertMessageWithOK(
//                            context, "",
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

            override fun onFailure(call: Call<guru_dakshina_response>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    private fun mySearchGuruDakshina(
        user_id: String,
        length: String,
        start: String,
        search: String,
        chapter_id: String
    ) {
//        val pd = CustomProgressBar(context)
//        pd.show()
        val call: Call<guru_dakshina_response> = MyHssApplication.instance!!.api.get_guru_dakshina(
            user_id, length, start, search, chapter_id
        )
        call.enqueue(object : Callback<guru_dakshina_response> {
            override fun onResponse(
                call: Call<guru_dakshina_response>,
                response: Response<guru_dakshina_response>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("status", response.body()?.status.toString())
                    if (response.body()?.status!!) {

                        try {
                            atheletsBeans = response.body()!!.data!!
                            Log.d("atheletsBeans", atheletsBeans.toString())
                            mAdapterGuru = GuruCustomAdapter(atheletsBeans)

                            guru_dakshina_list.adapter = mAdapterGuru
                            mAdapterGuru!!.notifyDataSetChanged()

                        } catch (e: ArithmeticException) {
                            println(e)
                        } finally {
                            println("Family")
                        }
                    } else {
                        Functions.displayMessage(context,response.body()?.message)
//                        Functions.showAlertMessageWithOK(
//                            context, "",
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
//                pd.dismiss()
            }

            override fun onFailure(call: Call<guru_dakshina_response>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
//                pd.dismiss()
            }
        })
    }
}