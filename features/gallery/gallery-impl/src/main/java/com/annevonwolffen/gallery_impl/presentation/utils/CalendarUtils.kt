package com.annevonwolffen.gallery_impl.presentation.utils

import android.content.res.Resources
import com.annevonwolffen.gallery_impl.R
import java.util.Calendar

fun Long.toCalendar(): Calendar = Calendar.getInstance().also { it.timeInMillis = this }

fun Long.toDateWithoutTime(): Long = Calendar.getInstance()
    .also { calendar ->
        calendar.timeInMillis = this
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
    }.timeInMillis

fun Calendar.toDateString(resources: Resources): String = resources.getString(
    R.string.date_format,
    get(Calendar.DAY_OF_MONTH),
    resources.getMonthNameByIndex(get(Calendar.MONTH)),
    get(Calendar.YEAR)
)

fun Calendar.toDayOfWeekString(resources: Resources): String = resources.getStringArray(
    R.array.days_of_week
)[this.get(Calendar.DAY_OF_WEEK) - 1]

fun Calendar.isEqualByDate(calendar: Calendar): Boolean =
    (this.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
        && this.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
        && this.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH))