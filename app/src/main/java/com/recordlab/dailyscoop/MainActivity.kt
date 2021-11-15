package com.recordlab.dailyscoop

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.recordlab.dailyscoop.databinding.ActivityMainBinding
import com.recordlab.dailyscoop.ui.auth.SignInActivity
import com.recordlab.dailyscoop.ui.profile.SignOutDialogInterface
import com.recordlab.dailyscoop.ui.profile.lock.AppLock
import com.recordlab.dailyscoop.ui.profile.lock.AppLockConst
import com.recordlab.dailyscoop.ui.profile.lock.AppPasswordActivity

class MainActivity : AppCompatActivity(), SignOutDialogInterface {
    var lock = true
    private val finishIntervalTime : Long = 3000
    private var backPressedTime : Long = 0

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val navView = binding.navView

        init()

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_history,
                R.id.navigation_profile
            )
        )
        val toolbar = binding.tbMain
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowCustomEnabled(true)  // custom하기 위해
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        toolbar.elevation = 2F // 툴바 위로 가져오기.

        setupActionBarWithNavController(navController, appBarConfiguration)
        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            AppLockConst.UNLOCK_PASSWORD ->
                if (resultCode == Activity.RESULT_OK) {
                    lock = false
                }
        }
    }

    override fun onStart() {
        super.onStart()
        if (lock && AppLock(this).isPassLockSet()) {
            val intent = Intent(this, AppPasswordActivity::class.java).apply {
                putExtra(AppLockConst.type, AppLockConst.UNLOCK_PASSWORD)
            }
            startActivityForResult(intent, AppLockConst.UNLOCK_PASSWORD)
        }
    }

//    override fun onPause() {
//        super.onPause()
//        if (AppLock(this).isPassLockSet()) {
//            lock = true
//        }
//    }

    private fun init() {
        lock = if (AppLock(this).isPassLockSet()) true else false
    }

    // 로그아웃 다이얼로그 확인 버튼 클릭
    override fun okBtnClicked() {
        Toast.makeText(this, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()

        // 토큰 제거
        val pref = getSharedPreferences("TOKEN", 0)
        val edit = pref.edit() // 수정모드(추가, 수정)
        edit.remove("token") // key, value
        edit.apply() // 저장 완료
        //val to = pref.getString("token","없음")
        //Toast.makeText(this, "토큰 : $to", Toast.LENGTH_SHORT).show()

        // 로그인 액티비티로 이동
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        val tempTime = System.currentTimeMillis()
        val intervalTime = tempTime - backPressedTime

        if (0 <= intervalTime && finishIntervalTime >= intervalTime) {
            super.onBackPressed();
        }
        else {
            backPressedTime = tempTime;
            val message = R.string.app_backbutton_finish
            Toast.makeText(getApplicationContext(), "'뒤로'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }
}