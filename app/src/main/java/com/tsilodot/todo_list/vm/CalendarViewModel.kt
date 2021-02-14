package com.tsilodot.todo_list.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tsilodot.todo_list.model.DateVo
import com.tsilodot.todo_list.model.HourVo
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*

class CalendarViewModel : ViewModel() {

    private val nowDateModel: DateVo
    private var endDateModel: DateVo = DateVo()

    private val _selectedDateModel =  MutableLiveData<DateVo>()
    val selectedDateModel: LiveData<DateVo> = _selectedDateModel

    private val _todayDateModel =  MutableLiveData<DateVo>()
    val todayDateModel: LiveData<DateVo> = _todayDateModel

    private val _dateLiveData =  MutableLiveData<DateVo>()
    val dateLiveData: LiveData<DateVo> = _dateLiveData

    init {
        val nowDay = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        nowDateModel = DateVo(year = nowDay.substring(0, 4).toInt(), month = nowDay.substring(nowDay.indexOf("-") + 1, nowDay.lastIndexOf("-")).toInt(), day = nowDay.substring(nowDay.lastIndexOf("-") + 1, nowDay.indexOf(" ")).toInt())
        setToday(nowDateModel)
    }

    fun setSelectedDateModel(date: DateTime){
        _selectedDateModel.value = DateVo(year = date.year, month = date.monthOfYear, day = date.dayOfMonth, hour = date.hourOfDay, minute = date.minuteOfHour, second = date.secondOfMinute)
    }

    fun setEndDateModel(date: DateTime){
        endDateModel = DateVo(year = date.year, month = date.monthOfYear, day = date.dayOfMonth)
        if (endDateModel.month!! > 12) {
            endDateModel.year = endDateModel.year!! + 1
            endDateModel.month = 1
        }
    }

    fun getNowDateModel() = nowDateModel
    fun getEndDateModel() = endDateModel

    fun setChangeDate(year: Int, month: Int, option: String){
        if(option == "next"){
            if(nowDateModel.year!! > year || (nowDateModel.month!! >= month && nowDateModel.year!! >= year)) return

            if(month <= 1){
                _dateLiveData.postValue(DateVo(year-1, 12))
            }
            else _dateLiveData.postValue(DateVo(year,  (month-1)))
        }
        else if(option == "prev"){
            if(endDateModel.year!! < year || (endDateModel.year!! <= year && endDateModel.month!! <= month)) return

            if(month >= 12){
                _dateLiveData.postValue(DateVo((year+1), 1))
            }
            else _dateLiveData.postValue(DateVo((year), (month+1)))
        }
        else{
            _dateLiveData.postValue(DateVo(year, month))
        }
    }

    fun setSelectDay(year: Int, month: Int, day:Int) {
        _selectedDateModel.postValue(DateVo(year, month, day))
    }

    private fun setToday(dateModel: DateVo) {
        _todayDateModel.postValue(dateModel)
    }
}