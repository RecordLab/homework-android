package com.recordlab.dailyscoop.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.recordlab.dailyscoop.data.DiaryData

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    var data = MutableLiveData<MutableList<DiaryData>>()
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}