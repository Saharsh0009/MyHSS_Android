package com.myhss.ui.SuchanaBoard

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import android.window.OnBackInvokedDispatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.DebugLog
import com.myhss.Utils.Functions
import com.myhss.appConstants.AppParam
import com.myhss.ui.NotificationList.Adapter.NotificationItemAdapter
import com.myhss.ui.NotificationList.Model.Data
import com.myhss.ui.NotificationList.Model.NotificationDatum
import com.myhss.ui.NotificationList.Model.NotificationType
import com.myhss.ui.SuchanaBoard.Adapter.NotificationAdapter
import com.uk.myhss.Main.HomeActivity
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NotificationList : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    lateinit var data_not_found_layout: RelativeLayout
    lateinit var notification_list: RecyclerView
    lateinit var rootLayout: RelativeLayout
    lateinit var back_arrow: ImageView
    lateinit var img_filter: ImageView
    lateinit var header_title: TextView
    lateinit var MEMBERID: String
    private var notificationData: List<Data> = ArrayList<Data>() // main api list
    val notificationTypeList: MutableList<NotificationType> = mutableListOf() // type list
    private var notificationAdapter: NotificationAdapter? = null
    lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var notificationTypeDialog: BottomSheetDialog
    var receivedNotiID = "0"

    @SuppressLint("SetTextI18n", "CutPasteId", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_suchana_board)

        sessionManager = SessionManager(this)
        // Obtain the FirebaseAnalytics instance.
        sessionManager.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        sessionManager.firebaseAnalytics.setUserId("Notification")
        sessionManager.firebaseAnalytics.setUserProperty("Notification", "NotificationList")

        sessionManager.firebaseAnalytics = Firebase.analytics
        sessionManager.firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        //  Firebase crashlytics

        back_arrow = findViewById(R.id.back_arrow)
        header_title = findViewById(R.id.header_title)
        img_filter = findViewById(R.id.info_tooltip)

        header_title.text = getString(R.string.my_notification)
        img_filter.setImageResource(R.drawable.ic_filter)
        img_filter.visibility = View.GONE
        back_arrow.setOnClickListener(DebouncedClickListener {
//            val i = Intent(this, HomeActivity::class.java)
//            startActivity(i)
//            finishAffinity()


            onBackPressed()

        })

        data_not_found_layout = findViewById(R.id.data_not_found_layout)
        notification_list = findViewById(R.id.notification_list)
        rootLayout = findViewById(R.id.rootLayout)
        mLayoutManager = LinearLayoutManager(this@NotificationList)
        notification_list.layoutManager = mLayoutManager
        if (sessionManager.fetchSHAKHAID() != "") {
            if (Functions.isConnectingToInternet(this)) {
                MEMBERID = sessionManager.fetchMEMBERID()!!
                callNotificationListData(MEMBERID, "all")
            } else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.no_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        img_filter.setOnClickListener(DebouncedClickListener {
            openNotificationTypeDialog()
        })


        val receivedIntent = intent
        if (receivedIntent != null && receivedIntent.hasExtra(AppParam.NOTIFIC_ID)) {
            receivedNotiID = receivedIntent.getStringExtra(AppParam.NOTIFIC_ID).toString()
        } else {
            receivedNotiID = "0"
        }


    }

    private fun callNotificationListData(member_id: String, sType: String) {
        val pd = CustomProgressBar(this)
        pd.show()
        val builderData: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builderData.addFormDataPart("member_id", member_id)
        builderData.addFormDataPart("type", sType)
        val requestBody: MultipartBody = builderData.build()
        val call: Call<NotificationDatum> =
            MyHssApplication.instance!!.api.postNotificationListData(requestBody)
        call.enqueue(object : Callback<NotificationDatum> {
            override fun onResponse(
                call: Call<NotificationDatum>,
                response: Response<NotificationDatum>
            ) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body()?.status!!) {
                        data_not_found_layout.visibility = View.GONE
                        try {
                            notificationData = response.body()!!.data
                            addNotificationType("ALL", "0")
                            val uniqueTypeNames: HashSet<String> = HashSet()
                            for (dataType in notificationData) {
                                if (uniqueTypeNames.add(dataType.notific_type_name)) {
                                    addNotificationType(
                                        dataType.notific_type_name,
                                        dataType.notific_type_id
                                    )
                                }
                            }
                            setNotificationRv(notificationData)
                            if (notificationTypeList.size > 2) {
                                img_filter.visibility = View.VISIBLE
                            } else {
                                img_filter.visibility = View.GONE
                            }
                        } catch (e: ArithmeticException) {
                            DebugLog.e("e => $e")
                        }
                    } else {
                        data_not_found_layout.visibility = View.VISIBLE
                    }
                } else {
                    Functions.showAlertMessageWithOK(
                        this@NotificationList, "Message", getString(R.string.some_thing_wrong),
                    )
                }
                pd.dismiss()
            }

            override fun onFailure(call: Call<NotificationDatum>, t: Throwable) {
                Toast.makeText(this@NotificationList, t.message, Toast.LENGTH_LONG).show()
                pd.dismiss()
            }
        })
    }

    private fun setNotificationRv(notificData: List<Data>) {
        DebugLog.e("receivedNotiID : $receivedNotiID")
        notificationAdapter =
            NotificationAdapter(notificData, "Notification Detail", receivedNotiID, MEMBERID)
        notification_list.adapter = notificationAdapter
        notificationAdapter!!.notifyDataSetChanged()
    }

    private fun openNotificationTypeDialog() {
        notificationTypeDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val bottomSheetView = layoutInflater.inflate(R.layout.dialog_notification_type, null)
        notificationTypeDialog.setContentView(bottomSheetView)
        val rv_notific_type = bottomSheetView.findViewById<RecyclerView>(R.id.rv_notific_type)
        rv_notific_type.layoutManager = LinearLayoutManager(this)
        val adapter = NotificationItemAdapter(notificationTypeList) { selectedItem ->
            notificationTypeDialog.dismiss()
            filterNotification(selectedItem)
        }
        rv_notific_type.adapter = adapter
        notificationTypeDialog.show()
    }

    fun addNotificationType(sName: String, sId: String) {
        val notificationType = NotificationType(sName, sId)
        notificationTypeList.add(notificationType)
    }

    private fun filterNotification(selectedItem: String) {
        if (selectedItem == "0") {
            setNotificationRv(notificationData)
        } else {
            val notificFilterData: MutableList<Data> = mutableListOf()
            for (dataType in notificationData) {
                if (selectedItem == dataType.notific_type_id) {
                    notificFilterData.add(dataType)
                }
            }
            setNotificationRv(notificFilterData)
        }
    }


    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        super.onBackPressed()
    }
}