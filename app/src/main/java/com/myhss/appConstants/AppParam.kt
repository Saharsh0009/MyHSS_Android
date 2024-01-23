package com.myhss.appConstants

import com.myhss.Main.notific_type.NotificTypeData

/**
 * Created by Nikunj Dhokia on 27-06-2023.
 */
interface AppParam {

    companion object {
        val NOTIFIC_KEY = "notification"
        val NOTIFIC_ID = "notification_id"
        var notificTypeData: List<NotificTypeData>? = null
    }

}