package com.recordlab.dailyscoop.ui.home.diary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.data.Diary

class DiaryAdapter(private val onClick: (Diary) -> Unit) : RecyclerView.Adapter<DiaryListViewHolder>() {
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_main_diary, parent, false)
        return DiaryListViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: DiaryListViewHolder, position: Int) {
        val diary = getItemId(position)
        holder.bind(diary)
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}

object DiaryDiffCallback : DiffUtil.ItemCallback<Diary>() {
    override fun areItemsTheSame(oldItem: Diary, newItem: Diary): Boolean = (oldItem == newItem)

    override fun areContentsTheSame(oldItem: Diary, newItem: Diary): Boolean =
        (oldItem.id == newItem.id)

}