package com.recordlab.dailyscoop.ui.diary

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.databinding.ActivityDiaryDetailBinding
import com.recordlab.dailyscoop.network.RetrofitClient.service
import com.recordlab.dailyscoop.network.enqueue
import java.util.*

class DiaryDetailActivity : AppCompatActivity() {

    lateinit var diaryDate : String
    val emotions = mutableListOf<String>()

    lateinit var rvAdapter: DiaryDetailEmotionRVAdapter

    private lateinit var binding : ActivityDiaryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        diaryDate = intent.getStringExtra("diaryDate").toString()
        getDiary(binding)
        rvAdapter = DiaryDetailEmotionRVAdapter(emotions)

        val rv : RecyclerView = findViewById(R.id.rv)
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv.adapter = rvAdapter

        /*binding.backBtn.setOnClickListener {
            finish()
        }*/
        val tbDiaryDetail : Toolbar = findViewById(R.id.tb_diary_detail)
        setSupportActionBar(tbDiaryDetail)
        supportActionBar!!.setDisplayShowCustomEnabled(true)  // custom하기 위해
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)


        // 사진 클릭 시 전체화면 기능
       var imgFull = false
        binding.diaryImg.setOnClickListener {
            binding.diaryImgFullLayout.visibility = View.VISIBLE
            imgFull = true
        }
        binding.diaryImgFullBackBtn.setOnClickListener {
            binding.diaryImgFullLayout.visibility = View.INVISIBLE
            imgFull = false
        }
        // 뒤로가기 버튼 누를 시 finish()가 아닌 사진 전체화면 닫기
        onBackPressedDispatcher.addCallback(this) {
            if (imgFull) {
                binding.diaryImgFullLayout.visibility = View.INVISIBLE
                imgFull = false
            } else {
                finish()
            }
        }
    }

    private fun getDiary(binding: ActivityDiaryDetailBinding) {
        val pref = getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val token =  pref?.getString("token", null)
        val header = mutableMapOf<String, String?>()
        header["Authorization"] = token

        service.requestGetDiaryDetail(header = header, date = diaryDate).enqueue(
            onSuccess = {
                when (it.code()) {
                    200 -> {
                        val res = it.body()

                        binding.diaryContent.text = "\n${res?.content.toString()}\n"
                        if(it.body()?.image == "default"){
                            binding.diaryImg.visibility = View.GONE

                        } else {
                            Glide.with(this).load(res?.image).into(binding.diaryImg)
                            Glide.with(this).load(res?.image).into(binding.diaryImgFull)
                        }
                        val datetime = res?.date.toString()
                        val year = datetime.substring(0, 4).toInt()
                        val month = datetime.substring(5, 7)
                        val day = datetime.substring(8, 10)
                        val date = Calendar.getInstance()
                        date.set(year, month.toInt()-1, day.toInt())
                        var dayOfWeek = ""
                        when (date.get(Calendar.DAY_OF_WEEK)) {
                            1 -> {
                                dayOfWeek = "일"
                            }
                            2 -> {
                                dayOfWeek = "월"
                            }
                            3 -> {
                                dayOfWeek = "화"
                            }
                            4 -> {
                                dayOfWeek = "수"
                            }
                            5 -> {
                                dayOfWeek = "목"
                            }
                            6 -> {
                                dayOfWeek = "금"
                            }
                            7 -> {
                                dayOfWeek = "토"
                            }
                        }
                        binding.dateHeader.text = "${month.toString()}월 ${day.toString()}일 ${dayOfWeek}요일"

                        emotions.addAll(res?.emotions!!)
                        setTheme(binding, res?.theme)

                        rvAdapter.notifyDataSetChanged()
                    }
                }
            }
        )
    }

    private fun setTheme(binding: ActivityDiaryDetailBinding, theme: String) {
        when (theme) {
            "paper_white" -> {
                binding.diaryBg.setBackgroundResource(R.drawable.theme_paper_white)
                supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_black_24)
                setTextColor(0)
            }
            "paper_ivory" -> {
                binding.diaryBg.setBackgroundResource(R.drawable.theme_paper_ivory)
                supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_black_24)
                setTextColor(0)
            }
            "paper_dark" -> {
                binding.diaryBg.setBackgroundResource(R.drawable.theme_paper_dark)
                supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
                setTextColor(1)
            }
            "sky_day" -> {
                binding.diaryBg.setBackgroundResource(R.drawable.theme_sky_day_bright)
                supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
                setTextColor(0)
            }
            "sky_night" -> {
                binding.diaryBg.setBackgroundResource(R.drawable.theme_sky_night)
                supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
                setTextColor(1)
            }
            "window" -> {
                binding.diaryBg.setBackgroundResource(R.drawable.theme_window)
                supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_black_24)
                setTextColor(1)
            }
        }
    }

    fun setTextColor(mode: Int) {
        when (mode) {
            0 -> {
                val darkColor: Int = Color.argb(0xCC, 0x30, 0x30, 0x30)
                colorMode(darkColor)
            }
            1 -> { // 밝은 텍스트
                val brightColor = Color.argb(0xCC, 0xDB, 0xDB, 0xDB)
                colorMode(brightColor)
            }
        }
    }


    fun colorMode(color: Int) {
        binding.diaryContent.setTextColor(color)
        binding.dateHeader.setTextColor(color)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}