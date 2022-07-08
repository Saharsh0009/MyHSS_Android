package com.uk.myhss.ui.my_family.Adapter

import android.content.Context
import android.content.Intent
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.uk.myhss.R
import com.uk.myhss.ui.guru_dakshina.GuruDakshinaRegularDetail
import com.uk.myhss.ui.my_family.Model.Datum_guru_dakshina
import java.util.*


class GuruCustomAdapter(val userList: List<Datum_guru_dakshina>) :
    RecyclerView.Adapter<GuruCustomAdapter.ViewHolder>() {

    //    lateinit var adapter_view: LinearLayout
    private val context: Context? = null

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.guru_dakshina_layout, parent, false)
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

    fun filter(p0: String?): String? {
//        TODO("Not yet implemented")
        return p0
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(my_family_DatumGurudakshina: Datum_guru_dakshina) {
            val username_name = itemView.findViewById(R.id.username_name) as TextView
            val amount_txt = itemView.findViewById(R.id.amount_txt) as TextView
            val regular_onetime_type = itemView.findViewById(R.id.regular_onetime_type) as TextView
            val regular_onetime_id = itemView.findViewById(R.id.regular_onetime_id) as TextView
            val user_shakha_type = itemView.findViewById(R.id.user_shakha_type) as TextView
            val date_txt = itemView.findViewById(R.id.date_txt) as TextView
            val righr_menu = itemView.findViewById(R.id.righr_menu) as ImageView
            val regular_onetime_layout =
                itemView.findViewById(R.id.regular_onetime_layout) as LinearLayout
            val adapter_view = itemView.findViewById(R.id.adapter_view) as LinearLayout

            if (my_family_DatumGurudakshina.dakshina == "Regular") {
                regular_onetime_layout.setBackgroundResource(R.drawable.regular_layout)
                regular_onetime_id.visibility = View.VISIBLE
            } else if (my_family_DatumGurudakshina.dakshina == "One-Time") {
                regular_onetime_layout.setBackgroundResource(R.drawable.onetime_layout)
                regular_onetime_id.visibility = View.INVISIBLE
            }

            username_name.text =
                my_family_DatumGurudakshina.firstName!!.capitalize(Locale.ROOT) + " " + my_family_DatumGurudakshina.lastName!!.capitalize(Locale.ROOT)

            /*if (my_family_DatumGurudakshina.middleName == "") {
                username_name.text =
                    my_family_DatumGurudakshina.firstName!!.capitalize(Locale.ROOT) + " " + my_family_DatumGurudakshina.lastName!!.capitalize(Locale.ROOT)
            } else {
                username_name.text =
                    my_family_DatumGurudakshina.firstName!!.capitalize(Locale.ROOT) + " " + my_family_DatumGurudakshina.middleName!!.capitalize(Locale.ROOT) + " " + my_family_DatumGurudakshina.lastName!!.capitalize(Locale.ROOT)
            }*/
            user_shakha_type.text = my_family_DatumGurudakshina.chapterName!!.capitalize(Locale.ROOT)
            amount_txt.text = "\u00a3" + my_family_DatumGurudakshina.paidAmount
            regular_onetime_type.text = my_family_DatumGurudakshina.dakshina
            regular_onetime_id.text = my_family_DatumGurudakshina.orderId
            date_txt.text = my_family_DatumGurudakshina.startDate

            righr_menu.visibility = View.GONE

            righr_menu.setOnClickListener {
                val popup = PopupMenu(itemView.context, itemView, Gravity.RIGHT)
                popup.inflate(R.menu.header_menu)
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
                val i =
                    Intent(itemView.context, GuruDakshinaRegularDetail::class.java)
                i.putExtra("username_name", username_name.text.toString())
                i.putExtra("user_shakha_type", user_shakha_type.text.toString())
                i.putExtra("amount_txt", amount_txt.text.toString())
                i.putExtra("regular_onetime_type", regular_onetime_type.text.toString())
                i.putExtra("regular_onetime_id", regular_onetime_id.text.toString())
                i.putExtra("date_txt", date_txt.text.toString())
                i.putExtra("status", my_family_DatumGurudakshina.status)
                i.putExtra("txnId", my_family_DatumGurudakshina.txnId)
                i.putExtra("giftAid", my_family_DatumGurudakshina.giftAid)
                i.putExtra("order_id", my_family_DatumGurudakshina.orderId)
                i.putExtra("dakshina", my_family_DatumGurudakshina.dakshina)
                i.putExtra("recurring", my_family_DatumGurudakshina.recurring)
                itemView.context.startActivity(i)
            }
        }
    }
}