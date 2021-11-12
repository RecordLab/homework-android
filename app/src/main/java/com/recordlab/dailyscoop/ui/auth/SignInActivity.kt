package com.recordlab.dailyscoop.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.recordlab.dailyscoop.MainActivity
import com.recordlab.dailyscoop.databinding.ActivitySignInBinding
import com.recordlab.dailyscoop.network.RetrofitClient.service
import com.recordlab.dailyscoop.network.request.RequestSignIn
import com.recordlab.dailyscoop.network.enqueue
import com.recordlab.dailyscoop.ui.profile.ProfileFontActivity

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

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
            Toast.makeText(this.getApplicationContext(),"kakao", Toast.LENGTH_SHORT).show();

        }

        // 구글 로그인 버튼 클릭
        val googleBtnClicked = binding.googleBtn
        googleBtnClicked.setOnClickListener{
            Toast.makeText(this.getApplicationContext(),"google", Toast.LENGTH_SHORT).show();

        }
    }

    private fun signIn(data: RequestSignIn) {
        service.requestSingin(body = data).enqueue(
            onSuccess = {
                when (it.code()){
                    in 200..209 -> {
                        val nic = it.body()?.nickname
                        val to = it.body()?.token
                        Toast.makeText(this.applicationContext,"$nic 님 반갑습니다", Toast.LENGTH_SHORT).show();

                        val pref = getSharedPreferences("TOKEN", 0)
                        val edit = pref.edit() // 수정모드(추가, 수정)
                        edit.putString("token", "Bearer ".plus(to)) // key, value
                        edit.apply() // 저장 완료

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    401 -> {
                        Toast.makeText(this.applicationContext,it.message(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        )


    }
}