package com.recordlab.dailyscoop.ui.profile.day

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.ImageView
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.databinding.ActivityProfileDdayBinding

class ProfileDdayActivity : AppCompatActivity(), ProfileDdayDialogInterface {
    private lateinit var binding: ActivityProfileDdayBinding
    private var isDayOn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileDdayBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)

        // 현재 디데이 설정 상태 확인
        loadDayData()

        // 스위치 변경 리스터
        val switch = binding.switch1
        switch.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                isDayOn = if(isChecked){
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

        // 디데이 설정 클릭
        val dayPickBtnClicked = binding.bg45
        dayPickBtnClicked.setOnClickListener{
            dayPick()
        }

        // 뒤로가기 버튼 클릭
        val backBtnClicked = findViewById<ImageView>(R.id.imageView6)
        backBtnClicked.setOnClickListener{
            finish()
        }
    }

    // 현재 디데이 설정 상태 불러오기
    private fun loadDayData() {
        val pref = getSharedPreferences("com.example.DailyScoop.PREFERENCE_FILE_KEY", 0)
        // 스위치 on/off
        isDayOn = pref.getBoolean("dayStatus", false)
        binding.switch1.isChecked = isDayOn
        // on/off 따른 텍스트 색상 변경
        if(isDayOn) {
            binding.textView4.setTextColor(getColor(R.color.default_textview))
            binding.textView14.setTextColor(getColor(R.color.default_textview))
        }
        else {
            binding.textView4.setTextColor(getColor(R.color.disableGray))
            binding.textView14.setTextColor(getColor(R.color.disableGray))
        }
        // 저장된 알림 시간으로 변경
        val year = pref.getInt("dayYear", 2021)
        val month = pref.getInt("dayMonth", 11)
        val day = pref.getInt("dayDay", 11)
        settingDay(year, month, day)
    }

    // 디데이 설정
    private fun dayPick() {
        if(!isDayOn) return

        // 알림 시간 고르는 다이얼로그
        val dayDialog = ProfileDdayDialog(this, this)
        dayDialog.show()
    }

    override fun dialogDdayOkBtnClicked(year: Int, month: Int, day: Int) {
        // 디데이 시간 저장
        val pref = getSharedPreferences("com.example.DailyScoop.PREFERENCE_FILE_KEY", 0)
        val edit = pref.edit()
        edit.putInt("dayYear", year)
        edit.putInt("dayMonth", month)
        edit.putInt("dayDay", day)
        edit.apply()

        // 설정한 시간으로 변경
        settingDay(year, month, day)
    }

    private fun settingDay(year: Int, month: Int, day: Int) {
        var day2 = day
        when (month){
            2 -> {
                if(day > 28) day2 = 28
            }
            4, 6, 9, 11 -> {
                if(day == 31) day2 = 30
            }
        }
        binding.textView4.text = getString(R.string.year_picker_formatter, year).plus(".")
            .plus(getString(R.string.month_picker_formatter, month)).plus(".")
            .plus(getString(R.string.month_picker_formatter, day2))
    }

    // 종료
    override fun onDestroy() {
        super.onDestroy()

        // 알림 상태 저장
        val pref = getSharedPreferences("com.example.DailyScoop.PREFERENCE_FILE_KEY", 0)
        val edit = pref.edit()
        edit.putBoolean("dayStatus", isDayOn)
        edit.apply()
    }
}