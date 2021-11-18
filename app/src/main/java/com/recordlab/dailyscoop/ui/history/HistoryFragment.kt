package com.recordlab.dailyscoop.ui.history

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HistoryFragment : Fragment() {

    val emotionsBig = mutableListOf<MutableList<String>>()
    val emotionsSmall = mutableListOf<MutableList<String>>()

    lateinit var rvBigAdapter: HistoryEmotionBigRVAdapter
    lateinit var rvSmallAdapter: HistoryEmotionSmallRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var binding = FragmentHistoryBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true) // 앱 바 작업 버튼 추가하기

        getEmotionsCount(binding)

        val emotionsBigRV = binding.root.findViewById<RecyclerView>(R.id.rv_emotions_big)
        emotionsBigRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvBigAdapter = HistoryEmotionBigRVAdapter(emotionsBig)
        emotionsBigRV.adapter = rvBigAdapter

        val emotionsSmallRV = binding.root.findViewById<RecyclerView>(R.id.rv_emotions_small)
        emotionsSmallRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvSmallAdapter = HistoryEmotionSmallRVAdapter(emotionsSmall)
        emotionsSmallRV.adapter = rvSmallAdapter

        getDiaryCount(binding)

        val wordCloudView = binding.root.findViewById<WordCloudView>(R.id.wordCloud)
        // temporary hard coding
        val wordCloudList = listOf<WordCloud>(
            WordCloud("나는", 1),
            WordCloud("오늘도", 2),
            WordCloud("또는", 2),
            WordCloud("일기", 3),
            WordCloud("당한", 3),
            WordCloud("파도타기", 3),
            WordCloud("흠...", 3),
            WordCloud("좋았다", 3),
            WordCloud("싸피", 3),
            WordCloud("코딩공부", 3),
            WordCloud("가진다", 3)
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

        val date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        service.requestGetEmotionsCount(header = header, type = "monthly", date = date).enqueue(
            onSuccess = {
                when (it.code()) {
                    200 -> {
                        val emotionRes = it.body()?.emotions
                        val emotionsList = mutableListOf(
                            "angry" to emotionRes?.angry,
                            "anxious" to emotionRes?.anxious,
                            "bored" to emotionRes?.bored,
                            "excitement" to emotionRes?.excitement,
                            "fun" to emotionRes?.funn,
                            "happy" to emotionRes?.happy,
                            "joy" to emotionRes?.joy,
                            "nervous" to emotionRes?.nervous,
                            "relax" to emotionRes?.relax,
                            "sad" to emotionRes?.sad,
                            "sound" to emotionRes?.sound,
                            "tired" to emotionRes?.tired
                        )
                        emotionsList.sortByDescending {it.second}

                        var countSum: Int = 0
                        for (emotion in emotionsList) {
                            countSum += emotion.second!!
                        }

                        var calculatedIdx = 1
                        for (idx in 0..7) {
                            if (idx == 0 || emotionsList[idx].second!! != emotionsList[idx-1].second) {
                                calculatedIdx = idx + 1
                            }

                            if (idx <= 2) {
                                emotionsBig.add(mutableListOf(
                                    emotionsList[idx].first,
                                    calculatedIdx.toString(),
                                    ((emotionsList[idx].second!!.toDouble() / countSum)!! * 100).toInt().toString()
                                ))
                            } else {
                                emotionsSmall.add(mutableListOf(
                                    emotionsList[idx].first,
                                    calculatedIdx.toString()
                                ))
                            }
                        }
                        rvBigAdapter.notifyDataSetChanged()
                        rvSmallAdapter.notifyDataSetChanged()
                    }
                }
            }
        )
    }

    private fun getDiaryCount(binding: FragmentHistoryBinding) {
        val pref = context?.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        val token =  pref?.getString("token", null)
        val header = mutableMapOf<String, String?>()
        header["Authorization"] = token

        val date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        service.requestGetDiariesCount(header = header, type = "monthly", date = date).enqueue(
            onSuccess = {
                when (it.code()) {
                    200 -> {
                        val res = it.body()
                        var currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("M"))

                        val recordMonth = binding.root.findViewById<TextView>(R.id.recordMonth)
                        val countMonth = binding.root.findViewById<TextView>(R.id.countMonth)

                        recordMonth.text = "${currentMonth}월"
                        countMonth.text = "${res?.diaryCount} / ${res?.dayCount}"
                    }
                }
            }
        )
        service.requestGetDiariesCount(header = header, type = "yearly", date = date).enqueue(
            onSuccess = {
                when (it.code()) {
                    200 -> {
                        val res = it.body()
                        var currentYear = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"))

                        val recordYear = binding.root.findViewById<TextView>(R.id.recordYear)
                        val countYear = binding.root.findViewById<TextView>(R.id.countYear)

                        recordYear.text = "${currentYear}년"
                        countYear.text = "${res?.diaryCount} / ${res?.dayCount}"
                    }
                }
            }
        )
    }
}