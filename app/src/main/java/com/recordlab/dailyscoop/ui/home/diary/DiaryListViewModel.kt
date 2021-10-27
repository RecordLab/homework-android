package com.recordlab.dailyscoop.ui.home.diary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.recordlab.dailyscoop.data.Diary
import javax.sql.DataSource

class DiaryListViewModel(val dataSource: DataSource) : ViewModel() {
    val diaryListData = dataSource.get
    private val diaries: MutableLiveData<List<Diary>> by lazy {
        MutableLiveData<List<Diary>>().also {
            loadDiaries()
        }
    }

    fun getDiaries() : LiveData<List<Diary>>{
        return
    }

    fun loadDiaries() {

    }
}

class DiaryListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiaryListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DiaryListViewModel(
                dataSource =
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}