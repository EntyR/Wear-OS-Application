package com.harman.wearosapp.app.other

import java.time.ZonedDateTime

/**
 * Return pair of ZonedDateTime hours value in UTC system with TimePeriod
 */
fun ZonedDateTime.getUTCHours() = if (this.hour >= 12) hour - 12 to UTCPeriod.PostMeridiem else this.hour to UTCPeriod.AnteMeridiem

enum class UTCPeriod(
    val stringValue: String
){
    AnteMeridiem("AM"),
    PostMeridiem("PM");
}