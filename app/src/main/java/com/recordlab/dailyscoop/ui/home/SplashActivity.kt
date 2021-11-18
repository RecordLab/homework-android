package com.recordlab.dailyscoop.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.recordlab.dailyscoop.MainActivity
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.network.RetrofitClient.service
import com.recordlab.dailyscoop.network.enqueue
import com.recordlab.dailyscoop.ui.auth.SignInActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 토큰값 가져오기
        val pref = getSharedPreferences("TOKEN", 0)
        val token = pref.getString("token","false")

        val header = mutableMapOf<String, String?>()

        // 로그인 되어있지 않을 시, SignInActivity 이동
        if (token == "false") {
            goSignIn()
        // 로그인 되어있을 시, MainActivity 이동
        } else {
            // 토큰 만료 확인
            header["Content-type"] = "application/json; charset=UTF-8"
            header["Authorization"] = token

            service.requestUserInfo2(header = header).enqueue(
                onSuccess = {
                    when(it.code()){
                        in 200..209 -> { // 정상 토큰
//                            val edit = pref.edit()
//                            edit.putString("email", it.body()?.id)
//                            edit.putString("nickname", it.body()?.nickname)
//                            edit.apply()
                            goMain()
                        }
                        else -> { // 비정상 토큰
                            goSignIn()
                        }
                    }
                }
            )


        }
    }

    private fun goSignIn() {
        Handler().postDelayed({
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }, 2000)
    }

    private fun goMain() {
        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000)
    }

}