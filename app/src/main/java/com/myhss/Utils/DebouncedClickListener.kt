package com.myhss.Utils

import android.view.View

class DebouncedClickListener(private val onDebouncedClick: () -> Unit) : View.OnClickListener {


    private var lastClickTime = 0L
    private val debounceInterval = 700L
    override fun onClick(view: View) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime >= debounceInterval) {
            lastClickTime = currentTime
            onDebouncedClick.invoke()
        }
    }
}