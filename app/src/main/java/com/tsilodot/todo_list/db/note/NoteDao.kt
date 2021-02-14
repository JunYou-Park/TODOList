package com.tsilodot.todo_list.db.note

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tsilodot.todo_list.model.NoteVo

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(noteVo: NoteVo)

    @Update
    fun update(noteVo: NoteVo)

    @Delete
    fun delete(vararg noteVo: NoteVo)

    @Query("DELETE FROM note_table")
    fun deleteAll()

    @Query("SELECT * FROM note_table ORDER BY start_date DESC limit :limit")
    fun getAllNotes(limit: Int): LiveData<List<NoteVo>>

}