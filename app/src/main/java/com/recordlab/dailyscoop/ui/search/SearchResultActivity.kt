package com.recordlab.dailyscoop.ui.search

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.data.DiaryData
import com.recordlab.dailyscoop.databinding.ActivitySearchResultBinding
import com.recordlab.dailyscoop.network.RetrofitClient.service
import com.recordlab.dailyscoop.network.enqueue


class SearchResultActivity : AppCompatActivity() {
    val DEBUG_SEARCH_TAG = ">>>SEARCH RESULT ACTIVITY>>>"
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var binding : ActivitySearchResultBinding
    private lateinit var searchAdapter: SearchAdapter
    val searchData = mutableListOf<DiaryData>()
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        sharedPref = getSharedPreferences("TOKEN", Context.MODE_PRIVATE)

        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        val tbSearch: Toolbar = findViewById(R.id.tb_search_result)
        setSupportActionBar(tbSearch)
        supportActionBar!!.setDisplayShowCustomEnabled(true)  // custom하기 위해
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_black_24)

        val searchRecyclerView : RecyclerView = findViewById(R.id.rv_list_search_result)
        searchRecyclerView.layoutManager = LinearLayoutManager(this)
        searchRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        searchViewModel.items.observe(this, Observer {
            searchRecyclerView.adapter = SearchAdapter(it)
        })

        val svSearch: SearchView = findViewById(R.id.sv_search_result)
        svSearch.onActionViewExpanded()
        svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
//                Log.d("asd", query.toString())
                loadData(query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                Log.d("qwe", "문자 변경 중")
                return false
            }
        })

        /*val inputMethodManager: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        */

        val searchView = binding.svSearchResult
        searchView.onFocusChangeListener = object: View.OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (!hasFocus && v != null) {
                    hideSoftKeyboard(v)
                }
            }
        }

    }

    /*fun softkeyboardFocus(view: View){
        // 선택한게 검색창이 아닌 경우
        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                hideSoftKeyboard(this@SearchResultActivity)
                false
            }
        }

        // 레이아웃 컨테이너가
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                softkeyboardFocus(innerView)
            }
        }
    }*/

    fun hideSoftKeyboard(view: View) {
        val inputMethodManager: InputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager.isAcceptingText) {
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadData(search: String) {
        val header = mutableMapOf<String, String?>()

        header["Content-type"] = "application/json; charset=UTF-8"
        header["Authorization"] = sharedPref.getString("token", "token")

        if (header["Authorization"] == "token") {

        } else {
            service.requestSearchDiaries(header = header, search = search, sort = -1).enqueue(
                onSuccess = {
                    when (it.code()) {
                        in 200..299 -> {
                            // 성공 처리
                            searchData.clear()
                            searchData.addAll(it.body()!!.data)
                            searchViewModel.items.postValue(searchData)
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