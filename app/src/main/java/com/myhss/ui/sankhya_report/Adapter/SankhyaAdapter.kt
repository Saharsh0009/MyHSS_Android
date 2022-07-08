package com.uk.myhss.ui.sankhya_report.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.uk.myhss.R
import com.uk.myhss.Utils.SessionManager
import com.uk.myhss.ui.my_family.Model.Datum
import com.uk.myhss.ui.sankhya_report.Model.SelectableItem
import java.util.*
import kotlin.collections.ArrayList


class SankhyaAdapter(
    val userList: List<Datum>,
    var selectAllItems: Boolean,
    val selected_user: ArrayList<String>,
    val selected_userName: ArrayList<String>
) : RecyclerView.Adapter<SankhyaAdapter.ViewHolder>() {

    private var clickListener: ClickListener? = null

    lateinit var adapter_view: LinearLayout

    val mItems = mutableListOf<Datum>()
//    private var selectAllItems: Boolean = false
    private val selected_position = -1

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.add_sankhya_layout,
            parent,
            false
        )
        return ViewHolder(v, selectAllItems, selected_user, selected_userName)
    }

//    override fun getItemId(position: Int): Long {
//        return position.toLong()
//    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun updateList(list: List<Datum>) {
        mItems.addAll(list)
        notifyDataSetChanged()
    }

    fun selectAllItems(selectAll: Boolean){
        selectAllItems = selectAll
        notifyDataSetChanged()
    }

    fun unselectAllItems(unselectAll: Boolean){
        selectAllItems = unselectAll
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
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
    class ViewHolder(itemView: View, val selectAllItems: Boolean, val selected_user: ArrayList<String>,
    val selected_userName: ArrayList<String>) : RecyclerView.ViewHolder(itemView) {
        private var IsVisible = true
        private var IsVisiblenew = true

        val MULTI_SELECTION = 2
        val SINGLE_SELECTION = 1

        var mItem: SelectableItem? = null
        var itemSelectedListener: OnItemSelectedListener? = null

        val select: SelectableItem? = null
        private val context: Context? = null
        private lateinit var sessionManager: SessionManager

        fun bindItems(my_family_DatumGurudakshina: Datum) {
            val active_inactive_view = itemView.findViewById(R.id.active_inactive_view) as RelativeLayout
            val family_user_name = itemView.findViewById(R.id.family_user_name) as TextView
            val present_txt  = itemView.findViewById(R.id.present_txt) as TextView
            val absent_txt  = itemView.findViewById(R.id.absent_txt) as TextView
            val active_inactive_txt  = itemView.findViewById(R.id.active_inactive_txt) as TextView
            val adapter_view  = itemView.findViewById(R.id.adapter_view) as LinearLayout

            if (my_family_DatumGurudakshina.middleName == "") {
                family_user_name.text = my_family_DatumGurudakshina.firstName!!.capitalize(Locale.ROOT) + " " +
                        my_family_DatumGurudakshina.lastName!!.capitalize(Locale.ROOT)
            } else {
                family_user_name.text = my_family_DatumGurudakshina.firstName!!.capitalize(Locale.ROOT) + " " +
                        my_family_DatumGurudakshina.middleName!!.capitalize(Locale.ROOT) + " " +
                        my_family_DatumGurudakshina.lastName!!.capitalize(Locale.ROOT)
            }

            sessionManager = SessionManager(itemView.context)
            Log.d("selectAllItems", selectAllItems.toString())
            Log.d("selectAllItems==>", sessionManager.fetchSELECTED_ALL()!!)
            Log.d("selectAllItems==>", sessionManager.fetchSELECTED_TRUE()!!)
            Log.d("selectAllItems==>", sessionManager.fetchSELECTED_FALSE()!!)
            Log.d("selected_user==>", selected_user.toString())
            Log.d("selected_userName==>", selected_userName.toString())

            if (sessionManager.fetchSELECTED_ALL() == "true") {
                present_txt.setBackgroundResource(R.drawable.gree_present_round)
                present_txt.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                present_txt.tag = itemView

                absent_txt.setBackgroundResource(R.drawable.grayround_border)
                absent_txt.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.grayColorColor
                    )
                )
                absent_txt.tag = itemView
            }

            if (sessionManager.fetchSELECTED_FALSE() == "false") {
                absent_txt.setBackgroundResource(R.drawable.red_present_round)
                absent_txt.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))

                present_txt.setBackgroundResource(R.drawable.grayround_border)
                present_txt.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.grayColorColor
                    )
                )

                sessionManager.saveSELECTED_FALSE("false")
                present_txt.tag = itemView
                absent_txt.tag = itemView
            }

            if (sessionManager.fetchSELECTED_ALL() == "true") {
                present_txt.setBackgroundResource(R.drawable.gree_present_round)
                present_txt.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                present_txt.tag = itemView

                absent_txt.setBackgroundResource(R.drawable.grayround_border)
                absent_txt.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.grayColorColor
                    )
                )
                absent_txt.tag = itemView
            } else {  //if (sessionManager.fetchSELECTED_FALSE() == "false") {
                present_txt.setBackgroundResource(R.drawable.grayround_border)
                present_txt.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.grayColorColor
                    )
                )
                present_txt.tag = itemView

                absent_txt.setBackgroundResource(R.drawable.grayround_border)
                absent_txt.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.grayColorColor
                    )
                )
                absent_txt.tag = itemView
            }

            present_txt.setOnClickListener {
//                Toast.makeText(itemView.context, "Present", Toast.LENGTH_SHORT).show()

//                select!!.isSelected

                if(IsVisible) {
                    IsVisible = false
                present_txt.isSelected = selectAllItems

                present_txt.setBackgroundResource(R.drawable.gree_present_round)
                present_txt.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))

                absent_txt.setBackgroundResource(R.drawable.grayround_border)
                absent_txt.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.grayColorColor
                    )
                )

                sessionManager.saveSELECTED_TRUE("true")
                present_txt.tag = itemView
                absent_txt.tag = itemView

                selected_user.add(my_family_DatumGurudakshina.memberId!!)
                selected_userName.add(my_family_DatumGurudakshina.firstName.capitalize(Locale.ROOT) + " " +
                        my_family_DatumGurudakshina.lastName.capitalize(Locale.ROOT))

                } else {
                IsVisible = true
                present_txt.isSelected = selectAllItems

                present_txt.setBackgroundResource(R.drawable.grayround_border)
                present_txt.setTextColor(ContextCompat.getColor(itemView.context, R.color.grayColorColor))

                sessionManager.saveSELECTED_TRUE("false")
                present_txt.tag = itemView
                absent_txt.tag = itemView

                selected_user.clear()
                selected_userName.clear()
                }


                /*if(IsVisible)
                {
                    present_txt.setBackgroundResource(R.drawable.gree_present_round)
                    present_txt.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                    IsVisible = false
                }
                else if(!IsVisible)
                {
                    present_txt.setBackgroundResource(R.drawable.grayround_border)
                    present_txt.setTextColor(ContextCompat.getColor(itemView.context, R.color.grayColorColor))

                    absent_txt.setBackgroundResource(R.drawable.red_present_round)
                    absent_txt.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                    IsVisible = true
                }*/
            }

            absent_txt.visibility = View.INVISIBLE

            absent_txt.setOnClickListener {
//                Toast.makeText(itemView.context, "Absent", Toast.LENGTH_SHORT).show()

//                select!!.isSelected

                absent_txt.isSelected = selectAllItems

                absent_txt.setBackgroundResource(R.drawable.red_present_round)
                absent_txt.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))

                present_txt.setBackgroundResource(R.drawable.grayround_border)
                present_txt.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.grayColorColor
                    )
                )

                sessionManager.saveSELECTED_FALSE("true")
                present_txt.tag = itemView
                absent_txt.tag = itemView

//                selected_user.remove(my_family_DatumGurudakshina.memberId!!)
//                selected_userName.remove(my_family_DatumGurudakshina.firstName.capitalize(Locale.ROOT) + " " +
//                        my_family_DatumGurudakshina.lastName.capitalize(Locale.ROOT))

                selected_user.clear()
                selected_userName.clear()

                /*if(IsVisible)
                {
                    absent_txt.setBackgroundResource(R.drawable.red_present_round)
                    absent_txt.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                    IsVisible = false
                }
                else if(!IsVisible)
                {
                    absent_txt.setBackgroundResource(R.drawable.grayround_border)
                    absent_txt.setTextColor(ContextCompat.getColor(itemView.context, R.color.grayColorColor))

                    present_txt.setBackgroundResource(R.drawable.gree_present_round)
                    present_txt.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                    IsVisible = true
                }*/
            }


            adapter_view.setOnClickListener {
//                Toast.makeText(itemView.context, "Adapter", Toast.LENGTH_SHORT).show()
            }

        }
    }

    interface ClickListener {
        fun onItemClick(v: View, position: Int)
    }

    interface OnItemSelectedListener {
        fun onItemSelected(item: SelectableItem?)
    }

}