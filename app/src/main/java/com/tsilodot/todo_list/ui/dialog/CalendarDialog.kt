package com.tsilodot.todo_list.ui.dialog

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tsilodot.todo_list.R
import com.tsilodot.todo_list.databinding.DialogCalendarBinding
import com.tsilodot.todo_list.listener.CalendarDialogListener
import com.tsilodot.todo_list.model.DateVo
import com.tsilodot.todo_list.util.Cal
import com.tsilodot.todo_list.util.CalendarPrint
import com.tsilodot.todo_list.util.Lambda.Companion.afterTextChanged
import com.tsilodot.todo_list.util.ShowMessage.Companion.showLog
import com.tsilodot.todo_list.vm.CalendarViewModel
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class CalendarDialog (private val selectedDate: DateTime, private val endDate: DateTime) : BottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var binding: DialogCalendarBinding
    private lateinit var calendarViewModel: CalendarViewModel

    private lateinit var nowDateModel: DateVo
    private lateinit var selectDateModel: DateVo
    private lateinit var startDateModel: DateVo
    private lateinit var endDateModel: DateVo

    private var year: Int = 0
    private var month: Int = 0
    private var day:Int = 0
    private var weekend: String = ""

    private lateinit var calDate: Array<Array<CheckBox>>
    private lateinit var cal: Cal
    private lateinit var todayCheckBox: CheckBox

    private var dialogInterface: CalendarDialogListener? = null

    fun setDialogInterface(dialogInterface: CalendarDialogListener) {
        this.dialogInterface = dialogInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialog)

        calendarViewModel = ViewModelProvider(requireActivity()).get(CalendarViewModel::class.java)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.dialog_calendar, container)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding(view)

        setOnClickListener()

        setObserver()


    }


    private fun initBinding(view: View) {
        binding = DialogCalendarBinding.bind(view)
        calDate = arrayOf(
            arrayOf(binding.cbCalSun1, binding.cbCalMon1, binding.cbCalTue1, binding.cbCalWed1, binding.cbCalThu1, binding.cbCalFri1, binding.cbCalSat1),
            arrayOf(binding.cbCalSun2, binding.cbCalMon2, binding.cbCalTue2, binding.cbCalWed2, binding.cbCalThu2, binding.cbCalFri2, binding.cbCalSat2),
            arrayOf(binding.cbCalSun3, binding.cbCalMon3, binding.cbCalTue3, binding.cbCalWed3, binding.cbCalThu3, binding.cbCalFri3, binding.cbCalSat3),
            arrayOf(binding.cbCalSun4, binding.cbCalMon4, binding.cbCalTue4, binding.cbCalWed4, binding.cbCalThu4, binding.cbCalFri4, binding.cbCalSat4),
            arrayOf(binding.cbCalSun5, binding.cbCalMon5, binding.cbCalTue5, binding.cbCalWed5, binding.cbCalThu5, binding.cbCalFri5, binding.cbCalSat5),
            arrayOf(binding.cbCalSun6, binding.cbCalMon6, binding.cbCalTue6, binding.cbCalWed6, binding.cbCalThu6, binding.cbCalFri6, binding.cbCalSat6)
        )

        todayCheckBox = binding.cbCalSun1

        calendarViewModel.setEndDateModel(endDate)
        calendarViewModel.setSelectedDateModel(selectedDate)

        calendarViewModel.setChangeDate(selectedDate.year, selectedDate.monthOfYear, "null")

    }

    private fun setOnClickListener(){

        binding.ivbNext.setOnClickListener {
            val tmpYear = binding.tvYear.text.toString().substring(0, binding.tvYear.text.toString().indexOf("년"))
            val tmpMonth = binding.tvMonth.text.toString().substring(0, binding.tvMonth.text.toString().indexOf("월"))
            calendarViewModel.setChangeDate(tmpYear.toInt(), tmpMonth.toInt(), "prev")
        }

        binding.ivbPrev.setOnClickListener {
            val tmpYear = binding.tvYear.text.toString().substring(0, binding.tvYear.text.toString().indexOf("년"))
            val tmpMonth = binding.tvMonth.text.toString().substring(0, binding.tvMonth.text.toString().indexOf("월"))
            calendarViewModel.setChangeDate(tmpYear.toInt(), tmpMonth.toInt(), "next")
        }

        for(i in calDate.indices) {
            for (j in calDate[i].indices) {
                calDate[i][j].setOnClickListener(this)
            }
        }
    }


    private fun setObserver(){
        nowDateModel = calendarViewModel.getNowDateModel()
        endDateModel = calendarViewModel.getEndDateModel()

        calendarViewModel.selectedDateModel.observe(requireActivity(), {
            year = it.year!!
            month = it.month!!
            day = it.day!!
            selectDateModel = it
        })

        calendarViewModel.todayDateModel.observe(requireActivity(), {
            nowDateModel = it
        })

        calendarViewModel.dateLiveData.observe(requireActivity(), {
            makeCalendar(it.year!!, it.month!!)
        })
    }

    private fun makeCalendar(year: Int, month: Int) {

        binding.tvYear.text = "${year}년  "
        binding.tvMonth.text = "${month}월"
        val calendarPrint = CalendarPrint(year = year, month = month)
        cal = (calendarPrint.printCal())
        for(i in cal.calDate.indices){
            for(j in cal.calDate[i].indices){
                calDate[i][j].isEnabled = true // 초기화
                calDate[i][j].isChecked = false

                if(cal.calDate[i][j].toString() == "null" ) cal.calDate[i][j] = "" // null 값이 들어오면 빈 칸으로 변경

                calDate[i][j].text = cal.calDate[i][j].toString()

                if(calDate[i][j].text == "") calDate[i][j].isEnabled = false // 빈 칸이면 선택 X
                else{ // 빈 칸이 아니면
                    if(nowDateModel.year == year && nowDateModel.month == month && nowDateModel.day!! > calDate[i][j].text.toString().toInt()) calDate[i][j].isEnabled = false
                    if(endDateModel.year == year && endDateModel.month == month && endDateModel.day!! < calDate[i][j].text.toString().toInt()) calDate[i][j].isEnabled = false
                }

                if(cal.calDate[i][j]?.isNotBlank() == true && (nowDateModel.year == year && nowDateModel.month == month && nowDateModel.day == cal.calDate[i][j]!!.toInt())){
                    calDate[i][j].setBackgroundResource(R.drawable.selector_calandar_today_circle)
                    todayCheckBox = calDate[i][j]
                }
                else if(todayCheckBox.id == calDate[i][j].id) todayCheckBox.setBackgroundResource(R.drawable.selector_calandar_circle)


                if(cal.calDate[i][j]?.isNotBlank() == true && (selectDateModel.year == year && selectDateModel.month == month && selectDateModel.day == cal.calDate[i][j]!!.toInt())){
                    calDate[i][j].isChecked = true
                    weekend = (calDate[i][j].tag.toString())
                }
            }
        }
    }

    override fun onClick(v: View) {
        selectDay(v.id)
    }

    private fun selectDay(checkBoxId: Int){
        val dayBox = requireView().findViewById<CheckBox>(checkBoxId)
        day = dayBox.text.toString().toInt()
        val tmpYear = binding.tvYear.text.toString().substring(0, binding.tvYear.text.toString().indexOf("년"))
        val tmpMonth = binding.tvMonth.text.toString().substring(0, binding.tvMonth.text.toString().indexOf("월"))
        year = tmpYear.toInt()
        month = tmpMonth.toInt()
        weekend = (dayBox.tag.toString())

        if(selectDateModel.year == year && selectDateModel.month == month){
            for(i in calDate.indices){
                for(j in calDate[i].indices){
                    if(calDate[i][j].text.toString().isNotBlank()) calDate[i][j].isChecked = calDate[i][j].text.toString().toInt() == day
                }
            }
        }

        calendarViewModel.setSelectDay(year, month, day)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val date = "${year}년 ${formatTen(month)}월 ${formatTen(day)}일 ${getWeekend(weekend)}"
        val dateTimeFormatter = DateTimeFormat.forPattern("yyyyMMddHHmmss")
        showLog("$year ${formatTen(month)} ${formatTen(day)}")
        val jodaDate = dateTimeFormatter.parseDateTime("$year${formatTen(month)}${formatTen(day)}000000")
        dialogInterface!!.onClicked(date, jodaDate)
    }

    private fun formatTen(time: Int) = if(time<10) "0$time" else time.toString()

    private fun getWeekend(weekend: String): String{
        return when(weekend){
            "sun" -> "(일)"
            "mon" -> "(월)"
            "tue" -> "(화)"
            "wed" -> "(수)"
            "thu" -> "(목)"
            "fri" -> "(금)"
            "sat" -> "(토)"
            else -> ""
        }

    }
}