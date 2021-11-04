package com.recordlab.dailyscoop.ui.home.diary

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.data.TimeToString
import com.recordlab.dailyscoop.databinding.ActivityDiaryWriteBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DiaryWriteActivity: AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDiaryWriteBinding
    private lateinit var backgroundLayout: ConstraintLayout
    private lateinit var backgroundImage: ImageView
    private lateinit var contentText: EditText
    val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//            binding.ivWriteDiary.setImageURI(result.data?.data)
            Glide.with(backgroundLayout).load(result.data?.data).into(binding.ivWriteDiary)
    }
    private val DW_DEBUG_TAG = "DiaryWrite_DEBUG>>"

    var writeDate: Date? = null
    var diaryContent: String? = null
    var emotions: List<String>? = null
    var theme: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryWriteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (intent.hasExtra("date")) {

        }
        val image = findViewById<ImageView>(R.id.iv_write_diary)
        val toolbar = binding.tbDiaryWrite.toolbar
        toolbar.background.alpha = 0
        // 토큰은 이걸로 저장해서 보내주면 됨.
        // val token = getSharedPreferences("TOKEN", Context.MODE_PRIVATE)


        backgroundLayout = binding.clDiaryWrite
        backgroundImage = binding.ivBackground
        contentText = binding.etWriteDiary

        image.setOnClickListener(this)
        binding.chipPaperWhite.setOnClickListener(this)
        binding.chipPaperIvory.setOnClickListener(this)
        binding.chipPaperBlack.setOnClickListener(this)
        binding.chipWindow.setOnClickListener(this)
        binding.chipSkyDay.setOnClickListener(this)
        binding.chipSkyNight.setOnClickListener(this)

        setSupportActionBar(toolbar)
        val getActionBar = supportActionBar
        if (getActionBar != null) {
            getActionBar.setDisplayShowCustomEnabled(true)  // custom하기 위해
            getActionBar.setDisplayShowTitleEnabled(false)
            getActionBar.setDisplayHomeAsUpEnabled(true)
            getActionBar.setDisplayShowHomeEnabled(true)
            getActionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }

        //액티비티 시작 시 캘린더에서 클릭한 날짜로 넘겨받기.임의로 현재날짜 지정.

        val date = Date()
        binding.tbDiaryWrite.toolbarId.text = TimeToString().convert(date, 0)
        binding.tbDiaryWrite.toolbarId.setTextColor(Color.argb(0xCC, 0x30, 0x30, 0x30))

        Log.d(DW_DEBUG_TAG, "액티비티 생성")

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_write_diary -> {
                val intent = Intent(Intent.ACTION_PICK) // Intent.ACTION_PICK 부터 CONTENT_TYPE, image/*까지 설정하면 갤러리 열림.
                intent.type = MediaStore.Images.Media.CONTENT_TYPE
                intent.type = "image/*"
//                intent.action = Intent.ACTION_GET_CONTENT // 이 설정을 풀면 파일 폴더에서 사진 선택하는 방식.
                getContent.launch(intent)
            }
            R.id.chip_paper_white -> {
                Glide.with(backgroundLayout).load(R.drawable.paper_texture_1).into(backgroundImage)
                setTextColor(0)
            }
            R.id.chip_paper_ivory -> {
                Glide.with(backgroundLayout).load(R.drawable.paper_texture_2).into(backgroundImage)
                setTextColor(0)
            }
            R.id.chip_paper_black -> {
                Glide.with(backgroundLayout).load(R.drawable.annie_unsplash_black_paper).into(backgroundImage)
                Log.d(DW_DEBUG_TAG, "paper black chosen")
                setTextColor(1)
            }
            R.id.chip_window -> {
                Glide.with(backgroundLayout).load(R.drawable.kevin_unsplash_window).into(backgroundImage)
                setTextColor(1)
                Log.d(DW_DEBUG_TAG, "window chosen")
            }
            R.id.chip_sky_day -> {
                Glide.with(backgroundLayout).load(R.drawable.sky_with_moon).into(backgroundImage)
                setTextColor(0)
                Log.d(DW_DEBUG_TAG, "sky day chosen")
            }
            R.id.chip_sky_night -> {
                Glide.with(backgroundLayout).load(R.drawable.night_sky).into(backgroundImage)
                setTextColor(1)
                Log.d(DW_DEBUG_TAG, "sky night chosen")
            }
        }
    }
    fun setTextColor(mode: Int) {
        when(mode){
            0 -> {
                binding.tbDiaryWrite.toolbarId.setTextColor(Color.argb(0xCC, 0x30, 0x30, 0x30))
                binding.tvDiaryEmotion.setTextColor(Color.argb(0xCC, 0x30, 0x30, 0x30))
                contentText.setTextColor(Color.argb(0xCC, 0x30, 0x30, 0x30))
            }
            1 -> { // 밝은 텍스트
                binding.tbDiaryWrite.toolbarId.setTextColor(Color.argb(0xCC, 0xDB, 0xDB, 0xDB))
                binding.tvDiaryEmotion.setTextColor(Color.argb(0xCC, 0xDB, 0xDB, 0xDB))
                contentText.setTextColor(Color.argb(0xCC, 0xDB, 0xDB, 0xDB))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_save, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.action_save -> {
                // 여기에서 통신 붙이기
                val intent = Intent(this, DiaryActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}