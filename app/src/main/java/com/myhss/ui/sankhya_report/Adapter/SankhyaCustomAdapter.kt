package com.uk.myhss.ui.sankhya_report.Adapter

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.*
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.uk.myhss.R
import com.uk.myhss.ui.sankhya_report.Model.Sankhya_Datum
import com.uk.myhss.ui.sankhya_report.SankhyaDetail
import java.util.*
import kotlin.collections.ArrayList


class SankhyaCustomAdapter(var userList: List<Sankhya_Datum>) : RecyclerView.Adapter<SankhyaCustomAdapter.ViewHolder>(), Filterable {

    lateinit var adapter_view: LinearLayout
    private val context: Context? = null
    var countryFilterList = ArrayList<Sankhya_Datum>()

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.sankhya_adapter, parent, false)
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
        fun bindItems(my_family_DatumGurudakshina: Sankhya_Datum) {
            val active_inactive_view = itemView.findViewById(R.id.active_inactive_view) as RelativeLayout
            val family_name = itemView.findViewById(R.id.family_name) as TextView
            val family_address  = itemView.findViewById(R.id.family_address) as TextView
//            val active_inactive_txt  = itemView.findViewById(R.id.active_inactive_txt) as TextView
            val email_txt  = itemView.findViewById(R.id.email_txt) as TextView
            val call_txt  = itemView.findViewById(R.id.call_txt) as TextView
            val email_img  = itemView.findViewById(R.id.email_img) as ImageView
            val call_img  = itemView.findViewById(R.id.call_img) as ImageView
//            val active_inactive_img  = itemView.findViewById(R.id.active_inactive_img) as ImageView
            val righr_menu  = itemView.findViewById(R.id.righr_menu) as ImageView
            val adapter_view  = itemView.findViewById(R.id.adapter_view) as LinearLayout

            family_name.text = my_family_DatumGurudakshina.chapterName!!.capitalize(Locale.ROOT)
            family_address.text = my_family_DatumGurudakshina.eventDate!!.capitalize(Locale.ROOT)
            call_txt.text = my_family_DatumGurudakshina.phone!!
            email_txt.text = my_family_DatumGurudakshina.email!!

            active_inactive_view.visibility = View.GONE

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
//                itemView.context.startActivity(Intent(itemView.context, SankhyaDetail::class.java))
                val i = Intent(itemView.context, SankhyaDetail::class.java)
                i.putExtra("SANKHYA", "SANKHYA")
                i.putExtra("SANKHYA_ID", my_family_DatumGurudakshina.id)
                itemView.context.startActivity(i)
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

        /*private fun showPopup() {
            val popup = PopupMenu(context, view)
            popup.inflate(R.menu.header_menu)

            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

                when (item!!.itemId) {
                    R.id.edit -> {
                        Toast.makeText(adapter_view, item.title, Toast.LENGTH_SHORT).show()
                    }
                    R.id.delete -> {
                        Toast.makeText(adapter_view, item.title, Toast.LENGTH_SHORT).show()
                    }
                }

                true
            })

            popup.show()
        }*/
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    countryFilterList = userList as ArrayList<Sankhya_Datum>
                } else {
                    val resultList = ArrayList<Sankhya_Datum>()
                    for (row in userList) {
                        if (row.chapterName!!.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            resultList.add(row)
                        } else if (row.email!!.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            resultList.add(row)
                        } else if (row.phone!!.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            resultList.add(row)
                        } else if (row.utsav!!.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            resultList.add(row)
                        }
                    }
                    countryFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = countryFilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                countryFilterList = results?.values as ArrayList<Sankhya_Datum>
                notifyDataSetChanged()
            }
        }
    }
}