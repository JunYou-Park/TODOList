package com.tsilodot.todo_list.listener

import org.joda.time.DateTime

interface CalendarDialogListener {
    fun onClicked(date: String, jodaDate: DateTime)
}