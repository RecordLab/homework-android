package com.recordlab.dailyscoop.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.recordlab.dailyscoop.R

class ProfileFriendActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_friend)

        val click1 = findViewById<View>(R.id.view_lock_setting_toggle)
        val click2 = findViewById<View>(R.id.bg45)
        val click3 = findViewById<View>(R.id.bg454)

        click1.setOnClickListener{
            Toast.makeText(this.getApplicationContext(),"친구초대", Toast.LENGTH_SHORT).show()
        }

        click2.setOnClickListener{
            Toast.makeText(applicationContext,"팔로우", Toast.LENGTH_SHORT).show()
        }
        click3.setOnClickListener{
            Toast.makeText(applicationContext,"검색", Toast.LENGTH_SHORT).show()
        }

        // 뒤로가기 버튼 클릭
        val backBtnClicked = findViewById<ImageView>(R.id.imageView6)
        backBtnClicked.setOnClickListener{
            finish()
        }

        /*val test_BtnClicke = findViewById<Button>(R.id.test_Btn)
        test_BtnClicke.setOnClickListener{
            uploadImage()
        }*/
    }

    // 테스트
    private fun uploadImage() {
        val pref = getSharedPreferences("com.example.DailyScoop.PREFERENCE_FILE_KEY", 0)
        val path = pref.getString("profileImage", "null")
        Toast.makeText(applicationContext,path, Toast.LENGTH_SHORT).show()
    }
}