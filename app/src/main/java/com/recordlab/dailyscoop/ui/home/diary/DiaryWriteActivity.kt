package com.recordlab.dailyscoop.ui.home.diary

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.databinding.ActivityDiaryWriteBinding

class DiaryWriteActivity: AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDiaryWriteBinding
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

        image.setOnClickListener(this)

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
        }
    }
}