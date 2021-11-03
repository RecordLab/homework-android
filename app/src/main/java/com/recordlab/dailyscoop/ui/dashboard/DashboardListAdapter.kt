package com.recordlab.dailyscoop.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.recordlab.dailyscoop.R

class DashboardListAdapter(private val items: ArrayList<DashboardListItem>) :
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
        fun bind(item: DashboardListItem) {
            val date = view.findViewById<TextView>(R.id.tv_list_diary_date)
            val content = view.findViewById<TextView>(R.id.tv_list_diary_content)
            val img = view.findViewById<ImageView>(R.id.iv_list_diary)
            date.text = item.date
            content.text = item.content
            Glide.with(itemView).load(item.img).into(img)
        }
    }
}
