package com.myhss.ui.suryanamaskar.Model

/**
 * Created by Nikunj Dhokia on 08-06-2023.
 */
class BarchartDataModel : java.io.Serializable {
    private var value_x: String? = null
    private var value_y: String? = null
    private var value_user: String? = null
    private var value_id: String? = null

    fun getValue_x(): String? {
        return value_x
    }

    fun setValue_x(value_x: String?) {
        this.value_x = value_x
    }

    fun getValue_y(): String? {
        return value_y
    }

    fun setValue_y(value_y: String?) {
        this.value_y = value_y
    }

    fun getValue_user(): String? {
        return value_user
    }

    fun setValue_user(value_user: String?) {
        this.value_user = value_user
    }

    fun getValue_ID(): String? {
        return value_id
    }

    fun setValue_ID(value_id: String?) {
        this.value_id = value_id
    }


}