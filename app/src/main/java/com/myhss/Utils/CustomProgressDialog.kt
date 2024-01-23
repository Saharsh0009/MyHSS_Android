package com.myhss.Utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.uk.myhss.R

class CustomProgressDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawableResource(R.color.transparent_Color)
        setContentView(R.layout.view_custom_progress_bar_sn)

        val imageViewGif = findViewById<ImageView>(R.id.imageViewGif)
        Glide.with(context)
            .asGif()
            .load(R.drawable.suryannamaskar)
            .transform(RoundedCorners(20))
            .into(imageViewGif)
        setCancelable(false)
    }
}