package com.recordlab.dailyscoop.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.databinding.FragmentHomeBinding
import com.recordlab.dailyscoop.ui.home.diary.DiaryAdapter
import com.recordlab.dailyscoop.ui.home.diary.DiaryListViewModel
import com.recordlab.dailyscoop.ui.home.diary.DiaryListViewModelFactory
import com.recordlab.dailyscoop.ui.home.widget.QuickDiaryFragment
import com.recordlab.dailyscoop.ui.home.widget.QuotationFragment

private const val NUM_WIDGET = 3
class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var viewPager: ViewPager2
    private lateinit var quotationFragment: QuotationFragment
    private lateinit var binding: FragmentHomeBinding
//    private lateinit var
    private val diaryListViewModel by viewModels<DiaryListViewModel> {
        DiaryListViewModelFactory(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        val view = binding.root
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val diaryAdapter = DiaryAdapter {}
        return root
    }

    private inner class PagerAdpater(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int {
            return NUM_WIDGET
        }

        override fun createFragment(position: Int): Fragment {
            if (position == 0) {
               return QuotationFragment()
            } else if (position == 1) {
                return QuickDiaryFragment()
            }
//            else {
//
//            }
            return QuotationFragment()
        }


    }
}