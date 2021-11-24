package com.recordlab.dailyscoop.ui.profile.bookmark

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.databinding.ActivityProfileBookmarkBinding
import com.recordlab.dailyscoop.network.RetrofitClient.service
import com.recordlab.dailyscoop.network.enqueue
import com.recordlab.dailyscoop.network.response.Favorite

class ProfileBookmarkActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private var quotations : List<Favorite>? = null
    private lateinit var binding: ActivityProfileBookmarkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_bookmark)

        sharedPref = getSharedPreferences("TOKEN", Context.MODE_PRIVATE)

        // 뒤로가기 버튼 클릭
        val backBtnClicked = findViewById<ImageView>(R.id.imageView10)
        backBtnClicked.setOnClickListener{
            finish()
        }

//        val bookmarkRecyclerView : RecyclerView = findViewById(R.id.rv_bookmark_list)
//        bookmarkRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        val actionBar: MaterialToolbar = findViewById(R.id.mtb_bookmark)
        setSupportActionBar(actionBar)
        val getActionBar = supportActionBar
        if (getActionBar != null) {
            getActionBar.setDisplayShowCustomEnabled(true)  // custom하기 위해
            getActionBar.setDisplayShowTitleEnabled(false)
            getActionBar.setDisplayHomeAsUpEnabled(true)
            getActionBar.setDisplayShowHomeEnabled(true)
            getActionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_black_24)
            getActionBar.setTitle(R.string.title_bookmark)
        }
        getQuotation()
    }

    private fun getQuotation() {
        val header = mutableMapOf<String, String?>()

        header["Content-type"] = "application/json; charset=UTF-8"
        header["Authorization"] = sharedPref.getString("token", "token")

        service.requestGetQuotation(header = header).enqueue(
            onSuccess = {
                quotations =  it.body()!!.favorite!!
                val bookmarkRecyclerView : RecyclerView = findViewById(R.id.rv_bookmark_list)
                bookmarkRecyclerView.adapter = ProfileBookmarkAdapter(quotations!!)
            },
            onFail = {},
            onError = {}
        )
    }
}