package com.myhss.AllShakha.Adapter

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myhss.AllShakha.ShakhaDetailsActivity
import com.uk.myhss.AddMember.Get_Shakha.Datum_Get_Shakha
import com.uk.myhss.R
import com.uk.myhss.Utils.SessionManager
import java.util.*
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.net.ParseException
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import java.text.DecimalFormat

import android.widget.*
import java.lang.Exception

class ShakhaAdapter(
//    var shakha_data: List<Datum_Get_Shakha>,
//    val Currlatitude: Double,
//    val Currlongitude: Double
//    val DistanceInmiles: List<String>
) : RecyclerView.Adapter<ShakhaAdapter.ViewHolder>(), Filterable {

    private var shakha_data: List<Datum_Get_Shakha> =
        ArrayList<Datum_Get_Shakha>()

    private var shakhadata: List<Datum_Get_Shakha> =
        ArrayList<Datum_Get_Shakha>()

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.shakha_adapter, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(shakhadata[position]) //, Currlatitude, Currlongitude) // listener
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return shakhadata.size
    }

    fun getCount(): Int {
        return shakhadata.size
    }

    fun getItem(position: Int): Any? {
        return shakhadata[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setData(dataList: List<Datum_Get_Shakha>) {
        this.shakhadata = dataList
        this.shakha_data = dataList
        //Collections.sort(dataList)
        notifyDataSetChanged()
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var sessionManager: SessionManager
        private var Address: String = ""

        @RequiresApi(Build.VERSION_CODES.N)
        @SuppressLint("SetTextI18n", "ResourceAsColor")
        fun bindItems(
            shakha_list: Datum_Get_Shakha
//            Currlatitude: Double,
//            Currlongitude: Double
//            distance_miles: String
        ) {

            sessionManager = SessionManager(itemView.context)

            sessionManager.saveSEARCHVALURE(shakha_list.toString())
            Log.d("SEARCh", sessionManager.fetchSEARCHVALURE().toString())

            val shakha_title = itemView.findViewById(R.id.shakha_title) as TextView
            val shakha_address = itemView.findViewById(R.id.shakha_address) as TextView
            val shakha_contact_person = itemView.findViewById(R.id.shakha_contact_person) as TextView
            val shakha_day = itemView.findViewById(R.id.shakha_day) as TextView
            val shakha_email = itemView.findViewById(R.id.shakha_email) as TextView
            val shakha_phone = itemView.findViewById(R.id.shakha_phone) as TextView
            val shakha_distance = itemView.findViewById(R.id.shakha_distance) as TextView
            val shakha_adapter_view = itemView.findViewById(R.id.shakha_adapter_view) as LinearLayout
            val shakha_adapter_map = itemView.findViewById(R.id.shakha_adapter_map) as LinearLayout


            /*Log.d("Currlatitude", Currlatitude.toString())
            Log.d("Currlatitude", Currlongitude.toString())
            Log.d("distanceInMiles++++", shakha_list.getdistanceInMiles().toString())

            val loc1 = Location("")
            loc1.latitude = Currlatitude
            loc1.longitude = Currlongitude

            val loc2 = Location("")
            loc2.latitude = shakha_list.getLatitude()!!.toDouble()
            loc2.longitude = shakha_list.getLongitude()!!.toDouble()

            val distanceInMeters: Float = ((loc1.distanceTo(loc2)/1609.344).toFloat())
            val distanceInMiles: Float //= loc1.distanceTo(loc2)

            val mil = (loc1.distanceTo(loc2)) * 1000
//            val mil = 6357 * 1000
            distanceInMiles = (mil / 1609.344).toFloat()*/

            if (shakha_list.getBuildingName()!!.isNotEmpty()) {
                Address = shakha_list.getBuildingName()!!+", "+shakha_list.getAddressLine1()!!+", "+
                        shakha_list.getAddressLine2()!!+", "+shakha_list.getPostalCode()!!+", "+
                        shakha_list.getCity()!!+", "+shakha_list.getCountry()!!
            } else {
                Address = shakha_list.getAddressLine1()!!+", "+
                        shakha_list.getAddressLine2()!!+", "+shakha_list.getPostalCode()!!+", "+
                        shakha_list.getCity()!!+", "+shakha_list.getCountry()!!
            }

            /*val formater = DecimalFormat("#.##")
            Log.d("Distance KM", distanceInMeters.toString())
            Log.d("Distance Miles", distanceInMiles.toString())
            Log.d("Distance-->", formater.format(distanceInMiles))

            val strDouble: String = String.format("%.2f", distanceInMeters)
            Log.d("strDouble Miles", strDouble)*/

//            shakha_list.getdistanceInMiles() = strDouble

            try {
                if (shakha_list.getStartTime()!!.isNotEmpty() && shakha_list.getEndTime()!!.isNotEmpty()) {
                    val StartTime: String = shakha_list.getStartTime()!!
                    val EndTime: String = shakha_list.getEndTime()!!

                    if (StartTime != "" && EndTime != "") {
                        val start_time = StringTokenizer(StartTime)
                        val end_time = StringTokenizer(EndTime)
                        val starttime = start_time.nextToken()
                        val endtime = end_time.nextToken()

                        val sdf = SimpleDateFormat("hh:mm:ss")
                        val sdfs = SimpleDateFormat("hh:mm a")
                        val dt_starttime: Date
                        val dt_endtime: Date

                        var Start_time: String = ""
                        var End_time: String = ""
                        try {
                            dt_starttime = sdf.parse(starttime)
                            dt_endtime = sdf.parse(endtime)
                            System.out.println("Time Display: " + sdfs.format(dt_starttime)) // <-- I got result here
                            System.out.println("Time Display: " + sdfs.format(dt_endtime)) // <-- I got result here

                            Start_time = sdfs.format(dt_starttime)
                            End_time = sdfs.format(dt_endtime)
                        } catch (e: ParseException) {
                            e.printStackTrace()
                        }
                        shakha_day.text = shakha_list.getDay() + ": " + Start_time + "-" + End_time
                    }
                }
            } catch (e: Exception) {
                println("Null pointer exception")
            }

            val DIS = shakha_list.getdistanceInMiles()!!.toFloat()

            shakha_contact_person.text =
                shakha_list.getContactPersonName()!!.capitalize(Locale.ROOT)
            shakha_title.text = shakha_list.getChapterName()!!.capitalize(Locale.ROOT)
            shakha_address.text = Address.capitalize(Locale.ROOT)
            shakha_email.text = shakha_list.getEmail()
            shakha_phone.text = shakha_list.getPhone()
            if (!shakha_list.getdistanceInMiles()!!.equals("") && !shakha_list.getdistanceInMiles()!!
                    .equals(null)) {
                shakha_distance.text = DIS.toString() + " Miles"
//                        shakha_distance.text = "$strDouble Miles"
            }

            shakha_adapter_view.setOnClickListener {
//                Toast.makeText(itemView.context, "Suchana", Toast.LENGTH_SHORT).show()
                val i = Intent(itemView.context, ShakhaDetailsActivity::class.java)
                i.putExtra("Shakha_ID", shakha_list.getOrgChapterId())
                i.putExtra("Shakha_Name", shakha_list.getChapterName())
                i.putExtra("Shakha_Contact_Person", shakha_list.getContactPersonName())
                i.putExtra("Shakha_Address", Address)
                i.putExtra("Shakha_Email", shakha_list.getEmail())
                i.putExtra("Shakha_Phone", shakha_list.getPhone())
                i.putExtra("Shakha_Day", shakha_list.getDay())
                i.putExtra("Shakha_Start", shakha_list.getStartTime())
                i.putExtra("Shakha_End", shakha_list.getEndTime())
                i.putExtra("Lati", shakha_list.getLatitude())
                i.putExtra("Longi", shakha_list.getLongitude())
                i.putExtra("MAP", "")
                itemView.context.startActivity(i)
            }

            shakha_phone.setOnClickListener {
                val call: Uri = Uri.parse("tel:" + shakha_phone.text.toString())
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = call
                if (ActivityCompat.checkSelfPermission(itemView.context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    itemView.context.startActivity(intent)
                }
            }

            shakha_email.setOnClickListener {
                val to = shakha_email.text.toString()
                val subject = ""
                val message = ""

                val intent = Intent(Intent.ACTION_SEND)
                val addressees = arrayOf(to)
                intent.putExtra(Intent.EXTRA_EMAIL, addressees)
                intent.putExtra(Intent.EXTRA_SUBJECT, subject)
                intent.putExtra(Intent.EXTRA_TEXT, message)
                intent.type = "message/rfc822"
                itemView.context.startActivity(Intent.createChooser(intent, "Send Email using:"));
            }

            shakha_adapter_map.setOnClickListener {
//                Toast.makeText(itemView.context, "Go to Map", Toast.LENGTH_SHORT).show()
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr="+shakha_list.getLatitude()!!.toDouble()+","+shakha_list.getLongitude()!!.toDouble())
                )
                itemView.context.startActivity(intent)
                /*try {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(
                            "geo:" + Currlatitude
                                .toString() + "," + Currlongitude
                                .toString() + "?q=" + shakha_list.latitude!!.toDouble()
                                .toString() + "," + shakha_list.longitude!!.toDouble()
                                .toString() + "(" + label.toString() + ")"
                        )
                    )
                    intent.component = ComponentName(
                        "com.google.android.apps.maps",
                        "com.google.android.maps.MapsActivity"
                    )
                    itemView.context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    try {
                        itemView.context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=com.google.android.apps.maps")
                            )
                        )
                    } catch (anfe: ActivityNotFoundException) {
                        itemView.context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.apps.maps")
                            )
                        )
                    }
                    e.printStackTrace()
                }*/
            }
        }

        private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
            val theta = lon1 - lon2
            var dist = (Math.sin(deg2rad(lat1))
                    * sin(deg2rad(lat2))
                    + (cos(deg2rad(lat1))
                    * cos(deg2rad(lat2))
                    * cos(deg2rad(theta))))
            dist = acos(dist)
            dist = rad2deg(dist)
            dist *= 60 * 1.1515
            Log.d("dist", dist.toString())
            val mil = 6357 * 1000
            dist = mil / 1609.344
            Log.d("dist Miles", dist.toString())
                return dist //<= 40
        }

        private fun deg2rad(deg: Double): Double {
            return deg * Math.PI / 180.0
        }

        private fun rad2deg(rad: Double): Double {
            return rad * 180.0 / Math.PI
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    shakhadata //= shakha_data as List<Datum_Get_Shakha>
                } else {
                    val resultList = ArrayList<Datum_Get_Shakha>()
                    for (row in shakhadata) {
                        if (row.getChapterName()!!.toLowerCase().contains(constraint.toString().toLowerCase()) ||
                            row.getBuildingName()!!.toLowerCase().contains(constraint.toString().toLowerCase()) ||
                            row.getAddressLine1()!!.toLowerCase().contains(constraint.toString().toLowerCase()) ||
                            row.getAddressLine2()!!.toLowerCase().contains(constraint.toString().toLowerCase()) ||
                            row.getPostalCode()!!.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            resultList.add(row)
                        }
                    }
                    shakhadata = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = shakhadata
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                shakhadata = results?.values as ArrayList<Datum_Get_Shakha>
                notifyDataSetChanged()
            }
        }
    }


    // Filter Class
    /*fun filterDateRange(charText: Date, charText1: Date) {
//        shakha_data.clear()
        if (charText.equals("") || charText.equals(null)) {
//            ticketList.addAll(filteredTicketList)
            shakha_data
        } else {
            for (wp in shakha_data) {
//                val sdf = SimpleDateFormat("yyyy-MM-dd")
                try {
//                    val strDate = sdf.parse(wp.getTicketLoggedTime())
                    if (charText1.after(wp.distanceInMiles) && charText.before(strDate)) {
//                        shakha_data.add(wp)
                    } else {
                    }
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }
        }
        notifyDataSetChanged()
    }*/
}