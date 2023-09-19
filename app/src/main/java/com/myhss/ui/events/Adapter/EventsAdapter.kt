package com.uk.myhss.ui.my_family.Adapter

import android.annotation.SuppressLint
import android.content.Context
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
import com.myhss.Utils.DebugLog
import com.myhss.ui.events.EventsDetails
import com.myhss.ui.events.model.Eventdata
import com.myhss.ui.suryanamaskar.Model.BarchartDataModel
import com.uk.myhss.R
import java.util.*


class EventsAdapter(val eventListData: List<Eventdata>, private val sEveType: String) :
    RecyclerView.Adapter<EventsAdapter.ViewHolder>() {

    lateinit var adapter_view: RelativeLayout
    private val context: Context? = null

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.event_layout, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(eventListData[position], sEveType)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return eventListData.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bindItems(eventdata: Eventdata, sEveType_: String) {

            val event_name = itemView.findViewById(R.id.event_name) as TextView
            val start_date = itemView.findViewById(R.id.start_date) as TextView
            val end_date = itemView.findViewById(R.id.end_date) as TextView
            val txtEventMode = itemView.findViewById(R.id.txtEventMode) as TextView
            val booked_img = itemView.findViewById(R.id.booked_img) as ImageView

            val adapter_view = itemView.findViewById(R.id.adapter_view) as RelativeLayout
            event_name.text = eventdata.event_title
            start_date.text = eventdata.event_start_date
            end_date.text = eventdata.event_end_date
//            txtEventMode.text = eventdata.event_chargable_or_not.toString()!!
            Glide.with(itemView.context)
                .load(eventdata.event_img)
                .transition(DrawableTransitionOptions.withCrossFade())
                .transform(RoundedCorners(30))
                .placeholder(R.drawable.app_logo) // Placeholder image while loading
                .error(R.drawable.app_logo) // Image to display on error
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(booked_img)


//            adapter_view.isEnabled = sEveType_ != "2"

            adapter_view.setOnClickListener(DebouncedClickListener {
                val i = Intent(itemView.context, EventsDetails::class.java)
                i.putExtra("eventdata", eventdata)
                i.putExtra("sEveType_", sEveType_)
                itemView.context.startActivity(i)
            })

        }
    }
}