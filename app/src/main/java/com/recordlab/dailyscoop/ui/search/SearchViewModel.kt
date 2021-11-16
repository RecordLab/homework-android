package com.recordlab.dailyscoop.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.recordlab.dailyscoop.data.DiaryData

class SearchViewModel : ViewModel() {

    var items = MutableLiveData<List<DiaryData>>()
}