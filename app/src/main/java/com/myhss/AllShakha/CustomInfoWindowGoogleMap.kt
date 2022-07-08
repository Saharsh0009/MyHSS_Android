//package com.myhss.AllShakha
//
//import android.app.Activity
//import android.content.Context
//import android.view.View
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.model.Marker
//import com.uk.myhss.R
//
//class CustomInfoWindowGoogleMap(val context: Context) : GoogleMap.InfoWindowAdapter {
//
//    override fun getInfoContents(p0: Marker?): View {
//
//        val mInfoView = (context as Activity).layoutInflater.inflate(R.layout.mew_custom_map_marker, null)
//        val mInfoWindow: InfoWindowData? = p0?.tag as InfoWindowData?
//
//        mInfoView.txtLocMarkerName.text = mInfoWindow?.mLocatioName
//
//        return mInfoView
//    }
//
//    override fun getInfoWindow(p0: Marker?): View? {
//        return null
//    }
//}
