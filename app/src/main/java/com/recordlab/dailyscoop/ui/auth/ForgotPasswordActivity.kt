package com.recordlab.dailyscoop.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.recordlab.dailyscoop.R

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val backBtnClicked10 = findViewById<ImageView>(R.id.imageView99)
        backBtnClicked10.setOnClickListener{
            finish()
        }


    }

    fun callFragment() {
        val passwordNeFragment: PasswordNewFragment = PasswordNewFragment()

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, passwordNeFragment)
        transaction.commit()
    }

}