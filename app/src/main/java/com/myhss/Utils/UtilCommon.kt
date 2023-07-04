package com.myhss.Utils

import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * Created by Nikunj Dhokia on 03-07-2023.
 */
class UtilCommon {


    companion object {
        fun roundOffDecimal(number: Double): Double? {
            val df = DecimalFormat("#.##")
//            df.roundingMode = RoundingMode.CEILING
            return df.format(number).toDouble()
        }

    }
}