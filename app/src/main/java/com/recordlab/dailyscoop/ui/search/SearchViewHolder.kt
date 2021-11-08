package com.recordlab.dailyscoop.ui.search

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.data.DiaryData
import com.recordlab.dailyscoop.data.TimeToString
import kotlin.properties.Delegates

class SearchViewHolder(itemView: View, val itemClick: (DiaryData, View) -> Unit) : RecyclerView.ViewHolder(itemView){
    var diaryIdx by Delegates.notNull<Int>()
    var diaryDate = itemView.findViewById<TextView>(R.id.tv_item_search_diary_date)
    var diaryContent = itemView.findViewById<TextView>(R.id.tv_item_search_diary_content)
    var diaryImage = itemView.findViewById<ImageView>(R.id.iv_item_search_diary)

    fun bind(item: DiaryData){
        diaryIdx = item.id
        diaryDate.text = TimeToString().convert(item.writeDay)
        if (item.content.length > 40){
            diaryContent.text = item.content.substring(0, 40)

        }else {
            diaryContent.text = item.content
        }
        if(item.image == null || ("").equals(item.image)){
            Glide.with(itemView).load("@drawable/white_rectangle.xml").into(diaryImage)
        }else {
            Glide.with(itemView).load(item.image).into(diaryImage)
        }
    }
}