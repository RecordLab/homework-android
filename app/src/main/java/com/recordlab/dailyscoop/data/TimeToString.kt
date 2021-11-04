package com.recordlab.dailyscoop.data

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class TimeToString {
    fun convert(timestamp: Timestamp): String {
        val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        return simpleDateFormat.format(timestamp)
    }
    fun convert(date: Date, mode: Int): String {
        val format1= SimpleDateFormat("yy-MM-dd")
        val format2= SimpleDateFormat("yy.MM.dd")
        when(mode){
            0 -> {
                return format1.format(date)
            }
            1 -> {
                return format2.format(date)
            }
        }
        return format1.format(date)
    }
}