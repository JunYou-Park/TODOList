package com.tsilodot.todo_list.util

import android.content.Context
import android.util.TypedValue

class ResolutionHelper {
    companion object{
        fun Context.getDimension(value: Float): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, value, this.resources.displayMetrics).toInt()
        }
    }
}