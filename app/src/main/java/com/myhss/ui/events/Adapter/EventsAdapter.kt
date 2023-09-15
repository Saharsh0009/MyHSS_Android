package com.uk.myhss.ui.my_family.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myhss.Utils.DebouncedClickListener
import com.myhss.ui.events.EventsDetails
import com.myhss.ui.events.model.Eventdata
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.ui.linked_family.Model.Get_Member_Listing_Datum
import java.util.*


class EventsAdapter(val userList: List<Eventdata>) :
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
        holder.bindItems(userList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bindItems(my_family_DatumGurudakshina: Eventdata) {

            val event_name = itemView.findViewById(R.id.event_name) as TextView
            val start_date = itemView.findViewById(R.id.start_date) as TextView
            val end_date = itemView.findViewById(R.id.end_date) as TextView
//            val event_info = itemView.findViewById(R.id.event_info) as TextView
            val booked_img = itemView.findViewById(R.id.booked_img) as ImageView

            val adapter_view = itemView.findViewById(R.id.adapter_view) as RelativeLayout

//            if (my_family_DatumGurudakshina.middleName != "") {
//                event_name.text =
//                    my_family_DatumGurudakshina.firstName!!.capitalize(Locale.ROOT) + " " +
//                            my_family_DatumGurudakshina.middleName!!.capitalize(Locale.ROOT) + " " +
//                            my_family_DatumGurudakshina.lastName!!.capitalize(Locale.ROOT)
//            } else {
//                event_name.text =
//                    my_family_DatumGurudakshina.firstName!!.capitalize(Locale.ROOT) + " " +
//                            my_family_DatumGurudakshina.lastName!!.capitalize(Locale.ROOT)
//            }
////            event_info.text = my_family_DatumGurudakshina.chapterName!!.capitalize(Locale.ROOT)
//
//            start_date.text = my_family_DatumGurudakshina.mobile
//            end_date.text = my_family_DatumGurudakshina.mobile

//            Glide.with(itemView.context)
//                .load(MyHssApplication.IMAGE_PDF_URL + sessionManager.fetchQUALIFICATION_FILE()).into(
//                    booked_img
//                )


            adapter_view.setOnClickListener(DebouncedClickListener {
//                Toast.makeText(itemView.context, "Adapter", Toast.LENGTH_SHORT).show()
                val i = Intent(itemView.context, EventsDetails::class.java)
                i.putExtra("EVENT", "Youth Club Event")
                i.putExtra(
                    "DISCRIPTION",
                    "I have created Youth Club Event in UK. I have created Youth Club Event in UK. I have created Youth Club Event in UK. I have created Youth Club Event in UK. I have created Youth Club Event in UK. I have created Youth Club Event in UK. I have created Youth Club Event in UK. I have created Youth Club Event in UK. I have created Youth Club Event in UK. I have created Youth Club Event in UK. I have created Youth Club Event in UK."
                )
                i.putExtra("INFO", "Sunday (06-01-2022)\n\n09:30 AM 11:00 AM\n\n")
                itemView.context.startActivity(i)
            })

        }
    }
}