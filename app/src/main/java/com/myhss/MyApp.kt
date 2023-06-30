package com.myhss

import android.app.Application
import com.myhss.appConstants.AppParam
import com.stripe.android.PaymentConfiguration

/**
 * Created by Nikunj Dhokia on 27-06-2023.
 */
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51MYS88SD1lLtRImz3pdqBn6WirB0AYgXZcvjAxz0AdipGLzrPHccYShud0nP0ALbbO5d7SVFNPLmKN2yz0MVa6yz00qHqe9oo0"
        )
    }
}