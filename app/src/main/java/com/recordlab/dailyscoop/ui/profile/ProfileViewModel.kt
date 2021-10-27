package com.recordlab.dailyscoop.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "여긴 프로필 프래그먼트"
    }
    val text: LiveData<String> = _text
}