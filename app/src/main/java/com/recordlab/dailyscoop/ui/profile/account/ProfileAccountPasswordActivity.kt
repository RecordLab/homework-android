package com.recordlab.dailyscoop.ui.profile.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.databinding.ActivityProfileAccountPasswordBinding
import com.recordlab.dailyscoop.network.RetrofitClient
import com.recordlab.dailyscoop.network.enqueue
import com.recordlab.dailyscoop.network.request.RequestChangePassword

class ProfileAccountPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileAccountPasswordBinding
    private var isChecked:Boolean = false // 입력 형식 체크용 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileAccountPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 입력값 형식 체크
        val passwordTextInput = binding.editTextTextPassword
        passwordTextInput.addTextChangedListener(textWatcher)
        val newPasswordInput = binding.editTextTextPassword1
        newPasswordInput.addTextChangedListener(textWatcher)
        val newPasswordCInput = binding.editTextTextPassword2
        newPasswordCInput.addTextChangedListener(textWatcher)

        val completeBtn = binding.textView8
        completeBtn.setOnClickListener {
            if(isChecked){
                val password = passwordTextInput.text.toString()
                val newPassword = newPasswordInput.text.toString()

                // 헤더
                val pref = getSharedPreferences("TOKEN", 0)
                val header = mutableMapOf<String, String?>()
                header["Content-type"] = "application/json; charset=UTF-8"
                header["Authorization"] = pref.getString("token", "token")

                // 바디
                val data = RequestChangePassword(password, newPassword)
                change(header, data)
            }
        }

        // 뒤로가기 버튼 클릭
        val backBtnClicked = binding.imageView6
        backBtnClicked.setOnClickListener{
            finish()
        }
    }

    private fun change(header: MutableMap<String, String?>, data: RequestChangePassword) {
        RetrofitClient.service.requestChangePassword(header = header, body = data).enqueue(
            onSuccess = {
                when (it.code()){
                    200 ->{
                        Toast.makeText(this, it.body()?.message, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    else ->{
                        Toast.makeText(this,it.message(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
    }

    // 변경할 비밀번호 입력
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            passwordCheck() // 비밀번호 길이 체크 및 비밀번호 확인 체크
        }
        // 입력 전 처리
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        // 입력 중 처리
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    // 비밀번호 길이 및 확인 체크
    private fun passwordCheck() {
        val passwordInput = binding.editTextTextPassword1.text.toString()
        val passwordCInput = binding.editTextTextPassword2.text.toString()
        val length = passwordInput.length

        // 비밀번호 길이가 8~16자이고, 비밀번호와 비밀번호 확인이 같으면 완료 버튼 활성화
        isChecked = if(length in 8..16 && (passwordInput == passwordCInput)){
            binding.textView8.setTextColor(getColor(R.color.underTheSea))
            true
        }else{
            binding.textView8.setTextColor(getColor(R.color.disableGray))
            false
        }
    }


}