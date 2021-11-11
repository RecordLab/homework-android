package com.recordlab.dailyscoop.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity
data class DiaryData(
    val content: String,
    val image: String,
    val date: Timestamp,
    val emotions: List<String>?,
    val theme: String
)