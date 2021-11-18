package com.recordlab.dailyscoop.ui.profile

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recordlab.dailyscoop.network.RetrofitClient
import com.recordlab.dailyscoop.network.enqueue
import com.recordlab.dailyscoop.network.request.RequestProfileImage
import com.recordlab.dailyscoop.network.response.ResponseImageUrl
import com.recordlab.dailyscoop.network.response.ResponseUserProfile
import com.recordlab.dailyscoop.network.response.ResponseUserProfileImage
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Call

class ProfileViewModel : ViewModel() {

    private var service = RetrofitClient.service
    var profileHeader = mutableMapOf<String, String?>()// = mutableMapOf<String, String?>()

    private var userID: String = ""
        set(value) {
            field = value
        }

//    private var _profileImage : MutableLiveData<String> = MutableLiveData("")
//    private var _nickname: MutableLiveData<String> = MutableLiveData("")
//    private var _profileImage : MutableLiveData<String>? = null
//    private var _nickname: MutableLiveData<String>? = null
    private var imageBody: MultipartBody.Part? = null

    val profileImage = MutableLiveData<String>()//LiveData<String> get() = _profileImage
    val     nickname = MutableLiveData<String>()// LiveData<String> get() = _nickname
    val loadingLiveData = MutableLiveData<Boolean>()

    fun requestUserInfo(){
        // 로딩 시작
        loadingLiveData.value = true
        viewModelScope.launch {
            val userInfo = service.requestUserInfo(header = profileHeader)
            profileImage.value = userInfo.image
            nickname.value = userInfo.nickname

            //로딩 끝
            loadingLiveData.value = false
        }
    }

    fun requestUserProfileUpload(imageBody: MultipartBody.Part){
        var userProfileImageCall: Call<ResponseImageUrl> = service.requestImageUrl(imageBody!!)
        userProfileImageCall.clone().enqueue(
            onSuccess = {
                when (it.code()) {
                    in 200..206 -> {
                        profileImage.apply { it.body()?.data }
                        Log.d("프로필 확인", "${profileImage}")
                    }
                }

            }, onFail = {

            }, onError = {

            }
        )
        // 개인정보에 반영하기.
        requestProfileChange()
    }

    fun requestProfileChange(){
        var userProfileImageChangeCall: Call<ResponseUserProfileImage> = service.requestUserImageChange(header = profileHeader, image = RequestProfileImage(profileImage.value.toString()))
        userProfileImageChangeCall.clone().enqueue(
            onSuccess = {
                when (it.code()) {
                    200 -> {
                        Log.d("플필 업로드", "프로필이 업로드되었습니다.")
                    }
                }
            }, onError = {

            }, onFail = {

            }
        )
    }
}