package com.recordlab.dailyscoop.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DiaryDao {
    @Query("select * from DiaryData")
    fun getAll(): LiveData<List<DiaryData>>

    @Insert
    fun insert(diaryData: DiaryData)


}