package com.tsilodot.todo_list.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DateVo(
    var year: Int? = null,
    var month: Int? = null,
    val day:Int? = null,
    val hour: Int? = null,
    val minute: Int? = null,
    val second: Int? = null
): Parcelable

data class HourVo(
    val hourValid: Boolean = false,
    var hour24: Int? = null,
    var hour12: Int? = null,
    val pm_am: String? = null,
)