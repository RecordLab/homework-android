package com.recordlab.dailyscoop.ui.diary

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.recordlab.dailyscoop.R

class DiaryDetailEmotionRVAdapter(val emotions : List<String>) : RecyclerView.Adapter<DiaryDetailEmotionRVAdapter.Viewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryDetailEmotionRVAdapter.Viewholder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.rv_diary_emotion, parent, false)
        return Viewholder(v)
    }

    override fun onBindViewHolder(holder: DiaryDetailEmotionRVAdapter.Viewholder, position: Int) {
        Log.d("DiaryDetailEmotionRVAdapter", emotions.toString())
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
                    imageView.setImageResource(R.drawable.angry_small)
                }
                "anxious" -> {
                    imageView.setImageResource(R.drawable.anxious_small)
                }
                "bored" -> {
                    imageView.setImageResource(R.drawable.bored_small)
                }
                "excitement" -> {
                    imageView.setImageResource(R.drawable.excitement_small)
                }
                "fun" -> {
                    imageView.setImageResource(R.drawable.fun_small)
                }
                "happy" -> {
                    imageView.setImageResource(R.drawable.happy_small)
                }
                "joy" -> {
                    imageView.setImageResource(R.drawable.joy_small)
                }
                "nervous" -> {
                    imageView.setImageResource(R.drawable.nervous_small)
                }
                "peaceful" -> {
                    imageView.setImageResource(R.drawable.peaceful)
                }
                "relax" -> {
                    imageView.setImageResource(R.drawable.relax_small)
                }
                "sad" -> {
                    imageView.setImageResource(R.drawable.sad_small)
                }
                "tired" -> {
                    imageView.setImageResource(R.drawable.tired_small)
                }
            }
        }
    }
}