package com.phoebus.demo.phastpay.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateUtils {
    const val DEFAULT_DATE_FORMAT = "dd/MM/yyyy"
    const val DEFAULT_TIME_FORMAT = "HH:mm"
    const val DEFAULT_DATE_AND_TIME = "dd/MM/yyyy HH:mm"
    const val DEFAULT_DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss"
    const val ISO_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val JAVA_DEFAULT_DATE_STRING = "EEE MMM dd HH:mm:ss z yyyy"

    fun formatDate(date: Date, format: String = DEFAULT_DATE_FORMAT): String {
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        return formatter.format(date)
    }

    fun parseDate(dateString: String, format: String = DEFAULT_DATE_FORMAT): Date? {
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        return try {
            formatter.parse(dateString)
        } catch (e: ParseException) {
            null
        }
    }

    fun getCurrentDate(): Date {
        return Calendar.getInstance().time
    }

    fun getCurrentDateFormatted(format: String = DEFAULT_DATE_FORMAT): String {
        return formatDate(getCurrentDate(), format)
    }

    fun parseDateAndTimeToUTC(date: String, time: String): String {
        val inputFormat = SimpleDateFormat(DEFAULT_DATE_AND_TIME, Locale.getDefault())
        val outputFormat = SimpleDateFormat(ISO_DATE_TIME, Locale.getDefault())

        outputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = inputFormat.parse("$date $time") ?: return ""
        return outputFormat.format(date)
    }

    fun formatDateStrUTCToStrLocal(isoDateTime: String): String {
        val isoFormat = SimpleDateFormat(ISO_DATE_TIME, Locale.getDefault())
        isoFormat.timeZone = TimeZone.getTimeZone("UTC")

        return try {
            val date: Date = isoFormat.parse(isoDateTime) ?: return ""
            val localFormat = SimpleDateFormat(DEFAULT_DATE_AND_TIME, Locale.getDefault())

            localFormat.timeZone = TimeZone.getDefault()
            localFormat.format(date)
        } catch (e: Exception) {
            ""
        }
    }

}