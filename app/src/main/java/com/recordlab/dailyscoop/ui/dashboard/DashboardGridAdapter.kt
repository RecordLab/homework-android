package com.recordlab.dailyscoop.ui.dashboard

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.ui.diary.DiaryDetailActivity
import com.recordlab.dailyscoop.data.DiaryData
import retrofit2.http.Url
import java.io.File
import java.net.URI
import java.net.URL

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

                if (item.image == "default") {
                    when (item.theme) {
                        "paper_dark" -> Glide.with(itemView).load(R.drawable.theme_paper_dark).into(img)
                        "paper_ivory" -> Glide.with(itemView).load(R.drawable.theme_paper_ivory).into(img)
                        "paper_white" -> Glide.with(itemView).load(R.drawable.theme_paper_white).into(img)
                        "sky_day" -> Glide.with(itemView).load(R.drawable.theme_sky_day).into(img)
                        "sky_day_bright" -> Glide.with(itemView).load(R.drawable.theme_sky_day_bright).into(img)
                        "sky_night" -> Glide.with(itemView).load(R.drawable.theme_sky_night).into(img)
                        "window" -> Glide.with(itemView).load(R.drawable.theme_window).into(img)
                    }
                } else {
                    Glide.with(itemView).load(item.image).into(img)
                }

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DiaryDetailActivity::class.java)
                    intent.putExtra("diaryDate", item.date.toString().substring(0, 10))
                    itemView.context.startActivity(intent)
                }
            }
        }

    }