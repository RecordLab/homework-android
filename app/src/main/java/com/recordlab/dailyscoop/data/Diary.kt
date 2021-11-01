package com.recordlab.dailyscoop.data

import java.sql.Timestamp

class Diary(
    val id: Int,
    val writeDay: Timestamp,
    val content: String,
    val image: String,
    val emotion1: Int,
    val emotion2: Int?,
    val emotion3: Int?,
    val theme: Int,
)