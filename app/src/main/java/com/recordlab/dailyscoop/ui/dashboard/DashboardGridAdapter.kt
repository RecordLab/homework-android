package com.recordlab.dailyscoop.ui.dashboard

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.ui.diary.DiaryDetailActivity
import com.recordlab.dailyscoop.data.DiaryData

class DashboardGridAdapter(private val items: List<DiaryData>) :
    RecyclerView.Adapter<DashboardGridAdapter.ViewHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder {
            val inflatedView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_grid_diary, parent, false)
            return ViewHolder(inflatedView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemCount(): Int = items.size

        class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            fun bind(item: DiaryData) {
                val img = view.findViewById<ImageView>(R.id.iv_grid_diary)
                Glide.with(itemView).load(item.image).into(img)

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DiaryDetailActivity::class.java)
                    // 임시 하드코딩
                    intent.putExtra("diaryDate", "2021-11-08")
                    itemView.context.startActivity(intent)
                }
            }
        }

    }