package com.recordlab.dailyscoop.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.recordlab.dailyscoop.R

class ProfileAccountPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            R.layout.activity_profile_account_password)

        // 뒤로가기 버튼 클릭
        val backBtnClicked = findViewById<ImageView>(R.id.imageView6)
        backBtnClicked.setOnClickListener{
            finish()
        }
    }
}