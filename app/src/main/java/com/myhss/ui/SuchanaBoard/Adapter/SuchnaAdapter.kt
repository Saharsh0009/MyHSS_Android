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
import com.myhss.Utils.DebouncedClickListener
import com.myhss.Utils.Functions
import com.myhss.Utils.UtilCommon
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
) : RecyclerView.Adapter<SuchnaAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.suchna_adapter, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(suchana_data[position])
    }

    override fun getItemCount(): Int {
        return suchana_data.size
    }

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

            suchna_time.text = UtilCommon.dateAndTimeformat(suchana.created_date!!)
            suchna_title.text = suchana.suchana_title!!.capitalize(Locale.ROOT)
            suchna_discription.text = suchana.suchana_text
            suchna_discriptionnew.text = suchana.suchana_text
            if (suchana.is_read == "1") {
                suchna_discription.setTextColor(Color.GRAY)
                suchna_discriptionnew.setTextColor(Color.GRAY)
            } else {
                suchna_discription.setTextColor(Color.BLACK)
                suchna_discriptionnew.setTextColor(Color.BLACK)
            }

            suchna_adapter_view.setOnClickListener(DebouncedClickListener {
                if (eventsIsVisible) {
                    suchna_discription.visibility = View.GONE
                    redLayout.visibility = View.VISIBLE
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
                    suchna_discription.visibility = View.VISIBLE
                    redLayout.visibility = View.GONE
                    eventsIsVisible = true
                }
            })
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