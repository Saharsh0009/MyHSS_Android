package com.myhss.Utils

import android.os.Build
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.RequiresApi
import com.google.android.material.textfield.TextInputEditText
import com.myhss.appConstants.AppParam
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by Nikunj Dhokia on 03-07-2023.
 */
class UtilCommon {

    companion object {
        val timeStampFmt = "yyyy-MM-dd HH:mm:ss"
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

        fun isNotificationTrue(sType: String): Boolean {
            DebugLog.e("sType : $sType")
            var isTrue = false
            if (sType == "0") { // 0 => suchana , no => nothing
                isTrue = true
            } else {
                for (n in AppParam.notificTypeData?.indices!!) {
                    if (sType == AppParam.notificTypeData!![n].id) {
                        return true
                    }
                }
            }
            return isTrue
        }

        fun dateAndTimeformat(stringName: String): String {
            val df: DateFormat = SimpleDateFormat(timeStampFmt)
            val dt = df.parse(stringName)
            val tdf: DateFormat = SimpleDateFormat("HH:mm a")
            val dfmt: DateFormat = SimpleDateFormat("dd/MM/yyyy")
            val timeOnly = tdf.format(dt)
            val dateOnly = dfmt.format(dt)

            return "$dateOnly $timeOnly"
        }


        fun htmlToText(sText: String): String {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                var stext = Html.fromHtml(sText, Html.FROM_HTML_MODE_COMPACT)
                stext.toString()
            } else {
                @Suppress("DEPRECATION")
                var stext = Html.fromHtml(sText)
                stext.toString()
            }
        }

        fun isUserUnder18(dateString: String): Boolean {
            val dateFormat = SimpleDateFormat("MM/dd/yyyy")
            val birthDate: Date = dateFormat.parse(dateString)
            val currentDate = Date()

            // Calculate the user's age
            val diff = currentDate.time - birthDate.time
            val age = (diff / (1000L * 60 * 60 * 24 * 365.25)).toInt()

            return age < 18
        }
    }
}