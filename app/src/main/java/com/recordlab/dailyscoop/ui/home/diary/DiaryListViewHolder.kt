package com.recordlab.dailyscoop.ui.home.diary

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.data.Diary
import com.recordlab.dailyscoop.data.TimeToString

class DiaryListViewHolder(itemView: View, val onClick: (Diary, View) -> Unit) :
    RecyclerView.ViewHolder(itemView) {
    private val diaryThumbnail: ImageView = itemView.findViewById(R.id.iv_main_diary)
    private val diaryPreview: TextView = itemView.findViewById(R.id.tv_main_diary_title)
    private val diaryDate: TextView = itemView.findViewById(R.id.tv_main_diary_date)

//    init {
//        itemView.setOnClickListener {
//
//        }
//    }

    /* bind diary image, preview and date*/
    fun bind(diary: Diary) {
        if (diary.image != null) {
            Glide.with(itemView)
                .load(diary.image)
                .error(R.drawable.img_error)
                .into(diaryThumbnail)
        } else {
            Glide.with(itemView)
                .load(R.drawable.img_null)
                .error(R.drawable.img_error)
                .into(diaryThumbnail)
        }
        diaryPreview.text = diary.content.substring(0, 10)
        diaryDate.text = TimeToString().convert(diary.writeDay)
    }

}