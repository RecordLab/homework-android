package com.recordlab.dailyscoop.network

import com.recordlab.dailyscoop.network.request.RequestSignin
import com.recordlab.dailyscoop.network.request.RequestSignup
import com.recordlab.dailyscoop.network.request.RequestWriteDiary
import com.recordlab.dailyscoop.network.response.ResponseDiaryList
import com.recordlab.dailyscoop.network.response.ResponseWriteDiary
import com.recordlab.dailyscoop.network.response.TokenData
import com.recordlab.dailyscoop.network.response.UserInfoData
import retrofit2.Call
import retrofit2.http.*

public interface RequestService {
    // 회원가입
    @POST("/signup")
    suspend fun requestSignup(@Body body: RequestSignup): Call<TokenData> // Call<ResponseSignup>

    // 로그인
    @POST("/singin")
    fun requestSingin(@Body body: RequestSignin): Call<UserInfoData> //Call<ResponseSignin>

    // 일기 작성
    @POST("/diaries")
    fun requestWriteDiary(
        @HeaderMap header: Map<String, String?>,
        @Body diary: RequestWriteDiary
    ): Call<ResponseWriteDiary>

    // 사용자가 작성한 전체 일기 보기.
    @GET("/api/diaries")
    fun requestGetDiaries(
        @HeaderMap header: Map<String, String?>
    ): Call<ResponseDiaryList>

    // 특정 날짜 일기 가져오기.
    @GET("/api/diaries/{diaryDate}")
    fun requestGetDiaryDetail(
        @HeaderMap header: Map<String, String?>,
        @Path("diaryDate") date: String
    ): Call<ResponseDiaryList>

    // 일기 수정
    @POST("/diaries")
    fun requestUpdateDiary(
        @HeaderMap header: Map<String, String?>,
        @Body diary: RequestWriteDiary
    ): Call<ResponseWriteDiary>
}