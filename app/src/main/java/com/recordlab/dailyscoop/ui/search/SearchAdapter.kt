package com.recordlab.dailyscoop.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.data.DiaryData

class SearchAdapter(private val itemClick: (DiaryData, View) -> Unit): RecyclerView.Adapter<SearchViewHolder>() {
    var data = mutableListOf<DiaryData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_diary, parent, false)
        return SearchViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun addDiary(item: DiaryData) {
        data.add(item)
    }
}