package com.recordlab.dailyscoop.ui.profile.lock

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.Switch
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.databinding.ActivityProfileLockBinding

class ProfileLockActivity : AppCompatActivity() {

    private var _binding : ActivityProfileLockBinding? = null
    private val binding get() = _binding!!
//    private lateinit var getResultText: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityProfileLockBinding.inflate(layoutInflater)

        setContentView(binding.root)

        init()

        val switch = binding.switch1
        switch.setOnCheckedChangeListener { buttonView, isChecked ->
             if (isChecked) {
                // off -> on
//                val intent = Intent(this, AppPasswordActivity::class.java)
//                getResultText.launch(intent)
                val intent = Intent(this, AppPasswordActivity::class.java).apply {
                    putExtra(AppLockConst.type, AppLockConst.ENABLE_PASSLOCK)
                }
                startActivityForResult(intent, AppLockConst.ENABLE_PASSLOCK)

            } else {
                // on -> off
                 val intent = Intent(this, AppPasswordActivity::class.java).apply {
                     putExtra(AppLockConst.type, AppLockConst.DISABLE_PASSLOCK)
                 }
                 startActivityForResult(intent, AppLockConst.DISABLE_PASSLOCK)
            }
        }

//        getResultText = registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()){ result ->
//            if (result.resultCode == RESULT_OK){
//                val mBool = result.data?.getBooleanExtra("lockStatus", false)
//                isLockOn = mBool ?: false
//            }
//        }

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
        // 잠금 설정 상태 가져오기
        val pref = getSharedPreferences("appLock", 0)
        binding.switch1.isChecked = (pref.getString("applock", "0") != "0")

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

    // 종료
    override fun onDestroy() {
        super.onDestroy()
    }
}