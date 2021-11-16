package com.recordlab.dailyscoop.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.recordlab.dailyscoop.R

class HistoryEmotionBigRVAdapter(val data: List<MutableList<String>>) : RecyclerView.Adapter<HistoryEmotionBigRVAdapter.Viewholder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryEmotionBigRVAdapter.Viewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_history_emotion_big, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: HistoryEmotionBigRVAdapter.Viewholder, position: Int) {
        holder.bindData(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindData(data : List<String>) {
            bindImg(data[0])
            val textViewRank = itemView.findViewById<TextView>(R.id.textRank)
            textViewRank.text = "${data[1]}위"
            val textViewProportion = itemView.findViewById<TextView>(R.id.textProportion)
            textViewProportion.text = "${data[2]}%"
        }

        fun bindImg(emotion : String) {
            val imageView = itemView.findViewById<ImageView>(R.id.emotionImg)
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
                "sound" -> {
                    imageView.setImageResource(R.drawable.sound_small)
                }
            }
        }
    }
}