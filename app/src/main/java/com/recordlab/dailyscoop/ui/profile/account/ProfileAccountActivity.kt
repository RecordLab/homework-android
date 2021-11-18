package com.recordlab.dailyscoop.ui.profile.account

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.recordlab.dailyscoop.MainActivity
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.databinding.ActivityProfileAccountBinding
import com.recordlab.dailyscoop.network.RetrofitClient
import com.recordlab.dailyscoop.network.RetrofitClient.service
import com.recordlab.dailyscoop.network.enqueue
import com.recordlab.dailyscoop.ui.home.SplashActivity
import com.recordlab.dailyscoop.ui.profile.ProfileFontActivity
import com.recordlab.dailyscoop.ui.profile.notice.ProfileNoticeDialog
import okhttp3.OkHttpClient
import kotlin.system.exitProcess


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

        // 소셜 로그인 시 비밀번호 변경 버튼 숨김
        val pref = getSharedPreferences("TOKEN", 0)
        if (pref.contains("social")) {
            binding.bg454.visibility = View.INVISIBLE
            binding.bg454.layoutParams.height = 0
            binding.tvChangePassword.visibility = View.INVISIBLE
            binding.tvChangePassword.layoutParams.height = 0
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

//        val fontBtnClicked = binding.bg8
//        fontBtnClicked.setOnClickListener{
//            val intent = Intent(this, ProfileFontActivity::class.java)
//            startActivity(intent)
//        }

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
        //Toast.makeText(this,"사실 탈퇴는 할 수 없습니다 \uD83D\uDE1C", Toast.LENGTH_SHORT).show();
        val pref = getSharedPreferences("TOKEN", 0)
        val header = mutableMapOf<String, String?>()
        header["Content-type"] = "application/json; charset=UTF-8"
        header["Authorization"] = pref.getString("token", "token")
        val userID = pref.getString("email", "token")
        service.requestDeleteAccount(header = header, userID = userID!!).enqueue(
            onSuccess = {
                when (it.code()){
                    200 ->{
                        Toast.makeText(this, it.body()?.message, Toast.LENGTH_SHORT).show()

                        // 토큰 지우기
                        val edit = pref.edit()
                        edit.remove("token")
                        edit.apply()

                        Handler().postDelayed({
                            finishAffinity() //해당 앱의 루트 액티비티를 종료시킨다. (API  16미만은 ActivityCompat.finishAffinity())
                            System.runFinalization() //현재 작업중인 쓰레드가 다 종료되면, 종료 시키라는 명령어이다.
                            val intent = Intent(this, SplashActivity::class.java)
                            startActivity(intent) // 액티비티 재시작
                            exitProcess(0) // 현재 액티비티를 종료시킨다.
                        },1000L)
                    }
                    else ->{
                        Toast.makeText(this,it.message(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
    }
}