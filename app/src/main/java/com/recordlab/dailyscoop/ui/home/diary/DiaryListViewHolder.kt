package com.recordlab.dailyscoop.ui.home.diary

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.data.Diary
import com.recordlab.dailyscoop.databinding.ItemMainDiaryBinding

class DiaryListViewHolder(itemView : View, val onClick : (Diary) -> Unit) : RecyclerView.ViewHolder(itemView) {
    private val diaryThumbnail: ImageView = itemView.findViewById(R.id.iv_main_diary)
    private val diaryPreview: TextView = itemView.findViewById(R.id.tv_main_diary_title)
    private val diaryDate: TextView = itemView.findViewById(R.id.tv_main_diary_date)

    init {
        itemView.setOnClickListener {

        }
    }

    /* bind diary image, preview and date*/
    fun bind(diary: Diary) {

    }

}