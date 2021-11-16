package com.recordlab.dailyscoop.ui.home.diary

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.data.DiaryData
import com.recordlab.dailyscoop.data.TimeToString
import com.recordlab.dailyscoop.ui.diary.DiaryDetailActivity

class DiaryViewHolder(val itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    var diaryText = itemView.findViewById<TextView>(R.id.tv_main_diary_title)
    var diaryDate = itemView.findViewById<TextView>(R.id.tv_main_diary_date)
    var diaryImage = itemView.findViewById<ImageView>(R.id.iv_main_diary)

    /* bind diary image, preview and date*/
    fun bind(item: DiaryData) {
        if (item.image == "default") {
            when (item.theme) {
                "paper_white" -> {
                    Glide.with(itemView)
                        .load(R.drawable.theme_paper_white)
                        .error(R.drawable.img_error)
                        .into(diaryImage)
                }
                "paper_ivory" -> {
                    Glide.with(itemView)
                        .load(R.drawable.theme_paper_ivory)
                        .error(R.drawable.img_error)
                        .into(diaryImage)
                }
                "paper_dark" -> {
                    Glide.with(itemView)
                        .load(R.drawable.theme_paper_dark)
                        .error(R.drawable.img_error)
                        .into(diaryImage)
                }
                "window" -> {
                    Glide.with(itemView)
                        .load(R.drawable.theme_window)
                        .error(R.drawable.img_error)
                        .into(diaryImage)
                }
                "sky_day" -> {
                    Glide.with(itemView)
                        .load(R.drawable.theme_sky_day_bright)
                        .error(R.drawable.img_error)
                        .into(diaryImage)
                }
                "sky_night" -> {
                    Glide.with(itemView)
                        .load(R.drawable.theme_sky_night)
                        .error(R.drawable.img_error)
                        .into(diaryImage)
                }
            }
        } else {
            Glide.with(itemView)
                .load(item.image)
                .error(R.drawable.img_error)
                .into(diaryImage)
        }
        if( item.content.length > 10) {
            diaryText.text = item.content.substring(0, 10)
        } else {
            diaryText.text = item.content
        }
        diaryDate.text = TimeToString().convert(item.date)
        itemView.setOnClickListener {
            val intent = Intent(itemView.context, DiaryDetailActivity::class.java)
            intent.putExtra("diaryDate", item.date.toString().substring(0, 10))
            (itemView.context as Activity).overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            itemView.context.startActivity(intent)
        }
    }
}