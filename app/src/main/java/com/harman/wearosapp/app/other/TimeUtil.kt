package com.harman.wearosapp.app.other

import android.annotation.SuppressLint
import android.icu.util.Calendar
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

/**
 * Return pair of ZonedDateTime hours value in UTC system with TimePeriod
 */
fun ZonedDateTime.getUTCHours() =
    if (this.hour >= 12) hour - 12 to UTCPeriod.PostMeridiem else this.hour to UTCPeriod.AnteMeridiem


fun getSecondsFromMillis(millis: Long): Long {
    val days = TimeUnit.MILLISECONDS.toDays(millis)
    val result = millis - TimeUnit.DAYS.toMillis(days)
    return TimeUnit.MILLISECONDS.toSeconds(result)
}


fun addSeconds(seconds: Long, addTo: Long): Long {
    return TimeUnit.SECONDS.toMillis(seconds) + addTo
}

@SuppressLint("SimpleDateFormat")
fun getFormattedDateFromMillis(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")

    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = millis
    return formatter.format(calendar.time)
}


enum class UTCPeriod(
    val stringValue: String
) {
    AnteMeridiem("AM"),
    PostMeridiem("PM");
}