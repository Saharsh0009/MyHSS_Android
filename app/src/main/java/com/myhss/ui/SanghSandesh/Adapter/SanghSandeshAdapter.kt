package com.myhss.ui.SanghSandesh.Adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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


class SanghSandeshAdapter(val suchana_data: List<Get_Suchana_Datum>, val userList: List<String>, val tab_type: String) : RecyclerView.Adapter<SanghSandeshAdapter.ViewHolder>() {

    val suchna_discriptio = ArrayList<String>()

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.sangh_sandesh_adapter, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        suchna_discriptio.add("<p>Namaste,</p>\n" +
                "\n" +
                "<p>As you may be aware that for the last few years, Hindus across the globe, particularly in the West are facing discrimination, harassment and victimisation by forces which promote Hindu hatred and misinformation on Hinduism and Hindutva. The forthcoming international conference titled &quot;Dismantle Global Hindutva&quot; is one of the many international conspiracies to undermine and damage Hindutva movement for which we all are committed. They project and propagate Hindutva as extremist and violent ideology which is outright falsehood.</p>\n" +
                "\n" +
                "<p>In order that Hindus and well wishers of Hindu community understand the true meaning of Hindutva and the prevalence of Hinduphobia in many places, India Knowledge (INK) has organised a conference titled &quot;Understanding Hindutva and Hinduphobia&quot; addressed by reputed scholars from across the globe. I would urge all pariwar organizations to encourage their members to register in large numbers and participate.</p>\n" +
                "\n" +
                "<p>Let us make this conference a great success and show our solidarity with Hindutva.</p>\n" +
                "\n" +
                "<p>Please register at https://www.indiaknowledge.org/understanding-hindutva-and-hinduophobia</p>\n" +
                "\n" +
                "<p>Pranaam</p>\n" +
                "\n" +
                "<p>Dhiraj D Shah</p>\n" +
                "\n" +
                "<p>Sanghachalak, HSS (UK)</p>")
        suchna_discriptio.add("<p>Namaste,</p>\n" +
                "\n" +
                "<p>As you may be aware that for the last few years, Hindus across the globe, particularly in the West are facing discrimination, harassment and victimisation by forces which promote Hindu hatred and misinformation on Hinduism and Hindutva. The forthcoming international conference titled &quot;Dismantle Global Hindutva&quot; is one of the many international conspiracies to undermine and damage Hindutva movement for which we all are committed. They project and propagate Hindutva as extremist and violent ideology which is outright falsehood.</p>\n" +
                "\n" +
                "<p>In order that Hindus and well wishers of Hindu community understand the true meaning of Hindutva and the prevalence of Hinduphobia in many places, India Knowledge (INK) has organised a conference titled &quot;Understanding Hindutva and Hinduphobia&quot; addressed by reputed scholars from across the globe. I would urge all pariwar organizations to encourage their members to register in large numbers and participate.</p>\n" +
                "\n" +
                "<p>Let us make this conference a great success and show our solidarity with Hindutva.</p>\n" +
                "\n" +
                "<p>Please register at https://www.indiaknowledge.org/understanding-hindutva-and-hinduophobia</p>\n" +
                "\n" +
                "<p>Pranaam</p>\n" +
                "\n" +
                "<p>Dhiraj D Shah</p>\n" +
                "\n" +
                "<p>Sanghachalak, HSS (UK)</p>")

//        holder.bindItems(suchana_data[position]) // listener
        holder.bindItems(suchana_data[position], userList[position], suchna_discriptio[position], tab_type) // listener
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return 2
//        return suchana_data.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var sessionManager: SessionManager
        private var eventsIsVisible = true

        @SuppressLint("SetTextI18n", "ResourceAsColor")
        fun bindItems(suchana: Get_Suchana_Datum, userList: String, suchna_discriptio: String, tab_type: String) {

            sessionManager = SessionManager(itemView.context)

            val sangh_sandesh_pic = itemView.findViewById(R.id.sangh_sandesh_pic) as ImageView
            val suchna_title = itemView.findViewById(R.id.suchna_title) as TextView
            val suchna_time = itemView.findViewById(R.id.suchna_time) as TextView
            val suchna_discription  = itemView.findViewById(R.id.suchna_discription) as TextView

            val suchna_discriptionnew  = itemView.findViewById(R.id.suchna_discriptionnew) as TextView

            val suchna_adapter_view  = itemView.findViewById(R.id.suchna_adapter_view) as LinearLayout
            val redLayout  = itemView.findViewById(R.id.redLayout) as LinearLayout

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

//            Glide.with(itemView)
//                .load(MyHssApplication.IMAGE_PDF_URL + sessionManager.fetchQUALIFICATION_FILE()).into(
//                    sangh_sandesh_pic)

            suchna_title.text = userList.capitalize(Locale.ROOT)

            val sangh_Sandesh: String = getColoredSpanned(suchna_discriptio, "#FF000000")!!

            suchna_discription.text = Html.fromHtml(sangh_Sandesh)

            suchna_discriptionnew.text = Html.fromHtml(sangh_Sandesh)

            if (suchana.is_read == "1") {
                suchna_discription.setTextColor(Color.GRAY)
                suchna_discriptionnew.setTextColor(Color.GRAY)
            } else {
                suchna_discription.setTextColor(Color.BLACK)
                suchna_discriptionnew.setTextColor(Color.BLACK)
            }

            suchna_adapter_view.setOnClickListener {
                if (eventsIsVisible) {
                    suchna_discription.visibility = View.GONE
                    redLayout.visibility = View.VISIBLE
//                    slideUp(redLayout)
                    eventsIsVisible = false

//                    if (suchana.is_read == "0") {
//                        if (sessionManager.fetchSHAKHAID() != "") {
//                            if (Functions.isConnectingToInternet(itemView.context)) {
//                                val MEMBERID: String = sessionManager.fetchMEMBERID()!!
//                                val SUCHANAID: String = suchana.id!!
//
//                                mySuchanaSeen(MEMBERID, SUCHANAID)
//                            } else {
//                                Toast.makeText(
//                                    itemView.context,
//                                    itemView.context.resources.getString(R.string.no_connection),
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        }
//                    }
                } else {
                    suchna_discription.visibility = View.VISIBLE
                    redLayout.visibility = View.GONE
//                    slideDown(redLayout)
                    eventsIsVisible = true
                }
            }
        }

        open fun getColoredSpanned(text: String, color: String): String? {
            return "<font color=$color>$text</font>"
        }

        // slide the view from below itself to the current position
        fun slideUp(view: View) {
            view.visibility = View.VISIBLE
            val animate = TranslateAnimation(
                0F,  // fromXDelta
                0F,  // toXDelta
                view.height.toFloat(),  // fromYDelta
                0F
            ) // toYDelta
            animate.duration = 500
            animate.fillAfter = true
            view.startAnimation(animate)
        }

        // slide the view from its current position to below itself
        fun slideDown(view: View) {
            view.visibility = View.GONE
            val animate = TranslateAnimation(
                0F,  // fromXDelta
                0F,  // toXDelta
                0F,  // fromYDelta
                view.height.toFloat()
            ) // toYDelta
            animate.duration = 500
            animate.fillAfter = true
            view.startAnimation(animate)
        }

        private fun mySuchanaSeen(
            member_id: String,
            suchana_id: String
        ) {
            val pd = CustomProgressBar(itemView.context)
            pd.show()
            val call: Call<Get_Suchana_Seen_Response> =
                MyHssApplication.instance!!.api.get_seen_suchana_by_member(member_id, suchana_id)
            call.enqueue(object : Callback<Get_Suchana_Seen_Response> {
                override fun onResponse(
                    call: Call<Get_Suchana_Seen_Response>,
                    response: Response<Get_Suchana_Seen_Response>
                ) {
                    if (response.code() == 200 && response.body() != null) {
                        Log.d("status", response.body()?.status.toString())
                        if (response.body()?.status!!) {

                            var suchana_seen: List<Get_Suchana_Seen_Response> =
                                ArrayList<Get_Suchana_Seen_Response>()

                            try {
//                                Functions.showAlertMessageWithOK(
//                                    itemView.context, "",
//                                    response.body()?.message
//                                )
//                                suchana_seen = response.body()!!.message.toString()
                                Log.d("suchana_seen", response.body()!!.message.toString())

                            } catch (e: ArithmeticException) {
                                println(e)
                            } finally {
                                println("Family")
                            }
                        } else {
                            Functions.displayMessage(itemView.context,response.body()?.message)
//                            Functions.showAlertMessageWithOK(
//                                itemView.context, "",
////                        "Message",
//                                response.body()?.message
//                            )
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