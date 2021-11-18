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

    // 유저 정보 불러오기
    @GET("/api/user")
    suspend fun requestUserInfo(
        @HeaderMap header: Map<String, String?>
    ) : ResponseUserProfile

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

    @PUT("/api/user/set_image")
    fun requestUserImageChange(
        @HeaderMap header: Map<String, String?>,
        @Body image: RequestProfileImage
    ): Call<ResponseUserProfileImage>

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
        @HeaderMap header: Map<String, String?>,
        @Query("sort") sort: Int
    ): Call<ResponseDiaryList>

    // 사용자가 작성한 일기 검색
    @GET("/api/diaries")
    fun requestSearchDiaries(
        @HeaderMap header: Map<String, String?>,
        @Query("search") search: String,
        @Query("sort") sort: Int = -1
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
        @Query("type") type: String,
        @Query("sort") sort: Int
    ) : Call<ResponseDiaryList>

    // 일기 수정
    @POST("/diaries")
    fun requestUpdateDiary(
        @HeaderMap header: Map<String, String?>,
        @Body diary: RequestWriteDiary
    ): Call<ResponseWriteDiary>

    // 일기 삭제
    @DELETE("/api/diaries/{date}")
    fun requestDeleteDiary(
        @HeaderMap header: Map<String, String?>,
        @Path("date") date: String
    ): Call<ResponseChange>

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

    // 명언 리스트
    @GET("/api/favorites")
    fun requestGetQuotation(
        @HeaderMap header: Map<String, String?>,
    ): Call<ResponseQuotationList>

    // 명언 저장
    @POST("/api/favorites")
    fun requestAddQuotation(
        @HeaderMap header: Map<String, String?>,
        @Body quote: RequestQuotation
    ): Call<ResponseChange>

    // 명언 삭제
    @HTTP(method = "DELETE", path = "/api/favorites", hasBody = true)
    fun requestDelQuotation(
        @HeaderMap header: Map<String, String?>,
        @Body quote: RequestQuotation
    ): Call<ResponseChange>
}