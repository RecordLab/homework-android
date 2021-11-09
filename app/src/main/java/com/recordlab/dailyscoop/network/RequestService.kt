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

    @POST("/singin")
    fun requestSingin(@Body body: RequestSignin): Call<UserInfoData> //Call<ResponseSignin>

    @POST("/diaries")
    fun requestWriteDiary(
        @HeaderMap header: Map<String, String?>,
        @Body diary: RequestWriteDiary
    ): Call<ResponseWriteDiary>

    @GET("/api/diaries")
    fun requestGetDiaries(
        @HeaderMap header: Map<String, String?>
    ): Call<ResponseDiaryList>

    @GET("/api/diaries/{diaryDate}")
    fun requestGetDiaryDetail(
        @HeaderMap header: Map<String, String?>,
        @Path("diaryDate") date: String
    ): Call<ResponseDiaryList>
}