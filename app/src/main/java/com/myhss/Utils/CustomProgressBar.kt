//package com.uk.myhss.Utils
//
//import android.app.Dialog
//import android.content.Context
//import android.os.Bundle
//import android.view.Window
//import com.uk.myhss.R
//
//class CustomProgressBar : Dialog {
//        var c: Context? = null
//
//        constructor(a: Context?) : super(a!!) {
//            c = a
//        }
//
//        constructor(a: Context?, myTheme: Int) : super(a!!, myTheme) {}
//
//        override fun onCreate(savedInstanceState: Bundle) {
//            super.onCreate(savedInstanceState)
//            requestWindowFeature(Window.FEATURE_NO_TITLE)
//            window!!.setBackgroundDrawableResource(R.color.transparentColor)
//            setContentView(R.layout.view_custom_progress_bar)
//            setCancelable(false)
//        }
//    }