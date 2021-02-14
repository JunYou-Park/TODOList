package com.tsilodot.todo_list.listener

import com.tsilodot.todo_list.model.NoteVo

interface NoteItemListener {
    fun onClick(model: Any, position: Int)

    fun onSelect(model: Any, option: String)
}