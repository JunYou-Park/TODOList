package com.tsilodot.todo_list.util

import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView

class Lambda {
    companion object{
        fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
            this.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    afterTextChanged.invoke(s.toString())
                }
            })
        }

        fun TextView.delTextView(text: String){
            val htmlText = "<del>$text</del>"
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) this.text = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
            else this.text = Html.fromHtml(htmlText)
        }
    }
}