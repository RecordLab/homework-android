package com.recordlab.dailyscoop.network

import com.recordlab.dailyscoop.network.request.*
import com.recordlab.dailyscoop.network.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface RequestService {
    // 회원가입
    @POST("/api/signup")
    fun requestSignup(@Body body: RequestSignup): Call<TokenData> // Call<ResponseSignup>

    // 로그인
    @POST("/api/login")
    fun requestSignIn(@Body body: RequestSignIn): Call<UserInfoData> //Call<ResponseSignin>

    // 유저 정보 가져오기
    @GET("/api/user/{userID}")
    fun requestUserInfo(
        @HeaderMap header: Map<String, String?>,
        @Path("userID") userID: String
    ): Call<ResponseUserInfo>

    // 닉네임 변경하기
    @PUT("/api/user/change_nickname")
    fun requestChangeNickname(
        @HeaderMap header: Map<String, String?>,
        @Body body: RequestChangeNickname
    ): Call<ResponseChange>

    // 비밀번호 변경하기
    @PUT("/api/user/change_password")
    fun requestChangePassword(
        @HeaderMap header: Map<String, String?>,
        @Body body: RequestChangePassword
    ): Call<ResponseChange>

    // 회원 탈퇴하기
    @DELETE("/api/user/{userID}")
    fun requestDeleteAccount(
        @HeaderMap header: Map<String, String?>,
        @Path("userID") userID: String
    ): Call<ResponseChange>

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

    @Multipart
    @POST("/api/image")
    fun requestImageUrl(
        //@HeaderMap header: Map<String, String?>,
        @Part file: MultipartBody.Part
    ): Call<ResponseImageUrl>

    @GET("/api/diaries/emotions")
    fun requestGetEmotionsCount(
        @HeaderMap header: Map<String, String?>,
        @Query("type") type: String,
        @Query("date") date: String
    ): Call<ResponseEmotionsCount>

    @GET("/api/diaries/count")
    fun requestGetDiariesCount(
        @HeaderMap header: Map<String, String?>,
        @Query("type") type: String,
        @Query("date") date: String
    ): Call<ResponseDiariesCount>
}