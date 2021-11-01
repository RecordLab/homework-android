package com.recordlab.dailyscoop.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.data.Diary
import com.recordlab.dailyscoop.databinding.FragmentHomeBinding
import com.recordlab.dailyscoop.ui.dashboard.DialogYearMonth
import com.recordlab.dailyscoop.ui.home.diary.DiaryAdapter
import com.recordlab.dailyscoop.ui.home.widget.QuickDiaryFragment
import com.recordlab.dailyscoop.ui.home.widget.QuotationFragment
import com.recordlab.dailyscoop.ui.search.SearchActivity

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
    private lateinit var dialog: DialogYearMonth
    val diaryData = mutableListOf<Diary>()

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
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

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
        dialog = DialogYearMonth((this.activity)!!.applicationContext)

        btnMore.setOnClickListener {
            print("버튼 클릭")
            Log.d(">>>>>>>HOME FRAGMENT>>>>>>", "버튼 클릭 리스터 작동")
            val customDialog =
                DialogYearMonth((this.activity)!!.applicationContext)//context?.let { it1 -> DialogYearMonth(it1) }
            customDialog.setOnOKClickedListener {
                Log.d(">>>>>>button clicked", "버튼 선택됨!")
            }

            customDialog.init()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
    }

    private fun initDiary(view: View) {
        diaryAdapter = DiaryAdapter(view.context) { diaryData, View ->
            {

            }
        }
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
//                    widget1 = QuotationFragment()
                    Log.d(DEBUG_TAG, "여기로 들어오는지 확인하기.")
                    fragmentTransaction.remove(widget1)
                    fragmentTransaction.commit()
                }
                return widget1
            } else /*if (position == 1)*/ {
                Log.d(DEBUG_TAG, "뷰페이저 아이템 순서 1번인 경우")
                if (widget2.isAdded) {
//                    widget2 = QuickDiaryFragment()
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