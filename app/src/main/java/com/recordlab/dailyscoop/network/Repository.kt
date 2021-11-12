package com.recordlab.dailyscoop.network

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.recordlab.dailyscoop.data.DiaryData

class Repository(val application: Application, val headerMap: Map<String, String?>) {
    private val service = RetrofitClient.service

    fun getDiaryData() : MutableLiveData<List<DiaryData>> {
        val data = MutableLiveData<List<DiaryData>>()

        service.requestGetDiaries(header = headerMap).enqueue(
            onSuccess = {
                it.message()
                if (it.code() == 200) {
                    Log.d("통신 성공", it.body()!!.data[0].content)
                    data.value = it.body()!!.data
                }
            },
            onError = {
                Toast.makeText(application.applicationContext, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            },
            onFail = {

            })
        return data
    }

}