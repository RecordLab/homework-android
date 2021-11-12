package com.recordlab.dailyscoop.ui.dashboard

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.data.DiaryData
import com.recordlab.dailyscoop.data.TimeToString
import com.recordlab.dailyscoop.ui.diary.DiaryDetailActivity

class DashboardListAdapter(private val items: List<DiaryData>) :
    RecyclerView.Adapter<DashboardListAdapter.ViewHolder>() {

        // RecyclerView 초기화 때 호출
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder {
            val inflatedView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_diary, parent, false)
            return ViewHolder(inflatedView)
        }

        // 생성된 View 에 보여줄 데이터 설정
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(items[position])
        }

        // 보여줄 아이템 개수
        override fun getItemCount(): Int = items.size

        // ViewHolder 단위 객체로 View 의 데이터를 설정
        class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            fun bind(item: DiaryData) {
                val date = view.findViewById<TextView>(R.id.tv_list_diary_date)
                val content = view.findViewById<TextView>(R.id.tv_list_diary_content)
                val img = view.findViewById<ImageView>(R.id.iv_list_diary)
                date.text = TimeToString().convert(item.date)
                if (item.content.length < 30) {
                    content.text = item.content
                } else {
                    content.text = "${item.content.substring(0, 30)}..."
                }

                Glide.with(itemView).load(item.image).transform(CenterCrop(), RoundedCorners(16)).into(img)

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DiaryDetailActivity::class.java)
                    intent.putExtra("diaryDate", item.date.toString().substring(0, 10))
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
