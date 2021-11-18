package com.recordlab.dailyscoop

import android.app.Application
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility


class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Kakao SDK 초기화
        KakaoSdk.init(this, "ba4c4a64a67509ca547ba2d761e0a8ff")

        val keyHash = Utility.getKeyHash(this)
        Log.d("해시 >> ", keyHash)
    }
}