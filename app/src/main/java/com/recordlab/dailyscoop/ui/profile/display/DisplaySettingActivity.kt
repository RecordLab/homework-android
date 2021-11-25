package com.recordlab.dailyscoop.ui.profile.display

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.databinding.ActivitySettingDisplayBinding
import com.recordlab.dailyscoop.ui.profile.ProfileFontActivity

class DisplaySettingActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySettingDisplayBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // binding 초기화
        binding = ActivitySettingDisplayBinding.inflate(layoutInflater)
        val view = binding.root
        // 화면 설정
        setContentView(view)

        binding.btnFont.setOnClickListener(this)

        val toolbar = binding.tbSettingDisplay.toolbar
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true)  // custom하기 위해
            actionBar.setDisplayShowTitleEnabled(false)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_black_24)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_font -> {
                val intent = Intent(applicationContext, ProfileFontActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
            R.id.btn_calendar -> {
                Toast.makeText(applicationContext, "캘린더 아이콘 설정", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}