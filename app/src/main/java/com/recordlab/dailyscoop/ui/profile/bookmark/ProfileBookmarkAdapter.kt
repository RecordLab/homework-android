package com.recordlab.dailyscoop.ui.profile.bookmark

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.network.response.Favorite

class ProfileBookmarkAdapter(private val items: List<Favorite>) :
    RecyclerView.Adapter<ProfileBookmarkAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bookmark_list, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Favorite) {
            val quote = view.findViewById<TextView>(R.id.tv_bookmark_item)
            val writer = view.findViewById<TextView>(R.id.tv_bookmark_writer)
            var index = item.quote!!.indexOf("-")
            if (index == -1) {
               index = item.quote!!.indexOf("–")
            }

            quote.text = item.quote!!.substring(0, index)
            writer.text = "–" + item.quote!!.substring(index+1) + " "
            quote.setTypeface(null, android.graphics.Typeface.BOLD)
            writer.setTypeface(null, android.graphics.Typeface.ITALIC)

            itemView.setOnClickListener {
//                Log.d("click", item.quote.toString())
            }
        }
    }
}