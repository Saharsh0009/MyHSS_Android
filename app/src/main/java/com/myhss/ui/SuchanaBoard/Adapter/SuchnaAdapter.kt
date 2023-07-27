package com.myhss.ui.SuchanaBoard.Adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.myhss.Utils.CustomProgressBar
import com.myhss.Utils.Functions
import com.myhss.ui.SuchanaBoard.Model.Get_Suchana_Datum
import com.myhss.ui.SuchanaBoard.Model.Get_Suchana_Seen_Response
import com.uk.myhss.R
import com.uk.myhss.Restful.MyHssApplication
import com.uk.myhss.Utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class SuchnaAdapter(
    val suchana_data: List<Get_Suchana_Datum>,
    val userList: List<String>,
    val tab_type: String
) : RecyclerView.Adapter<SuchnaAdapter.ViewHolder>() {

    val suchna_discriptio = ArrayList<String>()

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.suchna_adapter, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        suchna_discriptio.add("Guru Purnima is celebrated with a lot of enthusiasm every year across the country. This year, the occasion of Guru Purnima is being observed on July 5 coinciding with ‘Upachaya Chandra Grahan’ or the Penumbral lunar eclipse. In Hinduism, Jainism, Buddhism, guru is considered of utmost respect. Guru inculcates values, shows the path and gives experiential knowledge, one who helps guide a student's spiritual development.")
        suchna_discriptio.add("May God bless you with a long and prosperous life! Thank you so much, (sister's name), for making my life beautiful, tolerating me and keeping all my secrets. Happy Raksha Bandhan! I love you sister till death and will always be one call away in all your needs.")
        suchna_discriptio.add("May this Makar Sankranti fill your life with joy, happiness, and love. Wishing you and your family a very Happy Makar Sankranti. On this occasion of Makar Sankranti, may God bless you with good health and wealth. On this auspicious day of Makar Sankranti, I wish you are blessed with happiness, peace, and prosperity.")
        suchna_discriptio.add(
            "Happy Dussehra 2020 Wishes Images, Quotes, Status, Messages, Photos: Dussehra, known as Vijayadashmi, brings the five-day festivities of Durga Puja to a close. The day also marks Lord Ram’s triumph over the evil King Ravana. As per custom effigies of the latter are burnt to signify the same.\n" +
                    "\n" +
                    "In different parts of the country devotees prepare to bid farewell to the goddess. This year it falls on October 25. Even though things are slightly different this year, celebrate the day by exchanging these wishes."
        )

        holder.bindItems(suchana_data[position]) // listener
//        holder.bindItems(suchana_data[position], userList[position], suchna_discriptio[position], tab_type) // listener
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return suchana_data.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var sessionManager: SessionManager
        private var eventsIsVisible = true

        @SuppressLint("SetTextI18n", "ResourceAsColor")
        fun bindItems(suchana: Get_Suchana_Datum) {

            sessionManager = SessionManager(itemView.context)

            val suchna_title = itemView.findViewById(R.id.suchna_title) as TextView
            val suchna_time = itemView.findViewById(R.id.suchna_time) as TextView
            val suchna_discription = itemView.findViewById(R.id.suchna_discription) as TextView

            val suchna_discriptionnew =
                itemView.findViewById(R.id.suchna_discriptionnew) as TextView

            val suchna_adapter_view =
                itemView.findViewById(R.id.suchna_adapter_view) as LinearLayout
            val redLayout = itemView.findViewById(R.id.redLayout) as LinearLayout

            val str = suchana.created_date

            val fmt = "yyyy-MM-dd HH:mm:ss"
            val df: DateFormat = SimpleDateFormat(fmt)

            val dt = df.parse(str)

            val tdf: DateFormat = SimpleDateFormat("HH:mm a")
            val dfmt: DateFormat = SimpleDateFormat("dd/MM/yyyy")

            val timeOnly = tdf.format(dt)
            val dateOnly = dfmt.format(dt)
            System.out.println("Date: $dateOnly")
            System.out.println("Time: $timeOnly")
            suchna_time.text = dateOnly

            suchna_title.text = suchana.suchana_title!!.capitalize(Locale.ROOT)
            /*if (my_family_DatumGurudakshina.middleName != "") {
                suchna_title.text = my_family_DatumGurudakshina.firstName!!.capitalize(Locale.ROOT) + " " +
                        my_family_DatumGurudakshina.middleName!!.capitalize(Locale.ROOT) + " " +
                        my_family_DatumGurudakshina.lastName!!.capitalize(Locale.ROOT)
            } else {
                suchna_title.text = my_family_DatumGurudakshina.firstName!!.capitalize(Locale.ROOT) + " " +
                        my_family_DatumGurudakshina.lastName!!.capitalize(Locale.ROOT)
            }*/
            suchna_discription.text = suchana.suchana_text

            suchna_discriptionnew.text = suchana.suchana_text

            if (suchana.is_read == "1") {
                suchna_discription.setTextColor(Color.GRAY)
                suchna_discriptionnew.setTextColor(Color.GRAY)
            } else {
                suchna_discription.setTextColor(Color.BLACK)
                suchna_discriptionnew.setTextColor(Color.BLACK)
            }

            suchna_adapter_view.setOnClickListener {
//                Toast.makeText(itemView.context, "Suchana", Toast.LENGTH_SHORT).show()
                /*val i = Intent(itemView.context, SuchanaDetails::class.java)
                i.putExtra("Suchana_Type", tab_type)
                i.putExtra("Suchana_Title", suchna_title.text.toString())
                i.putExtra("Suchana_Discription", suchna_discription.text.toString())
                itemView.context.startActivity(i)*/
                if (eventsIsVisible) {
//                    sessionManager.saveNOTIFICATION_READ("1")
                    suchna_discription.visibility = View.GONE
                    redLayout.visibility = View.VISIBLE
//                    slideUp(redLayout)
                    eventsIsVisible = false

                    if (suchana.is_read == "0") {
                        if (sessionManager.fetchSHAKHAID() != "") {
                            if (Functions.isConnectingToInternet(itemView.context)) {
                                val MEMBERID: String = sessionManager.fetchMEMBERID()!!
                                val SUCHANAID: String = suchana.id!!

                                mySuchanaSeen(
                                    MEMBERID,
                                    SUCHANAID,
                                    suchna_discription,
                                    suchna_discriptionnew
                                )
                                suchana.is_read = "1"
                            } else {
                                Toast.makeText(
                                    itemView.context,
                                    itemView.context.resources.getString(R.string.no_connection),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } else {
//                    sessionManager.saveNOTIFICATION_READ("0")
                    suchna_discription.visibility = View.VISIBLE
                    redLayout.visibility = View.GONE
//                    slideDown(redLayout)
                    eventsIsVisible = true
                }
            }
        }

        private fun mySuchanaSeen(
            member_id: String,
            suchana_id: String,
            suchna_discription: TextView,
            suchna_discriptionnew: TextView,
        ) {
            val pd = CustomProgressBar(itemView.context)
            pd.show()
            val call: Call<Get_Suchana_Seen_Response> =
                MyHssApplication.instance!!.api.get_seen_suchana_by_member(suchana_id, member_id)
            call.enqueue(object : Callback<Get_Suchana_Seen_Response> {
                override fun onResponse(
                    call: Call<Get_Suchana_Seen_Response>,
                    response: Response<Get_Suchana_Seen_Response>
                ) {
                    if (response.code() == 200 && response.body() != null) {
                        Log.d("status", response.body()?.status.toString())
                        if (response.body()?.status!!) {
                            suchna_discription.setTextColor(Color.GRAY)
                            suchna_discriptionnew.setTextColor(Color.GRAY)
                        } else {
                            Functions.displayMessage(itemView.context, response.body()?.message)
                        }
                    } else {
                        Functions.showAlertMessageWithOK(
                            itemView.context, "Message",
                            itemView.context.getString(R.string.some_thing_wrong),
                        )
                    }
                    pd.dismiss()
                }

                override fun onFailure(call: Call<Get_Suchana_Seen_Response>, t: Throwable) {
                    Toast.makeText(itemView.context, t.message, Toast.LENGTH_LONG).show()
                    pd.dismiss()
                }
            })
        }
    }
}