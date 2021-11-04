package com.recordlab.dailyscoop.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Diary::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun diaryDao(): DiaryDao
}