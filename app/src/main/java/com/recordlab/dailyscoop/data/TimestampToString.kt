package com.recordlab.dailyscoop.data

import java.sql.Timestamp
import java.text.SimpleDateFormat

class TimestampToString {
    fun convert(timestamp: Timestamp): String {
        val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        return simpleDateFormat.format(timestamp)
    }
}