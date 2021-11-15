package com.recordlab.dailyscoop.ui.profile.account

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.recordlab.dailyscoop.MainActivity
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.databinding.ActivityProfileAccountBinding
import com.recordlab.dailyscoop.network.RetrofitClient
import com.recordlab.dailyscoop.network.enqueue
import com.recordlab.dailyscoop.ui.profile.ProfileFontActivity
import com.recordlab.dailyscoop.ui.profile.notice.ProfileNoticeDialog
import okhttp3.OkHttpClient




class ProfileAccountActivity : AppCompatActivity(), ProfileWithdrawDialogInterface {
    private lateinit var binding: ActivityProfileAccountBinding
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityProfileAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 초기 데이터 가져오기(이메일, 닉네임)
        loadData()

        // 변경한 닉네임 가져와서 바꿔 주기
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val nickname = it.data?.getStringExtra("nickname") ?: "error"
                binding.textView2.text = nickname

            }
        }

        // 이메일 변경 버튼 클릭(보류)
//        val emailBtnClicked = binding.bg4
//        emailBtnClicked.setOnClickListener{
//            val intent = Intent(this, ProfileAccountEmailActivity::class.java)
//            startActivity(intent)
//        }

        // 닉네임 변경 버튼 클릭
        val nickBtnClicked = binding.bg45
        nickBtnClicked.setOnClickListener{
            val intent = Intent(this, ProfileAccountNicknameActivity::class.java)
            activityResultLauncher.launch(intent)
        }

        // 비밀번호 변경 버튼 클릭
        val passBtnClicked = binding.bg454
        passBtnClicked.setOnClickListener{
            val intent = Intent(this, ProfileAccountPasswordActivity::class.java)
            startActivity(intent)
        }

        // 회원 탈퇴 버튼 클릭
        val disconnectBtnClicked = binding.bg4544
        disconnectBtnClicked.setOnClickListener{
            // 회원탈퇴 다이얼로그
            val withdrawDialog = ProfileWithdrawDialog(this, this)
            withdrawDialog.show()
        }

        val fontBtnClicked = binding.bg8
        fontBtnClicked.setOnClickListener{
            val intent = Intent(this, ProfileFontActivity::class.java)
            startActivity(intent)
        }

        // 뒤로가기 버튼 클릭
        val backBtnClicked = findViewById<ImageView>(R.id.backBtn)
        backBtnClicked.setOnClickListener{
            finish()
        }

    }

    private fun loadData() {
        val pref = getSharedPreferences("TOKEN", 0)
        binding.textView.text = pref.getString("email", "error")
        binding.textView2.text = pref.getString("nickname", "error")
//        sharedPref = getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
//        header["Content-type"] = "application/json; charset=UTF-8"
//        header["Authorization"] = sharedPref.getString("token", "token")
//        val email = sharedPref.getString("email", "error")
//
//        service.requestUserInfo(header = header, userID = email!!).enqueue(
//            onSuccess = {
//                when (it.code()){
//                    in 200..209 -> {
//                        binding.textView.text = it.body()?.id
//                        binding.textView2.text = it.body()?.nickname
//                    }
//                    401 -> {
//                        Toast.makeText(this.applicationContext,it.message(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        )
    }

    override fun dialogOkBtnClicked() {
        Toast.makeText(this,"사실 탈퇴는 할 수 없습니다 \uD83D\uDE1C", Toast.LENGTH_SHORT).show();
    }
}