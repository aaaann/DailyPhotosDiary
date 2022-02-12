package com.annevonwolffen.gallery_impl.presentation.utils

import android.content.res.Resources
import com.annevonwolffen.gallery_impl.R

fun Resources.getMonthNameByIndex(index: Int): String = getStringArray(R.array.months_genitive)[index]