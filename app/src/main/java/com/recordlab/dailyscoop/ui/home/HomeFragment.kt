package com.recordlab.dailyscoop.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.applikeysolutions.cosmocalendar.listeners.OnMonthChangeListener
import com.applikeysolutions.cosmocalendar.model.Month
import com.applikeysolutions.cosmocalendar.selection.SingleSelectionManager
import com.applikeysolutions.cosmocalendar.utils.SelectionType
import com.applikeysolutions.cosmocalendar.view.CalendarView
import com.recordlab.dailyscoop.MainActivity
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.data.DiaryData
import com.recordlab.dailyscoop.databinding.FragmentHomeBinding
import com.recordlab.dailyscoop.ui.SearchResultActivity
import com.recordlab.dailyscoop.ui.home.diary.DiaryActivity
import com.recordlab.dailyscoop.ui.home.diary.DiaryAdapter
import com.recordlab.dailyscoop.ui.home.diary.DiaryWriteActivity
import com.recordlab.dailyscoop.ui.home.widget.QuickDiaryFragment
import com.recordlab.dailyscoop.ui.home.widget.QuotationFragment
import java.sql.Timestamp
import java.text.SimpleDateFormat


private const val NUM_WIDGET = 2
private const val DEBUG_TAG = ">>>>>>HOME FRAGMENT >>>>>"

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var viewPager: ViewPager2
    private lateinit var widget1: QuotationFragment
    private lateinit var widget2: QuickDiaryFragment
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private lateinit var diaryAdapter: DiaryAdapter

    //    private lateinit var dialog: DialogYearMonth
    val diaryData = mutableListOf<DiaryData>()

    //    private val diaryListViewModel by viewModels<DiaryListViewModel> {
//        DiaryListViewModelFactory(this)
//    }
    override fun onCreate(savedInstanceState: Bundle?) { // 프래그먼트 최초 생성 시
        super.onCreate(savedInstanceState)
        widget1 = QuotationFragment()
        widget2 = QuickDiaryFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this)
                .get(HomeViewModel::class.java) //ViewModelProvider(activity.viewModelStore).get(HomeViewModel::class.java)//, ViewModelProvider.AndroidViewModelFactory()).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        val calendarView: CalendarView = binding.cvHome
//        var yearTextView = binding.tvHomeYear
//        var monthTextView = binding.tvHomeMonth

        init(view)

        setHasOptionsMenu(true) // 작업 버튼 추가하기. https://developer.android.com/training/appbar/actions

        viewPager = binding.vpMainWidget
        Log.d(
            ">>>>>>HOME FRAGMENT >>>>>",
            "view Id : " + view.id + "... " + viewPager.get(0) + "<<<<< 뷰 페이저 id"
        )

        var adapter = PagerAdpater(this.requireActivity())
        viewPager!!.adapter = adapter
        viewPager.setCurrentItem(0)

        val btnMore = binding.btnMore

        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            binding.textHome.text = it
            //textView.text = it
        })
        val btnById: TextView = binding.btnMore //root.findViewById(R.id.btn_more)
        Log.d(DEBUG_TAG, ">" + btnById.text + "<<  이게 원래 텍스트")
        btnMore.text = "더보기"
        Log.d(DEBUG_TAG, "> btnMore.text" + btnMore.text + " 변경 확인하기 " + btnById.text)

        calendarView.selectionType = SelectionType.SINGLE
        calendarView.calendarOrientation = 0
        calendarView.setOnMonthChangeListener(object : OnMonthChangeListener {

            override fun onMonthChanged(month: Month?) {
                Log.d(DEBUG_TAG, "Month: ${month.toString()}")
            }
        })

        calendarView.selectedDays
        calendarView.selectionManager(SingleSelectionManager {
            var result =
                SimpleDateFormat("yyyy - MM - dd").format(calendarView.selectedDays.get(0).calendar.time) + "\n"
            Toast.makeText(requireContext(), result, Toast.LENGTH_SHORT).show()
            Log.d(DEBUG_TAG, "호이!!!!! $result")
        })

        /*

        monthTextView.setOnClickListener {
            val customDialog =
                DialogYearMonth(requireContext())
            customDialog.init()
            customDialog.setOnClickListener(object : DialogYearMonth.DialogOKClickedListener{
                override fun onOKClicked(content: Boolean, year: Int, month: Int) {
                    yearTextView.text = "$year"
                    monthTextView.text = "${month}"
                }
            })
        }*/

        btnMore.setOnClickListener {
            Log.d(DEBUG_TAG, "더보기 클릭")
            val intent = Intent(activity, DiaryWriteActivity::class.java)
            startActivity(intent)

            if (activity != null) {
                Log.d(DEBUG_TAG, "activity is not null")
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
    }

    private fun init(view: View) {
        diaryAdapter = DiaryAdapter(view.context) { DiaryData, View ->
            val intent = Intent(activity as MainActivity, DiaryActivity::class.java)
            intent.putExtra("diaryIdx", DiaryData.id)
            startActivity(intent)
        }
        binding.rvHomeDiary.adapter = diaryAdapter
        loadData(view)
    }

    private fun initCalendar() {

    }

    private fun loadData(view: View) {
        diaryData.apply {
            for (i in 0..5) {
                add(
                    DiaryData(
                        id = 0,
                        writeDay = Timestamp.valueOf("2021-11-05"),
                        content = "배가 고픈 오늘!",
                        image = "",
                        emotion1 = 0,
                        emotion2 = 1,
                        emotion3 = 2,
                        theme = "sky(night)",
                    )
                )
            }
        }
        diaryAdapter.data = diaryData
        diaryAdapter.notifyDataSetChanged()
    }

    private inner class PagerAdpater(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        var fragmentManager: FragmentManager = fa.supportFragmentManager;
        var fragmentTransaction = fragmentManager.beginTransaction()

        override fun getItemCount(): Int {
            return NUM_WIDGET
        }

        override fun createFragment(position: Int): Fragment {
            if (position == 0) {
                Log.d(DEBUG_TAG, "뷰페이저 아이템 순서 0번인 경우")
                if (widget1.isAdded) {
                    Log.d(DEBUG_TAG, "여기로 들어오는지 확인하기.")
                    fragmentTransaction.remove(widget1)
                    fragmentTransaction.commit()
                }
                return widget1
            } else /*if (position == 1)*/ {
                Log.d(DEBUG_TAG, "뷰페이저 아이템 순서 1번인 경우")
                if (widget2.isAdded) {
                    fragmentTransaction.remove(widget2)
                    fragmentTransaction.commit()
                }
                return widget2
            }
//            else {
//                return widget1
//            }
        }


    }

    override fun onClick(v: View?) {
        when (v?.id) {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                val intent = Intent(context, SearchResultActivity::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)

                if (activity != null) {
                    Log.d(DEBUG_TAG, "activity is not null")
                }

                Log.d(DEBUG_TAG, "search button clicked!!")

                Log.d(DEBUG_TAG, "SearchActivity 전송,,,")
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

private fun CalendarView.selectionManager(singleSelectionManager: SingleSelectionManager) {

}
