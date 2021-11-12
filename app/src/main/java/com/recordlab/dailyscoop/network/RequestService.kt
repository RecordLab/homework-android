package com.recordlab.dailyscoop.network

import com.recordlab.dailyscoop.network.request.RequestSignIn
import com.recordlab.dailyscoop.network.request.RequestSignup
import com.recordlab.dailyscoop.network.request.RequestWriteDiary
import com.recordlab.dailyscoop.network.response.*
import retrofit2.Call
import retrofit2.http.*
import java.util.*
import kotlin.collections.ArrayList

interface RequestService {
    // 회원가입
    @POST("/api/signup")
    fun requestSignup(@Body body: RequestSignup): Call<TokenData> // Call<ResponseSignup>

    // 로그인
    @POST("/api/login")
    fun requestSignIn(@Body body: RequestSignIn): Call<UserInfoData> //Call<ResponseSignin>

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
    ): Call<ResponseDiaryDetail>

    @GET("/api/diaries/calendar")
    fun requestGetCalendar(
        @HeaderMap header: Map<String, String?>,
        @Query("date") date: String,
        @Query("type") type: String
    ) : Call<ResponseDiaryList>

    // 일기 수정
    @POST("/diaries")
    fun requestUpdateDiary(
        @HeaderMap header: Map<String, String?>,
        @Body diary: RequestWriteDiary
    ): Call<ResponseWriteDiary>

    // 명언 api
    @GET("https://api.qwer.pw/request/helpful_text")
    fun requestQuotation(
        @Query("apikey") apikey: String
    ): Call<List<ResponseQuotation>>
}