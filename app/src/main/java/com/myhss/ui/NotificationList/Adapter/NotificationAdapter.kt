package com.myhss.ui.SuchanaBoard.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.UtilCommon
import com.myhss.ui.NotificationList.Model.Data
import com.myhss.ui.SuchanaBoard.NotificationDetails
import com.uk.myhss.R
import com.uk.myhss.Utils.SessionManager
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class NotificationAdapter(private val notificationList: List<Data>, private val tabType: String) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.suchna_adapter, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(
            notificationList[position],
            tabType
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
            not_data: Data,
            tab_type: String
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
            suchna_title.text = not_data.notification_title
            suchna_time.text = UtilCommon.dateAndTimeformat(not_data.created_at)
            suchna_discription.text = not_data.notific_type_name
            suchna_discriptionnew.text = not_data.notification_message

            suchna_adapter_view.setOnClickListener(DebouncedClickListener {
                val i = Intent(itemView.context, NotificationDetails::class.java)
                i.putExtra("Suchana_Type", tab_type)
                i.putExtra("Suchana_Title", suchna_title.text.toString())
                i.putExtra("Suchana_Discription", suchna_discription.text.toString())
                i.putExtra("Suchana_DiscriptionNew", suchna_discriptionnew.text.toString())
                i.putExtra("Suchana_time", suchna_time.text.toString())
                itemView.context.startActivity(i)
            })
        }
    }
}