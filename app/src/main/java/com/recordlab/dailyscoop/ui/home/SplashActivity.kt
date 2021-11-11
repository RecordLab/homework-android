package com.recordlab.dailyscoop.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.recordlab.dailyscoop.MainActivity
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.ui.auth.SignInActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 토큰값 가져오기
        val pref = getSharedPreferences("TOKEN", 0)
        val token = pref.getString("token","false")

        // 로그인 되어있지 않을 시, SignInActivity 이동
        if (token == "false") {
            Handler().postDelayed({
                startActivity(Intent(this, SignInActivity::class.java))
                finish()
            }, 3000)
        // 로그인 되어있을 시, MainActivity 이동
        } else {
            Handler().postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, 3000)
        }
    }
}