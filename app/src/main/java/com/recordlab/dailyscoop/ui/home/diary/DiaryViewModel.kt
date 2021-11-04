package com.recordlab.dailyscoop.ui.home.diary

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.recordlab.dailyscoop.data.AppDatabase
import com.recordlab.dailyscoop.data.Diary

class DiaryViewModel(application: Application): AndroidViewModel(application){
    private val db: AppDatabase = Room.databaseBuilder(
        application,
        AppDatabase::class.java, "dailyscoop-db"
    ).build()

    fun getAll(): LiveData<List<Diary>> {
        return db.diaryDao().getAll()
    }

    suspend fun insert(diary: Diary){
        db.diaryDao().insert(diary)
    }
}