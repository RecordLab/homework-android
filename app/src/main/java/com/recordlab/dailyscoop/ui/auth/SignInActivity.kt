package com.recordlab.dailyscoop.ui.auth

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.recordlab.dailyscoop.R

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // 가입하기 버튼 클릭
        val signupBtnClicked = findViewById<Button>(R.id.sign_yet2)
        signupBtnClicked.setOnClickListener{
            //Toast.makeText(this.getApplicationContext(),"이것은 회원 탈퇴 메시지입니다.", Toast.LENGTH_SHORT).show();

            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)

        }

        // 로그인 버튼 클릭
        val loginBtnClicked = findViewById<Button>(R.id.login_button)
        loginBtnClicked.setOnClickListener{
            Toast.makeText(this.getApplicationContext(),"로그인", Toast.LENGTH_SHORT).show();

        }

        // 비밀번호 찾기 버튼 클릭
        val findpasswordBtnClicked = findViewById<Button>(R.id.login_password_button)
        findpasswordBtnClicked.setOnClickListener{
            //Toast.makeText(this.getApplicationContext(),"비밀번호 찾기", Toast.LENGTH_SHORT).show();
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        // 카카오로그인 버튼 클릭
        val kakaoBtnClicked = findViewById<Button>(R.id.kakao_btn)
        kakaoBtnClicked.setOnClickListener{
            Toast.makeText(this.getApplicationContext(),"kakao", Toast.LENGTH_SHORT).show();

        }

        // 가입하기 버튼 클릭
        val googleBtnClicked = findViewById<Button>(R.id.google_btn)
        googleBtnClicked.setOnClickListener{
            Toast.makeText(this.getApplicationContext(),"google", Toast.LENGTH_SHORT).show();

        }
    }
}