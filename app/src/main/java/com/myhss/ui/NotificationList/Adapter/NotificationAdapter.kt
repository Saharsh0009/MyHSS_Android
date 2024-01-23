package com.myhss.ui.SuchanaBoard.Adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.DebugLog
import com.myhss.Utils.Functions
import com.myhss.Utils.UtilCommon
import com.myhss.ui.NotificationList.Model.Data
import com.myhss.ui.SuchanaBoard.Model.Get_Suchana_Seen_Response
import com.myhss.ui.SuchanaBoard.NotificationDetails
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NotificationAdapter(
    private val notificationList: List<Data>,
    private val tabType: String,
    private val notificID: String,
    private val memberID: String
) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.suchna_adapter, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(
            notificationList[position],
            tabType, notificID, memberID
        )
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return notificationList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var sessionManager: SessionManager
//        private var eventsIsVisible = true

        @SuppressLint("SetTextI18n", "ResourceAsColor")
        fun bindItems(
            noti_data: Data,
            tab_type: String,
            notificID: String,
            memberID: String
        ) {
            sessionManager = SessionManager(itemView.context)
            val suchna_title = itemView.findViewById(R.id.suchna_title) as TextView
            val suchna_time = itemView.findViewById(R.id.suchna_time) as TextView
            val suchna_discription = itemView.findViewById(R.id.suchna_discription) as TextView
            val suchna_discriptionnew =
                itemView.findViewById(R.id.suchna_discriptionnew) as TextView
            val suchna_adapter_view =
                itemView.findViewById(R.id.suchna_adapter_view) as LinearLayout
            val redLayout = itemView.findViewById(R.id.redLayout) as LinearLayout

            redLayout.visibility = View.VISIBLE
            suchna_title.text = noti_data.notification_title
            suchna_time.text = UtilCommon.dateAndTimeformat(noti_data.created_at)
            suchna_discription.text = noti_data.notific_type_name
            suchna_discriptionnew.text = noti_data.notification_message
            val notReadNoti = ContextCompat.getColor(itemView.context, R.color.darkfiroziColor)

//            DebugLog.e("IS READ : " + noti_data.is_read)
            if (noti_data.is_read != "1") {
                suchna_discriptionnew.setTextColor(notReadNoti)
            } else {
                suchna_discriptionnew.setTextColor(Color.BLACK)
            }
            suchna_adapter_view.setOnClickListener(DebouncedClickListener {
                if (noti_data.is_read != "1") {
                    if (Functions.isConnectingToInternet(itemView.context)) {
                        callSeenNotificationApi(noti_data, suchna_discriptionnew, tab_type)
                    } else {
                        Toast.makeText(
                            itemView.context,
                            itemView.context.resources.getString(R.string.no_connection),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    val i = Intent(itemView.context, NotificationDetails::class.java)
                    i.putExtra("Suchana_Type", tab_type)
                    i.putExtra("Suchana_Title", noti_data.notification_title)
                    i.putExtra("Suchana_Discription", noti_data.notific_type_name)
                    i.putExtra("Suchana_DiscriptionNew", noti_data.notification_message)
                    i.putExtra("Suchana_time", noti_data.created_at)
                    i.putExtra("recipientId", noti_data.recipientId)
                    itemView.context.startActivity(i)
                }
            })


            DebugLog.e("notificID : $notificID")
            DebugLog.e("noti_data.recipientId : ${noti_data.recipientId}")

            if (notificID != "0") {
                if (notificID == noti_data.notify_id && memberID == noti_data.member_id) {
                    suchna_discriptionnew.setTextColor(Color.BLACK)
                    noti_data.is_read = "1"

                    val i = Intent(itemView.context, NotificationDetails::class.java)
                    i.putExtra("Suchana_Type", tab_type)
                    i.putExtra("Suchana_Title", noti_data.notification_title)
                    i.putExtra("Suchana_Discription", noti_data.notific_type_name)
                    i.putExtra("Suchana_DiscriptionNew", noti_data.notification_message)
                    i.putExtra("Suchana_time", noti_data.created_at)
                    i.putExtra("recipientId", noti_data.recipientId)
                    itemView.context.startActivity(i)
                }
            }
        }

        private fun callSeenNotificationApi(
            noti_data: Data,
            suchna_discriptionnew: TextView,
            tab_type: String
        ) {
            val pd = CustomProgressBar(itemView.context)
            pd.show()
            val builderData: MultipartBody.Builder =
                MultipartBody.Builder().setType(MultipartBody.FORM)
            builderData.addFormDataPart("recipient_id", noti_data.recipientId)
            val requestBody: MultipartBody = builderData.build()
            val call: Call<Get_Suchana_Seen_Response> =
                MyHssApplication.instance!!.api.postSeenNotification(requestBody)
            call.enqueue(object : Callback<Get_Suchana_Seen_Response> {
                override fun onResponse(
                    call: Call<Get_Suchana_Seen_Response>,
                    response: Response<Get_Suchana_Seen_Response>
                ) {
                    if (response.code() == 200 && response.body() != null) {
//                        DebugLog.e("status : " + response.body()?.status.toString())
                        if (response.body()?.status!!) {
                            val readNoti =
                                ContextCompat.getColor(itemView.context, R.color.blackColor)
                            noti_data.is_read = "1"
                            suchna_discriptionnew.setTextColor(readNoti)

                            val i = Intent(itemView.context, NotificationDetails::class.java)
                            i.putExtra("Suchana_Type", tab_type)
                            i.putExtra("Suchana_Title", noti_data.notification_title)
                            i.putExtra("Suchana_Discription", noti_data.notific_type_name)
                            i.putExtra("Suchana_DiscriptionNew", noti_data.notification_message)
                            i.putExtra("Suchana_time", noti_data.created_at)
                            i.putExtra("recipientId", noti_data.recipientId)
                            itemView.context.startActivity(i)
                        } else {
                            Functions.displayMessage(itemView.context, response.body()?.message)
                        }
                    } else {
                        Functions.showAlertMessageWithOK(
                            itemView.context, "Message",
                            itemView.context.getString(R.string.some_thing_wrong),
                        )
                    }
                    pd.dismiss()
                }

                override fun onFailure(call: Call<Get_Suchana_Seen_Response>, t: Throwable) {
                    Toast.makeText(itemView.context, t.message, Toast.LENGTH_LONG).show()
                    pd.dismiss()
                }
            })
        }
    }
}