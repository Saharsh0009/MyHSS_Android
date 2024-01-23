package com.myhss.Utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

class DebouncedClickListener(private val onDebouncedClick: () -> Unit) : View.OnClickListener {


    private var lastClickTime = 0L
    private val debounceInterval = 1000L
    private var clickedView: View? = null
    override fun onClick(view: View) {
        clickedView = view
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime >= debounceInterval) {
            lastClickTime = currentTime
            onDebouncedClick.invoke()
        }
        hideKeyboardIfOpen()
    }

    private fun hideKeyboardIfOpen() {
        clickedView?.let { view ->
            val imm =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm.isActive) {
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

}