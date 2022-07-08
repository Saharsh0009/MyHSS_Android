package com.myhss.ui.sankhya_report.Model

import android.app.ListActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Toast
import com.uk.myhss.R

class AddCheckbox : ListActivity() {
    /** String array used as the datasource for the ArrayAdapter of the listview  */
    var countries = arrayOf(
        "India",
        "Pakistan",
        "Sri Lanka",
        "Bangladesh",
        "China",
        "Afghanistan"
    )

    var arrayListUserId: ArrayList<String> = ArrayList()

    /** Called when the activity is first created.  */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_check)
        /** Defining array adapter to store items for the listview  */
        val adapter = ArrayAdapter(this, R.layout.simple_list_item_multiple_choice, countries)
        /** Setting the arrayadapter for this listview   */
        listView.adapter = adapter
        /** Defining checkbox click event listener  */
        val clickListener =
            View.OnClickListener { v ->
                arrayListUserId.clear()
                val chk = v as CheckBox
                val itemCount = listView.count
                for (i in 0 until itemCount) {
                    listView.setItemChecked(i, chk.isChecked)
                    arrayListUserId.add(i.toString())
                }
                Log.d("Value", arrayListUserId.toString())
            }

        /** Defining click event listener for the listitem checkbox  */
        val itemClickListener =
            OnItemClickListener { arg0, arg1, arg2, arg3 ->
                arrayListUserId.clear()
                val selectedItem = arg2
                arrayListUserId.add(selectedItem.toString())
                Log.d("Value", arrayListUserId.toString())
                val chk = findViewById<View>(R.id.chkAll) as CheckBox
                val checkedItemCount = checkedItemCount
                if (listView.count == checkedItemCount) chk.isChecked =
                    true else chk.isChecked = false
            }

        /** Getting reference to checkbox available in the main.xml layout  */
        val chkAll = findViewById<View>(R.id.chkAll) as CheckBox
        /** Setting a click listener for the checkbox  */
        chkAll.setOnClickListener(clickListener)
        /** Setting a click listener for the listitem checkbox  */
        listView.onItemClickListener = itemClickListener
    }

    /**
     *
     * Returns the number of checked items
     */
    private val checkedItemCount: Int
        private get() {
            var cnt = 0
            val positions = listView.checkedItemPositions
            val itemCount = listView.count
            for (i in 0 until itemCount) {
                if (positions[i]) cnt++
            }
            return cnt
        }
}