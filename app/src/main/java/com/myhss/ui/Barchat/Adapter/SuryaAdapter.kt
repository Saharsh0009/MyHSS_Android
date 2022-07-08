package com.myhss.ui.Barchat.Adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.myhss.ui.Barchat.Model.Datum_Get_SuryaNamaskar
import com.uk.myhss.R
import com.uk.myhss.Utils.SessionManager
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class SuryaAdapter(var surya_namaskarlist: List<Datum_Get_SuryaNamaskar>) : RecyclerView.Adapter<SuryaAdapter.ViewHolder>() {

//    private var shakha_data: List<Datum_Get_SuryaNamaskar> =
//        ArrayList<Datum_Get_SuryaNamaskar>()

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.surya_namasakar_item, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(surya_namaskarlist[position]) //, Currlatitude, Currlongitude) // listener
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return surya_namaskarlist.size
    }

    fun getCount(): Int {
        return surya_namaskarlist.size
    }

    fun getItem(position: Int): Any? {
        return surya_namaskarlist[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setData(dataList: List<Datum_Get_SuryaNamaskar>) {
        this.surya_namaskarlist = dataList
        notifyDataSetChanged()
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var sessionManager: SessionManager
        private var Address: String = ""
        val sdf: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        @RequiresApi(Build.VERSION_CODES.N)
        @SuppressLint("SetTextI18n", "ResourceAsColor")
        fun bindItems(
            shakha_list: Datum_Get_SuryaNamaskar
        ) {

            sessionManager = SessionManager(itemView.context)

            val suryanamaskar_count = itemView.findViewById(R.id.suryanamaskar_count) as TextView
            val suryanamaskar_date = itemView.findViewById(R.id.suryanamaskar_date) as TextView

//            val dateFormated = SimpleDateFormat("dd/MM/yyyy").format(shakha_list.getcount_date())

            val fmt = "yyyy-MM-dd"
            val df: DateFormat = SimpleDateFormat(fmt)

            val dt = df.parse(shakha_list.getcount_date())

            val dfmt: DateFormat = SimpleDateFormat("dd/MM/yyyy")

            val dateOnly = dfmt.format(dt)
            suryanamaskar_count.text = "Count: "+shakha_list.getcount()
            suryanamaskar_date.text = "Date: "+dateOnly

        }
    }
}