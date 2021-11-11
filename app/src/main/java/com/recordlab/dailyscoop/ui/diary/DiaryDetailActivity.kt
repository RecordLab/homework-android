package com.recordlab.dailyscoop.ui.diary

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.databinding.ActivityDiaryDetailBinding
import com.recordlab.dailyscoop.network.RetrofitClient.service
import com.recordlab.dailyscoop.network.enqueue

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
                        Glide.with(this).load(res?.image).into(binding.diaryImg)
                        val datetime = res?.date.toString()
                        binding.dateHeader.text = "${datetime.substring(5, 7)}월 ${datetime.substring(8, 10)}일 요일"
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
            }
            "paper_ivory" -> {
                binding.diaryBg.setBackgroundResource(R.drawable.theme_paper_ivory)
            }
            "paper_black" -> {
                binding.diaryBg.setBackgroundResource(R.drawable.theme_paper_black)
            }
            "sky_day" -> {
                binding.diaryBg.setBackgroundResource(R.drawable.theme_sky_day)
            }
            "sky_night" -> {
                binding.diaryBg.setBackgroundResource(R.drawable.theme_sky_night)
            }
            "window" -> {
                binding.diaryBg.setBackgroundResource(R.drawable.theme_window)
            }
        }
    }
}