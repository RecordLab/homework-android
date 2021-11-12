package com.recordlab.dailyscoop.ui.profile.account

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.databinding.ActivityProfileAccountBinding
import com.recordlab.dailyscoop.ui.profile.ProfileFontActivity
import com.recordlab.dailyscoop.ui.profile.notice.ProfileNoticeDialog

class ProfileAccountActivity : AppCompatActivity(), ProfileWithdrawDialogInterface {
    private lateinit var binding: ActivityProfileAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityProfileAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadData()

        val emailBtnClicked = binding.bg4
        emailBtnClicked.setOnClickListener{
            val intent = Intent(this, ProfileAccountEmailActivity::class.java)
            startActivity(intent)
        }

        val nickBtnClicked = binding.bg45
        nickBtnClicked.setOnClickListener{
            val intent = Intent(this, ProfileAccountNicknameActivity::class.java)
            startActivity(intent)
        }

        val passBtnClicked = binding.bg454
        passBtnClicked.setOnClickListener{
            val intent = Intent(this, ProfileAccountPasswordActivity::class.java)
            startActivity(intent)

//            val intent = Intent(this, SignInActivity::class.java)
//            startActivity(intent)
        }

        val disconnectBtnClicked = binding.bg4544
        disconnectBtnClicked.setOnClickListener{
            //Toast.makeText(this.getApplicationContext(),"이것은 회원 탈퇴 메시지입니다.", Toast.LENGTH_SHORT).show();

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
    }

    override fun dialogOkBtnClicked() {
        Toast.makeText(this,"사실 탈퇴는 할 수 없습니다 \uD83D\uDE1C", Toast.LENGTH_SHORT).show();
    }
}