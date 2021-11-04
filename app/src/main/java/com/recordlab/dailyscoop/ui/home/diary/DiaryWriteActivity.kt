package com.recordlab.dailyscoop.ui.home.diary

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
import com.recordlab.dailyscoop.databinding.ActivityDiaryWriteBinding

class DiaryWriteActivity: AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDiaryWriteBinding
    private lateinit var backgroundLayout: ConstraintLayout
    private lateinit var backgroundImage: ImageView
    private lateinit var contentText: EditText
    val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            binding.ivWriteDiary.setImageURI(result.data?.data)
    }
    private val DW_DEBUG_TAG = "DiaryWrite_DEBUG>>"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryWriteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val image = findViewById<ImageView>(R.id.iv_write_diary)
        val toolbar = binding.tbDiaryWrite.toolbar
        toolbar.background.alpha = 0
        val background = binding.ivBackground
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
                contentText.setTextColor(Color.argb(0xCC, 0x30, 0x30, 0x30))
            }
            R.id.chip_paper_ivory -> {
                Glide.with(backgroundLayout).load(R.drawable.paper_texture_2).into(backgroundImage)
                contentText.setTextColor(Color.argb(0xCC, 0x30, 0x30, 0x30))
            }
            R.id.chip_paper_black -> {
                Log.d(DW_DEBUG_TAG, "paper black chosen")
                contentText.setTextColor(Color.argb(0xCC, 0xDB, 0xDB, 0xDB))
            }
            R.id.chip_window -> {
                Log.d(DW_DEBUG_TAG, "window chosen")
            }
            R.id.chip_sky_day -> {
                Log.d(DW_DEBUG_TAG, "sky day chosen")
            }
            R.id.chip_sky_night -> {
                Glide.with(backgroundLayout).load(R.drawable.night_sky).into(backgroundImage)
                Log.d(DW_DEBUG_TAG, "sky night chosen")
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
                // 여기에
                val intent = Intent(this, DiaryActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}