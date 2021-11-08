package com.recordlab.dailyscoop.ui.profile

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.Switch
import android.widget.Toast
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.databinding.ActivityProfileLockBinding
import com.recordlab.dailyscoop.ui.profile.lock.AppLock
import com.recordlab.dailyscoop.ui.profile.lock.AppLockConst
import com.recordlab.dailyscoop.ui.profile.lock.AppPasswordActivity

class ProfileLockActivity : AppCompatActivity() {

    private var _binding : ActivityProfileLockBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityProfileLockBinding.inflate(layoutInflater)

        setContentView(binding.root)

        init()

        val switch = findViewById<Switch>(R.id.switch1)
        // val switchValue = switch.isSelected // 현재 상태
        switch.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if(isChecked){
                    // off -> on
                    Toast.makeText(applicationContext,"off -> on", Toast.LENGTH_SHORT).show();
                }else{
                    // on -> off
                    Toast.makeText(applicationContext,"on -> off", Toast.LENGTH_SHORT).show();
                }
            }
        })

        // 뒤로가기 버튼 클릭
        val backBtnClicked = findViewById<ImageView>(R.id.imageView6)
        backBtnClicked.setOnClickListener{
            finish()
        }

        binding.btnSetlock.setOnClickListener {
            val intent = Intent(this, AppPasswordActivity::class.java).apply {
                putExtra(AppLockConst.type, AppLockConst.ENABLE_PASSLOCK)
            }
            startActivityForResult(intent, AppLockConst.ENABLE_PASSLOCK)

        }

        binding.btnDellock.setOnClickListener {
            val intent = Intent(this, AppPasswordActivity::class.java).apply {
                putExtra(AppLockConst.type, AppLockConst.DISABLE_PASSLOCK)
            }
            startActivityForResult(intent, AppLockConst.DISABLE_PASSLOCK)
        }

        binding.btnChangelock.setOnClickListener {
            val intent = Intent(this, AppPasswordActivity::class.java).apply {
                putExtra(AppLockConst.type, AppLockConst.CHANGE_PASSWORD)
            }
            startActivityForResult(intent, AppLockConst.CHANGE_PASSWORD)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            AppLockConst.ENABLE_PASSLOCK ->
                if (resultCode == Activity.RESULT_OK) {
                    init()
                }

            AppLockConst.DISABLE_PASSLOCK ->
                if (resultCode == Activity.RESULT_OK) {
                    init()
                }

            AppLockConst.CHANGE_PASSWORD ->
                if (resultCode == Activity.RESULT_OK) {
                    init()
                }
        }
    }

    private fun init() {
        if (AppLock(this).isPassLockSet()) {
            binding.btnSetlock.isEnabled = false
            binding.btnDellock.isEnabled = true
            binding.btnChangelock.isEnabled = true
        } else {
            binding.btnSetlock.isEnabled = true
            binding.btnDellock.isEnabled = false
            binding.btnChangelock.isEnabled = false
        }
    }
}