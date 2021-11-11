package com.recordlab.dailyscoop.ui.home.diary

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.data.DiaryData
import com.recordlab.dailyscoop.data.TimeToString
import kotlin.properties.Delegates

class DiaryViewHolder(itemView: View, val itemClick: (DiaryData, View) -> Unit) :
    RecyclerView.ViewHolder(itemView) {
    var diaryText = itemView.findViewById<TextView>(R.id.tv_main_diary_title)
    var diaryDate = itemView.findViewById<TextView>(R.id.tv_main_diary_date)
    var diaryImage = itemView.findViewById<ImageView>(R.id.iv_main_diary)

    /* bind diary image, preview and date*/
    fun bind(data: DiaryData) {
        if (data.image != null) {
            Glide.with(itemView)
                .load(data.image)
                .error(R.drawable.img_error)
                .into(diaryImage)
        } else {
            Glide.with(itemView)
                .load(R.drawable.flower_unsplash)
                .error(R.drawable.img_error)
                .into(diaryImage)
        }
        if( data.content.length > 10) {
            diaryText.text = data.content.substring(0, 10)
        } else {
            diaryText.text = data.content
        }
        diaryDate.text = TimeToString().convert(data.date)
        itemView.setOnClickListener { itemClick(data, itemView) }
    }
}