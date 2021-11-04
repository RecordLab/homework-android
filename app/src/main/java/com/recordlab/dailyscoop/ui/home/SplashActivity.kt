package com.recordlab.dailyscoop.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.recordlab.dailyscoop.MainActivity
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.ui.auth.SignUpActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 로그인 되어있지 않을 시, SignUpActivity로
        // 임시로 t/f 집어넣어 놓음
        if (false) {
            Handler().postDelayed({
                startActivity(Intent(this, SignUpActivity::class.java))
                finish()
            }, 3000)
        // 로그인 되어있을 시, MainActivity로
        } else {
            Handler().postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, 3000)
        }
    }
}