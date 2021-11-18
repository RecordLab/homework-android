package com.recordlab.dailyscoop.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.databinding.ActivityProfileFontBinding

class ProfileFontActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileFontBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileFontBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)

        // 설정된 폰트 값 가져오기
        loadFontData()

        // 뒤로가기 버튼 클릭
        val backBtnClicked = binding.imageView68
        backBtnClicked.setOnClickListener{
            finish()
        }
    }

    // 라디오 버튼 클릭
    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked
            val setFont = binding.testTxt

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_1 ->
                    if (checked) {
                        setFont.typeface = resources.getFont(R.font.binggraesamanco)
                        saveFontData(1)
                    }
                R.id.radio_2 ->
                    if (checked) {
                        // Ninjas rule
                        setFont.typeface = resources.getFont(R.font.spoqa_han_sans_neo_medium)
                        saveFontData(2)
                    }
                R.id.radio_3 ->
                    if (checked) {
                        // Ninjas rule
                        setFont.typeface = resources.getFont(R.font.hsyuji)
                        saveFontData(3)
                    }
                R.id.radio_4 ->
                    if (checked) {
                        // Ninjas rule
                        setFont.typeface = resources.getFont(R.font.nanum_hand_goding)
                        saveFontData(4)
                    }
                R.id.radio_5 ->
                    if (checked) {
                        // Ninjas rule
                        setFont.typeface = resources.getFont(R.font.nanum_hand_mago)
                        saveFontData(5)
                    }
                R.id.radio_6 ->
                    if (checked) {
                        // Ninjas rule
                        setFont.typeface = resources.getFont(R.font.cafe24dongdong)
                        saveFontData(6)
                    }
                R.id.radio_7 ->
                    if (checked) {
                        // Ninjas rule
                        setFont.typeface = resources.getFont(R.font.cafe24ohsquareair)
                        saveFontData(7)
                    }
                R.id.radio_8 ->
                    if (checked) {
                        // Ninjas rule
                        setFont.typeface = resources.getFont(R.font.cafe24shiningstar)
                        saveFontData(8)
                    }
                R.id.radio_9 ->
                    if (checked) {
                        // Ninjas rule
                        setFont.typeface = resources.getFont(R.font.cafe24ssukssuk)
                        saveFontData(9)
                    }
                R.id.radio_10 ->
                    if (checked) {
                        // Ninjas rule
                        setFont.typeface = resources.getFont(R.font.cafe24syongsyong)
                        saveFontData(10)
                    }
                R.id.radio_11 ->
                    if (checked) {
                        // Ninjas rule
                        setFont.typeface = resources.getFont(R.font.cookierun_regular)
                        saveFontData(11)
                    }
            }
        }
    }

    private fun loadFontData() {
        // 저장된 폰트값 가져오기
        val pref = getSharedPreferences("com.example.DailyScoop.PREFERENCE_FILE_KEY", 0)
        val savedFont = pref.getInt("diaryFont", 2)

        // 디폴트 설정
        val radioGroup = binding.radioGroup
        val setFont = binding.testTxt
        when (savedFont){
            1 -> {
                radioGroup.check(binding.radio1.id)
                setFont.typeface = resources.getFont(R.font.spoqa_han_sans_neo_bold)
            }
            2 -> {
                radioGroup.check(binding.radio2.id)
                setFont.typeface = resources.getFont(R.font.spoqa_han_sans_neo_medium)
            }
            3 -> {
                radioGroup.check(binding.radio3.id)
                setFont.typeface = resources.getFont(R.font.spoqa_han_sans_neo_thin)
            }
            4 -> {
                radioGroup.check(binding.radio4.id)
                setFont.typeface = resources.getFont(R.font.nanum_hand_goding)
            }
            5 -> {
                radioGroup.check(binding.radio5.id)
                setFont.typeface = resources.getFont(R.font.nanum_hand_mago)
            }
        }

    }

    private fun saveFontData(i: Int) {
        // 폰트 저장
        val pref = getSharedPreferences("com.example.DailyScoop.PREFERENCE_FILE_KEY", 0)
        val edit = pref.edit() // 수정모드(추가, 수정)
        edit.putInt("diaryFont", i) // key, value
        edit.apply() // 저장 완료
    }
}