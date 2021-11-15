package com.recordlab.dailyscoop.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.applikeysolutions.cosmocalendar.listeners.OnMonthChangeListener
import com.applikeysolutions.cosmocalendar.model.Month
import com.applikeysolutions.cosmocalendar.selection.SingleSelectionManager
import com.applikeysolutions.cosmocalendar.settings.appearance.ConnectedDayIconPosition
import com.applikeysolutions.cosmocalendar.settings.lists.connected_days.ConnectedDays
import com.applikeysolutions.cosmocalendar.utils.SelectionType
import com.applikeysolutions.cosmocalendar.view.CalendarView
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.data.DiaryData
import com.recordlab.dailyscoop.data.TimeToString
import com.recordlab.dailyscoop.databinding.FragmentHomeBinding
import com.recordlab.dailyscoop.network.RetrofitClient
import com.recordlab.dailyscoop.network.enqueue
import com.recordlab.dailyscoop.ui.diary.DiaryDetailActivity
import com.recordlab.dailyscoop.ui.home.diary.DiaryAdapter
import com.recordlab.dailyscoop.ui.home.diary.DiaryWriteActivity
import com.recordlab.dailyscoop.ui.home.widget.QuickDiaryFragment
import com.recordlab.dailyscoop.ui.home.widget.QuotationFragment
import com.recordlab.dailyscoop.ui.search.SearchResultActivity
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*


private const val NUM_WIDGET = 2
private const val DEBUG_TAG = ">>>>>>HOME FRAGMENT >>>>>"

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var viewPager: ViewPager2
    private lateinit var widget1: QuotationFragment
    private lateinit var widget2: QuickDiaryFragment
    private var _binding: FragmentHomeBinding? = null
    private lateinit var selectedDate: Calendar
    private val service = RetrofitClient.service
    private lateinit var sharedPref: SharedPreferences

    private val binding get() = _binding!!

    private lateinit var diaryAdapter: DiaryAdapter

    val diaryData = mutableListOf<DiaryData>()
    private var days = mutableSetOf<Long>()

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
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
           // ViewModelProviders.of(this)[HomeViewModel::class.java] //ViewModelProvider(activity.viewModelStore).get(HomeViewModel::class.java)//, ViewModelProvider.AndroidViewModelFactory()).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        val calendarView: CalendarView = binding.cvHome
//        val fab: View = binding.fabDiary
        sharedPref = requireActivity().getSharedPreferences("TOKEN", Context.MODE_PRIVATE)
        /*val token: String? = sharedPref.getString("token", "token")
        if (token == "token") {
            with(sharedPref.edit()) {
                putString(
                    "token",
                    "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6ImhhaWxleWhpMTRAZ21haWwuY29tIiwiZXhwIjoxNjM5NDc3NzQzfQ.uHJxXzmGAnlm3CgFES-Hpd2nvIkDCQ9TmhEBZXYqJa8"
                )
                commit()
            }
        }else { // 로그인 붙이고 이 부분 지우기.
            with(sharedPref.edit()) {
                putString(
                    "token",
                    "Bearer $token"
                )
                apply()
                commit()
            }
        }*/

        init(view)

        setHasOptionsMenu(true) // 작업 버튼 추가하기. https://developer.android.com/training/appbar/actions

        viewPager = binding.vpMainWidget
        Log.d(
            ">>>>>>HOME FRAGMENT >>>>>",
            "view Id : " + view.id + "... " + viewPager.get(0) + "<<<<< 뷰 페이저 id"
        )

        // 뷰 페이저 어댑터
        var adapter = PagerAdpater(this.requireActivity())
        viewPager!!.adapter = adapter
        viewPager.setCurrentItem(0)

        val btnMore = binding.btnMore

        // diaryAdapter 지정하기
        /*diaryAdapter = DiaryAdapter()
        binding.rvHomeDiary.adapter = diaryAdapter
        // observer
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            binding.textHome.text = it
            //textView.text = it
        })
        homeViewModel.diaryData.observe(viewLifecycleOwner) {
            diaryAdapter.updateDiary(it)
//            binding.rvHomeDiary.adapter. = DiaryAdapter(it)
        }*/

        val btnById: TextView = binding.btnMore //root.findViewById(R.id.btn_more)
        Log.d(DEBUG_TAG, ">" + btnById.text + "<<  이게 원래 텍스트")
        btnMore.text = "더보기"
        Log.d(DEBUG_TAG, "> btnMore.text" + btnMore.text + " 변경 확인하기 " + btnById.text)

        calendarView.selectionType = SelectionType.SINGLE
        calendarView.calendarOrientation = 0
        calendarView.setOnMonthChangeListener(object : OnMonthChangeListener {

            override fun onMonthChanged(month: Month?) {
                Log.d(DEBUG_TAG, "Month: ${month.toString()}")
                // connected 일기 날짜
            }
        })

        calendarView.selectionManager = SingleSelectionManager {
            // if selected day has its own diary record -> send to diary detail activity
            Log.d(DEBUG_TAG, "${calendarView.selectedDays[0].calendar.get(Calendar.YEAR)}-" +
                    "${calendarView.selectedDays[0].calendar.get(Calendar.MONTH) + 1}-" +
                    "${calendarView.selectedDays[0].calendar.get(Calendar.DAY_OF_MONTH)}"
            )
//            Log.d(DEBUG_TAG, ">> 시간은 이렇게 표시된다. ${TimeToString().convert(calendarView.selectedDays[0].calendar.time, 2)}")
            val chosenDate = TimeToString().convert(calendarView.selectedDays[0].calendar.time, 3)
            Log.d("날짜 클릭 ", "$chosenDate ")
            //set에 있는지 확인하고 꺼내 쓰기.
            Log.d(DEBUG_TAG, "선택한 날짜 as 시간 :${calendarView.selectedDays[0].calendar.timeInMillis} 현재 날짜 AS Long ${System.currentTimeMillis()}")
            // 미래 시점이면 아직 날짜가 되지 않았다고 띄워주기.
            if (calendarView.selectedDays[0].calendar.timeInMillis > System.currentTimeMillis()){

                Toast.makeText(this.context, "...", Toast.LENGTH_SHORT).show()
            }else {
                /*for (item in diaryData){
                    days.add(Date(item.date.time).time) // days 안에 들어있는 것 timestamp -> long
                    Log.d(DEBUG_TAG, "${Date(item.date.time).time}")
                }*/
                // calendar 안에 들어있는 것 : calendar
                Log.d("시간 비교" , "${calendarView.selectedDays[0].calendar.time.time} " +
                        "캘린더 클릭으로 가져오는 timeinMillis ${calendarView.selectedDays[0].calendar.timeInMillis}")
                if (days.contains(calendarView.selectedDays[0].calendar.time.time)){ // 일기 있는 경우
                    val intent = Intent(activity, DiaryDetailActivity::class.java)
                    intent.putExtra("diaryDate", chosenDate)
                    startActivity(intent)
                } else { // 없는 경우
                    // 다이얼로그로 먼저 물어보기.
                    val intent = Intent(activity, DiaryWriteActivity::class.java)
                    intent.putExtra("date", chosenDate)
                    startActivity(intent)

                }
            }

        }

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
        diaryAdapter = DiaryAdapter()
        binding.rvHomeDiary.adapter = diaryAdapter
        // observer
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            binding.textHome.text = it
            //textView.text = it
        })
        homeViewModel.diaryData.observe(viewLifecycleOwner) {
            diaryAdapter.updateDiary(it)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun init(view: View) {
        loadData(view)
    }


    private fun loadData(view: View) {
        val header = mutableMapOf<String, String?>()
        // 헤더설정하기
        header["Content-type"] = "application/json; charset=UTF-8"
        header["Authorization"] = sharedPref.getString("token", "token")

        if (header["Authorization"] == "token") { // 로그인 안 한 상태..?
            diaryData.apply {
                add(
                    DiaryData(
                        date = Timestamp.valueOf("2021-11-08 09:52:11"),
                        content = "로그인 하셈!",
                        image = "/flower_unsplash.png",
                        emotions = mutableListOf("happy", "sound"),
                        theme = "sky(night)",
                    )
                )
            }
        } else { // 로그인 한 상태.
            service.requestGetDiaries(header = header).enqueue(
                onSuccess = {
                    when (it.code()) {
                        in 200..299 -> {
                            Log.d("통신 성공", it.body()!!.data[0].content)
                            diaryData.clear()
                            diaryData.addAll(it.body()!!.data)
                            homeViewModel.diaryData.postValue(diaryData)
                            diaryAdapter.notifyDataSetChanged()

                            // 달력에 일기 작성날짜 mark하기.
                            days.clear()
                            val sdf = SimpleDateFormat("MM-dd-yyyy")
                            for (item in diaryData){
                                days.add(Date(item.date.time).time)
                                Log.d(DEBUG_TAG, "${Date(item.date.time).time}")
                            }

                            val textColor: Int = R.color.veryBerry
                            val selectedTextColor: Int = R.color.baraRed
                            val disabledTextColor: Int = R.color.twinkleBlue
                            val connectedDays = ConnectedDays(days, textColor, selectedTextColor, disabledTextColor)

                            //Add Connect days to calendar
                            binding.cvHome.addConnectedDays(connectedDays)

                            binding.cvHome.connectedDayIconRes = R.drawable.ic_baseline_flag_coral_12
                            binding.cvHome.connectedDayIconPosition = ConnectedDayIconPosition.TOP // TOP & BOTTOM

                            binding.cvHome.update()
                        }
                        400 -> {

                        }
                        in 500..599 -> {
                            // 서버 에러.
                        }
                    }
                },
                onError = {
                    Toast.makeText(requireContext(), "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                },
                onFail = {

                }
            )

        }
        /*diaryData.apply {
            for (i in 0..5) {
                add(
                    DiaryData(
                        id = 0,
                        writeDay = Timestamp.valueOf("2021-11-05 09:52:11"),
                        content = "배가 고픈 오늘!",
                        image = "/flower_unsplash.png",
                        emotion1 = 0,
                        emotion2 = 1,
                        emotion3 = 2,
                        theme = "sky(night)",
                    )
                )
            }
        }
        diaryAdapter.data = diaryData
        diaryAdapter.notifyDataSetChanged()*/


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
                activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)

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

