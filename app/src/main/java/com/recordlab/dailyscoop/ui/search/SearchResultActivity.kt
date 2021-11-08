package com.recordlab.dailyscoop.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.databinding.ActivitySearchResultBinding

class SearchResultActivity : AppCompatActivity() {
    val DEBUG_SEARCH_TAG = ">>>SEARCH RESULT ACTIVITY>>>"
    private lateinit var binding : ActivitySearchResultBinding
    private lateinit var searchAdapter: SearchAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        val tbSearch : Toolbar = findViewById(R.id.tb_search_result)
        setSupportActionBar(tbSearch)
        supportActionBar!!.setDisplayShowCustomEnabled(true)  // custom하기 위해
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_black_24)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                Log.d(DEBUG_SEARCH_TAG, "버튼 클릭됨.")
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}