package com.recordlab.dailyscoop.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.recordlab.dailyscoop.R

class ProfileFriendActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_friend)

        val click1 = findViewById<View>(R.id.bg4)
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

    }
}