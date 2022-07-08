//package com.uk.myhss.Welcome
//
//import android.os.Bundle
//import android.widget.CheckBox
//import android.widget.CompoundButton
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.DefaultItemAnimator
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import butterknife.BindView
//import butterknife.ButterKnife
//import butterknife.OnCheckedChanged
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//import com.myhss.adapter.StudentAdapter
//import com.myhss.model.StudentModel
//import com.uk.myhss.R
//import java.io.BufferedReader
//import java.io.InputStream
//import java.io.InputStreamReader
//
//
////class DemoActivity : AppCompatActivity() {
////
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        setContentView(R.layout.welcome_demo)
////
////    }
////
////}
//
//class DemoActivity : AppCompatActivity() {
////    @BindView(R.id.recycler_view)
//    var recycler_view: RecyclerView? = null
//
////    @BindView(R.id.cbSelectAll)
//    var cbSelectAll: CheckBox? = null
//
////    @BindView(R.id.tvSelect)
//    var tvSelect: TextView? = null
//    var mAdapter: StudentAdapter? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.welcome_demo)
//        ButterKnife.bind(this)
//
//        tvSelect = findViewById(R.id.tvSelect)
//        cbSelectAll = findViewById(R.id.cbSelectAll)
//        recycler_view = findViewById(R.id.recycler_view)
//
//        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
//        recycler_view!!.layoutManager = mLayoutManager
//        recycler_view!!.itemAnimator = DefaultItemAnimator()
//        recycler_view!!.isNestedScrollingEnabled = false
////        mAdapter = StudentAdapter(this, prepareData())
//        //mAdapter.notifyDataSetChanged();
//        recycler_view!!.adapter = mAdapter
//    }
//
//    @OnCheckedChanged(R.id.cbSelectAll)
//    fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
//        if (mAdapter != null) {
//            mAdapter!!.toggleSelection(isChecked)
//            tvSelect!!.text = if (isChecked) "Deselect All" else "Select All"
//        }
//    }
//
//    fun prepareData(): List<StudentModel?>? {
//        try {
//            var studentList: List<StudentModel?>? = ArrayList()
//            val stream: InputStream = assets.open("Student.json")
//            val reader = BufferedReader(InputStreamReader(stream))
//            val builder = StringBuilder()
//            var line: String? = ""
//            while (reader.readLine().also { line = it } != null) {
//                builder.append(line)
//            }
//            val data = builder.toString()
//            studentList = Gson().fromJson<List<StudentModel?>>(
//                data,
//                object : TypeToken<List<StudentModel?>?>() {}.type
//            )
//            return studentList
//        } catch (e: Exception) {
//            //java.lang.IllegalStateException: Expected BEGIN_ARRAY but was BEGIN_OBJECT at line 1 column 2 path $
//            //dont keep root key...
//            e.printStackTrace()
//        }
//        return null
//    }
//}