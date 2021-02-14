package com.tsilodot.todo_list.util

import android.content.Context
import android.util.Log
import android.widget.Toast

class ShowMessage {
    companion object{
        fun showMsg(context: Context, msg: Any){
            Toast.makeText(context, msg.toString(), Toast.LENGTH_SHORT).show()
        }
        fun showLog(msg: Any){
            Log.d("확인", msg.toString())
        }
    }
}