package com.myhss.ui.sankhya_report.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uk.myhss.ui.my_family.Model.Datum
import java.util.*


internal class ApproveRecyclerView(
    context: Context,
    val athelets_Beans: List<Datum>,
    val arrayListUser: ArrayList<String>,
    val arrayListUserId: ArrayList<String>,
    var selectAllItems: Boolean,
    var arrayData: String
) :
    RecyclerView.Adapter<ApproveRecyclerView.sViewHolder>() {

    val mItems = mutableListOf<Datum>()
    var selectAll = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): sViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(com.uk.myhss.R.layout.add_sankhya_adapter, parent, false)
        return sViewHolder(v)
    }

    override fun onBindViewHolder(holder: sViewHolder, position: Int) {
        holder.bindItems(athelets_Beans[position], selectAllItems)
    }

    override fun getItemCount(): Int {
        return athelets_Beans.size
    }

    fun getItem(position: Int): Any {
        // TODO Auto-generated method stub
        return athelets_Beans.size
    }

    override fun getItemId(position: Int): Long {
        // TODO Auto-generated method stub
        return position.toLong()
    }

    fun updateList(list: List<Datum>) {
        mItems.addAll(list)
        notifyDataSetChanged()
    }

    fun selectAllItems() {  // selectAll: Boolean
        Log.e("onClickSelectAll", "yes")
//        for (i in 0 until checkBoxState.length) {
//            checkBoxState.get(i) = true
//        }
        selectAll = true
        selectAllItems = selectAll
        notifyDataSetChanged()
    }

    fun unselectAllItems() {   // unselectAll: Boolean
        Log.e("onClickSelectAll", "no")
        selectAll = false
        selectAllItems = selectAll
        notifyDataSetChanged()
    }

    internal inner class sViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        var dealerid: String? = null

        fun bindItems(athelets_Beans: Datum, selectAllItems: Boolean) {
            val tvname = itemView.findViewById(com.uk.myhss.R.id.tvContent) as TextView
            val checkBoxparent = itemView.findViewById(com.uk.myhss.R.id.chbContent) as CheckBox

//            checkBoxparent.isChecked = selectAllItems

            if (!selectAll) {
                checkBoxparent.isChecked = false
                arrayListUser.clear()
                arrayListUserId.clear()

                if (athelets_Beans.middleName == "") {
                    arrayListUser.remove(
                        athelets_Beans.firstName!!.capitalize(Locale.ROOT) + " " +
                                athelets_Beans.lastName!!.capitalize(Locale.ROOT)
                    )

                    arrayListUserId.remove(athelets_Beans.memberId)
                } else {
                    arrayListUser.remove(
                        athelets_Beans.firstName!!.capitalize(Locale.ROOT) + " " +
                                athelets_Beans.middleName!!.capitalize(Locale.ROOT) + " " +
                                athelets_Beans.lastName!!.capitalize(Locale.ROOT)
                    )

                    arrayListUserId.remove(athelets_Beans.memberId)
                }

//                mItems.add(ar)

                //                        arrayListUser.remove(athelets_Beans[position].getmText1())
                arrayData =
                    arrayListUser.toString().replace("[", "").replace("]", "").trim()

            } else {
                checkBoxparent.isChecked = true
                arrayListUser.clear()
                arrayListUserId.clear()

                if (athelets_Beans.middleName == "") {
                    (!arrayListUser.contains(
                        athelets_Beans.firstName!!.capitalize(Locale.ROOT) + " " +
                                athelets_Beans.lastName!!.capitalize(Locale.ROOT)
                    ))
                    arrayListUser.add(
                        athelets_Beans.firstName!!.capitalize(Locale.ROOT) + " " +
                                athelets_Beans.lastName!!.capitalize(Locale.ROOT)
                    )

                    (!arrayListUserId.contains(
                        athelets_Beans.memberId
                    ))
                    arrayListUserId.add(
                        athelets_Beans.memberId!!
                    )

                } else {
                    (!arrayListUser.contains(
                        athelets_Beans.firstName!!.capitalize(Locale.ROOT) + " " +
                                athelets_Beans.middleName!!.capitalize(Locale.ROOT) + " " +
                                athelets_Beans.lastName!!.capitalize(Locale.ROOT)
                    ))
                    arrayListUser.add(
                        athelets_Beans.firstName!!.capitalize(Locale.ROOT) + " " +
                                athelets_Beans.middleName!!.capitalize(Locale.ROOT) + " " +
                                athelets_Beans.lastName!!.capitalize(Locale.ROOT)
                    )

                    (!arrayListUserId.contains(
                        athelets_Beans.memberId
                    ))
                    arrayListUserId.add(
                        athelets_Beans.memberId!!
                    )
                }

                arrayData = arrayListUser.toString().replace("[", "").replace("]", "").trim()
            }

            if (checkBoxparent.isChecked) {

                if (athelets_Beans.middleName == "") {
                    (!arrayListUser.contains(
                        athelets_Beans.firstName!!.capitalize(Locale.ROOT) + " " +
                                athelets_Beans.lastName!!.capitalize(Locale.ROOT)
                    ))
                    arrayListUser.add(
                        athelets_Beans.firstName!!.capitalize(Locale.ROOT) + " " +
                                athelets_Beans.lastName!!.capitalize(Locale.ROOT)
                    )

                    (!arrayListUserId.contains(
                        athelets_Beans.memberId
                    ))
                    arrayListUserId.add(
                        athelets_Beans.memberId!!
                    )

                } else {
                    (!arrayListUser.contains(
                        athelets_Beans.firstName!!.capitalize(Locale.ROOT) + " " +
                                athelets_Beans.middleName!!.capitalize(Locale.ROOT) + " " +
                                athelets_Beans.lastName!!.capitalize(Locale.ROOT)
                    ))
                    arrayListUser.add(
                        athelets_Beans.firstName!!.capitalize(Locale.ROOT) + " " +
                                athelets_Beans.middleName!!.capitalize(Locale.ROOT) + " " +
                                athelets_Beans.lastName!!.capitalize(Locale.ROOT)
                    )

                    (!arrayListUserId.contains(
                        athelets_Beans.memberId
                    ))
                    arrayListUserId.add(
                        athelets_Beans.memberId!!
                    )
                }

                arrayData = arrayListUser.toString().replace("[", "").replace("]", "").trim()
            } else {

                if (athelets_Beans.middleName == "") {
                    arrayListUser.remove(
                        athelets_Beans.firstName!!.capitalize(Locale.ROOT) + " " +
                                athelets_Beans.lastName!!.capitalize(Locale.ROOT)
                    )

                    arrayListUserId.remove(athelets_Beans.memberId)
                } else {
                    arrayListUser.remove(
                        athelets_Beans.firstName!!.capitalize(Locale.ROOT) + " " +
                                athelets_Beans.middleName!!.capitalize(Locale.ROOT) + " " +
                                athelets_Beans.lastName!!.capitalize(Locale.ROOT)
                    )

                    arrayListUserId.remove(athelets_Beans.memberId)
                }

                //                        arrayListUser.remove(athelets_Beans[position].getmText1())
                arrayData =
                    arrayListUser.toString().replace("[", "").replace("]", "").trim()
            }

            if (athelets_Beans.middleName == "") {
                tvname.text = athelets_Beans.firstName!!.capitalize(Locale.ROOT) + " " +
                        athelets_Beans.lastName!!.capitalize(Locale.ROOT)
            } else {
                tvname.text = athelets_Beans.firstName!!.capitalize(Locale.ROOT) + " " +
                        athelets_Beans.middleName!!.capitalize(Locale.ROOT) + " " +
                        athelets_Beans.lastName!!.capitalize(Locale.ROOT)
            }

            //===========click listner of check box===============//
            checkBoxparent.setOnClickListener {
                val isChecked = checkBoxparent.isChecked
//                for (i in 0 until athelets_Beans) {
                if (isChecked) {

                    if (athelets_Beans.middleName == "") {
                        (!arrayListUser.contains(
                            athelets_Beans.firstName!!.capitalize(Locale.ROOT) + " " +
                                    athelets_Beans.lastName!!.capitalize(Locale.ROOT)
                        ))
                        arrayListUser.add(
                            athelets_Beans.firstName!!.capitalize(Locale.ROOT) + " " +
                                    athelets_Beans.lastName!!.capitalize(Locale.ROOT)
                        )

                        (!arrayListUserId.contains(
                            athelets_Beans.memberId
                        ))
                        arrayListUserId.add(
                            athelets_Beans.memberId!!
                        )

                    } else {
                        (!arrayListUser.contains(
                            athelets_Beans.firstName!!.capitalize(Locale.ROOT) + " " +
                                    athelets_Beans.middleName!!.capitalize(Locale.ROOT) + " " +
                                    athelets_Beans.lastName!!.capitalize(Locale.ROOT)
                        ))
                        arrayListUser.add(
                            athelets_Beans.firstName!!.capitalize(Locale.ROOT) + " " +
                                    athelets_Beans.middleName!!.capitalize(Locale.ROOT) + " " +
                                    athelets_Beans.lastName!!.capitalize(Locale.ROOT)
                        )

                        (!arrayListUserId.contains(
                            athelets_Beans.memberId
                        ))
                        arrayListUserId.add(
                            athelets_Beans.memberId!!
                        )
                    }

                    arrayData = arrayListUser.toString().replace("[", "").replace("]", "").trim()
                } else {

                    if (athelets_Beans.middleName == "") {
                        arrayListUser.remove(
                            athelets_Beans.firstName!!.capitalize(Locale.ROOT) + " " +
                                    athelets_Beans.lastName!!.capitalize(Locale.ROOT)
                        )

                        arrayListUserId.remove(athelets_Beans.memberId)
                    } else {
                        arrayListUser.remove(
                            athelets_Beans.firstName!!.capitalize(Locale.ROOT) + " " +
                                    athelets_Beans.middleName!!.capitalize(Locale.ROOT) + " " +
                                    athelets_Beans.lastName!!.capitalize(Locale.ROOT)
                        )

                        arrayListUserId.remove(athelets_Beans.memberId)
                    }

                    //                        arrayListUser.remove(athelets_Beans[position].getmText1())
                    arrayData =
                        arrayListUser.toString().replace("[", "").replace("]", "").trim()
                }
//                }
            }
        }
    }

}