package com.recordlab.dailyscoop.ui.dashboard

import android.content.Intent
import android.view.*
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.data.DiaryData
import com.recordlab.dailyscoop.databinding.FragmentDashboardBinding
import com.recordlab.dailyscoop.databinding.FragmentHomeBinding
import com.recordlab.dailyscoop.network.RetrofitClient
import com.recordlab.dailyscoop.network.RetrofitClient.service
import com.recordlab.dailyscoop.network.enqueue
import com.recordlab.dailyscoop.ui.search.SearchResultActivity
import java.sql.Timestamp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.security.auth.callback.Callback

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    val diaryData = mutableListOf<DiaryData>()
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root = binding.root
//        val textView: TextView = root.findViewById(R.id.text_dashboard)
//        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        setHasOptionsMenu(true) // 앱 바 작업 버튼 추가하기.

        sharedPref = requireActivity().getSharedPreferences("TOKEN", Context.MODE_PRIVATE)

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

        loadData(root, "$nowYear-$nowMonth-01")

        // 모아보기 날짜 변경
        val datePickBtn = root.findViewById<View>(R.id.iv_nav_gallery_calender)
        datePickBtn.setOnClickListener {

            val dialog = AlertDialog.Builder(context).create()
            val edialog : LayoutInflater = LayoutInflater.from(context)
            val mView : View = edialog.inflate(R.layout.custom_dialog_datepicker, null)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

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
            year.minValue = 2021
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

                loadData(root, "$nowYear-$nowMonth-01")

                // 날짜 변경 후 데이터 새로고침
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

    private fun loadData(view: View, date: String) {
        val header = mutableMapOf<String, String?>()

        header["Content-type"] = "application/json; charset=UTF-8"
        header["Authorization"] = sharedPref.getString("token", "token")

        if (header["Authorization"] == "token") {

        } else {
            service.requestGetCalendar(header = header, date = date, type = "monthly").enqueue(
                onSuccess = {
                    when (it.code()) {
                        in 200..299 -> {
                            // 성공 처리
//                            Log.d("통신 성공", it.body()!!.data[0].content)
                            diaryData.clear()
                            diaryData.addAll(it.body()!!.data)
                            dashboardViewModel.items.postValue(diaryData)
                        }
                        400 -> {
                            // 400 처리
                        }
                        else -> {

                        }
                    }
                },
                onError = {
                },
                onFail = {
                }

            )
        }

    }

}