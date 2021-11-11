package com.recordlab.dailyscoop.ui.profile.notice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.Switch
import android.widget.Toast
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.databinding.ActivityProfileNoticeBinding
import com.recordlab.dailyscoop.databinding.ActivitySignUpBinding

class ProfileNoticeActivity : AppCompatActivity(), ProfileNoticeDialogInterface {
    private lateinit var binding: ActivityProfileNoticeBinding
    private var isNoticeOn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileNoticeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 현재 알람 설정 상태 확인
        loadNoticeData()

        val switch = binding.switch1
        // val switchValue = switch.isSelected // 현재 상태
        switch.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                isNoticeOn = if(isChecked){
                    // off -> on
                    Toast.makeText(applicationContext,"off -> on", Toast.LENGTH_SHORT).show()
                    binding.textView4.setTextColor(getColor(R.color.default_textview))
                    binding.textView14.setTextColor(getColor(R.color.default_textview))
                    true
                }else{
                    // on -> off
                    Toast.makeText(applicationContext,"on -> off", Toast.LENGTH_SHORT).show()
                    binding.textView4.setTextColor(getColor(R.color.disableGray))
                    binding.textView14.setTextColor(getColor(R.color.disableGray))
                    false
                }
            }
        })

        // 시간 설정 클릭
        val timePickBtnClicked = binding.bg45
        timePickBtnClicked.setOnClickListener{
            timePick()
        }

        // 뒤로가기 버튼 클릭
        val backBtnClicked = binding.imageView6
        backBtnClicked.setOnClickListener{
            finish()
        }
    }

    // 현재 알림 설정 상태 불러오기
    private fun loadNoticeData() {
        val pref = getSharedPreferences("com.example.DailyScoop.PREFERENCE_FILE_KEY", 0)

        isNoticeOn = pref.getBoolean("noticeStatus", false)
        binding.switch1.isChecked = isNoticeOn
        if(isNoticeOn) {
            binding.textView4.setTextColor(getColor(R.color.default_textview))
            binding.textView14.setTextColor(getColor(R.color.default_textview))
        }
        else {
            binding.textView4.setTextColor(getColor(R.color.disableGray))
            binding.textView14.setTextColor(getColor(R.color.disableGray))
        }

        val hour = pref.getInt("noticeHour", 23)
        val minute = pref.getInt("noticeMinute", 0)

        binding.textView4.text = getString(R.string.month_picker_formatter, hour).plus(":").plus(getString(R.string.month_picker_formatter, minute))

    }

    // 알림 시간 설정
    private fun timePick() {
        if(!isNoticeOn) return

        val noticeDialog = ProfileNoticeDialog(this, this)
        noticeDialog.show()
    }

    // 종료
    override fun onDestroy() {
        super.onDestroy()

        // 알림 상태 저장
        val pref = getSharedPreferences("com.example.DailyScoop.PREFERENCE_FILE_KEY", 0)
        val edit = pref.edit()
        edit.putBoolean("noticeStatus", isNoticeOn)
        edit.apply()
    }

    override fun dialogOkBtnClicked(hour:Int, minute:Int) {
        // 알림 시간 저장
        Log.d("notice", "notice Activity")
        val pref = getSharedPreferences("com.example.DailyScoop.PREFERENCE_FILE_KEY", 0)
        val edit = pref.edit()
        edit.putInt("noticeHour", hour)
        edit.putInt("noticeMinute", minute)
        edit.apply()

        binding.textView4.text = getString(R.string.month_picker_formatter, hour).plus(":").plus(getString(R.string.month_picker_formatter, minute))

    }
}