package com.myhss.ui.SuchanaBoard.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myhss.Utils.DebouncedClickListener
import com.uk.myhss.R
import com.uk.myhss.Utils.SessionManager
import java.util.*


class NotificationAdapter(val userList: List<String>, val tab_type: String) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    val suchna_discriptio = ArrayList<String>()

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.suchna_adapter, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        suchna_discriptio.add("New membership application added successfully in Test Shakha.")
        suchna_discriptio.add("Membership successfully updated.")
        suchna_discriptio.add("One-Time Dakshina successfully done by user.")
        suchna_discriptio.add("Added Sankhya successfully.")

        holder.bindItems(userList[position], suchna_discriptio[position], tab_type) // listener
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return 4
//        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var sessionManager: SessionManager
        private var eventsIsVisible = true
        var b: Boolean = true

        @SuppressLint("SetTextI18n", "ResourceAsColor")
        fun bindItems(
            my_family_DatumGurudakshina: String,
            suchna_discriptio: String,
            tab_type: String
        ) { // listener: OnItemClickListener

            sessionManager = SessionManager(itemView.context)

            val sharedPreferences = itemView.context.getSharedPreferences(
                "production",
                Context.MODE_PRIVATE
            )

            val suchna_title = itemView.findViewById(R.id.suchna_title) as TextView
            val suchna_discription = itemView.findViewById(R.id.suchna_discription) as TextView

            val suchna_discriptionnew =
                itemView.findViewById(R.id.suchna_discriptionnew) as TextView

            val suchna_adapter_view =
                itemView.findViewById(R.id.suchna_adapter_view) as LinearLayout
            val redLayout = itemView.findViewById(R.id.redLayout) as LinearLayout

//            sessionManager.saveNOTIFICATION_READ(false)

            /*if (sharedPreferences.getString("READ", "") != "") {
                if (sharedPreferences.getString("READ", "true") == "") {
                    suchna_discription.typeface = Typeface.DEFAULT_BOLD
                } else {
                    suchna_discription.typeface = Typeface.DEFAULT
                }
            }*/

            /*if (b == true) {
                suchna_discription.typeface = Typeface.DEFAULT_BOLD
            } else{
                suchna_discription.typeface = Typeface.DEFAULT
            }*/
//            if (sessionManager.fetchNOTIFICATION_READ() != "") {
//                if (sessionManager.fetchNOTIFICATION_READ() == "1") {
//                    suchna_discription.typeface = Typeface.DEFAULT_BOLD
//                } /*else if (sessionManager.fetchNOTIFICATION_READ() == true) {
//                suchna_discription.typeface = Typeface.DEFAULT
//            }*/
//            }

            suchna_title.text = my_family_DatumGurudakshina.capitalize(Locale.ROOT)
            /*if (my_family_DatumGurudakshina.middleName != "") {
                suchna_title.text = my_family_DatumGurudakshina.firstName!!.capitalize(Locale.ROOT) + " " +
                        my_family_DatumGurudakshina.middleName!!.capitalize(Locale.ROOT) + " " +
                        my_family_DatumGurudakshina.lastName!!.capitalize(Locale.ROOT)
            } else {
                suchna_title.text = my_family_DatumGurudakshina.firstName!!.capitalize(Locale.ROOT) + " " +
                        my_family_DatumGurudakshina.lastName!!.capitalize(Locale.ROOT)
            }*/
            suchna_discription.text = suchna_discriptio
//                "Kotlin is a cross-platform, statically typed, general-purpose programming language with type inference. Kotlin is designed to interoperate fully with Java, and the JVM version of Kotlin's standard library depends on the Java Class Library, but type inference allows its syntax to be more concise"

            suchna_discriptionnew.text = suchna_discriptio
//                "Kotlin is a cross-platform, statically typed, general-purpose programming language with type inference. Kotlin is designed to interoperate fully with Java, and the JVM version of Kotlin's standard library depends on the Java Class Library, but type inference allows its syntax to be more concise. Kotlin is a cross-platform, statically typed, general-purpose programming language with type inference. Kotlin is designed to interoperate fully with Java, and the JVM version of Kotlin's standard library depends on the Java Class Library, but type inference allows its syntax to be more concise."

            suchna_adapter_view.setOnClickListener(DebouncedClickListener {
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
//                    b = true

                    sharedPreferences.edit().apply {
                        putString("READ", "true")
                    }.apply()

                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString("READ", "true")
                    editor.apply()
                    editor.commit()
                } else {
//                    sessionManager.saveNOTIFICATION_READ("0")
                    suchna_discription.visibility = View.VISIBLE
                    redLayout.visibility = View.GONE
//                    slideDown(redLayout)
                    eventsIsVisible = true
                }
            })
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
    }
}