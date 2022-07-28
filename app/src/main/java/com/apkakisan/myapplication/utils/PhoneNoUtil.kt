package com.apkakisan.myapplication.utils

import android.telephony.PhoneNumberUtils
import com.apkakisan.myapplication.helpers.PHONE_COUNTRY_CODE_FORMAT
import java.lang.Exception

object PhoneNoUtil {

    /** formats 3234364949 to (323) 436-4949 */
    fun format10DigitToUS(phone: String): String {
        try {
            return PhoneNumberUtils.formatNumber(phone, PHONE_COUNTRY_CODE_FORMAT)
        } catch (ex: Exception) {
            println("Formatting 10 digit phone no to US failed.")
        }
        return ""
    }

    /** formats (323) 436-4949 to 3234364949 */
    fun formatUSTo10Digit(phone: String): String {
        var tempPhone = phone
        try {
            tempPhone = tempPhone.filter { it.isLetterOrDigit() || it.isWhitespace() }
            tempPhone = tempPhone.replace(" ", "")
        } catch (ex: Exception) {
            println("Formatting US phone no to 10 digits failed.")
        }
        return tempPhone
    }

    /** formats 10 digit (3234364949) to following based on build type...
     * +923234364949 for pakistan if build type is debug_with_registration
     * +913234364949 for india if build type is rahul
     */
    fun formatForServer(phone: String): String {
        var tempPhone = ""
        try {
            tempPhone = if (BuildTypeUtil.isDebugWithRegistration())
                PhoneNumberUtils.formatNumberToE164(phone, "PK")
            else
                PhoneNumberUtils.formatNumberToE164(phone, "IN")
        } catch (ex: Exception) {
            println("Formatting phone no for server failed.")
        }
        return tempPhone
    }
}