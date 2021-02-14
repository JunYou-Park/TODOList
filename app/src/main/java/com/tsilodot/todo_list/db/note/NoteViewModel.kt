package com.tsilodot.todo_list.db.note

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tsilodot.todo_list.model.NoteVo

class NoteViewModel(application: Application): AndroidViewModel(application) {

    class NoteFactory(private val application: Application): ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>) = NoteViewModel(application) as T

    }

    private val repository: NoteRepository = NoteRepository(application)

    fun getAllNotes(limit: Int) = repository.getAllNotes(limit)

    fun insert(noteVo: NoteVo) = repository.insert(noteVo)

    fun update(noteVo: NoteVo) = repository.update(noteVo)

    fun deleteAll() = repository.deleteAll()

    fun delete(noteVo: NoteVo) = repository.delete(noteVo)



}