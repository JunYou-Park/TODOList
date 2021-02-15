package com.tsilodot.todo_list.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "note_table")
data class NoteVo(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,

    @ColumnInfo(name = "title")
    val title: String = "",

    @ColumnInfo(name = "content")
    val content: String = "",

    @ColumnInfo(name = "start_date")
    val startDate: Long = 0L,

    @ColumnInfo(name = "end_date")
    val endDate: Long = 0L,

    @ColumnInfo(name = "done")
    var done: Boolean = false

):Parcelable
