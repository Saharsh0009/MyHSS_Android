package com.myhss.ui.NotificationList.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myhss.Utils.DebouncedClickListener
import com.myhss.ui.NotificationList.Model.NotificationType
import com.uk.myhss.R

class NotificationItemAdapter(
    private val items: List<NotificationType>,
    private val onItemClick: (String) -> Unit
) :
    RecyclerView.Adapter<NotificationItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dialog_notification_type_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position].notiTypeName
        val item_id = items[position].notiTypeID
        holder.bind(item, item_id)
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: String, item_id: String) {
            itemView.setOnClickListener(DebouncedClickListener { onItemClick(item_id) })
            val txt_notific_type_item = itemView.findViewById<TextView>(R.id.txt_notific_type_item)
            txt_notific_type_item.text = item
        }
    }
}