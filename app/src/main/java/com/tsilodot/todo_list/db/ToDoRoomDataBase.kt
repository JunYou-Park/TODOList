package com.tsilodot.todo_list.db

import android.content.Context
import androidx.room.*
import com.tsilodot.todo_list.db.note.NoteDao
import com.tsilodot.todo_list.model.NoteVo
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(entities = [NoteVo::class], version = 2)
abstract class ToDoRoomDataBase: RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object{
        @Volatile
        private var INSTANCE: ToDoRoomDataBase? = null
        private const val NUMBER_OF_THREADS = 3

        val databaseWriteExecutorService: ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

        fun getDatabase(context: Context): ToDoRoomDataBase{
            if(INSTANCE==null){
                synchronized(ToDoRoomDataBase::class.java){
                    if(INSTANCE==null){
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                            ToDoRoomDataBase::class.java, "RoomDB")
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }

}