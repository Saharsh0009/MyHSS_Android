package com.myhss.ui.Barchat

import java.util.*

class Util {
    fun randomFloatBetween(min: Float, max: Float): Float {
        val r = Random()
        return min + r.nextFloat() * (max - min)
    }
}