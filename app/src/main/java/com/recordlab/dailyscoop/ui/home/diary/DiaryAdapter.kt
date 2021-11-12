package com.recordlab.dailyscoop.ui.home.diary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.data.DiaryData

class DiaryAdapter(private val data: List<DiaryData>) :
    RecyclerView.Adapter<DiaryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_main_diary, parent, false)
        return DiaryViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiaryViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    /*fun addDiary(item: DiaryData) {
        data.add(item)
    }*/
}

/*
object DiaryDiffCallback : DiffUtil.ItemCallback<DiaryData>() {
    override fun areItemsTheSame(oldItem: DiaryData, newItem: DiaryData): Boolean =
        (oldItem == newItem)

    override fun areContentsTheSame(oldItem: DiaryData, newItem: DiaryData): Boolean =
        (oldItem.id == newItem.id)

}*/
