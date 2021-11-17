package com.recordlab.dailyscoop.ui.home.diary

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

import com.recordlab.dailyscoop.data.DiaryData

class DiaryViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var diaryData: MutableLiveData<MutableList<DiaryData>>

    /*private val db: AppDatabase = Room.databaseBuilder(
        application,
        AppDatabase::class.java, "dailyscoop-db"
    ).build()

    fun getAll(): LiveData<List<Diary>> {
        return db.diaryDao().getAll()
    }

    suspend fun insert(diary: Diary){
        db.diaryDao().insert(diary)
    }*/
}