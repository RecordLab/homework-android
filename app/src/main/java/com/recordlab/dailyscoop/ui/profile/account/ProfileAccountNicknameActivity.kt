package com.recordlab.dailyscoop.ui.profile.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.databinding.ActivityProfileAccountNicknameBinding
import com.recordlab.dailyscoop.network.RetrofitClient.service
import com.recordlab.dailyscoop.network.enqueue
import com.recordlab.dailyscoop.network.request.RequestChangeNickname
import android.content.Intent

import android.R.attr.name




class ProfileAccountNicknameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileAccountNicknameBinding
    private var isPossible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityProfileAccountNicknameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 입력 체크
        val emailTextInput = binding.editTextTextEmailAddress
        emailTextInput.addTextChangedListener(textWatcher)

        val completeBtn = binding.textView8
        completeBtn.setOnClickListener {
            if(isPossible){
                val newNickname = emailTextInput.text.toString()

                val pref = getSharedPreferences("TOKEN", 0)
                val header = mutableMapOf<String, String?>()
                header["Content-type"] = "application/json; charset=UTF-8"
                header["Authorization"] = pref.getString("token", "token")
                //Log.d("token", "헤더 : ${header["Authorization"]!!}")
                val data = RequestChangeNickname(newNickname)

                change(header, data)
            }
        }

        // 뒤로가기 버튼 클릭
        val backBtnClicked = binding.imageView6
        backBtnClicked.setOnClickListener{
            finish()
        }
    }

    private fun change(header: MutableMap<String, String?>, data: RequestChangeNickname) {
        service.requestChangeNickname(header = header, body = data).enqueue(
            onSuccess = {
                when (it.code()){
                    200 ->{
                        Toast.makeText(this, it.body()?.message, Toast.LENGTH_SHORT).show()
                        val pref = getSharedPreferences("TOKEN", 0)
                        val edit = pref.edit()
                        edit.putString("nickname", binding.editTextTextEmailAddress.text.toString())
                        edit.apply()

                        val intent = Intent(this, ProfileAccountActivity::class.java).apply {
                            putExtra("nickname", binding.editTextTextEmailAddress.text.toString())
                        }
                        setResult(RESULT_OK, intent)
                        finish()

                        // 새로고침
//                        overridePendingTransition(0, 0) //인텐트 효과 없애기
//                        val intent = intent //인텐트
//                        startActivity(intent) //액티비티 열기
//                        overridePendingTransition(0, 0) //인텐트 효과 없애기

                    }
                    else ->{
                        Toast.makeText(this,it.message(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
    }

    // 변경할 닉네임 입력
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val text = binding.editTextTextEmailAddress.text ?: ""
            val length = text.toString().length
            isPossible = when (length) {
                in 2..8 -> { // 닉네임 2~8자 제한
                    binding.textView8.setTextColor(getColor(R.color.underTheSea))
                    true
                }
                else -> {
                    binding.textView8.setTextColor(getColor(R.color.disableGray))
                    false
                }
            }
        }
        // 입력 전 처리
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        // 입력 중 처리
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
}