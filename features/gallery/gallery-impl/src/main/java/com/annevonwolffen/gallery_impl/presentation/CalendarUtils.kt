package com.annevonwolffen.gallery_impl.presentation

import android.content.res.Resources
import com.annevonwolffen.gallery_impl.R
import com.annevonwolffen.gallery_impl.presentation.utils.getMonthNameByIndex
import java.util.Calendar

fun Long.toCalendar(): Calendar = Calendar.getInstance().also { it.timeInMillis = this }

fun Calendar.toString(resources: Resources): String = resources.getString(
    R.string.date_format,
    get(Calendar.DAY_OF_MONTH),
    resources.getMonthNameByIndex(Calendar.MONTH - 1),
    get(Calendar.YEAR)
)

fun Calendar.isEqualByDate(calendar: Calendar): Boolean =
    (this.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
        && this.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
        && this.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH))