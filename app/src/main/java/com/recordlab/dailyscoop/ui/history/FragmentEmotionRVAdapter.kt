package com.recordlab.dailyscoop.ui.history

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class FragmentEmotionRVAdapter(val emotions : List<String>) : RecyclerView.Adapter<FragmentEmotionRVAdapter.Viewholder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FragmentEmotionRVAdapter.Viewholder {
        return View
    }

    override fun onBindViewHolder(holder: FragmentEmotionRVAdapter.Viewholder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindEmotion(emotion : String) {
            val imageView = itemView.findViewById<ImageView>()
        }
    }
}