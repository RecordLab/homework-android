package com.recordlab.dailyscoop.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity
data class DiaryData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val writeDay: Timestamp,
    val content: String,
    val image: String,
    val emotion1: Int,
    val emotion2: Int?,
    val emotion3: Int?,
    val theme: String,
)