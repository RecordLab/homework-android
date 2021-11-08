package com.recordlab.dailyscoop.ui.diary

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.recordlab.dailyscoop.R

class DiaryDetailEmotionRVAdapter(val emotions : ArrayList<String>) : RecyclerView.Adapter<DiaryDetailEmotionRVAdapter.Viewholder>() {
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

        fun bindEmotions(emotion : String) {
            val imageView = itemView.findViewById<ImageView>(R.id.emotionImage)
            when (emotion) {
                "angry" -> {
                    imageView.setImageResource(R.drawable.angry)
                }
                "anxious" -> {
                    imageView.setImageResource(R.drawable.anxious)
                }
                "bored" -> {
                    imageView.setImageResource(R.drawable.bored)
                }
                "excitement" -> {
                    imageView.setImageResource(R.drawable.excitement)
                }
                "fun" -> {
//                    imageView.setImageResource(R.drawable.fun)
                }
                "happy" -> {
                    imageView.setImageResource(R.drawable.happy)
                }
                "joy" -> {
                    imageView.setImageResource(R.drawable.joy)
                }
                "nervous" -> {
                    imageView.setImageResource(R.drawable.nervous)
                }
                "peaceful" -> {
                    imageView.setImageResource(R.drawable.peaceful)
                }
                "relax" -> {
                    imageView.setImageResource(R.drawable.relax)
                }
                "sad" -> {
                    imageView.setImageResource(R.drawable.sad)
                }
                "tired" -> {
                    imageView.setImageResource(R.drawable.tired)
                }
            }
        }
    }
}