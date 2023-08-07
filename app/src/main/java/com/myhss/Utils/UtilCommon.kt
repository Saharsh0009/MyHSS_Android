package com.myhss.Utils

import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText
import java.text.DecimalFormat
import java.util.regex.Matcher
import java.util.regex.Pattern

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

        fun isOnlyLetters(stringName: String): Boolean {//isOnlyLetters
            val pattern: Pattern
            val matcher: Matcher
            val PASSWORD_PATTERN = "^[A-Za-z]*\$"
            pattern = Pattern.compile(PASSWORD_PATTERN)
            matcher = pattern.matcher(stringName)
            return matcher.matches()

        }

        fun isValidUserName(stringName: String): Boolean {
            val regex = Regex("^[a-zA-Z0-9]{5,}\$")
            return regex.matches(stringName)
        }

        fun isValidPassword(password: String): Boolean {
            val regex = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+\$).{8,}\$")
            return regex.matches(password)
        }

//        fun clearFocusFromAllEditTexts(parentLayout: ViewGroup) {
//            for (i in 0 until parentLayout.childCount) {
//                val view: View = parentLayout.getChildAt(i)
//                if (view is TextInputEditText) {
//                    view.clearFocus()
//                }
//            }
//        }

    }
}