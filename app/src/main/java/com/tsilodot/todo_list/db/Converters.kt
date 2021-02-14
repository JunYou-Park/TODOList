package com.tsilodot.todo_list.db

import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun timeToDate(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTime(date: Date): Long {
        return date.time
    }

}