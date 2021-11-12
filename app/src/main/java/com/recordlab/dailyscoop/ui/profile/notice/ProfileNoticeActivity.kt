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

        // 스위치 변경 리스너
        val switch = binding.switch1
        switch.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                isNoticeOn = if(isChecked){
                    // off -> on
                    binding.textView4.setTextColor(getColor(R.color.default_textview))
                    binding.textView14.setTextColor(getColor(R.color.default_textview))
                    true
                }else{
                    // on -> off
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
        // 스위치 on/off
        isNoticeOn = pref.getBoolean("noticeStatus", false)
        binding.switch1.isChecked = isNoticeOn
        // on/off 따른 텍스트 색상 변경
        if(isNoticeOn) {
            binding.textView4.setTextColor(getColor(R.color.default_textview))
            binding.textView14.setTextColor(getColor(R.color.default_textview))
        }
        else {
            binding.textView4.setTextColor(getColor(R.color.disableGray))
            binding.textView14.setTextColor(getColor(R.color.disableGray))
        }
        // 저장된 알림 시간으로 변경
        val hour = pref.getInt("noticeHour", 23)
        val minute = pref.getInt("noticeMinute", 0)
        binding.textView4.text = getString(R.string.month_picker_formatter, hour).plus(":").plus(getString(R.string.month_picker_formatter, minute))
    }

    // 알림 시간 설정
    private fun timePick() {
        if(!isNoticeOn) return

        // 알림 시간 고르는 다이얼로그
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
        // 알림 시간
        val pref = getSharedPreferences("com.example.DailyScoop.PREFERENCE_FILE_KEY", 0)
        val edit = pref.edit()
        edit.putInt("noticeHour", hour)
        edit.putInt("noticeMinute", minute)
        edit.apply()

        // 설정한 시간으로 변경
        binding.textView4.text = getString(R.string.month_picker_formatter, hour).plus(":").plus(getString(R.string.month_picker_formatter, minute))
    }
}