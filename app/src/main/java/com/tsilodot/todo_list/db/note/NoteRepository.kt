package com.tsilodot.todo_list.db.note

import android.app.Application
import com.tsilodot.todo_list.db.ToDoRoomDataBase
import com.tsilodot.todo_list.model.NoteVo

class NoteRepository(application: Application) {

    private val noteDao: NoteDao

    init {
        val db = ToDoRoomDataBase.getDatabase(application)
        noteDao = db.noteDao()
    }

    fun getAllNotes(limit: Int, done: Boolean) = noteDao.getAllNotes(limit, done)

    fun insert(noteVo: NoteVo) = ToDoRoomDataBase.databaseWriteExecutorService.execute { noteDao.insert(noteVo) }

    fun deleteAll() = ToDoRoomDataBase.databaseWriteExecutorService.execute {noteDao.deleteAll()}

    fun delete(noteVo: NoteVo) = ToDoRoomDataBase.databaseWriteExecutorService.execute{noteDao.delete(noteVo)}

    fun update(noteVo: NoteVo) = ToDoRoomDataBase.databaseWriteExecutorService.execute{noteDao.update(noteVo)}
}