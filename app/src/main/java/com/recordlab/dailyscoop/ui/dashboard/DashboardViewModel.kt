package com.recordlab.dailyscoop.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.data.DiaryData

class DashboardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    var items = MutableLiveData<List<DiaryData>>()

}