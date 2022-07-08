package com.uk.myhss.ui.my_family.Adapter

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.*
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.uk.myhss.R
import com.uk.myhss.ui.linked_family.Model.Get_Member_Listing_Datum

import java.util.*


class CustomAdapter(val userList: List<Get_Member_Listing_Datum>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    lateinit var adapter_view: LinearLayout
    private val context: Context? = null

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_layout, parent, false)
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
        fun bindItems(my_family_DatumGurudakshina: Get_Member_Listing_Datum) {
            val active_inactive_view = itemView.findViewById(R.id.active_inactive_view) as RelativeLayout
            val family_name = itemView.findViewById(R.id.family_name) as TextView
            val family_address  = itemView.findViewById(R.id.family_address) as TextView
            val active_inactive_txt  = itemView.findViewById(R.id.active_inactive_txt) as TextView
            val active_inactive  = itemView.findViewById(R.id.active_inactive) as TextView
            val email_txt  = itemView.findViewById(R.id.email_txt) as TextView
            val call_txt  = itemView.findViewById(R.id.call_txt) as TextView
            val email_img  = itemView.findViewById(R.id.email_img) as ImageView
            val call_img  = itemView.findViewById(R.id.call_img) as ImageView
            val active_inactive_img  = itemView.findViewById(R.id.active_inactive_img) as ImageView
            val righr_menu  = itemView.findViewById(R.id.righr_menu) as ImageView
            val adapter_view  = itemView.findViewById(R.id.adapter_view) as LinearLayout
            if (my_family_DatumGurudakshina.middleName != "") {
                family_name.text = my_family_DatumGurudakshina.firstName!!.capitalize(Locale.ROOT) + " " +
                        my_family_DatumGurudakshina.middleName!!.capitalize(Locale.ROOT) + " " +
                        my_family_DatumGurudakshina.lastName!!.capitalize(Locale.ROOT)
            } else {
                family_name.text = my_family_DatumGurudakshina.firstName!!.capitalize(Locale.ROOT) + " " +
                        my_family_DatumGurudakshina.lastName!!.capitalize(Locale.ROOT)
            }
            family_address.text = my_family_DatumGurudakshina.chapterName!!.capitalize(Locale.ROOT)
            email_txt.text = my_family_DatumGurudakshina.email!!

            call_txt.text = my_family_DatumGurudakshina.mobile

            if (my_family_DatumGurudakshina.status == "1") {
                active_inactive_txt.text = itemView.context.getString(R.string.active_txt)
                active_inactive_txt.setTextColor(ContextCompat.getColor(itemView.context, R.color.greenColor))
                active_inactive_img.setImageResource(R.drawable.active_icon)
            } else {//if (my_family_DatumGurudakshina.status == "1") {
                active_inactive_txt.text = itemView.context.getString(R.string.inactive_txt)
                active_inactive_txt.setTextColor(ContextCompat.getColor(itemView.context, R.color.redColor))
                active_inactive_img.setImageResource(R.drawable.inactive_icon)
            } /*else {
                active_inactive_txt.text = itemView.context.getString(R.string.pending_txt)
                active_inactive_txt.setTextColor(ContextCompat.getColor(itemView.context, R.color.redColor))
                active_inactive_img.setImageResource(R.drawable.close_round_icon)
                active_inactive_img.setColorFilter(ContextCompat.getColor(itemView.context, R.color.redColor), android.graphics.PorterDuff.Mode.MULTIPLY)
            }*/

            if (my_family_DatumGurudakshina.ageCategories == itemView.context.getString(R.string.baal)) {
                active_inactive_view.setBackgroundResource(R.drawable.baal_background)
            } else if (my_family_DatumGurudakshina.ageCategories == itemView.context.getString(R.string.baalika)) {
                active_inactive_view.setBackgroundResource(R.drawable.baalika_background)
            } else if (my_family_DatumGurudakshina.ageCategories == itemView.context.getString(R.string.male_shishu)) {
                active_inactive_view.setBackgroundResource(R.drawable.male_shishu_background)
            } else if (my_family_DatumGurudakshina.ageCategories == itemView.context.getString(R.string.female_shishu)) {
                active_inactive_view.setBackgroundResource(R.drawable.female_shishu_background)
            } else if (my_family_DatumGurudakshina.ageCategories == itemView.context.getString(R.string.kishore)) {
                active_inactive_view.setBackgroundResource(R.drawable.kishor_background)
            } else if (my_family_DatumGurudakshina.ageCategories == itemView.context.getString(R.string.kishori)) {
                active_inactive_view.setBackgroundResource(R.drawable.kishori_background)
            } else if (my_family_DatumGurudakshina.ageCategories == itemView.context.getString(R.string.tarun)) {
                active_inactive_view.setBackgroundResource(R.drawable.tarun_background)
            } else if (my_family_DatumGurudakshina.ageCategories == itemView.context.getString(R.string.taruni)) {
                active_inactive_view.setBackgroundResource(R.drawable.taruni_background)
            } else if (my_family_DatumGurudakshina.ageCategories == itemView.context.getString(R.string.yuva)) {
                active_inactive_view.setBackgroundResource(R.drawable.yuva_background)
            } else if (my_family_DatumGurudakshina.ageCategories == itemView.context.getString(R.string.yuvati)) {
                active_inactive_view.setBackgroundResource(R.drawable.yuvati_background)
            } else if (my_family_DatumGurudakshina.ageCategories == itemView.context.getString(R.string.proudh)) {
                active_inactive_view.setBackgroundResource(R.drawable.proudh_background)
            } else if (my_family_DatumGurudakshina.ageCategories == itemView.context.getString(R.string.proudha)) {
                active_inactive_view.setBackgroundResource(R.drawable.proudha_background)
            }

            active_inactive.text = my_family_DatumGurudakshina.ageCategories

            righr_menu.visibility = View.GONE

            righr_menu.setOnClickListener {
                val popup = PopupMenu(itemView.context, itemView, Gravity.RIGHT)
                popup.inflate(R.menu.header_menu)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    popup.gravity = Gravity.END
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                        popup.setForceShowIcon(true)
                    }
                }
                popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
                    when (item!!.itemId) {
                        R.id.edit -> {
                            Toast.makeText(itemView.context, item.title, Toast.LENGTH_SHORT).show()
                        }
                        R.id.delete -> {
                            Toast.makeText(itemView.context, item.title, Toast.LENGTH_SHORT).show()
                        }
                    }
                    true
                })
                popup.show()
            }

            adapter_view.setOnClickListener {
//                Toast.makeText(itemView.context, "Adapter", Toast.LENGTH_SHORT).show()
            }

            call_img.setOnClickListener {
                val call: Uri = Uri.parse("tel:" + call_txt.text.toString())
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = call
                if (ActivityCompat.checkSelfPermission(itemView.context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}