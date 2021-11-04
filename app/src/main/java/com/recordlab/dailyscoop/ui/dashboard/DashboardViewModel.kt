package com.recordlab.dailyscoop.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.recordlab.dailyscoop.R

class DashboardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    var items = MutableLiveData<ArrayList<DashboardItem>>()

    init {
        var temp = ArrayList<DashboardItem>()
        temp.apply {
            add(DashboardItem("1월", "123", R.drawable.happy))
            add(DashboardItem("2월", "1234", R.drawable.bored))
        }
        items.postValue(temp)
    }
}