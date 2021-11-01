package com.recordlab.dailyscoop.ui.home.diary

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.data.Diary

class DiaryAdapter(private val context: Context, val itemClick: (Diary, View) -> Unit) :
    RecyclerView.Adapter<DiaryListViewHolder>() {
    val data = mutableListOf<Diary>()

    /*class DiaryListViewHolder(itemView : View, val onClick : (Diary) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val diaryThumbnail: ImageView = itemView.findViewById(R.id.iv_main_diary)
        private val diaryPreview: TextView = itemView.findViewById(R.id.tv_main_diary_title)
        private val diaryDate: TextView = itemView.findViewById(R.id.tv_main_diary_date)

        init {
            itemView.setOnClickListener {

            }
        }

        *//* bind diary image, preview and date*//*
        fun bind(diary: Diary) {

        }

    }*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_main_diary, parent, false)
        return DiaryListViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: DiaryListViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}

object DiaryDiffCallback : DiffUtil.ItemCallback<Diary>() {
    override fun areItemsTheSame(oldItem: Diary, newItem: Diary): Boolean = (oldItem == newItem)

    override fun areContentsTheSame(oldItem: Diary, newItem: Diary): Boolean =
        (oldItem.id == newItem.id)

}