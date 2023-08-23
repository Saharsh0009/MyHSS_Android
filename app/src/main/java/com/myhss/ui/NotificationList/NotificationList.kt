package com.myhss.ui.SuchanaBoard

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
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
    private var notificationData: List<Data> = ArrayList<Data>()
    val notificationTypeList: MutableList<NotificationType> = mutableListOf()
    private var notificationAdapter: NotificationAdapter? = null
    lateinit var mLayoutManager: LinearLayoutManager

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
        img_filter.visibility = View.VISIBLE
        back_arrow.setOnClickListener(DebouncedClickListener {
            val i = Intent(this, HomeActivity::class.java)
            startActivity(i)
            finishAffinity()
        })




        data_not_found_layout = findViewById(R.id.data_not_found_layout)
        notification_list = findViewById(R.id.notification_list)
        rootLayout = findViewById(R.id.rootLayout)
        mLayoutManager = LinearLayoutManager(this@NotificationList)
        notification_list.layoutManager = mLayoutManager
        if (sessionManager.fetchSHAKHAID() != "") {
            if (Functions.isConnectingToInternet(this)) {
                MEMBERID = sessionManager.fetchMEMBERID()!!
                notificationListData(MEMBERID)
            } else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.no_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        img_filter.setOnClickListener(DebouncedClickListener {

        })

    }

    private fun notificationListData(member_id: String) {
        val pd = CustomProgressBar(this)
        pd.show()
        val builderData: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builderData.addFormDataPart("member_id", member_id)
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
                            val uniqueTypeNames: HashSet<String> = HashSet()
                            for (dataType in notificationData) {
                                if (uniqueTypeNames.add(dataType.notific_type_name)) {
                                    val notificationType = NotificationType(
                                        dataType.notific_type_name,
                                        dataType.notific_type_id
                                    )
                                    notificationTypeList.add(notificationType)
                                }
                            }
                            notificationAdapter =
                                NotificationAdapter(notificationData, "Notification Detail")
                            notification_list.adapter = notificationAdapter
                            notificationAdapter!!.notifyDataSetChanged()

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

//    private fun openNotificationType() {
//        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
//        val view_d = layoutInflater.inflate(R.layout.dialog_select_galley_pdf, null)
//        val btnClose = view_d.findViewById<ImageView>(R.id.close_layout)
//        val btn_image = view_d.findViewById<LinearLayout>(R.id.select_gallery)
//        val btn_pdf = view_d.findViewById<LinearLayout>(R.id.select_pdf)
//
//        btnClose.setOnClickListener(DebouncedClickListener {
//            dialog.dismiss()
//        })
//
//        btn_image.setOnClickListener(DebouncedClickListener {
//            openGalleryForImage()
//            dialog.dismiss()
//        })
//        btn_pdf.setOnClickListener(DebouncedClickListener {
//            showFileChooserforPDF()
//            dialog.dismiss()
//        })
//        dialog.setCancelable(true)
//        dialog.setContentView(view_d)
//        dialog.show()
//    }
}