package com.recordlab.dailyscoop.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.kakao.sdk.auth.model.Prompt
import com.kakao.sdk.user.UserApiClient
import com.google.firebase.auth.FirebaseAuth
import com.recordlab.dailyscoop.MainActivity
import com.recordlab.dailyscoop.databinding.ActivitySignInBinding
import com.recordlab.dailyscoop.network.RetrofitClient.service
import com.recordlab.dailyscoop.network.request.RequestSignIn
import com.recordlab.dailyscoop.network.enqueue
import com.recordlab.dailyscoop.ui.profile.ProfileFontActivity

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var mAuth: FirebaseAuth
    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mAuth = FirebaseAuth.getInstance()
        // 가입하기 버튼 클릭
        val signupBtnClicked = binding.signYet2
        signupBtnClicked.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // 로그인 버튼 클릭
        val loginBtnClicked = binding.loginButton
        loginBtnClicked.setOnClickListener{
            //Toast.makeText(this.getApplicationContext(),"로그인", Toast.LENGTH_SHORT).show();
            val email = binding.loginEmailText.text.toString()
            val password = binding.loginPasswordText.text.toString()

            val data = RequestSignIn(email, password)
            signIn(data)

            val pref = getSharedPreferences("TOKEN", 0)
            val edit = pref.edit() // 수정모드(추가, 수정)
            edit.putString("email", email) // key, value
            edit.apply() // 저장 완료
        }

        // 비밀번호 찾기 버튼 클릭
        val findPasswordBtnClicked = binding.loginPasswordButton
        findPasswordBtnClicked.setOnClickListener{
            //Toast.makeText(this.getApplicationContext(),"비밀번호 찾기", Toast.LENGTH_SHORT).show();
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        // 카카오로그인 버튼 클릭
        val socialKBtnClicked = binding.kakaoBtn
        socialKBtnClicked.setOnClickListener{
            UserApiClient.instance.loginWithKakaoAccount(this, prompts = listOf(Prompt.LOGIN)) { token, error ->
                if (error != null) {
                    Toast.makeText(this.applicationContext,"다시 시도해 주세요", Toast.LENGTH_SHORT).show();
                }
                else if (token != null) {
                    Log.i("kakao", "로그인 성공 ${token.accessToken}")

                    // 카카오 토큰 서버로 전송
                    val header = mutableMapOf<String, String?>()
                    header["Authorization"] = token.accessToken
                    service.requestSocial(header = header, type = "kakao").enqueue(
                        onSuccess = {
                            when(it.code()){
                                in 200..209 -> {
                                    val nic = it.body()?.nickname
                                    val to = it.body()?.token
                                    Toast.makeText(this.applicationContext,"반갑습니다", Toast.LENGTH_SHORT).show();

                                    val pref = getSharedPreferences("TOKEN", 0)
                                    val edit = pref.edit() // 수정모드(추가, 수정)
                                    edit.putString("token", "Bearer ".plus(to)) // key, value
                                    edit.putString("nickname", nic)
                                    edit.apply() // 저장 완료

                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                else -> {
                                    Toast.makeText(this.applicationContext,"다시 시도해 주세요", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    )

//                    // 사용자 정보 요청 (기본)
//                    UserApiClient.instance.me { user, errorr ->
//                        if (errorr != null) {
//                            Log.e("kakao", "사용자 정보 요청 실패", errorr)
//                        }
//                        else if (user != null) {
//                            Log.i("kakao", "사용자 정보 요청 성공" +
//                                    "\n회원번호: ${user.id}" +
//                                    "\n이메일: ${user.kakaoAccount?.email}" +
//                                    "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
//                                    "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")
//                        }
//                    }
                }
            }
        }

        // 구글 로그인 버튼 클릭
//        val googleBtnClicked = binding.googleBtn
//        googleBtnClicked.setOnClickListener{
//            Toast.makeText(this.getApplicationContext(),"google", Toast.LENGTH_SHORT).show();
//
//        }
    }

    private fun signIn(data: RequestSignIn) {
        service.requestSignIn(body = data).enqueue(
            onSuccess = {
                when (it.code()){
                    in 200..209 -> {
                        val nic = it.body()?.nickname
                        val to = it.body()?.token
                        Toast.makeText(this.applicationContext,"반갑습니다", Toast.LENGTH_SHORT).show();

                        val pref = getSharedPreferences("TOKEN", 0)
                        val edit = pref.edit() // 수정모드(추가, 수정)
                        edit.putString("token", "Bearer ".plus(to)) // key, value
                        edit.putString("nickname", nic)
                        edit.apply() // 저장 완료

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    401 -> {
                        Toast.makeText(this.applicationContext,"아이디나 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        )


    }

}