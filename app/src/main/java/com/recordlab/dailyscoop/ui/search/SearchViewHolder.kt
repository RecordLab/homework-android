package com.recordlab.dailyscoop.ui.search

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.data.DiaryData
import com.recordlab.dailyscoop.data.TimeToString
import com.recordlab.dailyscoop.ui.diary.DiaryDetailActivity
import kotlin.properties.Delegates

class SearchViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView){
    private var diaryDate = itemView.findViewById<TextView>(R.id.tv_item_search_diary_date)
    private var diaryContent = itemView.findViewById<TextView>(R.id.tv_item_search_diary_content)
    private var diaryImage = itemView.findViewById<ImageView>(R.id.iv_item_search_diary)

    fun bind(item: DiaryData){
        diaryDate.text = TimeToString().convert(item.date)
        if (item.content.length > 40){
            diaryContent.text = item.content.substring(0, 40)

        }else {
            diaryContent.text = item.content
        }
        if(item.image == "default" || ("").equals(item.image)){
            when (item.theme) {
                "paper_dark" -> Glide.with(itemView).load(R.drawable.theme_paper_dark).into(diaryImage)
                "paper_ivory" -> Glide.with(itemView).load(R.drawable.theme_paper_ivory).into(diaryImage)
                "paper_white" -> Glide.with(itemView).load(R.drawable.theme_paper_white).into(diaryImage)
                "sky_day" -> Glide.with(itemView).load(R.drawable.theme_sky_day).into(diaryImage)
                "sky_day_bright" -> Glide.with(itemView).load(R.drawable.theme_sky_day_bright).into(diaryImage)
                "sky_night" -> Glide.with(itemView).load(R.drawable.theme_sky_night).into(diaryImage)
                "window" -> Glide.with(itemView).load(R.drawable.theme_window).into(diaryImage)
            }
        }else {
            Glide.with(itemView).load(item.image).into(diaryImage)
        }

        itemView.setOnClickListener {
            val intent = Intent(itemView.context, DiaryDetailActivity::class.java)
            intent.putExtra("diaryDate", item.date.toString().substring(0, 10))
            itemView.context.startActivity(intent)
        }
    }
}