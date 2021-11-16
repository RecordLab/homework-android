package com.recordlab.dailyscoop.ui.history

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.databinding.FragmentHistoryBinding
import com.recordlab.dailyscoop.network.RetrofitClient.service
import com.recordlab.dailyscoop.network.enqueue
import com.recordlab.dailyscoop.ui.search.SearchResultActivity
import net.alhazmy13.wordcloud.WordCloud
import net.alhazmy13.wordcloud.WordCloudView

class HistoryFragment : Fragment() {

    private lateinit var historyViewModel: HistoryViewModel

    val emotions = mutableListOf<MutableList<String>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var binding = FragmentHistoryBinding.inflate(inflater, container, false)

//        historyViewModel =
//            ViewModelProviders.of(this).get(HistoryViewModel::class.java)
//        val root = inflater.inflate(R.layout.fragment_history, container, false)
//        val textView: TextView = root.findViewById(R.id.text_history)
//        historyViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        setHasOptionsMenu(true) // 앱 바 작업 버튼 추가하기

        getEmotionsCount(binding)

        // temporary hard coding
        emotions.add(mutableListOf("angry", "1", "53"))
        emotions.add(mutableListOf("fun", "2", "13"))
        emotions.add(mutableListOf("relax", "3", "12"))
        emotions.add(mutableListOf("angry", "4"))
        emotions.add(mutableListOf("fun", "5"))
        emotions.add(mutableListOf("relax", "6"))
        emotions.add(mutableListOf("angry", "7"))
        emotions.add(mutableListOf("fun", "8"))

        val emotionsBigRV = binding.root.findViewById<RecyclerView>(R.id.rv_emotions_big)
        emotionsBigRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        emotionsBigRV.adapter = HistoryEmotionBigRVAdapter(emotions.slice(0..2))

        val emotionsSmallRV = binding.root.findViewById<RecyclerView>(R.id.rv_emotions_small)
        emotionsSmallRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        emotionsSmallRV.adapter = HistoryEmotionSmallRVAdapter(emotions.slice(3..7))

        val wordCloudView = binding.root.findViewById<WordCloudView>(R.id.wordCloud)
        // temporary hard coding
        val wordCloudList = listOf<WordCloud>(
            WordCloud("test", 1),
            WordCloud("test", 2),
            WordCloud("test", 3),
            WordCloud("test", 4),
            WordCloud("test", 5),
            WordCloud("test", 6),
            WordCloud("test", 1),
            WordCloud("test", 2),
            WordCloud("test", 3),
            WordCloud("test", 4),
            WordCloud("test", 5),
            WordCloud("test", 6)
        )
        wordCloudView.setDataSet(wordCloudList)
        wordCloudView.notifyDataSetChanged()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                val intent = Intent(activity, SearchResultActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getEmotionsCount(binding: FragmentHistoryBinding) {
        val pref = context?.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val token =  pref?.getString("token", null)
        val header = mutableMapOf<String, String?>()
        header["Authorization"] = token

        service.requestGetEmotionsCount(header = header, type = "yearly", date = "2021-11-15").enqueue(
            onSuccess = {
                Log.d("HistoryFragment", it.toString())
            }
        )
    }
}