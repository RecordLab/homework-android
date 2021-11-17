package com.recordlab.dailyscoop.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.recordlab.dailyscoop.network.RetrofitClient
import com.recordlab.dailyscoop.network.enqueue
import com.recordlab.dailyscoop.network.response.ResponseImageUrl
import com.recordlab.dailyscoop.network.response.ResponseUserProfile
import okhttp3.MultipartBody
import retrofit2.Call

class ProfileViewModel : ViewModel() {

    private var service = RetrofitClient.service
    private var header = mutableMapOf<String, String?>()

    private var userID: String = ""
        set(value) {
            field = value
        }

    private var _profileImage : String = "null"
    private var _nickname: String = "null"
//    private var _profileImage : MutableLiveData<String>? = null
//    private var _nickname: MutableLiveData<String>? = null
    private var imageBody: MultipartBody.Part? = null

    private val _text = MutableLiveData<String>().apply {
        value = "여긴 프로필 프래그먼트"
    }

    fun setHeader(map: MutableMap<String, String?>) {
        header = map
        Log.d("헤더 설정", "${header["Authorization"]}")
        requestUserInfo()
    }

    fun setImageBody(fileBody: MultipartBody.Part) {
        imageBody = fileBody
        requestUserProfileUpload()
    }

    private var userProfileCall: Call<ResponseUserProfile> = service.requestUserInfo(header = header)
//    private var userProfileImageCall: Call<ResponseImageUrl> = service.requestImageUrl(imageBody!!)

    val text: LiveData<String> = _text
    var profileImage: String = _profileImage
    var nickname: String = _nickname

    fun requestUserInfo(){
        userProfileCall = service.requestUserInfo(header = header)
        userProfileCall.clone().enqueue(
            onSuccess = {
                when (it.code()) {
                    in 200..299 -> {
//                        userprofile.value = it.body().profile
//                        profileImage = it.body()?.image.toString()
                        _nickname = it.body()?.nickname.toString()

//                        Log.d("닉네임 비교", "${profileImage} 프로필 이미지 적용 확인. $_profileImage")
                        Log.d("닉네임 비교", "${it.body()?.nickname} 흠 ${_nickname} 프로필 이미지 적용 확인. $nickname")
                    }
                }
            },
            onFail = {

            },
            onError = {

            }
        )
    }

    fun requestUserProfileUpload(){
        var userProfileImageCall: Call<ResponseImageUrl> = service.requestImageUrl(imageBody!!)
        userProfileImageCall.clone().enqueue(
            onSuccess = {
                when (it.code()) {
                    in 200..299 -> {
                        _profileImage.apply { it.body()?.data }

                    }
                }

            }, onFail = {

            }, onError = {

            }
        )
    }
}