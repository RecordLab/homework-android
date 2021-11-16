package com.recordlab.dailyscoop.network

import com.recordlab.dailyscoop.network.request.RequestSignIn
import com.recordlab.dailyscoop.network.request.RequestSignup
import com.recordlab.dailyscoop.network.request.RequestWriteDiary
import com.recordlab.dailyscoop.network.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    // 유저 정보 불러오기
    @GET("/api/user")
    fun requestUserInfo(
        @HeaderMap header: Map<String, String?>
    ) : Call<ResponseUserProfile>

    // 일기 작성
    @POST("/api/diaries")
    fun requestWriteDiary(
        @HeaderMap header: Map<String, String?>,
        @Body diary: RequestWriteDiary
    ): Call<ResponseWriteDiary>

    // 사용자가 작성한 전체 일기 보기.
    @GET("/api/diaries")
    fun requestGetDiaries(
        @HeaderMap header: Map<String, String?>
    ): Call<ResponseDiaryList>

    // 사용자가 작성한 일기 검색
    @GET("/api/diaries")
    fun requestSearchDiaries(
        @HeaderMap header: Map<String, String?>,
        @Query("search") search: String
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

    // 이미지 업로드
    @Multipart
    @POST("/api/image")
    fun requestImageUrl(
        //@HeaderMap header: Map<String, String?>,
        @Part file: MultipartBody.Part
    ): Call<ResponseImageUrl>
}