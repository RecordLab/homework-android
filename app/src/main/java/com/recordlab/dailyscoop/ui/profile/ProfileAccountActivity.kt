package com.recordlab.dailyscoop.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.recordlab.dailyscoop.R

class ProfileAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_account)

        val emailBtnClicked = findViewById<View>(R.id.bg4)
        emailBtnClicked.setOnClickListener{
            val intent = Intent(this, ProfileAccountEmailActivity::class.java)
            startActivity(intent)
        }

        val nickBtnClicked = findViewById<View>(R.id.bg45)
        nickBtnClicked.setOnClickListener{
            val intent = Intent(this, ProfileAccountNicknameActivity::class.java)
            startActivity(intent)
        }

        val passBtnClicked = findViewById<View>(R.id.bg454)
        passBtnClicked.setOnClickListener{
            val intent = Intent(this, ProfileAccountPasswordActivity::class.java)
            startActivity(intent)
        }

        val disconBtnClicked = findViewById<View>(R.id.bg4544)
        disconBtnClicked.setOnClickListener{
            Toast.makeText(this.getApplicationContext(),"이것은 회원 탈퇴 메시지입니다.", Toast.LENGTH_SHORT).show();
        }

    }
}