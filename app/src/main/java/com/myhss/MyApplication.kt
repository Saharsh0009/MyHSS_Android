package com.myhss

import android.app.Application
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.Tracker
import com.stripe.android.PaymentConfiguration
import com.uk.myhss.R


class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        sAnalytics = GoogleAnalytics.getInstance(this)

        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51MYS88SD1lLtRImz3pdqBn6WirB0AYgXZcvjAxz0AdipGLzrPHccYShud0nP0ALbbO5d7SVFNPLmKN2yz0MVa6yz00qHqe9oo0"
        )
    }

    // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
    /**
     * Gets the default [Tracker] for this [Application].
     * @return tracker
     */
    @get:Synchronized
    val defaultTracker: Tracker?
        get() {
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            if (sTracker == null) {
                sTracker = sAnalytics!!.newTracker(R.xml.global_tracker)
            }
            return sTracker
        }

    companion object {
        private var sAnalytics: GoogleAnalytics? = null
        private var sTracker: Tracker? = null
    }
}