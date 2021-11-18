package com.recordlab.dailyscoop.ui.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.databinding.ActivitySignUpBinding
import com.recordlab.dailyscoop.network.RetrofitClient.service
import com.recordlab.dailyscoop.network.request.RequestSignup
import com.recordlab.dailyscoop.network.enqueue

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private var isChecked1:Boolean = false // 입력 형식 체크용 변수(통일 필요)
    private var isChecked2:Boolean = false
    private var isChecked3:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 인증 버튼 클릭
        val certBtnClicked = binding.button
        certBtnClicked.setOnClickListener{
            Toast.makeText(this,"인증", Toast.LENGTH_SHORT).show()
        }

//        // 확인 버튼 클릭
//        val checkBtnClicked = binding.button2
//        checkBtnClicked.setOnClickListener{
//            Toast.makeText(this.getApplicationContext(),"확인", Toast.LENGTH_SHORT).show()
//        }

        // 입력값 형식 체크
        val emailTextInput = binding.editTextTextEmailAddress4
        emailTextInput.addTextChangedListener(textWatcher)
        val nameTextInput = binding.editTextTextEmailAddress5
        nameTextInput.addTextChangedListener(textWatcher)
        val passwordInput = binding.editTextTextPassword3
        passwordInput.addTextChangedListener(textWatcher)
        val passwordCInput = binding.editTextTextPassword4
        passwordCInput.addTextChangedListener(textWatcher)

        // 가입하기 버튼 클릭
        val realSignupBtnClicked = binding.button3
        realSignupBtnClicked.setOnClickListener{
            if(isChecked1 && isChecked2 && isChecked3) {
                val email = emailTextInput.text.toString()
                val nickname = nameTextInput.text.toString()
                val password = passwordInput.text.toString()

                val data = RequestSignup(email, nickname, password)
                signUp(data)
            }
        }

        // 뒤로가기 버튼 클릭
        val signupBackBtnClicked = binding.imageView9
        signupBackBtnClicked.setOnClickListener{
            finish()
        }
    }

    // 이메일 또는 비밀번호를 입력할 때
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            emailCheck() // 닉네임 길이 체크
            passwordCheck() // 비밀번호 길이 체크 및 비밀번호 확인 체크
            val signUpBtn = binding.button3
            if(isChecked1 && isChecked2 && isChecked3){
                // 입력 형식 만족한 경우 가입 버튼 활성화
                signUpBtn.setBackgroundResource(R.drawable.yellow_rectangle)
            }else{
                signUpBtn.setBackgroundResource(R.drawable.gray_rectangle)
            }
        }
        // 입력 전 처리
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        // 입력 중 처리
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    // 닉네임 길이 체크
    private fun emailCheck() {
        val nameTextInput = binding.editTextTextEmailAddress5
        val nameCheck = binding.imageView11
        val length = nameTextInput.text.toString().length
        isChecked1 = if (length in 2..8) {
            // 2~8자인 경우 체크 표시 활성화
            nameCheck.setImageResource(R.drawable.ic_baseline_check_24_green)
            true
        }else{
            nameCheck.setImageResource(R.drawable.ic_baseline_check_24)
            false
        }
    }

    // 비밀번호 길이 및 확인 체크
    private fun passwordCheck() {
        val passwordInput = binding.editTextTextPassword3.text.toString()
        val passwordCInput = binding.editTextTextPassword4.text.toString()
        val passwordCheck1 = binding.imageView12
        val passwordCheck2 = binding.imageView13
        val length = passwordInput.length
        isChecked2 = if (length in 8..16) {
            // 8~16자인 경우 체크 표시 활성화
            passwordCheck1.setImageResource(R.drawable.ic_baseline_check_24_green)
            true
        }else{
            passwordCheck1.setImageResource(R.drawable.ic_baseline_check_24)
            false
        }

        isChecked3 = if (passwordInput == passwordCInput && length > 0) {
            // 비밀번호와 비밀번호 확인이 같은 경우 체크 표시 활성화
            passwordCheck2.setImageResource(R.drawable.ic_baseline_check_24_green)
            true
        }else{
            passwordCheck2.setImageResource(R.drawable.ic_baseline_check_24)
            false
        }
    }

    // 회원가입 api
    private fun signUp(data: RequestSignup) {
        service.requestSignup(body = data).enqueue(
            onSuccess = {
                when (it.code()){
                    201 ->{
                        // 정상적으로 가입한 경우, activity 종료(로그인 화면에서 새 액티비티를 시작했으므로 로그인 화면으로 돌아감)
                        Toast.makeText(this,"회원가입을 완료했습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    400 -> {
                        Toast.makeText(this,"이미 가입한 계정입니다.", Toast.LENGTH_SHORT).show()
                    }
                    else ->{
                        Log.d("로그 회원가입", it.code().toString())
                    }
                }
            }

        )
    }


}