package com.recordlab.dailyscoop.ui.profile.notice

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import com.recordlab.dailyscoop.AlarmReceiver
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.databinding.ActivityProfileNoticeBinding

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

                    cancelAlarm()

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

        setAlarm(hour, minute)



        // 설정한 시간으로 변경
        binding.textView4.text = getString(R.string.month_picker_formatter, hour).plus(":").plus(getString(R.string.month_picker_formatter, minute))
    }

    private fun setAlarm(hour: Int, minute: Int) {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, AlarmReceiver::class.java)  // 알람 조건이 충족되었을 때, 리시버로 전달될 인텐트를 설정합니다.
        val pendingIntent = PendingIntent.getBroadcast(     // AlarmManager가 인텐트를 갖고 있다가 일정 시간이 흐른 뒤에 전달하기 때문에 PendingIntent로 만들어야 합니다. PendingIntent의 requestCode 인자로 NOTIFICATION_ID를 전달하였습니다.
            this, AlarmReceiver.NOTIFICATION_ID, intent,
            PendingIntent.FLAG_UPDATE_CURRENT)

        // 알람 주기(1일)
        val repeatInterval: Long = AlarmManager.INTERVAL_DAY

        // 시간에 맞게 알람 설정
        val calendar: Calendar = Calendar.getInstance().apply { // 1
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }

        // 알람 시작
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, // 2
            calendar.timeInMillis,
            repeatInterval,
            pendingIntent)
    }

    private fun cancelAlarm() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)  // 알람 조건이 충족되었을 때, 리시버로 전달될 인텐트를 설정합니다.
        val pendingIntent = PendingIntent.getBroadcast(     // AlarmManager가 인텐트를 갖고 있다가 일정 시간이 흐른 뒤에 전달하기 때문에 PendingIntent로 만들어야 합니다. PendingIntent의 requestCode 인자로 NOTIFICATION_ID를 전달하였습니다.
            this, AlarmReceiver.NOTIFICATION_ID, intent,
            PendingIntent.FLAG_UPDATE_CURRENT)


        alarmManager.cancel(pendingIntent)    // 알람을 취소할 때는 등록한 PendingIntent를 인자로 전달합니다.

    }


}