package com.recordlab.dailyscoop.ui.home.diary

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import javax.sql.DataSource

class DiaryListViewModel(val dataSource: DataSource, application: Application) : AndroidViewModel(application) {

    /*private val diaries: MutableLiveData<List<Diary>> by lazy {
        MutableLiveData<List<Diary>>().also {
            loadDiaries()
        }
    }

    fun getDiaries() : LiveData<List<Diary>>{
        return diaries
    }

    fun loadDiaries() {

    }
}

class DiaryListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiaryListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DiaryListViewModel(
                dataSource = DataSource.getSource("Diary")
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }*/
}