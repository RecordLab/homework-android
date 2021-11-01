package com.recordlab.dailyscoop.ui.history

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.ui.search.SearchActivity

class HistoryFragment : Fragment() {

    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        historyViewModel =
            ViewModelProviders.of(this).get(HistoryViewModel::class.java)
        print("히스토리 프래그먼트 설정 접속")
        val root = inflater.inflate(R.layout.fragment_history, container, false)
        val textView: TextView = root.findViewById(R.id.text_history)
        historyViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        setHasOptionsMenu(true) // 앱 바 작업 버튼 추가하기

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.action_search -> {
                val intent = Intent(context, SearchActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}