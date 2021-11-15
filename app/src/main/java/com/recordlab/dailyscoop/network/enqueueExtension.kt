package com.recordlab.dailyscoop.network

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun<ResponseType> Call<ResponseType>.enqueue(
    onSuccess: (response: Response<ResponseType>) -> Unit,
    onError: () -> Unit = {Log.d("Response Error", "응답 실패")},
    onFail: () -> Unit = { Log.d("Network Error", "통신 실패")}
) {
    this.enqueue(object: Callback<ResponseType> {
        override fun onResponse(call: Call<ResponseType>, response: Response<ResponseType>?) {
            if(response == null){
                Log.d("로그", "null")
            }
            else{
            when(response.code()){
                in 200..299 -> { // success

                }
                401 -> {
                    // 권한 없음.
                }
                in 400..499 -> {
//                    Log.d("네트워크 확인 ", "${call.request().body}")

                }
                in 500..599 -> {
                    // 서버 에러
                }
            }
            onSuccess(response) ?: onError()}
        }

        override fun onFailure(call: Call<ResponseType>, t: Throwable) {
            onFail()
            Log.d("onFailure() >> ", t.stackTraceToString())
        }

    })
}
