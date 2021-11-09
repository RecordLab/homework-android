package com.recordlab.dailyscoop.ui.dashboard

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.ui.diary.DiaryDetailActivity

class DashboardGridAdapter(private val items: ArrayList<DashboardItem>) :
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
            fun bind(item: DashboardItem) {
                val img = view.findViewById<ImageView>(R.id.iv_grid_diary)
                Glide.with(itemView).load(item.img).into(img)

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DiaryDetailActivity::class.java)
//                    intent.putExtra("id", 일기 id)
                    itemView.context.startActivity(intent)
                }
            }
        }

    }