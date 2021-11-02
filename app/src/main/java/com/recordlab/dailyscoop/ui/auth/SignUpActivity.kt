package com.recordlab.dailyscoop.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.recordlab.dailyscoop.R

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // 인증 버튼 클릭
        val certBtnClicked = findViewById<Button>(R.id.button)
        certBtnClicked.setOnClickListener{
            Toast.makeText(this.getApplicationContext(),"인증", Toast.LENGTH_SHORT).show();
        }

        // 확인 버튼 클릭
        val checkBtnClicked = findViewById<Button>(R.id.button2)
        checkBtnClicked.setOnClickListener{
            Toast.makeText(this.getApplicationContext(),"확인", Toast.LENGTH_SHORT).show();
        }

        // 가입하기 버튼 클릭
        val realsignupBtnClicked = findViewById<Button>(R.id.button3)
        realsignupBtnClicked.setOnClickListener{
            Toast.makeText(this.getApplicationContext(),"회원가입", Toast.LENGTH_SHORT).show();
        }

        // 뒤로가기 버튼 클릭
        val signupBackBtnClicked = findViewById<ImageView>(R.id.imageView9)
        signupBackBtnClicked.setOnClickListener{
            finish()
        }
    }
}