package com.recordlab.dailyscoop.ui.auth

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.recordlab.dailyscoop.databinding.ActivitySignUpBinding
import com.recordlab.dailyscoop.network.RetrofitClient.service
import com.recordlab.dailyscoop.network.request.RequestSignup
import com.recordlab.dailyscoop.network.enqueue

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 인증 버튼 클릭
        val certBtnClicked = binding.button
        certBtnClicked.setOnClickListener{
            Toast.makeText(this.getApplicationContext(),"인증", Toast.LENGTH_SHORT).show()
        }

        // 확인 버튼 클릭
        val checkBtnClicked = binding.button2
        checkBtnClicked.setOnClickListener{
            Toast.makeText(this.getApplicationContext(),"확인", Toast.LENGTH_SHORT).show()
        }

        // 가입하기 버튼 클릭
        val realSignupBtnClicked = binding.button3
        realSignupBtnClicked.setOnClickListener{
            val email = binding.editTextTextEmailAddress4.text.toString()
            val nickname = binding.editTextTextEmailAddress5.text.toString()
            val password = binding.editTextTextPassword3.text.toString()

            val data = RequestSignup(email, nickname, password)
            //Toast.makeText(this.getApplicationContext(),email, Toast.LENGTH_SHORT).show()
            signUp(data)
        }

        // 뒤로가기 버튼 클릭
        val signupBackBtnClicked = binding.imageView9
        signupBackBtnClicked.setOnClickListener{
            finish()
        }
    }

    private fun signUp(data: RequestSignup) {
        service.requestSignup(body = data).enqueue(
            onSuccess = {
                //Log.d("로그 회원가입", it.code().toString())
                when (it.code()){
                    in 200..299 ->{
                        Log.d("로그 회원가입", it.code().toString())
                    }
                    400 -> {
                        Toast.makeText(this.getApplicationContext(),"이미 가입된 계정입니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        )
    }


}