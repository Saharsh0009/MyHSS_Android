package com.uk.myhss.ui.my_family.Adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.myhss.Utils.DebouncedClickListener
import com.myhss.ui.events.EventsDetails
import com.myhss.ui.events.model.Eventdata
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import java.util.*


class EventsAdapter(val eventListData: MutableList<Eventdata>, private var sEveType: String) :
    RecyclerView.Adapter<EventsAdapter.ViewHolder>() {
    lateinit var adapter_view: RelativeLayout

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.event_layout, parent, false)
        return ViewHolder(v)
    }

    fun addData(newData: List<Eventdata>, st: String) {
        sEveType = st
        eventListData.addAll(newData)
        notifyDataSetChanged()
    }

    fun setData(newData: List<Eventdata>, st: String) {
        eventListData.clear()
        sEveType = st
        eventListData.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(eventListData[position], sEveType, position)
    }

    override fun getItemCount(): Int {
        return eventListData.size
    }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bindItems(eventdata: Eventdata, sEveType_: String, position: Int) {
            val event_name = itemView.findViewById(R.id.event_name) as TextView
            val start_date = itemView.findViewById(R.id.start_date) as TextView
            val end_date = itemView.findViewById(R.id.end_date) as TextView
            val txtEventMode = itemView.findViewById(R.id.txtEventMode) as TextView
            val booked_img = itemView.findViewById(R.id.booked_img) as ImageView
            val active_inactive_view =
                itemView.findViewById(R.id.active_inactive_view) as RelativeLayout

            val adapter_view = itemView.findViewById(R.id.adapter_view) as RelativeLayout
            event_name.text = eventdata.event_title
            start_date.text = eventdata.event_start_date
            end_date.text = eventdata.event_end_date
            if (eventdata.event_chargable_or_not.toString()!! == "0") {
                txtEventMode.text = "Paid"
                active_inactive_view.setBackgroundResource(R.drawable.baalika_background)
            } else {
                txtEventMode.text = "Free"
                active_inactive_view.setBackgroundResource(R.drawable.baal_background)
            }
            Glide.with(itemView.context)
                .load(MyHssApplication.IMAGE_URL_EVENT + eventdata.event_img)
//                .load("https://pbs.twimg.com/profile_images/1605297940242669568/q8-vPggS_400x400.jpg")
                .transition(DrawableTransitionOptions.withCrossFade())
                .transform(RoundedCorners(30))
                .placeholder(R.drawable.splash) // Placeholder image while loading
                .error(R.drawable.splash) // Image to display on error
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(booked_img)

            adapter_view.setOnClickListener(DebouncedClickListener {
                val i = Intent(itemView.context, EventsDetails::class.java)
                i.putExtra("eventdata", eventdata)
                i.putExtra("sEveType_", sEveType_)
                itemView.context.startActivity(i)
            })

        }
    }
}