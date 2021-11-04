package com.recordlab.dailyscoop.ui.diary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.recordlab.dailyscoop.R

class DiaryDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_detail)

        val rv : RecyclerView = findViewById(R.id.rv)

        val emotions = ArrayList<Int>()

        val rvAdapter = DiaryDetailEmotionRVAdapter(emotions)
        rv.adapter = rvAdapter
    }
}