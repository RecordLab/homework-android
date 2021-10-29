package com.recordlab.dailyscoop.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.Toast
import com.recordlab.dailyscoop.R

class ProfileNoticeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_notice)

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
    }
}