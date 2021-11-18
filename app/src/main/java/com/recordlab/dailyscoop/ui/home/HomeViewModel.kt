package com.recordlab.dailyscoop.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.data.DiaryData
import com.recordlab.dailyscoop.network.Repository
import com.recordlab.dailyscoop.network.RetrofitClient

class HomeViewModel(application: Application) : AndroidViewModel(application) {
//    var data = MutableLiveData<MutableList<DiaryData>>()
    private val service = RetrofitClient.service
    private lateinit var repository: Repository
    private lateinit var header : Map<String, String?>

    var diaryData = MutableLiveData<List<DiaryData>>()
    var loadingLiveData = MutableLiveData<Boolean>()
    private val _text = MutableLiveData<String>().apply {
        val message = getApplication<Application>().resources.getString(R.string.checkout_these_diaries)
        value = message
    }
    val text: LiveData<String> = _text

    fun setHeader(map: Map<String, String?>) {
        header = map
        repository = Repository(getApplication(), header)
    }

    /*fun getDiaryData(): MutableLiveData<List<DiaryData>> {
        diaryData = repository.getDiaryData()
        return diaryData as MutableLiveData<List<DiaryData>>
    }*/


}