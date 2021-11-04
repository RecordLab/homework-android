package com.recordlab.dailyscoop.ui.diary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.recordlab.dailyscoop.R

class DiaryDetailEmotionRVAdapter(val emotions : ArrayList<Int>) : RecyclerView.Adapter<DiaryDetailEmotionRVAdapter.Viewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryDetailEmotionRVAdapter.Viewholder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.rv_diary_emotion, parent, false)
        return Viewholder(v)
    }

    override fun onBindViewHolder(holder: DiaryDetailEmotionRVAdapter.Viewholder, position: Int) {
        holder.bindEmotions(emotions[position])
    }

    override fun getItemCount(): Int {
        return emotions.size
    }

    inner class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindEmotions(emotion : Int) {
            val imageView = itemView.findViewById<ImageView>(R.id.emotionImage)

            imageView.setImageResource(emotion)
        }
    }
}