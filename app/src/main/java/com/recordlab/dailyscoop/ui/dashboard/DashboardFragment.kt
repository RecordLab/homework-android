package com.recordlab.dailyscoop.ui.dashboard

import android.content.Intent
import android.view.*
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import androidx.compose.ui.Alignment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.ui.SearchResultActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        setHasOptionsMenu(true) // 앱 바 작업 버튼 추가하기.

        // RecyclerView
        val listRecyclerView = root.findViewById<RecyclerView>(R.id.rv_dashboard_list)
        listRecyclerView.layoutManager = LinearLayoutManager(context)
        listRecyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

        val gridRecyclerView = root.findViewById<RecyclerView>(R.id.rv_dashboard_grid)
        gridRecyclerView.layoutManager = GridLayoutManager(context, 4)
        
        // observer
        dashboardViewModel.items.observe(viewLifecycleOwner, Observer {
            listRecyclerView.adapter = DashboardListAdapter(it)
            gridRecyclerView.adapter = DashboardGridAdapter(it)
        })
        
        // 레이아웃 변경
        val layoutBtn = root.findViewById<View>(R.id.iv_nav_gallery_grid)
        layoutBtn.setOnClickListener {
            if (it.isSelected) {
                listRecyclerView.visibility = View.INVISIBLE
                gridRecyclerView.visibility = View.VISIBLE
            } else {
                listRecyclerView.visibility = View.VISIBLE
                gridRecyclerView.visibility = View.INVISIBLE
            }
            it.isSelected = !it.isSelected
        }

        // 최초 선택 날짜
        val selectedDate : TextView = root.findViewById(R.id.tv_nav_gallery_date)
        var nowYear = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"))
        var nowMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("MM"))
        selectedDate.text = "$nowYear.$nowMonth"

        // 모아보기 날짜 변경
        val datePickBtn = root.findViewById<View>(R.id.iv_nav_gallery_calender)
        datePickBtn.setOnClickListener {

            val dialog = AlertDialog.Builder(context).create()
            val edialog : LayoutInflater = LayoutInflater.from(context)
            val mView : View = edialog.inflate(R.layout.custom_dialog_datepicker, null)

            val year : com.shawnlin.numberpicker.NumberPicker = mView.findViewById(R.id.np_datepicker_year_picker)
            val month : com.shawnlin.numberpicker.NumberPicker = mView.findViewById(R.id.np_datepicker_month_picker)
            val cancel : Button = mView.findViewById(R.id.btn_datepicker_dlg_cancel)
            val save : Button = mView.findViewById(R.id.btn_datepicker_dlg_ok)

            // 순환 안되게 막기
            year.wrapSelectorWheel = false
            month.wrapSelectorWheel = false

            // editText 설정 해제
            year.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            month.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

            // 최솟값, 최댓값 설정
            year.minValue = 2015
            year.maxValue = 2022
            month.minValue = 1
            month.maxValue = 12

            // 초기값 설정
            year.value = nowYear.toInt()
            month.value = nowMonth.toInt()

            // 취소 버튼 클릭시
            cancel.setOnClickListener {
                dialog.dismiss()
                dialog.cancel()
            }

            // 완료 버튼 클릭시
            save.setOnClickListener {
                nowYear = year.value.toString()
                nowMonth = if (month.value < 10) "0${month.value}" else month.value.toString()
                selectedDate.text = "$nowYear.$nowMonth"

                // 날짜 변경 후 데이터 새로고침
                var temp = ArrayList<DashboardItem>()
                temp.apply {
                    add(DashboardItem("1월", "123", R.drawable.happy))
                    add(DashboardItem("2월", "1234", R.drawable.bored))
                    add(DashboardItem("1월", "123", R.drawable.happy))
                    add(DashboardItem("2월", "1234", R.drawable.bored))
                }
                dashboardViewModel.items.postValue(temp)



                dialog.dismiss()
                dialog.cancel()
            }

            dialog.setView(mView)
            dialog.create()
            dialog.show()
        }

        return root
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
}