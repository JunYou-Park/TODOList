package com.tsilodot.todo_list.util

import java.util.*

class CalendarPrint(year: Int, month: Int) {
    // Calendar 인스턴스 생성
    private var cal = Calendar.getInstance()
    private var modelCal: Cal? = null
    // 요일 표시 헤더

    // 날짜 데이터 배열
    private val calDate = Array(6) { arrayOfNulls<String>(7) }
    private val width = 7 // 배열 가로 넓이
    private var startDay // 월 시작 요일
            = 0
    private var lastDay // 월 마지막 날짜
            = 0
    private var inputDate = 1 // 입력 날짜

    // 달력 출력
    fun printCal(): Cal {

        // 날짜 배열 출력
        var row = 0
        for (j in 1 until lastDay + startDay) {
            if ((j - 1) % width == width - 1) {
                row++
            }
        }
        return modelCal!!
    }

    // 생성자 : 데이터 생성
    init {
        // 입력 month(월) 은 1~12 사이의 값이다.
        if (month < 1 || month > 12) {
            throw Exception()
        } else {

            // Calendar에 년,월,일 셋팅
            cal[Calendar.YEAR] = year
            cal[Calendar.MONTH] = month - 1
            cal[Calendar.DATE] = 1
            startDay = cal[Calendar.DAY_OF_WEEK] // 월 시작 요일
            lastDay = cal.getActualMaximum(Calendar.DATE) // 월 마지막 날짜

            // 2차 배열에 날짜 입력
            var row = 0
            var i = 1
            while (inputDate <= lastDay) {

                // 시작 요일이 오기전에는 공백 대입
                if (i < startDay) calDate[row][i - 1] = ""
                else {
                    // 날짜 배열에 입력
                    calDate[row][(i - 1) % width] = inputDate.toString()
                    inputDate++

                    // 가로 마지막 열에 오면 행 바꿈
                    if (i % width == 0) row++
                }
                i++
            }
            modelCal = Cal(calDate)
        }
    }
}

data class Cal(
        var calDate: Array<Array<String?>>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Cal

        if (!calDate.contentDeepEquals(other.calDate)) return false

        return true
    }

    override fun hashCode(): Int {
        return calDate.contentDeepHashCode()
    }

}