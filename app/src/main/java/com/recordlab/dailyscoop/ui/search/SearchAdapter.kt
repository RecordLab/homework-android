package com.recordlab.dailyscoop.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.data.DiaryData

class SearchAdapter(private val items: List<DiaryData>): RecyclerView.Adapter<SearchViewHolder>() {
//    var data = mutableListOf<DiaryData>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_diary, parent, false)
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_diary, parent, false)
        return SearchViewHolder(inflatedView)
    }


    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

//    fun addDiary(item: DiaryData) {
//        items.add(item)
//    }
}