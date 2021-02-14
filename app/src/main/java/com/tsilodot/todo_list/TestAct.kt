package com.tsilodot.todo_list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tsilodot.todo_list.databinding.ActivityTestBinding
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*

class TestAct : AppCompatActivity() {

    private val binding: ActivityTestBinding by lazy { ActivityTestBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val date = DateTime()

//        binding.tvTest.text = date.toString("yyyy년 MM월 dd일 a hh시 mm분")
        binding.tvTest.text = "${date.year}  ${date.monthOfYear}  ${date.dayOfMonth}  ${date.hourOfDay}  ${date.minuteOfHour}  ${date.secondOfMinute}"
    }
}