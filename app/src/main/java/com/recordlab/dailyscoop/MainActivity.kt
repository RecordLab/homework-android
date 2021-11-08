package com.recordlab.dailyscoop

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.recordlab.dailyscoop.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.recordlab.dailyscoop.ui.profile.ProfileFriendActivity
import com.recordlab.dailyscoop.ui.profile.lock.AppLock
import com.recordlab.dailyscoop.ui.profile.lock.AppLockConst
import com.recordlab.dailyscoop.ui.profile.lock.AppPasswordActivity

class MainActivity : AppCompatActivity() {
    var lock = true

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

}